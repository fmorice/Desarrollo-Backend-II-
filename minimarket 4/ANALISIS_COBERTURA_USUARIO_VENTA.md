# Análisis de Cobertura de Pruebas - JaCoCo
## Usuario y Venta - Objetivo: 80%

**Fecha:** 2026-06-14  
**Proyecto:** Minimarket Spring Boot  
**Objetivo:** Aumentar cobertura JaCoCo a 80% en funcionalidades Usuario y Venta

---

## 1. CLASES RELACIONADAS CON USUARIO

### Entidades
- ✅ **Usuario.java** - Entity
- ✅ **Rol.java** - Entity  
- ⚠️ **LoginRequest.java** - Model (vacío)

### Servicios
- **UsuarioService.java** - Interface
- **UsuarioServiceImpl.java** - Implementación
- **RolService.java** - Interface
- **RolServiceImpl.java** - Implementación

### Seguridad
- **CustomUserDetailsService.java** - Service
- **CustomUserDetails.java** - UserDetails Implementation

### Controladores
- **UsuarioController.java** - REST API

### Repositorios
- **UsuarioRepository.java** - JPA Interface
- **RolRepository.java** - JPA Interface

---

## 2. CLASES RELACIONADAS CON VENTA

### Entidades
- ✅ **Venta.java** - Entity
- ✅ **DetalleVenta.java** - Entity

### Servicios
- **VentaService.java** - Interface
- **VentaServiceImpl.java** - Implementación
- **DetalleVentaService.java** - Interface
- **DetalleVentaServiceImpl.java** - Implementación

### Controladores
- **VentaController.java** - REST API
- **DetalleVentaController.java** - REST API

### Repositorios
- **VentaRepository.java** - JPA Interface
- **DetalleVentaRepository.java** - JPA Interface

---

## 3. PRUEBAS EXISTENTES vs FALTANTES

### ✅ PRUEBAS EXISTENTES (Cobertura Parcial)

#### UsuarioTest.java
**Métodos cubiertos:**
- testCrearUsuario() - Crea usuario con roles
- testEquals() - Valida igualdad de objetos
- testAgregarRoles() - Agrega múltiples roles
- testUsuario_SinDatosRequeridos_Falla() - Valida campos nulos

**Métodos sin cobertura:**
- Usuario.setId(Long id) - Sin verificación explícita
- Usuario.getId() - Sin verificación explícita
- Usuario.getPassword()/setPassword() - Parcialmente cubierto
- Usuario.getUsername()/setUsername() - Parcialmente cubierto
- Usuario.getRoles()/setRoles() - Parcialmente cubierto

#### VentaServiceTest.java
**Métodos cubiertos:**
- testFindAll() - Mock de findAll()
- testSave() - Mock de save()

**Métodos sin cobertura:**
- VentaServiceImpl.findById(Long id) - PARCIAL (no cubre el caso null)
- VentaServiceImpl.findByUsuarioId(Long usuarioId) - NO CUBIERTO
- VentaServiceImpl.findByUsuarioId() - verifyMock - NO CUBIERTO

### ❌ PRUEBAS FALTANTES (CRÍTICAS)

#### Servicios de Usuario
| Clase | Métodos Sin Cobertura | Prioridad |
|-------|----------------------|-----------|
| **UsuarioServiceImpl** | findAll(), findById(), findByUsername(), save(), deleteById() | ALTA |
| **RolServiceImpl** | findByNombre() | MEDIA |

#### Servicios de Venta
| Clase | Métodos Sin Cobertura | Prioridad |
|-------|----------------------|-----------|
| **VentaServiceImpl** | findByUsuarioId() | ALTA |
| **DetalleVentaServiceImpl** | findAll(), findById(), save(), deleteById(), findByVentaId() | ALTA |

