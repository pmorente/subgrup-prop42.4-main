package edu.upc.prop.clusterxx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase que maneja las similitudes entre productos, gestionando una matriz de similitudes.
 * Permite crear, consultar, eliminar y gestionar productos, así como sus relaciones de similitud.
 */
public class Similitud {

    /**
     * Mapa de traducción entre el nombre del producto y el id de la columna de la matriz
     */
    private Map<String, Integer> Trad = new HashMap<>();

    /**
     * Mapa de traducción inversa entre el id de la columna de la matriz y el nombre del producto
     */
    private Map<Integer, String> InTrad = new HashMap<>();

    /**
     * Matriz de similitudes entre productos
     */
    private float[][] Matrix = new float[1000][1000];

    /**
     * Constructor de la clase Similitud. Inicializa la matriz con valores inválidos (-1.0).
     */
    public Similitud() {
        for (int i = 0; i < 1000; ++i)
            for (int j = 0; j < 1000; ++j)
                Matrix[i][j] = (float) -1.0;
    }

    /**
     * Crea una similitud entre dos productos y la guarda en la matriz de similitudes.
     * @param product1 El primer producto
     * @param product2 El segundo producto
     * @param coef El coeficiente de similitud entre los productos (debe estar entre 0 y 1, excluidos)
     * @throws IllegalArgumentException Si alguno de los productos no existe o el coeficiente no es válido
     */
    public void createSimilitud(String product1, String product2, float coef) {

        if (!Trad.containsKey(product1)) {
            throw new IllegalArgumentException("Product1 doesn't exists: " + product1);
        }
        if (!Trad.containsKey(product2)) {
            throw new IllegalArgumentException("Product2 doesn't exists: " + product2);
        }
        if((coef >= 1.0f) || (coef < 0.0f)) {
            throw new IllegalArgumentException("The coeficient isn't valid, it should be a value between 0 and 1(not included), and instead it's: " + coef);
        }
        Matrix[Trad.get(product1)][Trad.get(product2)] = coef;
        Matrix[Trad.get(product2)][Trad.get(product1)] = coef;
    }

    /**
     * Consulta la similitud entre dos productos, identificados por sus nombres.
     * @param product1 El primer producto
     * @param product2 El segundo producto
     * @return El coeficiente de similitud entre los productos
     * @throws IllegalArgumentException Si alguno de los productos no existe o no hay relación entre ellos
     */
    public float consultarSimilitud(String product1, String product2) {
        if (!Trad.containsKey(product1)) {
            throw new IllegalArgumentException("Product1 doesn't exists: " + product1);
        }
        if (!Trad.containsKey(product2)) {
            throw new IllegalArgumentException("Product2 doesn't exists: " + product2);
        }
        float coef = Matrix[Trad.get(product1)][Trad.get(product2)];
        if (coef == (float) -1.0) throw new IllegalArgumentException("Relation between products doesn't exists: " + product1 + " " + product2);
        else return coef;
    }

    /**
     * Consulta la similitud entre dos productos, identificados por sus ids.
     * @param product1 El id del primer producto
     * @param product2 El id del segundo producto
     * @return El coeficiente de similitud entre los productos
     * @throws IllegalArgumentException Si los ids son inválidos o no existe relación entre los productos
     */
    public float consultarSimilitud(int product1, int product2) {
        if (product1 >= Trad.size()) {
            throw new IllegalArgumentException("Tried to get product with id "+ product1 +" but max id is " + (Trad.size()-1));
        }
        if (product2 >= Trad.size()) {
            throw new IllegalArgumentException("Tried to get product with id "+ product2 +" but max id is " + (Trad.size()-1));
        }
        float coef = Matrix[product1][product2];
        if (coef == (float) -1.0) throw new IllegalArgumentException("Relation between products doesn't exists: " + InTrad.get(product1) + " " + InTrad.get(product2));
        else return coef;
    }

    /**
     * Elimina la similitud entre dos productos, identificados por sus nombres.
     * @param product1 El primer producto
     * @param product2 El segundo producto
     * @throws IllegalArgumentException Si alguno de los productos no existe
     */
    public void eliminarSimilitud(String product1, String product2) {
        if(product1.equals(product2)) throw new IllegalArgumentException("The products are the same: " + product1);
        if (!Trad.containsKey(product1)) {
            throw new IllegalArgumentException("Product1 doesn't exists: " + product1);
        }
        if (!Trad.containsKey(product2)) {
            throw new IllegalArgumentException("Product2 doesn't exists: " + product2);
        }
        Matrix[Trad.get(product1)][Trad.get(product2)] = (float) -1.0;
        Matrix[Trad.get(product2)][Trad.get(product1)] = (float) -1.0;
    }

