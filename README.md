# EA3 – Incorporando UI y BD

Aplicación de escritorio en Java Swing que recibe la configuración de una base de datos,
valida la conexión con JDBC y muestra los errores que ocurran.

## Estructura

```
src/conexionbd/
├── Main.java                      Punto de entrada
├── modelo/
│   ├── MotorBaseDatos.java        enum: MySQL, PostgreSQL, SQL Server (driver, puerto, URL)
│   ├── ConfiguracionConexion.java Datos ingresados por el usuario + validación
│   └── ResultadoConexion.java     Éxito / error + detalle
├── servicio/
│   └── ServicioConexion.java      Class.forName + DriverManager.getConnection (try-with-resources)
└── vista/
    └── VentanaConexion.java       JFrame (BorderLayout + GridBagLayout + FlowLayout)
lib/                               Driver JDBC (mysql-connector-j-8.4.0.jar)
```

## Requisitos

- JDK 11 o superior.
- MySQL (por ejemplo, XAMPP) con la base de datos `iud_academico`.
- Driver: [mysql-connector-j-8.4.0.jar](https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar), copiado en `lib/`.

## Ejecutar

En Windows, doble clic en `ejecutar.bat`. También se puede hacer a mano:

```bash
javac -encoding UTF-8 -d bin src/conexionbd/*.java src/conexionbd/*/*.java
java -cp "bin;lib/*" conexionbd.Main
```

(En Linux o macOS se usa `bin:lib/*`).
