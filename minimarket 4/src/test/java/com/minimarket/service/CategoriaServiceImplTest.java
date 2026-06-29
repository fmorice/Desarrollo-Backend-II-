package com.minimarket.service;

import com.minimarket.entity.Categoria;
import com.minimarket.repository.CategoriaRepository;
import com.minimarket.service.impl.CategoriaServiceImpl;
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
public class CategoriaServiceImplTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaServiceImpl categoriaService;

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

    @Test
    void testFindAll_ConCategorias() {
        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);
        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList());

        List<Categoria> resultado = categoriaService.findAll();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());
    }

    @Test
    void testFindById_CategoriaExistente() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria1));

        Categoria resultado = categoriaService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Bebidas", resultado.getNombre());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_CategoriaNoExistente() {
        when(categoriaRepository.findById(999L)).thenReturn(Optional.empty());

        Categoria resultado = categoriaService.findById(999L);

        assertNull(resultado);
    }

    @Test
    void testSave_CategoriaNueva() {
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre("Congelados");

        when(categoriaRepository.save(nuevaCategoria)).thenReturn(nuevaCategoria);

        Categoria resultado = categoriaService.save(nuevaCategoria);

        assertNotNull(resultado);
        assertEquals("Congelados", resultado.getNombre());
        verify(categoriaRepository, times(1)).save(nuevaCategoria);
    }

    @Test
    void testSave_CategoriaConNombreLargo() {
        Categoria categoria = new Categoria();
        categoria.setNombre("Productos de Limpieza y Hogar");

        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria resultado = categoriaService.save(categoria);

        assertNotNull(resultado);
        assertEquals("Productos de Limpieza y Hogar", resultado.getNombre());
    }

    @Test
    void testSave_ActualizarCategoria() {
        Categoria existente = new Categoria();
        existente.setId(1L);
        existente.setNombre("Bebidas Premium");

        when(categoriaRepository.save(existente)).thenReturn(existente);

        Categoria resultado = categoriaService.save(existente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void testDeleteById_CategoriaExistente() {
        categoriaService.deleteById(1L);

        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_CategoriaNoExistente() {
        categoriaService.deleteById(999L);

        verify(categoriaRepository, times(1)).deleteById(999L);
    }
}
