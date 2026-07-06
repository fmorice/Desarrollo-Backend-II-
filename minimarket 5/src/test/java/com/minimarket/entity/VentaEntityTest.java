package com.minimarket.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VentaEntityTest {

    private Venta venta;
    private Usuario usuario;
    private DetalleVenta detalleVenta;
    private Producto producto;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("admin");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Laptop");
        producto.setPrecio(1000.0);

        detalleVenta = new DetalleVenta();
        detalleVenta.setId(1L);
        detalleVenta.setCantidad(2);
        detalleVenta.setPrecio(1000.0);
        detalleVenta.setProducto(producto);

        venta = new Venta();
    }

    // ====== PRUEBAS DE ID ======
    @Test
    void testSetId_GetId() {
        // Act
        venta.setId(1L);

        // Assert
        assertEquals(1L, venta.getId());
    }

    @Test
    void testGetId_NoAsignado() {
        // Act & Assert
        assertNull(venta.getId());
    }

    // ====== PRUEBAS DE USUARIO ======
    @Test
    void testSetUsuario_GetUsuario() {
        // Act
        venta.setUsuario(usuario);

        // Assert
        assertNotNull(venta.getUsuario());
        assertEquals(1L, venta.getUsuario().getId());
        assertEquals("admin", venta.getUsuario().getUsername());
    }

    @Test
    void testGetUsuario_NoAsignado() {
        // Act & Assert
        assertNull(venta.getUsuario());
    }

    @Test
    void testSetUsuario_UsuarioDiferente() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user");

        // Act
        venta.setUsuario(usuario);
        assertEquals("admin", venta.getUsuario().getUsername());

        venta.setUsuario(usuario2);

        // Assert
        assertEquals("user", venta.getUsuario().getUsername());
    }

    // ====== PRUEBAS DE FECHA ======
    @Test
    void testSetFecha_GetFecha() {
        // Arrange
        Date fecha = new Date();

        // Act
        venta.setFecha(fecha);

        // Assert
        assertNotNull(venta.getFecha());
        assertEquals(fecha, venta.getFecha());
    }

    @Test
    void testGetFecha_NoAsignado() {
        // Act & Assert
        assertNull(venta.getFecha());
    }

    @Test
    void testSetFecha_FechaAntigua() {
        // Arrange
        Date fechaAntigua = new Date(System.currentTimeMillis() - 86400000); // Hace 1 día

        // Act
        venta.setFecha(fechaAntigua);

        // Assert
        assertEquals(fechaAntigua, venta.getFecha());
        assertTrue(venta.getFecha().before(new Date()));
    }

    @Test
    void testSetFecha_FechaActual() {
        // Arrange
        Date fechaActual = new Date();

        // Act
        venta.setFecha(fechaActual);

        // Assert
        assertEquals(fechaActual, venta.getFecha());
    }

    // ====== PRUEBAS DE DETALLES ======
    @Test
    void testSetDetalles_GetDetalles() {
        // Arrange
        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(detalleVenta);

        // Act
        venta.setDetalles(detalles);

        // Assert
        assertNotNull(venta.getDetalles());
        assertEquals(1, venta.getDetalles().size());
        assertTrue(venta.getDetalles().contains(detalleVenta));
    }

    @Test
    void testGetDetalles_NoAsignado() {
        // Act & Assert
        assertNull(venta.getDetalles());
    }

    @Test
    void testSetDetalles_MultiplicosDetalles() {
        // Arrange
        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Mouse");
        producto2.setPrecio(30.0);

        DetalleVenta detalleVenta2 = new DetalleVenta();
        detalleVenta2.setId(2L);
        detalleVenta2.setCantidad(5);
        detalleVenta2.setPrecio(30.0);
        detalleVenta2.setProducto(producto2);

        List<DetalleVenta> detalles = Arrays.asList(detalleVenta, detalleVenta2);

        // Act
        venta.setDetalles(detalles);

        // Assert
        assertEquals(2, venta.getDetalles().size());
        assertTrue(venta.getDetalles().contains(detalleVenta));
        assertTrue(venta.getDetalles().contains(detalleVenta2));
    }

    @Test
    void testSetDetalles_ListaVacia() {
        // Act
        venta.setDetalles(new ArrayList<>());

        // Assert
        assertNotNull(venta.getDetalles());
        assertTrue(venta.getDetalles().isEmpty());
    }

    // ====== PRUEBAS ADICIONALES ======
    @Test
    void testVentaCompleta() {
        // Arrange
        Date fecha = new Date();
        List<DetalleVenta> detalles = new ArrayList<>();
        detalles.add(detalleVenta);

        // Act
        venta.setId(1L);
        venta.setUsuario(usuario);
        venta.setFecha(fecha);
        venta.setDetalles(detalles);

        // Assert
        assertEquals(1L, venta.getId());
        assertEquals("admin", venta.getUsuario().getUsername());
        assertEquals(fecha, venta.getFecha());
        assertEquals(1, venta.getDetalles().size());
    }

    @Test
    void testSetIdMultiplicasVeces() {
        // Act
        venta.setId(1L);
        venta.setId(2L);
        venta.setId(3L);

        // Assert
        assertEquals(3L, venta.getId());
    }

    @Test
    void testSetUsuarioMultiplicasVeces() {
        // Arrange
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setUsername("user2");

        Usuario usuario3 = new Usuario();
        usuario3.setId(3L);
        usuario3.setUsername("user3");

        // Act
        venta.setUsuario(usuario);
        venta.setUsuario(usuario2);
        venta.setUsuario(usuario3);

        // Assert
        assertEquals(3L, venta.getUsuario().getId());
        assertEquals("user3", venta.getUsuario().getUsername());
    }

    @Test
    void testSetDetallesMultiplicasVeces() {
        // Arrange
        List<DetalleVenta> detalles1 = new ArrayList<>();
        detalles1.add(detalleVenta);

        Producto producto2 = new Producto();
        producto2.setId(2L);
        DetalleVenta detalleVenta2 = new DetalleVenta();
        detalleVenta2.setId(2L);
        detalleVenta2.setProducto(producto2);

        List<DetalleVenta> detalles2 = new ArrayList<>();
        detalles2.add(detalleVenta2);

        // Act
        venta.setDetalles(detalles1);
        assertEquals(1, venta.getDetalles().size());

        venta.setDetalles(detalles2);

        // Assert
        assertEquals(1, venta.getDetalles().size());
        assertTrue(venta.getDetalles().contains(detalleVenta2));
    }

    @Test
    void testVentaSinDetalles() {
        // Act
        venta.setId(5L);
        venta.setUsuario(usuario);
        venta.setFecha(new Date());
        venta.setDetalles(new ArrayList<>());

        // Assert
        assertEquals(5L, venta.getId());
        assertNotNull(venta.getDetalles());
        assertTrue(venta.getDetalles().isEmpty());
    }

    @Test
    void testVentaConVariosProductosYUsuario() {
        // Arrange
        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            Producto prod = new Producto();
            prod.setId((long) i);
            prod.setNombre("Producto " + i);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setId((long) i);
            detalle.setProducto(prod);
            detalle.setCantidad(i);

            detalles.add(detalle);
        }

        // Act
        venta.setId(10L);
        venta.setUsuario(usuario);
        venta.setFecha(new Date());
        venta.setDetalles(detalles);

        // Assert
        assertEquals(3, venta.getDetalles().size());
        assertEquals("admin", venta.getUsuario().getUsername());
    }
}
