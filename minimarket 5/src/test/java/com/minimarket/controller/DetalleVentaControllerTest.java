package com.minimarket.controller;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.entity.Venta;
import com.minimarket.service.DetalleVentaService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleVentaControllerTest {

    @Mock
    private DetalleVentaService detalleVentaService;

    @InjectMocks
    private DetalleVentaController detalleVentaController;

    private DetalleVenta detalleVenta;
    private DetalleVenta detalleVenta2;
    private Venta venta;
    private Producto producto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Mouse Logitech");
        producto.setPrecio(25.0);
        producto.setStock(100);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Teclado Mecánico");
        producto2.setPrecio(80.0);
        producto2.setStock(50);

        venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(usuario);
        venta.setFecha(new Date());

        detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(3);
        detalleVenta.setPrecio(25.0);

        detalleVenta2 = new DetalleVenta();
        detalleVenta2.setId(2L);
        detalleVenta2.setVenta(venta);
        detalleVenta2.setProducto(producto2);
        detalleVenta2.setCantidad(1);
        detalleVenta2.setPrecio(80.0);
    }

    // ====== PRUEBAS DE listarDetalleVentas() ======
    @Test
    void testListarDetalleVentas_ConDetalles() {
        // Arrange
        List<DetalleVenta> detallesList = Arrays.asList(detalleVenta, detalleVenta2);
        when(detalleVentaService.findAll()).thenReturn(detallesList);

        // Act
        List<DetalleVenta> resultado = detalleVentaController.listarDetalleVentas();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(detalleVentaService, times(1)).findAll();
    }

    @Test
    void testListarDetalleVentas_ListaVacia() {
        // Arrange
        when(detalleVentaService.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<DetalleVenta> resultado = detalleVentaController.listarDetalleVentas();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(detalleVentaService, times(1)).findAll();
    }

    // ====== PRUEBAS DE obtenerDetalleVentaPorId() ======
    @Test
    void testObtenerDetalleVentaPorId_DetalleExistente() {
        // Arrange
        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta);

        // Act
        ResponseEntity<DetalleVenta> resultado = detalleVentaController.obtenerDetalleVentaPorId(1L);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(1L, resultado.getBody().getId());
        assertEquals(3, resultado.getBody().getCantidad());
        verify(detalleVentaService, times(1)).findById(1L);
    }

    @Test
    void testObtenerDetalleVentaPorId_DetalleNoExistente() {
        // Arrange
        when(detalleVentaService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<DetalleVenta> resultado = detalleVentaController.obtenerDetalleVentaPorId(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(detalleVentaService, times(1)).findById(999L);
    }

    // ====== PRUEBAS DE guardarDetalleVenta() ======
    @Test
    void testGuardarDetalleVenta_DetalleNuevo() {
        // Arrange
        DetalleVenta nuevoDetalle = new DetalleVenta();
        nuevoDetalle.setVenta(venta);
        nuevoDetalle.setProducto(producto);
        nuevoDetalle.setCantidad(5);
        nuevoDetalle.setPrecio(25.0);

        when(detalleVentaService.save(nuevoDetalle)).thenReturn(detalleVenta);

        // Act
        DetalleVenta resultado = detalleVentaController.guardarDetalleVenta(nuevoDetalle);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(3, resultado.getCantidad());
        verify(detalleVentaService, times(1)).save(nuevoDetalle);
    }

    @Test
    void testGuardarDetalleVenta_DetalleConPrecio() {
        // Arrange
        DetalleVenta detalleConPrecio = new DetalleVenta();
        detalleConPrecio.setVenta(venta);
        detalleConPrecio.setProducto(producto);
        detalleConPrecio.setCantidad(10);
        detalleConPrecio.setPrecio(25.0);

        when(detalleVentaService.save(detalleConPrecio)).thenReturn(detalleConPrecio);

        // Act
        DetalleVenta resultado = detalleVentaController.guardarDetalleVenta(detalleConPrecio);

        // Assert
        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
        assertEquals(25.0, resultado.getPrecio());
        verify(detalleVentaService, times(1)).save(detalleConPrecio);
    }

    // ====== PRUEBAS DE actualizarDetalleVenta() ======
    @Test
    void testActualizarDetalleVenta_DetalleExistente() {
        // Arrange
        DetalleVenta detalleActualizado = new DetalleVenta();
        detalleActualizado.setCantidad(5);
        detalleActualizado.setPrecio(30.0);
        detalleActualizado.setProducto(producto);

        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta);
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalleActualizado);

        // Act
        ResponseEntity<DetalleVenta> resultado = detalleVentaController.actualizarDetalleVenta(1L, detalleActualizado);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertNotNull(resultado.getBody());
        assertEquals(5, resultado.getBody().getCantidad());
        verify(detalleVentaService, times(1)).findById(1L);
        verify(detalleVentaService, times(1)).save(any(DetalleVenta.class));
    }

    @Test
    void testActualizarDetalleVenta_DetalleNoExistente() {
        // Arrange
        DetalleVenta detalleActualizado = new DetalleVenta();
        detalleActualizado.setCantidad(5);
        detalleActualizado.setPrecio(30.0);

        when(detalleVentaService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<DetalleVenta> resultado = detalleVentaController.actualizarDetalleVenta(999L, detalleActualizado);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(detalleVentaService, times(1)).findById(999L);
        verify(detalleVentaService, never()).save(any());
    }

    // ====== PRUEBAS DE eliminarDetalleVenta() ======
    @Test
    void testEliminarDetalleVenta_DetalleExistente() {
        // Arrange
        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta);
        doNothing().when(detalleVentaService).deleteById(1L);

        // Act
        ResponseEntity<Void> resultado = detalleVentaController.eliminarDetalleVenta(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(detalleVentaService, times(1)).findById(1L);
        verify(detalleVentaService, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarDetalleVenta_DetalleNoExistente() {
        // Arrange
        when(detalleVentaService.findById(999L)).thenReturn(null);

        // Act
        ResponseEntity<Void> resultado = detalleVentaController.eliminarDetalleVenta(999L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
        assertNull(resultado.getBody());
        verify(detalleVentaService, times(1)).findById(999L);
        verify(detalleVentaService, never()).deleteById(any());
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testObtenerDetalleVentaPorId_VariosDetalles() {
        // Arrange
        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta);
        when(detalleVentaService.findById(2L)).thenReturn(detalleVenta2);

        // Act
        ResponseEntity<DetalleVenta> resultado1 = detalleVentaController.obtenerDetalleVentaPorId(1L);
        ResponseEntity<DetalleVenta> resultado2 = detalleVentaController.obtenerDetalleVentaPorId(2L);

        // Assert
        assertEquals(HttpStatus.OK, resultado1.getStatusCode());
        assertEquals(HttpStatus.OK, resultado2.getStatusCode());
        assertEquals(1L, resultado1.getBody().getId());
        assertEquals(2L, resultado2.getBody().getId());
    }

    @Test
    void testActualizarDetalleVenta_MantieneId() {
        // Arrange
        DetalleVenta detalleActualizado = new DetalleVenta();
        detalleActualizado.setId(1L);
        detalleActualizado.setCantidad(5);
        detalleActualizado.setPrecio(30.0);

        when(detalleVentaService.findById(1L)).thenReturn(detalleVenta);
        when(detalleVentaService.save(any(DetalleVenta.class))).thenReturn(detalleActualizado);

        // Act
        ResponseEntity<DetalleVenta> resultado = detalleVentaController.actualizarDetalleVenta(1L, detalleActualizado);

        // Assert
        assertEquals(HttpStatus.OK, resultado.getStatusCode());
        assertEquals(1L, resultado.getBody().getId());
    }

    @Test
    void testListarDetalleVentas_DetallesDiferentes() {
        // Arrange
        Producto producto3 = new Producto();
        producto3.setId(3L);
        producto3.setNombre("Monitor");
        producto3.setPrecio(150.0);

        DetalleVenta detalleVenta3 = new DetalleVenta();
        detalleVenta3.setId(3L);
        detalleVenta3.setVenta(venta);
        detalleVenta3.setProducto(producto3);
        detalleVenta3.setCantidad(1);
        detalleVenta3.setPrecio(150.0);

        List<DetalleVenta> detallesList = Arrays.asList(detalleVenta, detalleVenta2, detalleVenta3);
        when(detalleVentaService.findAll()).thenReturn(detallesList);

        // Act
        List<DetalleVenta> resultado = detalleVentaController.listarDetalleVentas();

        // Assert
        assertEquals(3, resultado.size());
        assertEquals(25.0, resultado.get(0).getPrecio());
        assertEquals(80.0, resultado.get(1).getPrecio());
        assertEquals(150.0, resultado.get(2).getPrecio());
    }
}
