
package edu.upc.prop.clusterxx;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
/**
 * Clase de pruebas unitarias para la clase {@link Similitud}.
 * Verifica la funcionalidad de las operaciones principales relacionadas con productos y sus similitudes.
 */
public class TestSimilitud {

    private Similitud simi;
    /**
     * Configura el entorno de pruebas antes de cada prueba unitaria.
     * Se inicializa una nueva instancia de {@link Similitud}.
     */
    @Before
    public void setUp() {
        
        simi = new Similitud();

    }
    /**
     * Verifica que un producto se cree correctamente.
     */
    @Test
    public void createProductCorrectly() {

        simi.createProduct("fresa");
        assertEquals("fresa",simi.traducir(0));
        assertEquals(0, simi.traducir_id("fresa"));

    }
    /**
     * Verifica que no se puedan crear productos duplicados.
     * Se espera una excepción de tipo {@link IllegalArgumentException}.
     */
    @Test
    public void createDoubledProductIncorrectly() {

        simi.createProduct("fresa");
        try {
            simi.createProduct("fresa");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product already exists: fresa", e.getMessage());
        }

    }
    /**
     * Verifica que se pueda eliminar un producto correctamente, y que las similitudes asociadas se manejen adecuadamente.
     */
    @Test
    public void removeProductCorrectly() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");
        simi.createProduct("pera");
        simi.createSimilitud("fresa", "manzana", 0.9f);
        simi.createSimilitud("pera", "manzana", 0.8f);
        simi.createSimilitud("fresa", "pera", 0.7f);

        simi.removeProduct("fresa");

        try {
            simi.consultarSimilitud("fresa", "manzana");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product1 doesn't exists: fresa", e.getMessage());
        }
        simi.healthCheck();
        
        try {
            simi.traducir(2);            
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Tried to get product with id 2 but max id is 1", e.getMessage());
        }
        try {
            simi.traducir_id("fresa");            
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("El producto que intentas traducir no existe", e.getMessage());
        }
        
        assertEquals(0.8, simi.consultarSimilitud("pera", "manzana"), 0.0001f);
        assertEquals(0.8, simi.consultarSimilitud("manzana", "pera"), 0.0001f);

        assertEquals("pera", simi.traducir(0));
        assertEquals("manzana", simi.traducir(1));
        try {
            simi.consultarSimilitud(0, 0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Relation between products doesn't exists: pera pera", e.getMessage());
        }

    }
    /**
     * Verifica que no se puedan eliminar productos que no existen.
     */
    @Test
    public void removeProductIncorrectly() {
        try {
            simi.removeProduct("pera");         
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
        assertEquals("Product with name pera does not exist.", e.getMessage());
        }
    }
    /**
     * Verifica que se puedan crear correctamente y actualizar, las similitudes entre productos.
     */
    @Test
    public void createSimilitudCorrectly() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");
        simi.createSimilitud("fresa", "manzana", 0.9f);
        assertEquals(0.9, simi.consultarSimilitud("fresa", "manzana"), 0.0001f);
        assertEquals(0.9, simi.consultarSimilitud("manzana", "fresa"), 0.0001f);
        assertEquals(0.9, simi.consultarSimilitud(0, 1), 0.0001f);
        assertEquals(0.9, simi.consultarSimilitud(1, 0), 0.0001f);
        
