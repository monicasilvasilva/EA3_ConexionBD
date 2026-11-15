package conexionbd;

import conexionbd.vista.VentanaConexion;

import javax.swing.SwingUtilities;

// Punto de entrada del programa: crea la ventana y la muestra
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaConexion ventana = new VentanaConexion();
            ventana.setVisible(true);
        });
    }
}
