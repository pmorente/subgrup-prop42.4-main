package edu.upc.prop.clusterxx.controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;

import edu.upc.prop.clusterxx.Estadisticas;
import edu.upc.prop.clusterxx.forms.FileBrowser;
import edu.upc.prop.clusterxx.forms.productsView;
import edu.upc.prop.clusterxx.forms.shelveView;
import edu.upc.prop.clusterxx.forms.similitudsView;
import edu.upc.prop.clusterxx.forms.usersView;

/**
 * Controlador de la capa de presentación
 */
public class CtrlPresentation {

    /**
     * Controlador de dominio
     */
    private CtrlDomini ctrlDominio;

    /**
     * Vista principal
     */
    JFrame frame; // Main frame

    // This views extends from JPanel
    /**
     * Vista principal que se representa en el frame
     */
    private JPanel vistaPrincipal = null;

    // Views
    /**
     * Vista de productos
     */
    private productsView vistaProducts = null;

    /**
     * Vista de similitudes
     */
    private similitudsView vistaSimi = null;

    /**
     * Vista de estanterías
     */
    private shelveView vistaShelve = null;

    /**
     * Vista de usuarios
     */
    private usersView vistaUsers = null;

    // Menu items
    /**
     * Barra del menu
     */
    JMenuBar menuBar;

    /**
     * Botón del menu de logout
     */
    private JMenuItem logoutItem;

    /**
     * Botón del menu de importar
     */
    private JMenuItem importItem;

    /**
     * Botón del menu de exportar
     */
    private JMenuItem exportItem;

    /**
     * Botón del menu de guardar
     */
    private JMenuItem saveItem;

    /**
     * Botón del menu de about
     */
    private JMenuItem aboutItem;

    /**
     * Estado del autosave
     */
    boolean isAutoSaveActivated;

    // Constructor
    /**
     * Constructor de la clase CtrlPresentation
     */
    public CtrlPresentation() {

        isAutoSaveActivated = false;

        try {
            ctrlDominio = new CtrlDomini(); // Initialize the domain controller
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        // GUI look&Feel
        try {
            // Set GTK+ Look and Feel
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception e) {
            System.err.println("GTK+ Look and Feel is not available. Falling back to system default.");
        }

        // Create the main view
        if (vistaProducts == null) {
            vistaProducts = new productsView(this);
        }
        if (vistaSimi == null) {
            vistaSimi = new similitudsView(this); // Inject this CtrlPresentation
        }
        if (vistaShelve == null) {
            vistaShelve = new shelveView(this); // Inject this CtrlPresentation
        }
        if (vistaUsers == null) {
            vistaUsers = new usersView(this); // Inject this CtrlPresentation
        }

        vistaPrincipal = vistaUsers;
        startFrame();


    }

    // Activate keybindings
    /**
     * Activa los keybindings
     */
    public void activateKeyBindings() {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = frame.getRootPane().getActionMap();

        // Key bindings and actions for Save All (Ctrl+S)
        KeyStroke saveKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(saveKeyStroke, "saveAction");
        actionMap.put("saveAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the action for Save All
                actionPerformed_buttonSave(e);
            }
        });

