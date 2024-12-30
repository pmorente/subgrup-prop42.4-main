package edu.upc.prop.clusterxx;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * Clase que implementa un algoritmo para calcular distribuciones óptimas
 * utilizando un metodo de fuerza bruta
 * se usa un enfoque paralelo basado en ForkJoinPool, con una alternativa
 * secuencial en caso de fallo.
 */
public class AlgorithmSlow extends Algorithm {


    /**
     * Constructor de la clase AlgorithmSlow.
     *
     * @param n_rows Número de filas en la distribución.
     * @param n_columns Número de columnas en la distribución.
     * @param simil Matriz de similitudes entre los elementos.
     */
    AlgorithmSlow(int n_rows, int n_columns, float[][] simil){
        super(n_rows, n_columns, simil);
    }

    /**
     * Clase interna que representa una tarea
     * para explorar con el metodo de tree strategy con la granularidad mas fina possible
     */
    private class Tarea_buscar extends RecursiveAction {

        /**
         * Nivel actual en el arbol de búsqueda
         */
        private final int nivel;

        /**
         * Distribución parcial actual
         */
        private final ArrayList<Integer> distribucionActual;
        /**
        *  Lista que indica si un producto ya ha sido utilizado
        */
        private final ArrayList<Boolean> visto;

        /**
         * Coste acumulado hasta el nivel actual
         */
        private final double costeActual;

        /**
         * Constructor de Tarea_buscar.
         *
         * @param nivel Nivel actual en el árbol de búsqueda.
         * @param distribucionActual Distribución parcial actual.
         * @param visto Lista que indica si un producto ya ha sido utilizado.
         * @param costeActual Coste acumulado hasta el nivel actual.
         */
        public Tarea_buscar(int nivel, ArrayList<Integer> distribucionActual,
                                      ArrayList<Boolean> visto, double costeActual) {
            this.nivel = nivel;
            this.distribucionActual = new ArrayList<>(distribucionActual);
            this.visto = new ArrayList<>(visto);
            this.costeActual = costeActual;
        }

        /**
         * Método que realiza la busqueda de forma recursiva
         * Utilizando el tree strategy, con el punto de sincronización es en el caso base
         */
        @Override
        protected void compute() {
            if (nivel == n_prod) {
                synchronized (AlgorithmSlow.this) {
                    n_it++;
                    if (costeActual < mejorCoste) {
                        mejorCoste = costeActual;
                        set_distribucionFinal(distribucionActual);
                    }
                }
                return;
            }
            if (costeActual >= mejorCoste) return;
            List<Tarea_buscar> subTareas = new ArrayList<>();

            for (int producto = 0; producto < n_prod; producto++) {
                if (!visto.get(producto)) {
                    distribucionActual.set(nivel, producto);
                    visto.set(producto, true);

                    double nuevoCoste = costeActual + calcularCosteDistribucionParcial(distribucionActual, nivel);


                    Tarea_buscar task = new Tarea_buscar(nivel + 1,
                            new ArrayList<>(distribucionActual),
                            new ArrayList<>(visto),
                            nuevoCoste);
                    subTareas.add(task);

                    visto.set(producto, false);
                    distribucionActual.set(nivel, -1);
                }
            }
            invokeAll(subTareas);
        }
    }


    /**
     * Realiza una búsqueda secuencial para calcular la distribución óptima.
     * En caso de que haya algun error en el calcul paralelo
     *
     * @param nivel Nivel actual en el árbol de búsqueda.
     * @param distribucionActual Distribución parcial actual.
     * @param visto Lista que indica si un producto ya ha sido utilizado.
     * @param costeActual Coste acumulado hasta el nivel actual.
     */
    private void buscar_distribucion_sequencial(int nivel, ArrayList<Integer> distribucionActual,
                                     ArrayList<Boolean> visto, double costeActual) {
        if (nivel == n_prod) {
            n_it++;
            if (costeActual < mejorCoste) {
                mejorCoste = costeActual;
                set_distribucionFinal(distribucionActual);
            }
            return;
        }

        if (costeActual >= mejorCoste) return;

        for (int producto = 0; producto < n_prod; producto++) {
            if (!visto.get(producto)) {
                distribucionActual.set(nivel, producto);
                visto.set(producto, true);

                double nuevoCoste = costeActual + calcularCosteDistribucionParcial(distribucionActual, nivel);
                buscar_distribucion_sequencial(nivel + 1, distribucionActual,visto, nuevoCoste);

                visto.set(producto, false);
                distribucionActual.set(nivel, -1);
            }
        }
    }

    /**
     * Calcula el coste parcial de una distribución hasta un nivel dado.
     *
     * @param distribucionActual Distribución parcial actual.
     * @param nivel Nivel actual en el árbol de búsqueda.
     * @return Coste parcial calculado.
     */
    private double calcularCosteDistribucionParcial(ArrayList<Integer> distribucionActual, int nivel) {
        double costeParcial = 0f;
        for(int i = 0; i < nivel; i++) {
            double affinity = simil[distribucionActual.get(i)][distribucionActual.get(nivel)];
            costeParcial += affinity*calcularDistancia(i,nivel);
        }
        return costeParcial;
    }

    /**
     * Reimplementacion del metodo de Algorithm que
     * calcula la distribución óptima en paralelo
     * o alternativamente un método secuencial en caso de fallo.
     */
    public void calcular_distribucion() {
        super.calcular_distribucion();
        Instant ini = Instant.now();

        ArrayList<Integer> distribucionActual = new ArrayList<>(Collections.nCopies(n_prod, -1));
        ArrayList<Boolean> visto = new ArrayList<>(Collections.nCopies(n_prod, false));
        try {
            Tarea_buscar mainTask = new Tarea_buscar(0, distribucionActual, visto, 0);
            threadPool.invoke(mainTask);
        }
        catch (Exception e) {
            distribucionActual = new ArrayList<>(Collections.nCopies(n_prod, -1));
            visto = new ArrayList<>(Collections.nCopies(n_prod, false));
            buscar_distribucion_sequencial(0, distribucionActual, visto,0);
        }
        finally {
            threadPool.shutdown();
        }
        duration = Duration.between(ini, Instant.now());
    }
}
