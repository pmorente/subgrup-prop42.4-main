package edu.upc.prop.clusterxx.csvUtils;

/**
 * Clase que importa un conjunto de productos desde un archivo CSV.
 * @author Pau Morente (pau.morente@estudiantat.upc.edu)
 */

public class CsvImportCjtProduct extends CsvImport {

    /**
     * Crea un objeto producto a partir de una línea de un archivo CSV.
     * @param csvLine Línea del archivo CSV.
     * @return Objeto creado a partir de la línea del archivo CSV.
     * @throws IllegalArgumentException Si la línea del archivo CSV no tiene 3 elementos.
    */
    @Override
    protected Object[] createObjectFromCsv(String csvLine) {
        String[] productData = csvLine.split(",");
        Object[] data = new Object[3];  // Create an Object array with 3 elements

        if (productData.length == 3) {
            String name = productData[0];
            double price = Double.parseDouble(productData[1]);
            String description = productData[2];

            // Store each element in the Object array
            data[0] = name;          // String for name
            data[1] = price;         // Double for price
            data[2] = description;   // String for description
        } else {
            throw new IllegalArgumentException("Invalid CSV line, expected 3 elements: " + csvLine);
        }

        return data;  // Return the Object array with all values
    }
}

