package edu.upc.prop.clusterxx.csvUtils;

import java.util.List;

import edu.upc.prop.clusterxx.CjtProduct;
import edu.upc.prop.clusterxx.Product;

/**
 * Clase específica para exportar datos de Productos a formato CSV
 * @author Pau Morente (pau.morente@estudiantat.upc.edu)
 */

    // Specific class to export Product data to CSV format
    /**
     * Clase específica para exportar datos de Productos a formato CSV
     */
    public class CsvExportCjtProduct extends CsvExport<CjtProduct> {
        @Override
        protected List<String> extractData(CjtProduct catalog) {
            return catalog.getAllProducts().stream()
                    .map(Product::toString)  // Using Product's toString method for CSV formatting
                    .toList();
        }
    }

