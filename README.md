# Agroconecta Servlets EV02

Proyecto Java Web para la evidencia `GA7-220501096-AA2-EV02 - modulos de software codificados y probados`.

Esta version implementa modulos CRUD de productos y stock usando:

- Java 17
- Maven
- Servlets
- JSP
- JSTL
- JDBC directo con MySQL Connector/J
- MySQL 8 en Docker

## Relacion con la evidencia

La guia solicita formularios HTML con Servlets, uso de metodos GET y POST, elementos JSP y conexion a base de datos.

En este proyecto:

- `ProductoServlet` y `StockServlet` reciben las peticiones HTTP.
- Los metodos `doGet` y `doPost` manejan consulta, formularios, creacion, actualizacion, desactivacion y eliminacion.
- Las vistas JSP estan en `src/main/webapp/WEB-INF/views`.
- `ProductoDAO` y `StockDAO` realizan la conexion y operaciones SQL usando JDBC.
- `DatabaseConnection` centraliza la conexion con MySQL.

## Requisitos

Para ejecutar el proyecto se necesita:

- Java 17 o superior
- Maven
- Docker
- Docker Compose

## Base de datos

El proyecto incluye un archivo `docker-compose.yml` y el script:

```text
database/init/01-agroconecta.sql
```

Este script crea la base de datos `Agroconecta`, el usuario de aplicacion y las tablas necesarias.

Credenciales usadas por el proyecto:

```text
Base de datos: Agroconecta
Usuario: agro_backend
Password: agro_backend123
Puerto: 3306
```

Para levantar MySQL:

```bash
docker compose up -d
```

La aplicacion tiene valores locales por defecto para conectarse a MySQL. Si se necesita cambiar la conexion sin modificar el codigo, se pueden definir estas variables de entorno:

```bash
export DB_URL="jdbc:mysql://localhost:3306/Agroconecta?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true"
export DB_USER="agro_backend"
export DB_PASSWORD="agro_backend123"
```

## Ejecutar la aplicacion

Desde la carpeta del proyecto:

```bash
cd Agroconecta_Servlets_EV02
mvn jetty:run
```

La aplicacion queda disponible en:

```text
http://localhost:8082/productos
```

## Probar el CRUD desde la interfaz

Abrir en el navegador:

```text
http://localhost:8082/productos
```

Desde esa pantalla se puede:

- Consultar productos activos.
- Crear un producto con el boton `Nuevo producto`.
- Editar un producto con el boton `Editar`.
- Desactivar un producto con el boton `Desactivar`.
- Navegar al modulo de stock con el boton `Stock`.

La eliminacion se implemento como borrado logico: el registro no se elimina fisicamente, sino que el campo `activo` cambia a `false`.

## Probar el modulo de stock

Abrir en el navegador:

```text
http://localhost:8082/stock
```

Desde esa pantalla se puede:

- Consultar registros de stock con nombres reales de usuarios y productos.
- Crear un registro con el boton `Nuevo stock`.
- Editar la cantidad disponible.
- Eliminar un registro de stock.

El modulo de stock usa `INNER JOIN` para relacionar las tablas `stock`, `usuarios` y `productos`.

## Probar con curl

Crear producto:

```bash
curl -X POST http://localhost:8082/productos \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=crear&nombre=Producto+EV02&descripcion=Producto+creado+desde+Servlet&unidadMedida=kg&precioUnitario=3500&imagenUrl=https://example.com/producto.jpg"
```

Actualizar producto:

```bash
curl -X POST http://localhost:8082/productos \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=actualizar&id=100&nombre=Papa+Pastusa+Actualizada&descripcion=Producto+actualizado&unidadMedida=kg&precioUnitario=4200&imagenUrl=https://example.com/papa.jpg"
```

Desactivar producto:

```bash
curl -X POST http://localhost:8082/productos \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=desactivar&id=100"
```

Crear stock:

```bash
curl -X POST http://localhost:8082/stock \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=crear&usuarioId=1&productoId=100&cantidad=7.500"
```

Actualizar stock:

```bash
curl -X POST http://localhost:8082/stock \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=actualizar&id=1&usuarioId=2&productoId=100&cantidad=15.000"
```

Eliminar stock:

```bash
curl -X POST http://localhost:8082/stock \
  -H "Content-Type: application/x-www-form-urlencoded" \
  --data "action=eliminar&id=1"
```

## Estructura principal

```text
src/main/java/com/agroconecta/config/DatabaseConnection.java
src/main/java/com/agroconecta/dao/ProductoDAO.java
src/main/java/com/agroconecta/dao/StockDAO.java
src/main/java/com/agroconecta/model/Producto.java
src/main/java/com/agroconecta/model/Stock.java
src/main/java/com/agroconecta/servlet/ProductoServlet.java
src/main/java/com/agroconecta/servlet/StockServlet.java
src/main/webapp/index.jsp
src/main/webapp/WEB-INF/views/productos.jsp
src/main/webapp/WEB-INF/views/formulario-producto.jsp
src/main/webapp/WEB-INF/views/stock.jsp
src/main/webapp/WEB-INF/views/formulario-stock.jsp
src/main/webapp/assets/css/styles.css
```

## Comandos de verificacion

Compilar:

```bash
mvn package
```

Ejecutar:

```bash
mvn jetty:run
```

Generar archivo comprimido de entrega desde la carpeta padre:

```bash
zip -r PAOLACABALLERO_AA2_EV02.zip Agroconecta_Servlets_EV02 \
  -x "*/target/*" "*/.git/*" "*/.idea/*"
```
