package edu.upc.prop.clusterxx;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Clase de pruebas unitarias para la clase {@link Product}.
 * Verifica el correcto funcionamiento de sus metodos y comportamientos clave.
 */
public class TestProduct {
    /**
     * Prueba que el constructor con descripción inicialice correctamente
     * los atributos {@code name}, {@code price} y {@code description}.
     */
    @Test
    public void constructorWithDescriptionWorksCorrectly() {
        Product product = new Product("Laptop", 1000.0, "High-end laptop");
        assertEquals("Laptop", product.getName());
        assertEquals(1000.0, product.getPrice(), 0.0);
        assertEquals("High-end laptop", product.getDescription());
    }
    /**
     * Prueba que el metodo {@code setPrice} actualice correctamente el precio del producto.
     */
    @Test
    public void setPriceUpdatesPriceSuccessfully() {
        Product product = new Product("Tablet", 300.0, "High-end tablet");
        product.setPrice(400.0);
        assertEquals(400.0, product.getPrice(), 0.0);
    }
    /**
     * Prueba que el metodo {@code setPrice} lance una excepcion {@link IllegalArgumentException}
     * cuando se intenta establecer un valor negativo como precio.
     *
     * @throws IllegalArgumentException cuando el precio es negativo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setPriceThrowsExceptionForNegativeValue() {
        Product product = new Product("Tablet", 300.0, "High-end tablet");
        product.setPrice(-100.0);
    }
    /**
     * Prueba que el metodo {@code setDescription} actualice correctamente la descripcion del producto.
     */
    @Test
    public void setDescriptionUpdatesDescriptionSuccessfully() {
        Product product = new Product("Camera", 800.0, "Digital camera");
        product.setDescription("Updated digital camera");
        assertEquals("Updated digital camera", product.getDescription());
    }
    /**
     * Prueba que el metodo {@code toString} devuelva una representacion correcta del producto
     * en el formato {@code name,price,description}.
     */
    @Test
    public void toStringReturnsCorrectRepresentation() {
        Product product = new Product("Manzana", 1.0, "Mejor manzana del mercado");
        String expected = "Manzana,1.0,Mejor manzana del mercado";
        assertEquals(expected, product.toString());
    }
}

