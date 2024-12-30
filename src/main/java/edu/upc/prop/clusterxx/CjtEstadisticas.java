package edu.upc.prop.clusterxx;

import java.time.LocalDateTime;
import java.util.*;
/**
 * Clase que gestiona un conjunto de objetos Estadisticas, proporcionando diferentes formas de almacenarlos,
 * ordenarlos y acceder a ellos en base a criterios como fecha de creacion, numero de iteraciones o coste.
 */
public class CjtEstadisticas {

    /**
     * Lista que define un conjunto de estadisticas.
     */
    private ArrayList<Estadisticas> conj_estadisticas;

    /**
     * Constructor que inicializa las estructuras de datos utilizadas para gestionar los objetos Estadisticas.
     */
    public CjtEstadisticas() {
        conj_estadisticas = new ArrayList<>();
    }

    /**
     * Adds a new Estadisticas object to the collection.
     *
     * @param estadisticas The Estadisticas object to add.
     */
    public void addEstadistica(Estadisticas estadisticas) {
        conj_estadisticas.add(estadisticas);
    }

    /**
     * Returns all Estadisticas objects sorted by creation date (most recent first).
     *
     * @return List of sorted Estadisticas objects
     */
    public List<Estadisticas> getEstadisticasSortedByCreationDate() {
        Collections.sort(conj_estadisticas,(e1,e2)->(e2.getTimestamp().compareTo(e1.getTimestamp())));
        return conj_estadisticas;
    }

    /**
     * Returns a list of Estadisticas objects sorted by number of iterations (descending).
     *
     * @return List of Estadisticas sorted by iterations
     */
    public List<Estadisticas> getNumberOfIterationsList() {
        Collections.sort(conj_estadisticas,(e1,e2)->(Integer.compare(e2.getNumberIterations(),e1.getNumberIterations())));
        return conj_estadisticas;
    }

    /**
     * Returns a list of Estadisticas objects sorted by cost (descending).
     *
     * @return List of Estadisticas sorted by cost
     */
    public List<Estadisticas> getCostList() {
        Collections.sort(conj_estadisticas,(e1,e2)->(Double.compare(e2.getCost(),e1.getCost())));
        return conj_estadisticas;
    }

    /**
     * Clears all Estadisticas objects from the collection.
     */
    public void reset() {
        conj_estadisticas.clear();
    }

}


