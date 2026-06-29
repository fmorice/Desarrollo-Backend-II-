package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.service.ProductoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks
    private ProductoController productoController;

    private Producto producto1;
    private Producto producto2;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNombre("Bebidas");

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Coca Cola");
        producto1.setPrecio(2.5);
        producto1.setStock(100);
        producto1.setCategoria(categoria);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Pepsi");
        producto2.setPrecio(2.3);
        producto2.setStock(50);
        producto2.setCategoria(categoria);
    }

    @Test
    void testListarProductos_ConProductos() {
        List<Producto> productos = Arrays.asList(producto1, producto2);
        when(productoService.findAll()).thenReturn(productos);

        List<Producto> resultado = productoController.listarProductos();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Coca Cola", resultado.get(0).getNombre());
        verify(productoService, times(1)).findAll();
    }

    @Test
    void testListarProductos_ListaVacia() {
        when(productoService.findAll()).thenReturn(Arrays.asList());

        List<Producto> resultado = productoController.listarProductos();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(productoService, times(1)).findAll();
    }

    @Test
    void testListarProductos_ConVariosProductos() {
        Producto producto3 = new Producto();
        producto3.setId(3L);
        producto3.setNombre("Sprite");
        producto3.setPrecio(2.0);
        producto3.setStock(75);

        List<Producto> productos = Arrays.asList(producto1, producto2, producto3);
        when(productoService.findAll()).thenReturn(productos);

        List<Producto> resultado = productoController.listarProductos();

        assertEquals(3, resultado.size());
        verify(productoService, times(1)).findAll();
    }

    @Test
    void testObtenerProductoPorId_ProductoExistente() {
        when(productoService.findById(1L)).thenReturn(producto1);

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Coca Cola", response.getBody().getNombre());
        verify(productoService, times(1)).findById(1L);
    }

    @Test
    void testObtenerProductoPorId_ProductoNoExistente() {
        when(productoService.findById(999L)).thenReturn(null);

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).findById(999L);
    }

    @Test
    void testObtenerProductoPorId_VariosProductos() {
        when(productoService.findById(2L)).thenReturn(producto2);

        ResponseEntity<Producto> response = productoController.obtenerProductoPorId(2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pepsi", response.getBody().getNombre());
        assertEquals(2.3, response.getBody().getPrecio());
    }

    @Test
    void testGuardarProducto_ProductoNuevo() {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Fanta");
        nuevoProducto.setPrecio(2.2);
        nuevoProducto.setStock(60);

        when(productoService.save(nuevoProducto)).thenReturn(nuevoProducto);

        Producto resultado = productoController.guardarProducto(nuevoProducto);

        assertNotNull(resultado);
        assertEquals("Fanta", resultado.getNombre());
        verify(productoService, times(1)).save(nuevoProducto);
    }

    @Test
    void testGuardarProducto_ConPrecioNegativo() {
        Producto productoInvalido = new Producto();
        productoInvalido.setNombre("Producto Invalido");
        productoInvalido.setPrecio(-5.0);
        productoInvalido.setStock(10);

        when(productoService.save(productoInvalido)).thenReturn(productoInvalido);

        Producto resultado = productoController.guardarProducto(productoInvalido);

        assertNotNull(resultado);
        verify(productoService, times(1)).save(productoInvalido);
    }

    @Test
    void testGuardarProducto_ConStockCero() {
        Producto productoSinStock = new Producto();
        productoSinStock.setNombre("Producto Sin Stock");
        productoSinStock.setPrecio(3.0);
        productoSinStock.setStock(0);

        when(productoService.save(productoSinStock)).thenReturn(productoSinStock);

        Producto resultado = productoController.guardarProducto(productoSinStock);

        assertNotNull(resultado);
        assertEquals(0, resultado.getStock());
    }

    @Test
    void testActualizarProducto_ProductoExistente() {
        Producto productoActualizado = new Producto();
        productoActualizado.setNombre("Coca Cola Zero");
        productoActualizado.setPrecio(2.8);
        productoActualizado.setStock(150);

        when(productoService.findById(1L)).thenReturn(producto1);
        when(productoService.save(any(Producto.class))).thenReturn(productoActualizado);

        ResponseEntity<Producto> response = productoController.actualizarProducto(1L, productoActualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(productoService, times(1)).findById(1L);
        verify(productoService, times(1)).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto_ProductoNoExistente() {
        when(productoService.findById(999L)).thenReturn(null);

        ResponseEntity<Producto> response = productoController.actualizarProducto(999L, producto1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(productoService, times(1)).findById(999L);
        verify(productoService, never()).save(any(Producto.class));
    }

    @Test
    void testActualizarProducto_CambiaPrecio() {
        Producto actualizado = new Producto();
        actualizado.setNombre("Coca Cola");
        actualizado.setPrecio(3.0);
        actualizado.setStock(100);

        when(productoService.findById(1L)).thenReturn(producto1);
        when(productoService.save(any(Producto.class))).thenReturn(actualizado);

        ResponseEntity<Producto> response = productoController.actualizarProducto(1L, actualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3.0, response.getBody().getPrecio());
    }

    @Test
    void testEliminarProducto_ProductoExistente() {
        when(productoService.findById(1L)).thenReturn(producto1);

        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoService, times(1)).findById(1L);
        verify(productoService, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProducto_ProductoNoExistente() {
        when(productoService.findById(999L)).thenReturn(null);

        ResponseEntity<Void> response = productoController.eliminarProducto(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productoService, times(1)).findById(999L);
        verify(productoService, never()).deleteById(any());
    }

    @Test
    void testEliminarProducto_VerificaDelecion() {
        when(productoService.findById(1L)).thenReturn(producto1);

        ResponseEntity<Void> response = productoController.eliminarProducto(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(productoService).deleteById(1L);
    }
}