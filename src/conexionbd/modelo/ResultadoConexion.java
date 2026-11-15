package conexionbd.modelo;

/**
 * Resultado de un intento de conexión: si fue exitoso y el detalle para mostrar al usuario.
 */
public class ResultadoConexion {

    private final boolean exitoso;
    private final String detalle;

    private ResultadoConexion(boolean exitoso, String detalle) {
        this.exitoso = exitoso;
        this.detalle = detalle;
    }

    public static ResultadoConexion exito(String detalle) {
        return new ResultadoConexion(true, detalle);
    }

    public static ResultadoConexion error(String detalle) {
        return new ResultadoConexion(false, detalle);
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getDetalle() {
        return detalle;
    }
}
