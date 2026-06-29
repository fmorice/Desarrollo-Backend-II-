package com.minimarket.controller;

import com.minimarket.entity.Categoria;
import com.minimarket.service.CategoriaService;
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
public class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    private Categoria categoria1;
    private Categoria categoria2;

    @BeforeEach
    void setUp() {
        categoria1 = new Categoria();
        categoria1.setId(1L);
        categoria1.setNombre("Bebidas");

        categoria2 = new Categoria();
        categoria2.setId(2L);
        categoria2.setNombre("Snacks");
    }

    // ====== PRUEBAS DE listarCategorias() ======
    @Test
    void testListarCategorias_ConCategorias() {
        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);
        when(categoriaService.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaController.listarCategorias();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Bebidas", resultado.get(0).getNombre());
        verify(categoriaService, times(1)).findAll();
    }

    @Test
    void testListarCategorias_ListaVacia() {
        when(categoriaService.findAll()).thenReturn(Arrays.asList());

        List<Categoria> resultado = categoriaController.listarCategorias();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
        verify(categoriaService, times(1)).findAll();
    }

    @Test
    void testListarCategorias_ConVariasCategorias() {
        Categoria categoria3 = new Categoria();
        categoria3.setId(3L);
        categoria3.setNombre("Lácteos");

        List<Categoria> categorias = Arrays.asList(categoria1, categoria2, categoria3);
        when(categoriaService.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaController.listarCategorias();

        assertEquals(3, resultado.size());
    }

    // ====== PRUEBAS DE obtenerCategoriaPorId() ======
    @Test
    void testObtenerCategoriaPorId_CategoriaExistente() {
        when(categoriaService.findById(1L)).thenReturn(categoria1);

        ResponseEntity<Categoria> response = categoriaController.obtenerCategoriaPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bebidas", response.getBody().getNombre());
        verify(categoriaService, times(1)).findById(1L);
    }

    @Test
    void testObtenerCategoriaPorId_CategoriaNoExistente() {
        when(categoriaService.findById(999L)).thenReturn(null);

        ResponseEntity<Categoria> response = categoriaController.obtenerCategoriaPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(categoriaService, times(1)).findById(999L);
    }

    // ====== PRUEBAS DE guardarCategoria() ======
    @Test
    void testGuardarCategoria_CategoriaNueva() {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Congelados");

        when(categoriaService.save(nuevaCategoria)).thenReturn(nuevaCategoria);

        Categoria resultado = categoriaController.guardarCategoria(nuevaCategoria);

        assertNotNull(resultado);
        assertEquals("Congelados", resultado.getNombre());
        verify(categoriaService, times(1)).save(nuevaCategoria);
    }

    @Test
    void testGuardarCategoria_ConNombreLargo() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Productos de Limpieza y Hogar");

        when(categoriaService.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaController.guardarCategoria(categoria);

        assertNotNull(resultado);
        assertEquals("Productos de Limpieza y Hogar", resultado.getNombre());
    }

    // ====== PRUEBAS DE actualizarCategoria() ======
    @Test
    void testActualizarCategoria_CategoriaExistente() {
        Categoria actualizada = new Categoria();
        actualizada.setNombre("Bebidas Premium");

        when(categoriaService.findById(1L)).thenReturn(categoria1);
        when(categoriaService.save(any(Categoria.class))).thenReturn(actualizada);

        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(1L, actualizada);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        verify(categoriaService, times(1)).findById(1L);
        verify(categoriaService, times(1)).save(any(Categoria.class));
    }

    @Test
    void testActualizarCategoria_CategoriaNoExistente() {
        when(categoriaService.findById(999L)).thenReturn(null);

        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(999L, categoria1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoriaService, never()).save(any(Categoria.class));
    }

    // ====== PRUEBAS DE eliminarCategoria() ======
    @Test
    void testEliminarCategoria_CategoriaExistente() {
        when(categoriaService.findById(1L)).thenReturn(categoria1);

        ResponseEntity<Void> response = categoriaController.eliminarCategoria(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(categoriaService, times(1)).findById(1L);
        verify(categoriaService, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarCategoria_CategoriaNoExistente() {
        when(categoriaService.findById(999L)).thenReturn(null);

        ResponseEntity<Void> response = categoriaController.eliminarCategoria(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(categoriaService, never()).deleteById(any());
    }
}
