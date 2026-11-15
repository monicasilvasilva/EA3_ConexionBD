package conexionbd.controlador;

import conexionbd.datos.ConexionBD;

import java.sql.SQLException;

// Controlador: recibe la petición de la vista, valida los datos
public class ConexionController {

    private ConexionBD conexionBD;

    public ConexionController() {
        conexionBD = new ConexionBD();
    }

    // Valida los datos del formulario y prueba la conexión
    public String probarConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException {

        // 1. Validar que los datos obligatorios no estén vacíos
        if (url.isEmpty() || usuario.isEmpty()) {
            throw new IllegalArgumentException("La URL y el usuario son obligatorios.");
        }

        // 2. Pedirle a la capa de datos que se conecte
        return conexionBD.probarConexion(url, usuario, contrasena);
    }
}
