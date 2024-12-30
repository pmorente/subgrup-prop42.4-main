
package edu.upc.prop.clusterxx;
/**
 * Clase que representa un producto con un nombre, precio y descripción.
 * Ofrece métodos para obtener y modificar sus propiedades.
 * 
 * @author Pau Morente (pau.morente@estudiantat.upc.edu)
 */
public class Product {

    /**
     * Nombre del producto.
     */
    private String name;

    /**
     * Precio del producto.
     */
    private double price;

    /**
     * Descripción del producto.
     */
    private String description;

    /**
     * Constructor del producto con parámetros iniciales.
     * 
     * @param name Nombre del producto.
     * @param price Precio del producto, debe ser mayor o igual a cero.
     * @param description Descripción del producto.
     * @throws IllegalArgumentException Si el precio es menor a cero.
     */
    Product(String name, double price, String description) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be less than zero.");
        }
        this.name = name;
        this.price = price;
        this.description = description;
    }

    /**
     * Obtiene el nombre del producto.
     * 
     * @return Nombre del producto.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el precio del producto.
     * 
     * @return Precio del producto.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Establece un nuevo precio para el producto.
     * 
     * @param price Nuevo precio, debe ser mayor o igual a cero.
     * @throws IllegalArgumentException Si el precio es menor a cero.
     */
    public void setPrice(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be less than zero.");
        }
        this.price = price;
    }

    /**
     * Obtiene la descripción del producto.
     * 
     * @return Descripción del producto.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece una nueva descripción para el producto.
     * 
     * @param description Nueva descripción del producto.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Devuelve el producto en formato string.
     * 
     * @return String con el nombre, precio y descripción del producto separados por comas.
     */
    @Override
    public String toString() {
        return name + "," + price + "," + description;
    }
}