    /**
     * Realiza un chequeo de salud sobre la matriz de similitudes, comprobando si hay relaciones pendientes por agregar.
     * @throws NumberFormatException Si hay productos sin similitud registrada
     */
    public void healthCheck() {
        String Error = "";
        for (int i = 0; i < Trad.size(); ++i) {
            for (int j = i+1; j < Trad.size(); ++j) {
                if(Matrix[i][j] == (float)-1.0) Error += "The following two products still do not have Similarity: " + InTrad.get(i) + " " + InTrad.get(j) + "\n";
            }
        }
        if(!Error.equals("")) throw new NumberFormatException(Error);
    }

    /**
     * Crea un nuevo producto, agregándolo a la matriz de similitudes.
     * @param name El nombre del nuevo producto
     * @throws IllegalArgumentException Si el producto ya existe
     */
    public void createProduct( String name) {
        if(Trad.containsKey(name)) throw new IllegalArgumentException("Product already exists: " + name);
        Trad.put(name, Trad.size());
        InTrad.put(InTrad.size(), name);
    }

    /**
     * Elimina un producto y sus traducciones, borrando sus relaciones de similitud.
     * @param name El nombre del producto a eliminar
     * @throws IllegalArgumentException Si el producto no existe
     */
    public void removeProduct(String name) {
        if(Trad.get(name) == null) throw new IllegalArgumentException("Product with name "+name+" does not exist.");
        int id = Trad.get(name);
        if(id == Trad.size()-1){
            Trad.remove(name);
            InTrad.remove(id);
            for(int j = 0; j < id; ++j) Matrix[id][j] = (float)-1.0;
            for(int i = 0; i < id; ++i) Matrix[i][id] = (float)-1.0;
        }
        else{
            Trad.remove(name);
            InTrad.remove(id);
            int n = Trad.size();
            String ultimo = InTrad.get(n);
            InTrad.remove(n);
            Trad.remove(ultimo);
            for(int j = 0; j < n; ++j) {
                Matrix[id][j] = Matrix[n][j];
                Matrix[n][j] = (float)-1.0;
            }
            for(int i = 0; i < n; ++i) {
                Matrix[i][id] = Matrix[i][n];
                Matrix[i][n] = (float)-1.0;
            }
            Matrix[id][id] = (float)-1.0;
            Trad.put(ultimo, id);
            InTrad.put(id, ultimo);
        }
    }

    /**
     * Obtiene el número de productos registrados.
     * @return El número total de productos
     */
    public int numProds() {
        return Trad.size();
    }

    /**
     * Traduce un id a su nombre correspondiente.
     * @param id El id del producto
     * @return El nombre del producto
     * @throws IllegalArgumentException Si el id no es válido
     */
    public String traducir(int id){
        if(id >= Trad.size()) throw new IllegalArgumentException("Tried to get product with id "+ id +" but max id is " + (Trad.size()-1));
        return InTrad.get(id);
    }

    /**
     * Traduce un nombre de producto a su id correspondiente.
     * @param nombre El nombre del producto
     * @return El id del producto
     * @throws IllegalArgumentException Si el producto no existe
     */
    public int traducir_id(String nombre){
        if(!Trad.containsKey(nombre)) throw new IllegalArgumentException("El producto que intentas traducir no existe");
        int id = Trad.get(nombre);
        return id;
    }

    /**
     * Representa la matriz de similitudes en formato de cadena.
     * @return Una cadena con la representación de la matriz de similitudes
     */
    public String toString(){
        String result = "";
        String Prod1 = "";
        String Prod2 = "";
        for(int i = 0; i < Trad.size(); ++i){
            Prod1 = InTrad.get(i);
            for(int j = 0; j < Trad.size(); ++j){
                Prod2 = InTrad.get(j);
                result += "(" + Prod1 + "," + Prod2 + "," + Matrix[i][j] + ")";
            }
        }
        return result;
    }

    /**
     * Obtiene la matriz de similitudes.
     * @return La matriz de similitudes
     */
    public float[][] getMatrix() {
        return Matrix;
    }

    /**
     * Obtiene una lista de las similitudes registradas.
     * @return Una lista de similitudes en formato de cadena
     */
    public List<String> getSimilaritiesList() {
        List<String> similarities = new ArrayList<>();
        for (int i = 0; i < Trad.size(); ++i) {
            String prod1 = InTrad.get(i);
            for (int j = i + 1; j < Trad.size(); ++j) {
                String prod2 = InTrad.get(j);
                float coef = Matrix[i][j];
                if (coef != -1.0) {
                    similarities.add(prod1 + "," + prod2 + "," + coef);
                }
            }
        }
        return similarities;
    }
}
