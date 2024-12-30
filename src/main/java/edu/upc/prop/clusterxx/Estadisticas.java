package edu.upc.prop.clusterxx;
import java.time.LocalDateTime;
import java.util.ArrayList;
/**
 * Clase que representa las estadísticas de una ejecución de un algoritmo.
 * Incluye información como la distribución, dimensiones de la estantería, costo,
 * número de iteraciones y una marca de tiempo.
 */
public class Estadisticas {

    /**
     * Marca de tiempo que indica cuándo se creó esta instancia de Estadisticas.
     */
    private LocalDateTime timestamp;

    /**
     * Distribución de la estantería calcualda.
     */
    private ArrayList<String> distribution;

    /**
     * Número de columnas en la estantería.
     */
    private int columns;

    /**
     * Número de filas en la estantería.
     */
    private int rows;

    /**
     * Tiempo de ejecución del algoritmo.
     */
    private double time;

    /**
     * Costo de la distribución.
     */
    private double cost;

    /**
     * Número de iteraciones realizadas por el algoritmo.
     */
    private int number_iterations;

    /**
     * Constructor que inicializa las estadísticas con los valores proporcionados.
     * 
     * @param distribution Lista que representa la distribución de elementos.
     * @param columns Número de columnas de la estantería.
     * @param rows Número de filas de la estantería.
     * @param time Tiempo de ejecución del algoritmo.
     * @param cost Costo del algoritmo.
     * @param number_iterations Número de iteraciones realizadas.
     */
    public Estadisticas(ArrayList<String> distribution, int columns, int rows, double time, double cost, int number_iterations) {
        this.timestamp = LocalDateTime.now(); // Establece la hora actual como la marca de tiempo
        this.distribution = distribution;
        this.columns = columns;
        this.rows = rows;
        this.time = time;
        this.cost = cost;
        this.number_iterations = number_iterations;
    }

    /**
     * Constructor por defecto que inicializa las estadísticas con valores predeterminados.
     */
    public Estadisticas() {
        this.timestamp = LocalDateTime.now(); // Establece la hora actual como la marca de tiempo
        this.distribution = null;
        this.columns = 0;
        this.rows = 0;
        this.time = 0.0;
        this.cost = 0.0;
        this.number_iterations = 0;
    }

    /**
     * Obtiene la distribución de la estantería.
     * 
     * @return Lista que representa la distribución de productos.
     */
    public ArrayList<String> getDistribution() {
        return distribution;
    }

    /**
     * Obtiene el tiempo de ejecución asociado.
     * 
     * @return Tiempo de ejecución en segundos.
     */
    public double getTime() {
        return time;
    }

    /**
     * Obtiene el costo de la distribución.
     * 
     * @return Costo de la distribución.
     */
    public double getCost() {
        return cost;
    }

    /**
     * Obtiene el número de iteraciones realizadas.
     * 
     * @return Número de iteraciones.
     */
    public int getNumberIterations() {
        return number_iterations;
    }

    /**
     * Obtiene el número de columnas de la estantería.
     * 
     * @return Número de columnas.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Obtiene el número de filas de la estantería.
     * 
     * @return Número de filas.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Obtiene la marca de tiempo de creación de esta instancia.
     * 
     * @return Marca de tiempo.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

}
