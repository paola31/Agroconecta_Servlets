package com.agroconecta.dao;

import com.agroconecta.config.DatabaseConnection;
import com.agroconecta.model.Producto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAO {

    public List<Producto> listarActivos() {
        String sql = """
                SELECT id, nombre, descripcion, unidad_medida, precio_unitario, imagen_url,
                       creado_en, actualizado_en, activo
                FROM productos
                WHERE activo = true
                ORDER BY nombre ASC
                """;

        List<Producto> productos = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                productos.add(mapearProducto(resultSet));
            }

            return productos;
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible listar los productos", exception);
        }
    }

    public Optional<Producto> buscarPorId(long id) {
        String sql = """
                SELECT id, nombre, descripcion, unidad_medida, precio_unitario, imagen_url,
                       creado_en, actualizado_en, activo
                FROM productos
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearProducto(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible consultar el producto", exception);
        }
    }

    public void crear(Producto producto) {
        String sql = """
                INSERT INTO productos (nombre, descripcion, unidad_medida, precio_unitario, imagen_url, activo)
                VALUES (?, ?, ?, ?, ?, true)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarParametrosProducto(statement, producto);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    producto.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible crear el producto", exception);
        }
    }

    public void actualizar(Producto producto) {
        String sql = """
                UPDATE productos
                SET nombre = ?, descripcion = ?, unidad_medida = ?, precio_unitario = ?, imagen_url = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            asignarParametrosProducto(statement, producto);
            statement.setLong(6, producto.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible actualizar el producto", exception);
        }
    }

    public void desactivar(long id) {
        String sql = "UPDATE productos SET activo = false WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible desactivar el producto", exception);
        }
    }

    private void asignarParametrosProducto(PreparedStatement statement, Producto producto) throws SQLException {
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setString(3, producto.getUnidadMedida());
        statement.setBigDecimal(4, producto.getPrecioUnitario());
        statement.setString(5, producto.getImagenUrl());
    }

    private Producto mapearProducto(ResultSet resultSet) throws SQLException {
        Producto producto = new Producto();
        producto.setId(resultSet.getLong("id"));
        producto.setNombre(resultSet.getString("nombre"));
        producto.setDescripcion(resultSet.getString("descripcion"));
        producto.setUnidadMedida(resultSet.getString("unidad_medida"));
        producto.setPrecioUnitario(resultSet.getBigDecimal("precio_unitario"));
        producto.setImagenUrl(resultSet.getString("imagen_url"));
        producto.setActivo(resultSet.getBoolean("activo"));
        producto.setCreadoEn(toLocalDateTime(resultSet.getTimestamp("creado_en")));
        producto.setActualizadoEn(toLocalDateTime(resultSet.getTimestamp("actualizado_en")));
        return producto;
    }

    private java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
