package edu.upc.prop.clusterxx.csvUtils;
/** 
 * Esta clase se encarga de importar los datos de similitud de un archivo CSV.
 */
public class CsvImportSimilitud extends CsvImport {


    /**
     * Crea un objeto similitud a partir de una línea de un archivo CSV.
     * @param csvLine Línea del archivo CSV.
     * @return Objeto creado a partir de la línea del archivo CSV.
     * @throws IllegalArgumentException Si la línea del archivo CSV no tiene 3 elementos.
     */
    @Override
    protected Object[] createObjectFromCsv(String csvLine) {
        String[] similaritiesData = csvLine.split(",");
        Object[] data = new Object[3];  // Create an Object array with 3 elements

        if (similaritiesData.length == 3) {
            String nameProduct1 = similaritiesData[0];
            String nameProduct2 = similaritiesData[1];
            float coefficient = Float.parseFloat(similaritiesData[2]);

            // Store each value in the Object array
            data[0] = nameProduct1;       // String for nameProduct1
            data[1] = nameProduct2;       // String for nameProduct2
            data[2] = coefficient;        // Float for coefficient

        } else {
            throw new IllegalArgumentException("Invalid CSV line, expected 3 elements: " + csvLine);
        }

        return data;  // Return the populated Object array
    }
}

