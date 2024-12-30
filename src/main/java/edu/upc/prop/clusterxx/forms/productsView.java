package edu.upc.prop.clusterxx.forms;

import edu.upc.prop.clusterxx.CSSUtils;
import edu.upc.prop.clusterxx.controller.CtrlPresentation;
import org.w3c.dom.Text;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Clase que representa la vista de productos en la aplicación.
 * Esta clase proporciona una interfaz gráfica de usuario (GUI) que permite gestionar un catálogo de productos
 * realizando operaciones como agregar, eliminar, actualizar precios, descripciones, e importar/exportar productos.
 *
 * La vista se basa en un diseño con paneles intercambiables y una barra de menú para seleccionar diferentes acciones.
 * La lógica de la aplicación se maneja a través de un controlador de presentación (CtrlPresentation) que interactúa con
 * la interfaz de usuario y las acciones de los productos.
 */

public class productsView extends JPanel {
    /**
     * Panel principal para contener el contenido dinámico de la vista.
     * Este panel se utiliza para gestionar las diferentes vistas o paneles que se muestran en la interfaz
     * utilizando un diseño de tipo CardLayout.
     */
    private JPanel contentPanel; // Panel to hold dynamic content

    /**
     * ComboBox para seleccionar el nombre del producto a actualizar el precio.
     */
    private JComboBox<String> namePComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto a actualizar la descripción.
     */
    private JComboBox<String> nameDComboBox ;

    /**
     * ComboBox para seleccionar el nombre del producto a eliminar.
     */
    private JComboBox<String> nameComboBox  ;

    /**
     * Botón que ejecuta la acción de refrescar el catálogo de productos en la vista.
     * Al hacer clic en este botón, se actualiza el contenido del catálogo, mostrando los productos más recientes.
     */
    private JButton refreshAction_Button;

    /**
     * Tabla que muestra los productos del catálogo.
     * Se utiliza para mostrar una lista de productos con sus detalles (nombre, precio, descripción).
     */
    private JTable refreshAction_catalogTable;

    // Components for Add Product
    /**
     * Botón que ejecuta la acción de agregar un nuevo producto al catálogo.
     * Al hacer clic en este botón, se recoge la información proporcionada por el usuario y se agrega el producto.
     */
    private JButton addProductAction_Button;
    /**
     * Campo de texto para ingresar el nombre del nuevo producto.
     */
    private JTextField addProductAction_nameField, addProductAction_descriptionField, addProductAction_priceField;

    // Components for Update Price
    /**
     * Botón que ejecuta la acción de actualizar el precio de un producto en el catálogo.
     * Al hacer clic en este botón, se actualiza el precio del producto según la información proporcionada.
     */
    private JButton updatePriceAction_Button;

    /**
     * Campo de texto para ingresar el valor del producto cuyo precio se desea actualizar.
     */
    private JTextField updatePriceAction_priceField;

    // Components for Update Description
    /**
     * Botón que ejecuta la acción de actualizar la descripción de un producto en el catálogo.
     * Al hacer clic en este botón, se actualiza la descripción del producto según la información proporcionada.
     */
    private JButton updateDescriptionAction_Button;
    /**
     * Campo de texto para ingresar la descripción del producto cuya descripción se desea actualizar.
     */
    private JTextField updateDescriptionAction_descriptionField;

    // Components for Delete Product
    /**
     * Botón que ejecuta la acción de eliminar un producto del catálogo.
     * Al hacer clic en este botón, el producto especificado por el usuario será eliminado del catálogo.
     */
    private JButton deleteProductAction_Button;

    // Components for Import Products
    /**
     * Botón que ejecuta la acción de importar productos desde un archivo.
     * Al hacer clic en este botón, se abre un cuadro de diálogo para seleccionar un archivo de productos a importar.
     */
    private JButton importProductsAction_Button;


    /**
     * Botón que ejecuta la acción de exportar productos a un archivo.
     * Al hacer clic en este botón, se exporta el catálogo de productos a un archivo con el nombre especificado.
     */
    private JButton exportProducts_Button;
    /**
     * Campo de texto para ingresar el nombre del archivo de destino para la exportación.
     */
    private JTextField exportAction_nameField;

