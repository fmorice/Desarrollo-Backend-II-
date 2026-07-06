package com.minimarket.service;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.repository.InventarioRepository;
import com.minimarket.service.impl.InventarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceImplTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @InjectMocks
    private InventarioServiceImpl inventarioService;

    private Inventario movimiento1;
    private Inventario movimiento2;

    @BeforeEach
    void setUp() {

        Producto producto1 = new Producto();
        producto1.setId(1L);

        Producto producto2 = new Producto();
        producto2.setId(2L);

        movimiento1 = new Inventario();
        movimiento1.setId(1L);
        movimiento1.setProducto(producto1);
        movimiento1.setCantidad(10);
        movimiento1.setTipoMovimiento("ENTRADA");
        movimiento1.setFechaMovimiento(new Date());

        movimiento2 = new Inventario();
        movimiento2.setId(2L);
        movimiento2.setProducto(producto2);
        movimiento2.setCantidad(5);
        movimiento2.setTipoMovimiento("SALIDA");
        movimiento2.setFechaMovimiento(new Date());
    }

    @Test
    void testFindAll_ConMovimientos() {

        List<Inventario> movimientos = Arrays.asList(movimiento1, movimiento2);

        when(inventarioRepository.findAll()).thenReturn(movimientos);

        List<Inventario> resultado = inventarioService.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void testFindAll_ListaVacia() {

        when(inventarioRepository.findAll()).thenReturn(List.of());

        List<Inventario> resultado = inventarioService.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(inventarioRepository, times(1)).findAll();
    }

    @Test
    void testFindById_MovimientoExistente() {

        when(inventarioRepository.findById(1L))
                .thenReturn(Optional.of(movimiento1));

        Inventario resultado = inventarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("ENTRADA", resultado.getTipoMovimiento());
        assertEquals(10, resultado.getCantidad());

        verify(inventarioRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_MovimientoNoExistente() {

        when(inventarioRepository.findById(999L))
                .thenReturn(Optional.empty());

        Inventario resultado = inventarioService.findById(999L);

        assertNull(resultado);

        verify(inventarioRepository, times(1)).findById(999L);
    }

    @Test
    void testSave_MovimientoEntrada() {

        Producto producto = new Producto();
        producto.setId(1L);

        Inventario entrada = new Inventario();
        entrada.setProducto(producto);
        entrada.setCantidad(50);
        entrada.setTipoMovimiento("ENTRADA");
        entrada.setFechaMovimiento(new Date());

        when(inventarioRepository.save(entrada))
                .thenReturn(entrada);

        Inventario resultado = inventarioService.save(entrada);

        assertNotNull(resultado);
        assertEquals("ENTRADA", resultado.getTipoMovimiento());
        assertEquals(50, resultado.getCantidad());

        verify(inventarioRepository, times(1)).save(entrada);
    }

    @Test
    void testSave_MovimientoSalida() {

        Producto producto = new Producto();
        producto.setId(2L);

        Inventario salida = new Inventario();
        salida.setProducto(producto);
        salida.setCantidad(10);
        salida.setTipoMovimiento("SALIDA");
        salida.setFechaMovimiento(new Date());

        when(inventarioRepository.save(salida))
                .thenReturn(salida);

        Inventario resultado = inventarioService.save(salida);

        assertNotNull(resultado);
        assertEquals("SALIDA", resultado.getTipoMovimiento());
        assertEquals(10, resultado.getCantidad());

        verify(inventarioRepository, times(1)).save(salida);
    }

    @Test
    void testSave_MovimientoAjuste() {

        Producto producto = new Producto();
        producto.setId(1L);

        Inventario ajuste = new Inventario();
        ajuste.setProducto(producto);
        ajuste.setCantidad(5);
        ajuste.setTipoMovimiento("AJUSTE");
        ajuste.setFechaMovimiento(new Date());

        when(inventarioRepository.save(ajuste))
                .thenReturn(ajuste);

        Inventario resultado = inventarioService.save(ajuste);

        assertNotNull(resultado);
        assertEquals("AJUSTE", resultado.getTipoMovimiento());

        verify(inventarioRepository, times(1)).save(ajuste);
    }

    @Test
    void testSave_MovimientoConCantidadGrande() {

        Producto producto = new Producto();
        producto.setId(1L);

        Inventario movimiento = new Inventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(1000);
        movimiento.setTipoMovimiento("ENTRADA");
        movimiento.setFechaMovimiento(new Date());

        when(inventarioRepository.save(movimiento))
                .thenReturn(movimiento);

        Inventario resultado = inventarioService.save(movimiento);

        assertNotNull(resultado);
        assertEquals(1000, resultado.getCantidad());

        verify(inventarioRepository, times(1)).save(movimiento);
    }

    @Test
    void testDeleteById_MovimientoExistente() {

        inventarioService.deleteById(1L);

        verify(inventarioRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteById_MovimientoNoExistente() {

        inventarioService.deleteById(999L);

        verify(inventarioRepository, times(1)).deleteById(999L);
    }
}