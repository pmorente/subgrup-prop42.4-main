package edu.upc.prop.clusterxx.csvUtils;

import java.util.List;

/**
 * Clase por defecto que exporta datos a un archivo CSV
 */
public class CsvExportDefault extends CsvExport<List<String>>{

    @Override
    /**
     * Función por defecto que extrae los datos a exportar.
     * @param data Lista de Strings a exportar.
     * @return Lista de Strings con los datos exportados.
     */
    protected List<String> extractData(List<String> data) {
        return data;
    }

}
