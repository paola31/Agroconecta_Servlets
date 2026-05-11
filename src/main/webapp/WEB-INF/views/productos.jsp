<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agroconecta - Productos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<main class="page">
    <section class="header">
        <div>
            <p class="eyebrow">Agroconecta</p>
            <h1>Inventario de productos</h1>
        </div>
        <a class="button primary" href="${pageContext.request.contextPath}/productos?action=nuevo">Nuevo producto</a>
    </section>

    <section class="panel">
        <div class="table-header">
            <h2>Productos activos</h2>
            <span>${productos.size()} registro(s)</span>
        </div>

        <div class="table-wrapper">
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Unidad</th>
                    <th>Precio</th>
                    <th>Creado</th>
                    <th>Estado</th>
                    <th class="actions">Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty productos}">
                        <tr>
                            <td colspan="7" class="empty">No hay productos activos registrados.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="producto" items="${productos}">
                            <tr>
                                <td>${producto.id}</td>
                                <td>
                                    <strong>${producto.nombre}</strong>
                                    <small>${producto.descripcion}</small>
                                </td>
                                <td>${producto.unidadMedida}</td>
                                <td>
                                    $ <fmt:formatNumber value="${producto.precioUnitario}" maxFractionDigits="0"/>
                                </td>
                                <td>${producto.creadoEn}</td>
                                <td><span class="status">Activo</span></td>
                                <td class="actions">
                                    <a class="button secondary" href="${pageContext.request.contextPath}/productos?action=editar&id=${producto.id}">Editar</a>
                                    <form method="post" action="${pageContext.request.contextPath}/productos" class="inline-form">
                                        <input type="hidden" name="action" value="desactivar">
                                        <input type="hidden" name="id" value="${producto.id}">
                                        <button class="button danger" type="submit">Desactivar</button>
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
