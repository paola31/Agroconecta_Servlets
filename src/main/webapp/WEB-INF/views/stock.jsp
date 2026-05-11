<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agroconecta - Stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<main class="page">
    <section class="header">
        <div>
            <p class="eyebrow">Agroconecta</p>
            <h1>Stock de productores</h1>
        </div>
        <div class="header-actions">
            <a class="button secondary" href="${pageContext.request.contextPath}/productos">Productos</a>
            <a class="button primary" href="${pageContext.request.contextPath}/stock?action=nuevo">Nuevo stock</a>
        </div>
    </section>

    <section class="panel">
        <div class="table-header">
            <h2>Registros de stock</h2>
            <span>${registrosStock.size()} registro(s)</span>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Productor</th>
                    <th>Producto</th>
                    <th>Cantidad</th>
                    <th>Actualizado</th>
                    <th class="actions">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty registrosStock}">
                        <tr>
                            <td colspan="6" class="empty">No hay registros de stock.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="stock" items="${registrosStock}">
                            <tr>
                                <td>${stock.id}</td>
                                <td>
                                    <strong>${stock.nombreUsuario}</strong>
                                    <small>Usuario ID: ${stock.usuarioId}</small>
                                </td>
                                <td>
                                    <strong>${stock.nombreProducto}</strong>
                                    <small>Producto ID: ${stock.productoId}</small>
                                </td>
                                <td>
                                    <fmt:formatNumber value="${stock.cantidad}" maxFractionDigits="3"/>
                                </td>
                                <td>${stock.actualizadoEn}</td>
                                <td class="actions">
                                    <a class="button secondary" href="${pageContext.request.contextPath}/stock?action=editar&id=${stock.id}">Editar</a>
                                    <form method="post" action="${pageContext.request.contextPath}/stock" class="inline-form">
                                        <input type="hidden" name="action" value="eliminar">
                                        <input type="hidden" name="id" value="${stock.id}">
                                        <button class="button danger" type="submit">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </section>
</main>
</body>
</html>
