package com.minimarket.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioEntityTest {

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
    }

    // ====== PRUEBAS DE ID ======
    @Test
    void testSetId_GetId() {
        // Act
        usuario.setId(1L);

        // Assert
        assertEquals(1L, usuario.getId());
    }

    @Test
    void testGetId_NoAsignado() {
        // Act & Assert
        assertNull(usuario.getId());
    }

    @Test
    void testSetId_IdLargo() {
        // Act
        usuario.setId(999999L);

        // Assert
        assertEquals(999999L, usuario.getId());
    }

    // ====== PRUEBAS DE USERNAME ======
    @Test
    void testSetUsername_GetUsername() {
        // Act
        usuario.setUsername("admin");

        // Assert
        assertEquals("admin", usuario.getUsername());
    }

    @Test
    void testGetUsername_NoAsignado() {
        // Act & Assert
        assertNull(usuario.getUsername());
    }

    @Test
    void testSetUsername_UsernameConEspacios() {
        // Act
        usuario.setUsername("admin user");

        // Assert
        assertEquals("admin user", usuario.getUsername());
    }

    @Test
    void testSetUsername_UsernameVacio() {
        // Act
        usuario.setUsername("");

        // Assert
        assertEquals("", usuario.getUsername());
    }

    // ====== PRUEBAS DE PASSWORD ======
    @Test
    void testSetPassword_GetPassword() {
        // Act
        usuario.setPassword("password123");

        // Assert
        assertEquals("password123", usuario.getPassword());
    }

    @Test
    void testGetPassword_NoAsignado() {
        // Act & Assert
        assertNull(usuario.getPassword());
    }

    @Test
    void testSetPassword_PasswordFuerte() {
        // Act
        usuario.setPassword("P@ssw0rd!Fuerte123");

        // Assert
        assertEquals("P@ssw0rd!Fuerte123", usuario.getPassword());
    }

    // ====== PRUEBAS DE ROLES ======
    @Test
    void testSetRoles_GetRoles() {
        // Act
        usuario.setRoles(Set.of(rolAdmin));

        // Assert
        assertNotNull(usuario.getRoles());
        assertEquals(1, usuario.getRoles().size());
        assertTrue(usuario.getRoles().contains(rolAdmin));
    }

    @Test
    void testGetRoles_NoAsignado() {
        // Act & Assert
        assertNull(usuario.getRoles());
    }

    @Test
    void testSetRoles_MultiplicasRoles() {
        // Act
        usuario.setRoles(Set.of(rolAdmin, rolUser));

        // Assert
        assertEquals(2, usuario.getRoles().size());
        assertTrue(usuario.getRoles().contains(rolAdmin));
        assertTrue(usuario.getRoles().contains(rolUser));
    }

    @Test
    void testSetRoles_RolesVacio() {
        // Act
        usuario.setRoles(Set.of());

        // Assert
        assertNotNull(usuario.getRoles());
        assertTrue(usuario.getRoles().isEmpty());
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testUsuarioCompleto() {
        // Act
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setPassword("pass123");
        usuario.setRoles(Set.of(rolAdmin, rolUser));

        // Assert
        assertEquals(1L, usuario.getId());
        assertEquals("admin", usuario.getUsername());
        assertEquals("pass123", usuario.getPassword());
        assertEquals(2, usuario.getRoles().size());
    }

    @Test
    void testSetIdMultiplicasVeces() {
        // Act
        usuario.setId(1L);
        usuario.setId(2L);
        usuario.setId(3L);

        // Assert
        assertEquals(3L, usuario.getId());
    }

    @Test
    void testSetUsernameMultiplicasVeces() {
        // Act
        usuario.setUsername("user1");
        usuario.setUsername("user2");
        usuario.setUsername("user3");

        // Assert
        assertEquals("user3", usuario.getUsername());
    }

    @Test
    void testSetRolesMultiplicasVeces() {
        // Act
        usuario.setRoles(Set.of(rolAdmin));
        usuario.setRoles(Set.of(rolUser));
        usuario.setRoles(Set.of(rolAdmin, rolUser));

        // Assert
        assertEquals(2, usuario.getRoles().size());
    }
}
