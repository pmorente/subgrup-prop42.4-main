
package edu.upc.prop.clusterxx;

/**
 * Clase que gestiona los datos de una sesión.
 */
public class SessionData {

    /**
     * Catálogo de productos.
     */
    private CjtProduct catalog;

    /**
     * Instancia de Similitud.
     */
    private Similitud sim;

    /**
     * Estantería de la sesión.
     */
    private Shelve shelve;

    /**
     * Constructor que inicializa los datos de la sesión con los valores proporcionados.
     * 
     * @param catalog Catálogo de productos.
     * @param sim Instancia de Similitud.
     * @param shelve Estantería.
     */
    public SessionData(CjtProduct catalog, Similitud sim, Shelve shelve) {
        this.catalog = catalog;
        this.sim = sim;
        this.shelve = shelve;
    }

    /**
     * Obtiene el catálogo de productos asociado a la sesión.
     * 
     * @return Catálogo de productos.
     */
    public CjtProduct getCjtProduct() {
        return catalog;
    }

    /**
     * Obtiene la Similitud asociada a la sesión.
     * 
     * @return Instancia de Similitud.
     */
    public Similitud getSimilitud() {
        return sim;
    }

    /**
     * Obtiene el estante asociado a la sesión.
     * 
     * @return Estante asociado.
     */
    public Shelve getShelve() {
        return shelve;
    }
}
