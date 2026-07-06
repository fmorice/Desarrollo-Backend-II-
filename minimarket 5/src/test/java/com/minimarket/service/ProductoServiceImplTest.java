package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.entity.Producto;
import com.minimarket.repository.ProductoRepository;
import com.minimarket.service.impl.ProductoServiceImpl;
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
public class ProductoServiceImplTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoServiceImpl productoService;

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
    void testFindAll_ConProductos() {
        List<Producto> productos = Arrays.asList(producto1, producto2);

        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {

        when(productoRepository.findAll()).thenReturn(Arrays.asList());

        List<Producto> resultado = productoService.findAll();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_ProductoExistente() {

        when(productoRepository.findById(1L))
                .thenReturn(Optional.of(producto1));

        Producto resultado = productoService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Coca Cola", resultado.getNombre());

        verify(productoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_ProductoNoExistente() {

        when(productoRepository.findById(999L))
                .thenReturn(Optional.empty());

        Producto resultado = productoService.findById(999L);

        assertNull(resultado);

        verify(productoRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_ProductoNuevo() {

        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre("Fanta");
        nuevoProducto.setPrecio(2.0);
        nuevoProducto.setStock(75);

        when(productoRepository.save(nuevoProducto))
                .thenReturn(nuevoProducto);

        Producto resultado = productoService.save(nuevoProducto);

        assertNotNull(resultado);
        assertEquals("Fanta", resultado.getNombre());

        verify(productoRepository, times(1)).save(nuevoProducto);
    }

    @Test
    void testSave_ProductoConPrecioAlto() {

        Producto producto = new Producto();
        producto.setNombre("Champagne");
        producto.setPrecio(100.0);
        producto.setStock(10);

        when(productoRepository.save(producto))
                .thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals(100.0, resultado.getPrecio());

        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testSave_ProductoSinStock() {

        Producto producto = new Producto();
        producto.setNombre("Producto Agotado");
        producto.setPrecio(5.0);
        producto.setStock(0);

        when(productoRepository.save(producto))
                .thenReturn(producto);

        Producto resultado = productoService.save(producto);

        assertNotNull(resultado);
        assertEquals(0, resultado.getStock());

        verify(productoRepository, times(1)).save(producto);
    }

    @Test
    void testDeleteById_ProductoExistente() {

        productoService.deleteById(1L);

        verify(productoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_ProductoNoExistente() {

        productoService.deleteById(999L);

        verify(productoRepository, times(1)).deleteById(999L);
    }
}