
package edu.upc.prop.clusterxx;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.*;

import edu.upc.prop.clusterxx.csvUtils.CsvExportDefault;
import edu.upc.prop.clusterxx.csvUtils.CsvImportUsers;

/**
 * Clase que gestiona las sesiones de usuarios, incluyendo la creación y eliminación de usuarios,
 * así como la importación y exportación de datos de sesión.
 */


public class SessionManager {

    /**
     * Ruta relativa de la carpeta base donde se almacenan los usuarios.
     */
    private static final String USERS_BASE_FOLDER = "Users"; // Relative path for user folders

    /**
     * Ruta relativa del fichero del sistema de usuarios existentes.
     */
    private static final String USERS_SYSTEM_FILE = "Users/users.csv";


    /**
     * Constructor de la clase SessionManager. Asegura que la carpeta base de usuarios existe.
     *
     * @throws Exception Si ocurre un error al crear la carpeta base.
     */
    public SessionManager() throws Exception {
        ensureUsersBaseFolderExists();

    }

    /**
     * Asegura que la carpeta base de usuarios existe. Si no existe, la crea.
     *
     * @throws Exception Si ocurre un error al crear la carpeta.
     */
    private void ensureUsersBaseFolderExists() throws Exception {
        File baseFolder = new File(USERS_BASE_FOLDER);

        // Create the base Users folder if it doesn't exist
        if (!baseFolder.exists()) {
            if (!baseFolder.mkdir()) {
                throw new Exception("Error creating the base folder 'Users'.");
            }
        }

        // Ensure the users.csv file exists
        File usersFile = new File(USERS_SYSTEM_FILE);
        if (!usersFile.exists()) {
            try {
                if (!usersFile.createNewFile()) {
                    throw new Exception("Error creating the file 'users.csv'.");
                }
            } catch (IOException e) {
                throw new Exception("Error creating the file 'users.csv': " + e.getMessage(), e);
            }
        }


    }

    /**
     * Agrega un nuevo usuario al sistema creando una carpeta asociada.
     *
     * @param username Nombre de usuario.
     * @throws Exception Si el usuario ya existe o si ocurre un error al crear la carpeta.
     * @return Ruta de la carpeta del usuario.
     */
    public String createUser(String username) throws Exception {

        ensureUsersBaseFolderExists();

        // Define the user folder path relative to the base folder
        String folderPath = USERS_BASE_FOLDER + File.separator + username;

        // Check if the folder for the user exists; if not, create it
        File userFolder = new File(folderPath);
        if (userFolder.exists()) {
            throw new Exception("The user already exists.");
        }

        if (!userFolder.mkdir()) {
            throw new Exception("Error creating the user's folder: " + username);
        }

        // Create the SESSION_USER_DATA.csv file inside the user folder
        File sessionUserDataFile = new File(userFolder, "SESSION_USER_DATA.csv");
        try (FileWriter writer = new FileWriter(sessionUserDataFile)) {
            writer.write("::Products::\n::END::\n");
            writer.write("::Similarities::\n::END::\n");
            writer.write("::Shelf::\n::END::\n");
        } catch (IOException e) {
            throw new Exception("Error creating the file. SESSION_USER_DATA.csv: " + e.getMessage(), e);
        }

        // Import users from the CSV file
        CsvImportUsers usersImporter = new CsvImportUsers();
        List<Object[]> importedData = usersImporter.importData(USERS_SYSTEM_FILE);

        HashMap<String, String> users = convertToUserFolderMap(importedData);

        // Check if the user already exists
        if (users.containsKey(username)) {
            throw new Exception("The user already exists.");
        }

        folderPath = folderPath + File.separator + "SESSION_USER_DATA.csv";

        // Add the new user
        users.put(username, folderPath);

        // Export the updated list of users
        CsvExportDefault usersExporter = new CsvExportDefault();
        List<String> exportData = new ArrayList<>();
        for (Map.Entry<String, String> entry : users.entrySet()) {
            String stringData = entry.getKey() + "," + entry.getValue();
            exportData.add(stringData);
        }

        //Delete previous USERS_SYSTEM_FILE
        File userFile = new File(USERS_SYSTEM_FILE);
        if (userFile.exists() && !userFile.delete()) {
            throw new Exception("The existing user file could not be deleted.");
        }
        usersExporter.exportData(exportData, USERS_SYSTEM_FILE);


        return folderPath;
    }

