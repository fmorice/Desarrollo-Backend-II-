package com.minimarket.controller;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.service.CarritoService;
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
public class CarritoControllerTest {

    @Mock
    private CarritoService carritoService;

    @InjectMocks
    private CarritoController carritoController;

    private Carrito carrito1;
    private Carrito carrito2;
    private Usuario usuario;
    private Producto producto;

    @BeforeEach
    void setUp() {

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setUsername("testuser");

        producto = new Producto();
        producto.setId(1L);
        producto.setNombre("Coca Cola");
        producto.setPrecio(2.5);

        carrito1 = new Carrito();
        carrito1.setId(1L);
        carrito1.setUsuario(usuario);
        carrito1.setProducto(producto);
        carrito1.setCantidad(2);

        carrito2 = new Carrito();
        carrito2.setId(2L);
        carrito2.setUsuario(usuario);
        carrito2.setProducto(producto);
        carrito2.setCantidad(1);
    }

    @Test
    void testListarCarrito_ConProductos() {

        List<Carrito> carritos = Arrays.asList(carrito1, carrito2);

        when(carritoService.findAll()).thenReturn(carritos);

        List<Carrito> resultado = carritoController.listarCarrito();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(carritoService, times(1)).findAll();
    }

    @Test
    void testListarCarrito_ListaVacia() {

        when(carritoService.findAll()).thenReturn(Arrays.asList());

        List<Carrito> resultado = carritoController.listarCarrito();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        verify(carritoService, times(1)).findAll();
    }

    @Test
    void testListarCarrito_ConVariosProductos() {

        Producto producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Pepsi");
        producto2.setPrecio(2.3);

        Carrito carrito3 = new Carrito();
        carrito3.setId(3L);
        carrito3.setProducto(producto2);
        carrito3.setCantidad(3);

        List<Carrito> carritos = Arrays.asList(carrito1, carrito2, carrito3);

        when(carritoService.findAll()).thenReturn(carritos);

        List<Carrito> resultado = carritoController.listarCarrito();

        assertEquals(3, resultado.size());
    }

    @Test
    void testObtenerCarritoPorId_Existente() {

        when(carritoService.findById(1L)).thenReturn(carrito1);

        ResponseEntity<Carrito> response =
                carritoController.obtenerCarritoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCantidad());

        verify(carritoService, times(1)).findById(1L);
    }

    @Test
    void testObtenerCarritoPorId_NoExistente() {

        when(carritoService.findById(999L)).thenReturn(null);

        ResponseEntity<Carrito> response =
                carritoController.obtenerCarritoPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(carritoService, times(1)).findById(999L);
    }

    @Test
    void testAgregarProductoAlCarrito_ProductoNuevo() {

        Producto nuevoProducto = new Producto();
        nuevoProducto.setId(3L);
        nuevoProducto.setNombre("Sprite");
        nuevoProducto.setPrecio(2.0);

        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setProducto(nuevoProducto);
        nuevoCarrito.setCantidad(1);

        when(carritoService.save(nuevoCarrito))
                .thenReturn(nuevoCarrito);

        Carrito resultado =
                carritoController.agregarProductoAlCarrito(nuevoCarrito);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCantidad());

        verify(carritoService, times(1)).save(nuevoCarrito);
    }

    @Test
    void testAgregarProductoAlCarrito_IncrementarCantidad() {

        Carrito carritoActualizado = new Carrito();
        carritoActualizado.setProducto(producto);
        carritoActualizado.setCantidad(5);

        when(carritoService.save(carritoActualizado))
                .thenReturn(carritoActualizado);

        Carrito resultado =
                carritoController.agregarProductoAlCarrito(carritoActualizado);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());
    }

    @Test
    void testAgregarProductoAlCarrito_ConCantidadGrande() {

        Carrito carrito = new Carrito();
        carrito.setProducto(producto);
        carrito.setCantidad(100);

        when(carritoService.save(carrito))
                .thenReturn(carrito);

        Carrito resultado =
                carritoController.agregarProductoAlCarrito(carrito);

        assertNotNull(resultado);
        assertEquals(100, resultado.getCantidad());
    }

    @Test
    void testActualizarCarrito_Existente() {

        Carrito actualizado = new Carrito();
        actualizado.setId(1L);
        actualizado.setCantidad(3);

        when(carritoService.findById(1L))
                .thenReturn(carrito1);

        when(carritoService.save(any(Carrito.class)))
                .thenReturn(actualizado);

        ResponseEntity<Carrito> response =
                carritoController.actualizarCarrito(1L, actualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(carritoService, times(1)).findById(1L);
        verify(carritoService, times(1)).save(any(Carrito.class));
    }

    @Test
    void testActualizarCarrito_NoExistente() {

        when(carritoService.findById(999L)).thenReturn(null);

        ResponseEntity<Carrito> response =
                carritoController.actualizarCarrito(999L, carrito1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(carritoService, never()).save(any(Carrito.class));
    }

    @Test
    void testEliminarProductoDelCarrito_Existente() {

        when(carritoService.findById(1L)).thenReturn(carrito1);

        ResponseEntity<Void> response =
                carritoController.eliminarProductoDelCarrito(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(carritoService, times(1)).findById(1L);
        verify(carritoService, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarProductoDelCarrito_NoExistente() {

        when(carritoService.findById(999L)).thenReturn(null);

        ResponseEntity<Void> response =
                carritoController.eliminarProductoDelCarrito(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(carritoService, never()).deleteById(anyLong());
    }
}