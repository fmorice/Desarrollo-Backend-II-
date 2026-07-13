# Minimarket Plus Backend API

Este proyecto corresponde al desarrollo del backend para el sistema **Minimarket Plus**, integrando estándares de documentación **OpenAPI (Swagger)** y navegación hipermedia mediante **HATEOAS** (Hypermedia as the Engine of Application State) para mejorar la calidad técnica y la navegabilidad de la API.

## 🚀 Descripción del Proyecto

En esta etapa, optimizamos la arquitectura mediante un contrato API robusto y enlaces dinámicos, facilitando el trabajo de integración para desarrolladores y sistemas externos. El sistema implementa microservicios para gestionar productos, carritos de compra, inventario y usuarios.

## 🛠 Tecnologías Utilizadas

* **Java 17+**
* **Spring Boot 3.x**
* **SpringDoc OpenAPI (Swagger UI)**
* **Spring HATEOAS**
* **H2 Database (In-Memory)**
* **Maven**

## 📋 Características Principales

* **Documentación:** Interfaz interactiva vía `/swagger-ui.html`.
* **Navegación:** Implementación de `EntityModel` y `CollectionModel` para enlaces `_links`.
* **Persistencia:** Carga inicial automática de datos de prueba al arrancar el sistema.
* **Arquitectura:** Estructura modular (controller, service, entity, repository).

## ⚙️ Configuración y Ejecución

Para levantar el proyecto y validar la persistencia, sigue estos pasos desde la terminal en la raíz del proyecto:

### 1. Limpieza y Compilación

Elimina archivos de compilaciones previas para asegurar un entorno limpio:

```bash
# Windows
mvnw.cmd clean

# Linux / macOS
./mvnw clean

```

### 2. Ejecución del Backend

Inicia el microservicio. El sistema detectará las dependencias y preparará la base de datos H2:

```bash
# Windows
mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run

```

### 3. Validación del Entorno

Una vez que el log indique `Started MinimarketApplication`, puedes acceder a:

* **Documentación:** `http://localhost:8080/swagger-ui.html`
* **Consola H2:** `http://localhost:8080/h2-console`
* *Nota: Las credenciales de acceso deben coincidir con las definidas en tu archivo `src/main/resources/application.properties` (usuario: `admin` / contraseña: `admin123`).*



### 4. Prueba de Operatividad (cURL)

Puedes verificar la estructura HATEOAS mediante una petición GET a los productos:

```bash
curl -X GET http://localhost:8080/api/productos -H 'Accept: application/json'

```

## 🔗 Endpoints Principales

| Recurso | Método | Descripción |
| --- | --- | --- |
| `/api/productos` | GET | Lista productos con enlaces HATEOAS |
| `/api/carrito` | POST | Agrega productos al carrito |
| `/api/inventario` | GET | Consulta movimientos de stock |
| `/api/usuarios` | GET | Gestión de usuarios registrados |

## 🧪 Validación y Pruebas

* **Persistencia:** La persistencia se valida mediante la carga automática de datos iniciales vía Hibernate, garantizando que el entorno esté listo desde el primer arranque.
* **Contratos:** Puedes validar el JSON exportado desde `/v3/api-docs` importándolo directamente en **Postman** para verificar la integridad del contrato OpenAPI.

---




