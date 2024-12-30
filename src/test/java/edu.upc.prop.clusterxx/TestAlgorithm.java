package edu.upc.prop.clusterxx;

import org.junit.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

/**
 * Clase para comprobar el correcto funcionamiento de la clase Algorithm
 */
public class TestAlgorithm {
    /**
     * Calcula el coste de una distribucion utilizando la matriz de similitudes.
     *
     * @param distribucion Lista de enteros que representa la distribucion de los productos.
     * @param simil        Objeto de tipo Similitud que contiene la matriz de similitudes.
     * @param num_fila     Numero de filas de la distribucion.
     * @param num_columnas Numero de columnas de la distribucion.
     * @return El coste calculado de la distribucion.
     */
    private double get_coste(ArrayList<Integer> distribucion, Similitud simil,
                             int num_fila, int num_columnas) {
        return new Algorithm(num_fila, num_columnas, simil.getMatrix()).calcularCosteDistribucion(distribucion);
    }

    /**
     * Verifica si dos costes son iguales dentro de un margen de tolerancia.
     *
     * @param c1    Primer coste a comparar.
     * @param c2    Segundo coste a comparar.
     * @param DELTA Margen de tolerancia permitido.
     * @return true si los costes son equivalentes dentro del margen, false en caso contrario.
     */
    private boolean equal_cost(double c1, double c2, double DELTA) {
        return (c1 >= c2 - DELTA) && (c1 <= c2 + DELTA);
    }

    /**
     * Crea una matriz de similitudes conocida para un conjunto de productos.
     *
     * @param num_prod Numero de productos.
     * @return Objeto Similitud que contiene la matriz de similitudes generada.
     */
    private Similitud known_simil_distribution(int num_prod) {
        Similitud simil = new Similitud();
        for (int i = 0; i < num_prod; ++i) {
            simil.createProduct("Producto_" + i);
        }
        for (int i = 0; i < num_prod - 1; ++i) {
            for (int j = i; j < num_prod; ++j) {
                if(i!=j) simil.createSimilitud("Producto_" + (j), "Producto_" + (i), 0.01f);
            }
        }
        simil.createSimilitud("Producto_" + 0, "Producto_" + (num_prod - 1), 0.99f);
        for (int i = 1; i < num_prod; ++i) {
            simil.createSimilitud("Producto_" + (i - 1), "Producto_" + (i), 0.99f);
        }
        return simil;
    }

    /**
     * Crea una matriz de similitudes aleatoria para un numero de filas y columnas.
     *
     * @param num_fila    Numero de filas.
     * @param num_columna Numero de columnas.
     * @return Objeto Similitud con una matriz de similitudes generada aleatoriamente.
     */
    private Similitud random_simil_distribution(int num_fila, int num_columna) {
        int num_prod = num_columna * num_fila;
        Similitud simil = new Similitud();
        for (int i = 0; i < num_prod; ++i) {
            simil.createProduct("Producto_" + i);
        }
        for (int i = 0; i < num_prod - 1; ++i) {
            for (int j = i; j < num_prod; ++j) {
                simil.createSimilitud("Producto_" + (j), "Producto_" + (i), (float) Math.random());
            }
        }
        return simil;
    }

    /**
     * Crea una distribucion esperada con los productos ordenados secuencialmente.
     *
     * @param num_prod Numero de productos.
     * @return Lista de enteros que representa la distribucion esperada.
     */
    private ArrayList<Integer> expected_simil_distribution(int num_prod) {
        ArrayList<Integer> r = new ArrayList<>();
        for (int i = 0; i < num_prod; ++i) r.add(i);
        return r;
    }

