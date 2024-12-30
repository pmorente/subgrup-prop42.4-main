package edu.upc.prop.clusterxx.forms;

import edu.upc.prop.clusterxx.CSSUtils;
import edu.upc.prop.clusterxx.controller.CtrlPresentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Clase que representa la vista para gestionar similitudes de productos.
 *
 * Esta clase proporciona una interfaz de usuario para realizar operaciones
 * sobre similitudes de productos, como agregar, actualizar, eliminar,
 * importar, exportar y realizar comprobaciones de estado.
 *
 */
public class similitudsView extends JPanel {
    /**
     * Panel principal que contiene el contenido dinámico de la vista.
     * Este panel utiliza un `CardLayout` para gestionar y mostrar diferentes vistas de manera dinámica,
     * permitiendo cambiar entre paneles según las acciones del usuario.
     */
    private JPanel contentPanel; // Main dynamic content panel (CardLayout)

    // Refresh Similituds action buttons

    /**
     * ComboBox para seleccionar el nombre del producto 1 para crear/actualizar similitud
     */
    private JComboBox<String> add1ComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto 2 para crear/actualizar similitud
     */
    private JComboBox<String> add2ComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto 1 para eliminar similitud
     */
    private JComboBox<String> delete1ComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto 2 para eliminar similitud
     */
    private JComboBox<String> delete2ComboBox ;


    /**
     * Botón que ejecuta la acción de refrescar la lista de similitudes entre productos.
     * Al hacer clic en este botón, se actualiza la tabla que muestra las similitudes existentes entre productos.
     */
    private JButton refreshSimilitudsAction_Button;
    /**
     * Tabla que muestra las similitudes entre productos.
     * Esta tabla se actualiza cada vez que el usuario ejecuta la acción de refrescar similitudes.
     * Muestra los productos que son similares, junto con el nivel de similitud.
     */
    private JTable refreshAction_catalogSimilitudTable;

    // Add Similitud action components
    /**
     * Campo de texto donde el usuario ingresa el coeficiente de similitud entre los productos.
     * El coeficiente debe ser un valor entre 0 y 1, que indica el nivel de similitud entre los productos.
     */
    private JTextField AddSimilitudAction_similarityField;
    /**
     * Botón que ejecuta la acción de agregar una nueva similitud entre dos productos.
     * Al hacer clic en este botón, se recogen los valores de los campos de entrada y se crea una nueva similitud.
     */
    private JButton AddSimilitudAction_addButton;




    // Delete Similitud action components
    /**
     * Botón que ejecuta la acción de eliminar una similitud existente entre dos productos.
     * Al hacer clic en este botón, se recogen los valores de los campos de entrada y se elimina la similitud.
     */
    private JButton deleteSimilitud_deleteButton;

    // Healthcheck action components
    /**
     * Botón que ejecuta la acción de verificación de salud del sistema.
     * Al hacer clic en este botón, se ejecuta una comprobación de salud en el sistema y se muestra el resultado.
     */
    private JButton healthcheck_Button;

    // Import action components
    /**
     * Campo de texto donde el usuario ingresa la ruta del archivo desde el cual se importarán los datos.
     * Este campo es utilizado cuando el usuario quiere importar datos relacionados con similitudes o productos.
     */
    private JTextField importAction_filePathField;
    /**
     * Botón que ejecuta la acción de importar datos desde un archivo.
     * Al hacer clic en este botón, se recoge la ruta del archivo y se importan los datos relacionados con similitudes.
     */
    private JButton importAction_importButton;

    // Export action components
    /**
     * Campo de texto donde el usuario ingresa el nombre del archivo en el que se exportarán los datos.
     * Este campo es utilizado cuando el usuario quiere exportar datos relacionados con similitudes o productos.
     */
    private JTextField exportAction_filePathField;
    /**
     * Botón que ejecuta la acción de exportar datos a un archivo.
     * Al hacer clic en este botón, se recoge el nombre del archivo y se exportan los datos relacionados con similitudes.
     */
    private JButton exportAction_exportButton;

    // Reference to controller
    /**
     * Controlador de presentación que gestiona la lógica de la aplicación.
     * El controlador de presentación se utiliza para realizar operaciones y acciones en la aplicación.
     */
    private CtrlPresentation controller;


    /**
     * Constructor de la vista de similitudes.
     *
     * Inicializa la vista y configura los componentes de la interfaz de usuario.
     *
     * @param controller Controlador de presentación que gestiona la lógica de la aplicación.
     */
    public similitudsView(CtrlPresentation controller) {
        this.controller = controller;

        // Initialize the UI
        initUI();
    }

