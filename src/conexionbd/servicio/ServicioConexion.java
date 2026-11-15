package conexionbd.servicio;

import conexionbd.modelo.ConfiguracionConexion;
import conexionbd.modelo.ResultadoConexion;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lógica de conexión a la base de datos, separada de la interfaz gráfica.
 * Carga el driver, abre la conexión con DriverManager.getConnection() dentro de un
 * try-with-resources (la conexión se cierra sola) y traduce los errores JDBC
 * a un mensaje entendible.
 */
public class ServicioConexion {

    private static final int TIEMPO_ESPERA_SEGUNDOS = 5;

    /**
     * Intenta conectarse con la configuración recibida.
     * Nunca lanza excepciones: siempre devuelve un ResultadoConexion.
     */
    public ResultadoConexion probarConexion(ConfiguracionConexion configuracion) {
        try {
            configuracion.validar();
            cargarDriver(configuracion.getMotor().getClaseDriver());
            DriverManager.setLoginTimeout(TIEMPO_ESPERA_SEGUNDOS);

            try (Connection conexion = DriverManager.getConnection(
                    configuracion.getUrl(),
                    configuracion.getUsuario(),
                    configuracion.getContrasena())) {
                return ResultadoConexion.exito(describirConexion(conexion, configuracion));
            }

        } catch (IllegalArgumentException e) {
            return ResultadoConexion.error("Datos incompletos: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            return ResultadoConexion.error(describirDriverFaltante(configuracion, e));
        } catch (SQLException e) {
            return ResultadoConexion.error(describirErrorSql(configuracion, e));
        }
    }

    /** Carga explícitamente la clase del driver JDBC. */
    private void cargarDriver(String claseDriver) throws ClassNotFoundException {
        Class.forName(claseDriver);
    }

    private String describirConexion(Connection conexion, ConfiguracionConexion configuracion)
            throws SQLException {
        DatabaseMetaData metadatos = conexion.getMetaData();
        return "CONEXIÓN EXITOSA\n"
                + "URL: " + configuracion.getUrl() + "\n"
                + "Usuario: " + metadatos.getUserName() + "\n"
                + "Motor: " + metadatos.getDatabaseProductName() + " "
                + metadatos.getDatabaseProductVersion() + "\n"
                + "Driver: " + metadatos.getDriverName() + " " + metadatos.getDriverVersion() + "\n"
                + "La conexión se cerró automáticamente (try-with-resources).";
    }

    private String describirDriverFaltante(ConfiguracionConexion configuracion,
                                           ClassNotFoundException e) {
        return "ERROR: no se encontró el driver JDBC.\n"
                + "Clase buscada: " + e.getMessage() + "\n"
                + "Solución: agregue el .jar del driver de "
                + configuracion.getMotor() + " a la carpeta lib/ (classpath).";
    }

    /** Recorre la cadena de SQLException y muestra mensaje, SQLState y código de error. */
    private String describirErrorSql(ConfiguracionConexion configuracion, SQLException e) {
        StringBuilder detalle = new StringBuilder("ERROR JDBC al conectar a ")
                .append(configuracion.getUrl()).append("\n");
        SQLException actual = e;
        int numero = 1;
        while (actual != null) {
            detalle.append("[").append(numero++).append("] ")
                    .append(actual.getClass().getSimpleName()).append("\n")
                    .append("   Mensaje: ").append(actual.getMessage()).append("\n")
                    .append("   SQLState: ").append(actual.getSQLState()).append("\n")
                    .append("   Código de error: ").append(actual.getErrorCode()).append("\n");
            actual = actual.getNextException();
        }
        detalle.append("Posible causa: ").append(sugerirCausa(e.getSQLState()));
        return detalle.toString();
    }

    /** Traduce las clases de SQLState más comunes a una sugerencia para el usuario. */
    private String sugerirCausa(String sqlState) {
        if (sqlState == null) {
            return "revise la URL, el servidor y que el driver corresponda al motor.";
        }
        if (sqlState.startsWith("08")) {
            return "no hay comunicación con el servidor (¿está encendido? ¿host y puerto correctos?).";
        }
        if (sqlState.startsWith("28")) {
            return "usuario o contraseña incorrectos.";
        }
        if (sqlState.startsWith("3D") || sqlState.equals("42000")) {
            return "la base de datos no existe o el usuario no tiene permiso sobre ella.";
        }
        return "consulte el mensaje y el SQLState anteriores.";
    }
}
