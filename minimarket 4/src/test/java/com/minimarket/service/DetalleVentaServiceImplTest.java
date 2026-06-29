package com.minimarket.service;

import com.minimarket.entity.DetalleVenta;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Venta;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.DetalleVentaRepository;
import com.minimarket.service.impl.DetalleVentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DetalleVentaServiceImplTest {

    @Mock
    private DetalleVentaRepository detalleVentaRepository;

    @InjectMocks
    private DetalleVentaServiceImpl detalleVentaService;

    private DetalleVenta detalleVenta;
    private DetalleVenta detalleVenta2;
    private Venta venta;
    private Producto producto;

    @BeforeEach
    void setUp() {
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

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");

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

    // ====== PRUEBAS DE findAll() ======
    @Test
    void testFindAll_ConDetalles() {
        // Arrange
        List<DetalleVenta> detallesList = Arrays.asList(detalleVenta, detalleVenta2);
        when(detalleVentaRepository.findAll()).thenReturn(detallesList);

        // Act
        List<DetalleVenta> resultado = detalleVentaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getId());
        assertEquals(2L, resultado.get(1).getId());
        verify(detalleVentaRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        // Arrange
        when(detalleVentaRepository.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<DetalleVenta> resultado = detalleVentaService.findAll();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(detalleVentaRepository, times(1)).findAll();
    }

    // ====== PRUEBAS DE findById() ======
    @Test
    void testFindById_DetalleExistente() {
        // Arrange
        when(detalleVentaRepository.findById(1L)).thenReturn(Optional.of(detalleVenta));

        // Act
        DetalleVenta resultado = detalleVentaService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(3, resultado.getCantidad());
        assertEquals("Mouse Logitech", resultado.getProducto().getNombre());
        verify(detalleVentaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_DetalleNoExistente() {
        // Arrange
        when(detalleVentaRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        DetalleVenta resultado = detalleVentaService.findById(999L);

        // Assert
        assertNull(resultado);
        verify(detalleVentaRepository, times(1)).findById(999L);
    }

    // ====== PRUEBAS DE save() ======
    @Test
    void testSave_DetalleNuevo() {
        // Arrange
        DetalleVenta nuevoDetalle = new DetalleVenta();
        nuevoDetalle.setVenta(venta);
        nuevoDetalle.setProducto(producto);
        nuevoDetalle.setCantidad(5);
        nuevoDetalle.setPrecio(25.0);

        when(detalleVentaRepository.save(nuevoDetalle)).thenReturn(detalleVenta);

        // Act
        DetalleVenta resultado = detalleVentaService.save(nuevoDetalle);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(3, resultado.getCantidad());
        verify(detalleVentaRepository, times(1)).save(nuevoDetalle);
    }

    @Test
    void testSave_DetalleConPrecioCalculado() {
        // Arrange
        DetalleVenta detalleConPrecio = new DetalleVenta();
        detalleConPrecio.setId(3L);
        detalleConPrecio.setVenta(venta);
        detalleConPrecio.setProducto(producto);
        detalleConPrecio.setCantidad(10);
        detalleConPrecio.setPrecio(25.0); // Total será 250.0

        when(detalleVentaRepository.save(detalleConPrecio)).thenReturn(detalleConPrecio);

        // Act
        DetalleVenta resultado = detalleVentaService.save(detalleConPrecio);

        // Assert
        assertNotNull(resultado);
        assertEquals(10, resultado.getCantidad());
        assertEquals(25.0, resultado.getPrecio());
        verify(detalleVentaRepository, times(1)).save(detalleConPrecio);
    }

    // ====== PRUEBAS DE deleteById() ======
    @Test
    void testDeleteById_DetalleExistente() {
        // Arrange
        doNothing().when(detalleVentaRepository).deleteById(1L);

        // Act
        detalleVentaService.deleteById(1L);

        // Assert
        verify(detalleVentaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_DetalleNoExistente() {
        // Arrange
        doNothing().when(detalleVentaRepository).deleteById(999L);

        // Act
        detalleVentaService.deleteById(999L);

        // Assert
        verify(detalleVentaRepository, times(1)).deleteById(999L);
    }

    // ====== PRUEBAS DE findByVentaId() ======
    @Test
    void testFindByVentaId_ConDetalles() {
        // Arrange
        List<DetalleVenta> detallesVenta = Arrays.asList(detalleVenta, detalleVenta2);
        when(detalleVentaRepository.findByVentaId(1L)).thenReturn(detallesVenta);

        // Act
        List<DetalleVenta> resultado = detalleVentaService.findByVentaId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(1L, resultado.get(0).getVenta().getId());
        assertEquals(1L, resultado.get(1).getVenta().getId());
        verify(detalleVentaRepository, times(1)).findByVentaId(1L);
    }

    @Test
    void testFindByVentaId_SinDetalles() {
        // Arrange
        when(detalleVentaRepository.findByVentaId(999L)).thenReturn(new ArrayList<>());

        // Act
        List<DetalleVenta> resultado = detalleVentaService.findByVentaId(999L);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(detalleVentaRepository, times(1)).findByVentaId(999L);
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testDetalleConMultiplosProductos() {
        // Arrange
        Producto producto3 = new Producto();
        producto3.setId(3L);
        producto3.setNombre("Monitor 24 pulgadas");
        producto3.setPrecio(150.0);

        DetalleVenta detalleProducto3 = new DetalleVenta();
        detalleProducto3.setId(3L);
        detalleProducto3.setVenta(venta);
        detalleProducto3.setProducto(producto3);
        detalleProducto3.setCantidad(2);
        detalleProducto3.setPrecio(150.0);

        when(detalleVentaRepository.save(detalleProducto3)).thenReturn(detalleProducto3);

        // Act
        DetalleVenta resultado = detalleVentaService.save(detalleProducto3);

        // Assert
        assertEquals(3L, resultado.getId());
        assertEquals("Monitor 24 pulgadas", resultado.getProducto().getNombre());
    }

    @Test
    void testDetalleConCantidadAlta() {
        // Arrange
        DetalleVenta detalleCantidadAlta = new DetalleVenta();
        detalleCantidadAlta.setId(4L);
        detalleCantidadAlta.setVenta(venta);
        detalleCantidadAlta.setProducto(producto);
        detalleCantidadAlta.setCantidad(1000);
        detalleCantidadAlta.setPrecio(25.0);

        when(detalleVentaRepository.save(detalleCantidadAlta)).thenReturn(detalleCantidadAlta);

        // Act
        DetalleVenta resultado = detalleVentaService.save(detalleCantidadAlta);

        // Assert
        assertEquals(1000, resultado.getCantidad());
        assertEquals(25000.0, resultado.getCantidad() * resultado.getPrecio());
    }
}
