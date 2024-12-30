package edu.upc.prop.clusterxx;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
/**
 * Clase de pruebas unitarias para la clase {@link Estadisticas}.
 * Verifica la funcionalidad de los métodos de la clase {@link Estadisticas}.
 */
public class TestEstadisticas {

    private Estadisticas estadisticas;
    /**
     * Metodo de configuración que se ejecuta antes de cada prueba.
     * Inicializa una instancia de {@link Estadisticas} con valores predefinidos.
     */
    @Before
    public void setUp() {
        estadisticas = new Estadisticas(new ArrayList<>(Arrays.asList("X", "Y", "Z")), 10, 20, 5.0, 15.5, 30);
    }
    /**
     * Prueba el metodo {@link Estadisticas#getDistribution()}.
     * Verifica que se devuelva correctamente la distribución inicial.
     */
    @Test
    public void testGetDistribution() {
        assertEquals(Arrays.asList("X", "Y", "Z"), estadisticas.getDistribution());
    }
    /**
     * Prueba el metodo {@link Estadisticas#getColumns()}.
     * Verifica que se devuelva correctamente el número de columnas.
     */
    @Test
    public void testGetColumns() {
        assertEquals(10, estadisticas.getColumns());
    }
    /**
     * Prueba el metodo {@link Estadisticas#getRows()}.
     * Verifica que se devuelva correctamente el número de filas.
     */
    @Test
    public void testGetRows() {
        assertEquals(20, estadisticas.getRows());
    }
    /**
     * Prueba el metodo {@link Estadisticas#getTime()}.
     * Verifica que se devuelva correctamente el tiempo de ejecución.
     */
    @Test
    public void testGetTime() {
        assertEquals(5.0, estadisticas.getTime(), 0.001);
    }
    /**
     * Prueba el metodo {@link Estadisticas#getCost()}.
     * Verifica que se devuelva correctamente el costo asociado.
     */
    @Test
    public void testGetCost() {
        assertEquals(15.5, estadisticas.getCost(), 0.001);
    }
    /**
     * Prueba el metodo {@link Estadisticas#getNumberIterations()}.
     * Verifica que se devuelva correctamente el número de iteraciones.
     */
    @Test
    public void testGetNumberIterations() {
        assertEquals(30, estadisticas.getNumberIterations());
    }
    /**
     * Prueba el metodo {@link Estadisticas#getTimestamp()}.
     * Verifica que el timestamp generado no sea nulo.
     */
    @Test
    public void testGetTimestamp() {
        assertNotNull(estadisticas.getTimestamp());
    }
}

