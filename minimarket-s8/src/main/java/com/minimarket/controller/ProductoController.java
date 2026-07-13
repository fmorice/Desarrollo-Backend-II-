package com.minimarket.controller;

import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión de productos del minimarket")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    public CollectionModel<EntityModel<Producto>> listarProductos() {
        List<EntityModel<Producto>> productos = productoService.findAll().stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(ProductoController.class).obtenerProductoPorId(p.getId())).withSelfRel(),
                        linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos")))
                .collect(Collectors.toList());

        return CollectionModel.of(productos, linkTo(methodOn(ProductoController.class).listarProductos()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public ResponseEntity<EntityModel<Producto>> obtenerProductoPorId(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        if (producto == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(EntityModel.of(producto,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(id)).withSelfRel(),
                linkTo(methodOn(ProductoController.class).listarProductos()).withRel("productos")));
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto")
    public EntityModel<Producto> guardarProducto(@RequestBody Producto producto) {
        Producto saved = productoService.save(producto);
        return EntityModel.of(saved,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(saved.getId())).withSelfRel());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente")
    public ResponseEntity<EntityModel<Producto>> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto existente = productoService.findById(id);
        if (existente == null) return ResponseEntity.notFound().build();

        producto.setId(id);
        Producto actualizado = productoService.save(producto);
        return ResponseEntity.ok(EntityModel.of(actualizado,
                linkTo(methodOn(ProductoController.class).obtenerProductoPorId(id)).withSelfRel()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        if (productoService.findById(id) == null) return ResponseEntity.notFound().build();
        productoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}