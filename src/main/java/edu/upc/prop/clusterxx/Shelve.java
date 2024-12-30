package edu.upc.prop.clusterxx;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Clase que representa una estantería (Shelve) para gestionar productos en una distribución,
 * con diferentes métodos para añadir, eliminar, intercambiar productos y calcular costos de distribución
 * con diferentes algoritmos.
 */
public class Shelve {

    /**
     * Numero de filas de la estanteria
     */
    private int n_rows;

    /**
     * Numero de columnas de la estanteria.
     */
    private int n_columns;

    /**
     * Lista que define la distribucion de productos.
     * Puede almacenar identificadores o nombres de productos en un formato especifico.
     */
    protected ArrayList<String> distribution;

    /**
     * Numero total de productos gestionados por la clase.
     */
    private int num_prods;

    /**
     * Objeto para gestionar estadisticas rapidas (fastStats).
     */
    CjtEstadisticas fastStats = new CjtEstadisticas();
    /**
     * Objeto para gestionar estadisticas lentas (slowStats).
     */
    CjtEstadisticas slowStats = new CjtEstadisticas();
    /**
     * Objeto para gestionar una tercera categoria de estadisticas (thirdStats).
     */
    CjtEstadisticas thirdStats = new CjtEstadisticas();

    /**
     * Estadisticas del ultimo algoritmo ejecutado.
     */
    Estadisticas Last_algorithm_stats = null;

    /**
     * Obtiene el número total de productos en la estantería.
     * 
     * @return El número de productos.
     */
    public int numProducts() {
        return num_prods;
    }

    /**
     * Obtiene la dimensión total de la estantería (el número de celdas de la estantería).
     * 
     * @return El número de celdas en la estantería.
     */
    public int dimensionShelve() {
        return n_rows * n_columns;
    }

    /**
     * Obtiene las estadísticas del último algoritmo ejecutado.
     *
     * @return Las estadísticas del último algoritmo ejecutado.
     */
    public Estadisticas Get_last_algorithm_stats() {
        return Last_algorithm_stats;
    }

    /**
     * Constructor de la clase Shelve.
     * 
     * @param fila Número de filas en la estantería.
     * @param columna Número de columnas en la estantería.
     * @param listaProductos Lista de productos para colocar en la estantería.
     * @throws IllegalArgumentException Si las dimensiones son negativas o el número de productos no coincide
     *                                  con el espacio disponible en la estantería o si hay productos repetidos.
     */
    public Shelve(int fila, int columna, String[] listaProductos) {
        if (fila < 0 || columna < 0) {
            throw new IllegalArgumentException("Error: rows must be > 0 and columns must be > 0");
        }
        if (fila * columna != listaProductos.length) {
            int numero = fila * columna;
            throw new IllegalArgumentException("Incorrect number of products, number of products must be " + listaProductos.length);
        }

        Set<String> repetidos = new HashSet<>();
        distribution = new ArrayList<>(); // Ensure distribution is initialized here if not done already.

        for (int i = 0; i < listaProductos.length; ++i) {
            if (!repetidos.add(listaProductos[i])) throw new IllegalArgumentException("repeated products");
            distribution.add(listaProductos[i]);
        }
        n_rows = fila;
        n_columns = columna;
        num_prods = fila * columna;
        this.Last_algorithm_stats = null;
    }

    /**
     * Obtiene el costo actual de la estantería.
     * 
     * @param simil El objeto Similitud que contiene la matriz de similitudes.
     * @return El costo actual de la estantería.
     * @throws IllegalArgumentException Si el tamaño de la estantería no coincide con el número de productos en el objeto Similitud.
     */
    public double get_current_shelve_cost(Similitud simil) {
        if (n_columns * n_rows != simil.numProds()) {
            throw new IllegalArgumentException("The size of the shelve dosen't match with the number of products");
        }
        ArrayList<Integer> translateList = new ArrayList<>();
        for (String pid : distribution) {
            translateList.add(simil.traducir_id(pid));
        }
        return new Algorithm(n_rows, n_columns, simil.getMatrix()).calcularCosteDistribucion(translateList);
    }


    /**
     * Intercambia las posiciones de dos productos en la estantería.
     * 
     * @param name El nombre del primer producto.
     * @param name2 El nombre del segundo producto.
     * @throws IllegalArgumentException Si alguno de los productos no existe en la estantería.
     */
    public void exchangePositions(String name, String name2) {
        if (!distribution.contains(name) || !distribution.contains(name2)) {
            if (!distribution.contains(name) && !distribution.contains(name2)) {
                throw new IllegalArgumentException("The products " + name + " and " + name2 + " don't exist in the shelve");
            } else if (!distribution.contains(name)) {
                throw new IllegalArgumentException("The product " + name + " doesn't exist in the shelve");
            } else {
                throw new IllegalArgumentException("The product " + name2 + " doesn't exist in the shelve");
            }
        }
        int indexName = distribution.indexOf(name);
        int indexName2 = distribution.indexOf(name2);
        distribution.set(indexName, name2);
        distribution.set(indexName2, name);
    }

