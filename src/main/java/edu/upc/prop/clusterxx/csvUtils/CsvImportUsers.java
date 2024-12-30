package edu.upc.prop.clusterxx.csvUtils;

/**
 * Clase que importa usuarios desde un archivo CSV.
 */
public class CsvImportUsers extends CsvImport {

    /**
     * Método que crea un objeto User a partir de una línea CSV.
     * @param csvLine Línea CSV
     * @return Objeto creado
     * @throws IllegalArgumentException Si la línea CSV no tiene 2 elementos.
     */
    @Override
    protected Object[] createObjectFromCsv(String csvLine) {
        String[] userData = csvLine.split(",");
        Object[] data = new Object[2]; // Adjust the size based on the number of expected fields

        if (userData.length == 2) { // Assuming CSV has exactly 3 elements: username, userPath, etc.
            String username = userData[0];
            String userPath = userData[1];

            // Store each value in the Object array
            data[0] = username;       // String for username
            data[1] = userPath;       // String for userPath


        } else {
            throw new IllegalArgumentException("Invalid CSV line, expected 2 elements: " + csvLine);
        }

        return data; // Return the populated Object array
    }


}
