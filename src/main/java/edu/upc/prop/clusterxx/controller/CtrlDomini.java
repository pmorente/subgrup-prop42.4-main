package edu.upc.prop.clusterxx.controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.upc.prop.clusterxx.Estadisticas;
import edu.upc.prop.clusterxx.Product;
import edu.upc.prop.clusterxx.SessionData;
import edu.upc.prop.clusterxx.Users;

/**
 * Controlador principal que gestiona la lógica del dominio de la aplicacion.
 * Se encarga de la interaccion entre los controladores del catalogo, similitudes, estanteria, persistencia y el autosave.
 */
public class CtrlDomini {

    /**
     * Controlador del conjunto de productos
     */
    private CtrlCjtProduct ctrlCatalog;

    /**
     * Controlador de similitud
     */
    private CtrlSimilitud ctrlSimilitud;

    /**
     * Controlador de la estantería
     */
    private CtrlShelve ctrlShelve;

    /**
     * Controlador de persistencia
     */
    private CtrlPersistence ctrlPersistence;

    /**
     * Clase que gestiona los usuarios del sistema
     */
    private Users Users;

    /**
     * Atributo para ejecutar tareas programadas de manera concurrente
     */
    private ScheduledExecutorService scheduler;

     /**
     * Constructor de la clase CtrlDomini. Inicializa los controladores internos de catálogo, similitudes, estantería y persistencia.
     * @throws Exception Si ocurre un error al inicializar los controladores.
     */
    public CtrlDomini() throws Exception {
        //Initialize the other controllers

        ctrlPersistence = new CtrlPersistence();
        ctrlCatalog = new CtrlCjtProduct();
        ctrlSimilitud = new CtrlSimilitud();
        ctrlShelve = new CtrlShelve();
        Users = new Users(ctrlPersistence.getUserDataAtSystem());

    }

    /**
     * Valida la entrada proporcionada por el usuario.
     * 
     * @param input Entrada a validar.
     * @param type Si es 0, valores alfanuméricos, sin espacios, sin puntos. Si es 2 valores alfanuméricos, sin espacios. Si es 1, valores alfanuméricos sin puntos
     * @return La entrada validada.
     * @throws IllegalArgumentException Si la entrada es nula, vacía o contiene caracteres no alfanuméricos.
     */
    public String domainCtrl_checkInput(String input, int type) throws IllegalArgumentException {
        // Check if input is null
        if (input == null) {
            throw new IllegalArgumentException("The input cannot be null.");
        }

        // Trim whitespace and check if input is empty
        input = input.trim(); // Removes leading and trailing whitespace
        if (input.isEmpty()) {
            throw new IllegalArgumentException("The input cannot be empty or consist only of spaces.");
        }

        // Check if input contains only alphanumeric characters (modify regex as needed)
        // Type depends on the level of restriction of the input. 0: alphanumeric characters, 1: alphanumeric characters and spaces, 2: alphanumeric characters and dots
        if(type == 0) {
            if (!input.matches("^[a-zA-Z0-9]*$")) {
                throw new IllegalArgumentException("The input can only contain alphanumeric characters, no spaces and no dots.");
            }
        }else if(type == 1) {
            if (!input.matches("^[a-zA-Z0-9 ]*$")) {
                throw new IllegalArgumentException("The input can only contain alphanumeric characters, no dots.");
            }
        }else{
            if (!input.matches("^[a-zA-Z0-9.]*$")) {
                throw new IllegalArgumentException("The input can only contain alphanumeric characters, no spaces.");
            }
        }
        return input;
    }

    /**
     * Valida el nombre de archivo proporcionado por el usuario.
     *
     * @param input Nombre de archivo a validar.
     * @return El nombre de archivo validado.
     * @throws IllegalArgumentException Si el nombre de archivo es nulo, vacío o no tiene la extensión .csv
     */
    public String domainCtrl_checkFileName(String input) throws IllegalArgumentException {
        // Check if input is null
        if (input == null) {
            throw new IllegalArgumentException("The input cannot be null.");
        }

        // Trim whitespace and check if input is empty
        input = input.trim(); // Removes leading and trailing whitespace
        if (input.isEmpty()) {
            throw new IllegalArgumentException("The input cannot be empty or just spaces.");
        }

        // Check if input matches allowed file name pattern
        if (!input.matches("^[a-zA-Z0-9_\\- ]+\\.(csv)$")) {
            throw new IllegalArgumentException("The file name must have a .csv extension.");
        }

        return input;
    }

