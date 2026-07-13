package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.service.InventarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/inventario")
@Tag(name = "Inventario", description = "Gestión de movimientos de inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    @Operation(summary = "Listar todos los movimientos de inventario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de movimientos obtenida exitosamente")
    })
    public CollectionModel<EntityModel<Inventario>> listarMovimientos() {
        List<EntityModel<Inventario>> inventarios = inventarioService.findAll().stream()
                .map(i -> EntityModel.of(i,
                        linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(i.getId())).withSelfRel(),
                        linkTo(methodOn(InventarioController.class).listarMovimientos()).withRel("inventario")))
                .collect(Collectors.toList());

        return CollectionModel.of(inventarios,
                linkTo(methodOn(InventarioController.class).listarMovimientos()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener movimiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    public ResponseEntity<EntityModel<Inventario>> obtenerMovimientoPorId(@PathVariable Long id) {
        Inventario inv = inventarioService.findById(id);
        if (inv == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(EntityModel.of(inv,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(id)).withSelfRel(),
                linkTo(methodOn(InventarioController.class).listarMovimientos()).withRel("inventario")));
    }

    @PostMapping
    @Operation(summary = "Registrar un nuevo movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento registrado exitosamente")
    })
    public EntityModel<Inventario> registrarMovimiento(@RequestBody Inventario inventario) {
        Inventario saved = inventarioService.save(inventario);
        return EntityModel.of(saved,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(saved.getId())).withSelfRel());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar movimiento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<EntityModel<Inventario>> actualizarMovimiento(@PathVariable Long id, @RequestBody Inventario inventario) {
        Inventario existente = inventarioService.findById(id);
        if (existente == null) return ResponseEntity.notFound().build();

        inventario.setId(id);
        Inventario actualizado = inventarioService.save(inventario);
        return ResponseEntity.ok(EntityModel.of(actualizado,
                linkTo(methodOn(InventarioController.class).obtenerMovimientoPorId(id)).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long id) {
        if (inventarioService.findById(id) == null) return ResponseEntity.notFound().build();
        inventarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}