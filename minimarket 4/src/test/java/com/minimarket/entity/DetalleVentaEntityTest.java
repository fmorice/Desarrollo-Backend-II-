package com.minimarket.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DetalleVentaEntityTest {

    private DetalleVenta detalleVenta;
    private Venta venta;
    private Producto producto;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");

        venta = new Venta();
        venta.setId(1L);
        venta.setUsuario(usuario);

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(1000.0);

        detalleVenta = new DetalleVenta();
    }

    // ====== PRUEBAS DE ID ======
    @Test
    void testSetId_GetId() {
        // Act
        detalleVenta.setId(1L);

        // Assert
        assertEquals(1L, detalleVenta.getId());
    }

    @Test
    void testGetId_NoAsignado() {
        // Act & Assert
        assertNull(detalleVenta.getId());
    }

    // ====== PRUEBAS DE VENTA ======
    @Test
    void testSetVenta_GetVenta() {
        // Act
        detalleVenta.setVenta(venta);

        // Assert
        assertNotNull(detalleVenta.getVenta());
        assertEquals(1L, detalleVenta.getVenta().getId());
    }

    @Test
    void testGetVenta_NoAsignado() {
        // Act & Assert
        assertNull(detalleVenta.getVenta());
    }

    // ====== PRUEBAS DE PRODUCTO ======
    @Test
    void testSetProducto_GetProducto() {
        // Act
        detalleVenta.setProducto(producto);

        // Assert
        assertNotNull(detalleVenta.getProducto());
        assertEquals(1L, detalleVenta.getProducto().getId());
        assertEquals("Laptop", detalleVenta.getProducto().getNombre());
    }

    @Test
    void testGetProducto_NoAsignado() {
        // Act & Assert
        assertNull(detalleVenta.getProducto());
    }

    // ====== PRUEBAS DE CANTIDAD ======
    @Test
    void testSetCantidad_GetCantidad() {
        // Act
        detalleVenta.setCantidad(5);

        // Assert
        assertEquals(5, detalleVenta.getCantidad());
    }

    @Test
    void testGetCantidad_NoAsignado() {
        // Act & Assert
        assertNull(detalleVenta.getCantidad());
    }

    @Test
    void testSetCantidad_CantidadAlta() {
        // Act
        detalleVenta.setCantidad(1000);

        // Assert
        assertEquals(1000, detalleVenta.getCantidad());
    }

    @Test
    void testSetCantidad_CantidadUno() {
        // Act
        detalleVenta.setCantidad(1);

        // Assert
        assertEquals(1, detalleVenta.getCantidad());
    }

    // ====== PRUEBAS DE PRECIO ======
    @Test
    void testSetPrecio_GetPrecio() {
        // Act
        detalleVenta.setPrecio(100.0);

        // Assert
        assertEquals(100.0, detalleVenta.getPrecio());
    }

    @Test
    void testGetPrecio_NoAsignado() {
        // Act & Assert
        assertNull(detalleVenta.getPrecio());
    }

    @Test
    void testSetPrecio_PrecioAlto() {
        // Act
        detalleVenta.setPrecio(9999.99);

        // Assert
        assertEquals(9999.99, detalleVenta.getPrecio());
    }

    @Test
    void testSetPrecio_PrecioBajo() {
        // Act
        detalleVenta.setPrecio(0.01);

        // Assert
        assertEquals(0.01, detalleVenta.getPrecio());
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testDetalleVentaCompleto() {
        // Act
        detalleVenta.setId(1L);
        detalleVenta.setVenta(venta);
        detalleVenta.setProducto(producto);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(1000.0);

        // Assert
        assertEquals(1L, detalleVenta.getId());
        assertEquals(1L, detalleVenta.getVenta().getId());
        assertEquals("Laptop", detalleVenta.getProducto().getNombre());
        assertEquals(2, detalleVenta.getCantidad());
        assertEquals(1000.0, detalleVenta.getPrecio());
    }

    @Test
    void testSetIdMultiplicasVeces() {
        // Act
        detalleVenta.setId(1L);
        detalleVenta.setId(2L);
        detalleVenta.setId(3L);

        // Assert
        assertEquals(3L, detalleVenta.getId());
    }

    @Test
    void testSetCantidadMultiplicasVeces() {
        // Act
        detalleVenta.setCantidad(5);
        detalleVenta.setCantidad(10);
        detalleVenta.setCantidad(15);

        // Assert
        assertEquals(15, detalleVenta.getCantidad());
    }

    @Test
    void testSetPrecioMultiplicasVeces() {
        // Act
        detalleVenta.setPrecio(100.0);
        detalleVenta.setPrecio(200.0);
        detalleVenta.setPrecio(300.0);

        // Assert
        assertEquals(300.0, detalleVenta.getPrecio());
    }

    @Test
    void testDetalleVentaConProductoDiferente() {
        // Arrange
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse");
        producto2.setPrecio(30.0);

        // Act
        detalleVenta.setProducto(producto);
        assertEquals("Laptop", detalleVenta.getProducto().getNombre());

        detalleVenta.setProducto(producto2);

        // Assert
        assertEquals("Mouse", detalleVenta.getProducto().getNombre());
        assertEquals(30.0, detalleVenta.getProducto().getPrecio());
    }

    @Test
    void testDetalleVentaConVentaDiferente() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);

        Venta venta2 = new Venta();
        venta2.setId(2L);
        venta2.setUsuario(usuario2);

        // Act
        detalleVenta.setVenta(venta);
        assertEquals(1L, detalleVenta.getVenta().getId());

        detalleVenta.setVenta(venta2);

        // Assert
        assertEquals(2L, detalleVenta.getVenta().getId());
    }

    @Test
    void testCalculoTotalDetalle() {
        // Act
        detalleVenta.setCantidad(5);
        detalleVenta.setPrecio(25.0);

        // Assert
        double total = detalleVenta.getCantidad() * detalleVenta.getPrecio();
        assertEquals(125.0, total);
    }

    @Test
    void testMultiplesDetalles() {
        // Arrange
        DetalleVenta detalle1 = new DetalleVenta();
        detalle1.setId(1L);
        detalle1.setCantidad(2);
        detalle1.setPrecio(100.0);

        DetalleVenta detalle2 = new DetalleVenta();
        detalle2.setId(2L);
        detalle2.setCantidad(3);
        detalle2.setPrecio(50.0);

        // Act & Assert
        assertEquals(2, detalle1.getCantidad());
        assertEquals(3, detalle2.getCantidad());
        double total = (detalle1.getCantidad() * detalle1.getPrecio()) + 
                       (detalle2.getCantidad() * detalle2.getPrecio());
        assertEquals(350.0, total);
    }
}
