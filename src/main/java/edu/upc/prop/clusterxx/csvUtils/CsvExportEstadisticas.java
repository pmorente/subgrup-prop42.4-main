package edu.upc.prop.clusterxx.csvUtils;

import java.util.ArrayList;
import java.util.List;

import edu.upc.prop.clusterxx.Estadisticas;

/**
 * Clase que exporta las estadísticas a un archivo CSV.
 */
public class CsvExportEstadisticas extends CsvExport<Estadisticas> {


    
    /**
     * Función que extrae los datos de las estadísticas y los devuelve en una tabla.
     * @param stats Estadísticas a exportar.
     * @return ArrayList de Strings con los datos de las estadísticas.
     */
    @Override
    protected List<String> extractData(Estadisticas stats) {
        List<String> datas;

        datas = generateTable(String.valueOf(stats.getTime()), String.valueOf(stats.getCost()), stats.getNumberIterations(), stats.getDistribution(), stats.getRows(), stats.getColumns());
        return datas;
    }


    /** 
     * Función que genera una tabla con los datos de las estadísticas.
     * @param time Tiempo de ejecución.
     * @param cost Coste de la solución.
     * @param numberOfIterations Número de iteraciones.
     * @param distribution Distribución de los productos.
     * @param n Número de filas.
     * @param p Número de columnas.
     * @return ArrayList de String con los datos de las estadísticas.
     */
    private static List<String> generateTable(String time, String cost, int numberOfIterations, ArrayList<String> distribution, int n, int p) {
        List<String> tableLines = new ArrayList<>();
        int cellWidth = 12;
        int dataIndex = 0;

        // Helper function to draw a borderline
        String border = "+".concat("-".repeat(cellWidth)).repeat(p) + "+";
        tableLines.add(border);

        // Loop to create table rows with product data
        for (int i = 0; i < n; i++) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < p; j++) {
                String value = (dataIndex < distribution.size()) ? distribution.get(dataIndex++) : "";
                row.append("| ").append(String.format("%-" + (cellWidth - 2) + "s", value));
            }
            row.append(" |");
            tableLines.add(row.toString());
            if (i < n - 1) tableLines.add(border); // Horizontal line between rows
        }

        // Add bottom border
        tableLines.add(border);
        // Add the stats outside the table
        tableLines.add("\nTime: " + time);
        tableLines.add("Cost: " + cost);
        tableLines.add("Number of iterations: " + numberOfIterations);

        return tableLines;
    }
}