    // SESSION METHODS DOMAIN CONTROLLER
    /**
     * Crea un nuevo usuario con el nombre de usuario proporcionado.
     * 
     * @param username Nombre de usuario.
     * @throws Exception Si ocurre un error al crear el usuario.
     */
    public void domainCtrl_createUser(String username) throws Exception {

        //Seguida receta de la clase Users
        try {
        //Checks if user already exists
        Users.alreadyExistsUser(username);
        //CREATE USER AT PERSISTENCE SYSTEM
            String folderPath;
            folderPath =  ctrlPersistence.createUserAtSystem(username);
       // ADD IT TO THE RUNNING SYSTEM
        Users.createUser(username, folderPath);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error:" + e.getMessage());
        }

    }

    /**
     * Crea un nuevo usuario con el nombre de usuario proporcionado.
     * 
     * @param username Nombre de usuario.
     * @throws Exception Si ocurre un error al eliminar el usuario.
     */
    public void domainCtrl_deleteUser(String username) throws Exception {
        //Seguida receta de la clase Users
        try{
            //Checks if doesn't exists
            Users.doesNotExistUser(username);
            //DELETE USER AT PERSISTENCE SYSTEM
            ctrlPersistence.deleteUserAtSystem(username);
            // DELETE IT FROM RUNNING SYSTEM
            Users.deleteUser(username);

        } catch (Exception e) {
            throw new IllegalArgumentException("Error:" + e.getMessage());
        }

    }

    /**
     * Inicia sesión con el nombre de usuario proporcionado.
     * 
     * @param username Nombre de usuario.
     * @throws Exception Si el usuario no existe o ocurre un error al iniciar sesión.
     */
    public void domainCtrl_login(String username) throws Exception {

        Users.logIn(username);

        try{
            SessionData sessionData = ctrlPersistence.importSession(Users.getRouteLoggedIn());
            ctrlCatalog.CtrlSetCatalog(sessionData.getCjtProduct());
            ctrlSimilitud.CtrlSetSimilitud(sessionData.getSimilitud());
            ctrlShelve.CtrlSetShelve(sessionData.getShelve());
        }catch (Exception e) {
            throw new Exception("Error importing the session: " + e.getMessage());
        }


    }

    /**
     * Obtiene el nombre del usuario que ha iniciado sesión.
     *
     * @return Nombre del usuario que ha iniciado sesión.
     */
    public String domainCtrl_getUserLoggedIn() {
        return Users.getLogInUser();
    }


    /**
     * Obtiene una lista de los usuarios registrados en el sistema.
     * 
     * @return Lista de usuarios.
     * @throws Exception Si ocurre un error al obtener los usuarios.
     */
    public List<String> domainCtrl_getUsers() throws Exception {
        return Users.usersAsString();
    }


    /**
     * Importa una sesión desde un archivo especificado.
     * 
     * @param filepath Ruta del archivo a importar.
     * @throws Exception Si ocurre un error al importar la sesión.
     */
    public void domainCtrl_ImportSession(String filepath) throws Exception {
        try{
            SessionData sessionData = ctrlPersistence.importSession(filepath);
            ctrlCatalog.CtrlSetCatalog(sessionData.getCjtProduct());
            ctrlSimilitud.CtrlSetSimilitud(sessionData.getSimilitud());
            ctrlShelve.CtrlSetShelve(sessionData.getShelve());
        }catch (Exception e) {
            throw new Exception("Error importing the session: " + e.getMessage());
        }
    }

    /**
     * Guarda una sesión desde un archivo especificado.
     * 
     * @throws Exception Si ocurre un error al guardar la sesión.
     */
    public void domainCtrl_saveSession() throws Exception{
        try {
            ctrlPersistence.exportAutosave(Users.getRouteLoggedIn(), ctrlCatalog.CtrlGetCjtProduct(), ctrlSimilitud.CtrlGetSimilitud(), ctrlShelve.CtrlGetShelveForExport());
        } catch (Exception e) {
            throw new Exception("Error exporting the session: " + e.getMessage());
        }
    }

