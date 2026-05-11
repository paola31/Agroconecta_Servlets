package com.agroconecta.dao;

import com.agroconecta.config.DatabaseConnection;
import com.agroconecta.model.OpcionFormulario;
import com.agroconecta.model.Stock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StockDAO {

    public List<Stock> listar() {
        String sql = """
                SELECT s.id, s.usuario_id, u.nombre AS nombre_usuario,
                       s.producto_id, p.nombre AS nombre_producto,
                       s.cantidad, s.creado_en, s.actualizado_en
                FROM stock s
                INNER JOIN usuarios u ON s.usuario_id = u.id
                INNER JOIN productos p ON s.producto_id = p.id
                ORDER BY u.nombre ASC, p.nombre ASC
                """;

        List<Stock> registros = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                registros.add(mapearStock(resultSet));
            }

            return registros;
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible listar el stock", exception);
        }
    }

    public Optional<Stock> buscarPorId(long id) {
        String sql = """
                SELECT s.id, s.usuario_id, u.nombre AS nombre_usuario,
                       s.producto_id, p.nombre AS nombre_producto,
                       s.cantidad, s.creado_en, s.actualizado_en
                FROM stock s
                INNER JOIN usuarios u ON s.usuario_id = u.id
                INNER JOIN productos p ON s.producto_id = p.id
                WHERE s.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapearStock(resultSet));
                }
            }

            return Optional.empty();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible consultar el stock", exception);
        }
    }

    public void crear(Stock stock) {
        String sql = """
                INSERT INTO stock (usuario_id, producto_id, cantidad)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            asignarParametrosStock(statement, stock);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible crear el registro de stock", exception);
        }
    }

    public void actualizar(Stock stock) {
        String sql = """
                UPDATE stock
                SET usuario_id = ?, producto_id = ?, cantidad = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            asignarParametrosStock(statement, stock);
            statement.setLong(4, stock.getId());
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible actualizar el registro de stock", exception);
        }
    }

    public void eliminar(long id) {
        String sql = "DELETE FROM stock WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException exception) {
            throw new IllegalStateException("No fue posible eliminar el registro de stock", exception);
        }
    }

    public List<OpcionFormulario> listarUsuariosActivos() {
        String sql = """
                SELECT id, nombre
                FROM usuarios
                WHERE estado = 'activo'
                ORDER BY nombre ASC
                """;

        return listarOpciones(sql, "No fue posible listar los usuarios");
    }

    public List<OpcionFormulario> listarProductosActivos() {
        String sql = """
                SELECT id, nombre
                FROM productos
                WHERE activo = true
                ORDER BY nombre ASC
                """;

        return listarOpciones(sql, "No fue posible listar los productos");
    }

    private List<OpcionFormulario> listarOpciones(String sql, String mensajeError) {
        List<OpcionFormulario> opciones = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                opciones.add(new OpcionFormulario(
                        resultSet.getLong("id"),
                        resultSet.getString("nombre")
                ));
            }

            return opciones;
        } catch (SQLException exception) {
            throw new IllegalStateException(mensajeError, exception);
        }
    }

    private void asignarParametrosStock(PreparedStatement statement, Stock stock) throws SQLException {
        statement.setLong(1, stock.getUsuarioId());
        statement.setLong(2, stock.getProductoId());
        statement.setBigDecimal(3, stock.getCantidad());
    }

    private Stock mapearStock(ResultSet resultSet) throws SQLException {
        Stock stock = new Stock();
        stock.setId(resultSet.getLong("id"));
        stock.setUsuarioId(resultSet.getLong("usuario_id"));
        stock.setNombreUsuario(resultSet.getString("nombre_usuario"));
        stock.setProductoId(resultSet.getLong("producto_id"));
        stock.setNombreProducto(resultSet.getString("nombre_producto"));
        stock.setCantidad(resultSet.getBigDecimal("cantidad"));
        stock.setCreadoEn(toLocalDateTime(resultSet.getTimestamp("creado_en")));
        stock.setActualizadoEn(toLocalDateTime(resultSet.getTimestamp("actualizado_en")));
        return stock;
    }

    private java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
