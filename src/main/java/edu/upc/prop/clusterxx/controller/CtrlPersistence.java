package edu.upc.prop.clusterxx.controller;

import java.util.List;

import edu.upc.prop.clusterxx.CjtProduct;
import edu.upc.prop.clusterxx.Estadisticas;
import edu.upc.prop.clusterxx.SessionData;
import edu.upc.prop.clusterxx.SessionManager;
import edu.upc.prop.clusterxx.Shelve;
import edu.upc.prop.clusterxx.Similitud;
import edu.upc.prop.clusterxx.csvUtils.CsvExportCjtProduct;
import edu.upc.prop.clusterxx.csvUtils.CsvExportDefault;
import edu.upc.prop.clusterxx.csvUtils.CsvExportEstadisticas;
import edu.upc.prop.clusterxx.csvUtils.CsvImportCjtProduct;
import edu.upc.prop.clusterxx.csvUtils.CsvImportShelve;
import edu.upc.prop.clusterxx.csvUtils.CsvImportSimilitud;
/**
 * Clase que gestiona la persistencia de datos en la aplicacion.
 * Esta clase se encarga de importar y exportar datos de diversas entidades, como productos, similitudes, estadsiticas,
 * y sesiones, utilizando archivos CSV. Ademas, gestiona la creacion y eliminacion de usuarios en el sistema.
 */
public class CtrlPersistence {

        /**
         * Gestor de sesiones que maneja las operaciones relacionadas con la persistencia de sesiones en el sistema.
         */
        private SessionManager sessionManager;

        /**
         * Constructor de la clase CtrlPersistence.
         * Inicializa el gestor de sesiones (SessionManager).
         *
         * @throws IllegalArgumentException Si ocurre un error al inicializar el gestor de sesiones.
         */
        public CtrlPersistence() {
            try{
                this.sessionManager = new SessionManager();
            } catch (Exception e) {
                throw new IllegalArgumentException("Error al inicialitzar el gestor de Sessions");
            }
        }


        /**
         * Exporta un conjunto de datos en formato CSV utilizando una ruta de archivo proporcionada.
         *
         * @param data Lista de cadenas que representan los datos a exportar.
         * @param file_path Ruta del archivo donde se exportaran los datos.
         * @throws Exception Si ocurre un error durante la exportacion de los datos.
         */
        public void exportDefault(List<String> data, String file_path) throws Exception {
            CsvExportDefault csvExport = new CsvExportDefault();
            csvExport.exportData(data, file_path);
        }

        /**
         * Importa un conjunto de datos de productos desde un archivo CSV.
         *
         * @param file_path Ruta del archivo CSV desde el cual se importaran los productos.
         * @return Lista de objetos que representan los productos importados.
         * @throws Exception Si ocurre un error durante la importacion de los productos.
         */
        public List<Object[]> importCjtProducts(String file_path) throws Exception {

            // Import for CjtProduct
            CsvImportCjtProduct productImporter = new CsvImportCjtProduct();
            List<Object[]> productData = productImporter.importData(file_path);

        return productData;
        }

        /**
         * Exporta un conjunto de productos en formato CSV utilizando una ruta de archivo proporcionada.
         *
         * @param catalog Conjunto de productos a exportar.
         * @param file_path Ruta del archivo donde se exportaran los productos.
         * @throws Exception Si ocurre un error durante la exportacion de los productos.
         */
        public void exportCjtProducts(CjtProduct catalog, String file_path) throws Exception {

            CsvExportCjtProduct csvExport = new CsvExportCjtProduct();
            csvExport.exportData(catalog, file_path);

        }

        /**
         * Importa un conjunto de datos de similitudes desde un archivo CSV.
         *
         * @param file_path Ruta del archivo CSV desde el cual se importaran las similitudes.
         * @return Lista de objetos que representan las similitudes importadas.
         * @throws Exception Si ocurre un error durante la importacion de las similitudes.
         */
        public List<Object[]> importSimilituds(String file_path) throws Exception {

            CsvImportSimilitud similitudImporter = new CsvImportSimilitud();
            List<Object[]> similitudData = similitudImporter.importData(file_path);

        return similitudData;
        }


