# Minimarket Plus

Sistema backend desarrollado con **Spring Boot** para la administración de un minimarket. El proyecto permite gestionar productos, categorías, inventario, carritos de compra, ventas y usuarios, incorporando mecanismos de autenticación y autorización mediante **Spring Security**.

---

# Descripción

Minimarket Plus es una API REST desarrollada como proyecto académico para la asignatura **Desarrollo Backend II**.

El sistema implementa una arquitectura en capas que facilita la mantenibilidad del código y aplica buenas prácticas utilizando Spring Boot, Spring Data JPA y Spring Security.

Además, incorpora pruebas unitarias con **JUnit 5**, **Mockito** y análisis de cobertura mediante **JaCoCo** para validar la calidad del software.

---

#  Funcionalidades

- Gestión de productos.
- Gestión de categorías.
- Administración de inventario.
- Gestión de carritos de compra.
- Registro de ventas.
- Administración de usuarios y roles.
- Autenticación mediante Spring Security.
- Control de acceso basado en roles.
- Pruebas unitarias de controladores, servicios, entidades y componentes de seguridad.

---

# 🏗 Arquitectura

El proyecto sigue una arquitectura en capas:

```
Controller
      │
      ▼
Service
      │
      ▼
Repository
      │
      ▼
Base de Datos (H2)
```

Esta estructura permite separar responsabilidades y facilita el mantenimiento del sistema.

---

#  Estructura del proyecto

```
src
├── main
│   ├── java/com/minimarket
│   │   ├── controller
│   │   ├── entity
│   │   ├── repository
│   │   ├── security
│   │   └── service
│   └── resources
│       └── application.properties
│
└── test
    └── java/com/minimarket
        ├── controller
        ├── entity
        ├── security
        └── service
```

---

# 🛠 Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.1
- Spring Data JPA
- Spring Security
- Maven
- Lombok
- H2 Database
- JUnit 5
- Mockito
- JaCoCo
- Visual Studio Code
- SpringDoc OpenAPI
- Swagger UI

---

# Seguridad

El sistema implementa mecanismos de autenticación y autorización mediante Spring Security.

Se incluyen componentes de seguridad para controlar el acceso a las operaciones críticas del sistema, permitiendo restringir funcionalidades según el rol del usuario.

Entre los componentes desarrollados se encuentran:

- CustomUserDetails
- CustomUserDetailsService
- Configuración de Spring Security
- Roles de usuario

Las pruebas unitarias verifican el correcto funcionamiento de estos mecanismos.

---

# Base de datos

Durante el desarrollo y ejecución de las pruebas se utiliza:

**H2 Database**

Esto permite ejecutar el proyecto sin necesidad de instalar una base de datos externa.

---

# 🧪 Pruebas unitarias

El proyecto incorpora pruebas automatizadas utilizando:

- JUnit 5
- Mockito
- Spring Security Test

Las pruebas cubren diferentes capas del sistema:

### Controladores

- CarritoController
- CategoriaController
- DetalleVentaController
- InventarioController
- ProductoController
- UsuarioController
- VentaController

### Servicios

- CarritoService
- CategoriaService
- DetalleVentaService
- InventarioService
- ProductoService
- UsuarioService
- VentaService

### Entidades

- Usuario
- Rol
- Venta
- DetalleVenta

### Seguridad

- CustomUserDetails
- CustomUserDetailsService

---

# 📊 Cobertura de código

La cobertura de las pruebas unitarias se analiza mediante **JaCoCo**.

Para generar el reporte:

```bash
mvn clean test
```

El reporte HTML queda disponible en:

```
target/site/jacoco/index.html
```

---

# Ejecución del proyecto

Clonar el repositorio

```bash
git clone https://github.com/TU-USUARIO/minimarket.git
```

Ingresar al proyecto

```bash
cd minimarket
```

Compilar

```bash
mvn clean install
```

Ejecutar

```bash
mvn spring-boot:run
```

---

# 📖 Documentación de la API (OpenAPI / Swagger)

El proyecto incorpora documentación automática de la API mediante **SpringDoc OpenAPI**, lo que permite visualizar y probar los endpoints disponibles desde una interfaz web.

## Iniciar el proyecto

Compilar el proyecto:

```bash
mvn clean install
```

Ejecutar la aplicación:

```bash
mvn spring-boot:run
```

## Acceder a Swagger UI

Una vez iniciada la aplicación, abrir el siguiente enlace en el navegador:

```
http://localhost:8080/swagger-ui/index.html
```

Desde esta interfaz es posible:

- Visualizar todos los endpoints disponibles.
- Consultar los parámetros de entrada y salida.
- Revisar los códigos de respuesta HTTP.
- Probar los servicios utilizando la opción **Try it out**.

## Documento OpenAPI (JSON)

La especificación OpenAPI también puede consultarse en formato JSON desde:

```
http://localhost:8080/v3/api-docs
```

Este archivo puede importarse en herramientas como **Postman** para validar la consistencia de los endpoints documentados.


---

# 🧪 Ejecutar las pruebas

```bash
mvn test
```

o

```bash
mvn clean verify
```

---

#  Calidad del software

Durante el desarrollo se aplicaron buenas prácticas como:

- Arquitectura en capas.
- Separación de responsabilidades.
- Inyección de dependencias.
- Pruebas unitarias.
- Simulación de dependencias mediante Mockito.
- Medición de cobertura utilizando JaCoCo.
- Validación de autenticación y autorización mediante Spring Security.

---

