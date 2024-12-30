package edu.upc.prop.clusterxx.forms;

import edu.upc.prop.clusterxx.CSSUtils;
import edu.upc.prop.clusterxx.Estadisticas;
import edu.upc.prop.clusterxx.controller.CtrlPresentation;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * Esta clase representa la vista del estante (shelve), gestionando diferentes paneles y acciones de la interfaz de usuario.
 * Proporciona métodos para inicializar el estante, mostrarlo, intercambiar productos, agregar o eliminar productos,
 * calcular distribuciones y gestionar estadísticas, importación y exportación de datos.
 */
public class shelveView extends JPanel {

    // Dynamic content panel
    /**
     * Panel principal de contenido dinámico que utiliza un CardLayout para gestionar y mostrar
     * diferentes pantallas o vistas dentro de la interfaz de usuario.
     *
     * @see CardLayout
     */
    private JPanel contentPanel; // Main dynamic content panel (CardLayout)

    /**
     * ComboBox para seleccionar el nombre del producto 1 para intercambiar en shelve
     */
    private JComboBox<String> exchange1ComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto 2 intercambiar en shelve
     */
    private JComboBox<String> exchange2ComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto 1 para eliminar similitud
     */
    private JComboBox<String> deleteComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto 2 para eliminar similitud
     */
    private JComboBox<String> addComboBox ;
    /**
     * Campo de texto donde el usuario ingresa el número de filas para la inicialización de estantes.
     * Este campo es utilizado por el panel para definir las dimensiones de los estantes a crear.
     */
    private JTextField InitShelveAction_RowField;
    /**
     * Campo de texto donde el usuario ingresa el número de columnas para la inicialización de estantes.
     * Este campo es utilizado por el panel para definir las dimensiones de los estantes a crear.
     */
    private JTextField InitShelveAction_ColField;
    /**
     * Botón de acción para inicializar el estante con las dimensiones proporcionadas por el usuario.
     */
    private JButton InitShelveAction_Button;

    // Show Shelve panel components
    /**
     * Panel que muestra la tabla de productos en el estante.
     * Este panel se actualiza dinámicamente con los productos actuales en el estante.
     */
    private JPanel refreshAction_catalogShelveTable;
    /**
     * Botón de acción para actualizar la tabla de productos en el estante.
     */
    private JButton refreshShelveAction_Button;

    /**
     * Botón de acción para intercambiar dos productos en el estante.
     */
    private JButton ExchangeShelveAction_modifyButton;

    // Add Product to Shelve panel components
    /**
     * Botón de acción para agregar un producto al estante.
     */
    private JButton AddProductShelveAction_addButton;

    // Delete Product from Shelve panel components
    /**
     * Botón de acción para eliminar un producto del estante.
     */
    private JButton DeleteProductShelveAction_deleteButton;

    // Calculate Distribution panel components
    /**
     * Botón de acción para calcular la distribución de productos utilizando el algoritmo 1.
     */
    private JButton calculateAction_algorithm1Button;
    /**
     * Botón de acción para calcular la distribución de productos utilizando el algoritmo 2.
     */
    private JButton calculateAction_algorithm2Button;
    /**
     * Botón de acción para calcular la distribución de productos utilizando el algoritmo 3.
     */
    private JButton calculateAction_algorithm3Button;

    // Stats
    /**
     * Paneles que contienen las tablas utilizadas en el historial de estadísticas. Cada panel corresponde
     * a una tabla específica que muestra información sobre las estadísticas de los productos o acciones.
     */
    private JPanel table1Panel, table2Panel, table3Panel, table4Panel, table5Panel, table6Panel, table7Panel, table8Panel, table9Panel;
    /**
     * Botón de acción para mostrar el historial de estadísticas.
     */
    JButton statisticsHistoryButton;
    /**
     * Botón de acción para restablecer todas las tablas de estadísticas.
     */
    JButton resetAllButton;
    // Import Shelve panel components
    /**
     * Botón que ejecuta la acción de importación de productos desde un archivo o fuente externa
     * para agregarlos al sistema de estantes.
     */
    private JButton importAction_importButton;

    // Export Shelve panel components
    /**
     * Campo de texto donde el usuario ingresa la ruta del archivo de destino para exportar los productos del estante.
     */
    private JTextField exportAction_filePathField;
    /**
     * Botón que ejecuta la acción de exportación de productos del estante a un archivo o destino externo.
     */
    private JButton exportAction_exportButton;

    // Reference to controller
    /**
     * Controlador para gestionar la lógica de la presentación y las acciones de la interfaz de usuario.
     */
    private CtrlPresentation controller;

    /**
     * Constructor que inicializa la vista del estante.
     *
     * @param controller El controlador para gestionar la lógica de la presentación.
     */
    public shelveView(CtrlPresentation controller) {

        this.controller = controller;

        // Initialize the UI
        initUI();
    }

