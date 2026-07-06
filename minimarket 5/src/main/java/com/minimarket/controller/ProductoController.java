package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")

// =====================================================
// AGREGADO SEMANA 7
// Documentación del controlador Producto para OpenAPI
// =====================================================
@Tag(name = "Productos", description = "Operaciones para la gestión de productos del minimarket")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint GET /api/productos
    // =====================================================
    @Operation(
            summary = "Listar productos",
            description = "Obtiene la lista completa de productos registrados en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    })
    @GetMapping
    public List<Producto> listarProductos() {
        return productoService.findAll();
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint GET /api/productos/{id}
    // =====================================================
    @Operation(
            summary = "Buscar producto por ID",
            description = "Obtiene la información de un producto utilizando su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProductoPorId(

            @Parameter(
                    description = "ID del producto",
                    example = "1"
            )
            @PathVariable Long id) {

        Producto producto = productoService.findById(id);

        return (producto != null)
                ? ResponseEntity.ok(producto)
                : ResponseEntity.notFound().build();
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint POST /api/productos
    // =====================================================
    @Operation(
            summary = "Crear producto",
            description = "Registra un nuevo producto en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public Producto guardarProducto(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Información del producto a registrar",
                    required = true
            )

            @RequestBody Producto producto) {

        return productoService.save(producto);
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint PUT /api/productos/{id}
    // =====================================================
    @Operation(
            summary = "Actualizar producto",
            description = "Actualiza la información de un producto existente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(

            @Parameter(
                    description = "ID del producto",
                    example = "1"
            )
            @PathVariable Long id,

            @RequestBody Producto producto) {

        Producto productoExistente = productoService.findById(id);

        if (productoExistente != null) {

            producto.setId(id);

            return ResponseEntity.ok(productoService.save(producto));
        }

        return ResponseEntity.notFound().build();
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint DELETE /api/productos/{id}
    // =====================================================
    @Operation(
            summary = "Eliminar producto",
            description = "Elimina un producto utilizando su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(

            @Parameter(
                    description = "ID del producto",
                    example = "1"
            )
            @PathVariable Long id) {

        Producto producto = productoService.findById(id);

        if (producto != null) {

            productoService.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}