    /**
     * Prueba para verificar la ejecucion del algoritmo lento (AlgorithmSlow).
     * Compara el coste y la distribucion obtenidos con los esperados.
     */
    @Test
    public void test_execution_slow_algorithm() {
        int num_prod = 10;
        Similitud simil = known_simil_distribution(num_prod);
        ArrayList<Integer> expected_result = expected_simil_distribution(num_prod);
        AlgorithmSlow algorithm = new AlgorithmSlow(1,num_prod,simil.getMatrix());
        algorithm.calcular_distribucion();
        double coste_esperado = get_coste(expected_result,simil,1,num_prod);
        assertTrue("Costes diferentes",equal_cost(coste_esperado,algorithm.get_Coste_distribucion(),1e-8));
        assertNotNull(algorithm.get_distribucionFinal());
        assertEquals(algorithm.get_distribucionFinal().toString(),expected_result.toString());
    }
    /**
     * Prueba para verificar la ejecucion del algoritmo rapido (AlgorithmFast).
     * Compara el coste obtenido con el esperado.
     */
    @Test
    public void test_execution_fast_algorithm() {
        int num_prod = 50;
        Similitud simil = known_simil_distribution(num_prod);
        ArrayList<Integer> expected_result = expected_simil_distribution(num_prod);
        AlgorithmFast algorithm = new AlgorithmFast(1,num_prod,simil.getMatrix());
        algorithm.calcular_distribucion();
        double coste_esperado = get_coste(expected_result,simil,1,num_prod);
        String print_error = "Costes diferentes esperado: " + coste_esperado + " , " +
                algorithm.get_Coste_distribucion();

        assertTrue(print_error,equal_cost(coste_esperado,algorithm.get_Coste_distribucion(),
                coste_esperado));

        assertNotNull(algorithm.get_distribucionFinal());
    }
    /**
     * Prueba para verificar la ejecucion del algoritmo aleatorio (AlgorithmRandom).
     * Compara el coste obtenido con el esperado.
     */
    @Test
    public void test_execution_random_algorithm() {
        int num_prod = 50;
        Similitud simil = known_simil_distribution(num_prod);
        ArrayList<Integer> expected_result = expected_simil_distribution(num_prod);
        AlgorithmRandom algorithm = new AlgorithmRandom(1,num_prod,simil.getMatrix());
        algorithm.calcular_distribucion();
        double coste_esperado = get_coste(expected_result,simil,1,num_prod);
        String print_error = "Costes diferentes esperado: " + coste_esperado + " , " +
                algorithm.get_Coste_distribucion();

        assertTrue(print_error,equal_cost(coste_esperado,algorithm.get_Coste_distribucion(),
                coste_esperado));

        assertNotNull(algorithm.get_distribucionFinal());
    }
    /**
     * Prueba para comparar los costes obtenidos entre los algoritmos rapido y lento.
     * Verifica que las diferencias en coste sean aceptables.
     */
    @Test
    public void test_compare_fast_slow_algorithm() {
        int num_filas = 4;
        int num_columnas = 3;

        Similitud simil = random_simil_distribution(num_filas,num_columnas);
        AlgorithmFast algorithm1 = new AlgorithmFast(num_filas,num_columnas,simil.getMatrix());
        AlgorithmSlow algorithm2 = new AlgorithmSlow(num_filas,num_columnas,simil.getMatrix());

        algorithm1.calcular_distribucion();
        algorithm2.calcular_distribucion();

        assertTrue("Costes demasiado distintos entre los algoritmos",
                equal_cost(algorithm1.get_Coste_distribucion(),
                algorithm2.get_Coste_distribucion(),
                algorithm2.get_Coste_distribucion()*0.1));
    }
    /**
     * Prueba unitaria que compara los resultados de AlgorithmRandom yAlgorithmSlow.
     * Verifica que los costes generados sean equivalentes dentro de un margen del 10%.
     */
    @Test
    public void test_compare_random_slow_algorithm() {
        int num_filas = 4;
        int num_columnas = 3;

        Similitud simil = random_simil_distribution(num_filas,num_columnas);
        AlgorithmRandom algorithm1 = new AlgorithmRandom(num_filas,num_columnas,simil.getMatrix());
        AlgorithmSlow algorithm2 = new AlgorithmSlow(num_filas,num_columnas,simil.getMatrix());

        algorithm1.calcular_distribucion();
        algorithm2.calcular_distribucion();

        assertTrue("Costes demasiado distintos entre los algoritmos",
                equal_cost(algorithm1.get_Coste_distribucion(),
                        algorithm2.get_Coste_distribucion(),
                        algorithm2.get_Coste_distribucion()*0.1));
    }
    /**
     * Prueba unitaria que compara los resultados de AlgorithmRandom y AlgorithmFast.
     * Verifica que los costes generados sean equivalentes dentro de un margen del 10%.
     */
    @Test
    public void test_compare_random_fast_algorithm() {
        int num_filas = 4;
        int num_columnas = 3;

        Similitud simil = random_simil_distribution(num_filas,num_columnas);
        AlgorithmRandom algorithm1 = new AlgorithmRandom(num_filas,num_columnas,simil.getMatrix());
        AlgorithmFast algorithm2 = new AlgorithmFast(num_filas,num_columnas,simil.getMatrix());

        algorithm1.calcular_distribucion();
        algorithm2.calcular_distribucion();

        assertTrue("Costes demasiado distintos entre los algoritmos",
                equal_cost(algorithm1.get_Coste_distribucion(),
                        algorithm2.get_Coste_distribucion(),
                        algorithm2.get_Coste_distribucion()*0.1));
    }

