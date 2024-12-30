package edu.upc.prop.clusterxx;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Clase que implementa un algoritmo de aproximacion para calcular distribuciones.
 * Utiliza un enfoque hill climbing basado en conjuntos abiertos y cerrados (A*), con paralelismo para
 * explorar un nodo más rápido.
 */
public class AlgorithmFast extends Algorithm{

    /**
     * Constructor de la clase AlgorithmFast.
     *
     * @param n_rows Número de filas en la distribución.
     * @param n_columns Número de columnas en la distribución.
     * @param simil Matriz de similitudes entre los elementos.
     */
    AlgorithmFast(int n_rows, int n_columns, float[][] simil){
        super(n_rows, n_columns, simil);
    }

    /**
     * Método que implementa el algoritmo principal para explorar distribuciones.
     * Utiliza un conjunto abierto de nodos pendientes de explorar y un conjunto cerrado
     * para evitar procesar nodos repetidos.
     *
     * @throws Exception Si ocurre un error en la ejecución del algoritmo.
     */
    private void computar_distribucion() throws Exception {
        openSet = new PriorityBlockingQueue<>(n_prod,Comparator.comparingDouble((Nodo n) -> n.Cost));
        ArrayList<Integer> dist_ini = get_random_dist();
        Nodo currentNode = new Nodo(dist_ini, heuristica(dist_ini));
        openSet.add(currentNode);

        while (!openSet.isEmpty()) {
            String hashkey = currentNode.distribucion.toString();

            if (closedSet.contains(hashkey)) continue;
            closedSet.add(hashkey);

            if (currentNode.Cost < mejorCoste) {
                mejorCoste = currentNode.Cost;
                set_distribucionFinal(currentNode.distribucion);
            }

            final Nodo currentFinalNode = currentNode;
            threadPool.submit(()->{
                List<int[]> lista_vecinos = IntStream.range(0, n_prod - 1)
                        .boxed()
                        .flatMap(i -> IntStream.range(i + 1, n_prod)
                                .mapToObj(j -> new int[]{i, j}))
                        .toList();

                lista_vecinos.parallelStream().forEach(pair -> {
                    compute_neighbour(currentFinalNode, pair[0], pair[1]);
                });
            }).join();
            currentNode = openSet.take();
            n_it++;
        }
    }

    /**
     * Calcula la distribución buena aproximada utilizando un enfoque paralelo basado en
     * conjuntos abiertos y cerrados para explorar las distribuciones posibles.
     *
     * En caso de error, se captura y se informa en la salida estándar.
     */
    public void calcular_distribucion() {
        super.calcular_distribucion();
        Instant ini = Instant.now();
        try {
            computar_distribucion();
        }catch (Exception e) {
            System.out.println("Error en computar distribucion: " + e.getMessage());
        }
        finally {
            threadPool.shutdown();
        }
        duration = Duration.between(ini, Instant.now());
    }
}