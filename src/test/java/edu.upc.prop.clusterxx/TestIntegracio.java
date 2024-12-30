package edu.upc.prop.clusterxx;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

import edu.upc.prop.clusterxx.controller.CtrlDomini;


public class TestIntegracio {
    private CtrlDomini cd;
    private static final String TEST_FILE_PATH_CJTPRODUCT = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_cjtProduct.csv").toString();
    private static final String TEST_FILE_PATH_SIMILITUD = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Similitud.csv").toString();
    private static final String TEST_FILE_PATH_SHELVE = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Shelve.csv").toString();
    private static final String TEST_FILE_PATH_ESTADISTICAS = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Estadisticas.csv").toString();
    private static final String TEST_FILE_PATH_SESSION = Paths.get(System.getProperty("user.home"), "Downloads", "test_export_Session.csv").toString();

    @Before
    public void setUp() throws IOException{
        try {
            cd = new CtrlDomini();
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_ESTADISTICAS));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
    }


    /**
     * Este test verifica que se pueda crear un producto o importarlo y todos sus posibles resultados.
     * Objeto de la prueba: Crear Producto
     * Ficheros necesarios: - (test_export_cjtProduct.csv creado en el test)
     * Valores estudiados: 
     *      -Crear un producto con nombre, precio y descripción correctos
     *      -Crear un producto con nombre, precio y descripción incorrectos
     */
    @Test
    public void testCreateProduct() throws IOException {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        assertEquals(1, cd.domainCtrl_ShowProducts().size());
        assertEquals("Manzana", cd.domainCtrl_ShowProducts().get(0)[0]);
        assertEquals(Double.toString(1000.0), cd.domainCtrl_ShowProducts().get(0)[1]);
        assertEquals("Fruta", cd.domainCtrl_ShowProducts().get(0)[2]);

        try {
            cd.domainCtrl_ImportManuallyProducts("Manzana", -1000.0, "Fruta");
            fail("Se esperaba excepción IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El producto ya existe: Manzana", e.getMessage());
        }

        try {
            cd.domainCtrl_ImportManuallyProducts("Pera", -1000.0, "Fruta");
            fail("Se esperaba excepción IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Price cannot be less than zero.", e.getMessage());
        }

        List<String> csvLines = Arrays.asList("Peras,1.0,Peras de la huerta");        
        Files.createFile(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        Files.write(Paths.get(TEST_FILE_PATH_CJTPRODUCT), csvLines);

        try{
            cd.domainCtrl_ImportProducts(TEST_FILE_PATH_CJTPRODUCT);
        } catch(Exception e){
            fail("There should not be any exception");
        }

        assertEquals(2, cd.domainCtrl_ShowProducts().size());
        assertEquals("Peras", cd.domainCtrl_ShowProducts().get(1)[0]);
        assertEquals(Double.toString(1.0), cd.domainCtrl_ShowProducts().get(1)[1]);
        assertEquals("Peras de la huerta", cd.domainCtrl_ShowProducts().get(1)[2]);

        try{
            cd.domainCtrl_ImportProducts(TEST_FILE_PATH_CJTPRODUCT);
        } catch(Exception e){
            assertEquals("El producto ya existe: Peras", e.getMessage());
        }

        for (int i = 0; i < 998; i++) {
            cd.domainCtrl_ImportManuallyProducts("Producto" + i, 1000.0, "Descripción");
        }

        try {
            cd.domainCtrl_ImportManuallyProducts("Producto1000", 1000.0, "Descripción");
            fail("Se esperaba excepción IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Este test verifica que se pueda eliminar un producto y todos sus posibles resultados.
     * Objeto de la prueba: Eliminar Productos
     * Ficheros necesarios: - 
     * Valores estudiados: 
     *      -Eliminar un producto existente
     *      -Eliminar un producto inexistente
     */
    @Test
    public void testDeleteProduct() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");


        cd.domainCtrl_RemoveProduct("Manzana");
        System.out.println(cd.domainCtrl_ShowProducts());
        List<String[]> lista = cd.domainCtrl_ShowProducts();
        assertEquals(1, lista.size());
        assertEquals("Pera", lista.get(0)[0]);
        assertEquals(Double.toString(2000.0), lista.get(0)[1]);
        assertEquals("Fruta", lista.get(0)[2]);

        try {
            cd.domainCtrl_RemoveProduct("Manzana");
            fail("Se esperaba excepción IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El producto con el nombre Manzana no existe.", e.getMessage());
        }


    }

    /**
     * Este test verifica que se pueda cambiar la descripción de un producto y todos sus posibles resultados.
     * Objeto de la prueba: Cambiar Descripción de Producto
     * Ficheros necesarios: - 
     * Valores estudiados: 
     *      -Modificar un producto existente
     *      -Modificar un producto inexistente
     */
    @Test
    public void testModificarProducto() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        cd.domainCtrl_ChangeDescriptionProduct("Manzana", "Fruta de la huerta");
        
        List<String[]> lista = cd.domainCtrl_ShowProducts();
        assertEquals(2, lista.size());
        assertEquals("Manzana", lista.get(1)[0]);
        assertEquals(Double.toString(1000.0), lista.get(1)[1]);
        assertEquals("Fruta de la huerta", lista.get(1)[2]);

        cd.domainCtrl_RemoveProduct("Manzana");
        try {
            cd.domainCtrl_ChangeDescriptionProduct("Manzana", "Fruta de la huerta");
            fail("Se esperaba excepción IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El producto con el nombre Manzana no existe.", e.getMessage());
        }


        cd.domainCtrl_ChangePriceProduct("Pera", 30.0);

        lista = cd.domainCtrl_ShowProducts();
        assertEquals(1, lista.size());
        assertEquals("Pera", lista.get(0)[0]);
        assertEquals(Double.toString(30.0), lista.get(0)[1]);
        assertEquals("Fruta", lista.get(0)[2]);

        try {
            cd.domainCtrl_ChangePriceProduct("Per", 25);
            fail("Se esperaba excepción IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El producto con el nombre Per no existe.", e.getMessage());
        }

    }

    /**
     * Este test verifica que se pueda consultar productos y todos sus posibles resultados.
     * Objeto de la prueba: Consultar Productos
     * Ficheros necesarios: -
     * Valores estudiados:
     *     -Consultar productos existentes
     *     -Consultar productos inexistentes
     */
    @Test
    public void testConsultarProductos() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        List<String[]> lista = cd.domainCtrl_ShowProducts();
        assertEquals(2, lista.size());
        assertEquals("Pera", lista.get(0)[0]);
        assertEquals(Double.toString(2000.0), lista.get(0)[1]);
        assertEquals("Fruta", lista.get(0)[2]);
        assertEquals("Manzana", lista.get(1)[0]);
        assertEquals(Double.toString(1000.0), lista.get(1)[1]);
        assertEquals("Fruta", lista.get(1)[2]);

        try {
            cd.domainCtrl_ShowProducts().get(2);
            fail("Se esperaba excepción IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }


        try{
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        } catch (IOException e){
            fail("No se esperaba excepción");
        }
        try{
            cd.domainCtrl_ExportProducts(TEST_FILE_PATH_CJTPRODUCT);
        } catch (Exception e){
            fail("No se esperaba excepción "+ e.getMessage());
        }
        try{    
            cd = new CtrlDomini();
        } catch (Exception e){
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_ImportProducts(TEST_FILE_PATH_CJTPRODUCT);
        } catch (Exception e) {
        }
        lista = cd.domainCtrl_ShowProducts();
        assertEquals(2, lista.size());
        assertEquals("Pera", lista.get(0)[0]);
        assertEquals(Double.toString(2000.0), lista.get(0)[1]);
        assertEquals("Fruta", lista.get(0)[2]);
        assertEquals("Manzana", lista.get(1)[0]);
        assertEquals(Double.toString(1000.0), lista.get(1)[1]);
        assertEquals("Fruta", lista.get(1)[2]);

    }

    
    //=========================================================================================================
    //=================================== CASOS DE USO DE SIMILITUD ===========================================
    //=========================================================================================================

    /**
     * Este test verifica que se pueda crear la similitud entre dos productos y todos sus posibles resultados.
     * Objeto de la prueba: Cargar Similitud
     * Ficheros necesarios: - (test_export_Similitud.csv creado en el test)
     * Valores estudiados:
     *    -Cargar similitud entre dos productos existentes
     *    -Cargar similitud entre dos productos inexistentes
     *    -Cargar similitud con coeficiente incorrecto
     */
    @Test
    public void testCargarSimilitud() throws IOException {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try{
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
        } catch (Exception e){
            fail("No se esperaba excepción");
        }

        assertEquals("0.5", cd.domainCtrl_ShowSimilituds().get(0)[2]);


        try{
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pe", 0.5f);
            fail("Se esperaba excepción");
        } catch (Exception e){
            assertEquals("Product2 doesn't exists: Pe", e.getMessage());
        }
        try{
            cd.domainCtrl_ImportManuallySimilituds("Pe", "Manzana", 0.5f);
            fail("Se esperaba excepción");
        } catch (Exception e){
            assertEquals("Product1 doesn't exists: Pe", e.getMessage());
        }

        try{
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 1.5f);
            fail("Se esperaba excepción");
        } catch (Exception e){
            assertEquals("The coeficient isn't valid, it should be a value between 0 and 1(not included), and instead it's: 1.5", e.getMessage());
        }

        try{
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", -0.5f);
            fail("Se esperaba excepción");
        } catch (Exception e){
            assertEquals("The coeficient isn't valid, it should be a value between 0 and 1(not included), and instead it's: -0.5", e.getMessage());
        }



        List<String> csvLines = Arrays.asList(                
            "Manzana,Pera,0.8");        
        Files.createFile(Paths.get(TEST_FILE_PATH_SIMILITUD));
        Files.write(Paths.get(TEST_FILE_PATH_SIMILITUD), csvLines);

        try{
            cd.domainCtrl_ImportSimilituds(TEST_FILE_PATH_SIMILITUD);
        } catch(Exception e){
            fail("There should not be any exception");
        }
        assertEquals("0.8", cd.domainCtrl_ShowSimilituds().get(0)[2]);


        csvLines = Arrays.asList(                
            "Manzana,Naranja,0.8");        
        Files.write(Paths.get(TEST_FILE_PATH_SIMILITUD), csvLines);
        try{
            cd.domainCtrl_ImportSimilituds(TEST_FILE_PATH_SIMILITUD);
        } catch(Exception e){
            assertEquals("Product2 doesn't exists: Naranja", e.getMessage());
        }

        csvLines = Arrays.asList(                
            "Manzana,Pera,-0.8");        
        Files.write(Paths.get(TEST_FILE_PATH_SIMILITUD), csvLines);
        try{
            cd.domainCtrl_ImportSimilituds(TEST_FILE_PATH_SIMILITUD);
        } catch(Exception e){
            assertEquals("The coeficient isn't valid, it should be a value between 0 and 1(not included), and instead it's: -0.8", e.getMessage());
        }

    }


    /**
     * Este test verifica que se pueda consultar la similitud entre dos productos y todos sus posibles resultados.
     * Objeto de la prueba: Consultar Similitudes de Productos
     * Ficheros necesarios: - (test_export_Similitud.csv creado en el test)
     * Valores estudiados:
     *   -Consultar similitud entre dos productos existentes
     *   -Consultar similitud entre dos productos inexistentes
     *   -Consultar similitud entre productos sin existir ninguno
     *   -Exportar similitud entre productos existentes
     *   -Exportar similitud sin productos
     */
    @Test
    public void testConsultarSimilitud() {

        assertEquals(0, cd.domainCtrl_ShowSimilituds().size());

        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        assertEquals("0.5", cd.domainCtrl_ShowSimilituds().get(0)[2]);


        try{
            cd = new CtrlDomini();
        }
        catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
        } catch (IOException e) {
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_ExportSimilituds(TEST_FILE_PATH_SIMILITUD);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }


        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
        } catch (IOException e) {
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_ExportSimilituds(TEST_FILE_PATH_SIMILITUD);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        cd.domainCtrl_RemoveSimilitud("Manzana", "Pera");
        try {
            assertEquals(0, cd.domainCtrl_ShowSimilituds().size());
            fail("Se esperaba excepción");
        } catch (Exception e) {
        }

        try {
            cd.domainCtrl_ImportSimilituds(TEST_FILE_PATH_SIMILITUD);
        } catch (Exception e) {
        }
        List<String[]> lista = cd.domainCtrl_ShowSimilituds();
        assertEquals(1, lista.size());
        assertEquals("Manzana", lista.get(0)[0]);
        assertEquals("Pera", lista.get(0)[1]);
        assertEquals("0.5", lista.get(0)[2]);
    }

    /**
     * Este test verifica que se pueda eliminar la similitud entre dos productos y todos sus posibles resultados.
     * Objeto de la prueba: Eliminar Similitud de Productos
     * Ficheros necesarios: - 
     * Valores estudiados:
     *   -Eliminar similitud entre dos productos existentes
     *   -Eliminar similitud entre dos productos inexistentes
     */
    @Test
    public void testEliminarSimilitud() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        assertEquals("0.5", cd.domainCtrl_ShowSimilituds().get(0)[2]);

        cd.domainCtrl_RemoveSimilitud("Manzana", "Pera");
        try {
            assertEquals(0, cd.domainCtrl_ShowSimilituds().size());
            fail("Se esperaba excepción");
        } catch (Exception e) {
        }

        try {
            cd.domainCtrl_RemoveSimilitud("Manzana", "Per");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Product2 doesn't exists: Per", e.getMessage());
        }
        try {
            cd.domainCtrl_RemoveSimilitud("Manza", "Per");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Product1 doesn't exists: Manza", e.getMessage());
        }
    }

    /**
     * Este test verifica que se pueda modificar la similitud entre dos productos y todos sus posibles resultados.
     * Objeto de la prueba: Modificar Similitud de Productos
     * Ficheros necesarios: -
     * Valores estudiados:
     *  -Modificar similitud entre dos productos existentes
     *  -Modificar similitud entre dos productos inexistentes
     *  -Modificar similitud con coeficiente incorrecto
     */
    @Test
    public void testModificarSimilitud() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        assertEquals("0.5", cd.domainCtrl_ShowSimilituds().get(0)[2]);

        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.8f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        assertEquals("0.8", cd.domainCtrl_ShowSimilituds().get(0)[2]);

        try{
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pe", 0.5f);
            fail("Se esperaba excepción");
        } catch (Exception e){
            assertEquals("Product2 doesn't exists: Pe", e.getMessage());
        }
        try{
            cd.domainCtrl_ImportManuallySimilituds("Pe", "Manzana", 0.5f);
            fail("Se esperaba excepción");
        } catch (Exception e){
            assertEquals("Product1 doesn't exists: Pe", e.getMessage());
        }
    }



    //=========================================================================================================
    //==================================== CASOS DE USO DE ESTANTERIA =========================================
    //=========================================================================================================

    /**
     * Este test verifica que se pueda crear una estanteria y todos sus posibles resultados.
     * Objeto de la prueba: Crear Estantería
     * Ficheros necesarios: - 
     * Valores estudiados:
     *  -Crear una estanteria de tamaño correcto
     *  -Crear una estanteria de tamaño incorrecto
     */

     @Test
     public void testCrearEstanteria() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_InitializeShelve(2, 1);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        try {
            cd.domainCtrl_InitializeShelve(0, 1);
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Incorrect number of products, number of products must be 2", e.getMessage());
        }

        try{
            Object[] sh = cd.domainCtrl_ShowShelve();
            assertEquals(2, sh[0]);
            assertEquals(1, sh[1]);
            assertEquals("[Pera, Manzana]", sh[2].toString());
        } catch (Exception e){
            fail("No se esperaba excepción");
        }



        List<String> csvLines = List.of("1,2,Pera;Naranja");
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
            Files.write(Paths.get(TEST_FILE_PATH_SHELVE), csvLines);
        } catch (IOException e) {
            fail("No se esperaba excepción");
        }

        try {
            cd.domainCtrl_ImportShelve(TEST_FILE_PATH_SHELVE);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        try{
            Object[] sh = cd.domainCtrl_ShowShelve();
            assertEquals(1, sh[0]);
            assertEquals(2, sh[1]);
            assertEquals("[Pera, Naranja]", sh[2].toString());
        } catch (Exception e){
            fail("No se esperaba excepción");
        }
     }

    /**
     * Este test verifica que se pueda ver y exportar una estanteria y todos sus posibles resultados.
     * Objeto de la prueba: Ver y Exportar Estantería
     * Ficheros necesarios: - (test_export_Shelve.csv creado en el test)
     * Valores estudiados:
     *  -Ver una estanteria
     *  -Ver una estanteria sin productos
     *  -Exportar una estanteria
     *  -Exportar una estanteria sin productos
     */
     @Test
     public void testVerExportarEstanteria() {
        
        try{
            Object[] sh = cd.domainCtrl_ShowShelve();
            assertEquals(0, sh[0]);
            assertEquals(0, sh[1]);
            assertEquals("[]", sh[2].toString());
        } catch (Exception e){
            assertEquals("Shelf not initialized, please create a shelf first.", e.getMessage());
        }
        try{
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
            cd.domainCtrl_ExportShelve(TEST_FILE_PATH_SHELVE);
        } catch (Exception e){
            assertEquals("Error exporting the Shelf:Shelf not initialized, please create a shelf first.", e.getMessage());
        }
        
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_InitializeShelve(2, 1);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try{            
            Object[] sh = cd.domainCtrl_ShowShelve();
            assertEquals(2, sh[0]);
            assertEquals(1, sh[1]);
            assertEquals("[Pera, Manzana]", sh[2].toString());
            assertNotEquals(0, cd.domainCtrl_GetShelveCost());
        } catch (Exception e){
            fail("No se esperaba excepción");
        }

        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
        } catch (IOException e) {
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_ExportShelve(TEST_FILE_PATH_SHELVE);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
     }


     /**
      * Este test verifica que se pueda modificar una estanteria y todos sus posibles resultados.
      * Objeto de la prueba: Modificar Estantería
      * Ficheros necesarios: -
      * Valores estudidados:
      *  -Modificar una estanteria con productos correctos
      *  -Modificar una estanteria con productos incorrectos
      *  -Añadir un producto a una estanteria
      *  -Añadir un producto a una estanteria sin espacios
      *  -Eliminar un producto de una estanteria
      *  -Eliminar un producto de una estanteria con productos incorrectos
      */
    @Test
    public void testModificarEstanteria() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");

        try {
            cd.domainCtrl_InitializeShelve(2, 1);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try{            
            Object[] sh = cd.domainCtrl_ShowShelve();
            assertEquals(2, sh[0]);
            assertEquals(1, sh[1]);
            assertEquals("[Pera, Manzana]", sh[2].toString());
            assertNotEquals(0, cd.domainCtrl_GetShelveCost());
        } catch (Exception e){
            fail("No se esperaba excepción");
        }


        try {
            cd.domainCtrl_ModifyManuallyShelve("M", "P");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("The products M and P don't exist in the shelve", e.getMessage());
        }

        try {
            cd.domainCtrl_ModifyManuallyShelve("Pera", "Manzana");
        } catch (Exception e) {
            fail("No se esperaba excepción "+ e.getMessage());
        }
        try{            
            Object[] sh = cd.domainCtrl_ShowShelve();
            assertEquals(2, sh[0]);
            assertEquals(1, sh[1]);
            assertEquals("[Manzana, Pera]", sh[2].toString());
            assertNotEquals(0, cd.domainCtrl_GetShelveCost());
        } catch (Exception e){
            fail("No se esperaba excepción");
        }


        try {
            cd.domainCtrl_DeleteProductShelve("Naranja");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("the product Naranja doesn't exist in the shelve", e.getMessage());
        }

        try {
            cd.domainCtrl_DeleteProductShelve("Manzana");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }


        try {
            cd.domainCtrl_AddProductShelve("Naranja");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_AddProductShelve("Naranja");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("The product Naranja already exists in the shelf", e.getMessage());
        }

        try {
            cd.domainCtrl_AddProductShelve("Manzana");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("No empty slot available to add the product Manzana", e.getMessage());
        }
    }

    /**
     * Este test verifica que se pueda calcular la distribución de productos en una estanteria y todos sus posibles resultados.
     * Objeto de la prueba: Calcular Distribución de Productos
     * Ficheros necesarios: -
     * Valores estudiados:
     *  -Calcular la distribución de productos en una estanteria
     *  -Calcular la distribución de productos en una estanteria sin productos
     *  -Calcular la distribución de productos en una estanteria con productos incorrectos
     *  -Calcular la distribución de productos en una estanteria con productos sin similitudes
     */
    @Test
    public void testCalcularDistribucionProductos() {
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");
        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        String expectedError = "Error calculating distribution:Verification failed:Shelf not initialized, please create a shelf first.";
        try {
            cd.domainCtrl_ComputationFastShelve();
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ComputationSlowShelve();
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ComputationThirdShelve();
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }


        try {
            cd.domainCtrl_InitializeShelve(2, 1);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        expectedError = "Error calculating distribution:The following two products still do not have Similarity: Manzana Pera\n";
        cd.domainCtrl_RemoveSimilitud("Manzana", "Pera");

        try {
            cd.domainCtrl_ComputationFastShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ComputationSlowShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ComputationThirdShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }

        try {
            cd.domainCtrl_DeleteProductShelve("Manzana");
            cd.domainCtrl_AddProductShelve("Kiwi");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        
        expectedError= "Error calculating distribution:Verification failed:The product from the catalog Manzana is not in the shelf";
        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
            cd.domainCtrl_ComputationFastShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }        
        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
            cd.domainCtrl_ComputationSlowShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
            cd.domainCtrl_ComputationThirdShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }


        try {
            cd.domainCtrl_DeleteProductShelve("Pera");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        expectedError= "Error calculating distribution:Verification failed:The number of products between the catalog and the bookshelf do not match, the catalog has 2 and shelf has 1";
        try {
            cd.domainCtrl_ComputationFastShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ComputationSlowShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        try {
            cd.domainCtrl_ComputationThirdShelve();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals(expectedError, e.getMessage());
        }
        
        cd.domainCtrl_ImportManuallyProducts("Naranja", 3000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Mandarina", 4000.0, "Fruta");
        try {
            cd.domainCtrl_ImportManuallySimilituds("Naranja", "Mandarina", 0.5f);
            cd.domainCtrl_ImportManuallySimilituds("Pera", "Mandarina", 0.9f);
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Mandarina", 0.9f);
            cd.domainCtrl_ImportManuallySimilituds("Pera", "Manzana", 0.1f);
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Naranja", 0.8f);
            cd.domainCtrl_ImportManuallySimilituds("Pera", "Naranja", 0.6f);
            cd.domainCtrl_InitializeShelve(4, 1);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        String costeIni = cd.domainCtrl_GetShelveCost();
        try {
            cd.domainCtrl_ComputationFastShelve();
            assertNotEquals(costeIni, cd.domainCtrl_GetShelveCost());
            cd.domainCtrl_ComputationSlowShelve();
            assertNotEquals(costeIni, cd.domainCtrl_GetShelveCost());
            cd.domainCtrl_ComputationThirdShelve();
            assertNotEquals(costeIni, cd.domainCtrl_GetShelveCost());
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

    }


    /**
     * Este test verifica que se pueda obtener las estadísticas de una estanteria y todos sus posibles resultados.
     * Objeto de la prueba: Consultar Estadísticas
     * Ficheros necesarios: -
     * Valores estudiados:
     * -Consultar las estadísticas de una estanteria
     * -Consultar las estadísticas de una estanteria no inicializada
     * -Consultar las estadísticas de una estanteria no calculada
     */
    @Test
    public void testConsultarEstadisticas(){
        cd.domainCtrl_ImportManuallyProducts("Manzana", 1000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Pera", 2000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Naranja", 3000.0, "Fruta");
        cd.domainCtrl_ImportManuallyProducts("Mandarina", 4000.0, "Fruta");
        try {
            cd.domainCtrl_getFastEstadisticasByCost();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Error: Shelf not initialized, please create a shelf first.", e.getMessage());
        }
        try {
            cd.domainCtrl_getSlowEstadisticasByCost();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Error: Shelf not initialized, please create a shelf first.", e.getMessage());
        }
        try {
            cd.domainCtrl_getThirdEstadisticasByCost();
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Error: Shelf not initialized, please create a shelf first.", e.getMessage());
        }

        try {
            cd.domainCtrl_InitializeShelve(4, 1);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        
        try {
            assertEquals(0, cd.domainCtrl_getFastEstadisticasByCost().size());
            assertEquals(0, cd.domainCtrl_getSlowEstadisticasByCost().size());
            assertEquals(0, cd.domainCtrl_getThirdEstadisticasByCost().size());
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }


        try {
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Pera", 0.5f);
            cd.domainCtrl_ImportManuallySimilituds("Naranja", "Mandarina", 0.5f);
            cd.domainCtrl_ImportManuallySimilituds("Pera", "Mandarina", 0.9f);
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Mandarina", 0.9f);
            cd.domainCtrl_ImportManuallySimilituds("Pera", "Manzana", 0.1f);
            cd.domainCtrl_ImportManuallySimilituds("Manzana", "Naranja", 0.8f);
            cd.domainCtrl_ImportManuallySimilituds("Pera", "Naranja", 0.6f);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_ComputationFastShelve();
            cd.domainCtrl_ComputationSlowShelve();
            cd.domainCtrl_ComputationThirdShelve();
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            assertEquals(1, cd.domainCtrl_getFastEstadisticasByCost().size());
            assertEquals(1, cd.domainCtrl_getFastEstadisticasByNumIteration().size());
            assertEquals(1, cd.domainCtrl_getFastEstadisticasByTimestamp().size());

            assertEquals(1, cd.domainCtrl_getSlowEstadisticasByCost().size());
            assertEquals(1, cd.domainCtrl_getSlowEstadisticasByNumIteration().size());
            assertEquals(1, cd.domainCtrl_getSlowEstadisticasByTimestamp().size());

            assertEquals(1, cd.domainCtrl_getThirdEstadisticasByCost().size());
            assertEquals(1, cd.domainCtrl_getThirdEstadisticasByNumIteration().size());
            assertEquals(1, cd.domainCtrl_getThirdEstadisticasByTimestamp().size());
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_ESTADISTICAS));
            cd.domainCtrl_ExportEstadisticas(cd.domainCtrl_getFastEstadisticasByCost().get(0), TEST_FILE_PATH_ESTADISTICAS);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }


        try {
            cd.domainCtrl_ResetFastEstadisticas();
            cd.domainCtrl_ResetSlowEstadisticas();
            cd.domainCtrl_ResetThirdEstadisticas();

            assertEquals(0, cd.domainCtrl_getFastEstadisticasByCost().size());
            assertEquals(0, cd.domainCtrl_getFastEstadisticasByNumIteration().size());
            assertEquals(0, cd.domainCtrl_getFastEstadisticasByTimestamp().size());

            assertEquals(0, cd.domainCtrl_getSlowEstadisticasByCost().size());
            assertEquals(0, cd.domainCtrl_getSlowEstadisticasByNumIteration().size());
            assertEquals(0, cd.domainCtrl_getSlowEstadisticasByTimestamp().size());

            assertEquals(0, cd.domainCtrl_getThirdEstadisticasByCost().size());
            assertEquals(0, cd.domainCtrl_getThirdEstadisticasByNumIteration().size());
            assertEquals(0, cd.domainCtrl_getThirdEstadisticasByTimestamp().size());
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

    }


//=========================================================================================================
//====================================== CASOS DE USO DE SESIÓN ===========================================
//=========================================================================================================


    /**
     * Este test verifica si se puede crear un usuario y todos sus posibles resultados.
     * Objeto de la prueba: Crear usuario
     * Ficheros necesarios: - 
     * Valores estudiados:
     * -Crear un usuario
     * -Crear un usuario con nombre existente
     */
    @Test
    public void testcrearUsuario(){
        try {
            cd.domainCtrl_deleteUser("ThisIsATest");
        } catch (Exception e) {
        }

        try {
            cd.domainCtrl_createUser("ThisIsATest");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            cd.domainCtrl_createUser("ThisIsATest");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Error:The user already exists.", e.getMessage());
        }
    }

    /**
     * Este test verifica si se puede eliminar un usuario y todos sus posibles resultados.
     * Objeto de la prueba: Eliminar usuario
     * Ficheros necesarios: - 
     * Valores estudiados:
     * -Eliminar un usuario
     * -Eliminar un usuario inexistente
     */
    @Test
    public void testeliminarUsuario(){
        try {
            cd.domainCtrl_deleteUser("ThisIsATest");
        } catch (Exception e) {
        }
        try {
            cd.domainCtrl_deleteUser("ThisIsATest");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("Error:The user doesn't exists.", e.getMessage());
        }
        try {
            cd.domainCtrl_createUser("ThisIsATest");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try{
            cd.domainCtrl_deleteUser("ThisIsATest");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
    }

    /**
     * Este test verifica si se puede hacer login y todos sus posibles resultados.
     * Objeto de la prueba: Login
     * Ficheros necesarios: - 
     * Valores estudiados:
     * -Hacer login
     * -Hacer login con usuario inexistente
     */
    @Test
    public void testlogin(){
        try {
            cd.domainCtrl_deleteUser("ThisIsATest");
        } catch (Exception e) {
        }

        try {
            cd.domainCtrl_login("ThisIsATest");
            fail("Se esperaba excepción");
        } catch (Exception e) {
            assertEquals("The user doesn't exists.", e.getMessage());
        }
        try {
            cd.domainCtrl_createUser("ThisIsATest");
            cd.domainCtrl_login("ThisIsATest");
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
    }

    /**
     * Este test verifica que el autoguardado cumple con la lógica de estados esperada
     * Objeto de la prueba: Autoguardado
     * Ficheros necesarios: - (test_export_Sesion.csv creado en el test)
     * Valores estudiados:
     * -Cargar una sesión
     * -Cargar una sesión inexistente
     */
    @Test
    public void testAutoguardado(){
        try {
            cd.domainCtrl_activateAutoSave();
            cd.domainCtrl_activateAutoSave();
        } catch (Exception e) {
            assertEquals("Auto-save is already active.", e.getMessage());
        }
        try {
            cd.domainCtrl_deactivateAutoSave();
            cd.domainCtrl_deactivateAutoSave();
        } catch (Exception e) {
            assertEquals("Auto-save is not active.", e.getMessage());
        }

    }

    /**
     * Este test verifica que se pueda guardar una sesión y todos sus posibles resultados.
     * Objeto de la prueba: Guardar Sesión
     * Ficheros necesarios: - (test_export_Sesion.csv creado en el test)
     * Valores estudiados:
     * -Guardar una sesión
     * -Guardar una sesión sin productos, estanteria, similitudes y estadísticas
     */
    @Test
    public void testGuardarSesion() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_ESTADISTICAS));
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            cd.domainCtrl_ExportSession(TEST_FILE_PATH_SESSION);
        } catch (Exception e) {
            assertEquals("Error exporting the session: Shelf not initialized, please create a shelf first.", e.getMessage());
        }

        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            cd.domainCtrl_saveSession();
        } catch (Exception e) {
            assertEquals("Error exporting the session: There is no user log in", e.getMessage());
        }

        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            try {
                cd.domainCtrl_deleteUser("ThisIsATest");
            } catch (Exception e) {
            }
            cd.domainCtrl_createUser("ThisIsATest");
            cd.domainCtrl_login("test");
            cd.domainCtrl_saveSession();
        } catch (Exception e) {
            assertEquals("The user doesn't exists.", e.getMessage());
        }

        try{
            cd = new CtrlDomini();
            cd.domainCtrl_ImportManuallyProducts("M", 1000.0, "Fruta");
            cd.domainCtrl_ImportManuallyProducts("P", 2000.0, "Fruta");
            cd.domainCtrl_ImportManuallySimilituds("M", "P", 0.5f);
            cd.domainCtrl_InitializeShelve(2, 1);
            cd.domainCtrl_ComputationFastShelve();
        } catch (Exception e){
            fail("No se esperaba excepción "+ e.getMessage());
        }

        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            cd.domainCtrl_ExportSession(TEST_FILE_PATH_SESSION);
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            try {
                cd.domainCtrl_deleteUser("ThisIsATest");
            } catch (Exception e) {

            }
            cd.domainCtrl_createUser("ThisIsATest");
            cd.domainCtrl_login("ThisIsATest");
            cd.domainCtrl_saveSession();

            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            cd.domainCtrl_saveSession();
        } catch (Exception e) {
            fail("No se esperaba excepción "+ e.getMessage());
        }

    }

    /**
     * Este test verifica si se puede crear una sesión y todos sus posibles resultados.
     * Objeto de la prueba: Crear Sesión
     * Ficheros necesarios: - (test_export_Sesion.csv creado en el test)
     * Valores estudiados:
     * -Crear una sesión desde un fichero
     * -Crear una sesión desde un usuario
     * -Crear una sesión desde fichero inexistente
     * -Crear una sesión desde usuario inexistente
     */
    @Test
    public void testcrearSesion(){
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
            cd.domainCtrl_ImportSession(TEST_FILE_PATH_SESSION);
            fail("Se esperaba excepción");
        } catch (Exception e) {
        }
        try {
            try {
                cd.domainCtrl_deleteUser("ThisIsATest");
            } catch (Exception e) {
            }
            cd.domainCtrl_login("ThisIsATest");
            fail("Se esperaba excepción");
        } catch (Exception e) {
        }
        try{
            cd = new CtrlDomini();
            cd.domainCtrl_createUser("ThisIsATest");
            cd.domainCtrl_login("ThisIsATest");
            cd.domainCtrl_ImportManuallyProducts("M", 1000.0, "Fruta");
            cd.domainCtrl_ImportManuallyProducts("P", 2000.0, "Fruta");
            cd.domainCtrl_ImportManuallySimilituds("M", "P", 0.5f);
            cd.domainCtrl_InitializeShelve(2, 1);
            cd.domainCtrl_ComputationFastShelve();
        } catch (Exception e){
            fail("No se esperaba excepción "+ e.getMessage());
        }


        try {
            cd.domainCtrl_saveSession();
        } catch (Exception e) {
            fail("No se esperaba excepción");
        }

        try {
            cd.domainCtrl_ExportSession(TEST_FILE_PATH_SESSION);
            cd.domainCtrl_ImportSession(TEST_FILE_PATH_SESSION);
        } catch (Exception e) {
            fail("No se esperaba excepción "+ e.getMessage());
        }

       try {
        cd.domainCtrl_login("ThisIsATest");
       } catch (Exception e) {
        fail("No se esperaba excepción");
       } 

    }


    @After
    public void tearDown() throws IOException {
        // Clean up test files after each test
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_CJTPRODUCT));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SIMILITUD));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SHELVE));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_ESTADISTICAS));
        Files.deleteIfExists(Paths.get(TEST_FILE_PATH_SESSION));
        try {
            cd.domainCtrl_deleteUser("ThisIsATest");
        } catch (Exception e) {
        }
    }


}