    /**
     * Convierte una lista de objetos en un mapa de usuarios y rutas de carpetas.
     *
     * @param importedData Lista de objetos con los datos importados.
     * @return Mapa de usuarios y rutas de carpetas.
     */
    public static HashMap<String, String> convertToUserFolderMap(List<Object[]> importedData) {
        HashMap<String, String> userFolderMap = new HashMap<>();

        for (Object[] row : importedData) {
            if (row.length >= 2 && row[0] instanceof String && row[1] instanceof String) {
                String username = (String) row[0];
                String folderPath = (String) row[1];
                userFolderMap.put(username, folderPath);
            }
        }

        return userFolderMap;
    }

    /**
     * Elimina un usuario del sistema, incluyendo su carpeta.
     *
     * @param username Nombre de usuario.
     * @throws Exception Si el usuario no existe o si ocurre un error al eliminar la carpeta.
     */
    public void deleteUser(String username) throws Exception {

        ensureUsersBaseFolderExists();
        // Define the user folder path
        File userFolder = new File(USERS_BASE_FOLDER + File.separator + username);

        if (!userFolder.exists()) {
            throw new Exception("User does not exist.");
        }

        // Delete the user's folder and its contents
        deleteFolderRecursively(userFolder);

        //Import users from the CSV file
        CsvImportUsers usersImporter = new CsvImportUsers();
        List<Object[]> importedData = usersImporter.importData(USERS_SYSTEM_FILE);

        HashMap<String, String> users = convertToUserFolderMap(importedData);

        // Check if the user exists in the list
        if (!users.containsKey(username)) {
            throw new Exception("The user does not exist in the system.");
        }

        // Remove the user from the list
        users.remove(username);

        // Export the updated list of users
        CsvExportDefault usersExporter = new CsvExportDefault();
        List<String> exportData = new ArrayList<>();
        for (Map.Entry<String, String> entry : users.entrySet()) {
            String stringData = entry.getKey() + "," + entry.getValue();
            exportData.add(stringData);
        }

        //Delete previous USERS_SYSTEM_FILE
        File userFile = new File(USERS_SYSTEM_FILE);
        if (userFile.exists() && !userFile.delete()) {
            throw new Exception("The existing user file could not be deleted.");
        }
        usersExporter.exportData(exportData, USERS_SYSTEM_FILE);

    }



