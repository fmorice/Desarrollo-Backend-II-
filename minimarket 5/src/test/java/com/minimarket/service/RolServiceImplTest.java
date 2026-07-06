package com.minimarket.service;

import com.minimarket.entity.Rol;
import com.minimarket.repository.RolRepository;
import com.minimarket.service.impl.RolServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RolServiceImplTest {

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private RolServiceImpl rolService;

    private Rol rolAdmin;
    private Rol rolUser;

    @BeforeEach
    void setUp() {
        rolAdmin = new Rol("ADMIN");
        rolAdmin.setId(1L);

        rolUser = new Rol("USER");
        rolUser.setId(2L);
    }

    // ====== PRUEBAS DE findByNombre() ======
    @Test
    void testFindByNombre_RolExistente() {
        // Arrange
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rolAdmin));

        // Act
        Optional<Rol> resultado = rolService.findByNombre("ADMIN");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("ADMIN", resultado.get().getNombre());
        assertEquals(1L, resultado.get().getId());
        verify(rolRepository, times(1)).findByNombre("ADMIN");
    }

    @Test
    void testFindByNombre_RolNoExistente() {
        // Arrange
        when(rolRepository.findByNombre("SUPERADMIN")).thenReturn(Optional.empty());

        // Act
        Optional<Rol> resultado = rolService.findByNombre("SUPERADMIN");

        // Assert
        assertFalse(resultado.isPresent());
        assertTrue(resultado.isEmpty());
        verify(rolRepository, times(1)).findByNombre("SUPERADMIN");
    }

    @Test
    void testFindByNombre_RolUser() {
        // Arrange
        when(rolRepository.findByNombre("USER")).thenReturn(Optional.of(rolUser));

        // Act
        Optional<Rol> resultado = rolService.findByNombre("USER");

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals("USER", resultado.get().getNombre());
        assertEquals(2L, resultado.get().getId());
        verify(rolRepository, times(1)).findByNombre("USER");
    }

    @Test
    void testFindByNombre_CaseSensitive() {
        // Arrange
        when(rolRepository.findByNombre("admin")).thenReturn(Optional.empty());
        when(rolRepository.findByNombre("ADMIN")).thenReturn(Optional.of(rolAdmin));

        // Act & Assert
        assertTrue(rolService.findByNombre("ADMIN").isPresent());
        assertFalse(rolService.findByNombre("admin").isPresent());
    }

    @Test
    void testFindByNombre_NullInput() {
        // Arrange
        when(rolRepository.findByNombre(null)).thenReturn(Optional.empty());

        // Act
        Optional<Rol> resultado = rolService.findByNombre(null);

        // Assert
        assertFalse(resultado.isPresent());
        verify(rolRepository, times(1)).findByNombre(null);
    }
}
