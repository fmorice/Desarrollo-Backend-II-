package com.minimarket.controller;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private Usuario usuario;
    private Usuario usuario2;
    private Rol rolAdmin;
    private Rol rolUser;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol("ADMIN");
        rolAdmin.setId(1L);

        rolUser = new Rol("USER");
        rolUser.setId(2L);

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setPassword("password123");
        usuario.setRoles(Set.of(rolAdmin));

        usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user");
        usuario2.setPassword("userpass456");
        usuario2.setRoles(Set.of(rolUser));
    }

    // ====== PRUEBAS DE listarUsuarios() ======
    @Test
    void testListarUsuarios_ConUsuarios() {
        // Arrange
        List<Usuario> usuariosList = Arrays.asList(usuario, usuario2);
        when(usuarioService.findAll()).thenReturn(usuariosList);

        // Act
        List<Usuario> resultado = usuarioController.listarUsuarios();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("admin", resultado.get(0).getUsername());
        assertEquals("user", resultado.get(1).getUsername());
        verify(usuarioService, times(1)).findAll();
    }

    @Test
    void testListarUsuarios_ListaVacia() {
        // Arrange
        when(usuarioService.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Usuario> resultado = usuarioController.listarUsuarios();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioService, times(1)).findAll();
    }

    // ====== PRUEBAS DE obtenerUsuarioPorId() ======
    @Test
    void testObtenerUsuarioPorId_UsuarioExistente() {
        // Arrange
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        ResponseEntity<Usuario> resultado = usuarioController.obtenerUsuarioPorId(1L);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("admin", resultado.getBody().getUsername());
        verify(usuarioService, times(1)).findById(1L);
    }

    @Test
    void testObtenerUsuarioPorId_UsuarioNoExistente() {
        // Arrange
        when(usuarioService.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Usuario> resultado = usuarioController.obtenerUsuarioPorId(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(usuarioService, times(1)).findById(999L);
    }

    // ====== PRUEBAS DE guardarUsuario() ======
    @Test
    void testGuardarUsuario_UsuarioNuevo() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("newuser");
        nuevoUsuario.setPassword("newpass123");
        nuevoUsuario.setRoles(Set.of(rolUser));

        when(usuarioService.save(nuevoUsuario)).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioController.guardarUsuario(nuevoUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        assertEquals(1L, resultado.getId());
        verify(usuarioService, times(1)).save(nuevoUsuario);
    }

    @Test
    void testGuardarUsuario_UsuarioConRoles() {
        // Arrange
        Usuario usuarioMultiRol = new Usuario();
        usuarioMultiRol.setUsername("superadmin");
        usuarioMultiRol.setPassword("super123");
        usuarioMultiRol.setRoles(Set.of(rolAdmin, rolUser));

        when(usuarioService.save(usuarioMultiRol)).thenReturn(usuarioMultiRol);

        // Act
        Usuario resultado = usuarioController.guardarUsuario(usuarioMultiRol);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getRoles().size());
        verify(usuarioService, times(1)).save(usuarioMultiRol);
    }

    // ====== PRUEBAS DE actualizarUsuario() ======
    @Test
    void testActualizarUsuario_UsuarioExistente() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUsername("admin_updated");
        usuarioActualizado.setPassword("newpass789");
        usuarioActualizado.setRoles(Set.of(rolAdmin, rolUser));

        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        ResponseEntity<Usuario> resultado = usuarioController.actualizarUsuario(1L, usuarioActualizado);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals("admin_updated", resultado.getBody().getUsername());
        verify(usuarioService, times(1)).findById(1L);
        verify(usuarioService, times(1)).save(any(Usuario.class));
    }

    @Test
    void testActualizarUsuario_UsuarioNoExistente() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setUsername("admin_updated");
        usuarioActualizado.setPassword("newpass789");

        when(usuarioService.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Usuario> resultado = usuarioController.actualizarUsuario(999L, usuarioActualizado);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(usuarioService, times(1)).findById(999L);
        verify(usuarioService, never()).save(any());
    }

    // ====== PRUEBAS DE eliminarUsuario() ======
    @Test
    void testEliminarUsuario_UsuarioExistente() {
        // Arrange
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioService).deleteById(1L);

        // Act
        ResponseEntity<Void> resultado = usuarioController.eliminarUsuario(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(usuarioService, times(1)).findById(1L);
        verify(usuarioService, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarUsuario_UsuarioNoExistente() {
        // Arrange
        when(usuarioService.findById(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> resultado = usuarioController.eliminarUsuario(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(usuarioService, times(1)).findById(999L);
        verify(usuarioService, never()).deleteById(any());
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testObtenerUsuarioPorId_VariosUsuarios() {
        // Arrange
        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.findById(2L)).thenReturn(Optional.of(usuario2));

        // Act
        ResponseEntity<Usuario> resultado1 = usuarioController.obtenerUsuarioPorId(1L);
        ResponseEntity<Usuario> resultado2 = usuarioController.obtenerUsuarioPorId(2L);

        // Assert
        assertEquals(HttpStatus.OK, resultado1.getStatusCode());
        assertEquals(HttpStatus.OK, resultado2.getStatusCode());
        assertEquals("admin", resultado1.getBody().getUsername());
        assertEquals("user", resultado2.getBody().getUsername());
    }

    @Test
    void testActualizarUsuario_MantieneId() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setUsername("admin_updated");
        usuarioActualizado.setPassword("newpass789");

        when(usuarioService.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioService.save(any(Usuario.class))).thenReturn(usuarioActualizado);

        // Act
        ResponseEntity<Usuario> resultado = usuarioController.actualizarUsuario(1L, usuarioActualizado);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1L, resultado.getBody().getId());
    }
}