    /**
     * Exporta la sesión actual a un archivo especificado.
     * 
     * @param filepath Ruta del archivo de exportación.
     * @throws Exception Si ocurre un error al exportar la sesión.
     */
    public void domainCtrl_ExportSession(String filepath) throws Exception {
            try {
                ctrlPersistence.exportSession(filepath, ctrlCatalog.CtrlGetCjtProduct(), ctrlSimilitud.CtrlGetSimilitud(), ctrlShelve.CtrlGetShelve());
            } catch (Exception e) {
                throw new Exception("Error exporting the session: " + e.getMessage());
            }
    }




    // CATALOG METHODS DOMAIN CONTROLLER
    /**
     * Crea un nuevo producto en el catálogo mediante los inputs del user.
     * 
     * @param name Nombre del producto.
     * @param price Precio del producto.
     * @param description Descripción del producto.
     */
    public void domainCtrl_ImportManuallyProducts(String name, double price, String description) {
        ctrlCatalog.CtrlCreateProduct(name, price, description);
        ctrlSimilitud.CtrlAddProductSimilituds(name); //Se añade el producto a la matriz de similitud
    }


    /**
     * Importa productos desde un archivo.
     * 
     * @param file_path Ruta del archivo de importación.
     * @throws Exception Si ocurre un error al importar los productos.
     */
    public void domainCtrl_ImportProducts(String file_path) throws Exception {
        List<Object[]> productData = ctrlPersistence.importCjtProducts(file_path);
        //Adding the products in the catalog and the similitud matrix
        for (Object[] product : productData) {
            domainCtrl_ImportManuallyProducts((String) product[0], (double) product[1], (String) product[2]);
        }
    }


    /**
     * Cambia el precio de un producto en el catálogo.
     * 
     * @param name Nombre del producto.
     * @param price Nuevo precio del producto.
     */
    public void domainCtrl_ChangePriceProduct(String name, double price) {
        ctrlCatalog.CtrlChangePriceProduct(name, price);
    }

    /**
     * Cambia la descripción de un producto en el catálogo.
     * 
     * @param name Nombre del producto.
     * @param description Nueva descripción del producto.
     */
    public void domainCtrl_ChangeDescriptionProduct(String name, String description) {
        ctrlCatalog.CtrlChangeDescriptionProduct(name, description);
    }

    /**
     * Muestra todos los productos del catálogo.
     *
     * @return Lista de productos.
     */
    public List<String[]> domainCtrl_ShowProducts() {
        return ctrlCatalog.CtrlShowProducts();
    }

    /**
     * Exporta el catálogo de productos a un archivo.
     * 
     * @param file_path Ruta del archivo de exportación.
     * @throws Exception Si ocurre un error al exportar el catálogo.
     */
    public void domainCtrl_ExportProducts(String file_path) throws Exception {
        ctrlPersistence.exportCjtProducts(ctrlCatalog.CtrlGetCjtProduct(), file_path);
    }

    /**
     * Elimina un producto del catálogo.
     * 
     * @param name Nombre del producto a eliminar.
     */
    public void domainCtrl_RemoveProduct(String name) {
        ctrlCatalog.CtrlRemoveProduct(name);
        ctrlSimilitud.CtrlRemoveProductSimilituds(name);
    }


    // SIMILITUD METHODS DOMAIN CONTROLLER
    /**
     * Crea manualmente una similitud entre dos productos.
     * 
     * @param producte1 Primer producto.
     * @param producte2 Segundo producto.
     * @param coef Coeficiente de similitud.
     * @throws Exception Si ocurre un error al crear la similitud.
     */
    public void domainCtrl_ImportManuallySimilituds(String producte1, String producte2, Float coef) throws Exception{
        ctrlSimilitud.CtrlCreateSimilituds(producte1, producte2, coef); //Se añade la similitud a la matriz de similitud
    }

    /**
    * Importa las similitudes desde un archivo.
     * @param path Ruta del archivo de importación.
     * @throws Exception Si ocurre un error al importar las similitudes.
     */
    public void domainCtrl_ImportSimilituds(String path) throws Exception {
        List<Object[]> similitudData = ctrlPersistence.importSimilituds(path);
        //Processing the imported similitud data
        for (Object[] similarity : similitudData) {
            ctrlSimilitud.CtrlCreateSimilituds((String) similarity[0], (String) similarity[1], (float) similarity[2]);
        }
    }

    /**
     * Realiza una comprobación de las similitudes.
     */
    public void domainCtrl_HealthCheckSimilitud(){
        ctrlSimilitud.CtrlHealthCheckSimilitud();
    }