    /**
     * Inicializa la interfaz de usuario.
     *
     * Configura todos los componentes de la interfaz, incluyendo menús,
     * paneles y listeners.
     *
     */
    private void initUI() {
        setLayout(new BorderLayout()); // Add spacing between regions
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Outer margin

        // Create the top menu
        JMenuBar topMenu = new JMenuBar();
        JMenu catalogMenu = new JMenu("Catalog");
        JMenu similitudesMenu = new JMenu("Similarity");
        Color cf = new Color(UIManager.getColor("Label.foreground").getRGB());
        Color cb = new Color(UIManager.getColor("Label.background").getRGB());
        similitudesMenu.setBackground(cf);
        similitudesMenu.setForeground(cb);
        similitudesMenu.setOpaque(true);
        JMenu shelveMenu = new JMenu("Shelf");

        topMenu.add(catalogMenu);
        topMenu.add(similitudesMenu);
        topMenu.add(shelveMenu);

        catalogMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.sincronizacionCatalog();
            }
        });

        shelveMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.sincronizacionShelve();
            }
        });

        add(topMenu, BorderLayout.NORTH); // Add the top menu to the main layout

        // Create the right-side menu
        JPanel menuPanel = new JPanel(new GridLayout(7, 1, 10, 10)); // Vertical buttons with spacing
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create menu buttons
        JButton showSimilitudsButton = new JButton("Show Similarity");
        showSimilitudsButton.addActionListener(e -> showCatalogSimilitudPanel());

        // Menu Buttons
        JButton addSimilitudButton = new JButton("Add/Update Similarity");
        addSimilitudButton.addActionListener(e -> showAddSimilitudPanel());



        JButton deleteSimilitudButton = new JButton("Delete Similarity");
        deleteSimilitudButton.addActionListener(e -> showDeleteSimilitudPanel());

        JButton healthcheckSimilitudButton = new JButton("Healthcheck");
        healthcheckSimilitudButton.addActionListener(e -> showHealthcheckPanel());

        JButton importarSimilitudsButton = new JButton("Import Similarity");
        importarSimilitudsButton.addActionListener(e -> showImportPanel());

        JButton exportSimilitudsButton = new JButton("Export Similarity");
        exportSimilitudsButton.addActionListener(e -> showExportPanel());

        // Add buttons to the menu
        menuPanel.add(showSimilitudsButton);
        menuPanel.add(addSimilitudButton);
        menuPanel.add(deleteSimilitudButton);
        menuPanel.add(healthcheckSimilitudButton);
        menuPanel.add(importarSimilitudsButton);
        menuPanel.add(exportSimilitudsButton);

        // Adjust button
        for (Component comp : menuPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setFocusPainted(false);
                button.setPreferredSize(new Dimension(120, 30)); // Smaller rectangular buttons
            }
        }

        // Main content panel (dynamic area to show different panels)
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding inside

        // Add menu and content panels to the main layout
        add(contentPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.WEST);

        // Add panels to the CardLayout
        contentPanel.add(createCatalogSimilitudPanel(), "Similarity");
        contentPanel.add(createAddSimilitudPanel(), "AddSimilarity");
        contentPanel.add(createDeleteSimilitudPanel(), "DeleteSimilarity");
        contentPanel.add(createHealthcheckPanel(), "Healthcheck");
        contentPanel.add(createImportPanel(), "Import");
        contentPanel.add(createExportPanel(), "Export");

        // Show the catalog Similituds panel by default
        showCatalogSimilitudPanel();
        startListeners();
    }

    // Panel creation methods
    /**
     * Crea un panel que muestra un catálogo de similitudes entre productos.
     * El panel contiene una tabla que visualiza las similitudes existentes, permitiendo que el usuario vea
     * todos los productos que tienen similitudes registradas.
     * También incluye un botón que permite actualizar la tabla con los datos más recientes.
     *
     * @return JPanel el panel que contiene el catálogo de similitudes y el botón de actualización.
     */
    private JPanel createCatalogSimilitudPanel() {
        JPanel catalogSimilitudsPanel = new JPanel(new BorderLayout());
        catalogSimilitudsPanel.setBorder(BorderFactory.createTitledBorder("All Similarity"));

        // Create table
        refreshAction_catalogSimilitudTable = new JTable();
        refreshAction_catalogSimilitudTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Add the table to a scroll pane with both horizontal and vertical scrolling
        JScrollPane scrollPane = new JScrollPane(refreshAction_catalogSimilitudTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        catalogSimilitudsPanel.add(scrollPane, BorderLayout.CENTER);

        // Create refresh button
        refreshSimilitudsAction_Button = new JButton("Refresh");
        catalogSimilitudsPanel.add(refreshSimilitudsAction_Button, BorderLayout.SOUTH);

        return catalogSimilitudsPanel;
    }


    /**
     * Crea un panel que permite al usuario agregar una nueva similitud entre dos productos.
     * El panel proporciona campos de entrada para que el usuario ingrese los detalles de la similitud,
     * como los nombres de los productos y el coeficiente de similitud.
     * También incluye un botón para agregar la similitud a la base de datos o lista.
     *
     * @return JPanel el panel que permite agregar una nueva similitud entre dos productos.
     */
    private JPanel createAddSimilitudPanel() {
        JPanel addSimilitudPanel = new JPanel(new GridBagLayout());
        addSimilitudPanel.setBorder(BorderFactory.createTitledBorder("Add/Update Similarity"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margins around components
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch components horizontally

        add1ComboBox = new JComboBox<>();
        add2ComboBox = new JComboBox<>();
        AddSimilitudAction_similarityField = new JTextField(20);
        AddSimilitudAction_addButton = new JButton("Add/Update Similarity");

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        addSimilitudPanel.add(new JLabel("Product1 Name:"), gbc);
        gbc.gridx = 1;
        addSimilitudPanel.add(add1ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addSimilitudPanel.add(new JLabel("Product2 Name:"), gbc);
        gbc.gridx = 1;
        addSimilitudPanel.add(add2ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addSimilitudPanel.add(new JLabel("Similarity Coefficient:"), gbc);
        gbc.gridx = 1;
        addSimilitudPanel.add(AddSimilitudAction_similarityField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><small><i>You can add or update Similarities from products that are already in the catalog by selecting from the list.</i></small></html>");
        instructionLabel.setFont(new Font("Lable.font", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        addSimilitudPanel.add(instructionLabel, gbc);
        gbc.gridwidth = 0;

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST; // Align button to the right
        addSimilitudPanel.add(AddSimilitudAction_addButton, gbc);

        return addSimilitudPanel;
    }

    /**
     * Crea un panel que permite al usuario eliminar una similitud entre dos productos.
     * El panel contiene campos de entrada para que el usuario ingrese los productos que desea eliminar
     * de la lista de similitudes.
     * También incluye un botón para realizar la eliminación de la similitud seleccionada.
     *
     * @return JPanel el panel para eliminar una similitud entre dos productos.
     */
    private JPanel createDeleteSimilitudPanel() {
        JPanel deleteSimilitudPanel = new JPanel(new GridBagLayout());
        deleteSimilitudPanel.setBorder(BorderFactory.createTitledBorder("Delete Similarity"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        delete1ComboBox = new JComboBox<>();
        delete2ComboBox = new JComboBox<>();
        deleteSimilitud_deleteButton = new JButton("Delete Similarity");

        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteSimilitudPanel.add(new JLabel("Product1 Name:"), gbc);
        gbc.gridx = 1;
        deleteSimilitudPanel.add(delete1ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        deleteSimilitudPanel.add(new JLabel("Product2 Name:"), gbc);
        gbc.gridx = 1;
        deleteSimilitudPanel.add(delete2ComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        deleteSimilitudPanel.add(deleteSimilitud_deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><small><i>You can add Similarities from products that are already in the catalog by selecting from the list.</i></small></html>");
        instructionLabel.setFont(new Font("Lable.font", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        deleteSimilitudPanel.add(instructionLabel, gbc);

        return deleteSimilitudPanel;
    }

    /**
     * Crea el panel de verificación del estado del sistema (Healthcheck).
     * Este panel contiene una etiqueta con un mensaje informativo y un botón
     * para ejecutar la acción de healthcheck del sistema.
     *
     * @return JPanel que representa el panel de healthcheck.
     */
    private JPanel createHealthcheckPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding for the whole panel

        String healthCheckText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<h2>Health Check: Missing Similarity Detection</h2>" +
                "<p>This health check verifies if any expected missing product similarity data.</p>" +
                "<p><b>How it works:</b></p>" +
                "<ul>" +
                "    <li>Compares existing similarities with the required pairs.</li>" +
                "    <li>Flags any missing similarities for review.</li>" +
                "</ul>" +
                "<p><b>Why it's useful?</b></p>" +
                "<ul>" +
                "    <li>Ensures that all similarities exists.</li>" +
                "    <li>Helps you find missing similarities.</li>" +
                "</ul>" +
                "</body>" +
                "</html>";
        JLabel healthLabel = new JLabel(healthCheckText );
        panel.add(new JScrollPane(healthLabel), BorderLayout.CENTER);

        healthcheck_Button = new JButton("Run Healthcheck");
        panel.add(healthcheck_Button, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Crea el panel de importación de productos.
     * Este panel contiene un área de texto donde se muestra información sobre la funcionalidad de importación
     * y un botón para activar la acción de importar productos.
     *
     * @return JPanel que representa el panel de importación de productos.
     */
    private JPanel createImportPanel() {
        JPanel importPanel = new JPanel(new BorderLayout());
        importPanel.setBorder(BorderFactory.createTitledBorder("Import Products"));

        String importText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<p>To import similarity data, the format needs to be the following:</p>" +
                "<pre><b>product1, product2, similarity_score</b></pre>" +
                "<hr>" +
                "<h3>Example:</h3>" +
                "<ul>" +
                "    <li><b>naranjas</b>, <b>uvas</b>, 0.2</li>" +
                "    <li><b>naranjas</b>, <b>peras</b>, 0.6</li>" +
                "    <li><b>uvas</b>, <b>kiwis</b>, 0.3</li>" +
                "</ul>" +
                "<hr>" +
                "<p>Each line must consist of two product names, <br>"+
                "separated by a comma, followed by the similarity score.</p>" +
                "</body>" +
                "</html>";

        JLabel importLabel = new JLabel(importText);

        // Use a JPanel with FlowLayout for top-left alignment
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));  // Align to top-left with some padding
        labelPanel.add(new JScrollPane(importLabel));

        // Add labelPanel to the top section of the BorderLayout
        importPanel.add(labelPanel, BorderLayout.NORTH);

        importAction_importButton = new JButton("Import");
        importPanel.add(importAction_importButton, BorderLayout.SOUTH);

        return importPanel;
    }


    /**
     * Crea el panel de exportación de productos.
     * Este panel contiene un área de texto que muestra detalles sobre la exportación,
     * un campo de texto para ingresar el nombre del archivo y un botón para ejecutar la acción de exportación.
     *
     * @return JPanel que representa el panel de exportación de productos.
     */
    private JPanel createExportPanel() {
        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export Products"));

        // File name field for the export action
        exportAction_filePathField = new JTextField(20);
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel for the file name
        fileNamePanel.add(new JLabel("File Name:"));
        fileNamePanel.add(exportAction_filePathField);
        exportPanel.add(fileNamePanel, BorderLayout.NORTH);

        String exportText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<p>The exported file will follow the format:</p>" +
                "<pre><b>product1, product2, similarity_score</b></pre>" +
                "<hr>" +
                "<h3>Example:</h3>" +
                "<ul>" +
                "    <li><b>naranjas</b>, <b>uvas</b>, 0.2</li>" +
                "    <li><b>naranjas</b>, <b>peras</b>, 0.6</li>" +
                "    <li><b>uvas</b>, <b>peras</b>, 0.3</li>" +
                "    <li><b>peras</b>, <b>naranjas</b>, 0.3</li>" +
                "</ul>" +
                "<hr>" +
                "<p>The format consists of: product names separated by a comma, <br>" +
                " followed by the similarity score.</p>" +
                "</body>" +
                "</html>";

        JLabel exportLabel = new JLabel(exportText);

        exportPanel.add(new JScrollPane(exportLabel), BorderLayout.CENTER);


        // Export button
        exportAction_exportButton = new JButton("Export");
        exportPanel.add(exportAction_exportButton, BorderLayout.SOUTH);

        return exportPanel;
    }

    // Panel switching methods
    /**
     * Muestra el panel del catálogo de similitudes.
     */
    public void showCatalogSimilitudPanel() {
        try {
            actionPerformed_buttonShowSimilitudsCatalog();
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Similarity");
        }catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra el panel para agregar similitudes.
     */
    private void showAddSimilitudPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "AddSimilarity");
        populateProductNames(add1ComboBox);
        populateProductNames(add2ComboBox);
    }

    /**
     * Muestra el panel para actualizar similitudes.
     */
    private void showUpdateSimilitudPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "UpdateSimilarity");
    }

    /**
     * Muestra el panel para eliminar similitudes.
     */
    private void showDeleteSimilitudPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "DeleteSimilarity");
        populateProductNames(delete1ComboBox);
        populateProductNames(delete2ComboBox);
    }

    /**
     * Muestra el panel de importación de productos.
     */
    private void showImportPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Import");
    }

    /**
     * Muestra el panel de exportación de productos.
     */
    private void showExportPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Export");
    }

    /**
     * Muestra el panel de healthcheck.
     */
    private void showHealthcheckPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Healthcheck");
    }

    // Listeners for the View
    /**
     * Inicia los oyentes de eventos para los botones de la interfaz.
     * Este método asigna acciones a los botones que permiten la interacción del usuario
     * con la interfaz, como mostrar el catálogo, agregar, actualizar, eliminar similitudes,
     * ejecutar healthcheck, importar y exportar productos.
     */
    private void startListeners() {

        // Listener for Show Catalog button
        refreshSimilitudsAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    actionPerformed_buttonShowSimilitudsCatalog();
                    JOptionPane.showMessageDialog(null, "Similarity Matrix refreshed!");
                }catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Listener for Add Similitud button
        AddSimilitudAction_addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonAddSimilitud(event);
            }
        });

        // Listener for Delete Similitud button
        deleteSimilitud_deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonDeleteSimilitud(event);
            }
        });

        // Listener for Healthcheck button
        healthcheck_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonHealthcheck(event);
            }
        });

        // Listener for Import button
        importAction_importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonImport(event);
            }
        });

        // Listener for Export button
        exportAction_exportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonExport(event);
            }
        });

    }

    //Action for Show Catalog
    /**
     * Acción ejecutada cuando el usuario hace clic en el botón para mostrar el catálogo de similitudes.
     * Recupera la lista de similitudes de productos desde el controlador y actualiza la tabla en la interfaz de usuario.
     *
     */
    public void actionPerformed_buttonShowSimilitudsCatalog() {

            // Retrieve the product catalog from the controller
            List<String[]> allProducts = controller.ctrlPresentation_getSimilituds();

            // Create a set of unique product names to build the matrix rows and columns
            Set<String> uniqueProducts = new TreeSet<>(); // TreeSet keeps products sorted
            for (String[] product : allProducts) {
                uniqueProducts.add(product[0]); // Add Product1
                uniqueProducts.add(product[1]); // Add Product2
            }

            // Convert the set to a list for indexed access
            List<String> productList = new ArrayList<>(uniqueProducts);
            int productCount = productList.size();

            // Create a similarity matrix (2D array)
            String[][] similarityMatrix = new String[productCount + 1][productCount + 1];

            // Initialize the first row and column with product names
            similarityMatrix[0][0] = "Product"; // Top-left corner
            for (int i = 0; i < productCount; i++) {
                similarityMatrix[0][i + 1] = productList.get(i); // Column headers
                similarityMatrix[i + 1][0] = productList.get(i); // Row headers
            }

            // Populate the matrix with similarity values
            for (String[] productPair : allProducts) {
                String product1 = productPair[0];
                String product2 = productPair[1];
                String similarity = productPair[2];

                int rowIndex = productList.indexOf(product1) + 1; // Find row index
                int colIndex = productList.indexOf(product2) + 1; // Find column index

                similarityMatrix[rowIndex][colIndex] = similarity; // Fill similarity value
                similarityMatrix[colIndex][rowIndex] = similarity; // Symmetric value
            }

            // Replace nulls with "-" or "0" for clarity
            for (int i = 1; i <= productCount; i++) {
                for (int j = 1; j <= productCount; j++) {
                    if (similarityMatrix[i][j] == null) {
                        similarityMatrix[i][j] = "-"; // Or "0"
                    }
                }
            }

            // Create a new table model for the matrix
            DefaultTableModel tableModel = new DefaultTableModel(similarityMatrix[0], 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make all cells non-editable
                }
            };

            // Add rows to the table model (skip the first row, which is the header)
            for (int i = 1; i < similarityMatrix.length; i++) {
                tableModel.addRow(similarityMatrix[i]);
            }

            // Update the JTable with the new model
            refreshAction_catalogSimilitudTable.setModel(tableModel);

            // Optionally refresh the table display
            refreshAction_catalogSimilitudTable.revalidate();
            refreshAction_catalogSimilitudTable.repaint();


    }


    //Action for Add Similitud
    /**
     * Acción ejecutada cuando el usuario hace clic en el botón para agregar una nueva similitud entre productos.
     * Recoge los valores de los campos de entrada, valida y crea una nueva similitud.
     *
     * @param event El evento generado al hacer clic en el botón.
     */
    public void actionPerformed_buttonAddSimilitud(ActionEvent event) {

        try {
            String product1 = (String) add1ComboBox.getSelectedItem();
            String product2 = (String) add2ComboBox.getSelectedItem();
            String similarity = controller.ctrlP_checkInput(AddSimilitudAction_similarityField.getText(), 3);

            float price = Float.parseFloat(similarity);
            controller.ctrlPresentation_createSimilituds(product1, product2, price);
            JOptionPane.showMessageDialog(this, "Similarity added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            populateProductNames(add1ComboBox);
            populateProductNames(add2ComboBox);
            AddSimilitudAction_similarityField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Action for Delete Similitud
    /**
     * Acción ejecutada cuando el usuario hace clic en el botón para eliminar una similitud entre dos productos.
     * Recoge los valores de los campos de entrada y elimina la similitud correspondiente.
     *
     * @param event El evento generado al hacer clic en el botón.
     */
    public void actionPerformed_buttonDeleteSimilitud(ActionEvent event) {

        try {

            String product1 = (String) delete1ComboBox.getSelectedItem();
            String product2 = (String) delete2ComboBox.getSelectedItem();

            controller.ctrlPresentation_deleteSimilituds(product1, product2);
            JOptionPane.showMessageDialog(this, "Similarity deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            populateProductNames(delete1ComboBox);
            populateProductNames(delete2ComboBox);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Action for Healthcheck
    /**
     * Acción ejecutada cuando el usuario hace clic en el botón para realizar un healthcheck de las similitudes.
     * Ejecuta una comprobación de la integridad de las similitudes en el sistema.
     *
     * @param event El evento generado al hacer clic en el botón.
     */
    public void actionPerformed_buttonHealthcheck(ActionEvent event) {
        try {
            controller.ctrlPresentation_healthCheckSimilituds();
            JOptionPane.showMessageDialog(this, "Healthcheck completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //Action for Import
    /**
     * Acción ejecutada cuando el usuario hace clic en el botón para importar similitudes desde un archivo.
     * Abre un diálogo para seleccionar un archivo y luego importa las similitudes.
     *
     * @param event El evento generado al hacer clic en el botón.
     */
    public void actionPerformed_buttonImport(ActionEvent event) {
        try {
            FileBrowser fileBrowser = new FileBrowser();
            // Open a file browser dialog
            String filePath = fileBrowser.browse(controller.getFrame(), false);
            if (filePath != null) {
                controller.ctrlPresentation_importSimilituds(filePath);
                JOptionPane.showMessageDialog(this, "Similituds imported successfully from" + filePath + " !", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No file selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    //Action for Export
    /**
     * Acción ejecutada cuando el usuario hace clic en el botón para exportar similitudes a un archivo.
     * Abre un diálogo para seleccionar una carpeta y luego exporta las similitudes a un archivo especificado.
     *
     * @param event El evento generado al hacer clic en el botón.
     */
    public void actionPerformed_buttonExport(ActionEvent event) {

        try {
            String name = controller.ctrlP_checkfile(exportAction_filePathField.getText());

            // Open a folder browser dialog
            FileBrowser fileBrowser = new FileBrowser();
            String folderPath = fileBrowser.browse(controller.getFrame(), true);

            if (folderPath != null) {
                // Combine folder path and file name to create the full file path
                String fullPath = folderPath + File.separator + name;

                controller.ctrlPresentation_exportSimilituds(fullPath);

                JOptionPane.showMessageDialog(controller.getFrame(),
                        "Similarities exported successfully to " + fullPath + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No folder selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(controller.getFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Puebla el JComboBox con los nombres de los productos.
     *
     * Este método obtiene la lista de productos desde el controlador y agrega los nombres de los productos al JComboBox.
     * Si ocurre un error al obtener la lista de productos, se puede mostrar un mensaje de error.
     * @param nameComboBox El JComboBox que se va a poblar con los nombres de los productos.
     */
    private void populateProductNames(JComboBox<String> nameComboBox) {
        nameComboBox.removeAllItems(); // Clear previous items
        try{
            List<String[]> products = controller.ctrlPresentation_getCatalog(); // Call your function to retrieve product data

            for (String[] product : products) {
                nameComboBox.addItem(product[0]); // Add product name to combo box
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}









