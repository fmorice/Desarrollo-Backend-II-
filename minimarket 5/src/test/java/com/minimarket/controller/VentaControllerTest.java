package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Rol;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaControllerTest {

    @Mock
    private VentaService ventaService;

    @InjectMocks
    private VentaController ventaController;

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
        producto.setNombre("Laptop Dell");
        producto.setPrecio(1200.0);
        producto.setStock(10);

        detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        detalleVenta.setCantidad(1);
        detalleVenta.setPrecio(1200.0);
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

    // ====== PRUEBAS DE listarVentas() ======
    @Test
    void testListarVentas_ConVentas() {
        // Arrange
        List<Venta> ventasList = Arrays.asList(venta, venta2);
        when(ventaService.findAll()).thenReturn(ventasList);

        // Act
        List<Venta> resultado = ventaController.listarVentas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(ventaService, times(1)).findAll();
    }

    @Test
    void testListarVentas_ListaVacia() {
        // Arrange
        when(ventaService.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Venta> resultado = ventaController.listarVentas();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(ventaService, times(1)).findAll();
    }

    @Test
    void testListarVentas_VentasMultiplesUsuarios() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user");

        Venta ventaUsuario2 = new Venta();
        ventaUsuario2.setId(3L);
        ventaUsuario2.setUsuario(usuario2);
        ventaUsuario2.setFecha(new Date());

        List<Venta> ventasList = Arrays.asList(venta, venta2, ventaUsuario2);
        when(ventaService.findAll()).thenReturn(ventasList);

        // Act
        List<Venta> resultado = ventaController.listarVentas();

        // Assert
        assertEquals(3, resultado.size());
        verify(ventaService, times(1)).findAll();
    }

    // ====== PRUEBAS DE obtenerVentaPorId() ======
    @Test
    void testObtenerVentaPorId_VentaExistente() {
        // Arrange
        when(ventaService.findById(1L)).thenReturn(venta);

        // Act
        ResponseEntity<Venta> resultado = ventaController.obtenerVentaPorId(1L);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals("admin", resultado.getBody().getUsuario().getUsername());
        verify(ventaService, times(1)).findById(1L);
    }

    @Test
    void testObtenerVentaPorId_VentaNoExistente() {
        // Arrange
        when(ventaService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<Venta> resultado = ventaController.obtenerVentaPorId(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(ventaService, times(1)).findById(999L);
    }

    @Test
    void testObtenerVentaPorId_VentaConDetalles() {
        // Arrange
        when(ventaService.findById(1L)).thenReturn(venta);

        // Act
        ResponseEntity<Venta> resultado = ventaController.obtenerVentaPorId(1L);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody().getDetalles());
        assertEquals(1, resultado.getBody().getDetalles().size());
    }

    // ====== PRUEBAS DE guardarVenta() ======
    @Test
    void testGuardarVenta_VentaNueva() {
        // Arrange
        Venta nuevaVenta = new Venta();
        nuevaVenta.setUsuario(usuario);
        nuevaVenta.setFecha(new Date());

        when(ventaService.save(nuevaVenta)).thenReturn(venta);

        // Act
        Venta resultado = ventaController.guardarVenta(nuevaVenta);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("admin", resultado.getUsuario().getUsername());
        verify(ventaService, times(1)).save(nuevaVenta);
    }

    @Test
    void testGuardarVenta_VentaConDetalles() {
        // Arrange
        Venta ventaConDetalles = new Venta();
        ventaConDetalles.setUsuario(usuario);
        ventaConDetalles.setFecha(new Date());
        ventaConDetalles.setDetalles(Arrays.asList(detalleVenta));

        when(ventaService.save(ventaConDetalles)).thenReturn(ventaConDetalles);

        // Act
        Venta resultado = ventaController.guardarVenta(ventaConDetalles);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getDetalles());
        assertEquals(1, resultado.getDetalles().size());
        verify(ventaService, times(1)).save(ventaConDetalles);
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testObtenerVentaPorId_VariasVentas() {
        // Arrange
        when(ventaService.findById(1L)).thenReturn(venta);
        when(ventaService.findById(2L)).thenReturn(venta2);

        // Act
        ResponseEntity<Venta> resultado1 = ventaController.obtenerVentaPorId(1L);
        ResponseEntity<Venta> resultado2 = ventaController.obtenerVentaPorId(2L);

        // Assert
        assertEquals(HttpStatus.OK, resultado1.getStatusCode());
        assertEquals(HttpStatus.OK, resultado2.getStatusCode());
        assertEquals(1L, resultado1.getBody().getId());
        assertEquals(2L, resultado2.getBody().getId());
    }

    @Test
    void testGuardarVenta_VentaSinDetalles() {
        // Arrange
        Venta ventaSinDetalles = new Venta();
        ventaSinDetalles.setId(3L);
        ventaSinDetalles.setUsuario(usuario);
        ventaSinDetalles.setFecha(new Date());

        when(ventaService.save(ventaSinDetalles)).thenReturn(ventaSinDetalles);

        // Act
        Venta resultado = ventaController.guardarVenta(ventaSinDetalles);

        // Assert
        assertNotNull(resultado);
        assertEquals(3L, resultado.getId());
        verify(ventaService, times(1)).save(ventaSinDetalles);
    }

    @Test
    void testListarVentas_VentasMultiplesDetalles() {
        // Arrange
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse");
        producto2.setPrecio(30.0);

        DetalleVenta detalle2 = new DetalleVenta();
        detalle2.setId(2L);
        detalle2.setCantidad(5);
        detalle2.setPrecio(30.0);
        detalle2.setProducto(producto2);

        Venta ventaMultiplesDetalles = new Venta();
        ventaMultiplesDetalles.setId(4L);
        ventaMultiplesDetalles.setUsuario(usuario);
        ventaMultiplesDetalles.setFecha(new Date());
        ventaMultiplesDetalles.setDetalles(Arrays.asList(detalleVenta, detalle2));

        List<Venta> ventasList = Arrays.asList(venta, ventaMultiplesDetalles);
        when(ventaService.findAll()).thenReturn(ventasList);

        // Act
        List<Venta> resultado = ventaController.listarVentas();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals(2, resultado.get(1).getDetalles().size());
    }
}
