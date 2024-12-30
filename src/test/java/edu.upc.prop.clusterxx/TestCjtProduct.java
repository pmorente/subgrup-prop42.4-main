package edu.upc.prop.clusterxx;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Clase de pruebas para la clase CjtProduct. Esta clase contiene metodos de prueba que verifican las operaciones
 * de creacion, obtencion, actualizacion y eliminacion de productos en el catalogo.
 */
public class TestCjtProduct {

    private CjtProduct catalog;
    /**
     * Metodo de inicializacion que se ejecuta antes de cada prueba.
     * Inicializa un objeto CjtProduct vacio.
     */
    @Before
    public void setUp() {
        catalog = new CjtProduct();
    }
    /**
     * Prueba que verifica si un producto puede ser creado correctamente con un nombre, precio y descripcion.
     */
    @Test
    public void createProductWithDescriptionWorksCorrectly() {
        Product product = catalog.createProduct("Laptop", 1000.0, "High-end laptop");
        assertNotNull(product);
        assertEquals("Laptop", product.getName());
        assertEquals(1000.0, product.getPrice(), 0.0);
        assertEquals("High-end laptop", product.getDescription());
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta crear un producto con un nombre
     * que ya existe en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void creatingProductThatAlreadyExistsThrowsException() {
        catalog.createProduct("Laptop", 1000.0, "High-end laptop");
        catalog.createProduct("Laptop", 1200.0, "Updated laptop");
    }
    /**
     * Prueba que verifica si un producto puede ser obtenido correctamente por su nombre.
     */
    @Test
    public void getProductByNameReturnsCorrectProduct() {
        Product product = catalog.createProduct("Tablet", 300.0, "Android tablet");
        Product foundProduct = catalog.getProductByName("Tablet");
        assertEquals(product, foundProduct);
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta obtener un producto que no existe
     * en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getProductByNameThrowsExceptionForNonExistentProduct() {
        catalog.getProductByName("NonExistent");
    }
    /**
     * Prueba que verifica si el precio de un producto se obtiene correctamente por su nombre.
     */
    @Test
    public void getPriceProductByNameReturnsCorrectPrice() {
        
        catalog.createProduct("Laptop", 1000.0, "High-end laptop");
        Double price = catalog.getPriceProductByName("Laptop");
        assertEquals(1000.0, price, 0.0);
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta obtener el precio de un producto
     * que no existe en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getPriceProductByNameThrowsExceptionForNonExistentProduct() {

        catalog.getPriceProductByName("NonExistentProduct");
    }
    /**
     * Prueba que verifica si la descripcion de un producto se obtiene correctamente por su nombre.
     */
    @Test
    public void getDescriptionProductByNameReturnsCorrectDescription() {

        catalog.createProduct("Phone", 500.0, "Latest smartphone");
        String description = catalog.getDescriptionProductByName("Phone");
        assertEquals("Latest smartphone", description);
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta obtener la descripcion de un
     * producto que no existe en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDescriptionProductByNameThrowsExceptionForNonExistentProduct() {

        catalog.getDescriptionProductByName("NonExistentProduct");
    }
    /**
     * Prueba que verifica si un producto puede ser eliminado correctamente del catalogo.
     */
    @Test
    public void removeProductDeletesProductSuccessfully() {
        catalog.createProduct("Smartwatch", 200.0, "Wearable device");
        assertTrue(catalog.hasProduct("Smartwatch"));
        catalog.removeProduct("Smartwatch");
        assertFalse(catalog.hasProduct("Smartwatch"));
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta eliminar un producto que no existe
     * en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeNonExistentProductThrowsException() {
        catalog.removeProduct("NonExistent");
    }
    /**
     * Prueba que verifica si el precio de un producto puede ser actualizado correctamente.
     */
    @Test
    public void updateProductPriceSuccessfullyChangesPrice() {
        Product product = catalog.createProduct("Headphones", 150.0, "Wireless headphones");
        catalog.updatePriceProduct("Headphones", 180.0);
        assertEquals(180.0, product.getPrice(), 0.0);
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta actualizar el precio de un producto
     * que no existe en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updatePriceForNonExistentProductThrowsException() {
        catalog.updatePriceProduct("NonExistent", 200.0);
    }
    /**
     * Prueba que verifica si la descripcion de un producto puede ser actualizada correctamente.
     */
    @Test
    public void updateProductDescriptionSuccessfullyChangesDescription() {
        Product product = catalog.createProduct("Camera", 800.0, "Digital camera");
        catalog.updateDescriptionProduct("Camera", "Updated digital camera");
        assertEquals("Updated digital camera", product.getDescription());
    }
    /**
     * Prueba que verifica si se lanza una excepcion cuando se intenta actualizar la descripcion de un
     * producto que no existe en el catalogo.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateDescriptionForNonExistentProductThrowsException() {
        catalog.updateDescriptionProduct("NonExistent", "New description");
    }
    /**
     * Prueba que verifica si el metodo hasProduct devuelve true para un producto que existe en el catalogo.
     */
    @Test
    public void hasProductReturnsTrueForExistingProduct() {
        catalog.createProduct("Monitor", 300.0, "4K monitor");
        assertTrue(catalog.hasProduct("Monitor"));
    }
    /**
     * Prueba que verifica si el metodo hasProduct devuelve false para un producto que no existe en el catalogo.
     */
    @Test
    public void hasProductReturnsFalseForNonExistentProduct() {
        assertFalse(catalog.hasProduct("Keyboard"));
    }
    /**
     * Prueba que verifica si el metodo getAllProducts devuelve una lista vacia cuando no hay productos en el catalogo.
     */
    @Test
    public void testGetAllProductsReturnsEmptyList() {
        // Test case 1: When there are no products, it should return an empty list
        List<Product> products = catalog.getAllProducts();
        assertTrue("Product list should be empty", products.isEmpty());
    }
    /**
     * Prueba que verifica si el metodo getAllProducts devuelve la lista correcta de productos cuando hay productos en el catalogo.
     */
    @Test
    public void testGetAllProductsReturnsCorrectProducts() {
        // Add products to the catalog
        Product product1 = catalog.createProduct("Manzana", 2.0, "Mejores manzanas del mercado");
        Product product2 = catalog.createProduct("Pera", 3.0, "Pera del norte");

        List<Product> products = catalog.getAllProducts();

        // Verify the list contains the correct products
        assertEquals("There should be 2 products in the list", 2, products.size());
        assertTrue("Product1 should be in the list", products.contains(product1));
        assertTrue("Product2 should be in the list", products.contains(product2));
    }


}
