package com.minimarket.service;

import com.minimarket.entity.Carrito;
import com.minimarket.entity.Producto;
import com.minimarket.entity.Usuario;
import com.minimarket.repository.CarritoRepository;
import com.minimarket.service.impl.CarritoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarritoServiceImplTest {

    @Mock
    private CarritoRepository carritoRepository;

    @InjectMocks
    private CarritoServiceImpl carritoService;

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
    void testFindAll_ConProductos() {
        List<Carrito> carritos = Arrays.asList(carrito1, carrito2);

        when(carritoRepository.findAll()).thenReturn(carritos);

        List<Carrito> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        when(carritoRepository.findAll()).thenReturn(Arrays.asList());

        List<Carrito> resultado = carritoService.findAll();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        verify(carritoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_CarritoExistente() {
        when(carritoRepository.findById(1L))
                .thenReturn(Optional.of(carrito1));

        Carrito resultado = carritoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(2, resultado.getCantidad());

        verify(carritoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_CarritoNoExistente() {
        when(carritoRepository.findById(999L))
                .thenReturn(Optional.empty());

        Carrito resultado = carritoService.findById(999L);

        assertNull(resultado);

        verify(carritoRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_ProductoNuevoEnCarrito() {
        Producto nuevoProducto = new Producto();
        nuevoProducto.setId(3L);
        nuevoProducto.setNombre("Sprite");

        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setProducto(nuevoProducto);
        nuevoCarrito.setCantidad(1);

        when(carritoRepository.save(nuevoCarrito))
                .thenReturn(nuevoCarrito);

        Carrito resultado = carritoService.save(nuevoCarrito);

        assertNotNull(resultado);
        assertEquals(1, resultado.getCantidad());

        verify(carritoRepository, times(1)).save(nuevoCarrito);
    }

    @Test
    void testSave_IncrementarCantidad() {
        Carrito actualizado = new Carrito();
        actualizado.setId(1L);
        actualizado.setProducto(producto);
        actualizado.setCantidad(5);

        when(carritoRepository.save(actualizado))
                .thenReturn(actualizado);

        Carrito resultado = carritoService.save(actualizado);

        assertNotNull(resultado);
        assertEquals(5, resultado.getCantidad());

        verify(carritoRepository, times(1)).save(actualizado);
    }

    @Test
    void testSave_ConCantidadGrande() {
        Carrito carrito = new Carrito();
        carrito.setProducto(producto);
        carrito.setCantidad(100);

        when(carritoRepository.save(carrito))
                .thenReturn(carrito);

        Carrito resultado = carritoService.save(carrito);

        assertNotNull(resultado);
        assertEquals(100, resultado.getCantidad());

        verify(carritoRepository, times(1)).save(carrito);
    }

    @Test
    void testDeleteById_CarritoExistente() {
        carritoService.deleteById(1L);

        verify(carritoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_CarritoNoExistente() {
        carritoService.deleteById(999L);

        verify(carritoRepository, times(1)).deleteById(999L);
    }
}