    /**
     * Inicializa la interfaz de usuario creando y organizando todos los componentes gráficos.
     */
    private void initUI() {

        setLayout(new BorderLayout()); // Add spacing between regions
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Outer margin

        // Create the top menu
        JMenuBar topMenu = new JMenuBar();
        JMenu catalogMenu = new JMenu("Catalog");
        JMenu similitudesMenu = new JMenu("Similarity");
        JMenu shelveMenu = new JMenu("Shelf");
        Color cf = new Color(UIManager.getColor("Label.foreground").getRGB());
        Color cb = new Color(UIManager.getColor("Label.background").getRGB());
        shelveMenu.setBackground(cf);
        shelveMenu.setForeground(cb);
        shelveMenu.setOpaque(true);

        topMenu.add(catalogMenu);
        topMenu.add(similitudesMenu);
        topMenu.add(shelveMenu);

        catalogMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.sincronizacionCatalog();
            }
        });

        similitudesMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.sincronizacionSimilitud();
            }
        });

        add(topMenu, BorderLayout.NORTH); // Add the top menu to the main layout

        // Create the right-side menu
        JPanel menuPanel = new JPanel(new GridLayout(7, 1, 10, 10)); // Vertical buttons with spacing
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create menu buttons
        JButton initializeShelveButton = new JButton("Initialize Shelf");
        initializeShelveButton.addActionListener(e -> showinitializeShelvePanel());

        JButton showShelveButton = new JButton("Show Shelf");
        showShelveButton.addActionListener(e -> showShowShelvePanel());

        JButton exchangeProductsButton = new JButton("Exchange Products");
        exchangeProductsButton.addActionListener(e -> showExchangeProductsPanel());

        JButton addProductShelveButton = new JButton("Add Product to Shelve");
        addProductShelveButton.addActionListener(e -> showAddProductShelvePanel());

        JButton deleteProductShelveButton = new JButton("Delete Product from Shelve");
        deleteProductShelveButton.addActionListener(e -> showDeleteProductShelvePanel());

        JButton calculateDistributionButton = new JButton("Calculate Distribution");
        calculateDistributionButton.addActionListener(e -> showCalculateDistributionPanel());

        statisticsHistoryButton = new JButton("Statistics History");
        statisticsHistoryButton.addActionListener(e -> showStatisticsHistoryPanel());

        JButton importShelveButton = new JButton("Import Shelf");
        importShelveButton.addActionListener(e -> showImportShelvePanel());

        JButton exportShelveButton = new JButton("Export Shelve");
        exportShelveButton.addActionListener(e -> showExportShelvePanel());

        // Add buttons to the menu
        menuPanel.add(initializeShelveButton);
        menuPanel.add(showShelveButton);
        menuPanel.add(exchangeProductsButton);
        menuPanel.add(addProductShelveButton);
        menuPanel.add(deleteProductShelveButton);
        menuPanel.add(calculateDistributionButton);
        menuPanel.add(statisticsHistoryButton);
        menuPanel.add(importShelveButton);
        menuPanel.add(exportShelveButton);

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
        contentPanel.add(createInitializeShelvePanel(), "initializeShelf");
        contentPanel.add(createShowShelvePanel(), "ShowShelf");
        contentPanel.add(createExchangeProductsPanel(), "ExchangeProducts");
        contentPanel.add(createAddProductShelvePanel(), "AddProductShelf");
        contentPanel.add(createDeleteProductShelvePanel(), "DeleteProductShelf");
        contentPanel.add(createCalculateDistributionPanel(), "CalculateDistribution");
        contentPanel.add(createStatisticsHistoryPanel(), "StatisticsHistory");
        contentPanel.add(createImportShelvePanel(), "ImportShelf");
        contentPanel.add(createExportShelvePanel(), "ExportShelf");

        // Show the Initialize Shelve panel by default
        showinitializeShelvePanel();
        startListeners();

    }


    /**
     * Crea el panel para inicializar el estante con la configuración de filas y columnas.
     *
     * @return Un panel para inicializar el estante.
     */
    private JPanel createInitializeShelvePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Initialize Shelf"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        InitShelveAction_RowField = new JTextField(20);
        InitShelveAction_ColField = new JTextField(20);
        InitShelveAction_Button = new JButton("Initialize Shelf");

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Number of Rows:"), gbc);
        gbc.gridx = 1;
        panel.add(InitShelveAction_RowField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Number of Columns:"), gbc);
        gbc.gridx = 1;
        panel.add(InitShelveAction_ColField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(InitShelveAction_Button, gbc);

        return panel;
    }

    /**
     * Crea el panel para mostrar el contenido del estante.
     *
     * @return Un panel que muestra el contenido del estante.
     */
    private JPanel createShowShelvePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Show Shelf"));

        // Panel to represent the shelve grid
        refreshAction_catalogShelveTable = new JPanel();
        refreshAction_catalogShelveTable.setLayout(new GridLayout()); // Initially empty, rows/cols set dynamically
        panel.add(new JScrollPane(refreshAction_catalogShelveTable), BorderLayout.CENTER);

        // Refresh button
        refreshShelveAction_Button = new JButton("Refresh");
        panel.add(refreshShelveAction_Button, BorderLayout.SOUTH);

        return panel;
    }


    /**
     * Crea el panel para intercambiar productos dentro del estante.
     *
     * @return Un panel para intercambiar productos.
     */
    private JPanel createExchangeProductsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Exchange Products"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        exchange1ComboBox = new JComboBox<>();
        exchange2ComboBox = new JComboBox<>();

        ExchangeShelveAction_modifyButton = new JButton("Exchange Products");

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Product 1 Name:"), gbc);
        gbc.gridx = 1;
        panel.add(exchange1ComboBox , gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Product 2 Name:"), gbc);
        gbc.gridx = 1;
        panel.add( exchange2ComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(ExchangeShelveAction_modifyButton, gbc);

        return panel;
    }

    /**
     * Crea un panel para agregar un producto al estante.
     * Este panel contiene un campo de texto para ingresar el nombre del producto y un botón para agregar el producto al estante.
     *
     * @return JPanel - El panel que contiene el campo de texto para el nombre del producto y el botón "Agregar Producto".
     */
    private JPanel createAddProductShelvePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Add Product to Shelf"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComboBox = new JComboBox<>();
        AddProductShelveAction_addButton = new JButton("Add Product");

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        panel.add(addComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(AddProductShelveAction_addButton, gbc);

        return panel;
    }

    /**
     * Crea un panel para eliminar un producto del estante.
     * Este panel contiene un campo de texto para ingresar el nombre del producto y un botón para eliminar el producto del estante.
     *
     * @return JPanel - El panel que contiene el campo de texto para el nombre del producto y el botón "Eliminar Producto".
     */
    private JPanel createDeleteProductShelvePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Delete Product from Shelf"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        deleteComboBox = new JComboBox<>();
        DeleteProductShelveAction_deleteButton = new JButton("Delete Product");

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        panel.add(deleteComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(DeleteProductShelveAction_deleteButton, gbc);

        return panel;
    }

    /**
     * Crea un panel para calcular la distribución de productos.
     * Este panel contiene tres algoritmos (con sus respectivos botones) y las descripciones correspondientes para cada uno.
     *
     * @return JPanel - El panel que contiene los botones para calcular la distribución utilizando tres algoritmos diferentes.
     */
    private JPanel createCalculateDistributionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Calculate Distribution"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Explanation 1 and Button 1
        JLabel explanationLabel1 = new JLabel("<html><strong>Hill Climbing Algorithm:</strong><br>Calculates de distribution using the Hill Climbing approximation.</html>");
        gbc.gridx = 0;
        gbc.gridy = 0;
        //gbc.gridwidth = 2;
        explanationLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(explanationLabel1, gbc);

        calculateAction_algorithm1Button = new JButton("Fast Algorithm");
        gbc.gridx = 0;
        gbc.gridy = 1;
       // gbc.gridwidth = 2;  // Reset grid width for the button
        panel.add(calculateAction_algorithm1Button, gbc);

        // Explanation 2 and Button 2
        JLabel explanationLabel2 = new JLabel("<html><strong>BackTracking Algorithm:</strong><br>Explores all distributions and finds the best one by backtracking.</html>");
        gbc.gridx = 0;
        gbc.gridy = 2;
        explanationLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(explanationLabel2, gbc);

        calculateAction_algorithm2Button = new JButton("Slow Algorithm");
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(calculateAction_algorithm2Button, gbc);

        // Explanation 3 and Button 3
        JLabel explanationLabel3 = new JLabel("<html><strong>Simplified Simulated Annealing Algorithm:</strong><br>Calculates the distribution with more probability</html>");
        gbc.gridx = 0;
        gbc.gridy = 4;
        explanationLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(explanationLabel3, gbc);

        calculateAction_algorithm3Button = new JButton("Random Algorithm");
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(calculateAction_algorithm3Button, gbc);


        return panel;
    }

    /**
     * Crea un panel para mostrar el historial de estadísticas.
     * Este panel contiene botones principales para elegir entre diferentes opciones, junto con un área para mostrar tablas relacionadas con cada opción seleccionada.
     *
     * @return JPanel - El panel que contiene botones para seleccionar opciones de estadísticas y tablas correspondientes.
     */
    private JPanel createStatisticsHistoryPanel() {
        // Parent panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Statistics History"));

        // Top section panel for main buttons and child buttons
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Small gaps between components
        gbc.fill = GridBagConstraints.NONE; // Keep buttons compact
        gbc.anchor = GridBagConstraints.WEST;

        // Create main buttons
        JButton fastButton = new JButton("Fast");
        JButton slowButton = new JButton("Slow");
        JButton thirdButton = new JButton("Random");

        // Standardize button size for main buttons
        Dimension mainButtonSize = new Dimension(100, 30); // Fixed width and height
        fastButton.setPreferredSize(mainButtonSize);
        slowButton.setPreferredSize(mainButtonSize);
        thirdButton.setPreferredSize(mainButtonSize);

        // Create child panel for the submenu buttons
        JPanel childPanel = new JPanel(new CardLayout());
        childPanel.setPreferredSize(new Dimension(420, 80)); // Fixed size for submenu area
        childPanel.setVisible(false); // Initially hidden

        // Submenu panels for each button
        JPanel fastChildPanel = createChildPanel("Fast");
        JPanel slowChildPanel = createChildPanel("Slow");
        JPanel thirdChildPanel = createChildPanel("Random");

        // Add child panels to CardLayout
        childPanel.add(fastChildPanel, "Fast");
        childPanel.add(slowChildPanel, "Slow");
        childPanel.add(thirdChildPanel, "Random");

        // Add action listeners to toggle the correct submenu panel
        fastButton.addActionListener(e -> {
            ((CardLayout) childPanel.getLayout()).show(childPanel, "Fast");
            childPanel.setVisible(true);
        });

        slowButton.addActionListener(e -> {
            ((CardLayout) childPanel.getLayout()).show(childPanel, "Slow");
            childPanel.setVisible(true);
        });

        thirdButton.addActionListener(e -> {
            ((CardLayout) childPanel.getLayout()).show(childPanel, "Random");
            childPanel.setVisible(true);
        });

        // Layout main buttons in a row
        gbc.gridx = 0;
        gbc.gridy = 0;
        topPanel.add(fastButton, gbc);

        gbc.gridx = 1;
        topPanel.add(slowButton, gbc);

        gbc.gridx = 2;
        topPanel.add(thirdButton, gbc);

        // Add submenu panel directly below the main buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Span across all three buttons
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topPanel.add(childPanel, gbc);

        // Placeholder panel for the table area with CardLayout
        JPanel tablePanel = new JPanel(new CardLayout());
        tablePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Initialize the nine table placeholders
        table1Panel = createTablePanel("Empty Statistics Fast by Time");
        table2Panel = createTablePanel("Empty Statistics Fast by Cost");
        table3Panel = createTablePanel("Empty Statistics Fast by Iterations");
        table4Panel = createTablePanel("Empty Statistics Slow by Time");
        table5Panel = createTablePanel("Empty Statistics Slow by Cost");
        table6Panel = createTablePanel("Empty Statistics Slow by Iterations");
        table7Panel = createTablePanel("Empty Statistics Third by Time");
        table8Panel = createTablePanel("Empty Statistics Third by Cost");
        table9Panel = createTablePanel("Empty Statistics Third by Iterations");

        JScrollPane scroll1 = new JScrollPane(table1Panel);
        JScrollPane scroll2 = new JScrollPane(table2Panel);
        JScrollPane scroll3 = new JScrollPane(table3Panel);
        JScrollPane scroll4 = new JScrollPane(table4Panel);
        JScrollPane scroll5 = new JScrollPane(table5Panel);
        JScrollPane scroll6 = new JScrollPane(table6Panel);
        JScrollPane scroll7 = new JScrollPane(table7Panel);
        JScrollPane scroll8 = new JScrollPane(table8Panel);
        JScrollPane scroll9 = new JScrollPane(table9Panel);

        // Add the placeholders to the table panel
        tablePanel.add(scroll1, "Table1");
        tablePanel.add(scroll2, "Table2");
        tablePanel.add(scroll3, "Table3");
        tablePanel.add(scroll4, "Table4");
        tablePanel.add(scroll5, "Table5");
        tablePanel.add(scroll6, "Table6");
        tablePanel.add(scroll7, "Table7");
        tablePanel.add(scroll8, "Table8");
        tablePanel.add(scroll9, "Table9");

        // Update child panel buttons to show respective tables
        updateChildPanelActions(fastChildPanel, tablePanel, 1);
        updateChildPanelActions(slowChildPanel, tablePanel, 4);
        updateChildPanelActions(thirdChildPanel, tablePanel, 7);

        // Reset All button
        resetAllButton = new JButton("Reset All");


        // Create bottom panel for the Reset All button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.add(resetAllButton);

        // Assemble the main layout
        panel.add(topPanel, BorderLayout.NORTH);  // Top section with buttons
        panel.add(tablePanel, BorderLayout.CENTER);  // Table area
        panel.add(bottomPanel, BorderLayout.SOUTH);  // Bottom section for Reset All button

        return panel;
    }


    /**
     * Metodo auxiliar para crear paneles hijos dinámicamente.
     * Este panel contiene botones para ordenar productos por tiempo, costo e iteraciones,
     * y una etiqueta de retroalimentación que se actualiza según la selección del usuario.
     *
     * @param type El tipo de panel hijo que se está creando, utilizado en el mensaje de retroalimentación.
     * @return JPanel - El panel hijo con botones y una etiqueta de retroalimentación.
     */
    private JPanel createChildPanel(String type) {
        JPanel childPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 1, 1); // Small gaps between components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Submenu buttons
        JButton timeButton = new JButton("Time Ordered");
        JButton costButton = new JButton("Cost Ordered");
        JButton iterationsButton = new JButton("Iterations Ordered");

        // Standardize child button size
        Dimension childButtonSize = new Dimension(150, 30); // Consistent width and height
        timeButton.setPreferredSize(childButtonSize);
        costButton.setPreferredSize(childButtonSize);
        iterationsButton.setPreferredSize(childButtonSize);

        // Feedback label for the child panel
        JLabel feedbackLabel = new JLabel(type + " - Select an option", SwingConstants.LEFT);
        feedbackLabel.setFont(new Font("Lable.font", Font.ITALIC, 10));

        // Layout for smaller buttons in a row
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST; // Align everything to the left
        childPanel.add(timeButton, gbc);

        gbc.gridx++;
        childPanel.add(costButton, gbc);

        gbc.gridx++;
        childPanel.add(iterationsButton, gbc);

        // Add feedback below the buttons
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;  // Span across all three buttons
        childPanel.add(feedbackLabel, gbc);

        // Add action listeners to update feedback
        timeButton.addActionListener(e -> feedbackLabel.setText(type + " - Ordered by Time selected."));
        costButton.addActionListener(e -> feedbackLabel.setText(type + " - Ordered by Cost selected."));
        iterationsButton.addActionListener(e -> feedbackLabel.setText(type + " - Ordered by Iterations selected."));

        return childPanel;
    }

    /**
     * Metodo auxiliar para crear paneles individuales de tabla.
     * Este panel contiene una etiqueta centrada que muestra el texto proporcionado como parámetro.
     *
     * @param labelText El texto que se mostrará en la etiqueta centrada del panel.
     * @return JPanel - El panel de tabla con la etiqueta correspondiente.
     */
    private JPanel createTablePanel(String labelText) {
        JPanel tablePanel = new JPanel(new BorderLayout());
        JLabel tableLabel = new JLabel(labelText, SwingConstants.CENTER);
        tablePanel.add(tableLabel, BorderLayout.CENTER);
        return tablePanel;
    }

    /**
     * Metodo auxiliar para actualizar las acciones de los botones en un panel hijo.
     * Cada botón en el panel hijo está vinculado a un panel de tabla correspondiente.
     * Cuando un botón es presionado, se muestra el panel de tabla correspondiente en el panel de contenido.
     *
     * @param childPanel El panel hijo que contiene los botones.
     * @param tablePanel El panel de tablas donde se mostrarán las tablas correspondientes.
     * @param startIndex El índice inicial para calcular qué tabla mostrar.
     */
    private void updateChildPanelActions(JPanel childPanel, JPanel tablePanel, int startIndex) {
        Component[] components = childPanel.getComponents();
        if (components.length >= 3) {
            JButton timeButton = (JButton) components[0];
            JButton costButton = (JButton) components[1];
            JButton iterationsButton = (JButton) components[2];

            timeButton.addActionListener(e -> ((CardLayout) tablePanel.getLayout()).show(tablePanel, "Table" + startIndex));
            costButton.addActionListener(e -> ((CardLayout) tablePanel.getLayout()).show(tablePanel, "Table" + (startIndex + 1)));
            iterationsButton.addActionListener(e -> ((CardLayout) tablePanel.getLayout()).show(tablePanel, "Table" + (startIndex + 2)));
        }
    }


    /**
     * Metodo que crea un panel para importar productos en estantes.
     * Este panel contiene un área de texto para mostrar detalles de la importación
     * y un botón para ejecutar la acción de importación.
     *
     * @return JPanel - El panel para importar productos, con un área de texto y un botón de importación.
     */
    private JPanel createImportShelvePanel() {
        JPanel importPanel = new JPanel(new BorderLayout());
        importPanel.setBorder(BorderFactory.createTitledBorder("Import Products"));


        String importText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<div class='content'>" +
                "<p>To import shelf data, use a text file with the following format:</p>" +
                "<pre><b>shelf_id, aisle_id, product_list</b></pre>" +
                "<hr>" +
                "<h3>Example:</h3>" +
                "<ul>" +
                "    <li><b>1</b>, <b>3</b>, peras;manzanas;platanos</li>" +
                "</ul>" +
                "<hr>" +
                "<h4>Each line represents:</h4>" +
                "<ul>" +
                "    <li><b>shelf_id</b>: The unique identifier for the shelf.</li>" +
                "    <li><b>aisle_id</b>: The identifier for the aisle where the shelf is located.</li>" +
                "    <li><b>product_list</b>: A semicolon-separated list of product names on the shelf.</li>" +
                "</ul>" +
                "<hr>" +
                "</div>" +
                "</body>" +
                "</html>";



        JLabel importLabel = new JLabel(importText);

        importPanel.add(new JScrollPane(importLabel), BorderLayout.CENTER);

        importAction_importButton = new JButton("Import");
        importPanel.add(importAction_importButton, BorderLayout.SOUTH);

        return importPanel;
    }

    /**
     * Metodo que crea un panel para exportar productos desde estantes.
     * Este panel contiene un área de texto para mostrar detalles de la exportación,
     * un campo de texto para especificar el nombre del archivo y un botón para ejecutar la acción de exportación.
     *
     * @return JPanel - El panel para exportar productos, con un área de texto, un campo para el nombre del archivo y un botón de exportación.
     */
    private JPanel createExportShelvePanel() {
        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export Products"));

        String exportText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<p>The exported file will follow the format:</p>" +
                "<pre><b>shelf_id, aisle_id, product_list</b></pre>" +
                "<hr>" +
                "<h3>Example:</h3>" +
                "<ul>" +
                "    <li><b>1</b>, <b>3</b>, peras;manzanas;platanos</li>" +
                "</ul>" +
                "<hr>" +
                "<h4>Each line represents:</h4>" +
                "<ul>" +
                "    <li><b>shelf_id</b>: The unique identifier for the shelf.</li>" +
                "    <li><b>aisle_id</b>: The identifier for the aisle where the shelf is located.</li>" +
                "    <li><b>product_list</b>: A semicolon-separated list of product names on the shelf.</li>" +
                "</ul>" +
                "<hr>" +
                "</body>" +
                "</html>";

        JLabel exportLabel = new JLabel(exportText);
        exportPanel.add(new JScrollPane(exportLabel), BorderLayout.CENTER);

        // File name field for the export action
        exportAction_filePathField = new JTextField(20);
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel for the file name
        fileNamePanel.add(new JLabel("File Name:"));
        fileNamePanel.add(exportAction_filePathField);
        exportPanel.add(fileNamePanel, BorderLayout.NORTH);

        // Export button
        exportAction_exportButton = new JButton("Export");
        exportPanel.add(exportAction_exportButton, BorderLayout.SOUTH);

        return exportPanel;
    }
    /**
     * Metodo para mostrar el panel de inicialización de estantes.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de inicialización de estantes.
     */
    public void showinitializeShelvePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "initializeShelf");
    }
    /**
     * Metodo para mostrar el panel de visualización de estantes.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de visualización de estantes.
     */
    private void showShowShelvePanel() {
            actionPerformed_buttonShowShelve();
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ShowShelf");
    }
    /**
     * Metodo para mostrar el panel de intercambio de productos.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de intercambio de productos.
     */
    private void showExchangeProductsPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ExchangeProducts");
        populateProductNames(exchange1ComboBox);
        populateProductNames(exchange2ComboBox);
    }
    /**
     * Metodo para mostrar el panel de agregar productos a estantes.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de agregar productos a estantes.
     */
    private void showAddProductShelvePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "AddProductShelf");
        populateProductNames(addComboBox);
    }
    /**
     * Metodo para mostrar el panel de eliminar productos de estantes.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de eliminar productos de estantes.
     */
    private void showDeleteProductShelvePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "DeleteProductShelf");
        populateProductNames(deleteComboBox);
    }
    /**
     * Metodo para mostrar el panel de cálculo de distribuciones.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de cálculo de distribuciones.
     */
    private void showCalculateDistributionPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "CalculateDistribution");
    }
    /**
     * Metodo para mostrar el panel de historial de estadísticas.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de historial de estadísticas.
     */
    private void showStatisticsHistoryPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "StatisticsHistory");
    }
    /**
     * Metodo para mostrar el panel de importación de estantes.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de importación de estantes.
     */
    private void showImportShelvePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ImportShelf");
    }
    /**
     * Metodo para mostrar el panel de exportación de estantes.
     * Este metodo cambia la vista actual del panel de contenido para mostrar el panel de exportación de estantes.
     */
    private void showExportShelvePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "ExportShelf");
    }

    /**
     * Metodo para iniciar los escuchadores de eventos de los botones.
     * Este metodo agrega escuchadores de eventos a los botones de la interfaz de usuario para manejar las acciones correspondientes.
     */
    private void startListeners() {
        InitShelveAction_Button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonInitShelve(event);
                    }
                });

        refreshShelveAction_Button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonShowShelve();
                    }
                });

        ExchangeShelveAction_modifyButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                       actionPerformed_buttonExchangeProducts(event);
                    }
                });

        AddProductShelveAction_addButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonAddProduct(event);
                    }
                });

        DeleteProductShelveAction_deleteButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonDeleteProduct(event);
                    }
                });

        calculateAction_algorithm1Button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonAlgorithm1(event);
                    }
                });

        calculateAction_algorithm2Button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonAlgorithm2(event);
                    }
                });

        calculateAction_algorithm3Button.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonAlgorithm3(event);
                    }
                });

        statisticsHistoryButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonStatistics(event);
                    }
                });

        resetAllButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonResetAllHistory(event);
                    }
                });

        importAction_importButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonImport(event);
                    }
                });

        exportAction_exportButton.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent event) {
                        actionPerformed_buttonExport(event);
                    }
                });
    }

    /**
     * Metodo para manejar la acción del botón de inicialización de estantes.
     * Este metodo recupera los valores de las filas y columnas del estante y llama al controlador para inicializar el estante.
     *
     * @param event El evento de acción del botón de inicialización de estantes.
     */
    public void actionPerformed_buttonInitShelve(ActionEvent event) {
        try {
            int rows = Integer.parseInt(controller.ctrlP_checkInput(InitShelveAction_RowField.getText(), 0));
            int cols = Integer.parseInt(controller.ctrlP_checkInput(InitShelveAction_ColField.getText(), 0));

            controller.ctrlPresentation_InitializeShelve(rows, cols);

            JOptionPane.showMessageDialog(this, "Shelf Initialized Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            InitShelveAction_RowField.setText("");
            InitShelveAction_ColField.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Metodo para manejar la acción del botón de mostrar estantes.
     * Este metodo llama al controlador para mostrar el estante en la interfaz de usuario.
     *
     */
    public void actionPerformed_buttonShowShelve() {
        try {
            // Fetch the shelve object as an Object[]
            Object[] shelveData = controller.ctrlPresentation_ShowShelve();

            // Parse rows and columns to integers
            int rows = Integer.parseInt(shelveData[0].toString()); // Convert to string and parse as integer
            int columns = Integer.parseInt(shelveData[1].toString()); // Convert to string and parse as integer

            // Safely cast the third element to ArrayList<String>
            ArrayList<String> distribution = (ArrayList<String>) shelveData[2];

            // Fetch cost from the controller
            String cost = controller.ctrlPresentation_GetCostShelve();

            String numIters = controller.ctrlPresentation_GetIterLastAlgo();

            // Clear the panel and update its layout
            refreshAction_catalogShelveTable.removeAll(); // Clear existing components
            refreshAction_catalogShelveTable.setLayout(new BorderLayout()); // Use BorderLayout for layout flexibility

            // Create a sub-panel for the grid layout
            JPanel gridPanel = new JPanel();
            gridPanel.setLayout(new GridLayout(rows, columns, 5, 5)); // Match rows and columns

            // Populate the panel with product labels
            int index = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    JLabel cellLabel = new JLabel();
                    if (index < distribution.size()) {
                        cellLabel.setText(distribution.get(index)); // Set the product name
                    } else {
                        cellLabel.setText(""); // Empty cell for missing products
                    }
                    cellLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align text
                    cellLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add a border to the cell
                    cellLabel.setOpaque(true); // Enable background color
                    cellLabel.setForeground(Color.BLACK);
                    cellLabel.setFont(new Font(Font.DIALOG,Font.BOLD,16));
                    cellLabel.setBackground(Color.LIGHT_GRAY); // Set cell background color
                    gridPanel.add(cellLabel); // Add label to the grid
                    index++;
                }
            }

            // Add the grid panel to the center of the layout
            refreshAction_catalogShelveTable.add(gridPanel, BorderLayout.CENTER);

            // Add a label to display the cost of the shelve
            JLabel currentDistLable = new JLabel("Cost: " + cost + " Iterations: " + numIters);
            currentDistLable.setHorizontalAlignment(SwingConstants.CENTER);
            currentDistLable.setFont(new Font("Lable.font", Font.BOLD, 14));
            refreshAction_catalogShelveTable.add(currentDistLable, BorderLayout.SOUTH); // Place it at the bottom

            // Refresh the panel to reflect changes
            refreshAction_catalogShelveTable.revalidate(); // Update layout
            refreshAction_catalogShelveTable.repaint(); // Repaint to show changes

        } catch (Exception e) {
            refreshAction_catalogShelveTable.removeAll(); // Clear existing components
            refreshAction_catalogShelveTable.repaint(); // Repaint to show changes
            // Show an error dialog in case of exceptions
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    /**
     * Metodo para manejar la acción del botón de intercambio de productos.
     * Este metodo recupera los nombres de los productos y llama al controlador para intercambiar los productos en el estante.
     *
     * @param event El evento de acción del botón de intercambio de productos.
     */
    public void actionPerformed_buttonExchangeProducts(ActionEvent event) {
        try {
            String product1 = (String) exchange1ComboBox.getSelectedItem();
            String product2 = (String) exchange2ComboBox.getSelectedItem();
            controller.ctrlPresentation_ModifyManuallyShelve(product1, product2);
            JOptionPane.showMessageDialog(this, "Modification done!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de agregar productos al estante.
     * Este metodo recupera el nombre del producto y llama al controlador para agregar el producto al estante.
     *
     * @param event El evento de acción del botón de agregar productos al estante.
     */
    public void actionPerformed_buttonAddProduct(ActionEvent event) {
        try {
            String product = (String) addComboBox.getSelectedItem();
            controller.ctrlPresentation_AddProductShelve(product);
            JOptionPane.showMessageDialog(this, "Product Added Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de eliminar productos del estante.
     * Este metodo recupera el nombre del producto y llama al controlador para eliminar el producto del estante.
     *
     * @param event El evento de acción del botón de eliminar productos del estante.
     */
    public void actionPerformed_buttonDeleteProduct(ActionEvent event) {
        try {
            String product = (String) deleteComboBox.getSelectedItem();
            controller.ctrlPresentation_DeleteProductShelve(product);
            JOptionPane.showMessageDialog(this, "Product Deleted Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            populateProductNames(deleteComboBox);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de algoritmo 1.
     * Este metodo llama al controlador para ejecutar el algoritmo 1 y muestra un mensaje de éxito o error.
     *
     * @param event El evento de acción del botón de algoritmo 1.
     */
    public void actionPerformed_buttonAlgorithm1(ActionEvent event) {
        try {
            controller.ctrlPresentation_CalculateFast();
            JOptionPane.showMessageDialog(this, "Algorithm Fast Executed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de algoritmo 2.
     * Este metodo llama al controlador para ejecutar el algoritmo 2 y muestra un mensaje de éxito o error.
     *
     * @param event El evento de acción del botón de algoritmo 2.
     */
    public void actionPerformed_buttonAlgorithm2(ActionEvent event) {
        try {
            controller.ctrlPresentation_CalculateSlow();
            JOptionPane.showMessageDialog(this, "Algorithm Slow Executed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de algoritmo 3.
     * Este metodo llama al controlador para ejecutar el algoritmo 3 y muestra un mensaje de éxito o error.
     *
     * @param event El evento de acción del botón de algoritmo 3.
     */
    public void actionPerformed_buttonAlgorithm3(ActionEvent event) {
        try {
            controller.ctrlPresentation_CalculateThird();
            JOptionPane.showMessageDialog(this, "Algorithm Random Executed Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de estadísticas.
     * Este metodo llama al controlador para recuperar las estadísticas y mostrarlas en la interfaz de usuario.
     *
     * @param event El evento de acción del botón de estadísticas.
     */
    public void actionPerformed_buttonStatistics(ActionEvent event) {
        try {
            // Retrieve the statistics
            List<Estadisticas> fast_StatsByTime = controller.ctrlPresentation_getFastEstadisticasByTimestamp();
            List<Estadisticas> fast_StatsByCost = controller.ctrlPresentation_getFastEstadisticasByCost();
            List<Estadisticas> fast_StatsByIterations = controller.ctrlPresentation_getFastEstadisticasByIterations();

            List<Estadisticas> slow_StatsByTime = controller.ctrlPresentation_getSlowEstadisticasByTimestamp();
            List<Estadisticas> slow_StatsByCost = controller.ctrlPresentation_getSlowEstadisticasByCost();
            List<Estadisticas> slow_StatsByIterations = controller.ctrlPresentation_getSlowEstadisticasByIterations();

            List<Estadisticas> third_StatsByTime = controller.ctrlPresentation_getThirdEstadisticasByTimestamp();
            List<Estadisticas> third_StatsByCost = controller.ctrlPresentation_getThirdEstadisticasByCost();
            List<Estadisticas> third_StatsByIterations = controller.ctrlPresentation_getThirdEstadisticasByIterations();

            // Create a helper method to populate panels with statistics
            populatePanel(table1Panel, fast_StatsByTime);
            populatePanel(table2Panel, fast_StatsByCost);
            populatePanel(table3Panel, fast_StatsByIterations);
            populatePanel(table4Panel, slow_StatsByTime);
            populatePanel(table5Panel, slow_StatsByCost);
            populatePanel(table6Panel, slow_StatsByIterations);
            populatePanel(table7Panel, third_StatsByTime);
            populatePanel(table8Panel, third_StatsByCost);
            populatePanel(table9Panel, third_StatsByIterations);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo auxiliar para poblar un panel con estadísticas.
     * Este metodo elimina todos los componentes del panel y agrega nuevos componentes para cada objeto de estadísticas.
     *
     * @param panel             El panel que se actualizará con las estadísticas.
     * @param estadisticasList  La lista de objetos de estadísticas que se mostrarán en el panel.
     */
    private void populatePanel(JPanel panel, List<Estadisticas> estadisticasList) {
        panel.removeAll(); // Clear the panel before adding new components
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (Estadisticas estadisticas : estadisticasList) {
            // Create a card panel for each Estadisticas object
            JPanel card = new JPanel();
            card.setLayout(new BorderLayout());
            card.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

            // Add data to the left side of the card
            JPanel dataPanel = new JPanel();
            dataPanel.setLayout(new GridLayout(0, 1, 0, 2)); // Vertical layout with minimal spacing
            dataPanel.add(new JLabel("Timestamp: " + estadisticas.getTimestamp().withNano(0)));
            dataPanel.add(new JLabel("Rows:" + estadisticas.getRows() + " Columns:" + estadisticas.getColumns()));
            card.add(dataPanel, BorderLayout.CENTER);

            // Add buttons to the right side of the card
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 2, 5, 5)); // Smaller buttons with gaps

            JButton showButton = new JButton("Show");
            showButton.setPreferredSize(new Dimension(75, 25));
            showButton.addActionListener(e -> showEstadisticas(estadisticas));
            buttonPanel.add(showButton);

            JButton exportButton = new JButton("Export");
            exportButton.setPreferredSize(new Dimension(75, 25));
            exportButton.addActionListener(e -> actionPerformed_exportEstadisticas(estadisticas));
            buttonPanel.add(exportButton);

            card.add(buttonPanel, BorderLayout.EAST);

            // Add the card to the panel
            panel.add(card);
        }

        panel.revalidate();
        panel.repaint();
    }

    /**
     * Metodo para manejar la acción del botón de restablecer todo el historial.
     * Este metodo llama al controlador para restablecer todas las estadísticas y muestra un mensaje de éxito o error.
     *
     * @param event El evento de acción del botón de restablecer todo el historial.
     */
    private void actionPerformed_buttonResetAllHistory(ActionEvent event) {
        try {
            // Here we could create three buttons to reset each type of statistics because the domain let us do that, but as usual
            // we are going to reset all the statistics at once. As it happens in real life with Google etc.
            controller.ctrlPresentation_resetFastEstadisticas();
            controller.ctrlPresentation_resetSlowEstadisticas();
            controller.ctrlPresentation_resetThirdEstadisticas();
            JOptionPane.showMessageDialog(this, "All history has been reset!", "Success", JOptionPane.INFORMATION_MESSAGE);
            //Need to update screen
            actionPerformed_buttonStatistics(event);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo auxiliar para mostrar los detalles de las estadísticas en un cuadro de diálogo.
     * Este metodo muestra los detalles de las estadísticas en un cuadro de diálogo o en otro componente de interfaz de usuario apropiado.
     *
     * @param estadisticas El objeto de estadísticas que se mostrará en el cuadro de diálogo.
     */
    private void showEstadisticas(Estadisticas estadisticas) {
        // Prepare the distribution as a matrix
        int columns = estadisticas.getColumns(); // Number of columns
        List<String> distribution = estadisticas.getDistribution(); // The distribution list
        StringBuilder matrixBuilder = new StringBuilder();

        for (int i = 0; i < distribution.size(); i++) {
            matrixBuilder.append(distribution.get(i));

            // Add a separator between columns (e.g., '|')
            if ((i + 1) % columns != 0) {
                matrixBuilder.append(" | "); // Separate columns with a pipe
            } else {
                matrixBuilder.append("\n"); // New row after the last column
            }
        }

        // Build the message to display
        String message = "Timestamp: " + estadisticas.getTimestamp() + "\n" +
                "Distribution:\n" + matrixBuilder.toString() + "\n" +
                "Columns: " + estadisticas.getColumns() + "\n" +
                "Rows: " + estadisticas.getRows() + "\n" +
                "Time: " + estadisticas.getTime() + "\n" +
                "Cost: " + estadisticas.getCost() + "\n" +
                "Iterations: " + estadisticas.getNumberIterations();

        JOptionPane.showMessageDialog(null, message, "Estadisticas Details", JOptionPane.INFORMATION_MESSAGE);
    }



    /**
     * Metodo auxiliar para exportar un objeto de estadísticas a un archivo.
     * Este metodo exporta un objeto de estadísticas a un archivo en el sistema de archivos local.
     *
     * @param estadisticas El objeto de estadísticas que se exportará a un archivo.
     */
    private void actionPerformed_exportEstadisticas(Estadisticas estadisticas) {
        // Export the Estadisticas object to a file
        try {
            // Prompt the user for a file name
            String fileName = controller.ctrlP_checkfile(JOptionPane.showInputDialog(controller.getFrame(), "Enter the file name:"));

            // Create a file browser dialog
            FileBrowser fileBrowser = new FileBrowser();
            String filePath = fileBrowser.browse(controller.getFrame(), true);
            String fullPath = filePath + File.separator + fileName;
            if (filePath != null) {
                // Write the Estadisticas object to the file
                controller.ctrlPresentation_ExportFastEstadisticas(estadisticas, fullPath);
                JOptionPane.showMessageDialog(null, "Estadisticas exported successfully to " + filePath + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No file selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Metodo para manejar la acción del botón de importación.
     * Este metodo llama al controlador para importar productos en el estante desde un archivo.
     *
     * @param event El evento de acción del botón de importación.
     */
    public void actionPerformed_buttonImport(ActionEvent event) {
        try {
            FileBrowser fileBrowser = new FileBrowser();
            // Open a file browser dialog
            String filePath = fileBrowser.browse(controller.getFrame(), false);
            if (filePath != null) {
                controller.ctrlPresentation_ImportShelve(filePath);
                JOptionPane.showMessageDialog(this, "Similarity imported successfully from" + filePath + " !", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No file selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Metodo para manejar la acción del botón de exportación.
     * Este metodo llama al controlador para exportar productos del estante a un archivo.
     *
     * @param event El evento de acción del botón de exportación.
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

                controller.ctrlPresentation_ExportShelve(fullPath);

                JOptionPane.showMessageDialog(controller.getFrame(),
                        "Shelf exported successfully to " + fullPath + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No folder selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
            exportAction_filePathField.setText("");

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