    /**
     * Elimina un producto de la estantería, sustituyéndolo por un espacio vacío ("-").
     * 
     * @param prod El nombre del producto a eliminar.
     * @throws IllegalArgumentException Si el producto no existe en la estantería.
     */
    public void removeProduct(String prod) {
        if(!distribution.contains(prod)) throw new IllegalArgumentException("the product " + prod + " doesn't exist in the shelve");
        int indexName = distribution.indexOf(prod);
        distribution.set(indexName, "-");
        --num_prods;
    }

    /**
     * Añade un producto a la estantería en un espacio vacío.
     * 
     * @param prod El nombre del producto a añadir.
     * @throws IllegalArgumentException Si el producto ya existe en la estantería.
     * @throws IllegalStateException Si no hay espacio vacío disponible para añadir el producto.
     */
    public void addProduct(String prod) {
        if (distribution.contains(prod)) {
            throw new IllegalArgumentException("The product " + prod + " already exists in the shelf");
        }

        int emptySlotIndex = distribution.indexOf("-");
        if (emptySlotIndex != -1) {
            distribution.set(emptySlotIndex, prod);
            num_prods++;
        } else {
            throw new IllegalStateException("No empty slot available to add the product " + prod);
        }
    }

    /**
     * Obtiene la representación en forma de cadena de la estantería.
     * 
     * @return Una cadena que representa la estantería en formato CSV.
     */
    public List<String> getDataShelveAsString() {
        List<String> data = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(n_rows).append(",");
        sb.append(n_columns).append(",");

        // Convert the distribution list to a CSV-friendly string
        String distributionString = distribution.stream()
                .collect(Collectors.joining(";")); // Join elements with a chosen delimiter
        sb.append(distributionString);

        data.add(sb.toString());
        return data;
    }

    /**
     * Obtiene el número de filas de la estantería.
     * 
     * @return El número de filas de la estantería.
     */
    public int getRow() {
        return n_rows;
    }

    /**
     * Obtiene el número de columnas de la estantería.
     * 
     * @return El número de columnas de la estantería.
     */
    public int getColumn() {
        return n_columns;
    }

    /**
     * Obtiene la distribución actual de productos en la estantería.
     * 
     * @return Una lista con la distribución actual de productos.
     */
    public ArrayList<String> getDistributionShelve() {
        return distribution;
    }

    /**
     * Calcula la distribución rápida de productos utilizando un algoritmo específico.
     * 
     * @param simil El objeto Similitud que contiene la matriz de similitudes.
     * @throws IllegalArgumentException Si el tamaño de la estantería no coincide con el número de productos en el objeto Similitud.
     */
    public void calculate_fast_distribution(Similitud simil) {
        if (n_columns * n_rows != simil.numProds()) {
            throw new IllegalArgumentException("The size of the shelve dosen't match with the number of products");
        }
        AlgorithmRandom algF = new AlgorithmRandom(n_rows, n_columns, simil.getMatrix());
        algF.calcular_distribucion();
        double time = algF.getTiempoTranscurrido().getSeconds();
        double cost = algF.get_Coste_distribucion();
        int num_it = algF.get_numberIterations();
        ArrayList<Integer> newDistr = algF.get_distribucionFinal();

        ArrayList<String> translateList = new ArrayList<>();
        for (int pid : newDistr) {
            translateList.add(simil.traducir(pid));
        }
        distribution = translateList;

        Last_algorithm_stats = new Estadisticas(distribution, n_columns, n_rows, time, cost, num_it);
        fastStats.addEstadistica(Last_algorithm_stats);
    }

    /**
     * Calcula la distribución aleatoria de productos utilizando un algoritmo específico.
     * 
     * @param simil El objeto Similitud que contiene la matriz de similitudes.
     * @throws IllegalArgumentException Si el tamaño de la estantería no coincide con el número de productos en el objeto Similitud.
     */
    public void calculate_Random_distribution(Similitud simil) {
        if (n_columns * n_rows != simil.numProds()) {
            throw new IllegalArgumentException("The size of the shelve dosen't match with the number of products");
        }
        AlgorithmRandom algF = new AlgorithmRandom(n_rows, n_columns, simil.getMatrix());
        algF.calcular_distribucion();
        double time = algF.getTiempoTranscurrido().getSeconds();
        double cost = algF.get_Coste_distribucion();
        int num_it = algF.get_numberIterations();
        ArrayList<Integer> newDistr = algF.get_distribucionFinal();

        ArrayList<String> translateList = new ArrayList<>();
        for (int pid : newDistr) {
            translateList.add(simil.traducir(pid));
        }
        distribution = translateList;

        Last_algorithm_stats= new Estadisticas(distribution, n_columns, n_rows, time, cost, num_it);
        thirdStats.addEstadistica(Last_algorithm_stats);
    }

