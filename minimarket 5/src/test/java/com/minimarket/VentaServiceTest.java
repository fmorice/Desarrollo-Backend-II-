package com.minimarket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.minimarket.repository.VentaRepository;
import com.minimarket.service.impl.VentaServiceImpl; // Importación correcta según tu archivo
import com.minimarket.entity.Venta;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
    private VentaServiceImpl ventaService; // Ahora sí reconoce el nombre de tu clase

    @Test
    void testFindAll() {
        when(ventaRepository.findAll()).thenReturn(new ArrayList<>());
        List<Venta> resultado = ventaService.findAll();
        assertNotNull(resultado);
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    void testSave() {
        Venta v = new Venta();
        when(ventaRepository.save(v)).thenReturn(v);
        Venta guardada = ventaService.save(v);
        assertNotNull(guardada);
        verify(ventaRepository, times(1)).save(v);
    }
}