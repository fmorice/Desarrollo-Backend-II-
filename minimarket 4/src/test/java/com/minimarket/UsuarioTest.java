package com.minimarket;

import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    // PRUEBA 1: Validar creación básica
    @Test
    public void testCrearUsuario() {
        Set<Rol> roles = Set.of(new Rol("ADMIN"));
        Usuario usuario = new Usuario();
        usuario.setUsername("adminUser");
        usuario.setPassword("securePassword123");
        usuario.setRoles(roles);

        assertNotNull(usuario);
        assertEquals("adminUser", usuario.getUsername());
        assertEquals("securePassword123", usuario.getPassword());
        assertEquals(1, usuario.getRoles().size());
        assertTrue(usuario.getRoles().stream().anyMatch(role -> role.getNombre().equals("ADMIN")));
    }

    // PRUEBA 2: Validar igualdad de objetos
    @Test
    public void testEquals() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setUsername("adminUser");
        usuario1.setPassword("securePassword123");

        Usuario usuario2 = new Usuario();
        usuario2.setId(1L);
        usuario2.setUsername("adminUser");
        usuario2.setPassword("securePassword123");

        assertEquals(usuario1.getId(), usuario2.getId());
        assertEquals(usuario1.getUsername(), usuario2.getUsername());
        assertEquals(usuario1.getPassword(), usuario2.getPassword());
    }

    // PRUEBA 3: Validar que se pueden agregar múltiples roles
    @Test
    public void testAgregarRoles() {
        Usuario usuario = new Usuario();
        usuario.setUsername("user1");
        usuario.setPassword("password");

        Rol roleUser = new Rol("USER");
        Rol roleAdmin = new Rol("ADMIN");
        usuario.setRoles(Set.of(roleUser, roleAdmin));

        assertEquals(2, usuario.getRoles().size());
        assertTrue(usuario.getRoles().stream().anyMatch(role -> role.getNombre().equals("USER")));
        assertTrue(usuario.getRoles().stream().anyMatch(role -> role.getNombre().equals("ADMIN")));
    }

    // PRUEBA 4 (NUEVA): Validar que un usuario sin datos arroje error (o campos nulos)
    @Test
    public void testUsuario_SinDatosRequeridos_Falla() {
        Usuario usuario = new Usuario();

        // No le asignamos username ni password
        assertNull(usuario.getUsername(), "El username debería ser nulo al inicializar");
        assertNull(usuario.getPassword(), "El password debería ser nulo al inicializar");

        // Verificamos que si intentamos obtener la colección de roles antes de asignarla, puede ser nula o vacía
        assertNull(usuario.getRoles(), "La lista de roles debería ser nula si no se ha inicializado");
    }
}