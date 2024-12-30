package edu.upc.prop.clusterxx;

import java.util.*;
import java.time.Duration;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;


/**
 * Super clase que implementa las funciones y estructuras de datos comunes en los distintos algoritmos
 *  para calcular distribuciones de productos.
 */
public class Algorithm {

    /**
     * Mejor coste en las iteraciones del algoritmo
     */
    protected double mejorCoste;

    /**
     * Número de productos en la estantería
     */
    protected int n_prod = -1;

    /**
     * Número de iteraciones del algoritmo
     */
    protected int n_it = 0;

    /**
     * Matriz de coeficientes de similitudes
     */
    protected float[][] simil = null;

    /**
     * Duración en tiempo del algoritmo
     */
    protected Duration duration = null;

    /**
     * Número de filas de la estantería
     */
    private int n_rows = -1;

    /**
     * Número de columnas de la estantería
     */
    private int n_columns = -1;

    /**
     * Distribución calculada de la estentería
     */
    private ArrayList<Integer> distribucionFinal = null;

    /**
     * Número de threads que va a usar el algoritmo para calcularse
     */
    protected int num_threads = 1;

    /**
     * Conjunto de nodos cerrados, es decir, nodos ya visitados en los algoritmos de aproximación.
     */
    protected HashSet<String> closedSet;

    /**
    * Cola de prioridad con nodos por visitar, ordenados según el coste de la distribución. Es una BlockingQueue para evitar condiciones de carrera en contextos paralelos.
    */
    protected PriorityBlockingQueue<Nodo> openSet;

    /**
    * Conjunto de hilos que ejecutan las tareas de exploración de la distribución.
    */
    protected ForkJoinPool threadPool;

    /**
     * Constructor de la clase.
     *
     * @param n_rows Número de filas en la estanteria.
     * @param n_columns Número de columnas en la estanteria.
     * @param simil Matriz de similitudes entre los elementos.
     */
    Algorithm(int n_rows, int n_columns, float[][] simil) {
        this.n_rows = n_rows;
        this.n_columns = n_columns;
        this.simil = simil;
        this.n_prod = n_rows*n_columns;
        this.mejorCoste = Double.MAX_VALUE;
        // Determinamos el numero de threads que podemos en funcion del numero de threads disponibles en el ordenador
        this.num_threads = Math.max(1,Runtime.getRuntime().availableProcessors() - 2);
        this.distribucionFinal = null;
    }

    /**
     * Calcula la distancia entre dos posiciones en la matriz
     * sin tener en cuenta las adyacencias entre el primer y ultimo elemento
     *
     * @param pos1 Primera posición.
     * @param pos2 Segunda posición.
     * @return Distancia parcial calculada.
     */
    private int calcDist_parcial(int pos1, int pos2) {
        int c1 = pos1 % n_columns;
        int r1 = pos1 / n_columns;
        int c2 = pos2 % n_columns;
        int r2 = pos2 / n_columns;
        int dc = Math.abs(c1 - c2);
        dc = Math.min(dc, (n_columns-1) - dc);
        int dr = Math.abs(r1-r2);
        return dc+dr;
    }

    /**
     * Calcula la distancia entre dos posiciones final considerando adyacencias entre el primer y ultimo elemento.
     *
     * @param pos1 Primera posición.
     * @param pos2 Segunda posición.
     * @return Distancia calculada.
     */
    protected int calcularDistancia(int pos1, int pos2) {
        if (n_prod == 2 ) return 1;
        int t1 = calcDist_parcial(pos1,pos2);
        int t2 = calcDist_parcial(pos1,0) + calcDist_parcial(pos2, n_prod - 1) + 1;
        int t3 = calcDist_parcial(pos2,0) + calcDist_parcial(pos1, n_prod - 1) + 1;
        return Math.min(t3,Math.min(t2,t1));
    }

    /**
     * Setter de la distribución final una vez ha sido calculada.
     *
     * @param distribucionFinal Distribución a establecer.
     */
    protected void set_distribucionFinal(ArrayList<Integer> distribucionFinal) {
        this.distribucionFinal = new ArrayList<>(distribucionFinal);
    }

    /**
     * Calcula el coste total de una distribución.
     *
     * @param distribucionActual Distribución de la que se calculara el coste.
     * @return Coste total calculado.
     */
    public double calcularCosteDistribucion(ArrayList<Integer> distribucionActual) {
        double costeTotal = 0f;
        for (int i = 0; i < n_prod; ++i){
            for (int j = i+1; j < n_prod; ++j) {
                double affinity = simil[distribucionActual.get(i)][distribucionActual.get(j)];
                costeTotal += affinity*calcularDistancia(i,j);
            }
        }
        return costeTotal;
    }

    /**
     * Genera una distribución aleatoria.
     *
     * @return Lista con la distribución generada aleatoriamente.
     */
    protected ArrayList<Integer> get_random_dist() {
        ArrayList<Integer> dist = new ArrayList<>(n_prod);
        for (int i = 0; i < n_prod; i++) {
            dist.add(i);
        }
        Collections.shuffle(dist);
        return dist;
    }