#### Controladores
| Clase | Métodos Sin Cobertura | Prioridad |
|-------|----------------------|-----------|
| **UsuarioController** | listarUsuarios(), obtenerUsuarioPorId(), guardarUsuario(), actualizarUsuario(), eliminarUsuario() | ALTA |
| **VentaController** | listarVentas(), obtenerVentaPorId(), guardarVenta() | ALTA |
| **DetalleVentaController** | Todos los métodos | ALTA |

#### Seguridad
| Clase | Métodos Sin Cobertura | Prioridad |
|-------|----------------------|-----------|
| **CustomUserDetailsService** | loadUserByUsername() - éxito y excepción | ALTA |
| **CustomUserDetails** | getAuthorities(), getPassword(), getUsername(), isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired(), isEnabled() | ALTA |

---

## 4. PLAN DE CREACIÓN DE PRUEBAS

### Archivos a Crear/Modificar

#### **Nuevos Archivos:**
1. ✏️ **src/test/java/com/minimarket/service/UsuarioServiceImplTest.java**
2. ✏️ **src/test/java/com/minimarket/service/RolServiceImplTest.java**
3. ✏️ **src/test/java/com/minimarket/service/VentaServiceImplTest.java** (Expandir)
4. ✏️ **src/test/java/com/minimarket/service/DetalleVentaServiceImplTest.java**
5. ✏️ **src/test/java/com/minimarket/controller/UsuarioControllerTest.java**
6. ✏️ **src/test/java/com/minimarket/controller/VentaControllerTest.java**
7. ✏️ **src/test/java/com/minimarket/controller/DetalleVentaControllerTest.java**
8. ✏️ **src/test/java/com/minimarket/security/CustomUserDetailsServiceTest.java**
9. ✏️ **src/test/java/com/minimarket/security/CustomUserDetailsTest.java**
10. ✏️ **src/test/java/com/minimarket/entity/UsuarioEntityTest.java** (Mejorar)
11. ✏️ **src/test/java/com/minimarket/entity/RolEntityTest.java**
12. ✏️ **src/test/java/com/minimarket/entity/VentaEntityTest.java**

#### **Archivos a Modificar:**
- VentaServiceTest.java → Renombrar a VentaServiceImplTest.java y expandir
- UsuarioTest.java → Ampliar cobertura

---

## 5. ESCENARIOS DE PRUEBA POR CLASE

### UsuarioServiceImpl
```
✓ findAll() - Lista vacía y con datos
✓ findById(Long id) - Caso encontrado y no encontrado
✓ findByUsername(String username) - Caso encontrado y no encontrado
✓ save(Usuario) - Guardar usuario nuevo
✓ deleteById(Long id) - Eliminar usuario existente
✓ Validar que utiliza correctamente el repository (verify)
```

### RolServiceImpl
```
✓ findByNombre(String nombre) - Caso encontrado y no encontrado
✓ Validar que utiliza correctamente el repository (verify)
```

### VentaServiceImpl
```
✓ findAll() - Expandir: lista vacía y con datos
✓ findById(Long id) - Expandir: caso null y con datos
✓ save(Venta) - Expandir: guardar
✓ findByUsuarioId(Long usuarioId) - NUEVO: lista vacía y con datos
✓ Validar comportamiento del método findById cuando retorna null
```

### DetalleVentaServiceImpl
```
✓ findAll() - NUEVO: lista vacía y con datos
✓ findById(Long id) - NUEVO: caso encontrado y no encontrado
✓ save(DetalleVenta) - NUEVO: guardar
✓ deleteById(Long id) - NUEVO: eliminar
✓ findByVentaId(Long ventaId) - NUEVO: lista vacía y con datos
```

### UsuarioController
```
✓ listarUsuarios() - Retorna lista de usuarios
✓ obtenerUsuarioPorId() - Caso 200 OK y 404 Not Found
✓ guardarUsuario() - POST crear usuario
✓ actualizarUsuario() - PUT actualizar usuario (200 OK y 404 Not Found)
✓ eliminarUsuario() - DELETE (204 No Content y 404 Not Found)
✓ Validar ResponseEntity correcta en cada caso
```

