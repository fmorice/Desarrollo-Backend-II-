package com.minimarket.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RolEntityTest {

    private Rol rol;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        rol = new Rol("DEFAULT_ROLE");
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
    }

    // ====== PRUEBAS DEL CONSTRUCTOR SIN PARÁMETROS ======
    @Test
    void testConstructorSinParametros() {
        // Act
        Rol rolVacio = new Rol("TEMP_ROLE");

        // Assert
        assertNotNull(rolVacio);
        assertNotNull(rolVacio.getNombre());
        assertEquals("TEMP_ROLE", rolVacio.getNombre());
    }

    // ====== PRUEBAS DEL CONSTRUCTOR CON NOMBRE ======
    @Test
    void testConstructorConNombre() {
        // Act
        Rol rolAdmin = new Rol("ADMIN");

        // Assert
        assertNotNull(rolAdmin);
        assertEquals("ADMIN", rolAdmin.getNombre());
        assertNull(rolAdmin.getId());
    }

    @Test
    void testConstructorConNombre_RolUser() {
        // Act
        Rol rolUser = new Rol("USER");

        // Assert
        assertEquals("USER", rolUser.getNombre());
    }

    // ====== PRUEBAS DEL CONSTRUCTOR CON TODOS LOS PARÁMETROS ======
    @Test
    void testConstructorConTodosLosParametros() {
        // Arrange
        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario);

        // Act
        Rol rol = new Rol(1L, "ADMIN", usuarios);

        // Assert
        assertEquals(1L, rol.getId());
        assertEquals("ADMIN", rol.getNombre());
        assertEquals(1, rol.getUsuarios().size());
        assertTrue(rol.getUsuarios().contains(usuario));
    }

    // ====== PRUEBAS DE ID ======
    @Test
    void testSetId_GetId() {
        // Act
        rol.setId(5L);

        // Assert
        assertEquals(5L, rol.getId());
    }

    @Test
    void testGetId_NoAsignado() {
        // Act & Assert
        assertNull(rol.getId());
    }

    // ====== PRUEBAS DE NOMBRE ======
    @Test
    void testSetNombre_GetNombre() {
        // Act
        rol.setNombre("ADMIN");

        // Assert
        assertEquals("ADMIN", rol.getNombre());
    }

    @Test
    void testGetNombre_Asignado() {
        // Act & Assert
        assertNotNull(rol.getNombre());
        assertEquals("DEFAULT_ROLE", rol.getNombre());
    }

    @Test
    void testSetNombre_DiferentesNombres() {
        // Act & Assert
        rol.setNombre("ADMIN");
        assertEquals("ADMIN", rol.getNombre());

        rol.setNombre("USER");
        assertEquals("USER", rol.getNombre());

        rol.setNombre("GUEST");
        assertEquals("GUEST", rol.getNombre());
    }

    // ====== PRUEBAS DE USUARIOS ======
    @Test
    void testSetUsuarios_GetUsuarios() {
        // Arrange
        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario);

        // Act
        rol.setUsuarios(usuarios);

        // Assert
        assertNotNull(rol.getUsuarios());
        assertEquals(1, rol.getUsuarios().size());
        assertTrue(rol.getUsuarios().contains(usuario));
    }

    @Test
    void testGetUsuarios_NoAsignado() {
        // Act & Assert
        assertNull(rol.getUsuarios());
    }

    @Test
    void testSetUsuarios_MultiplicosUsuarios() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user");

        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario);
        usuarios.add(usuario2);

        // Act
        rol.setUsuarios(usuarios);

        // Assert
        assertEquals(2, rol.getUsuarios().size());
        assertTrue(rol.getUsuarios().contains(usuario));
        assertTrue(rol.getUsuarios().contains(usuario2));
    }

    @Test
    void testSetUsuarios_ConjuntoVacio() {
        // Act
        rol.setUsuarios(new HashSet<>());

        // Assert
        assertNotNull(rol.getUsuarios());
        assertTrue(rol.getUsuarios().isEmpty());
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testRolCompleto() {
        // Arrange
        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario);

        // Act
        rol.setId(1L);
        rol.setNombre("ADMIN");
        rol.setUsuarios(usuarios);

        // Assert
        assertEquals(1L, rol.getId());
        assertEquals("ADMIN", rol.getNombre());
        assertEquals(1, rol.getUsuarios().size());
    }

    @Test
    void testConstructorConParametros_VerificaValores() {
        // Arrange
        Set<Usuario> usuarios = new HashSet<>();
        usuarios.add(usuario);

        // Act
        Rol rol = new Rol(2L, "USER", usuarios);

        // Assert
        assertNotNull(rol);
        assertEquals(2L, rol.getId());
        assertEquals("USER", rol.getNombre());
        assertEquals(1, rol.getUsuarios().size());
    }

    @Test
    void testSetNombreMultiplicasVeces() {
        // Act
        rol.setNombre("ROLE1");
        rol.setNombre("ROLE2");
        rol.setNombre("ROLE3");

        // Assert
        assertEquals("ROLE3", rol.getNombre());
    }

    @Test
    void testSetUsuariosMultiplicasVeces() {
        // Arrange
        Set<Usuario> usuarios1 = new HashSet<>();
        usuarios1.add(usuario);

        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        Set<Usuario> usuarios2 = new HashSet<>();
        usuarios2.add(usuario2);

        // Act
        rol.setUsuarios(usuarios1);
        assertEquals(1, rol.getUsuarios().size());
        
        rol.setUsuarios(usuarios2);

        // Assert
        assertEquals(1, rol.getUsuarios().size());
        assertTrue(rol.getUsuarios().contains(usuario2));
    }

    @Test
    void testRolAdminEspecifico() {
        // Act
        Rol rolAdmin = new Rol("ADMIN");
        rolAdmin.setId(1L);

        // Assert
        assertEquals(1L, rolAdmin.getId());
        assertEquals("ADMIN", rolAdmin.getNombre());
    }

    @Test
    void testRolUserEspecifico() {
        // Act
        Rol rolUser = new Rol("USER");
        rolUser.setId(2L);

        // Assert
        assertEquals(2L, rolUser.getId());
        assertEquals("USER", rolUser.getNombre());
    }
}