        simi.createSimilitud("fresa", "manzana", 0.7f);
        assertEquals(0.7, simi.consultarSimilitud("fresa", "manzana"), 0.0001f);
        assertEquals(0.7, simi.consultarSimilitud("manzana", "fresa"), 0.0001f);
        assertEquals(0.7, simi.consultarSimilitud(0, 1), 0.0001f);
        assertEquals(0.7, simi.consultarSimilitud(1, 0), 0.0001f);


    }
    /**
     * Verifica que las consultas a similitudes inexistentes arrojen excepciones adecuadas.
     */
    @Test
    public void similitudDoesntExist() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");

        try {
            simi.consultarSimilitud("fresa", "manzana");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Relation between products doesn't exists: fresa manzana", e.getMessage());
        }

        try {
            simi.consultarSimilitud(0, 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Relation between products doesn't exists: fresa manzana", e.getMessage());
        }

    }
    /**
     * Verifica que las operaciones sobre productos inexistentes arrojen excepciones adecuadas.
     */
    @Test
    public void productDoesntExist() {
        try {
            simi.createSimilitud("fresa", "manzana", 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product1 doesn't exists: fresa", e.getMessage());
        }

        simi.createProduct("fresa");

        try {
            simi.createSimilitud("fresa", "manzana", 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product2 doesn't exists: manzana", e.getMessage());
        }
        
        
        try {
            simi.consultarSimilitud("manzana", "fresa");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product1 doesn't exists: manzana", e.getMessage());
        }
        try {
            simi.consultarSimilitud("fresa", "manzana");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product2 doesn't exists: manzana", e.getMessage());
        }

        try {
            simi.consultarSimilitud(2, 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Tried to get product with id 2 but max id is 0", e.getMessage());
        }
        try {
            simi.consultarSimilitud(0, 1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Tried to get product with id 1 but max id is 0", e.getMessage());
        }


        try {
            simi.eliminarSimilitud("manzana", "fresa");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product1 doesn't exists: manzana", e.getMessage());
        }
        try {
            simi.eliminarSimilitud("fresa", "manzana");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Product2 doesn't exists: manzana", e.getMessage());
        }


    }

    /**
     * Verifica que los coeficientes invalidos (fuera del rango 0-1) no sean aceptados al crear similitudes.
     */
    @Test
    public void incorrectCoeficient() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");
        
        try {
            simi.createSimilitud("fresa", "manzana", 2);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The coeficient isn't valid, it should be a value between 0 and 1(not included), and instead it's: 2.0", e.getMessage());
        }

        try {
            simi.createSimilitud("fresa", "manzana", -1);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("The coeficient isn't valid, it should be a value between 0 and 1(not included), and instead it's: -1.0", e.getMessage());
        }

    }
    /**
     * Verifica que se puedan eliminar correctamente las similitudes entre productos.
     */
    @Test
    public void deleteSimilitudCorrectly() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");
        simi.createSimilitud("fresa", "manzana", 0.8f);
        
        assertEquals(0.8, simi.consultarSimilitud("fresa", "manzana"), 0.0001f);
        assertEquals(0.8, simi.consultarSimilitud("manzana", "fresa"), 0.0001f);
        
        simi.eliminarSimilitud("fresa", "manzana");

        try {
            simi.consultarSimilitud("fresa", "manzana");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Relation between products doesn't exists: fresa manzana", e.getMessage());
        }

        try {
            simi.consultarSimilitud("manzana", "fresa");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Relation between products doesn't exists: manzana fresa", e.getMessage());
        }
    }
    /**
     * Verifica que el metodo de chequeo de salud ({@link Similitud#healthCheck()}) funcione correctamente.
     */
    @Test
    public void healthCheckCorrectly() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");
        simi.createSimilitud("fresa", "manzana", 0.98f);
        
        simi.healthCheck();

    }
    /**
     * Verifica que el metodo de chequeo de salud lance excepciones cuando faltan similitudes.
     */
    @Test
    public void healthCheckIncorrectly() {

        simi.createProduct("fresa");
        simi.createProduct("manzana");
        
        try {
            simi.healthCheck();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (NumberFormatException e) {
            assertEquals("The following two products still do not have Similarity: fresa manzana\n", e.getMessage());
        }

    }

    /**
     * Verifica que se puedan obtener las similitudes de un producto en una lista correctamente.
     */
    @Test
    public void getSimilaritiesListCorrectly() {
        
        assertEquals("[]",simi.getSimilaritiesList().toString());
       
        simi.createProduct("fresa");
        simi.createProduct("manzana");
        simi.createProduct("pera");
        simi.createSimilitud("fresa", "manzana", 0.9f);
        simi.createSimilitud("pera", "manzana", 0.5f);

        assertEquals("[fresa,manzana,0.9, manzana,pera,0.5]",simi.getSimilaritiesList().toString());


    }
    

   
}