        // Key bindings and actions for Logout (Ctrl+A)
        KeyStroke logoutKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(logoutKeyStroke, "logoutAction");
        actionMap.put("logoutAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the action for Logout
                actionPerformed_buttonLogout(e);
            }
        });

        // Key bindings and actions for Import Session (Ctrl+D)
        KeyStroke importKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(importKeyStroke, "importAction");
        actionMap.put("importAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the action for Import Session
                actionPerformed_buttonImport(e);
            }
        });

        // Key bindings and actions for Export Session (Ctrl+F)
        KeyStroke exportKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(exportKeyStroke, "exportAction");
        actionMap.put("exportAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the action for Export Session
                actionPerformed_buttonExport(e);
            }
        });

        // Key bindings and actions for Help Window (Ctrl+Q)
        KeyStroke helpKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK);
        inputMap.put(helpKeyStroke, "aboutAction");
        actionMap.put("aboutAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Call the action for About Window
                actionPerformed_buttonAbout(e);
            }
        });
    }

    // Deactivate keybindings
    /**
     * Desactiva los keybindings
     */
    public void deactivateKeyBindings() {
        InputMap inputMap = frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Remove keybindings
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
        inputMap.remove(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
    }

    // Returns the main frame
    /**
     * Devuelve el frame principal
     * @return frame
     */
    public JFrame getFrame() {
        return frame;
    }

    // Start the presentation layer
    /**
     * Inicia la capa de presentación
     */
    public void startPresentation() {
        vistaPrincipal.setVisible(true); // Show the main view
    }

    // Listeners of the View
    /**
     * Inicia los listeners de la vista
     */
    public void startListeners(){


        // File menu items
        logoutItem.addActionListener
                (new ActionListener() {
                    public void actionPerformed (ActionEvent event) { actionPerformed_buttonLogout(event); }
                });

        importItem.addActionListener
                (new ActionListener() {
                    public void actionPerformed (ActionEvent event) {
                        actionPerformed_buttonImport(event);
                    }
                });

        exportItem.addActionListener
                (new ActionListener() {
                    public void actionPerformed (ActionEvent event) { actionPerformed_buttonExport(event);}
                });

        saveItem.addActionListener
                (new ActionListener() {
                    public void actionPerformed (ActionEvent event) { actionPerformed_buttonSave(event);}
                });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonAbout(event);
            }
        });

    }


    // Methods of the listeners interfaces

    /**
     * Método que se ejecuta al pulsar el botón de about
     * @param e Evento del botón
     */
    public void actionPerformed_buttonAbout(ActionEvent e) {
            JOptionPane.showMessageDialog(frame,
                    "This is Java Swing application to manage products distribution in a Supermarket\n" +
                            "It was developed as part of the Software Engineering course at UPC.\n" +
                            "Authors:\n" +
                            "Team Members:\n" +
                            "Andujar Sanchez, Enrique - enrique.andujar@estudiantat.upc.edu\n" +
                            "Esgleas Tafalla, Joan - joan.esgleas@estudiantat.upc.edu\n" +
                            "Guillamon Chavez, Gabriel - gabriel.guillamon@estudiantat.upc.edu\n" +
                            "Morente Alcober, Pau - pau.morente@estudiantat.upc.edu\n" +
                            "Project Statement – Fall Semester 2024/25\n\n" +
                            "Product Distribution in a Supermarket\n" +
                            "The objective is to find the optimal product distribution in a supermarket to increase customer purchases. \n" +
                            "The probability of a customer buying a product increases if it’s near a related product (e.g., if beer is near chips, \n" +
                            "the customer is likely to buy both). The system will optimize product placement based on similarity data provided by the user.\n\n",

                    "About",
                    JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Método que se ejecuta al pulsar el botón de importar
     * @param e Evento del botón
     */
    public void actionPerformed_buttonImport(ActionEvent e) {
        try {
        FileBrowser fileBrowser = new FileBrowser();
        // Open a file browser dialog
        String filePath = fileBrowser.browse(frame, false);
        if (filePath != null) {
            ctrlPresentation_importSession(filePath);
            JOptionPane.showMessageDialog(frame, "Session imported successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No file selected.");
        }

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Método que se ejecuta al pulsar el botón de exportar
     * @param e Evento del botón
     */
    public void actionPerformed_buttonExport(ActionEvent e) {
        try {
            //Create a dialog box to get the name
            String name_dirty = JOptionPane.showInputDialog(frame,
                    "Enter the name of the file:",
                    "File Name Input",
                    JOptionPane.PLAIN_MESSAGE);

            String name = ctrlP_checkfile(name_dirty); // Check the input

            //Open a file browser dialog to choose the folder
            FileBrowser fileBrowser = new FileBrowser();
            String folderPath = fileBrowser.browse(frame, true);

            //Check if a folder was selected
            if (folderPath != null) {
                // Combine the folder path and file name
                String fullPath = folderPath + File.separator + name;

                // Perform the export operation
                ctrlPresentation_exportSession(fullPath);

                JOptionPane.showMessageDialog(frame,
                        "Session exported successfully to: " + fullPath,
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // If no folder was selected, inform the user
                JOptionPane.showMessageDialog(frame,
                        "Export canceled. No folder selected.",
                        "Export Canceled",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception exception) {
            // Handle errors
            JOptionPane.showMessageDialog(frame,
                    "An error occurred: " + exception.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Método que se ejecuta al pulsar el botón de guardar
     * @param e Evento del botón
     */

    public void actionPerformed_buttonSave(ActionEvent e) {
        try {
            ctrlPresentation_saveAll();
            JOptionPane.showMessageDialog(frame, "Session saved successfully.");
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método que guarda la sesión de forma silenciosa en background
     */

    public void actionPerformed_buttonSaveSilent() {
        try {
            ctrlPresentation_saveAll();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame,
                    "Your session will not be saved. However, you can proceed. To save the session, you need to -> " + exception.getMessage(),
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Método que se ejecuta al pulsar el botón de logout
     * @param e Evento del botón
     */

    public void actionPerformed_buttonLogout(ActionEvent e) {
        try {
            actionPerformed_buttonSaveSilent(); // Save the session before logout
            sincronizacionUsers();
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(frame, "Your session will not be saved. In order to save the session you need to ->" + exception.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Método que se ejecuta al pulsar el botón de ayuda
     * @param e Evento del botón
     */
    public void actionPerformed_buttonHelpWindow(ActionEvent e) {

    }

    /**
     * Método que inicia la barra de menú de la vista
     */
    private void startMenuBar() {
        // Create the menu bar
        menuBar = new JMenuBar();

        // Create the "File" menu
        /**
         * Botón del menu de archivo
         */
        JMenu fileMenu = new JMenu("File");

        // Add menu items to "File"
        logoutItem = new JMenuItem("Logout (Ctrl+A)");
        importItem = new JMenuItem("Import Session (Ctrl+D)");
        exportItem = new JMenuItem("Export Session (Ctrl+F)");
        saveItem = new JMenuItem("Save All (Ctrl+S)");

        fileMenu.add(logoutItem);
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator(); // Add a separator line
        fileMenu.add(saveItem);

        // Add an Autosave toggle menu item
        JMenuItem autosaveItem = new JMenuItem(isAutoSaveActivated ? "Deactivate Autosave" : "Activate Autosave");
        fileMenu.add(autosaveItem);

        // Create the "Help" menu
        /**
         * Botón del menu de ayuda
         */
        JMenu helpMenu = new JMenu("Help");

        // Add menu items to "Help"
        aboutItem = new JMenuItem("About Ctrl+Q");
        helpMenu.add(aboutItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // Add a label for the logged-in user on the left
        JLabel userLabel = new JLabel("Logged as: " + ctrlPresentation_getUserLogged());
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Add padding
        menuBar.add(userLabel);

        // Add a spacer to push the autosave label to the right
        menuBar.add(Box.createHorizontalGlue());

        // Add a label for the autosave status on the right
        JLabel autoSaveLabel = new JLabel(isAutoSaveActivated ? "Autosave: Activated" : "Autosave: Deactivated");
        autoSaveLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Add padding
        menuBar.add(autoSaveLabel);

        // Add action listener to the autosave item to toggle autosave
        autosaveItem.addActionListener(e -> {
            if (isAutoSaveActivated) {
                // Call the domain controller to deactivate autosave
               try {
                     ctrlPresentation_deactivateAutoSave();
                } catch (Exception exception) {
                     JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
               } // Placeholder for the actual function
            } else {
                // Call the domain controller to activate autosave
                try {
                    ctrlPresentation_activateAutoSave();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(frame, exception.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                // Placeholder for the actual function
            }

            // Toggle the state and update the menu item and label
            isAutoSaveActivated = !isAutoSaveActivated;
            autosaveItem.setText(isAutoSaveActivated ? "Deactivate Autosave" : "Activate Autosave");
            autoSaveLabel.setText(isAutoSaveActivated ? "Autosave: Activated" : "Autosave: Deactivated");
        });
    }

    /**
     * Método que inicia el frame de la vista
     */
    private void startFrame() {

        frame = new JFrame("Welcome!");
        // Set default close operation
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(vistaPrincipal);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);
        // Set frame size and make it visible
        frame.setSize(800, 600); // Adjust size as needed
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }

    /**
     * Método que sincroniza la vista de usuarios
     */
    public void sincronizacionUsers() {

        frame.setTitle("Users View");
        vistaPrincipal = vistaUsers;
        deactivateKeyBindings();
        frame.setContentPane(vistaPrincipal);

        // Set the menu bar to the frame
        frame.setJMenuBar(null);
        // Set frame size and make it visible
        frame.setVisible(true);
    }

    /**
     * Método que sincroniza la vista de similitudes
     */
    public void sincronizacionSimilitud() {

        frame.setTitle("Similarity View");
        vistaSimi.showCatalogSimilitudPanel();
        vistaPrincipal = vistaSimi;
        startMenuBar();
        startListeners();
        activateKeyBindings();
        frame.setContentPane(vistaPrincipal);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Set frame size and make it visible
        frame.setVisible(true);
    }

    /**
     * Método que sincroniza la vista de catálogo
     */
    public void sincronizacionCatalog() {

        frame.setTitle("Catalog View");
        vistaProducts.showCatalogPanel();
        vistaPrincipal = vistaProducts;
        startMenuBar();
        startListeners();
        activateKeyBindings();
        frame.setContentPane(vistaPrincipal);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Set frame size and make it visible
        frame.setVisible(true);
    }

    /**
     * Método que sincroniza la vista de estantería
     */
    public void sincronizacionShelve() {

        frame.setTitle("Shelf View");
        vistaShelve.showinitializeShelvePanel();
        vistaPrincipal = vistaShelve;
        startMenuBar();
        startListeners();
        activateKeyBindings();
        frame.setContentPane(vistaPrincipal);

        // Set the menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Set frame size and make it visible
        frame.setVisible(true);
    }



    //////////////////////// Llamadas al controlador de dominio

    ///  Llamadas del Frame Principal a CtrlDominio
    /**
     * Método que llama al CtrlDomino y crea un usuario
     * @param username Nombre del usuario
     * @throws Exception Excepción si el usuario ya existe
     */
    public void ctrlPresentation_createUser(String username) throws Exception{
        ctrlDominio.domainCtrl_createUser(username);
    }

    /**
     * Método que llama al CtrlDomino y elimina un usuario
     * @param username Nombre del usuario
     * @throws Exception Excepción si el usuario no existe
     */
    public void ctrlPresentation_deleteUser(String username) throws Exception{
        ctrlDominio.domainCtrl_deleteUser(username);
    }

    /**
     * Método que llama al CtrlDomino y hace login de un usuario
     * @throws Exception Excepción si el usuario no existe
     * @param username Nombre del usuario
     */
    public void ctrlPresentation_loginUser(String username) throws Exception{
        ctrlDominio.domainCtrl_login(username);
    }

    /**
     * Método que llama al CtrlDomino y hace logout de un usuario
     * @return Nombre del usuario logueado
     */
    public String ctrlPresentation_getUserLogged() {
        return ctrlDominio.domainCtrl_getUserLoggedIn();
    }

    /**
     * Método que llama al CtrlDomino y obtiene todos los usuarios
     * @return Lista de usuarios
     * @throws Exception Si algo falla durante la obtención de los usuarios
     */
    public List<String> ctrlPresentation_getAllUsers() throws Exception {
        return ctrlDominio.domainCtrl_getUsers();
    }

    /**
     * Método que llama al CtrlDomino y exporta la sesión
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la exportación de la sesión
     */
    public void ctrlPresentation_exportSession(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ExportSession(filePath);
    }

    /**
     * Método que llama al CtrlDomino y importa la sesión
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la importación de la sesión
     */
    public void ctrlPresentation_importSession(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ImportSession(filePath);
    }

    /**
     * Método que llama al CtrlDomino y guarda la sesión
     * @throws Exception Si algo falla durante el guardado de la sesión
     */
    public void ctrlPresentation_saveAll() throws Exception {
        ctrlDominio.domainCtrl_saveSession();
    }




    ///  Llamadas del Autosave Functionality
    /**
     * Método que llama al CtrlDomino y desactiva el autosave
     * @throws Exception Si algo falla
     */
    public void ctrlPresentation_deactivateAutoSave() throws Exception{
        ctrlDominio.domainCtrl_deactivateAutoSave();
    }

    /**
     * Método que llama al CtrlDomino y activa el autosave
     * @throws Exception Si algo falla durante la activación del autosave
     */
    public void ctrlPresentation_activateAutoSave() throws Exception{
        ctrlDominio.domainCtrl_activateAutoSave();
    }


    /// Llamadas de View Products
    /**
     * Método que llama al CtrlDomino y comprueba la entrada
     * @param input Entrada
     * @param type Si es 0, valores alfanuméricos, sin espacios, sin puntos. Si es 2 valores alfanuméricos, sin espacios. Si es 1, valores alfanuméricos sin puntos
     * @return String
     */
    public String ctrlP_checkInput(String input, int type) {
        return ctrlDominio.domainCtrl_checkInput(input, type);
    }

    /**
     * Método que llama al CtrlDomino y comprueba el nombre de un archivo
     * @param input Nombre del archivo
     * @return String
     */
    public String ctrlP_checkfile(String input) {
        return ctrlDominio.domainCtrl_checkFileName(input);
    }

    /**
     * Método que llama al CtrlDomino y obtiene el catálogo
     * @return Lista de productos
     */
    public List<String[]> ctrlPresentation_getCatalog() {
        return ctrlDominio.domainCtrl_ShowProducts();
    }

    /**
     * Método que llama al CtrlDomino y añade un producto
     * @param name Nombre del producto
     * @param price Precio del producto
     * @param description Descripción del producto
     */
    public void ctrlPresentation_addProduct(String name, double price, String description) {
        ctrlDominio.domainCtrl_ImportManuallyProducts(name, price, description);
    }

    /**
     * Método que llama al CtrlDomino y actualiza el precio de un producto
     * @param name Nombre del producto
     * @param price Precio del producto
     */
    public void ctrlPresentation_updatePrice(String name, double price) {
        ctrlDominio.domainCtrl_ChangePriceProduct(name, price);
    }

    /**
     * Método que llama al CtrlDomino y actualiza la descripción de un producto
     * @param name Nombre del producto
     * @param description Descripción del producto
     */
    public void ctrlPresentation_updateDescription(String name, String description) {
        ctrlDominio.domainCtrl_ChangeDescriptionProduct(name, description);

    }

    /**
     * Método que llama al CtrlDomino y elimina un producto
     * @param name Nombre del producto
     */
    public void ctrlPresentation_deleteProduct(String name) {
        ctrlDominio.domainCtrl_RemoveProduct(name);

    }

    /**
     * Método que llama al CtrlDomino y importa productos
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la importacion
     */
    public void ctrlPresentation_importProducts(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ImportProducts(filePath);

    }

    /**
     * Método que llama al CtrlDomino y exporta productos
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la exportación
     */
    public void ctrlPresentation_exportProducts(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ExportProducts(filePath);

    }


    /// Llamadas de View Similituds
    /**
     * Método que llama al CtrlDomino y obtiene las similitudes
     * @return Lista de similitudes
     */
    public List<String[]> ctrlPresentation_getSimilituds() {
        return ctrlDominio.domainCtrl_ShowSimilituds();
    }

    /**
     * Método que llama al CtrlDomino y crea una similitud
     * @param product1 Producto 1
     * @param product2 Producto 2
     * @param similarity Similitud
     * @throws Exception Si algo falla mientras crea la similitud
     */
    public void ctrlPresentation_createSimilituds(String product1, String product2, float similarity)  throws Exception{
        ctrlDominio.domainCtrl_ImportManuallySimilituds(product1, product2, similarity);
    }

    /**
     * Método que llama al CtrlDomino y elimina similitudes
     * @param Product1 Producto 1
     * @param Product2 Producto 2
     */
    public void ctrlPresentation_deleteSimilituds(String Product1, String Product2) {
        ctrlDominio.domainCtrl_RemoveSimilitud(Product1, Product2);
    }

    /**
     * Método que llama al CtrlDomino y exporta similitudes
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la exportación
     */
    public void ctrlPresentation_exportSimilituds(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ExportSimilituds(filePath);
    }

    /**
     * Método que llama al CtrlDomino y importa similitudes
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la importación
     */
    public void ctrlPresentation_importSimilituds(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ImportSimilituds(filePath);
    }

    /**
     * Método que llama al CtrlDomino y hace un health check de las similitudes
     */
    public void ctrlPresentation_healthCheckSimilituds() {
        ctrlDominio.domainCtrl_HealthCheckSimilitud();
    }


    /// Llamadas de View Shelve & Estadísticas
    /**
     * Método que llama al CtrlDomino y inicializa la estantería
     * @param rows Filas
     * @param columns Columnas
     * @throws Exception Si algo falla durante la inicialización
     */
    public void ctrlPresentation_InitializeShelve(int rows, int columns) throws Exception {
        ctrlDominio.domainCtrl_InitializeShelve(rows, columns);
    }

    /**
     * Método que llama al CtrlDomino y modifica la estantería
     * @param name1 Nombre del primer producto a intercambiar
     * @param name2 Nombre del segundo producto a intercambiar
     * @throws Exception Si algo falla durante la modificación
     */
    public void ctrlPresentation_ModifyManuallyShelve(String name1, String name2) throws Exception {
        ctrlDominio.domainCtrl_ModifyManuallyShelve(name1, name2);
    }

    /**
     * Método que llama al CtrlDomino y muestra la estantería
     * @return Lista de productos en la estantería
     * @throws Exception Si algo falla durante la obtención de la estantería
     */
    public Object[] ctrlPresentation_ShowShelve() throws Exception {
        return ctrlDominio.domainCtrl_ShowShelve();
    }

    /**
     * Método que llama al CtrlDomino y obtiene el coste de la distribucion de la estantería
     * @return Coste de la estantería
     */
    public String ctrlPresentation_GetCostShelve() {
        return ctrlDominio.domainCtrl_GetShelveCost();
    }

    /**
     * Método que llama al CtrlDomino y obtiene el número de iteraciones del último algoritmo
     * @return Número de iteraciones
     */
    public String ctrlPresentation_GetIterLastAlgo() {
        return ctrlDominio.domainCtrl_GetIterLastAlgo();
    }
    /**
     * Método que llama al CtrlDomino y calcula la distribución de la estantería con el algoritmo rápido
     * @throws Exception Si algo falla durante el cálculo
     */
    public void ctrlPresentation_CalculateFast() throws Exception {
        ctrlDominio.domainCtrl_ComputationFastShelve();
    }

    /**
     * Método que llama al CtrlDomino y calcula la distribución de la estantería con el algoritmo lento
     * @throws Exception Si algo falla durante el cálculo
     */
    public void ctrlPresentation_CalculateSlow() throws Exception {
        ctrlDominio.domainCtrl_ComputationSlowShelve();
    }

    /**
     * Método que llama al CtrlDomino y calcula la distribución de la estantería con el tercer algoritmo
     * @throws Exception Si algo falla durante el cálculo
     */
    public void ctrlPresentation_CalculateThird() throws Exception {
        ctrlDominio.domainCtrl_ComputationThirdShelve();
    }

    /**
     * Método que llama al CtrlDomino y añade un producto a la estantería
     * @param product Producto a añadir
     * @throws Exception Si algo falla durante la adición
     */
    public void ctrlPresentation_AddProductShelve(String product) throws Exception {
        ctrlDominio.domainCtrl_AddProductShelve(product);
    }

    /**
     * Método que llama al CtrlDomino y elimina un producto de la estantería
     * @param product Producto a eliminar
     * @throws Exception Si algo falla durante la eliminación
     */
    public void ctrlPresentation_DeleteProductShelve(String product) throws Exception {
        ctrlDominio.domainCtrl_DeleteProductShelve(product);
    }

    /**
     * Método que llama al CtrlDomino y exporta la estantería
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la exportación
     */
    public void ctrlPresentation_ExportShelve(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ExportShelve(filePath);
    }

    /**
     * Método que llama al CtrlDomino y importa la estantería
     * @param filePath Ruta del archivo
     * @throws Exception Si algo falla durante la importación
     */
    public void ctrlPresentation_ImportShelve(String filePath) throws Exception {
        ctrlDominio.domainCtrl_ImportShelve(filePath);
    }

    /**
     * Método que llama al CtrlDomino y exporta las estadísticas rapidas por timestamp
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la exportación
     */
    public List<Estadisticas> ctrlPresentation_getFastEstadisticasByTimestamp() throws Exception{
        return ctrlDominio.domainCtrl_getFastEstadisticasByTimestamp();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas lentas por timestamp
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getSlowEstadisticasByTimestamp() throws Exception{
        return ctrlDominio.domainCtrl_getSlowEstadisticasByTimestamp();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas del tercer algoritmo por timestamp
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getThirdEstadisticasByTimestamp() throws Exception{
        return ctrlDominio.domainCtrl_getThirdEstadisticasByTimestamp();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas rápidas por iteraciones
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getFastEstadisticasByIterations() throws Exception{
        return ctrlDominio.domainCtrl_getFastEstadisticasByNumIteration();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas lentas por iteraciones
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getSlowEstadisticasByIterations() throws Exception{
        return ctrlDominio.domainCtrl_getSlowEstadisticasByNumIteration();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas del tercer algoritmo por iteraciones
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getThirdEstadisticasByIterations() throws Exception{
        return ctrlDominio.domainCtrl_getThirdEstadisticasByNumIteration();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas rápidas por coste
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getFastEstadisticasByCost() throws Exception{
        return ctrlDominio.domainCtrl_getFastEstadisticasByCost();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas lentas por coste
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getSlowEstadisticasByCost() throws Exception{
        return ctrlDominio.domainCtrl_getSlowEstadisticasByCost();
    }

    /**
     * Método que llama al CtrlDomino y obtiene las estadísticas del tercer algoritmo por coste
     * @return Lista de estadísticas
     * @throws Exception Si algo falla durante la obtención
     */
    public List<Estadisticas> ctrlPresentation_getThirdEstadisticasByCost() throws Exception{
        return ctrlDominio.domainCtrl_getThirdEstadisticasByCost();
    }

    /**
     * Método que llama al CtrlDomino y resetea las estadísticas rápidas
     * @throws Exception Si algo falla durante el reseteo
     */
    public void ctrlPresentation_resetFastEstadisticas() throws Exception{
        ctrlDominio.domainCtrl_ResetFastEstadisticas();
    }

    /**
     * Método que llama al CtrlDomino y resetea las estadísticas lentas
     * @throws Exception Si algo falla durante el reseteo
     */
    public void ctrlPresentation_resetSlowEstadisticas() throws Exception{
        ctrlDominio.domainCtrl_ResetSlowEstadisticas();
    }

    /**
     * Método que llama al CtrlDomino y resetea las estadísticas del tercer algoritmo
     * @throws Exception Si algo falla durante el reseteo
     */
    public void ctrlPresentation_resetThirdEstadisticas() throws Exception{
        ctrlDominio.domainCtrl_ResetThirdEstadisticas();
    }

    /**
     * Método que llama al CtrlDomino y exporta las estadísticas rápidas
     * @param estadisticas Estadísticas
     * @param path Ruta del archivo
     * @throws Exception Si algo falla durante la exportación
     */
    public void ctrlPresentation_ExportFastEstadisticas(Estadisticas estadisticas, String path) throws Exception {
        ctrlDominio.domainCtrl_ExportEstadisticas(estadisticas, path);
    }

}
