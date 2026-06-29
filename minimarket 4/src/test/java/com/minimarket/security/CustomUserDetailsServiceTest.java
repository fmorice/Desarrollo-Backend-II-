package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.UsuarioRepository;
import com.minimarket.security.model.CustomUserDetails;
import com.minimarket.security.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

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

    // ====== PRUEBAS DE loadUserByUsername() - ÉXITO ======
    @Test
    void testLoadUserByUsername_UsuarioExistente() {
        // Arrange
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        // Act
        CustomUserDetails resultado = (CustomUserDetails) customUserDetailsService.loadUserByUsername("admin");

        // Assert
        assertNotNull(resultado);
        assertEquals("admin", resultado.getUsername());
        assertEquals("password123", resultado.getPassword());
        verify(usuarioRepository, times(1)).findByUsername("admin");
    }

    @Test
    void testLoadUserByUsername_UsuarioConMultiplesRoles() {
        // Arrange
        Usuario usuarioMultiRol = new Usuario();
        usuarioMultiRol.setId(2L);
        usuarioMultiRol.setUsername("superadmin");
        usuarioMultiRol.setPassword("super123");
        usuarioMultiRol.setRoles(Set.of(rolAdmin, rolUser));

        when(usuarioRepository.findByUsername("superadmin")).thenReturn(Optional.of(usuarioMultiRol));

        // Act
        CustomUserDetails resultado = (CustomUserDetails) customUserDetailsService.loadUserByUsername("superadmin");

        // Assert
        assertNotNull(resultado);
        assertEquals("superadmin", resultado.getUsername());
        assertEquals(2, resultado.getAuthorities().size());
        verify(usuarioRepository, times(1)).findByUsername("superadmin");
    }

    // ====== PRUEBAS DE loadUserByUsername() - ERROR ======
    @Test
    void testLoadUserByUsername_UsuarioNoExistente() {
        // Arrange
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("noexiste");
        });
        verify(usuarioRepository, times(1)).findByUsername("noexiste");
    }

    @Test
    void testLoadUserByUsername_UsuarioNoExistente_MensajeExcepcion() {
        // Arrange
        String usuarioNoExistente = "usuarioInexistente";
        when(usuarioRepository.findByUsername(usuarioNoExistente)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException excepcion = assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(usuarioNoExistente);
        });
        assertTrue(excepcion.getMessage().contains("Usuario no encontrado"));
        assertTrue(excepcion.getMessage().contains(usuarioNoExistente));
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testLoadUserByUsername_CaseSensitive() {
        // Arrange
        when(usuarioRepository.findByUsername("ADMIN")).thenReturn(Optional.empty());
        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(usuario));

        // Act & Assert
        CustomUserDetails resultado = (CustomUserDetails) customUserDetailsService.loadUserByUsername("admin");
        assertNotNull(resultado);

        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername("ADMIN");
        });
    }

    @Test
    void testLoadUserByUsername_UsuarioConRolUser() {
        // Arrange
        Usuario usuarioUser = new Usuario();
        usuarioUser.setId(3L);
        usuarioUser.setUsername("normaluser");
        usuarioUser.setPassword("userpass789");
        usuarioUser.setRoles(Set.of(rolUser));

        when(usuarioRepository.findByUsername("normaluser")).thenReturn(Optional.of(usuarioUser));

        // Act
        CustomUserDetails resultado = (CustomUserDetails) customUserDetailsService.loadUserByUsername("normaluser");

        // Assert
        assertNotNull(resultado);
        assertEquals("normaluser", resultado.getUsername());
        assertEquals(1, resultado.getAuthorities().size());
        assertTrue(resultado.getAuthorities().stream()
                .anyMatch(auth -> "USER".equals(auth.getAuthority())));
    }

    @Test
    void testLoadUserByUsername_UsuarioSinRoles() {
        // Arrange
        Usuario usuarioSinRoles = new Usuario();
        usuarioSinRoles.setId(4L);
        usuarioSinRoles.setUsername("norolesuser");
        usuarioSinRoles.setPassword("nopass123");
        usuarioSinRoles.setRoles(Set.of());

        when(usuarioRepository.findByUsername("norolesuser")).thenReturn(Optional.of(usuarioSinRoles));

        // Act
        CustomUserDetails resultado = (CustomUserDetails) customUserDetailsService.loadUserByUsername("norolesuser");

        // Assert
        assertNotNull(resultado);
        assertEquals(0, resultado.getAuthorities().size());
    }
}
