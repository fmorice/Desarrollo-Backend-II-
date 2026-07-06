package com.minimarket.controller;

import com.minimarket.entity.Inventario;
import com.minimarket.entity.Producto;
import com.minimarket.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioControllerTest {

    @Mock
    private InventarioService inventarioService;

    @InjectMocks
    private InventarioController inventarioController;

    private Inventario movimiento1;
    private Inventario movimiento2;
    private Producto producto1;
    private Producto producto2;

    @BeforeEach
    void setUp() {

        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Coca Cola");
        producto1.setPrecio(2.5);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Pepsi");
        producto2.setPrecio(2.3);

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
    void testListarMovimientos_ConMovimientos() {
        List<Inventario> movimientos = Arrays.asList(movimiento1, movimiento2);

        when(inventarioService.findAll()).thenReturn(movimientos);

        List<Inventario> resultado =
                inventarioController.listarMovimientosDeInventario();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        verify(inventarioService, times(1)).findAll();
    }

    @Test
    void testListarMovimientos_ListaVacia() {

        when(inventarioService.findAll()).thenReturn(Arrays.asList());

        List<Inventario> resultado =
                inventarioController.listarMovimientosDeInventario();

        assertNotNull(resultado);
        assertEquals(0, resultado.size());

        verify(inventarioService, times(1)).findAll();
    }

    @Test
    void testListarMovimientos_ConVariosMovimientos() {

        Producto producto3 = new Producto();
        producto3.setId(3L);
        producto3.setNombre("Sprite");
        producto3.setPrecio(2.0);

        Inventario movimiento3 = new Inventario();
        movimiento3.setId(3L);
        movimiento3.setProducto(producto3);
        movimiento3.setCantidad(20);
        movimiento3.setTipoMovimiento("AJUSTE");
        movimiento3.setFechaMovimiento(new Date());

        List<Inventario> movimientos =
                Arrays.asList(movimiento1, movimiento2, movimiento3);

        when(inventarioService.findAll()).thenReturn(movimientos);

        List<Inventario> resultado =
                inventarioController.listarMovimientosDeInventario();

        assertEquals(3, resultado.size());
    }

    @Test
    void testObtenerMovimientoPorId_Existente() {

        when(inventarioService.findById(1L)).thenReturn(movimiento1);

        ResponseEntity<Inventario> response =
                inventarioController.obtenerMovimientoPorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("ENTRADA",
                response.getBody().getTipoMovimiento());

        verify(inventarioService, times(1)).findById(1L);
    }

    @Test
    void testObtenerMovimientoPorId_NoExistente() {

        when(inventarioService.findById(999L)).thenReturn(null);

        ResponseEntity<Inventario> response =
                inventarioController.obtenerMovimientoPorId(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

        verify(inventarioService, times(1)).findById(999L);
    }

    @Test
    void testRegistrarMovimiento_Entrada() {

        Inventario nuevoMovimiento = new Inventario();
        nuevoMovimiento.setProducto(producto1);
        nuevoMovimiento.setCantidad(50);
        nuevoMovimiento.setTipoMovimiento("ENTRADA");
        nuevoMovimiento.setFechaMovimiento(new Date());

        when(inventarioService.save(nuevoMovimiento))
                .thenReturn(nuevoMovimiento);

        Inventario resultado =
                inventarioController.registrarMovimiento(nuevoMovimiento);

        assertNotNull(resultado);
        assertEquals("ENTRADA", resultado.getTipoMovimiento());
        assertEquals(50, resultado.getCantidad());

        verify(inventarioService, times(1)).save(nuevoMovimiento);
    }

    @Test
    void testRegistrarMovimiento_Salida() {

        Inventario movimientoSalida = new Inventario();
        movimientoSalida.setProducto(producto2);
        movimientoSalida.setCantidad(10);
        movimientoSalida.setTipoMovimiento("SALIDA");
        movimientoSalida.setFechaMovimiento(new Date());

        when(inventarioService.save(movimientoSalida))
                .thenReturn(movimientoSalida);

        Inventario resultado =
                inventarioController.registrarMovimiento(movimientoSalida);

        assertNotNull(resultado);
        assertEquals("SALIDA", resultado.getTipoMovimiento());
    }

    @Test
    void testRegistrarMovimiento_Ajuste() {

        Inventario ajuste = new Inventario();
        ajuste.setProducto(producto1);
        ajuste.setCantidad(5);
        ajuste.setTipoMovimiento("AJUSTE");
        ajuste.setFechaMovimiento(new Date());

        when(inventarioService.save(ajuste))
                .thenReturn(ajuste);

        Inventario resultado =
                inventarioController.registrarMovimiento(ajuste);

        assertNotNull(resultado);
        assertEquals("AJUSTE", resultado.getTipoMovimiento());
    }

    @Test
    void testActualizarMovimiento_Existente() {

        Inventario actualizado = new Inventario();
        actualizado.setId(1L);
        actualizado.setProducto(producto1);
        actualizado.setCantidad(15);
        actualizado.setTipoMovimiento("ENTRADA");
        actualizado.setFechaMovimiento(new Date());

        when(inventarioService.findById(1L))
                .thenReturn(movimiento1);

        when(inventarioService.save(any(Inventario.class)))
                .thenReturn(actualizado);

        ResponseEntity<Inventario> response =
                inventarioController.actualizarMovimiento(1L, actualizado);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());

        verify(inventarioService, times(1)).findById(1L);
        verify(inventarioService, times(1))
                .save(any(Inventario.class));
    }

    @Test
    void testActualizarMovimiento_NoExistente() {

        when(inventarioService.findById(999L))
                .thenReturn(null);

        ResponseEntity<Inventario> response =
                inventarioController.actualizarMovimiento(999L, movimiento1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(inventarioService, never())
                .save(any(Inventario.class));
    }

    @Test
    void testEliminarMovimiento_Existente() {

        when(inventarioService.findById(1L))
                .thenReturn(movimiento1);

        ResponseEntity<Void> response =
                inventarioController.eliminarMovimiento(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(inventarioService, times(1)).findById(1L);
        verify(inventarioService, times(1)).deleteById(1L);
    }

    @Test
    void testEliminarMovimiento_NoExistente() {

        when(inventarioService.findById(999L))
                .thenReturn(null);

        ResponseEntity<Void> response =
                inventarioController.eliminarMovimiento(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        verify(inventarioService, never()).deleteById(any());
    }
}