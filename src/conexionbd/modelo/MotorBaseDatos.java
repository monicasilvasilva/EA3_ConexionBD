package conexionbd.modelo;

/**
 * Motores de base de datos que soporta la aplicación.
 * Cada motor conoce la clase de su driver JDBC, su puerto por defecto
 * y la plantilla de la URL de conexión.
 */
public enum MotorBaseDatos {

    MYSQL("MySQL / MariaDB", "com.mysql.cj.jdbc.Driver", 3306,
            "jdbc:mysql://%s:%d/%s"),
    POSTGRESQL("PostgreSQL", "org.postgresql.Driver", 5432,
            "jdbc:postgresql://%s:%d/%s"),
    SQL_SERVER("SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433,
            "jdbc:sqlserver://%s:%d;databaseName=%s;encrypt=false");

    private final String nombreVisible;
    private final String claseDriver;
    private final int puertoPorDefecto;
    private final String plantillaUrl;

    MotorBaseDatos(String nombreVisible, String claseDriver, int puertoPorDefecto, String plantillaUrl) {
        this.nombreVisible = nombreVisible;
        this.claseDriver = claseDriver;
        this.puertoPorDefecto = puertoPorDefecto;
        this.plantillaUrl = plantillaUrl;
    }

    public String getClaseDriver() {
        return claseDriver;
    }

    public int getPuertoPorDefecto() {
        return puertoPorDefecto;
    }

    /** Arma la URL JDBC con los datos que escribió el usuario. */
    public String construirUrl(String servidor, int puerto, String baseDatos) {
        return String.format(plantillaUrl, servidor, puerto, baseDatos);
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}