    /**
     * Método auxiliar para eliminar una carpeta y todo su contenido de forma recursiva.
     *
     * @param folder Carpeta a eliminar.
     * @throws Exception Si ocurre un error durante el proceso de eliminación.
     */
    private void deleteFolderRecursively(File folder) throws Exception {
        if (folder.isDirectory()) {
            // Delete all files and subdirectories
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFolderRecursively(file);
                }
            }
        }
        // Delete the folder itself
        if (!folder.delete()) {
            throw new Exception("The folder could not be deleted: " + folder.getAbsolutePath());
        }
    }




    /**
     * Devuelve una lista de objetos que contienen los nombres de usuario y sus rutas.
     *
     * @return Lista de objetos Object[] con la información de los usuarios.
     * @throws Exception Si ocurre un error al acceder a la carpeta de usuarios.
     */
    public List<Object[]> getUserData() throws Exception {

        // CHECK USERS FOLDERS IF EXISTS (optional but TBD)

        // IMPORT FROM THE CSV FILE OF USERS
        CsvImportUsers usersImporter = new CsvImportUsers();
        return usersImporter.importData(USERS_SYSTEM_FILE);
    }


    /**
     * Importa las líneas desde un archivo CSV.
     *
     * @param filePath Ruta del archivo.
     * @return Lista de líneas leídas.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    private List<String> importCSVSession(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new IOException("Error: " + e.getMessage());
        }
        return lines;
    }

    /**
     * Exporta las líneas a un archivo CSV.
     * 
     * @param lines Lista de líneas a escribir.
     * @param filePath Ruta del archivo.
     * @throws IOException Si ocurre un error al escribir el archivo.
     */
    private void exportCSVSession(List<String> lines, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            throw new IOException("File already exists. Please change the file name.");
        }

        // Write data to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }


    /**
     * Importa una sesión desde un archivo CSV.
     * 
     * @param filepath Ruta del archivo CSV.
     * @return Datos de la sesión importados.
     * @throws Exception Si ocurre un error al leer o procesar el archivo.
     */
    public SessionData importSession(String filepath) throws Exception {

        List<String> lines;
        try {
            lines = importCSVSession(filepath);
        } catch (IOException e) {
            throw new Exception("Error reading the file: " + e.getMessage());
        }
        CjtProduct catalog = new CjtProduct();
        Similitud sim = new Similitud();
        Shelve shelve = null;

        // Iterate through lines with nested loops based on parsing state
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);

            if (line.equals("::Products::")) {
                i++; // Move to the next line to start processing products
                while (i < lines.size() && !lines.get(i).equals("::END::")) {
                    String[] parts = lines.get(i).split(",");
                    String name = parts[0];
                    double price = Double.parseDouble(parts[1]);
                    String description = parts[2];
                    catalog.createProduct(name, price, description);
                    sim.createProduct(name);
                    i++;
                }
            }
            else if (line.equals("::Similarities::")) {
                i++; // Move to the next line to start processing similarities
                while (i < lines.size() && !lines.get(i).equals("::END::")) {
                    String[] parts = lines.get(i).split(",");
                    String product1 = parts[0];
                    String product2 = parts[1];
                    float similarity = Float.parseFloat(parts[2]);
                    sim.createSimilitud(product1, product2, similarity);
                    i++;
                }
            }
            else if (line.equals("::Shelf::")) {
                i++; // Move to the next line to start processing shelve data
                while (i < lines.size() && !lines.get(i).equals("::END::")) {
                    String[] parts = lines.get(i).split(",");
                    int rows = Integer.parseInt(parts[0]);
                    int columns = Integer.parseInt(parts[1]);
                    String[] products = parts[2].split(";");
                    shelve = new Shelve(rows, columns, products);
                    i++;
                }
            }
        }

        return new SessionData(catalog, sim, shelve);
    }

    /**
     * Exporta una sesión a un archivo CSV.
     *
     * @param filepath Ruta donde se exportará el archivo.
     * @param catalog Catálogo de productos a exportar.
     * @param sim Similitudes a exportar.
     * @param shelve Estantería a exportar.
     * @throws Exception Si ocurre un error durante la exportación.
     */
    public void exportAutosave(String filepath, CjtProduct catalog, Similitud sim, Shelve shelve) throws Exception {
        File file = new File(filepath);
        if (file.exists()) file.delete();
        exportSession(filepath,  catalog,  sim, shelve);
    }

    /**
     * Exporta una sesión a un archivo CSV.
     * 
     * @param filepath Ruta donde se eexportSessionxportará el archivo.
     * @param catalog Catálogo de productos a exportar.
     * @param sim Similitudes a exportar.
     * @param shelve Estantería a exportar.
     * @throws Exception Si ocurre un error durante la exportación.
     */
    public void exportSession(String filepath, CjtProduct catalog, Similitud sim, Shelve shelve) throws Exception {
        List<String> lines = new ArrayList<>();
        lines.add("::Products::");

        List<Product> products = catalog.getAllProducts();
        for (Product product : products) {
            lines.add(product.getName() + "," + product.getPrice() + "," + product.getDescription());
        }
        lines.add("::END::");

        lines.add("::Similarities::");
        List<String> listSim = sim.getSimilaritiesList();
        lines.addAll(listSim);
        lines.add("::END::");

        lines.add("::Shelf::");
        if(shelve != null) {
            lines.add(shelve.getRow() + "," + shelve.getColumn() + "," + String.join(";", shelve.getDistributionShelve()));
        }
        lines.add("::END::");

        exportCSVSession(lines, filepath);
    }

}
