package edu.upc.prop.clusterxx;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * Clase de pruebas unitarias para la clase Shelve.
 * Realiza pruebas sobre la funcionalidad de la clase Shelve.
 */
public class TestShelve {
    private Shelve shelve;
    /**
     * Metodo que se ejecuta antes de cada prueba para inicializar el objeto Shelve.
     */
    @Before
    public void setUp() {
        String[] setup = {"manzana", "pera", "choco", "agua", "zumo", "arroz"};
        shelve = new Shelve(2, 3, setup);
    }
    /**
     * Verifica que el estante se inicializa correctamente.
     */
    @Test
    public void iniciaCorrectamente() {
        String[] setup = {"manzana", "pera", "choco", "agua", "zumo", "arroz"};
        assertArrayEquals(setup, shelve.getDistributionShelve().toArray(new String[0]));
        assertEquals(2, shelve.getRow());
        assertEquals(3, shelve.getColumn());
        assertEquals(6, shelve.dimensionShelve());
        assertEquals(6, shelve.numProducts());
    }
    /**
     * Verifica que el intercambio de productos funciona correctamente.
     */
    @Test
    public void intercambiaCorrecta() {
        String[] setup = {"pera", "manzana", "choco", "agua", "zumo", "arroz"};
        shelve.exchangePositions("manzana", "pera");
        assertArrayEquals(setup, shelve.getDistributionShelve().toArray(new String[0]));
        assertEquals(2, shelve.getRow());
        assertEquals(3, shelve.getColumn());
        assertEquals(6, shelve.dimensionShelve());
        assertEquals(6, shelve.numProducts());
    }
    /**
     * Verifica que la eliminacion de un producto funciona correctamente.
     */
    @Test
    public void eliminaCorrecta() {
        String[] setup = {"manzana", "-", "choco", "agua", "zumo", "arroz"};
        shelve.removeProduct("pera");
        assertArrayEquals(setup, shelve.getDistributionShelve().toArray(new String[0]));
        assertEquals(2, shelve.getRow());
        assertEquals(3, shelve.getColumn());
        assertEquals(6, shelve.dimensionShelve());
        assertEquals(5, shelve.numProducts());
    }
    /**
     * Verifica que se puede agregar un producto correctamente.
     */
    @Test
    public void anadeCorrecta() {
        String[] setup = {"manzana", "uva", "choco", "agua", "zumo", "arroz"};
        shelve.removeProduct("pera");
        shelve.addProduct("uva");
        assertArrayEquals(setup, shelve.getDistributionShelve().toArray(new String[0]));
        assertEquals(2, shelve.getRow());
        assertEquals(3, shelve.getColumn());
        assertEquals(6, shelve.dimensionShelve());
        assertEquals(6, shelve.numProducts());
    }
    /**
     * Verifica que se obtiene correctamente la representacion en cadena del estante.
     */
    @Test
    public void getDataCorrecto() {
        String setup = "[2,3,manzana;-;choco;agua;zumo;arroz]";
        shelve.removeProduct("pera");
        assertEquals(setup, shelve.getDataShelveAsString().toString());

    }
    /**
     * Verifica que se obtiene correctamente el numero de filas del estante.
     */
    @Test
    public void getRowShelveCorrect() {

        assertEquals(2, shelve.getRow());

    }
    /**
     * Verifica que se obtiene correctamente el numero de columnas del estante.
     */
    @Test
    public void getColumnShelveCorrect() {

        assertEquals(3, shelve.getColumn());

    }
    /**
     * Verifica que se obtiene correctamente la distribucion de productos en el estante.
     */
    @Test
    public void getDistributionCorrect() {
        ArrayList<String> Setup = new ArrayList<>();
        Setup.add("manzana");
        Setup.add("pera");
        Setup.add("choco");
        Setup.add("agua");
        Setup.add("zumo");
        Setup.add("arroz");
        assertArrayEquals(Setup.toArray(new String[0]), shelve.getDistributionShelve().toArray(new String[0]));

    }

