package edu.upc.prop.clusterxx;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Clase de prueba para la clase {@link SessionManager}.
 * Contiene pruebas unitarias para verificar el correcto funcionamiento de los procesos de
 * importacion y exportacion de sesiones, así como el manejo de errores en diferentes casos.
 */
public class TestSessionManager {

    private SessionManager sessionManager;
    private static final Path TEST_FILE_PATH = Paths.get(System.getProperty("user.home"), "Downloads", "testSession.csv");
    private static final Path TEST_FILE_PATH_EXPORT = Paths.get(System.getProperty("user.home"), "Downloads", "CHECK_testSessionExport.csv");
    /**
     * Configuracion previa a cada prueba.
     * Inicializa una instancia de {@link SessionManager}.
     *
     * @throws IllegalArgumentException si ocurre un error al inicializar el gestor de sesiones.
     */
    @Before
    public void setUp() {

        try{
            this.sessionManager = new SessionManager();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error al inicialitzar el gestor de Sessions");
        }

    }
    /**
     * Limpieza posterior a cada prueba.
     * Elimina los archivos de prueba creados durante las pruebas para evitar residuos.
     */
    @After
    public void tearDown() {
        try {
            Files.deleteIfExists(TEST_FILE_PATH);
            Files.deleteIfExists(TEST_FILE_PATH_EXPORT);

            File userFolder = new File("Users/");
            if (userFolder.exists()) {
                deleteFolderRecursively(userFolder);
            }
        } catch (IOException e) {
            System.err.println("Error deleting files during tearDown: " + e.getMessage());
        }
    }

