package com.minimarket.controller;

import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios")
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioService.findAll().stream()
                .map(u -> EntityModel.of(u,
                        linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(u.getId())).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios")))
                .collect(Collectors.toList());

        return CollectionModel.of(usuarios, linkTo(methodOn(UsuarioController.class).listarUsuarios()).withSelfRel());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(u -> ResponseEntity.ok(EntityModel.of(u,
                        linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(id)).withSelfRel(),
                        linkTo(methodOn(UsuarioController.class).listarUsuarios()).withRel("usuarios"))))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Guardar un nuevo usuario")
    public EntityModel<Usuario> guardarUsuario(@RequestBody Usuario usuario) {
        Usuario saved = usuarioService.save(usuario);
        return EntityModel.of(saved,
                linkTo(methodOn(UsuarioController.class).obtenerUsuarioPorId(saved.getId())).withSelfRel());
    }
}