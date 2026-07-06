package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrito")

// =====================================================
// AGREGADO SEMANA 7
// Documentación del controlador Carrito para OpenAPI
// =====================================================
@Tag(name = "Carrito", description = "Operaciones para administrar el carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint GET /api/carrito
    // =====================================================
    @Operation(
            summary = "Listar carrito",
            description = "Obtiene todos los productos agregados al carrito."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito obtenido correctamente")
    })
    @GetMapping
    public List<Carrito> listarCarrito() {
        return carritoService.findAll();
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint GET /api/carrito/{id}
    // =====================================================
    @Operation(
            summary = "Buscar elemento del carrito por ID",
            description = "Obtiene un elemento específico del carrito."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Elemento encontrado"),
            @ApiResponse(responseCode = "404", description = "Elemento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> obtenerCarritoPorId(

            @Parameter(
                    description = "ID del registro del carrito",
                    example = "1"
            )
            @PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        return (carrito != null)
                ? ResponseEntity.ok(carrito)
                : ResponseEntity.notFound().build();
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint POST /api/carrito
    // =====================================================
    @Operation(
            summary = "Agregar producto al carrito",
            description = "Agrega un producto al carrito de compras."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto agregado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content)
    })
    @PostMapping
    public Carrito agregarProductoAlCarrito(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Información del producto que será agregado al carrito.",
                    required = true
            )

            @RequestBody Carrito carrito) {

        return carritoService.save(carrito);
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint PUT /api/carrito/{id}
    // =====================================================
    @Operation(
            summary = "Actualizar carrito",
            description = "Actualiza la información de un producto agregado al carrito."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Carrito actualizado"),
            @ApiResponse(responseCode = "404", description = "Elemento no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Carrito> actualizarCarrito(

            @Parameter(
                    description = "ID del registro del carrito",
                    example = "1"
            )
            @PathVariable Long id,

            @RequestBody Carrito carrito) {

        Carrito existente = carritoService.findById(id);

        if (existente != null) {

            carrito.setId(id);

            return ResponseEntity.ok(carritoService.save(carrito));
        }

        return ResponseEntity.notFound().build();
    }

    // =====================================================
    // AGREGADO SEMANA 7
    // Documentación del endpoint DELETE /api/carrito/{id}
    // =====================================================
    @Operation(
            summary = "Eliminar producto del carrito",
            description = "Elimina un producto del carrito utilizando su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Elemento no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProductoDelCarrito(

            @Parameter(
                    description = "ID del registro del carrito",
                    example = "1"
            )
            @PathVariable Long id) {

        Carrito carrito = carritoService.findById(id);

        if (carrito != null) {

            carritoService.deleteById(id);

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

}