    /**
     * Calcula la heurística para una distribución parcial.
     *
     * @param distribucionParcial Distribución parcial.
     * @return Valor heurístico calculado.
     */
    protected double heuristica(ArrayList<Integer> distribucionParcial) {
        return calcularCosteDistribucion(distribucionParcial);
    }

    /**
     * Calcula la heurística optimizada considerando unicamente
     * los cambios que genran una nueva distribucion
     * respecto al coste anterior dos posiciones intercambiadas.
     *
     * @param distribucionNueva Distribución nueva.
     * @param pCost Coste previo.
     * @param i Primera posición.
     * @param j Segunda posición.
     * @return Valor heurístico optimizado.
     */
    protected double heuristica(ArrayList<Integer> distribucionNueva, double pCost, int i, int j) {
        double CostParcial_antiguo = 0.0f;
        double CostParcial_nuevo = 0.0f;
        for (int ri = 0; ri < n_prod; ri++) {
            if ((ri != i ) && (ri != j)) {
                double af_antigua = simil[distribucionNueva.get(ri)][distribucionNueva.get(i)];
                double af_nueva = simil[distribucionNueva.get(ri)][distribucionNueva.get(j)];

                int dist_antigua = calcularDistancia(ri, i);
                int dist_nueva = calcularDistancia(ri, j);

                CostParcial_antiguo += (af_antigua * dist_antigua) + (af_nueva * dist_nueva);
                CostParcial_nuevo += (af_antigua * dist_nueva) + (af_nueva * dist_antigua);
            }
        }
        return pCost - CostParcial_antiguo + CostParcial_nuevo;
    }

    /**
     * Calcula el vecino de un nodo actual, evaluando el impacto del intercambio
     * de dos posiciones en la distribución.
     *
     * @param currentNode Nodo actual que se está procesando.
     * @param i índice de la primera posición a intercambiar.
     * @param j índice de la segunda posición a intercambiar.
     */
    protected void compute_neighbour(Nodo currentNode, int i, int j) {
        double n_coste = heuristica(currentNode.distribucion, currentNode.Cost, i, j);
        if(n_coste >= mejorCoste) return;
        ArrayList<Integer> nuevaDistribucion = new ArrayList<>(currentNode.distribucion);
        Collections.swap(nuevaDistribucion, i, j);
        if(closedSet.contains(nuevaDistribucion.toString())) return;
        Nodo neighborNode = new Nodo(nuevaDistribucion, n_coste);
        openSet.add(neighborNode);
    }

    /**
     * Clase interna que representa un nodo en el espacio de búsqueda.
     */
    protected class Nodo {
        /**
         * Distribucion del nodo actual
         */
        ArrayList<Integer> distribucion;

        /**
        * Coste del nodo actual
        */
        double Cost;
        /**
         * Constructora de Nodo.
         *
         * @param distribucion Distribución del nodo.
         * @param Cost Coste asociado.
         */
        Nodo(ArrayList<Integer> distribucion, double Cost) {
            this.distribucion = distribucion;
            this.Cost = Cost;
        }
    }

    /**
     * Consultora del número de iteraciones realizadas.
     *
     * @return Número de iteraciones.
     */
    public int get_numberIterations() {
        return n_it;
    }

    /**
     * Consuldtora el coste de la distribución final.
     *
     * @return Coste de la distribución final.
     * @throws IllegalStateException Si no se ha calculado una distribución con algun algoritmo.
     */
    public double get_Coste_distribucion() {
        if(distribucionFinal == null) throw new IllegalStateException("A distribution wasn't calculated previously");
        return calcularCosteDistribucion(distribucionFinal);
    }

    /**
     * Consultora de la distribución final calculada.
     *
     * @return Distribución final.
     * @throws IllegalStateException Si no se ha calculado una distribución con algun algoritmo.
     */
    public ArrayList<Integer> get_distribucionFinal() {
        if(distribucionFinal == null) throw new IllegalStateException("A distribution wasn't calculated previously");
        return distribucionFinal;
    }

    /**
     * Devuelve el tiempo transcurrido durante la ejecución.
     *
     * @return Duración de la ejecución.
     * @throws IllegalStateException Si no se ha calculado una distribución.
     */
    public Duration getTiempoTranscurrido() {
        if(duration == null) throw new IllegalStateException("A distribution wasn't calculated previously");
        return duration;
    }

    /**
     * Calcula la distribución final. Este metodo es reeimplementado en las
     * subclases de cada tipo de algoritmo.
     */
    public void calcular_distribucion() {
        n_prod = n_columns * n_rows;
        closedSet = new HashSet<>();
        n_it = 1;
        mejorCoste = Double.MAX_VALUE;
        threadPool = new ForkJoinPool(num_threads);
    }
}