    /**
     * Controlador de presentación que gestiona las operaciones relacionadas con los productos.
     * Este controlador actúa como un puente entre la vista y la lógica de negocio, permitiendo que la interfaz de usuario
     * interactúe con las operaciones de los productos.
     */
    private CtrlPresentation controller;

    /**
     * Constructor que inicializa la vista de productos.
     * Configura la interfaz de usuario y enlaza los componentes con el controlador.
     *
     * @param controller El controlador que gestiona las operaciones relacionadas con los productos.
     */
    public productsView(CtrlPresentation controller) {
        this.controller = controller;

        // Initialize UI components
        initUI();
    }

    /**
     * Inicializa los componentes visuales de la interfaz de usuario.
     * Se configura la estructura principal de la vista, incluyendo la barra de menú superior, los paneles de contenido dinámico y los botones de acción.
     * Además, se enlazan los botones con los métodos de manejo de la lógica de negocio mediante listeners.
     *
     * El layout principal utilizado es un BorderLayout. Los paneles de contenido que se muestran dinámicamente dentro del área central
     * utilizan un CardLayout para gestionar diferentes vistas de manera eficiente.
     */
    private void initUI() {

        //Uses BorderLayout to organize the components in the panel
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Create the top menu bar
        JMenuBar topMenu = new JMenuBar();
        JMenu catalogMenu = new JMenu("Catalog");
        Color cf = new Color(UIManager.getColor("Label.foreground").getRGB());
        Color cb = new Color(UIManager.getColor("Label.background").getRGB());
        catalogMenu.setBackground(cf);
        catalogMenu.setForeground(cb);
        catalogMenu.setOpaque(true);
        JMenu similitudesMenu = new JMenu("Similarity");
        JMenu shelveMenu = new JMenu("Shelf");

        topMenu.add(catalogMenu);
        topMenu.add(similitudesMenu);
        topMenu.add(shelveMenu);


        similitudesMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.sincronizacionSimilitud();
            }
        });

        shelveMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                controller.sincronizacionShelve();
            }
        });

        add(topMenu, BorderLayout.NORTH); // Add the top menu to the main layout

        // Right Menu Items
        // Create the menu on the right side with buttons of the functionalities of the view
        JPanel menuPanel = new JPanel(new GridLayout(7, 1, 10, 10)); // Vertical buttons with spacing
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton catalogButton = new JButton("Catalog");
        catalogButton.addActionListener(e -> showCatalogPanel());

        JButton addButton = new JButton("Add Product");
        addButton.addActionListener(e -> showAddPanel());

        JButton updatePriceButton = new JButton("Update Price");
        updatePriceButton.addActionListener(e -> showUpdatePricePanel());

        JButton updateDescriptionButton = new JButton("Update Description");
        updateDescriptionButton.addActionListener(e -> showUpdateDescriptionPanel());

        JButton deleteButton = new JButton("Delete Product");
        deleteButton.addActionListener(e -> showDeletePanel());

        JButton importButton = new JButton("Import");
        importButton.addActionListener(e -> showImportPanel());

        JButton exportButton = new JButton("Export");
        exportButton.addActionListener(e -> showExportPanel());

        menuPanel.add(catalogButton);
        menuPanel.add(addButton);
        menuPanel.add(updatePriceButton);
        menuPanel.add(updateDescriptionButton);
        menuPanel.add(deleteButton);
        menuPanel.add(importButton);
        menuPanel.add(exportButton);

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

        // Initialize all the panels and add them to the content area

        contentPanel.add(createCatalogPanel(), "Catalog");
        contentPanel.add(createAddPanel(), "Add");
        contentPanel.add(createUpdatePricePanel(), "UpdatePrice");
        contentPanel.add(createUpdateDescriptionPanel(), "UpdateDescription");
        contentPanel.add(createDeletePanel(), "Delete");
        contentPanel.add(createImportPanel(), "Import");
        contentPanel.add(createExportPanel(), "Export");

        // Show the catalog panel by default
        showCatalogPanel();
        startListeners();
    }

    /**
     * Crea el panel para mostrar el catálogo de productos.
     * Este panel utiliza un layout BorderLayout para organizar la tabla de productos en la parte central
     * y un botón de actualización en la parte inferior.
     *
     * El panel incluye una tabla JTable que se utiliza para mostrar todos los productos del catálogo.
     * Se puede actualizar la vista de la tabla con el botón de refresco que ejecuta una acción definida en el controlador.
     *
     * @return El panel que muestra el catálogo de productos.
     */
    private JPanel createCatalogPanel() {
        JPanel catalogPanel = new JPanel(new BorderLayout());
        catalogPanel.setBorder(BorderFactory.createTitledBorder("Product Catalog"));

        //refreshAction_catalogTextArea = new JTextArea(10, 30);
        //refreshAction_catalogTextArea.setEditable(false);
        //refreshAction_catalogTextArea.setText("Loading catalog...");
        refreshAction_catalogTable = new JTable();
        catalogPanel.add(new JScrollPane(refreshAction_catalogTable), BorderLayout.CENTER);


        refreshAction_Button = new JButton("Refresh");
        catalogPanel.add(refreshAction_Button, BorderLayout.SOUTH);

        return catalogPanel;
    }

    /**
     * Crea el panel para agregar un nuevo producto.
     * Este panel utiliza un layout GridBagLayout, que permite organizar los campos de texto y los botones de manera flexible.
     * El panel contiene tres campos de texto (para el nombre, la descripción y el precio) y un botón para agregar el producto al catálogo.
     *
     * @return El panel con los campos necesarios para agregar un nuevo producto.
     */
    private JPanel createAddPanel() {
        JPanel addPanel = new JPanel(new GridBagLayout());
        addPanel.setBorder(BorderFactory.createTitledBorder("Add Product"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addProductAction_nameField = new JTextField(20);
        addProductAction_descriptionField = new JTextField(20);
        addProductAction_priceField = new JTextField(20);
        addProductAction_Button = new JButton("Add Product");

        gbc.gridx = 0;
        gbc.gridy = 0;
        addPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        addPanel.add(addProductAction_nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        addPanel.add(addProductAction_descriptionField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addPanel.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        addPanel.add(addProductAction_priceField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        addPanel.add(addProductAction_Button, gbc);

        return addPanel;
    }

    /**
     * Crea el panel para actualizar el precio de un producto.
     * Este panel contiene un campo de texto para el nombre del producto y un campo de texto para el nuevo precio.
     * Además, tiene un botón para aplicar el cambio de precio al producto seleccionado.
     *
     * El layout utilizado es GridBagLayout para organizar los elementos de forma flexible.
     *
     * @return El panel con los campos necesarios para actualizar el precio de un producto.
     */
    private JPanel createUpdatePricePanel() {
        JPanel updatePricePanel = new JPanel(new GridBagLayout());
        updatePricePanel.setBorder(BorderFactory.createTitledBorder("Update Price"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;


         namePComboBox = new JComboBox<>();
         updatePriceAction_priceField = new JTextField(20);
         updatePriceAction_Button = new JButton("Update Price");

        gbc.gridx = 0;
        gbc.gridy = 0;
        updatePricePanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        updatePricePanel.add(namePComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        updatePricePanel.add(new JLabel("New Price:"), gbc);
        gbc.gridx = 1;
        updatePricePanel.add(updatePriceAction_priceField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        updatePricePanel.add(updatePriceAction_Button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><small><i>You can update the price of products that are already in the catalog by selecting from the list.</i></small></html>");
        instructionLabel.setFont(new Font("Lable.font", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        updatePricePanel.add(instructionLabel, gbc);

        return updatePricePanel;
    }

    /**
     * Crea el panel para actualizar la descripción de un producto.
     * Este panel permite ingresar el nombre del producto y la nueva descripción,
     * y contiene un botón para aplicar la actualización.
     *
     * @return El panel con los campos necesarios para actualizar la descripción de un producto.
     */
    private JPanel createUpdateDescriptionPanel() {
        JPanel updateDescriptionPanel = new JPanel(new GridBagLayout());
        updateDescriptionPanel.setBorder(BorderFactory.createTitledBorder("Update Description"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameDComboBox = new JComboBox<>();
        updateDescriptionAction_descriptionField = new JTextField(20);
        updateDescriptionAction_Button = new JButton("Update Description");


        gbc.gridx = 0;
        gbc.gridy = 0;
        updateDescriptionPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        updateDescriptionPanel.add(nameDComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        updateDescriptionPanel.add(new JLabel("New Description:"), gbc);
        gbc.gridx = 1;
        updateDescriptionPanel.add(updateDescriptionAction_descriptionField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        updateDescriptionPanel.add(updateDescriptionAction_Button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><small><i>You can update the description of products that are already in the catalog by selecting from the list.</i></small></html>");
        instructionLabel.setFont(new Font("Lable.font", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        updateDescriptionPanel.add(instructionLabel, gbc);
        return updateDescriptionPanel;
    }

    /**
     * Crea el panel para eliminar un producto del catálogo.
     * Este panel contiene un campo de texto donde el usuario debe ingresar el nombre del producto que desea eliminar
     * y un botón para confirmar la eliminación.
     *
     * El layout utilizado es GridBagLayout, que permite una disposición flexible de los componentes.
     *
     * @return El panel con los campos necesarios para eliminar un producto del catálogo.
     */
    private JPanel createDeletePanel() {
        JPanel deletePanel = new JPanel(new GridBagLayout());
        deletePanel.setBorder(BorderFactory.createTitledBorder("Delete Product"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nameComboBox = new JComboBox<>();
        deleteProductAction_Button = new JButton("Delete");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel instructionLabel = new JLabel("<html><small><i>You can delete products that are already in the catalog by selecting from the list.</i></small></html>");
        instructionLabel.setFont(new Font("Lable.font", Font.ITALIC, 12));
        instructionLabel.setForeground(Color.GRAY);
        deletePanel.add(instructionLabel, gbc);
        gbc.gridwidth = 0;

        gbc.gridy = 0;
        gbc.gridx = 1;
        deletePanel.add(nameComboBox, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        deletePanel.add(deleteProductAction_Button, gbc);

        return deletePanel;
    }

    /**
     * Crea el panel para importar productos desde un archivo.
     * Este panel contiene un botón que permite al usuario seleccionar un archivo desde su sistema local
     * para importar productos al catálogo. El botón se vincula a una función del controlador que maneja
     * el proceso de importación de productos.
     *
     * El layout utilizado es un GridLayout simple para organizar el botón de importación.
     *
     * @return El panel con el botón necesario para importar productos desde un archivo.
     */
    private JPanel createImportPanel() {
        JPanel importPanel = new JPanel(new BorderLayout());
        importPanel.setBorder(BorderFactory.createTitledBorder("Import Products"));
        String importText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<div class='content'>" +
                "<p>To import product data, use a text file with the following format:</p>" +
                "<pre><b>product_name, price, description</b></pre>" +
                "<hr>" +
                "<h3>Example:</h3>" +
                "<ul>" +
                "    <li><b>productA</b>, 2.5, High-quality product A</li>" +
                "    <li><b>productB</b>, 3.0, Durable and reliable product B</li>" +
                "    <li><b>productC</b>, 12.0, Premium-grade product C</li>" +
                "    <li><b>productD</b>, 4.0, Affordable product D with great features</li>" +
                "    <li><b>productE</b>, 3.0, Popular product E with positive reviews</li>" +
                "    <li><b>productF</b>, 1.0, Lightweight and versatile product F</li>" +
                "    <li><b>productG</b>, 12.0, Top-selling product G</li>" +
                "    <li><b>productH</b>, 5.0, Exclusive product H for special purposes</li>" +
                "</ul>" +
                "<hr>" +
                "<h4>Each line represents:</h4>" +
                "<ul>" +
                "    <li><b>product_name</b>: The name of the product.</li>" +
                "    <li><b>price</b>: Numeric value representing the price.</li>" +
                "    <li><b>description</b>: Short description of the product.</li>" +
                "</ul>" +
                "<hr>" +
                "</div>" +
                "</body>" +
                "</html>";

        JLabel importLabel = new JLabel(importText);

        importPanel.add(new JScrollPane(importLabel), BorderLayout.CENTER);

        importProductsAction_Button = new JButton("Import");
        importPanel.add(importProductsAction_Button, BorderLayout.SOUTH);

        return importPanel;
    }

    /**
     * Crea el panel para exportar productos a un archivo.
     * Este panel contiene un campo de texto donde el usuario puede ingresar el nombre del archivo de destino,
     * y un botón para ejecutar la exportación de productos.
     *
     * Al igual que otros paneles, se usa GridBagLayout para organizar los componentes de manera flexible.
     *
     * @return El panel con los campos necesarios para exportar productos a un archivo.
     */
    private JPanel createExportPanel() {
        JPanel exportPanel = new JPanel(new BorderLayout());
        exportPanel.setBorder(BorderFactory.createTitledBorder("Export Products"));

        String exportText = "<html>" +
                CSSUtils.CSS_Style()+
                "<body>" +
                "<p>The exported file will follow the format:</p>" +
                "<pre><b>product_name, price, description</b></pre>" +
                "<hr>" +
                "<h3>Example:</h3>" +
                "<ul>" +
                "    <li><b>itemAlpha</b>, 15.0, Item Alpha with enhanced performance</li>" +
                "    <li><b>itemBeta</b>, 7.5, Compact and user-friendly Item Beta</li>" +
                "    <li><b>itemGamma</b>, 5.0, Cost-effective Item Gamma for daily use</li>" +
                "    <li><b>itemDelta</b>, 20.0, Premium Item Delta with advanced features</li>" +
                "    <li><b>itemEpsilon</b>, 10.0, Multi-purpose Item Epsilon</li>" +
                "    <li><b>itemZeta</b>, 8.0, Reliable and durable Item Zeta</li>" +
                "    <li><b>itemEta</b>, 9.5, Versatile Item Eta with extended warranty</li>" +
                "    <li><b>itemTheta</b>, 12.0, Exclusive Item Theta for professionals</li>" +
                "</ul>" +
                "<hr>" +
                "<h4>Each line represents:</h4>" +
                "<ul>" +
                "    <li><b>product_name</b>: The name of the product.</li>" +
                "    <li><b>price</b>: Numeric value representing the price.</li>" +
                "    <li><b>description</b>: Short description of the product.</li>" +
                "</ul>" +
                "<hr>" +
                "</body>" +
                "</html>";
        JLabel exportLabel = new JLabel(exportText);
        exportPanel.add(new JScrollPane(exportLabel), BorderLayout.CENTER);

        // File name field for the export action
        exportAction_nameField = new JTextField(20);
        JPanel fileNamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Panel for the file name
        fileNamePanel.add(new JLabel("File Name:"));
        fileNamePanel.add(exportAction_nameField);
        exportPanel.add(fileNamePanel, BorderLayout.NORTH);

        // Export button
        exportProducts_Button = new JButton("Export");
        exportPanel.add(exportProducts_Button, BorderLayout.SOUTH);

        return exportPanel;
    }

    /**
     * Muestra el panel correspondiente al catálogo de productos.
     * Se utiliza el método `CardLayout.show()` para cambiar dinámicamente el contenido del área principal
     * y mostrar la vista del catálogo.
     */
    public void showCatalogPanel() {
        actionPerformed_buttonShowCatalog();
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Catalog");
    }

    /**
     * Muestra el panel para agregar un producto.
     * Este método cambia el contenido de la vista para mostrar el formulario de agregar un producto,
     * permitiendo al usuario ingresar los detalles del nuevo producto.
     */
    private void showAddPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Add");
    }

    /**
     * Muestra el panel para actualizar el precio de un producto.
     * Permite al usuario cambiar el precio de un producto mediante su nombre y el nuevo precio.
     */
    private void showUpdatePricePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "UpdatePrice");
        populateProductNames(namePComboBox);
    }

    /**
     * Muestra el panel para actualizar la descripción de un producto.
     * Este panel permite al usuario cambiar la descripción de un producto ya existente.
     */
    private void showUpdateDescriptionPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "UpdateDescription");
        populateProductNames(nameDComboBox);
    }

    /**
     * Muestra el panel para eliminar un producto.
     * En este panel, el usuario puede ingresar el nombre de un producto para eliminarlo del catálogo.
     */
    private void showDeletePanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Delete");
        populateProductNames(nameComboBox);
    }

    /**
     * Muestra el panel para importar productos desde un archivo.
     * Permite al usuario seleccionar un archivo de productos para importar al catálogo.
     */
    private void showImportPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Import");
    }

    /**
     * Muestra el panel para exportar productos a un archivo.
     * En este panel, el usuario puede especificar un nombre de archivo para exportar el catálogo de productos.
     */
    private void showExportPanel() {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "Export");
    }

    /**
     * Inicia los listeners para manejar los eventos de los botones.
     * Este método se encarga de definir las acciones que se ejecutan cuando el usuario interactúa con los botones.
     * Los botones de cada panel están asociados con sus respectivas acciones a través de listeners.
     */
    private void startListeners() {

        // Listener for Show Catalog button
        refreshAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonShowCatalog();
                // Optionally show a status message
                JOptionPane.showMessageDialog(null, "Catalog refreshed!");
            }
        });

        // Listener for Add Product button
        addProductAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonAddProduct(event);
            }
        });

        // Listener for Update Price button
        updatePriceAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonUpdatePrice(event);
            }
        });

        // Listener for Update Description button
        updateDescriptionAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonUpdateDescription(event);
            }
        });

        // Listener for Delete Product button
        deleteProductAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonDeleteProduct(event);
            }
        });

        // Listener for Import button
        importProductsAction_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonImportProducts(event);
            }
        });

        // Listener for Export button
        exportProducts_Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                actionPerformed_buttonExportProducts(event);
            }
        });

    }





    /**
     * Refresca el catálogo de productos cuando el botón de actualización es presionado.
     *
     * Este método:
     * 1. Obtiene la lista actualizada de productos desde el controlador.
     * 2. Limpia el modelo de la tabla actual.
     * 3. Población la tabla con los datos actualizados.
     *
     * Se utiliza el modelo de tabla `DefaultTableModel` para realizar las actualizaciones dinámicas.
     * Este proceso asegura que la vista del catálogo refleje siempre el estado actual del sistema.
     */
    public void actionPerformed_buttonShowCatalog() {
        // Retrieve the product catalog from the controller
        List<String[]> allProducts = controller.ctrlPresentation_getCatalog();

        // Define column names for the JTable (this should match the table's existing structure)
        String[] columnNames = {"Name", "Price", "Description"};

        // Create a new table model with the data
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // All cells are non-editable
            }
        };

        // Add data to the table model
        for (String[] product : allProducts) {
            tableModel.addRow(product); // Add each product (name, price, description)
        }

        // Assuming the parent container already has a JTable called "refreshAction_catalogTable"
        refreshAction_catalogTable.setModel(tableModel); // Update the JTable with the new model

        // Optionally refresh the table display (though not usually needed)
        refreshAction_catalogTable.revalidate();
        refreshAction_catalogTable.repaint();

    }


    /**
     * Maneja el evento de agregar un producto cuando el botón correspondiente es presionado.
     *
     * Este método:
     * 1. Obtiene los valores ingresados por el usuario en los campos de texto:
     *    - Nombre del producto.
     *    - Descripción del producto.
     *    - Precio del producto (convertido a un valor `double`).
     * 2. Llama al controlador para agregar el producto con los datos proporcionados.
     *
     * Si ocurre un error en la entrada (por ejemplo, el precio no es un número válido),
     * se puede implementar un manejo de excepciones (no incluido actualmente).
     * @param event El evento de acción que desencadena la adición de un producto.
     */
    public void actionPerformed_buttonAddProduct(ActionEvent event) {

        try {
            String name = controller.ctrlP_checkInput(addProductAction_nameField.getText(), 0);
            String description = controller.ctrlP_checkInput(addProductAction_descriptionField.getText(), 1);
            String priceText = controller.ctrlP_checkInput(addProductAction_priceField.getText(), 3);

            double price = Double.parseDouble(priceText);
            controller.ctrlPresentation_addProduct(name, price, description);
            JOptionPane.showMessageDialog(this, "Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            addProductAction_nameField.setText("");
            addProductAction_descriptionField.setText("");
            addProductAction_priceField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de actualización del precio de un producto cuando el botón correspondiente es presionado.
     *
     * Este método:
     * 1. Obtiene el nombre del producto y el nuevo precio desde los campos de texto.
     * 2. Convierte el nuevo precio ingresado a un valor `double`.
     * 3. Llama al controlador para actualizar el precio del producto con los datos proporcionados.
     *
     * Si ocurre un error en la entrada (por ejemplo, el nuevo precio no es un número válido),
     * se puede implementar un manejo de excepciones (no incluido actualmente).
     * @param event El evento de acción que desencadena la actualización del precio de un producto.
     */
    public void actionPerformed_buttonUpdatePrice(ActionEvent event) {
        try {

            String name = (String) namePComboBox.getSelectedItem();
            String priceText = controller.ctrlP_checkInput(updatePriceAction_priceField.getText(), 3);

            double price = Double.parseDouble(priceText);
            controller.ctrlPresentation_updatePrice(name, price);
            JOptionPane.showMessageDialog(this, "Price updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateProductNames(namePComboBox);
            updatePriceAction_priceField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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


    /**
     * Maneja el evento de actualización de la descripción de un producto cuando el botón correspondiente es presionado.
     *
     * Este método:
     * 1. Obtiene el nombre del producto y la nueva descripción desde los campos de texto.
     * 2. Llama al controlador para actualizar la descripción del producto con los datos proporcionados.
     *
     * Si los campos están vacíos o el producto no existe, se podría implementar un manejo de errores adicional.
     * @param event El evento de acción que desencadena la actualización de la descripción de un producto.
     */
    public void actionPerformed_buttonUpdateDescription(ActionEvent event) {
        try {
            String name = (String) nameDComboBox.getSelectedItem();
            String description = controller.ctrlP_checkInput(updateDescriptionAction_descriptionField.getText(), 2);

            controller.ctrlPresentation_updateDescription(name, description);
            JOptionPane.showMessageDialog(this, "Description updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateProductNames(nameDComboBox);
            updateDescriptionAction_descriptionField.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de eliminación de un producto cuando el botón correspondiente es presionado.
     *
     * Este método:
     * 1. Obtiene el nombre del producto desde el campo de texto.
     * 2. Llama al controlador para eliminar el producto con el nombre proporcionado.
     *
     * Si el campo de texto está vacío o el producto no existe, se podría mostrar un mensaje de error.
     * @param event El evento de acción que desencadena la eliminación de un producto.
     */
    public void actionPerformed_buttonDeleteProduct(ActionEvent event) {
        try {
            String name = (String) nameComboBox.getSelectedItem();

            controller.ctrlPresentation_deleteProduct(name);
            JOptionPane.showMessageDialog(this, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            populateProductNames(nameComboBox);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de importación de productos cuando el botón correspondiente es presionado.
     *
     * Este método abre un cuadro de diálogo para que el usuario seleccione un archivo desde su sistema local.
     * El archivo seleccionado es pasado al controlador, que realiza la lógica de importar productos al catálogo.
     *
     * Actualmente, la implementación para manejar el archivo está pendiente.
     * @param event El evento de acción que desencadena la importación de productos.
     */
    public void actionPerformed_buttonImportProducts(ActionEvent event) {
        try {
            FileBrowser fileBrowser = new FileBrowser();
            // Open a file browser dialog
            String filePath = fileBrowser.browse(controller.getFrame(), false);
            if (filePath != null) {
                controller.ctrlPresentation_importProducts(filePath);
                JOptionPane.showMessageDialog(this, "Products imported successfully from" + filePath + " !", "Success", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No file selected.");
            }
            } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de exportación de productos cuando el botón correspondiente es presionado.
     *
     * Este método:
     * 1. Obtiene el nombre del archivo desde el campo de texto.
     * 2. Llama al controlador para exportar los productos al archivo especificado.
     *
     * Si el nombre del archivo es inválido, se puede mostrar un mensaje de error.
     * @param event El evento de acción que desencadena la exportación de productos.
     */
    public void actionPerformed_buttonExportProducts(ActionEvent event) {
        try {

            String name = controller.ctrlP_checkfile(exportAction_nameField.getText());

            // Open a folder browser dialog
            FileBrowser fileBrowser = new FileBrowser();
            String folderPath = fileBrowser.browse(controller.getFrame(), true);

            if (folderPath != null) {
                // Combine folder path and file name to create the full file path
                String fullPath = folderPath + File.separator + name;

                controller.ctrlPresentation_exportProducts(fullPath);

                JOptionPane.showMessageDialog(controller.getFrame(),
                        "Products exported successfully to " + fullPath + "!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(controller.getFrame(), "No folder selected.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(controller.getFrame(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}





