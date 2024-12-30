package edu.upc.prop.clusterxx;

import javax.swing.*;
import java.awt.*;

/**
 * Clase de utilidad para generar estilos CSS y formatear colores en formato RGB.
 * <p>
 * Esta clase proporciona métodos para generar bloques de estilo CSS para un estilo consistente
 * y convertir colores en cadenas de formato RGB compatibles con CSS. Está diseñada para
 * simplificar el proceso de integrar objetos de color de Java o colores de UIManager
 * en estilos CSS.
 */
public class CSSUtils {

    /**
     * Convierte un color identificado por una etiqueta de UIManager en una cadena en formato RGB de CSS.
     *
     * @param Lable la etiqueta de UIManager para el color que se va a convertir.
     * @return una cadena que representa el color en formato RGB de CSS (por ejemplo, "rgb(255,255,255);").
     */
    private static String get_color_rgbFormatCSS(String Lable) {
        int R = UIManager.getColor(Lable).getRed();
        int G = UIManager.getColor(Lable).getGreen();
        int B = UIManager.getColor(Lable).getBlue();
        return "rgb("+R+','+G+','+B+");";
    }

    /**
     * Convierte un objeto {@link java.awt.Color} en una cadena en formato RGB de CSS.
     *
     * @param color el objeto {@link java.awt.Color} que se va a convertir.
     * @return una cadena que representa el color en formato RGB de CSS (por ejemplo, "rgb(255,255,255);").
     */
    private static String get_color_rgbFormatCSS(Color color) {
        int R = color.getRed();
        int G = color.getGreen();
        int B = color.getBlue();
        return "rgb("+R+','+G+','+B+");";
    }


    /**
     * Genera un bloque de estilo CSS para un estilo consistente en toda la aplicación.
     * <p>
     * El CSS generado incluye estilos para texto del cuerpo, encabezados, listas y texto preformateado,
     * aprovechando colores y fuentes del sistema. Está diseñado para integrarse sin problemas con los
     * componentes y temas de Java Swing.
     *
     * @return una cadena que contiene el bloque de estilo CSS.
     */
    public static String CSS_Style() {
        return  "<style>" +
                "    body { font-family:"+ Font.DIALOG+", sans-serif; font-size: 12px; color:"+get_color_rgbFormatCSS("Label.foreground")+" margin: 0; padding: 0; }" +
                "    h2 { font-size: 16px; font-weight: bold; }" +
                "    h3 { font-size: 14px; font-weight: bold; }" +
                "    h4 { font-size: 12px; font-weight: bold; }" +
                "    ul { margin-left: 20px; }" +
                "    li { font-size: 12px; font-weight: normal; line-height: 1.6; }" +
                "    pre { font-family:"+ Font.DIALOG+"; color:"+get_color_rgbFormatCSS(Color.BLACK)+"background-color:"+get_color_rgbFormatCSS(Color.LIGHT_GRAY)+" padding: 5px; border-radius: 12px; font-size: 12px; }" +
                "    .content { position: absolute; top: 20px; left: 20px; width: 90%; max-width: 800px; }" +
                "</style>";
    }
}
