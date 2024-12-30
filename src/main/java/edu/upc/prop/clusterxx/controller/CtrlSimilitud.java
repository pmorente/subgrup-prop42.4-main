package edu.upc.prop.clusterxx.controller;

import edu.upc.prop.clusterxx.Similitud;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar la logica relacionada con el objeto de similitud (Similitud).
 * Permite crear, modificar y obtener informacion sobre las similitudes entre productos.
 */

public class CtrlSimilitud {

    /**
     * Instancia del objeto Similitud que contiene la logica de las relaciones entre productos.
     */
     private Similitud similitud; //Similitud object

    /**
     * Constructor por defecto. Inicializa una nueva instancia de Similitud.
     */
    public CtrlSimilitud() {
        similitud = new Similitud();
    }

    /**
     * Crea una relacion de similitud entre dos productos con un coeficiente dado.
     *
     * @param producte1 El nombre del primer producto.
     * @param producte2 El nombre del segundo producto.
     * @param coef      El coeficiente de similitud entre los dos productos.
     * @throws Exception Si ocurre algun error al crear la similitud.
     */
    public void CtrlCreateSimilituds(String producte1, String producte2, Float coef) throws Exception{

        similitud.createSimilitud(producte1, producte2, coef);

    }

    /**
     * Anade un nuevo producto al sistema para considerarlo en las relaciones de similitud.
     *
     * @param name El nombre del producto a anadir.
     */
    public void CtrlAddProductSimilituds(String name){
        similitud.createProduct(name);
    }

    /**
     * Realiza una comprobación del estado interno del sistema de similitudes.
     * Este metodo puede ser utilizado para depurar o verificar que no existen inconsistencias.
     */
    public void CtrlHealthCheckSimilitud(){

        similitud.healthCheck();

    }

    /**
     * Muestra las relaciones de similitud entre todos los productos disponibles.
     * Cada relacion incluye los nombres de los dos productos y su coeficiente de similitud.
     *
     * @return Una lista de arrays de Strings, donde cada array contiene los datos de una relacion de similitud
     *         (nombre del primer producto, nombre del segundo producto, coeficiente de similitud).
     */
    public List<String[]> CtrlShowSimilituds(){
        List<String[]> similitudData = new ArrayList<>();

        for (int i = 0; i < similitud.numProds(); ++i) {
            for(int j = i+1; j < similitud.numProds(); ++j){
                if(i != j) {
                    String[] data = {similitud.traducir(i), similitud.traducir(j), String.valueOf(similitud.consultarSimilitud(i, j))};
                    similitudData.add(data);
                }
            }
        }

        return similitudData;

    }

    /**
     * Elimina una relacion de similitud entre dos productos especificos.
     *
     * @param primerProducte El nombre del primer producto.
     * @param segonProducte  El nombre del segundo producto.
     */
    public void CtrlRemoveSimilitud(String primerProducte, String segonProducte){

        similitud.eliminarSimilitud(primerProducte, segonProducte);

    }

    /**
     * Elimina un producto del sistema, eliminando tambien todas las relaciones de similitud asociadas a el.
     *
     * @param name El nombre del producto a eliminar.
     */
    public void CtrlRemoveProductSimilituds(String name){
        similitud.removeProduct(name);
    }

    /**
     * Obtiene el numero total de productos en el sistema.
     *
     * @return El numero de productos registrados.
     */
    public int CtrlGetNumSimilituds() {
        return similitud.numProds();
    }

    /**
     * Obtiene la instancia actual del objeto Similitud.
     *
     * @return La instancia actual de Similitud.
     */
    public Similitud CtrlGetSimilitud(){
        return similitud;
    }

    /**
     * Establece una nueva instancia de Similitud para este controlador.
     *
     * @param similitud La nueva instancia de Similitud.
     */
    public void CtrlSetSimilitud(Similitud similitud){
        this.similitud = similitud;
    }

    /**
     * Obtiene la lista de similitudes en formato de lista de Strings.
     * Cada elemento de la lista representa una relacion de similitud.
     *
     * @return Una lista de Strings con las relaciones de similitud.
     */
    public List<String> CtrlDataAsListString() {
        return similitud.getSimilaritiesList();
    }
}