    /**
     * Calcula la distribución lenta de productos utilizando un algoritmo específico.
     * 
     * @param simil El objeto Similitud que contiene la matriz de similitudes.
     * @throws IllegalArgumentException Si el tamaño de la estantería no coincide con el número de productos en el objeto Similitud.
     */
    public void calculate_slow_distribution(Similitud simil) {
        if (n_columns * n_rows != simil.numProds()) {
            throw new IllegalArgumentException("The size of the shelve dosen't match with the number of products");
        }
        AlgorithmSlow algF = new AlgorithmSlow(n_rows, n_columns, simil.getMatrix());
        algF.calcular_distribucion();
        double time = algF.getTiempoTranscurrido().getSeconds();
        double cost = algF.get_Coste_distribucion();
        int num_it = algF.get_numberIterations();
        ArrayList<Integer> newDistr = algF.get_distribucionFinal();

        ArrayList<String> translateList = new ArrayList<>();
        for (int pid : newDistr) {
            translateList.add(simil.traducir(pid));
        }
        distribution = translateList;

        Last_algorithm_stats = new Estadisticas(distribution, n_columns, n_rows, time, cost, num_it);
        slowStats.addEstadistica(Last_algorithm_stats);
    }

    /**
     * Obtiene todas las estadísticas de distribución rápida ordenadas por el número de iteraciones.
     * 
     * @return Una lista de estadísticas ordenadas por número de iteraciones.
     */
    public List<Estadisticas> getAllFastStatsByNumIteration() {
        return fastStats.getNumberOfIterationsList();
    }

    /**
     * Obtiene todas las estadísticas de distribución lenta ordenadas por el número de iteraciones.
     * 
     * @return Una lista de estadísticas ordenadas por número de iteraciones.
     */
    public List<Estadisticas> getAllSlowStatsByNumIteration() {
        return slowStats.getNumberOfIterationsList();
    }

    /**
     * Obtiene todas las estadísticas de distribución aleatoria ordenadas por el número de iteraciones.
     * 
     * @return Una lista de estadísticas ordenadas por número de iteraciones.
     */
    public List<Estadisticas> getAllThirdStatsByNumIteration() {
        return thirdStats.getNumberOfIterationsList();
    }

    /**
     * Obtiene todas las estadísticas de distribución rápida ordenadas por costo.
     * 
     * @return Una lista de estadísticas ordenadas por costo.
     */
    public List<Estadisticas> getAllFastStatsByCost() {
        return fastStats.getCostList();
    }

    /**
     * Obtiene todas las estadísticas de distribución lenta ordenadas por costo.
     * 
     * @return Una lista de estadísticas ordenadas por costo.
     */
    public List<Estadisticas> getAllSlowStatsByCost() {
        return slowStats.getCostList();
    }

    /**
     * Obtiene todas las estadísticas de distribución aleatoria ordenadas por costo.
     * 
     * @return Una lista de estadísticas ordenadas por costo.
     */
    public List<Estadisticas> getAllThirdStatsByCost() {
        return thirdStats.getCostList();
    }

    /**
     * Obtiene todas las estadísticas de distribución rápida ordenadas por fecha y hora de creación.
     * 
     * @return Una lista de estadísticas ordenadas por fecha y hora.
     */
    public List<Estadisticas> getAllFastStatsByTimestamp() {
        return fastStats.getEstadisticasSortedByCreationDate();
    }

    /**
     * Obtiene todas las estadísticas de distribución lenta ordenadas por fecha y hora de creación.
     * 
     * @return Una lista de estadísticas ordenadas por fecha y hora.
     */
    public List<Estadisticas> getAllSlowStatsByTimestamp() {
        return slowStats.getEstadisticasSortedByCreationDate();
    }

    /**
     * Obtiene todas las estadísticas de distribución aleatoria ordenadas por fecha y hora de creación.
     * 
     * @return Una lista de estadísticas ordenadas por fecha y hora.
     */
    public List<Estadisticas> getAllThirdStatsByTimestamp() {
        return thirdStats.getEstadisticasSortedByCreationDate();
    }

    /**
     * Resetea las estadísticas rápidas.
     */
    public void resetFastStats() {
        fastStats.reset();
    }

    /**
     * Resetea las estadísticas lentas.
     */
    public void resetSlowStats() {
        slowStats.reset();
    }

    /**
     * Resetea las estadísticas aleatorias.
     */
    public void resetThirdStats() {
        thirdStats.reset();
    }
}
