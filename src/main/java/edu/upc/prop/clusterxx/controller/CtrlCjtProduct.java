package edu.upc.prop.clusterxx.controller;

import edu.upc.prop.clusterxx.CjtProduct;
import edu.upc.prop.clusterxx.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para gestionar el conjunto de productos (CjtProduct).
 * Proporciona métodos para interactuar con los productos, como crearlos,
 * modificarlos y obtener información sobre ellos.
 */
public class CtrlCjtProduct {

    /**
     * Instancia del catálogo de productos (CjtProduct).
     */
    private CjtProduct catalog;

    /**
     * Constructor por defecto. Inicializa un nuevo catálogo de productos.
     */
    public CtrlCjtProduct() {
        catalog = new CjtProduct();

    }

    /**
     * Crea un nuevo producto en el catálogo.
     *
     * @param name        El nombre del producto.
     * @param price       El precio del producto.
     * @param description La descripción del producto.
     */
    public void CtrlCreateProduct(String name, double price, String description) {

        catalog.createProduct(name, price, description);

    }

    /**
     * Cambia el precio de un producto existente.
     *
     * @param name  El nombre del producto.
     * @param price El nuevo precio del producto.
     */
    public void CtrlChangePriceProduct(String name, double price) {

        catalog.getProductByName(name).setPrice(price);
    }

    /**
     * Cambia la descripción de un producto existente.
     *
     * @param name        El nombre del producto.
     * @param description La nueva descripción del producto.
     */
    public void CtrlChangeDescriptionProduct(String name, String description) {

        catalog.getProductByName(name).setDescription(description);
    }

    /**
     * Obtiene una lista con los datos de todos los productos en el catálogo.
     * Cada producto se representa como un array de Strings con su nombre, precio y descripción.
     *
     * @return Una lista de arrays de Strings, donde cada array contiene los datos de un producto.
     */
    public List<String[]> CtrlShowProducts() {
        List<Product> products = catalog.getAllProducts();
        List<String[]> productData = new ArrayList<>();

        for (Product product : products) {
            String[] data = {product.getName(), String.valueOf(product.getPrice()), product.getDescription()};
            productData.add(data);
        }

        return productData;
    }

    /**
     * Obtiene un array con los nombres de todos los productos en el catálogo.
     *
     * @return Un array de Strings con los nombres de todos los productos.
     */
    public String[] CtrlGetAllProductNames() {
        return catalog.getAllProductNames();
    }

    /**
     * Elimina un producto del catálogo según su nombre.
     *
     * @param name El nombre del producto a eliminar.
     */
    public void CtrlRemoveProduct(String name) {
        catalog.removeProduct(name);
    }

    /**
     * Obtiene el número total de productos en el catálogo.
     *
     * @return El número de productos en el catálogo.
     */
    public int CtrlGetNumProducts() {
        return catalog.getNumProducts();
    }

    /**
     * Obtiene el número total de productos en el catálogo.
     *
     * @return El número de productos en el catálogo.
     */
    public CjtProduct CtrlGetCjtProduct() {
        return catalog;
    }

    /**
     * Establece un nuevo catálogo de productos.
     *
     * @param catalog El nuevo catálogo de productos.
     */
    public void CtrlSetCatalog(CjtProduct catalog) {
        this.catalog = catalog;
    }


}
