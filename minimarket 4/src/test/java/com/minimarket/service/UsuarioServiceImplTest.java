package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;
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
    }

    // ====== PRUEBAS DE findAll() ======
    @Test
    void testFindAll_ConUsuarios() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user2");
        usuario2.setPassword("password456");
        usuario2.setRoles(Set.of(rolUser));

        List<Usuario> usuariosList = Arrays.asList(usuario, usuario2);
        when(usuarioRepository.findAll()).thenReturn(usuariosList);

        // Act
        List<Usuario> resultado = usuarioService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("admin", resultado.get(0).getUsername());
        assertEquals("user2", resultado.get(1).getUsername());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        // Arrange
        when(usuarioRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Usuario> resultado = usuarioService.findAll();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }

    // ====== PRUEBAS DE findById() ======
    @Test
    void testFindById_UsuarioExistente() {
        // Arrange
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.findById(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getUsername());
        assertEquals(1L, resultado.get().getId());
        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_UsuarioNoExistente() {
        // Arrange
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioService.findById(999L);

        // Assert
        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).findById(999L);
    }

    // ====== PRUEBAS DE findByUsername() ======
    @Test
    void testFindByUsername_UsuarioExistente() {
        // Arrange
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        // Act
        Optional<Usuario> resultado = usuarioService.findByUsername("admin");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("admin", resultado.get().getUsername());
        assertEquals(1L, resultado.get().getId());
        verify(usuarioRepository, times(1)).findByUsername("admin");
    }

    @Test
    void testFindByUsername_UsuarioNoExistente() {
        // Arrange
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> resultado = usuarioService.findByUsername("noexiste");

        // Assert
        assertFalse(resultado.isPresent());
        verify(usuarioRepository, times(1)).findByUsername("noexiste");
    }

    // ====== PRUEBAS DE save() ======
    @Test
    void testSave_UsuarioNuevo() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("newuser");
        nuevoUsuario.setPassword("newpass123");
        nuevoUsuario.setRoles(Set.of(rolUser));

        when(usuarioRepository.save(nuevoUsuario)).thenReturn(usuario);

        // Act
        Usuario resultado = usuarioService.save(nuevoUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).save(nuevoUsuario);
    }

    @Test
    void testSave_ActualizarUsuarioExistente() {
        // Arrange
        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setId(1L);
        usuarioActualizado.setUsername("admin_updated");
        usuarioActualizado.setPassword("newpassword789");
        usuarioActualizado.setRoles(Set.of(rolAdmin, rolUser));

        when(usuarioRepository.save(usuarioActualizado)).thenReturn(usuarioActualizado);

        // Act
        Usuario resultado = usuarioService.save(usuarioActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("admin_updated", resultado.getUsername());
        assertEquals(1L, resultado.getId());
        verify(usuarioRepository, times(1)).save(usuarioActualizado);
    }

    // ====== PRUEBAS DE deleteById() ======
    @Test
    void testDeleteById_UsuarioExistente() {
        // Arrange
        doNothing().when(usuarioRepository).deleteById(1L);

        // Act
        usuarioService.deleteById(1L);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_UsuarioNoExistente() {
        // Arrange
        doNothing().when(usuarioRepository).deleteById(999L);

        // Act
        usuarioService.deleteById(999L);

        // Assert
        verify(usuarioRepository, times(1)).deleteById(999L);
    }

    // ====== PRUEBAS ADICIONALES DE VALIDACIONES ======
    @Test
    void testUsuarioConMultiplesRoles() {
        // Arrange
        Usuario usuarioMultiRol = new Usuario();
        usuarioMultiRol.setId(3L);
        usuarioMultiRol.setUsername("superadmin");
        usuarioMultiRol.setPassword("super123");
        usuarioMultiRol.setRoles(Set.of(rolAdmin, rolUser));

        when(usuarioRepository.save(usuarioMultiRol)).thenReturn(usuarioMultiRol);

        // Act
        Usuario resultado = usuarioService.save(usuarioMultiRol);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getRoles().size());
        assertTrue(resultado.getRoles().stream().anyMatch(r -> "ADMIN".equals(r.getNombre())));
        assertTrue(resultado.getRoles().stream().anyMatch(r -> "USER".equals(r.getNombre())));
    }

    @Test
    void testFindByUsername_CaseSensitive() {
        // Arrange
        when(usuarioRepository.findByUsername("ADMIN")).thenReturn(Optional.empty());
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        // Act & Assert
        assertTrue(usuarioService.findByUsername("admin").isPresent());
        assertFalse(usuarioService.findByUsername("ADMIN").isPresent());
    }
}
