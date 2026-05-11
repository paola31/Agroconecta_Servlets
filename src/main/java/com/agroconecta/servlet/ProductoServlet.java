package com.agroconecta.servlet;

import com.agroconecta.dao.ProductoDAO;
import com.agroconecta.model.Producto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "ProductoServlet", urlPatterns = {"/productos"})
public class ProductoServlet extends HttpServlet {

    private final ProductoDAO productoDAO = new ProductoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("nuevo".equals(action)) {
            mostrarFormularioNuevo(request, response);
            return;
        }

        if ("editar".equals(action)) {
            mostrarFormularioEditar(request, response);
            return;
        }

        listarProductos(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("crear".equals(action)) {
                crearProducto(request, response);
                return;
            }

            if ("actualizar".equals(action)) {
                actualizarProducto(request, response);
                return;
            }

            if ("desactivar".equals(action)) {
                desactivarProducto(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/productos");
        } catch (IllegalArgumentException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("producto", leerProductoDesdeFormulario(request));
            request.setAttribute("modo", "actualizar".equals(action) ? "editar" : "crear");
            request.getRequestDispatcher("/WEB-INF/views/formulario-producto.jsp").forward(request, response);
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Producto> productos = productoDAO.listarActivos();
        request.setAttribute("productos", productos);
        request.getRequestDispatcher("/WEB-INF/views/productos.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("modo", "crear");
        request.setAttribute("producto", new Producto());
        request.getRequestDispatcher("/WEB-INF/views/formulario-producto.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = parseLong(request.getParameter("id"), "El id del producto es obligatorio");
        Producto producto = productoDAO.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        request.setAttribute("modo", "editar");
        request.setAttribute("producto", producto);
        request.getRequestDispatcher("/WEB-INF/views/formulario-producto.jsp").forward(request, response);
    }

    private void crearProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Producto producto = leerProductoDesdeFormulario(request);
        validarProducto(producto);
        productoDAO.crear(producto);
        response.sendRedirect(request.getContextPath() + "/productos");
    }

    private void actualizarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Producto producto = leerProductoDesdeFormulario(request);
        producto.setId(parseLong(request.getParameter("id"), "El id del producto es obligatorio"));
        validarProducto(producto);
        productoDAO.actualizar(producto);
        response.sendRedirect(request.getContextPath() + "/productos");
    }

    private void desactivarProducto(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = parseLong(request.getParameter("id"), "El id del producto es obligatorio");
        productoDAO.desactivar(id);
        response.sendRedirect(request.getContextPath() + "/productos");
    }

    private Producto leerProductoDesdeFormulario(HttpServletRequest request) {
        Producto producto = new Producto();
        producto.setNombre(request.getParameter("nombre"));
        producto.setDescripcion(request.getParameter("descripcion"));
        producto.setUnidadMedida(request.getParameter("unidadMedida"));
        producto.setImagenUrl(request.getParameter("imagenUrl"));

        String precio = request.getParameter("precioUnitario");
        if (precio != null && !precio.isBlank()) {
            producto.setPrecioUnitario(new BigDecimal(precio));
        }

        return producto;
    }

    private void validarProducto(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }

        if (producto.getUnidadMedida() == null || producto.getUnidadMedida().isBlank()) {
            throw new IllegalArgumentException("La unidad de medida es obligatoria");
        }

        if (producto.getPrecioUnitario() == null || producto.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio unitario debe ser mayor o igual a cero");
        }
    }

    private long parseLong(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return Long.parseLong(value);
    }
}
