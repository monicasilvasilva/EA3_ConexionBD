package conexionbd;

import conexionbd.servicio.ServicioConexion;
import conexionbd.vista.VentanaConexion;

import javax.swing.SwingUtilities;

/** Punto de entrada: crea el servicio, se lo entrega a la ventana y la muestra. */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaConexion ventana = new VentanaConexion(new ServicioConexion());
            ventana.setVisible(true);
        });
    }
}
