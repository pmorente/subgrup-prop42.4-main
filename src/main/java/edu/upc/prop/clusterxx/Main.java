
package edu.upc.prop.clusterxx;
import edu.upc.prop.clusterxx.controller.CtrlPresentation;


/**
 * Clase main, encargada de llamar al controlador de presentación y iniciar la ejecución.
 */
public class Main {

    /**
     * Método main que inicia la ejecución del programa.
     *
     * @param args Argumentos de la línea de comandos.
     * @throws Exception Si hay un error al iniciar la presentación.
     */
    public static void main (String[] args) throws Exception {


        javax.swing.SwingUtilities.invokeLater (
                () -> {
                    CtrlPresentation ctrlP = new CtrlPresentation();
                    ctrlP.startPresentation();
                });
    }
}


