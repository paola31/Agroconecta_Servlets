<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agroconecta - Formulario stock</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<main class="page">
    <section class="header">
        <div>
            <p class="eyebrow">Agroconecta</p>
            <h1>
                <c:choose>
                    <c:when test="${modo == 'editar'}">Editar stock</c:when>
                    <c:otherwise>Nuevo stock</c:otherwise>
                </c:choose>
            </h1>
        </div>
        <a class="button secondary" href="${pageContext.request.contextPath}/stock">Volver</a>
    </section>

    <section class="panel form-panel">
        <c:if test="${not empty error}">
            <div class="alert">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/stock" class="product-form">
            <c:choose>
                <c:when test="${modo == 'editar'}">
                    <input type="hidden" name="action" value="actualizar">
                    <input type="hidden" name="id" value="${stock.id}">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="action" value="crear">
                </c:otherwise>
            </c:choose>

            <div class="field">
                <label for="usuarioId">Productor o usuario</label>
                <select id="usuarioId" name="usuarioId" required>
                    <option value="">Seleccionar</option>
                    <c:forEach var="usuario" items="${usuarios}">
                        <option value="${usuario.id}" ${stock.usuarioId == usuario.id ? 'selected' : ''}>${usuario.nombre}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="field">
                <label for="productoId">Producto</label>
                <select id="productoId" name="productoId" required>
                    <option value="">Seleccionar</option>
                    <c:forEach var="producto" items="${productos}">
                        <option value="${producto.id}" ${stock.productoId == producto.id ? 'selected' : ''}>${producto.nombre}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="field">
                <label for="cantidad">Cantidad disponible</label>
                <input id="cantidad" name="cantidad" type="number" min="0" step="0.001" value="${stock.cantidad}" required>
            </div>

            <div class="form-actions">
                <a class="button secondary" href="${pageContext.request.contextPath}/stock">Cancelar</a>
                <button class="button primary" type="submit">Guardar</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>
