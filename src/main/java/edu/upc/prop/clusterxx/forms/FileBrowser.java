package edu.upc.prop.clusterxx.forms;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * Clase que permite abrir un cuadro de diálogo para seleccionar un archivo o carpeta.
 */

public class FileBrowser {

    /**
     * Abre un cuadro de diálogo para seleccionar un archivo o carpeta y devuelve la ruta del archivo o carpeta seleccionada.
     *
     * @param parent El componente padre del cuadro de diálogo (puede ser nulo).
     * @param selectFolders Si es verdadero, solo se podrán seleccionar carpetas; si es falso, solo se podrán seleccionar archivos.
     * @return La ruta absoluta del archivo o carpeta seleccionada, o nulo si no se seleccionó nada.
     */
    public String browse(JFrame parent, boolean selectFolders) {
        // Create the file chooser
        /**
         *
         * JFileChooser is a class that provides a dialog for selecting files or directories.
         */
        JFileChooser fileChooser = new JFileChooser();

        // Set the selection mode
        if (selectFolders) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // Only folders
        } else {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // Only files
        }

        // Show the open dialog
        int result = fileChooser.showOpenDialog(parent);

        // If a file or folder is selected, return its path
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }

        // If nothing is selected, return null
        return null;
    }
}

