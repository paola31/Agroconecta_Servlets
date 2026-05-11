package com.agroconecta.servlet;

import com.agroconecta.dao.StockDAO;
import com.agroconecta.model.Stock;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "StockServlet", urlPatterns = {"/stock"})
public class StockServlet extends HttpServlet {

    private final StockDAO stockDAO = new StockDAO();

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

        listarStock(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        try {
            if ("crear".equals(action)) {
                crearStock(request, response);
                return;
            }

            if ("actualizar".equals(action)) {
                actualizarStock(request, response);
                return;
            }

            if ("eliminar".equals(action)) {
                eliminarStock(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/stock");
        } catch (IllegalArgumentException | IllegalStateException exception) {
            request.setAttribute("error", exception.getMessage());
            request.setAttribute("stock", leerStockDesdeFormulario(request));
            request.setAttribute("modo", "actualizar".equals(action) ? "editar" : "crear");
            cargarOpcionesFormulario(request);
            request.getRequestDispatcher("/WEB-INF/views/formulario-stock.jsp").forward(request, response);
        }
    }

    private void listarStock(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Stock> registros = stockDAO.listar();
        request.setAttribute("registrosStock", registros);
        request.getRequestDispatcher("/WEB-INF/views/stock.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("modo", "crear");
        request.setAttribute("stock", new Stock());
        cargarOpcionesFormulario(request);
        request.getRequestDispatcher("/WEB-INF/views/formulario-stock.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long id = parseLong(request.getParameter("id"), "El id del stock es obligatorio");
        Stock stock = stockDAO.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro de stock no encontrado"));

        request.setAttribute("modo", "editar");
        request.setAttribute("stock", stock);
        cargarOpcionesFormulario(request);
        request.getRequestDispatcher("/WEB-INF/views/formulario-stock.jsp").forward(request, response);
    }

    private void crearStock(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Stock stock = leerStockDesdeFormulario(request);
        validarStock(stock);
        stockDAO.crear(stock);
        response.sendRedirect(request.getContextPath() + "/stock");
    }

    private void actualizarStock(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Stock stock = leerStockDesdeFormulario(request);
        stock.setId(parseLong(request.getParameter("id"), "El id del stock es obligatorio"));
        validarStock(stock);
        stockDAO.actualizar(stock);
        response.sendRedirect(request.getContextPath() + "/stock");
    }

    private void eliminarStock(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = parseLong(request.getParameter("id"), "El id del stock es obligatorio");
        stockDAO.eliminar(id);
        response.sendRedirect(request.getContextPath() + "/stock");
    }

    private void cargarOpcionesFormulario(HttpServletRequest request) {
        request.setAttribute("usuarios", stockDAO.listarUsuariosActivos());
        request.setAttribute("productos", stockDAO.listarProductosActivos());
    }

    private Stock leerStockDesdeFormulario(HttpServletRequest request) {
        Stock stock = new Stock();
        stock.setUsuarioId(parseLongOpcional(request.getParameter("usuarioId")));
        stock.setProductoId(parseLongOpcional(request.getParameter("productoId")));

        String cantidad = request.getParameter("cantidad");
        if (cantidad != null && !cantidad.isBlank()) {
            stock.setCantidad(new BigDecimal(cantidad));
        }

        return stock;
    }

    private void validarStock(Stock stock) {
        if (stock.getUsuarioId() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (stock.getProductoId() == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (stock.getCantidad() == null || stock.getCantidad().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor o igual a cero");
        }
    }

    private Long parseLongOpcional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Long.parseLong(value);
    }

    private long parseLong(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }

        return Long.parseLong(value);
    }
}
