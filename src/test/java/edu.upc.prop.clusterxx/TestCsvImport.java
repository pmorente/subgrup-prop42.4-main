package edu.upc.prop.clusterxx;
import edu.upc.prop.clusterxx.csvUtils.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import static org.junit.Assert.*;


/**
 * Clase de prueba para las clases importadoras de CSV.
 * Proporciona un conjunto de pruebas unitarias para verificar el correcto funcionamiento
 * de las clases de importacion de datos desde archivos CSV hacia las clases relacionadas,
 * como CjtProduct, Similitud y Shelve.
 * Nota: Hay ciertas excepciones que no son manejadas directamente por las clases importadoras,
 * ya que las validaciones especificas son responsabilidad de las clases receptoras (CjtProduct,
 * Similitud, Shelve). Por ejemplo:
 * - Valores negativos en los datos importados no son rechazados por la clase importadora,
 *   pero las clases receptoras lanzaran excepciones al validar estos datos.
 */
public class TestCsvImport {
    private static final String TEST_FILE_PATH_CJTPRODUCT = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_cjtProduct.csv").toString();
    private static final String TEST_FILE_PATH_SIMILITUD = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Similitud.csv").toString();
    private static final String TEST_FILE_PATH_SHELVE = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Shelve.csv").toString();
    private static final String TEST_FILE_PATH_ESTADISTICAS = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Estadisticas.csv").toString();




    private CjtProduct catalog;
    private Similitud similarities;
    private Shelve shelve;
    private Estadisticas stats;

    private CsvImport importer_cjtProduct;
    private CsvImport importer_similitud;
    private CsvImport importer_shelve;