        /**
         * Exporta un conjunto de similitudes en formato CSV utilizando una ruta de archivo proporcionada.
         *
         * @param file_path Ruta del archivo donde se exportaran las similitudes.
         * @return Lista de objetos que representan las similitudes exportadas.
         * @throws Exception Si ocurre un error durante la exportacion de las similitudes.
         */
        public List<Object[]> importShelve(String file_path) throws Exception {
            CsvImportShelve shelveImporter = new CsvImportShelve();
            List<Object[]> shelveData = shelveImporter.importData(file_path);

        return shelveData;
        }

        /**
         * Exporta las estadisticas del sistema a un archivo CSV.
         *
         * @param file_path Ruta del archivo donde se exportaran las estadisticas.
         * @param stats Estadisticas que se exportaran.
         * @throws Exception Si ocurre un error durante la exportacion de las estadisticas.
         */
        public void exportEstadisticas(String file_path, Estadisticas stats) throws Exception {

            CsvExportEstadisticas csvExporter = new CsvExportEstadisticas();
            csvExporter.exportData(stats, file_path);
        }

        /**
         * Importa los datos de una sesion desde un archivo CSV.
         *
         * @param file_path Ruta del archivo CSV desde el cual se importara la sesion.
         * @return Datos de la sesion importados.
         * @throws Exception Si ocurre un error durante la importacion de la sesion.
         */
        public SessionData importSession(String file_path) throws Exception{


            SessionData sessiondata = sessionManager.importSession(file_path);

        return sessiondata;
        }

    /**
     * Exporta los datos de una sesion a un archivo CSV.
     *
     * @param file_path Ruta del archivo donde se exportaran los datos de la sesion.
     * @param catalog Catalogo de productos.
     * @param similitud Similitudes entre productos.
     * @param shelve Estantes donde se almacenan los productos.
     * @throws Exception Si ocurre un error durante la exportacion de la sesion.
     */
    public void exportAutosave(String file_path, CjtProduct catalog, Similitud similitud, Shelve shelve ) throws Exception{
        sessionManager.exportAutosave(file_path, catalog, similitud, shelve);
    }

        /**
         * Exporta los datos de una sesion a un archivo CSV.
         *
         * @param file_path Ruta del archivo donde se exportaran los datos de la sesion.
         * @param catalog Catalogo de productos.
         * @param similitud Similitudes entre productos.
         * @param shelve Estantes donde se almacenan los productos.
         * @throws Exception Si ocurre un error durante la exportacion de la sesion.
         */
        public void exportSession(String file_path, CjtProduct catalog, Similitud similitud, Shelve shelve ) throws Exception{

            sessionManager.exportSession(file_path, catalog, similitud, shelve);

        }

    /**
     * Crea un nuevo usuario en el sistema.
     *
     * @param username Nombre del usuario a crear.
     * @return Resultado de la creacion del usuario (identificador del nuevo usuario).
     * @throws Exception Si ocurre un error durante la creacion del usuario.
     */
        public String createUserAtSystem(String username) throws Exception {
            return sessionManager.createUser(username);
        }

        /**
         * Elimina un usuario del sistema.
         *
         * @param username Nombre del usuario a eliminar.
         * @throws Exception Si ocurre un error durante la eliminacion del usuario.
         */
        public void deleteUserAtSystem(String username) throws Exception {
            sessionManager.deleteUser(username);

        }

        /**
         * Obtiene los datos de los usuarios registrados en el sistema.
         *
         * @return Lista de arrays de objetos, donde cada array contiene los datos de un usuario.
         * @throws Exception Si ocurre un error al obtener los datos de los usuarios.
         */
        public List<Object[]> getUserDataAtSystem() throws Exception {
            return sessionManager.getUserData();
        }





}
