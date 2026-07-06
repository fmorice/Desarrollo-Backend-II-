package com.minimarket.security;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.security.model.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CustomUserDetailsTest {

    private CustomUserDetails customUserDetails;
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
        usuario.setRoles(Set.of(rolAdmin, rolUser));

        customUserDetails = new CustomUserDetails(usuario);
    }

    // ====== PRUEBAS DEL CONSTRUCTOR ======
    @Test
    void testConstructor_InicializaCorrectamente() {
        // Act & Assert
        assertNotNull(customUserDetails);
        assertEquals("admin", customUserDetails.getUsername());
        assertEquals("password123", customUserDetails.getPassword());
    }

    // ====== PRUEBAS DE getAuthorities() ======
    @Test
    void testGetAuthorities_RetornaRoles() {
        // Act
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> "ADMIN".equals(auth.getAuthority())));
        assertTrue(authorities.stream()
                .anyMatch(auth -> "USER".equals(auth.getAuthority())));
    }

    @Test
    void testGetAuthorities_ConUnRol() {
        // Arrange
        Usuario usuarioUnRol = new Usuario();
        usuarioUnRol.setId(2L);
        usuarioUnRol.setUsername("user");
        usuarioUnRol.setPassword("pass123");
        usuarioUnRol.setRoles(Set.of(rolUser));

        CustomUserDetails customUserDetailsUnRol = new CustomUserDetails(usuarioUnRol);

        // Act
        Collection<? extends GrantedAuthority> authorities = customUserDetailsUnRol.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream()
                .anyMatch(auth -> "USER".equals(auth.getAuthority())));
    }

    @Test
    void testGetAuthorities_ConMultiplesRoles() {
        // Act
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        // Assert
        List<String> roleNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        
        assertTrue(roleNames.contains("ADMIN"));
        assertTrue(roleNames.contains("USER"));
    }

    // ====== PRUEBAS DE getPassword() ======
    @Test
    void testGetPassword_RetornaPasswordCorrecta() {
        // Act
        String password = customUserDetails.getPassword();

        // Assert
        assertNotNull(password);
        assertEquals("password123", password);
    }

    @Test
    void testGetPassword_NoRetornaNula() {
        // Act
        String password = customUserDetails.getPassword();

        // Assert
        assertNotNull(password);
    }

    // ====== PRUEBAS DE getUsername() ======
    @Test
    void testGetUsername_RetornaUsernameCorrecta() {
        // Act
        String username = customUserDetails.getUsername();

        // Assert
        assertNotNull(username);
        assertEquals("admin", username);
    }

    @Test
    void testGetUsername_NoRetornaNula() {
        // Act
        String username = customUserDetails.getUsername();

        // Assert
        assertNotNull(username);
    }

    // ====== PRUEBAS DE isAccountNonExpired() ======
    @Test
    void testIsAccountNonExpired_RetornaTrue() {
        // Act
        boolean resultado = customUserDetails.isAccountNonExpired();

        // Assert
        assertTrue(resultado);
    }

    // ====== PRUEBAS DE isAccountNonLocked() ======
    @Test
    void testIsAccountNonLocked_RetornaTrue() {
        // Act
        boolean resultado = customUserDetails.isAccountNonLocked();

        // Assert
        assertTrue(resultado);
    }

    // ====== PRUEBAS DE isCredentialsNonExpired() ======
    @Test
    void testIsCredentialsNonExpired_RetornaTrue() {
        // Act
        boolean resultado = customUserDetails.isCredentialsNonExpired();

        // Assert
        assertTrue(resultado);
    }

    // ====== PRUEBAS DE isEnabled() ======
    @Test
    void testIsEnabled_RetornaTrue() {
        // Act
        boolean resultado = customUserDetails.isEnabled();

        // Assert
        assertTrue(resultado);
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testTodosLosBoolearosRetornanTrue() {
        // Act & Assert
        assertTrue(customUserDetails.isAccountNonExpired());
        assertTrue(customUserDetails.isAccountNonLocked());
        assertTrue(customUserDetails.isCredentialsNonExpired());
        assertTrue(customUserDetails.isEnabled());
    }

    @Test
    void testCustomUserDetailsConUsuarioDiferente() {
        // Arrange
        Usuario usuarioDiferente = new Usuario();
        usuarioDiferente.setId(3L);
        usuarioDiferente.setUsername("otheradmin");
        usuarioDiferente.setPassword("otherpass456");
        usuarioDiferente.setRoles(Set.of(rolAdmin));

        CustomUserDetails customUserDetailsDiferente = new CustomUserDetails(usuarioDiferente);

        // Act & Assert
        assertEquals("otheradmin", customUserDetailsDiferente.getUsername());
        assertEquals("otherpass456", customUserDetailsDiferente.getPassword());
        assertNotEquals("admin", customUserDetailsDiferente.getUsername());
    }

    @Test
    void testGetAuthoritiesRetornaColeccionNoVacia() {
        // Act
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();

        // Assert
        assertNotNull(authorities);
        assertFalse(authorities.isEmpty());
        assertTrue(authorities.size() > 0);
    }

    @Test
    void testUsuarioConSoloUnRolAdmin() {
        // Arrange
        Usuario usuarioSoloAdmin = new Usuario();
        usuarioSoloAdmin.setId(4L);
        usuarioSoloAdmin.setUsername("pureAdmin");
        usuarioSoloAdmin.setPassword("adminonly");
        usuarioSoloAdmin.setRoles(Set.of(rolAdmin));

        CustomUserDetails customUserDetailsSoloAdmin = new CustomUserDetails(usuarioSoloAdmin);

        // Act & Assert
        Collection<? extends GrantedAuthority> authorities = customUserDetailsSoloAdmin.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ADMIN", authorities.stream().findFirst().get().getAuthority());
    }

    @Test
    void testConsistenciaDeAuthoritiesYRoles() {
        // Act
        Collection<? extends GrantedAuthority> authorities = customUserDetails.getAuthorities();
        Set<Rol> roles = usuario.getRoles();

        // Assert
        assertEquals(roles.size(), authorities.size());
        roles.forEach(rol ->
            assertTrue(authorities.stream()
                    .anyMatch(auth -> rol.getNombre().equals(auth.getAuthority())))
        );
    }
}