    /**
     * Muestra todas las similitudes.
     * 
     * @return Lista de similitudes.
     */
    public List<String[]> domainCtrl_ShowSimilituds(){
        return ctrlSimilitud.CtrlShowSimilituds();
    }

    /**
     * Exporta todas las similitudes a un archivo.
     * 
     * @param path_export Ruta del archivo de exportación.
     * @throws Exception Si ocurre un error al exportar las similitudes.
     */
    public void domainCtrl_ExportSimilituds(String path_export) throws Exception{
        ctrlPersistence.exportDefault(ctrlSimilitud.CtrlDataAsListString(), path_export);
    }


    /**
     * Elimina una similitud entre dos productos.
     * 
     * @param primerProducte Primer producto.
     * @param segonProducte Segundo producto.
     */
    public void domainCtrl_RemoveSimilitud(String primerProducte, String segonProducte){
        ctrlSimilitud.CtrlRemoveSimilitud(primerProducte, segonProducte);
    }


    // SHELVE METHODS DOMAIN CONTROLLER
    /**
     * Inicializa la estantería con un número de filas y columnas.
     * 
     * @param rows Número de filas de la estantería.
     * @param columns Número de columnas de la estantería.
     * @throws Exception Si ocurre un error al inicializar la estantería.
     */
    public void domainCtrl_InitializeShelve(int rows, int columns) throws Exception {
        ctrlShelve.CtrlInitializeShelve(rows, columns, ctrlCatalog.CtrlGetAllProductNames());
    }

    /**
     * Modifica manualmente la estantería con dos productos.
     * 
     * @param name1 Nombre del primer producto.
     * @param name2 Nombre del segundo producto.
     * @throws Exception Si ocurre un error al modificar la estantería.
     */
    public void domainCtrl_ModifyManuallyShelve(String name1, String name2)  throws Exception{
        ctrlShelve.CtrlModifyManuallyShelve(name1, name2);
    }

    /**
     * Importa una estantería desde un archivo.
     * 
     * @param path Ruta del archivo de importación.
     * @throws Exception Si ocurre un error al modificar la estantería.
     */
    public void domainCtrl_ImportShelve(String path)  throws Exception{

        List<Object[]> shelveData = ctrlPersistence.importShelve(path);
        Object[] data = shelveData.getFirst();
        ctrlShelve.CtrlInitializeShelve((int) data[0], (int) data[1], (String[]) data[2]);
    }


    /**
     * Muestra el estado actual de la estantería.
     * 
     * @return Datos de la estantería.
     * @throws Exception Si ocurre un error al mostrar la estantería.
     */
    public Object[] domainCtrl_ShowShelve() throws Exception {
        return ctrlShelve.CtrlShowShelve();
    }


    /**
     * Exporta los datos de la estantería a un archivo.
     * 
     * @param path Ruta del archivo de exportación.
     * @throws Exception Si ocurre un error al exportar la estantería.
     */
    public void domainCtrl_ExportShelve(String path) throws Exception{
        try {
            ctrlPersistence.exportDefault(ctrlShelve.CtrlDataAsListString(), path);

        } catch (Exception e) {
            throw new Exception("Error exporting the Shelf:" + e.getMessage());
        }

    }