    /**
     * Genera perfiles de ejecucion comparando los algoritmos AlgorithmRandom, AlgorithmFast y AlgorithmSlow.
     * Los resultados se escriben en un archivo CSV.
     */
    public void profile_comparation_all_algorithm() {
        int max_filas = 4;
        int max_columnas = 3;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/comparation_all_algorithm.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Rapido;" +
                            "Num iteraciones Random;" +
                            "Num iteraciones Lento;" +
                            "Tiempo(ns) Rapido;" +
                            "Tiempo(ns) Random;" +
                            "Tiempo(ns) Lento;" +
                            "Coste Rapido;" +
                            "Coste Random;" +
                            "Coste Lento;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Rapido;" +
                            "Num iteraciones Random;" +
                            "Num iteraciones Lento;" +
                            "Tiempo(ns) Rapido;" +
                            "Tiempo(ns) Random;" +
                            "Tiempo(ns) Lento;" +
                            "Coste Rapido;" +
                            "Coste Random;" +
                            "Coste Lento;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmSlow algoS = new AlgorithmSlow(i, j, simil.getMatrix());
                    algoS.calcular_distribucion();
                    AlgorithmFast algoF = new AlgorithmFast(i, j, simil.getMatrix());
                    algoF.calcular_distribucion();
                    AlgorithmRandom algoR = new AlgorithmRandom(i, j, simil.getMatrix());
                    algoR.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoR.get_numberIterations() + ";" +
                                    algoS.get_numberIterations() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    algoR.getTiempoTranscurrido().toNanos() + ";" +
                                    algoS.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";" +
                                    (float) algoR.get_Coste_distribucion() + ";" +
                                    (float) algoS.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoR.get_numberIterations() + ";" +
                                    algoS.get_numberIterations() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    algoR.getTiempoTranscurrido().toNanos() + ";" +
                                    algoS.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";" +
                                    (float) algoR.get_Coste_distribucion() + ";" +
                                    (float) algoS.get_Coste_distribucion() + ";");
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Genera perfiles de ejecucion comparando los algoritmos AlgorithmFast y AlgorithmSlow.
     * Los resultados se escriben en un archivo CSV.
     */
    public void profile_comparation_fast_slow() {
        int max_filas = 4;
        int max_columnas = 3;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/profile_comparation_fast_slow.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Rapido;" +
                            "Num iteraciones Lento;" +
                            "Tiempo(ns) Rapido;" +
                            "Tiempo(ns) Lento;" +
                            "Coste Rapido;" +
                            "Coste Lento;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Rapido;" +
                            "Num iteraciones Lento;" +
                            "Tiempo(ns) Rapido;" +
                            "Tiempo(ns) Lento;" +
                            "Coste Rapido;" +
                            "Coste Lento;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmSlow algoS = new AlgorithmSlow(i, j, simil.getMatrix());
                    algoS.calcular_distribucion();
                    AlgorithmFast algoF = new AlgorithmFast(i, j, simil.getMatrix());
                    algoF.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoS.get_numberIterations() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    algoS.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";" +
                                    (float) algoS.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoS.get_numberIterations() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    algoS.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";" +
                                    (float) algoS.get_Coste_distribucion() + ";");
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }


    /**
     * Genera perfiles de ejecucion comparando los algoritmos AlgorithmRandom y AlgorithmSlow.
     * Los resultados se escriben en un archivo CSV.
     */
    public void profile_comparation_random_slow() {
        int max_filas = 4;
        int max_columnas = 3;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/profile_comparation_random_slow.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Random;" +
                            "Num iteraciones Lento;" +
                            "Tiempo(ns) Random;" +
                            "Tiempo(ns) Lento;" +
                            "Coste Random;" +
                            "Coste Lento;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Random;" +
                            "Num iteraciones Lento;" +
                            "Tiempo(ns) Random;" +
                            "Tiempo(ns) Lento;" +
                            "Coste Random;" +
                            "Coste Lento;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmSlow algoS = new AlgorithmSlow(i, j, simil.getMatrix());
                    algoS.calcular_distribucion();
                    AlgorithmRandom algoF = new AlgorithmRandom(i, j, simil.getMatrix());
                    algoF.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoS.get_numberIterations() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    algoS.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";" +
                                    (float) algoS.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoS.get_numberIterations() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    algoS.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";" +
                                    (float) algoS.get_Coste_distribucion() + ";");
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Genera perfiles de ejecucion comparando los algoritmos AlgorithmRandom y AlgorithmFast.
     * Los resultados se escriben en un archivo CSV.
     */
    public void profile_comparation_random_fast() {
        int max_filas = 10;
        int max_columnas = 10;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/profile_comparation_random_fast.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Random;" +
                            "Num iteraciones Fast;" +
                            "Tiempo(ns) Random;" +
                            "Tiempo(ns) Fast;" +
                            "Coste Random;" +
                            "Coste Fast;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones Random;" +
                            "Num iteraciones Fast;" +
                            "Tiempo(ns) Random;" +
                            "Tiempo(ns) Fast;" +
                            "Coste Random;" +
                            "Coste Fast;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmFast algoF = new AlgorithmFast(i, j, simil.getMatrix());
                    algoF.calcular_distribucion();
                    AlgorithmRandom algoR = new AlgorithmRandom(i, j, simil.getMatrix());
                    algoR.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoR.get_numberIterations() + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoR.getTiempoTranscurrido().toNanos() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoR.get_Coste_distribucion() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algoR.get_numberIterations() + ";" +
                                    algoF.get_numberIterations() + ";" +
                                    algoR.getTiempoTranscurrido().toNanos() + ";" +
                                    algoF.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algoR.get_Coste_distribucion() + ";" +
                                    (float) algoF.get_Coste_distribucion() + ";");
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }


    /**
     * Genera un perfil de ejecucion para la implementacion lenta del algoritmo.
     * Los resultados se almacenan en un archivo CSV.
     */
    public void profile_execution_slow() {
        int max_filas = 4;
        int max_columnas = 3;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/execution_slow.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmSlow algo = new AlgorithmSlow(i, j, simil.getMatrix());
                    algo.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algo.get_numberIterations() + ";" +
                                    algo.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algo.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algo.get_numberIterations() + ";" +
                                    algo.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algo.get_Coste_distribucion() + ";"
                    );
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Genera un perfil de ejecucion para la implementacion rapida del algoritmo.
     * Los resultados se almacenan en un archivo CSV.
     */
    public void profile_execution_fast() {
        int max_filas = 8;
        int max_columnas = 8;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/execution_fast.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmFast algo = new AlgorithmFast(i, j, simil.getMatrix());
                    algo.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algo.get_numberIterations() + ";" +
                                    algo.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algo.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algo.get_numberIterations() + ";" +
                                    algo.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algo.get_Coste_distribucion() + ";"
                    );
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Genera un perfil de ejecucion para la implementacion aleatoria del algoritmo.
     * Los resultados se almacenan en un archivo CSV.
     */
    public void profile_execution_random() {
        int max_filas = 8;
        int max_columnas = 8;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/execution_random.csv"))) {
            System.out.println(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );

            archivo.write(
                    "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );
            archivo.newLine();

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    AlgorithmRandom algo = new AlgorithmRandom(i, j, simil.getMatrix());
                    algo.calcular_distribucion();
                    System.out.println(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algo.get_numberIterations() + ";" +
                                    algo.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algo.get_Coste_distribucion() + ";");

                    archivo.write(
                            i + ";" +
                                    j + ";" +
                                    (i * j) + ";" +
                                    algo.get_numberIterations() + ";" +
                                    algo.getTiempoTranscurrido().toNanos() + ";" +
                                    (float) algo.get_Coste_distribucion() + ";"
                    );
                    archivo.newLine();
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Genera un perfil de ejecucion para la implementacion multi-threaded lenta del algoritmo.
     * Los resultados se almacenan en un archivo CSV.
     */
    public void profile_execution_multithread_slow() {
        int max_filas = 4;
        int max_columnas = 3;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/execution_multithread_slow.csv"))) {
            System.out.println("Num threads;" +
                            "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );
            archivo.write("Num threads;"+
                            "Num fila;" +
                            "Num columna;" +
                            "Num elementos;" +
                            "Num iteraciones;" +
                            "Tiempo(ns);" +
                            "Coste;"
            );
            archivo.newLine();

            int max_num_threads = Math.max(1, Runtime.getRuntime().availableProcessors() - 2);

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    for (int num_threads = 1; num_threads <= max_num_threads; ++num_threads) {
                        AlgorithmSlow algo = new AlgorithmSlow(i, j, simil.getMatrix());
                        algo.num_threads = num_threads;
                        algo.calcular_distribucion();
                        System.out.println(num_threads + ";" +
                                i + ";" +
                                j + ";" +
                                (i * j) + ";" +
                                algo.get_numberIterations() + ";" +
                                algo.getTiempoTranscurrido().toNanos() + ";" +
                                (float) algo.get_Coste_distribucion() + ";");

                        archivo.write(num_threads + ";" +
                                i + ";" +
                                j + ";" +
                                (i * j) + ";" +
                                algo.get_numberIterations() + ";" +
                                algo.getTiempoTranscurrido().toNanos() + ";" +
                                (float) algo.get_Coste_distribucion() + ";"
                        );
                        archivo.newLine();
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }

    /**
     * Genera un perfil de ejecucion para la implementacion multi-threaded rapida del algoritmo.
     * Los resultados se almacenan en un archivo CSV.
     */
    public void profile_execution_multithread_fast() {
        int max_filas = 8;
        int max_columnas = 8;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/execution_multithread_fast.csv"))) {
            System.out.println("Num threads;" +
                    "Num fila;" +
                    "Num columna;" +
                    "Num elementos;" +
                    "Num iteraciones;" +
                    "Tiempo(ns);" +
                    "Coste;"
            );
            archivo.write("Num threads;" +
                    "Num fila;" +
                    "Num columna;" +
                    "Num elementos;" +
                    "Num iteraciones;" +
                    "Tiempo(ns);" +
                    "Coste;"
            );
            archivo.newLine();

            int max_num_threads = Math.max(1, Runtime.getRuntime().availableProcessors() - 2);
            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    for (int num_threads = 1; num_threads <= max_num_threads; ++num_threads) {
                        AlgorithmFast algo = new AlgorithmFast(i, j, simil.getMatrix());
                        algo.num_threads = num_threads;
                        algo.calcular_distribucion();
                        System.out.println(num_threads + ";" +
                                        i + ";" +
                                        j + ";" +
                                        (i * j) + ";" +
                                        algo.get_numberIterations() + ";" +
                                        algo.getTiempoTranscurrido().toNanos() + ";" +
                                        (float) algo.get_Coste_distribucion() + ";");
                        archivo.write( num_threads + ";" +
                                i + ";" +
                                j + ";" +
                                (i * j) + ";" +
                                algo.get_numberIterations() + ";" +
                                algo.getTiempoTranscurrido().toNanos() + ";" +
                                (float) algo.get_Coste_distribucion() + ";"
                        );
                        archivo.newLine();
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Genera un perfil de ejecucion para la implementacion multi-threaded aleatoria del algoritmo.
     * Los resultados se almacenan en un archivo CSV.
     */
    public void profile_execution_multithread_random() {
        int max_filas = 8;
        int max_columnas = 8;

        try (BufferedWriter archivo = Files.newBufferedWriter(
                Paths.get("AlgorithmMesurements/execution_multithread_random.csv"))) {
            System.out.println("Num threads;" +
                    "Num fila;" +
                    "Num columna;" +
                    "Num elementos;" +
                    "Num iteraciones;" +
                    "Tiempo(ns);" +
                    "Coste;"
            );
            archivo.write("Num threads;" +
                    "Num fila;" +
                    "Num columna;" +
                    "Num elementos;" +
                    "Num iteraciones;" +
                    "Tiempo(ns);" +
                    "Coste;"
            );
            archivo.newLine();

            int max_num_threads = Math.max(1, Runtime.getRuntime().availableProcessors() - 2);

            for (int i = 1; i <= max_filas; ++i) {
                for (int j = 1; j <= max_columnas; ++j) {
                    Similitud simil = random_simil_distribution(i, j);
                    for (int num_threads = 1; num_threads <= max_num_threads; ++num_threads) {
                        AlgorithmRandom algo = new AlgorithmRandom(i, j, simil.getMatrix());
                        algo.num_threads = num_threads;
                        algo.calcular_distribucion();
                        System.out.println(num_threads + ";" +
                                i + ";" +
                                j + ";" +
                                (i * j) + ";" +
                                algo.get_numberIterations() + ";" +
                                algo.getTiempoTranscurrido().toNanos() + ";" +
                                (float) algo.get_Coste_distribucion() + ";"
                        );
                        archivo.write( num_threads + ";" +
                                i + ";" +
                                j + ";" +
                                (i * j) + ";" +
                                algo.get_numberIterations() + ";" +
                                algo.getTiempoTranscurrido().toNanos() + ";" +
                                (float) algo.get_Coste_distribucion() + ";"
                        );
                        archivo.newLine();
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error");
        }
    }
}