    /**
     * Verifica que se lanza una excepcion cuando se intenta crear un estante con una dimension incorrecta.
     */
    @Test
    public void illArgCreacionDimension() {
        String[] setup = {"manzana", "pera", "choco", "agua", "zumo", "arroz"};
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Shelve stant = new Shelve(2, 2, setup);
                });
        assertEquals("Incorrect number of products, number of products must be 6", exception.getMessage());

    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta crear un estante con productos repetidos.
     */
    @Test
    public void illArgCreacionRepetidos() {
        String[] setup = {"pera", "pera", "choco", "agua", "zumo", "arroz"};
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Shelve stant = new Shelve(2, 3, setup);
                });
        assertEquals("repeated products", exception.getMessage());


    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta crear un estante con dimensiones negativas.
     */
    @Test
    public void illArgCreacionNegativo() {
        String[] setup = {"manzana"};
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    Shelve stant = new Shelve(-1, -1, setup);
                });
        assertEquals("Error: rows must be > 0 and columns must be > 0", exception.getMessage());

    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta intercambiar productos que no existen en el estante.
     */
    @Test
    public void illArgIntercambioNombreNoExiste1() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    shelve.exchangePositions("patatas", "pera");
                });
        assertEquals("The product patatas doesn't exist in the shelve", exception.getMessage());


    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta intercambiar productos que no existen en el estante.
     */
    @Test
    public void illArgIntercambioNombreNoExiste2() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    shelve.exchangePositions("pera", "patatas");
                });
        assertEquals("The product patatas doesn't exist in the shelve", exception.getMessage());

    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta intercambiar productos que no existen en el estante.
     */
    @Test
    public void illArgIntercambioNombreNoExiste3() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    shelve.exchangePositions("patatas", "cacahuetes");
                });
        assertEquals("The products patatas and cacahuetes don't exist in the shelve", exception.getMessage());
    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta eliminar un producto que no existe en el estante.
     */
    @Test
    public void illArgRemoveProductNotExist() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    shelve.removeProduct("patatas");
                });
        assertEquals("the product patatas doesn't exist in the shelve", exception.getMessage());
    }
    /**
     * Verifica que se lanza una excepcion cuando se intenta agregar un producto que ya existe en el estante.
     */
    @Test
    public void illArgAddProductExists() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> {
                    shelve.addProduct("manzana");
                });
        assertEquals("The product manzana already exists in the shelf", exception.getMessage());
    }
    /**
     * Verifica que se lanza una excepcion cuando no hay espacio disponible para agregar un nuevo producto.
     */
    @Test
    public void illArgAddFullShelve() {
        Exception exception = assertThrows(IllegalStateException.class,
                () -> {
                    shelve.addProduct("patatas");
                });
        assertEquals("No empty slot available to add the product patatas", exception.getMessage());
    }






    /**
     * Test para comprobar el correcto calculo de la distribucion con AlgortihmFast.
     */
    @Test
    public void CalcFastTest() {
        Similitud simil = new Similitud();
        simil.createProduct("manzana");
        simil.createProduct("pera");
        simil.createProduct("choco");
        simil.createProduct("agua");
        simil.createProduct("zumo");
        simil.createProduct("arroz");

        simil.createSimilitud("manzana", "pera", 0.6f);
        simil.createSimilitud("manzana", "choco", 0.7f);
        simil.createSimilitud("manzana", "agua", 0.4f);
        simil.createSimilitud("manzana", "zumo", 0.1f);
        simil.createSimilitud("manzana", "arroz", 0.2f);
        simil.createSimilitud("pera", "choco", 0.1f);
        simil.createSimilitud("pera", "agua", 0.4f);
        simil.createSimilitud("pera", "zumo", 0.3f);
        simil.createSimilitud("pera", "arroz", 0.4f);
        simil.createSimilitud("choco", "agua", 0.9f);
        simil.createSimilitud("choco", "zumo", 0.5f);
        simil.createSimilitud("choco", "arroz", 0.4f);
        simil.createSimilitud("agua", "zumo", 0.8f);
        simil.createSimilitud("agua", "arroz", 0.4f);
        simil.createSimilitud("zumo", "arroz", 0.6f);
        shelve.calculate_fast_distribution(simil);

        AlgorithmFast algF = new AlgorithmFast(2, 3, simil.getMatrix());
        algF.calcular_distribucion();

        ArrayList<Integer> newDistr = algF.get_distribucionFinal();
        ArrayList<String> combinedList = new ArrayList<>();
        for (int pid : newDistr) {
            combinedList.add(simil.traducir(pid));
        }
        assertNotNull(shelve.getDistributionShelve());
        assertEquals(6, shelve.getDistributionShelve().size()); // 6 products

    }
}