    /**
     * Obtiene el costo total de la estantería.
     * 
     * @return Costo de la estantería en formato de texto.
     */
    public String domainCtrl_GetShelveCost(){
        String result = "Inf";
        try {
            domainCtrl_fullCheckSystem();
            result = String.format("%.2f",ctrlShelve.CtrlGetCostShelve(ctrlSimilitud.CtrlGetSimilitud()));
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /**
     * Obtiene el número de productos en la estantería.
     *
     * @return Número de productos en la estantería.
     */
    public String domainCtrl_GetIterLastAlgo() {
        String res = "No algorithm has been executed.";
        if(ctrlShelve.CtrlGetIterLastAlgo() >= 0) res = ""+ctrlShelve.CtrlGetIterLastAlgo();
        return res;
    }

    /**
     * Realiza una distribución rápida de los productos en la estantería.
     * 
     * @throws Exception Si ocurre un error al calcular la distribución.
     */
    public void domainCtrl_ComputationFastShelve() throws Exception{

        try {
            domainCtrl_fullCheckSystem();
            ctrlShelve.CtrlCalculateFast(ctrlSimilitud.CtrlGetSimilitud());
        } catch (Exception e) {
            throw new Exception("Error calculating distribution:" + e.getMessage());

        }

    }

    /**
     * Realiza una distribución lenta de los productos en la estantería.
     * 
     * @throws Exception Si ocurre un error al calcular la distribución.
     */
    public void domainCtrl_ComputationSlowShelve() throws Exception{

        try {
            domainCtrl_fullCheckSystem();
            ctrlShelve.CtrlCalculateSlow(ctrlSimilitud.CtrlGetSimilitud());
        } catch (Exception e) {
            throw new Exception("Error calculating distribution:" + e.getMessage());

        }

    }

    /**
     * Realiza una distribución con el tercer algoritmo de la estantería.
     * 
     * @throws Exception Si ocurre un error al calcular la distribución.
     */
    public void domainCtrl_ComputationThirdShelve() throws Exception{

        try {
            domainCtrl_fullCheckSystem();
            ctrlShelve.CtrlCalculateThird(ctrlSimilitud.CtrlGetSimilitud());
        } catch (Exception e) {
            throw new Exception("Error calculating distribution:" + e.getMessage());

        }

    }

    /**
     * Añade un producto a la estantería.
     * 
     * @param product Nombre del producto a añadir.
     * @throws Exception Si ocurre un error al añadir el producto.
     */
    public void domainCtrl_AddProductShelve(String product) throws Exception{
        ctrlShelve.CtrlAddProductShelve(product);
    }

    /**
     * Elimina un producto de la estantería.
     * 
     * @param product Nombre del producto a eliminar.
     * @throws Exception Si ocurre un error al eliminar el producto.
     */
    public void domainCtrl_DeleteProductShelve(String product) throws Exception{
        ctrlShelve.CtrlDeleteProductShelve(product);
    }

    /**
     * Realiza una comprobación completa del sistema.
     * @throws Exception Si ocurre un error al comprobar el sistema.
     */
    public void domainCtrl_fullCheckSystem() throws Exception{
        ctrlSimilitud.CtrlHealthCheckSimilitud();
        try {
            if (ctrlCatalog.CtrlGetNumProducts() != ctrlShelve.CtrlGetNumProds()) {
                throw new Exception("The number of products between the catalog and the bookshelf do not match, the catalog has " + ctrlCatalog.CtrlGetNumProducts() + " and shelf has " + ctrlShelve.CtrlGetNumProds());
            }
            if (ctrlCatalog.CtrlGetNumProducts() != ctrlSimilitud.CtrlGetNumSimilituds()) {
                throw new Exception("The number of products between the catalog and the similarities do not match, the catalog has " + ctrlCatalog.CtrlGetNumProducts() + " and similarity has " + ctrlSimilitud.CtrlGetNumSimilituds());
            }

            for (Product product : ctrlCatalog.CtrlGetCjtProduct().getAllProducts()) {
                if (!ctrlShelve.CtrlGetShelve().getDistributionShelve().contains(product.getName())) {
                    throw new Exception("The product from the catalog " + product.getName() + " is not in the shelf");
                }
            }
        } catch (Exception e) {
            throw new Exception("Verification failed:" + e.getMessage());
        }
    }



    // Functions to get the statistics by timestamp

    /**
     * Obtiene las estadísticas rápidas por marca de tiempo.
     * 
     * @return Lista de estadísticas rápidas ordenadas por marca de tiempo.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getFastEstadisticasByTimestamp() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllFastStatsByTimestamp();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene las estadísticas lentas por marca de tiempo.
     * 
     * @return Lista de estadísticas lentas ordenadas por marca de tiempo.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getSlowEstadisticasByTimestamp() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllSlowStatsByTimestamp();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene las estadísticas del tercer algoritmo por marca de tiempo.
     * 
     * @return Lista de estadísticas del tercer algoritmo ordenadas por marca de tiempo.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getThirdEstadisticasByTimestamp() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllThirdStatsByTimestamp();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    // Funciones para obtener las estadísticas por número de iteraciones
    
    /**
     * Obtiene las estadísticas rápidas por número de iteraciones.
     * 
     * @return Lista de estadísticas rápidas ordenadas por número de iteraciones.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getFastEstadisticasByNumIteration() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllFastStatsByNumIteration();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene las estadísticas lentas por número de iteraciones.
     * 
     * @return Lista de estadísticas lentas ordenadas por número de iteraciones.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getSlowEstadisticasByNumIteration() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllSlowStatsByNumIteration();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene las estadísticas del tercer algoritmo por número de iteraciones.
     * 
     * @return Lista de estadísticas del tercer algoritmo ordenadas por número de iteraciones.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getThirdEstadisticasByNumIteration() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllThirdStatsByNumIteration();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    
    // Funciones para obtener las estadísticas por costo
    /**
     * Obtiene las estadísticas rápidas por costo.
     * 
     * @return Lista de estadísticas rápidas ordenadas por costo.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getFastEstadisticasByCost() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllFastStatsByCost();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene las estadísticas lentas por costo.
     * 
     * @return Lista de estadísticas lentas ordenadas por costo.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getSlowEstadisticasByCost() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllSlowStatsByCost();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Obtiene las estadísticas del tercer algoritmo por costo.
     * 
     * @return Lista de estadísticas del tercer algoritmo ordenadas por costo.
     * @throws Exception Si ocurre un error al obtener las estadísticas.
     */
    public List<Estadisticas> domainCtrl_getThirdEstadisticasByCost() throws Exception {
        try {
            return ctrlShelve.CtrlGetAllThirdStatsByCost();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Exporta las estadísticas a un archivo en la ruta especificada.
     * 
     * @param estadisticas Estadísticas a exportar.
     * @param path Ruta del archivo de exportación.
     * @throws Exception Si ocurre un error al exportar las estadísticas.
     */
    public void domainCtrl_ExportEstadisticas(Estadisticas estadisticas, String path) throws Exception {
        try {
            ctrlPersistence.exportEstadisticas(path, estadisticas);
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }


    // Functions to reset statistics
    /**
     * Restablece las estadísticas rápidas.
     * 
     * @throws Exception Si ocurre un error al restablecer las estadísticas.
     */
    public void domainCtrl_ResetFastEstadisticas() throws Exception {
        try {
            ctrlShelve.CtrlResetFastStats();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }
    
    /**
     * Restablece las estadísticas lentas.
     * 
     * @throws Exception Si ocurre un error al restablecer las estadísticas.
     */
    public void domainCtrl_ResetSlowEstadisticas() throws Exception {
        try {
            ctrlShelve.CtrlResetSlowStats();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }

    /**
     * Restablece las estadísticas del tercer algoritmo.
     * 
     * @throws Exception Si ocurre un error al restablecer las estadísticas.
     */
    public void domainCtrl_ResetThirdEstadisticas() throws Exception {
        try {
            ctrlShelve.CtrlResetThirdStats();
        } catch (Exception e) {
            throw new Exception("Error: " + e.getMessage());
        }
    }



    // AUTOSAVE METHODS DOMAIN CONTROLLER
    //Implementation of autosave. (Activation and deactivation)

    /**
     * Activa el autosave para guardar automáticamente los datos en intervalos regulares.
     *
     * @throws Exception Si ocurre un error al activar el autosave.
     */
    public void domainCtrl_activateAutoSave() throws Exception {

        if (scheduler != null && !scheduler.isShutdown()) {
            throw new IllegalStateException("Auto-save is already active.");
        }
        if (Runtime.getRuntime().availableProcessors() < 2) throw new Exception("Computer has no capacity");
        scheduler = Executors.newScheduledThreadPool(1);
        Runnable autoSaveTask = () -> {
            try{
                Escribir_autosave();
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };
        scheduler.scheduleAtFixedRate(autoSaveTask, 0, 2, TimeUnit.SECONDS);
    }

    /**
     * Escribe los datos en el archivo de autosave.
     *
     * @throws InterruptedException Si ocurre un error al escribir los datos.
     */
    private synchronized void Escribir_autosave() throws InterruptedException {

        try {
            ctrlPersistence.exportAutosave(
                    Users.getRouteLoggedIn(),
                    ctrlCatalog.CtrlGetCjtProduct(),
                    ctrlSimilitud.CtrlGetSimilitud(),
                    ctrlShelve.CtrlGetShelveForExport()
            );
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InterruptedException(e.getMessage());
        }
    }

    /**
     * Desactiva el autosave y detiene las tareas de guardado automático.
     * 
     * @throws Exception Si ocurre un error al desactivar el autosave.
     */
    public void domainCtrl_deactivateAutoSave() throws Exception {
        if (scheduler == null || scheduler.isShutdown()) {
            throw new IllegalStateException("Auto-save is not active.");
        }

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            throw new Exception("Failed to deactivate auto-save cleanly: " + e.getMessage());
        }
    }
}
