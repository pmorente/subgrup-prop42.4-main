package edu.upc.prop.clusterxx.csvUtils;

/**
 * Esta clase se encarga de importar un fichero CSV con los datos de los estantes.
 */

public class CsvImportShelve extends CsvImport {

    /** 
     * Método que decodifica los productos de un estante.
     * @param products String con los productos del estante.
     * @return Array de Strings con los productos del estante.
     */
    private String[] decodeProducts(String products) {
        // Delimiter used is ';'
        return products.split(";");
    }

    /**
     * Método que crea un objeto Shelve a partir de una línea CSV.
     * @param csvLine Línea CSV.
     * @return Array de objetos con los datos de la línea CSV.
     * @throws IllegalArgumentException Si la línea CSV no tiene 3 elementos.
     */
    @Override
    protected Object[] createObjectFromCsv(String csvLine) {
        String[] shelveData = csvLine.split(",");
        Object[] data = new Object[3];  // Create an Object array with 3 elements

        if (shelveData.length == 3) {
            int row = Integer.parseInt(shelveData[0]);
            int column = Integer.parseInt(shelveData[1]);
            String[] products = decodeProducts(shelveData[2]);

            if(products.length == row*column) {
                // Store each value in the Object array
                data[0] = row;       // String for rows of the shelve
                data[1] = column;       // String for columns of the shelve
                data[2] = products;        // String for products of the shelves

            }else{
                throw new IllegalArgumentException("Invalid CSV line, expected " + row*column + " products: " + csvLine);
            }



        } else {
            throw new IllegalArgumentException("Invalid CSV line, expected 3 elements: " + csvLine);
        }

        return data;  // Return the populated Object array
    }

}