### VentaController
```
✓ listarVentas() - Retorna lista de ventas
✓ obtenerVentaPorId() - Caso 200 OK y 404 Not Found
✓ guardarVenta() - POST crear venta
```

### DetalleVentaController
```
✓ Todos los métodos (si existen)
```

### CustomUserDetailsService
```
✓ loadUserByUsername() - NUEVO: Usuario encontrado
✓ loadUserByUsername() - NUEVO: Usuario NO encontrado (excepción)
✓ Validar que retorna CustomUserDetails correcto
✓ Validar que lanza UsernameNotFoundException
```

### CustomUserDetails
```
✓ getAuthorities() - Retorna Collection de GrantedAuthority
✓ getPassword() - Retorna password del usuario
✓ getUsername() - Retorna username del usuario
✓ isAccountNonExpired() - Retorna true
✓ isAccountNonLocked() - Retorna true
✓ isCredentialsNonExpired() - Retorna true
✓ isEnabled() - Retorna true
✓ Constructor - Inicializa con usuario
```

### Entidades (Getters/Setters)
```
✓ Usuario.java - Cobertura completa getters/setters
✓ Rol.java - Cobertura completa getters/setters
✓ Venta.java - Cobertura completa getters/setters
```

---

## 6. ESTIMACIÓN DE COBERTURA

| Componente | Métodos | Cubiertos Actuales | Meta | Incremento |
|------------|---------|-------------------|------|-----------|
| Usuario (Entity) | 10 | 50% | 100% | +50% |
| UsuarioService | 5 | 0% | 100% | +100% |
| UsuarioController | 5 | 0% | 100% | +100% |
| Rol (Entity) | 6 | 0% | 100% | +100% |
| RolService | 1 | 0% | 100% | +100% |
| Venta (Entity) | 10 | 0% | 100% | +100% |
| VentaService | 4 | 40% | 100% | +60% |
| VentaController | 3 | 0% | 100% | +100% |
| DetalleVenta (Entity) | 10 | 0% | 100% | +100% |
| DetalleVentaService | 5 | 0% | 100% | +100% |
| CustomUserDetailsService | 1 | 0% | 100% | +100% |
| CustomUserDetails | 7 | 0% | 100% | +100% |
| **TOTAL** | **67** | **~15%** | **80%+** | **+65%** |

---

## 7. HERRAMIENTAS Y PATRONES A USAR

### Frameworks
- **JUnit 5** - Framework de testing
- **Mockito** - Mock de dependencias
- **MockMvc** (para controladores) - Testing HTTP

### Anotaciones
- `@Test` - Marcar método como test
- `@ExtendWith(MockitoExtension.class)` - Integrar Mockito
- `@Mock` - Mock de dependencias
- `@InjectMocks` - Inyectar mocks
- `@WebMvcTest` - Testing de controladores

### Assertions
- `assertEquals()` - Igualdad
- `assertNotNull()` - No nulo
- `assertNull()` - Es nulo
- `assertTrue()` - Condición verdadera
- `assertFalse()` - Condición falsa
- `assertThrows()` - Excepción esperada

### Validaciones Mockito
- `verify(mock).method()` - Verificar llamadas
- `when(mock.method()).thenReturn(value)` - Comportamiento
- `times(n)` - Número de veces llamado

---

## 8. ARCHIVOS A ENTREGAR

1. Análisis completo (este documento)
2. 12 archivos de prueba nuevos/expandidos
3. Cobertura JaCoCo ≥ 80%
4. BUILD SUCCESS sin errores

---

## 9. SIGUIENTE PASO

Ejecutar en terminal:
```bash
cd /Users/florenciamorice/Downloads/"minimarket 2"
mvn clean test
mvn jacoco:report
```

Verificar cobertura en:
```
target/site/jacoco/index.html
```

---

**ESTADO:** Análisis Completado ✅  
**SIGUIENTE:** Crear archivos de prueba
