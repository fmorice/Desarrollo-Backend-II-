package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.service.CarritoService;
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
@RequestMapping("/api/carrito")
@Tag(name = "Carrito", description = "Gestión de carritos de compra")
public class CarritoController {

    private final CarritoService carritoService;

    @Autowired
    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los carritos")
    public CollectionModel<EntityModel<Carrito>> listarCarrito() {
        List<EntityModel<Carrito>> carritos = carritoService.findAll().stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito")))
                .collect(Collectors.toList());

        return CollectionModel.of(carritos, linkTo(methodOn(CarritoController.class).listarCarrito()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener carrito por ID")
    public ResponseEntity<EntityModel<Carrito>> obtenerCarritoPorId(@PathVariable Long id) {
        Carrito carrito = carritoService.findById(id);
        if (carrito == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(EntityModel.of(carrito,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(id)).withSelfRel(),
                linkTo(methodOn(CarritoController.class).listarCarrito()).withRel("carrito")));
    }

    @PostMapping
    @Operation(summary = "Agregar producto al carrito")
    public EntityModel<Carrito> agregarProductoAlCarrito(@RequestBody Carrito carrito) {
        Carrito saved = carritoService.save(carrito);
        return EntityModel.of(saved,
                linkTo(methodOn(CarritoController.class).obtenerCarritoPorId(saved.getId())).withSelfRel());
    }
}