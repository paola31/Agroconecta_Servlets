<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Agroconecta - Formulario producto</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/styles.css">
</head>
<body>
<main class="page">
    <section class="header">
        <div>
            <p class="eyebrow">Agroconecta</p>
            <h1>
                <c:choose>
                    <c:when test="${modo == 'editar'}">Editar producto</c:when>
                    <c:otherwise>Nuevo producto</c:otherwise>
                </c:choose>
            </h1>
        </div>
        <a class="button secondary" href="${pageContext.request.contextPath}/productos">Volver</a>
    </section>

    <section class="panel form-panel">
        <c:if test="${not empty error}">
            <div class="alert">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/productos" class="product-form">
            <c:choose>
                <c:when test="${modo == 'editar'}">
                    <input type="hidden" name="action" value="actualizar">
                    <input type="hidden" name="id" value="${producto.id}">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="action" value="crear">
                </c:otherwise>
            </c:choose>

            <div class="field">
                <label for="nombre">Nombre</label>
                <input id="nombre" name="nombre" value="${producto.nombre}" required maxlength="140">
            </div>

            <div class="field">
                <label for="unidadMedida">Unidad de medida</label>
                <select id="unidadMedida" name="unidadMedida" required>
                    <option value="">Seleccionar</option>
                    <option value="kg" ${producto.unidadMedida == 'kg' ? 'selected' : ''}>Kilogramo</option>
                    <option value="lb" ${producto.unidadMedida == 'lb' ? 'selected' : ''}>Libra</option>
                    <option value="paquete" ${producto.unidadMedida == 'paquete' ? 'selected' : ''}>Paquete</option>
                    <option value="caja" ${producto.unidadMedida == 'caja' ? 'selected' : ''}>Caja</option>
                </select>
            </div>

            <div class="field">
                <label for="precioUnitario">Precio unitario</label>
                <input id="precioUnitario" name="precioUnitario" type="number" min="0" step="0.01" value="${producto.precioUnitario}" required>
            </div>

            <div class="field">
                <label for="imagenUrl">URL de imagen</label>
                <input id="imagenUrl" name="imagenUrl" value="${producto.imagenUrl}" maxlength="255">
            </div>

            <div class="field full">
                <label for="descripcion">Descripción</label>
                <textarea id="descripcion" name="descripcion" rows="5">${producto.descripcion}</textarea>
            </div>

            <div class="form-actions">
                <a class="button secondary" href="${pageContext.request.contextPath}/productos">Cancelar</a>
                <button class="button primary" type="submit">Guardar</button>
            </div>
        </form>
    </section>
</main>
</body>
</html>