    /**
     * Configura el entorno de prueba antes de cada test.
     * Inicializa los objetos necesarios y las instancias de las clases importadoras.
     */
    @Before
    public void setUp() {
        // Initialize a CjtProduct with sample products
        catalog = new CjtProduct();
        similarities = new Similitud();

        // Initialize importers
        importer_cjtProduct = new CsvImportCjtProduct();
        importer_similitud = new CsvImportSimilitud();
        importer_shelve = new CsvImportShelve();

    }
    /**
     * Limpia los archivos generados durante las pruebas.
     * Elimina los archivos CSV creados en cada prueba para garantizar un entorno limpio.
     *
     * @throws IOException si ocurre un error al eliminar los archivos.
     */
    @After
    public void tearDown() throws IOException {
        // Clean up test files after each test
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_ESTADISTICAS));
    }
    /**
     * Verifica la importacion de datos de productos desde un archivo CSV.
     * Crea un archivo CSV con datos de productos y luego importa esos datos
     * para verificar que se hayan importado correctamente.
     *
     * @throws IOException si ocurre un error al manipular el archivo CSV.
     */
    @Test
    public void testImportDataCjtProduct() throws IOException {
        // Create a CSV file with sample product data
        List<String> csvLines = Arrays.asList(
                "Manzanas,1.0,Manzanas de la huerta",
                "Peras,2.0,Peras del norte",
                "Aceitunas,3.0,Aceitunas con hueso"
        );
        Files.createFile(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        Files.write(Paths.get(TEST_FILE_PATH_CJTPRODUCT), csvLines);

        // Import products from CSV file
        List<Object[]> importedData = importer_cjtProduct.importData(TEST_FILE_PATH_CJTPRODUCT);

        // Verify the imported products (in Object[] form)
        assertEquals(3, importedData.size());

        // Create a set of expected product names
        Set<String> expectedProductNames = new HashSet<>(Arrays.asList("Manzanas", "Peras", "Aceitunas"));

        // Print the expected product names
        System.out.println("Expected product names: " + expectedProductNames);

        // Process each imported product data
        for (Object[] productData : importedData) {
            String productName = (String) productData[0];
            System.out.println("Imported product: " + productName);
            assertTrue("Product should exist in the expected names", expectedProductNames.contains(productName));
        }
    }
    /**
     * Verifica la importacion de datos de similitudes desde un archivo CSV.
     * Crea un archivo CSV con datos de similitudes entre productos y los importa,
     * comprobando que los datos se hayan cargado correctamente.
     *
     * @throws IOException si ocurre un error al manipular el archivo CSV.
     */
    @Test
    public void testImportDataSimilitud() throws IOException {

        // Creating the products in the Similarity matrix (The Controller would do this)
        similarities.createProduct("Manzanas");
        similarities.createProduct("Peras");
        similarities.createProduct("Aceitunas");

        // Create a CSV file with sample similarity data
        List<String> csvLines = Arrays.asList(
                "Manzanas,Peras,0.8",
                "Manzanas,Aceitunas,0.5",
                "Peras,Aceitunas,0.6"
        );
        Files.createFile(Paths.get(TEST_FILE_PATH_SIMILITUD));
        Files.write(Paths.get(TEST_FILE_PATH_SIMILITUD), csvLines);

        // Import similarities from CSV file
        List<Object[]> importedData = importer_similitud.importData(TEST_FILE_PATH_SIMILITUD);
        assertEquals(3, importedData.size());
        // Create a set of expected similarity pairs and their coefficients
        Map<String, Float> expectedSimilarities = new HashMap<>();
        expectedSimilarities.put("Manzanas,Peras", 0.8f);
        expectedSimilarities.put("Manzanas,Aceitunas", 0.5f);
        expectedSimilarities.put("Peras,Aceitunas", 0.6f);

        // Print the expected similarities
        System.out.println("Expected similarities: " + expectedSimilarities);

        // Process each imported similarity data
        for (Object[] similarityData : importedData) {
            String product1 = (String) similarityData[0];
            String product2 = (String) similarityData[1];
            float coefficient = (Float) similarityData[2];
            System.out.println("Imported similarity: " + product1 + " <-> " + product2 + " : " + coefficient);

            String key = product1 + "," + product2;
            assertTrue("Similarity should exist in the expected map", expectedSimilarities.containsKey(key));
            assertEquals("Similarity coefficient should match", expectedSimilarities.get(key), coefficient, 0.0);
        }
    }

    /**
     * Verifica la importacion de datos de estanterias desde un archivo CSV.
     * Crea un archivo CSV con datos de distribucion de productos en estanterias y
     * comprueba que se importen correctamente.
     *
     * @throws IOException si ocurre un error al manipular el archivo CSV.
     */
    @Test
    public void testImportDataShelve() throws IOException {
        // Create a CSV file with sample product data
        List<String> csvLines = List.of(
                "1,3,Manzanas;Peras;Aceitunas"
        );
        Files.createFile(Paths.get(TEST_FILE_PATH_SHELVE));
        Files.write(Paths.get(TEST_FILE_PATH_SHELVE), csvLines);

        // Import products from CSV file
        List<Object[]> importedData = importer_shelve.importData(TEST_FILE_PATH_SHELVE);

        // Verify the imported products (in Object[] form)
        assertEquals(1, importedData.size());

        assertEquals(1, importedData.get(0)[0]);
        assertEquals(3, importedData.get(0)[1]);
        assertArrayEquals(new String[]{"Manzanas", "Peras", "Aceitunas"}, (String[]) importedData.get(0)[2]);
    }
    /**
     * Verifica el comportamiento al intentar importar datos desde un archivo CSV invalido.
     * Crea un archivo CSV con formato incorrecto y verifica que se lance una excepcion.
     */
    @Test
    public void testImportDataCjtProductWithInvalidFile() {
        // Creating an invalid CSV file
        List<String> invalidCsvLines = Arrays.asList(
                "Manzanas", // Missing other required fields
                "Peras,abc,3.0", // Second field is not a number
                "Aceitunas,3.0" // Missing description
        );

        // Create the invalid CSV file
        String invalidFilePath = Paths.get(System.getProperty("user.home"), "Downloads", "test_invalid_cjtProduct.csv").toString();
            try{
            Files.createFile(Paths.get(invalidFilePath));
            Files.write(Paths.get(invalidFilePath), invalidCsvLines);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Attempt to import data from the invalid CSV file
            try {
                List<Object[]> importedData = importer_cjtProduct.importData(invalidFilePath);
                fail("Expected an exception to be thrown due to invalid data, but none was thrown.");
            } catch (Exception e) {
                // Expected exception

            }
        try {
            Files.deleteIfExists(Paths.get(invalidFilePath));
        } catch (IOException e) {
            System.out.println("The file has been already deleted, you might have commented the deleteIfExists method");
        }
    }
    /**
     * Verifica el comportamiento al intentar importar datos desde un archivo CSV invalido.
     * Crea un archivo CSV con formato incorrecto y verifica que se lance una excepcion.
     */
    @Test
    public void testImportDataSimilitudWithInvalidFile() {
        // Creating an invalid CSV file
        List<String> invalidCsvLines = Arrays.asList(
                "Manzanas,Peras",
                "Manzanas,0.5,0.5",
                "Peras"
        );

        // Create the invalid CSV file
        String invalidFilePath = Paths.get(System.getProperty("user.home"), "Downloads", "test_invalid_Similitud.csv").toString();
        try{
            Files.createFile(Paths.get(invalidFilePath));
            Files.write(Paths.get(invalidFilePath), invalidCsvLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Attempt to import data from the invalid CSV file
        try {
            List<Object[]> importedData = importer_similitud.importData(invalidFilePath);
            fail("Expected an exception to be thrown due to invalid data, but none was thrown.");
        } catch (Exception e) {
            System.out.println("Expected exception: " + e.getMessage());
            // Expected exception
        }

        try {
            Files.deleteIfExists(Paths.get(invalidFilePath));
        } catch (IOException e) {
            System.out.println("The file has been already deleted, you might have commented the deleteIfExists method");
        }
    }
    /**
     * Verifica el comportamiento al intentar importar datos desde un archivo CSV invalido.
     * Crea un archivo CSV con formato incorrecto y verifica que se lance una excepcion.
     */
    @Test
    public void testImportDataShelveWithInvalidFile() {
        // Creating an invalid CSV file
        //Here we tried different variations
        //Tried variations:
        // 1,3,Manzanas;Peras;Aceitunas;Platanos
        // 1,3,Manzanas;Peras;
        // peras,3,Manzanas;Peras;Aceitunas
        List<String> invalidCsvLines = List.of(
                "1,3,Manzanas;Peras;" // Too many products

        );


        // Create the invalid CSV file
        String invalidFilePath = Paths.get(System.getProperty("user.home"), "Downloads", "test_invalid_Shelve.csv").toString();
        try{
            Files.createFile(Paths.get(invalidFilePath));
            Files.write(Paths.get(invalidFilePath), invalidCsvLines);
        } catch (IOException e) {
            System.out.println("The file has been already created, you might have commented the deleteIfExists method");
            e.printStackTrace();
        }
        // Attempt to import data from the invalid CSV file
        try {
            List<Object[]> importedData = importer_shelve.importData(invalidFilePath);
            // Print all elements of importedData
            for (Object[] dataArray : importedData) {
                System.out.println("Element:");
                for (Object element : dataArray) {
                    if (element instanceof String[]) {
                        System.out.println(" - String[]:");
                        for (String str : (String[]) element) {
                            System.out.println("   - " + str);
                        }
                    } else {
                        System.out.println(" - " + element);
                    }
                }
            }
            fail("Expected an exception to be thrown due to invalid data, but none was thrown.");
        } catch (Exception e) {
            System.out.println("Expected exception: " + e.getMessage());
            // Expected exception
        }

        try {
            Files.deleteIfExists(Paths.get(invalidFilePath));
        } catch (IOException e) {
            System.out.println("The file has been already deleted, you might have commented the deleteIfExists method");
        }
    }



}
