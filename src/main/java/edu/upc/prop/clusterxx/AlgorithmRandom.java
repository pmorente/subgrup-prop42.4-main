package edu.upc.prop.clusterxx;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.stream.IntStream;


/**
 * Clase que implementa un algoritmo de aproximacion para calcular distribuciones.
 * Utiliza un enfoque hill climbing basado en conjuntos abiertos y cerrados (A*), con paralelismo para
 * explorar un nodo más rápido.
 * Utiliza una probabilidad de salto para explorar
 * soluciones alternativas y un umbral de distancia para determinar si una nueva solución
 * es suficientemente distinta.
 */
public class AlgorithmRandom extends Algorithm{

    /**
    *  Valor constante con la probabilidad de realizar un salto a una nueva distribución de productos.
    */
    private static final int p_salto = 50;

    /**
    * Representa el porcentaje de productos de la distribución que se utiliza para determinar el umbral de diferencia entre dos distribuciones.
    */
    private static final int lambda_threshold_percentage = 20;

    /**
    * Es el umbral de diferencia entre dos distribuciones de productos.
    */
    private int lambda_threshold = 5;

    /**
     * Constructor de la clase AlgorithmRandom.
     *
     * @param n_rows Número de filas en la distribución.
     * @param n_columns Número de columnas en la distribución.
     * @param simil Matriz de similitudes entre los elementos.
     */
    AlgorithmRandom(int n_rows, int n_columns, float[][] simil){
        super(n_rows, n_columns, simil);
        this.lambda_threshold = (int)(((n_rows*n_columns)*lambda_threshold_percentage)/100);
    }

    /**
     * Verifica si la distancia entre dos distribuciones es suficiente según el umbral definido.
     *
     * @param n1 Nodo representando la primera distribución.
     * @param n2 Nodo representando la segunda distribución.
     * @return {@code true} si la distancia supera el umbral, {@code false} en caso contrario.
     */
    private boolean distancia_suficiente(Nodo n1, Nodo n2) {
        int lambda = 0;
        for (int i = 0; i < n1.distribucion.size(); ++i) {
            if(!Objects.equals(n1.distribucion.get(i), n2.distribucion.get(i))) lambda++;
            if(lambda >= lambda_threshold) return true;
        }
        return false;
    }

    /**
     * Método principal para computar la distribución utilizando una estrategia heurística
     * con elementos aleatorios. Introduce saltos aleatorios basados en probabilidad.
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

            if(((int) (Math.random() * 101))<= p_salto) {
                ArrayList<Integer> dist_iniN = get_random_dist();
                Nodo startNewNode = new Nodo(dist_iniN, heuristica(dist_iniN));
                if(!closedSet.contains(startNewNode.distribucion.toString()) &&
                        distancia_suficiente(currentNode,startNewNode) &&
                        startNewNode.Cost < currentNode.Cost) {
                    openSet.clear();
                    openSet.add(startNewNode);
                    currentNode = startNewNode;
                    continue;
                }
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
     * Calcula la distribución aproximada utilizando un enfoque paralelo basado en
     * conjuntos abiertos y cerrados introduciendo la possibilidad de
     * hace un salto aleatorio para explorar las distribuciones posibles.
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