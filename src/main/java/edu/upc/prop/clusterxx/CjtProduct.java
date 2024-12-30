package edu.upc.prop.clusterxx;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Clase para gestionar un conjunto de productos. Proporciona métodos para crear, obtener, actualizar
 * y eliminar productos del catálogo.
 *
 */
public class CjtProduct {
    /**
     * Mapa que almacena los productos con su nombre como clave
     *
     */
    private Map<String, Product> productMap = new HashMap<>();

    /**
     * Crea un nuevo producto y lo añade al catálogo.
     *
     * @param name El nombre del producto.
     * @param price El precio del producto.
     * @param description La descripción del producto.
     * @return El producto creado.
     * @throws IllegalArgumentException si el nombre ya existe o si el catálogo tiene más de 1000 productos.
     */
    public Product createProduct(String name, double price, String description) {
        if (productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto ya existe: " + name);
        }
        if (productMap.size() == 1000) {
            throw new IllegalArgumentException("El número máximo de productos es 1000.");
        }
        Product newProduct = new Product(name, price, description);
        productMap.put(name, newProduct);
        return newProduct;
    }

    /**
     * Obtiene un producto por su nombre.
     *
     * @param name El nombre del producto.
     * @return El producto correspondiente al nombre dado.
     * @throws IllegalArgumentException si no existe un producto con ese nombre.
     */
    public Product getProductByName(String name) {
        if (!productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto con el nombre " + name + " no existe.");
        }
        return productMap.get(name);
    }

    /**
     * Obtiene el precio de un producto por su nombre.
     *
     * @param name El nombre del producto.
     * @return El precio del producto.
     * @throws IllegalArgumentException si no existe un producto con ese nombre.
     */
    public Double getPriceProductByName(String name) {
        if (!productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto con el nombre " + name + " no existe.");
        }
        return productMap.get(name).getPrice();
    }

    /**
     * Obtiene la descripción de un producto por su nombre.
     *
     * @param name El nombre del producto.
     * @return La descripción del producto.
     * @throws IllegalArgumentException si no existe un producto con ese nombre.
     */
    public String getDescriptionProductByName(String name) {
        if (!productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto con el nombre " + name + " no existe.");
        }
        return productMap.get(name).getDescription();
    }

    /**
     * Elimina un producto del catálogo por su nombre.
     *
     * @param name El nombre del producto a eliminar.
     * @throws IllegalArgumentException si no existe un producto con ese nombre.
     */
    public void removeProduct(String name) {
        if (!productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto con el nombre " + name + " no existe.");
        }
        productMap.remove(name);
    }

    /**
     * Actualiza el precio de un producto por su nombre.
     *
     * @param name El nombre del producto.
     * @param price El nuevo precio del producto.
     * @throws IllegalArgumentException si no existe un producto con ese nombre.
     */
    public void updatePriceProduct(String name, double price) {
        if (!productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto con el nombre " + name + " no existe.");
        }
        Product existingProduct = productMap.get(name);
        existingProduct.setPrice(price);
    }

    /**
     * Actualiza la descripción de un producto por su nombre.
     *
     * @param name El nombre del producto.
     * @param description La nueva descripción del producto.
     * @throws IllegalArgumentException si no existe un producto con ese nombre.
     */
    public void updateDescriptionProduct(String name, String description) {
        if (!productMap.containsKey(name)) {
            throw new IllegalArgumentException("El producto con el nombre " + name + " no existe.");
        }
        Product existingProduct = productMap.get(name);
        existingProduct.setDescription(description);
    }

    /**
     * Verifica si existe un producto con el nombre dado.
     *
     * @param name El nombre del producto.
     * @return true si existe un producto con el nombre dado, false en caso contrario.
     */
    public boolean hasProduct(String name) {
        return productMap.containsKey(name);
    }

    /**
     * Devuelve todos los productos almacenados en el catálogo.
     *
     * @return Una lista de productos.
     */
    public List<Product> getAllProducts() {
        return List.copyOf(productMap.values());
    }

    /**
     * Devuelve el número de productos almacenados en el catálogo.
     *
     * @return El número de productos.
     */
    public int getNumProducts() {
        return productMap.size();
    }

    /**
     * Devuelve una lista con los nombres de todos los productos almacenados en el catálogo.
     *
     * @return Una lista de cadenas de texto (Strings) que contiene los nombres de todos los productos.
     * @throws IllegalStateException si no hay ningún producto en el catálogo.
     */
    public String[] getAllProductNames() {
        if (productMap.isEmpty()) {
            throw new IllegalStateException("No hay productos en el catálogo.");
        }
        return productMap.keySet().toArray(new String[0]);
    }
}

