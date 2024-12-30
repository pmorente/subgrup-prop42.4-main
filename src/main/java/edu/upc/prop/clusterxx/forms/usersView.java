package edu.upc.prop.clusterxx.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.*;

import edu.upc.prop.clusterxx.controller.CtrlPresentation;

/**
 * Representa la interfaz de usuario para mostrar, crear, iniciar sesión y eliminar usuarios.
 * Esta clase extiende {@link JPanel} y proporciona una vista para interactuar con los datos de usuario a través de botones
 * para iniciar sesión y eliminar usuarios, además de una lista de tarjetas de usuario.
 */

public class usersView extends JPanel {

    /**
     * Panel para mostrar el contenido dinámico.
     */
    private JPanel contentPanel; // Panel to hold dynamic content

    /**
     * Controlador que maneja las acciones del usuario.
     */
    private CtrlPresentation controller;

    /**
     * Constructor para inicializar el panel de vista de usuarios.
     *
     * @param controller El controlador que maneja las acciones del usuario y la lógica de negocio.
     */
    public usersView(CtrlPresentation controller) {
        this.controller = controller;
        InitUI();
    }

    /**
     * Inicializa los componentes de la interfaz de usuario como etiquetas, botones y paneles.
     * Establece el diseño del panel y agrega la barra de desplazamiento y los botones necesarios.
     */
    private void InitUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the panel

        // Title label at the top
        JLabel titleLabel = new JLabel("Users", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.DIALOG, Font.ITALIC, 12));
        add(titleLabel, BorderLayout.NORTH);

        // Scrollable content panel for user cards
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);

        // Create User button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createUserButton = new JButton("Create User");
        createUserButton.addActionListener(e -> actionPerformed_CreateUser());
        bottomPanel.add(createUserButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Populate with user data
        refreshUserList();
    }

    /**
     * Actualiza la lista de usuarios mostrando las tarjetas con la información de cada usuario.
     * Se obtiene la lista de usuarios desde el controlador.
     * Si ocurre un error al obtener los usuarios, se muestra un mensaje de error.
     */
    private void refreshUserList() {
        contentPanel.removeAll(); // Clear existing content

        // Fetch user list from the controller
        try {
            List<String> users = controller.ctrlPresentation_getAllUsers(); // Assume this method exists and returns a list of usernames
            for (String user : users) {
                contentPanel.add(createUserCard(user));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch user list.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }


    /**
     * Crea una tarjeta visual para representar un usuario con su nombre y botones para iniciar sesión o eliminarlo.
     *
     * @param username El nombre de usuario que se mostrará en la tarjeta.
     * @return Un panel que contiene la tarjeta del usuario con su nombre y los botones correspondientes.
     */
    private JPanel createUserCard(String username) {
        // Creating the layout for the card
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(UIManager.getBorder("TitledBorder.border"));
        // Cards will be 90px tall and expand horizontally
        cardPanel.setPreferredSize(new Dimension(400, 90));
        cardPanel.setMaximumSize(new Dimension(500, 90));

        // Username labeling on the left
        JLabel userLabel = new JLabel(username);
        userLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0)); // padding in the left
        cardPanel.add(userLabel, BorderLayout.CENTER);

        // Buttons panel on the right
        // Contains the Log In and Delete buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 5, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Padding

        JButton loginButton = new JButton("Log In");
        loginButton.setMargin(new Insets(5, 10, 5, 10));
        loginButton.addActionListener(e -> actionPerformed_LogIn(username));

        JButton deleteButton = new JButton("Delete User");
        deleteButton.setMargin(new Insets(5, 10, 5, 10));
        deleteButton.addActionListener(e -> actionPerformed_DeleteUser(username));

        buttonPanel.add(loginButton);
        buttonPanel.add(deleteButton);

        cardPanel.add(buttonPanel, BorderLayout.EAST);

        return cardPanel;
    }

    /**
     * Acción ejecutada al hacer clic en el botón para eliminar un usuario.
     * Muestra un cuadro de confirmación y, si se confirma, se elimina el usuario.
     *
     * @param username El nombre del usuario a eliminar.
     */
    private void actionPerformed_DeleteUser(String username) {

        // Double confirmation when process to eliminate a user
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete user: " + username + "?",
                "Delete Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {

            // Exception handler for the delete user process
            try {
                controller.ctrlPresentation_deleteUser(username);
                JOptionPane.showMessageDialog(this, "User " + username + " deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshUserList();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Failed to delete user: " + username,
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Acción ejecutada al hacer clic en el botón para iniciar sesión con un usuario.
     * Inicia sesión con el usuario indicado y sincroniza el catálogo.
     *
     * @param username El nombre del usuario con el que se desea iniciar sesión.
     */
    private void actionPerformed_LogIn(String username) {

        //Exception handler if error occurs when trying to log in
        try {
            controller.ctrlPresentation_loginUser(username);
            JOptionPane.showMessageDialog(this, "Logged in as " + username + ".",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            controller.sincronizacionCatalog();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to log in as " + username,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Acción ejecutada al hacer clic en el botón para crear un nuevo usuario.
     * Muestra un cuadro de diálogo para ingresar el nombre de usuario y crea el usuario.
     *
     */
    private void actionPerformed_CreateUser() {

        // As an alternative of creating a frame, we create a dialog
        JDialog createUserDialog = new JDialog(SwingUtilities.getWindowAncestor(this), "Create User", Dialog.ModalityType.APPLICATION_MODAL);
        createUserDialog.setLayout(new BorderLayout());
        createUserDialog.setSize(400, 200);
        createUserDialog.setLocationRelativeTo(this);

        // Dialog Content
        JPanel dialogContent = new JPanel(new GridLayout(2, 1, 10, 10));
        dialogContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel usernameLabel = new JLabel("Enter Username:");
        JTextField usernameField = new JTextField();

        dialogContent.add(usernameLabel);
        dialogContent.add(usernameField);

        createUserDialog.add(dialogContent, BorderLayout.CENTER);

        // Buttons of the dialog and the logic corresponding to them
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> {
            try{
            String username = controller.ctrlP_checkInput(usernameField.getText(), 0);
            if (!username.isEmpty()) {
                try {
                    controller.ctrlPresentation_createUser(username);
                    JOptionPane.showMessageDialog(createUserDialog, "User created successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    createUserDialog.dispose();
                    refreshUserList();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(createUserDialog, "Failed to create user: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(createUserDialog, "Username cannot be empty.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(createUserDialog, "Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> createUserDialog.dispose());

        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        //Creating the dialog
        createUserDialog.add(buttonPanel, BorderLayout.SOUTH);

        //Showing the dialog
        createUserDialog.setVisible(true);
    }
}
