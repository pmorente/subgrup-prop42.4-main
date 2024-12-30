package edu.upc.prop.clusterxx.controller;

import java.util.ArrayList;
import java.util.List;

import edu.upc.prop.clusterxx.Estadisticas;
import edu.upc.prop.clusterxx.Shelve;
import edu.upc.prop.clusterxx.Similitud;

/**
 * Controlador secundario que gestiona la lógica de estanteria.
 * Se encarga de todas las operaciones relacionadas con la estanteria.
 */
public class CtrlShelve {

    /**
     * Estanteria del sistema.
     */
    private Shelve shelve = null;


    /**
     * Inicializa la estanteria con las dimensiones y productos dados.
     * 
     * @param rows filas de la estanteria
     * @param columns columnas de la estanteria
     * @param products productos de la estanteria
     */
    public void CtrlInitializeShelve(Integer rows, Integer columns, String[] products){

        shelve = new Shelve(rows, columns, products);

    }


    /**
     * Modifica la estanteria manualmente, intercambiando las posiciones de dos productos.
     * 
     * @param name1 nombre del primer producto
     * @param name2 nombre del segundo producto
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlModifyManuallyShelve(String name1, String name2)  throws Exception {

        if(shelve != null) shelve.exchangePositions(name1, name2);
        else throw new Exception("Shelf not initialized, please create a shelf first.");

    }

    /**
     * Calcula el coste de la estanteria actual.
     * 
     * @param similitud similitud a utilizar
     * @return coste de la estanteria actual
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public double CtrlGetCostShelve(Similitud similitud) throws Exception{
        if(shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.get_current_shelve_cost(similitud);
    }

    /**
     * Devuelve el coste de la estanteria actual.
     *
     * @return coste de la estanteria actual
     */
    public int CtrlGetIterLastAlgo() {
        if(shelve.Get_last_algorithm_stats() == null) return -1;
        return shelve.Get_last_algorithm_stats().getNumberIterations();
    }

    /**
     * Muestra la estanteria actual.
     * 
     * @return objeto con las filas, columnas y distribucion de la estanteria
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public Object[] CtrlShowShelve() throws Exception {
        if (shelve != null) {
            int row = shelve.getRow();
            int column = shelve.getColumn();
            ArrayList<String> distribution = shelve.getDistributionShelve();
            //Nota: Modificado el orden, importante
            return new Object[]{row, column, distribution};
        } else {
            throw new Exception("Shelf not initialized, please create a shelf first.");
        }
    }

    /**
     * Calcula la distribucion de la estanteria actual con el algorithmo fast.
     * 
     * @param similitud similitud a utilizar
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlCalculateFast(Similitud similitud) throws Exception{
        if(shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        shelve.calculate_fast_distribution(similitud);
    }

    /**
     * Calcula la distribucion de la estanteria actual con el algorithmo slow.
     * 
     * @param similitud similitud a utilizar
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlCalculateSlow(Similitud similitud) throws Exception{
        if(shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        shelve.calculate_slow_distribution(similitud);
    }

    /**
     * Calcula la distribucion de la estanteria actual con el algorithmo third.
     * 
     * @param similitud similitud a utilizar
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlCalculateThird(Similitud similitud) throws Exception{
        if(shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        shelve.calculate_Random_distribution(similitud);
    }

    /**
     * Devuelve la distribucion de la estanteria actual.
     * 
     * @return distribucion de la estanteria actual en forma de lista de strings
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<String> CtrlDataAsListString() throws Exception{
        if(shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getDataShelveAsString();
    }

    /**
     * Añade un producto a la estanteria actual.
     * 
     * @param product nombre del producto a añadir
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlAddProductShelve(String product) throws Exception{

        if(shelve != null) {
            shelve.addProduct(product);
        }
        else throw new Exception("Shelf not initialized, please create a shelf first.");
    }

    /**
     * Elimina un producto de la estanteria actual.
     * 
     * @param product nombre del producto a eliminar
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlDeleteProductShelve(String product) throws Exception{

        if(shelve != null) {
            shelve.removeProduct(product);
        }
        else throw new Exception("Shelf not initialized, please create a shelf first.");
    }

    /**
     * Guarda en la estanteria actual, la que se ha importado.
     * @param shelve estanteria a guardar
     */
    public void CtrlSetShelve(Shelve shelve){
        this.shelve = shelve;
    }

    /**
     * Devuelve la estanteria
     * 
     * @return estanteria actual
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public Shelve CtrlGetShelve() throws Exception{
        if(shelve != null) return shelve;
        else throw new Exception("Shelf not initialized, please create a shelf first.");
    }

    /**
     * Devuelve la estanteria actual para exportarla.
     *
     * @return estanteria actual (Notice: Pot ser nul)
     */
    public Shelve CtrlGetShelveForExport() {
        return shelve;
    }



    /**
     * Devuelve el numero de productos de la estanteria actual.
     * 
     * @return numero de productos de la estanteria actual
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public int CtrlGetNumProds() throws Exception{
        if(shelve != null) return shelve.numProducts();
        else throw new Exception("Shelf not initialized, please create a shelf first.");
    }




    // Controller methods for fast stats
    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo fast, ordenadas por numero de iteracion.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllFastStatsByNumIteration() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllFastStatsByNumIteration();
    }

    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo fast, ordenadas por coste.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllFastStatsByCost() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllFastStatsByCost();
    }

    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo fast, ordenadas por timestamp.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    
    public List<Estadisticas> CtrlGetAllFastStatsByTimestamp() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllFastStatsByTimestamp();
    }

    /**
     * Resetea las estadisticas de las estanterias calculadas con el algortimo fast.
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlResetFastStats() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        shelve.resetFastStats();
    }

    // Controller methods for slow stats
    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo slow, ordenadas por numero de iteracion.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllSlowStatsByNumIteration() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllSlowStatsByNumIteration();
    }
    
    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo slow, ordenadas por coste.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllSlowStatsByCost() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllSlowStatsByCost();
    }

    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo slow, ordenadas por timestamp.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllSlowStatsByTimestamp() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllSlowStatsByTimestamp();
    }

    /**
     * Resetea las estadisticas de las estanterias calculadas con el algortimo slow.
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlResetSlowStats() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        shelve.resetSlowStats();
    }

    // Controller methods for third stats
    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo third, ordenadas por numero de iteracion.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllThirdStatsByNumIteration() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllThirdStatsByNumIteration();
    }

    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo third, ordenadas por coste.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllThirdStatsByCost() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllThirdStatsByCost();
    }

    /**
     * Devuelve todas las estadisticas de las estanterias calculadas con el algortimo third, ordenadas por timestamp.
     * @return lista de estadisticas
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public List<Estadisticas> CtrlGetAllThirdStatsByTimestamp() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        return shelve.getAllThirdStatsByTimestamp();
    }

    /**
     * Resetea las estadisticas de las estanterias calculadas con el algortimo third.
     * @throws Exception si la estanteria no ha sido inicializada
     */
    public void CtrlResetThirdStats() throws Exception {
        if (shelve == null) throw new Exception("Shelf not initialized, please create a shelf first.");
        shelve.resetThirdStats();
    }

}
