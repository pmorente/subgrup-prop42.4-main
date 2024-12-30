package edu.upc.prop.clusterxx;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

import edu.upc.prop.clusterxx.csvUtils.CsvExport;
import edu.upc.prop.clusterxx.csvUtils.CsvExportCjtProduct;
import edu.upc.prop.clusterxx.csvUtils.CsvExportDefault;
import edu.upc.prop.clusterxx.csvUtils.CsvExportEstadisticas;



public class TestCsvExport {
    private static final String TEST_FILE_PATH_CJTPRODUCT = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_cjtProduct.csv").toString();
    private static final String TEST_FILE_PATH_SIMILITUD = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Similitud.csv").toString();
    private static final String TEST_FILE_PATH_SHELVE = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Shelve.csv").toString();
    private static final String TEST_FILE_PATH_ESTADISTICAS = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Estadisticas.csv").toString();

    private CjtProduct catalog;
    private Similitud similarities;
    private Shelve shelve;
    private Estadisticas stats;

    private CsvExport<CjtProduct> exporterCjt;
    private CsvExport<List<String>> exporterDefault;
    private CsvExport<Estadisticas> exporterStats;

    /**
     * Metodo que se ejecuta antes de cada prueba. Inicializa los objetos
     * necesarios para realizar las pruebas.
     */
    @Before
    public void setUp() {
        // Initialize a CjtProduct with sample products
        catalog = new CjtProduct();
        catalog.createProduct("Manzanas", 1.0, "Manzanas de la huerta");
        catalog.createProduct("Peras", 2.0, "Peras del norte");
        catalog.createProduct("Aceitunas", 3.0, "Aceitunas con hueso");
 
        similarities = new Similitud();
        similarities.createProduct("Manzanas");
        similarities.createProduct("Peras");
        similarities.createProduct("Aceitunas");
        similarities.createSimilitud("Manzanas", "Peras", 0.5f);
        similarities.createSimilitud("Manzanas", "Aceitunas", 0.6f);
        similarities.createSimilitud("Peras", "Aceitunas", 0.7f);

        String[] products = {"Manzanas", "Peras", "Aceitunas"};
        shelve = new Shelve(1, 3, products);


        ArrayList<String> distribution = new ArrayList<>();
        distribution.add("Manzanas");
        distribution.add("Peras");
        distribution.add("Aceitunas");
        stats = new Estadisticas(distribution, 3, 1, 0.5, 0.6, 1250);
        // Initialize exporters
        exporterCjt = new CsvExportCjtProduct();
        exporterDefault = new CsvExportDefault();
        exporterStats = new CsvExportEstadisticas();
        
    }

    /**
     * Metodo que se ejecuta despues de cada prueba. Limpia los archivos generados.
     * @throws IOException Si ocurre un error al eliminar los archivos
     */
    @After
    public void tearDown() throws IOException {
        // Clean up test file after each test
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_ESTADISTICAS));
    }
    /**
     * Prueba que verifica la exportacion correcta de datos de productos a un archivo CSV.
     * @throws IOException Si ocurre un error durante la exportacion
     */
    @Test
    public void testExportDataCjtProductCorrect() throws IOException {

        // Export products to CSV file in the Downloads folder
        exporterCjt.exportData(catalog, TEST_FILE_PATH_CJTPRODUCT);

        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH_CJTPRODUCT)));

        // Read the file contents and verify it matches expected output
        List<String> expectedLines = Arrays.asList(
                "Manzanas,1.0,Manzanas de la huerta",
                "Peras,2.0,Peras del norte",
                "Aceitunas,3.0,Aceitunas con hueso"
        );

        List<String> actualLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH_CJTPRODUCT))) {
            actualLines = reader.lines().toList();
        }

        assertEquals(expectedLines.size(), actualLines.size());
        assertTrue(actualLines.containsAll(expectedLines));
    }
    /**
     * Prueba que verifica la exportacion correcta de datos de similitudes a un archivo CSV.
     * @throws IOException Si ocurre un error durante la exportacion
     */
    @Test
    public void testExportDataSimilitudCorrect() throws IOException {
        List<String> dataToExport = similarities.getSimilaritiesList();
        exporterDefault.exportData(dataToExport, TEST_FILE_PATH_SIMILITUD);

        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH_SIMILITUD)));

        // Read the file contents and verify it matches expected output
        List<String> expectedLines = Arrays.asList(
                "Manzanas,Peras,0.5",
                "Manzanas,Aceitunas,0.6",
                "Peras,Aceitunas,0.7"
        );

        List<String> actualLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH_SIMILITUD))) {
            actualLines = reader.lines().toList();
        }

        assertEquals(expectedLines.size(), actualLines.size());
        assertTrue(actualLines.containsAll(expectedLines));
    }

    /**
     * Prueba que verifica la exportacion correcta de datos de estantes (Shelve) a un archivo CSV.
     * @throws IOException Si ocurre un error durante la exportacion
     */
    @Test
    public void testExportDataShelveCorrect() throws IOException {
        List<String> dataToExport = shelve.getDataShelveAsString();
        exporterDefault.exportData(dataToExport, TEST_FILE_PATH_SHELVE);

        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH_SHELVE)));

        // Read the file contents and verify it matches expected output
        List<String> expectedLines = List.of(
                "1,3,Manzanas;Peras;Aceitunas"
        );

        List<String> actualLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH_SHELVE))) {
            actualLines = reader.lines().toList();
        }

        assertEquals(expectedLines.size(), actualLines.size());
        assertTrue(actualLines.containsAll(expectedLines));
    }

    /**
     * Prueba que verifica la exportacion correcta de datos estadisticos a un archivo CSV.
     * @throws IOException Si ocurre un error durante la exportacion
     */
    @Test
    public void testExportDataEstadisticasCorrect() throws IOException {
        exporterStats.exportData(stats, TEST_FILE_PATH_ESTADISTICAS);

        assertTrue(Files.exists(Paths.get(TEST_FILE_PATH_ESTADISTICAS)));

        // Read the file contents and verify it matches expected output
        List<String> expectedLines = List.of(
                "+------------+------------+------------+",
                "| Manzanas  | Peras     | Aceitunas  |",
                "+------------+------------+------------+",
                " ",
                "Time: 0.5",
                "Cost: 0.6",
                "Number of iterations: 1250"
        );

        List<String> actualLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(TEST_FILE_PATH_ESTADISTICAS))) {
            actualLines = reader.lines().toList();
        }

        assertEquals(expectedLines.size(), actualLines.size());
    }
}
