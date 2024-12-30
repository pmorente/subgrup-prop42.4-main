package edu.upc.prop.clusterxx;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Clase de pruebas para CjtEstadisticas.
 * Verifica el funcionamiento correcto de los métodos de la clase CjtEstadisticas,
 * asegurando que los datos se almacenen, ordenen y recuperen como se espera.
 */
public class TestCjtEstadisticas {

    private CjtEstadisticas cjtEstadisticas;
    private Estadisticas estadistica1;
    private Estadisticas estadistica2;
    private Estadisticas estadistica3;

    /**
     * Configura el entorno de pruebas.
     * Inicializa una nueva instancia de CjtEstadisticas y añade tres objetos de tipo Estadisticas
     * con diferentes atributos para garantizar un entorno de prueba consistente y controlado.
     */
    @Before
    public void setUp() {
        cjtEstadisticas = new CjtEstadisticas();

        // Crear objetos Estadisticas
        estadistica1 = new Estadisticas(new ArrayList<>(Arrays.asList("A", "B", "C")), 3, 5, 2.5, 19.0, 50);
        estadistica2 = new Estadisticas(new ArrayList<>(Arrays.asList("D", "E", "F")), 4, 6, 3.5, 20.0, 100);
        estadistica3 = new Estadisticas(new ArrayList<>(Arrays.asList("G", "H", "I")), 5, 7, 4.5, 5.0, 75);

        // Añadirlas al conjunto
        cjtEstadisticas.addEstadistica(estadistica1);
        cjtEstadisticas.addEstadistica(estadistica2);
        cjtEstadisticas.addEstadistica(estadistica3);
    }

    /**
     * Verifica que los objetos Estadisticas se añaden correctamente al conjunto.
     * Comprueba que el tamaño de las listas internas coincida con el número de objetos añadidos.
     */
    @Test
    public void testAddEstadistica() {
        assertEquals("El número de Estadisticas en la colección debe ser 3.", 3, cjtEstadisticas.getEstadisticasSortedByCreationDate().size());
        assertEquals("El número de Estadisticas ordenadas por iteraciones debe ser 3.", 3, cjtEstadisticas.getNumberOfIterationsList().size());
        assertEquals("El número de Estadisticas ordenadas por coste debe ser 3.", 3, cjtEstadisticas.getCostList().size());
    }

    /**
     * Verifica que los objetos Estadisticas se ordenen correctamente por fecha de creación,
     * de más reciente a más antiguo.
     */
    @Test
    public void testGetEstadisticasSortedByCreationDate() {
        List<Estadisticas> sortedByDate = cjtEstadisticas.getEstadisticasSortedByCreationDate();

        assertEquals("La Estadistica más reciente debe estar en la primera posición.",
                estadistica3.getTimestamp(), sortedByDate.get(0).getTimestamp());
        assertEquals("La segunda Estadistica más reciente debe estar en la segunda posición.",
                estadistica2.getTimestamp(), sortedByDate.get(1).getTimestamp());
        assertEquals("La Estadistica más antigua debe estar en la última posición.",
                estadistica1.getTimestamp(), sortedByDate.get(2).getTimestamp());
    }

    /**
     * Verifica que los objetos Estadisticas se ordenen correctamente por el número de iteraciones
     * en orden descendente.
     */
    @Test
    public void testGetNumberOfIterationsList() {
        List<Estadisticas> sortedByIterations = cjtEstadisticas.getNumberOfIterationsList();

        assertEquals("La Estadistica con mayor número de iteraciones debe estar en la primera posición.", estadistica2, sortedByIterations.get(0));
        assertEquals("La Estadistica con el segundo mayor número de iteraciones debe estar en la segunda posición.", estadistica3, sortedByIterations.get(1));
        assertEquals("La Estadistica con menor número de iteraciones debe estar en la última posición.", estadistica1, sortedByIterations.get(2));
    }

    /**
     * Verifica que los objetos Estadisticas se ordenen correctamente por coste
     * en orden descendente.
     */
    @Test
    public void testGetCostList() {
        List<Estadisticas> sortedByCost = cjtEstadisticas.getCostList();
        assertEquals("La Estadistica con mayor coste debe estar en la primera posición.", estadistica2, sortedByCost.get(0));
        assertEquals("La Estadistica con el segundo mayor coste debe estar en la segunda posición.", estadistica1, sortedByCost.get(1));
        assertEquals("La Estadistica con menor coste debe estar en la última posición.", estadistica3, sortedByCost.get(2));
    }


    /**
     * Verifica que el método reset elimina todos los objetos Estadisticas almacenados
     * en el conjunto, dejando las listas internas vacías.
     */
    @Test
    public void testReset() {
        cjtEstadisticas.reset();

        assertTrue("Después de resetear, la colección ordenada por fecha de creación debe estar vacía.", cjtEstadisticas.getEstadisticasSortedByCreationDate().isEmpty());
        assertTrue("Después de resetear, la colección ordenada por iteraciones debe estar vacía.", cjtEstadisticas.getNumberOfIterationsList().isEmpty());
        assertTrue("Después de resetear, la colección ordenada por coste debe estar vacía.", cjtEstadisticas.getCostList().isEmpty());
    }
}
