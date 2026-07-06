package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaServiceImplTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService;

    private Venta venta;
    private Venta venta2;
    private Usuario usuario;
    private DetalleVenta detalleVenta;
    private Producto producto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");
        usuario.setPassword("password123");
        usuario.setRoles(Set.of(new Rol("ADMIN")));

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(1000.0);
        producto.setStock(5);

        detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(1000.0);
        detalleVenta.setProducto(producto);

        venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(usuario);
        venta.setFecha(new Date());
        venta.setDetalles(Arrays.asList(detalleVenta));

        venta2 = new Venta();
        venta2.setId(2L);
        venta2.setUsuario(usuario);
        venta2.setFecha(new Date());
    }

    // ====== PRUEBAS DE findAll() ======
    @Test
    void testFindAll_ConVentas() {
        // Arrange
        List<Venta> ventasList = Arrays.asList(venta, venta2);
        when(ventaRepository.findAll()).thenReturn(ventasList);

        // Act
        List<Venta> resultado = ventaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        // Arrange
        when(ventaRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Venta> resultado = ventaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        verify(ventaRepository, times(1)).findAll();
    }

    // ====== PRUEBAS DE findById() ======
    @Test
    void testFindById_VentaExistente() {
        // Arrange
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        // Act
        Venta resultado = ventaService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("admin", resultado.getUsuario().getUsername());
        assertEquals(1, resultado.getDetalles().size());
        verify(ventaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_VentaNoExistente() {
        // Arrange
        when(ventaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Venta resultado = ventaService.findById(999L);

        // Assert
        assertNull(resultado);
        verify(ventaRepository, times(1)).findById(999L);
    }

    @Test
    void testFindById_RetornaNull() {
        // Arrange
        when(ventaRepository.findById(500L)).thenReturn(Optional.empty());

        // Act
        Venta resultado = ventaService.findById(500L);

        // Assert
        assertNull(resultado);
        assertTrue(resultado == null);
    }

    // ====== PRUEBAS DE save() ======
    @Test
    void testSave_VentaNueva() {
        // Arrange
        Venta nuevaVenta = new Venta();
        nuevaVenta.setUsuario(usuario);
        nuevaVenta.setFecha(new Date());

        when(ventaRepository.save(nuevaVenta)).thenReturn(venta);

        // Act
        Venta resultado = ventaService.save(nuevaVenta);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("admin", resultado.getUsuario().getUsername());
        verify(ventaRepository, times(1)).save(nuevaVenta);
    }

    @Test
    void testSave_VentaConDetalles() {
        // Arrange
        Venta ventaConDetalles = new Venta();
        ventaConDetalles.setId(3L);
        ventaConDetalles.setUsuario(usuario);
        ventaConDetalles.setFecha(new Date());
        ventaConDetalles.setDetalles(Arrays.asList(detalleVenta));

        when(ventaRepository.save(ventaConDetalles)).thenReturn(ventaConDetalles);

        // Act
        Venta resultado = ventaService.save(ventaConDetalles);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        assertNotNull(resultado.getDetalles());
        assertEquals(1, resultado.getDetalles().size());
        verify(ventaRepository, times(1)).save(ventaConDetalles);
    }

    // ====== PRUEBAS DE findByUsuarioId() ======
    @Test
    void testFindByUsuarioId_ConVentas() {
        // Arrange
        List<Venta> ventasUsuario = Arrays.asList(venta, venta2);
        when(ventaRepository.findByUsuarioId(1L)).thenReturn(ventasUsuario);

        // Act
        List<Venta> resultado = ventaService.findByUsuarioId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getUsuario().getId());
        assertEquals(1L, resultado.get(1).getUsuario().getId());
        verify(ventaRepository, times(1)).findByUsuarioId(1L);
    }

    @Test
    void testFindByUsuarioId_SinVentas() {
        // Arrange
        when(ventaRepository.findByUsuarioId(999L)).thenReturn(new ArrayList<>());

        // Act
        List<Venta> resultado = ventaService.findByUsuarioId(999L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        assertEquals(0, resultado.size());
        verify(ventaRepository, times(1)).findByUsuarioId(999L);
    }

    @Test
    void testFindByUsuarioId_ListaVacia() {
        // Arrange
        when(ventaRepository.findByUsuarioId(2L)).thenReturn(new ArrayList<>());

        // Act
        List<Venta> resultado = ventaService.findByUsuarioId(2L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(ventaRepository, times(1)).findByUsuarioId(2L);
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testVentaConFecha() {
        // Arrange
        Date fechaVenta = new Date(System.currentTimeMillis() - 86400000); // Hace 1 día
        Venta ventaAntigua = new Venta();
        ventaAntigua.setId(4L);
        ventaAntigua.setUsuario(usuario);
        ventaAntigua.setFecha(fechaVenta);

        when(ventaRepository.save(ventaAntigua)).thenReturn(ventaAntigua);

        // Act
        Venta resultado = ventaService.save(ventaAntigua);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getFecha());
        assertEquals(fechaVenta, resultado.getFecha());
    }

    @Test
    void testVentaMultiplesUsuarios() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user");
        usuario2.setPassword("pass123");

        Venta ventaUsuario2 = new Venta();
        ventaUsuario2.setId(5L);
        ventaUsuario2.setUsuario(usuario2);
        ventaUsuario2.setFecha(new Date());

        when(ventaRepository.findByUsuarioId(2L)).thenReturn(Arrays.asList(ventaUsuario2));

        // Act
        List<Venta> resultado = ventaService.findByUsuarioId(2L);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(2L, resultado.get(0).getUsuario().getId());
    }
}
