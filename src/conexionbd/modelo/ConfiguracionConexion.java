package conexionbd.modelo;

/**
 * Datos de configuración que el usuario ingresa en la ventana.
 * Es una clase de modelo: solo guarda y valida datos, no sabe nada de Swing ni de JDBC.
 */
public class ConfiguracionConexion {

    private final MotorBaseDatos motor;
    private final String servidor;
    private final int puerto;
    private final String baseDatos;
    private final String usuario;
    private final char[] contrasena;

    public ConfiguracionConexion(MotorBaseDatos motor, String servidor, int puerto,
                                 String baseDatos, String usuario, char[] contrasena) {
        this.motor = motor;
        this.servidor = servidor;
        this.puerto = puerto;
        this.baseDatos = baseDatos;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public MotorBaseDatos getMotor() {
        return motor;
    }

    public String getServidor() {
        return servidor;
    }

    public int getPuerto() {
        return puerto;
    }

    public String getBaseDatos() {
        return baseDatos;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return new String(contrasena);
    }

    /** URL JDBC construida dinámicamente a partir de los campos del formulario. */
    public String getUrl() {
        return motor.construirUrl(servidor, puerto, baseDatos);
    }

    /**
     * Revisa que los datos obligatorios estén completos y que el puerto sea válido.
     *
     * @throws IllegalArgumentException si falta algún dato
     */
    public void validar() {
        if (servidor == null || servidor.isBlank()) {
            throw new IllegalArgumentException("El servidor (host) es obligatorio.");
        }
        if (puerto < 1 || puerto > 65535) {
            throw new IllegalArgumentException("El puerto debe estar entre 1 y 65535.");
        }
        if (baseDatos == null || baseDatos.isBlank()) {
            throw new IllegalArgumentException("El nombre de la base de datos es obligatorio.");
        }
        if (usuario == null || usuario.isBlank()) {
            throw new IllegalArgumentException("El usuario es obligatorio.");
        }
    }
}
