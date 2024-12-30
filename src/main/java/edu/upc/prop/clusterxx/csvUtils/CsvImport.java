package edu.upc.prop.clusterxx.csvUtils;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta que implementa la funcionalidad de importar datos desde un archivo CSV.
 * @author Pau Morente (pau.morente@estudiantat.upc.edu)
 */

public abstract class CsvImport {
    /**
     * Método que importa los datos de un archivo CSV.
     * @param filePath Ruta del archivo CSV.
     * @return Lista de objetos con los datos importados.
     * @throws IOException Si hay un error al leer el archivo.
     */
    public final List<Object[]> importData(String filePath) throws IOException {
        List<Object[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Object[] value = createObjectFromCsv(line);
                data.add(value);
            }
        } catch (IOException e) {
            throw new IOException("Error reading the file: " + e.getMessage(), e);
        }
        return data;
    }


    // Abstract method to create objects from CSV lines
    /** Método abstracto que crea objetos a partir de las líneas de un archivo CSV.
     * @param csvLine Línea del archivo CSV.
     * @return Objeto creado a partir de la línea del archivo CSV.
     */
    protected abstract Object[] createObjectFromCsv(String csvLine);
}


