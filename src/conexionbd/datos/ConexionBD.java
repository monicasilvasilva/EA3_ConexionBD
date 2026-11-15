package conexionbd.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Capa de datos: clase que se encarga SOLO de la conexión a la base de datos.
public class ConexionBD {

    // --- 1. Constante con el driver de MySQL ---
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // --- 2. Método que prueba la conexión ---
    // Recibe la URL, el usuario y la contraseña que se escribieron en la ventana.
    // Si algo falla, lanza la excepción para que la ventana la muestre.
    public String probarConexion(String url, String usuario, String contrasena)
            throws ClassNotFoundException, SQLException {

        // Paso 1: cargar el driver JDBC (si no está el .jar, lanza ClassNotFoundException)
        Class.forName(DRIVER);

        // Paso 2: abrir la conexión con DriverManager dentro de un try-with-resources.
        // Al terminar el bloque, la conexión se cierra sola, aunque ocurra un error.
        try (Connection conexion = DriverManager.getConnection(url, usuario, contrasena)) {

            // Paso 3: si llegamos aquí, la conexión fue exitosa
            return "Conexión exitosa a la base de datos: " + conexion.getCatalog()
                    + "\nMotor: " + conexion.getMetaData().getDatabaseProductName()
                    + " " + conexion.getMetaData().getDatabaseProductVersion();
        }
    }
}