    private void deleteFolderRecursively(File folder) {
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolderRecursively(file);
                }
            }
        }
        folder.delete();
    }

    /**
     * Prueba la creación de un nuevo usuario.
     * Verifica que se crea correctamente la carpeta del usuario y el archivo SESSION_USER_DATA.csv.
     *
     * @throws Exception si ocurre un error durante el proceso.
     */
    @Test
    public void testCreateUser() throws Exception {
        String username = "testUser";
        String userFilePath = sessionManager.createUser(username);

        // Verificar que la carpeta del usuario se creó correctamente
        File userFolder = new File("Users" + File.separator + username);
        assertTrue("La carpeta del usuario no fue creada.", userFolder.exists());

        // Verificar que el archivo SESSION_USER_DATA.csv fue creado
        File userFile = new File(userFilePath);
        assertTrue("El archivo SESSION_USER_DATA.csv no fue creado.",userFile.exists());
    }

    /**
     * Prueba la creación de un usuario duplicado.
     * Verifica que se lanza una excepción con el mensaje "The user already exists.".
     *
     * @throws Exception si ocurre un error durante el proceso.
     */
    @Test
    public void testCreateUserDuplicate() throws Exception {
        String username = "testUser";
        sessionManager.createUser(username);

        Exception exception = assertThrows(Exception.class, () -> {
            sessionManager.createUser(username);
        });

        assertEquals("The user already exists.", exception.getMessage());
    }

    /**
     * Prueba la eliminación de un usuario existente.
     * Verifica que la carpeta del usuario es eliminada correctamente.
     *
     * @throws Exception si ocurre un error durante el proceso.
     */
    @Test
   public void testDeleteUser() throws Exception {
        String username = "testUser";
        sessionManager.createUser(username);

        sessionManager.deleteUser(username);

        // Verificar que la carpeta del usuario fue eliminada
        File userFolder = new File("Users" + File.separator + username);
        assertFalse( "La carpeta del usuario no fue eliminada.", userFolder.exists());
    }

    /**
     * Prueba la eliminación de un usuario inexistente.
     * Verifica que se lanza una excepción con el mensaje "User does not exist.".
     */
    @Test
    public void testDeleteNonExistentUser() {
        Exception exception = assertThrows(Exception.class, () -> {
            sessionManager.deleteUser("nonExistentUser");
        });
        assertEquals("User does not exist.", exception.getMessage());
    }

    /**
     * Prueba la obtención de datos de usuario.
     * Verifica que los datos retornados coinciden con los valores esperados.
     *
     * @throws Exception si ocurre un error durante el proceso.
     */
    @Test
    public void testGetUserData() throws Exception {
        String username = "testUser";
        sessionManager.createUser(username);

        List<Object[]> userData = sessionManager.getUserData();

        assertNotNull("Los datos del usuario no deben ser nulos.",userData);
        assertEquals("El nombre del usuario no coincide.",username, userData.get(0)[0]);
    }

    /**
     * Prueba la importacion correcta de datos de sesion desde un archivo CSV.
     * Verifica que los datos importados coincidan con los valores esperados.
     *
     * @throws Exception si ocurre un error durante el proceso de importacion.
     */
    @Test
    public void testImportSession_Success() throws Exception {
        // Mock CSV file for the test
        List<String> fileContent = new ArrayList<>();
        fileContent.add("::Products::");
        fileContent.add("ProductA,10.5,DescriptionA");
        fileContent.add("ProductB,11.5,DescriptionB");
        fileContent.add("::END::");
        fileContent.add("::Similarities::");
        fileContent.add("ProductA,ProductB,0.8");
        fileContent.add("::END::");
        fileContent.add("::Shelf::");
        fileContent.add("1,2,ProductA;ProductB");
        fileContent.add("::END::");

        // Create a test file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_PATH.toFile()))) {
            for (String line : fileContent) {
                writer.write(line);
                writer.newLine();
            }
        }

        // Run the test method
        SessionData sessionData = sessionManager.importSession(TEST_FILE_PATH.toString());

        // Verify the session data
        assertNotNull(sessionData);
        assertNotNull(sessionData.getCjtProduct());
        assertNotNull(sessionData.getSimilitud());
        assertNotNull(sessionData.getShelve());

        assertEquals("ProductA", sessionData.getCjtProduct().getProductByName("ProductA").getName());
        assertEquals(10.5, sessionData.getCjtProduct().getProductByName("ProductA").getPrice(), 0.01);
        assertEquals("DescriptionA", sessionData.getCjtProduct().getProductByName("ProductA").getDescription());

        assertEquals("ProductB", sessionData.getCjtProduct().getProductByName("ProductB").getName());
        assertEquals(11.5, sessionData.getCjtProduct().getProductByName("ProductB").getPrice(), 0.01);
        assertEquals("DescriptionB", sessionData.getCjtProduct().getProductByName("ProductB").getDescription());

        assertEquals(0.8, sessionData.getSimilitud().consultarSimilitud("ProductA", "ProductB"), 0.01);

        assertEquals(1, sessionData.getShelve().getRow());
        assertEquals(2, sessionData.getShelve().getColumn());
        assertEquals(new ArrayList<>(List.of("ProductA", "ProductB")), sessionData.getShelve().getDistributionShelve());
    }

    /**
     * Prueba el caso en el que el archivo de sesion para importar no existe.
     * Verifica que se lance una excepcion apropiada.
     *
     * @throws Exception si ocurre un error durante el proceso de importacion.
     */
    @Test(expected = Exception.class)
    public void testImportSession_FileNotFound() throws Exception {
        // Attempt to import from a nonexistent file to trigger an IOException
        sessionManager.importSession(Paths.get(System.getProperty("user.home"), "Downloads", "nonexistent.csv").toString());
    }
    /**
     * Prueba la exportación exitosa de datos de sesion a un archivo CSV.
     * Verifica que el archivo exportado se haya creado correctamente.
     *
     * @throws Exception si ocurre un error durante el proceso de exportacion.
     */
    @Test
    public void testExportSession_Success() throws Exception {
        // Create mock data
        CjtProduct catalog = new CjtProduct();
        catalog.createProduct("ProductA", 10.5, "DescriptionA");
        catalog.createProduct("ProductB", 11.5, "DescriptionB");

        Similitud sim = new Similitud();
        sim.createProduct("ProductA");
        sim.createProduct("ProductB");
        sim.createSimilitud("ProductA", "ProductB", 0.8f);

        Shelve shelve = new Shelve(1, 2, new String[]{"ProductA", "ProductB"});

        // Run the export session
        sessionManager.exportSession(TEST_FILE_PATH_EXPORT.toString(), catalog, sim, shelve);

        // Verify that the file is created
        File exportedFile = TEST_FILE_PATH_EXPORT.toFile();
        assertTrue(exportedFile.exists());
    }
    /**
     * Prueba el caso en el que el archivo de exportacion ya existe.
     * Verifica que se lance una excepcion de tipo {@link IOException}.
     *
     * @throws IOException si ocurre un error durante el proceso de exportacion.
     */
    @Test(expected = IOException.class)
    public void testExportSession_FileAlreadyExists() throws Exception {
        // Create a file beforehand to trigger an error
        File testFile = TEST_FILE_PATH_EXPORT.toFile();
        if (!testFile.exists()) {
            testFile.createNewFile();
        }

        CjtProduct catalog = new CjtProduct();
        catalog.createProduct("ProductA", 10.5, "DescriptionA");
        catalog.createProduct("ProductB", 11.5, "DescriptionB");

        Similitud sim = new Similitud();
        sim.createProduct("ProductA");
        sim.createProduct("ProductB");
        sim.createSimilitud("ProductA", "ProductB", 0.8f);

        Shelve shelve = new Shelve(1, 2, new String[]{"ProductA", "ProductB"});

        // Try to export to an existing file
        sessionManager.exportSession(TEST_FILE_PATH_EXPORT.toString(), catalog, sim, shelve);
    }
}


