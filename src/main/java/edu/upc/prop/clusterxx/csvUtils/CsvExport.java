package edu.upc.prop.clusterxx.csvUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
/**
 * Clase abstracta que define el método de plantilla para exportar datos a un archivo CSV.
 * @author Pau Morente (pau.morente@estudiantat.upc.edu)
 */

// Abstract class defining the Template Method
public abstract class CsvExport<T> {

    // Template method defining the export process
    /**
     * Método de plantilla que exporta los datos de un origen de datos a un archivo CSV.
     * @param dataSource Origen de datos a exportar.
     * @param filePath Ruta del archivo CSV de destino.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    public void exportData(T dataSource, String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            throw new IOException("File already exists. Please change the file name.");
        }

        List<String> dataLines = extractData(dataSource);
        writeFile(dataLines, filePath);
    }



    // Steps to be implemented by subclasses
    /**
     * Método abstracto que extrae los datos del origen de datos.
     * @param dataSource Origen de datos a exportar.
     * @return Lista de cadenas de texto con los datos extraídos.
     */
    protected abstract List<String> extractData(T dataSource);


    // Concrete method to write data to file
    /**
     * Método concreto que escribe las líneas de texto en un archivo.
     * @param lines Lista de cadenas de texto a escribir.
     * @param filePath Ruta del archivo de destino.
     * @throws IOException Si ocurre un error al escribir en el archivo.
     */
    private void writeFile(List<String> lines, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
}
