# EA3 – Incorporando UI y BD

Aplicación de escritorio en Java Swing para escribir la URL, el usuario y la contraseña de una
base de datos, probar la conexión con JDBC y mostrar los errores que ocurran.

Autora: Mónica Silva Silva – Programación Orientada a Objetos II, IU Digital.

## Estructura (patrón MVC)

| Paquete | Clase | ¿Qué hace? |
|---|---|---|
| `conexionbd` | `Main` | Crea la ventana y la muestra. |
| `vista` | `VentanaConexion` | Interfaz gráfica (JFrame + ActionListener). Lee los campos y muestra resultados o errores. |
| `controlador` | `ConexionController` | Valida los datos y le pide a la capa de datos que se conecte. |
| `datos` | `ConexionBD` | Carga el driver y abre la conexión con `DriverManager.getConnection()` en un try-with-resources. |

## Requisitos

- JDK 11 o superior.
- MySQL (XAMPP) con la base de datos `iud_academico`.
- Driver `mysql-connector-j-8.4.0.jar` en la carpeta `lib/` (ya incluido).

## Ejecutar

- En IntelliJ: abrir la carpeta del proyecto y ejecutar `Main`.
- En Windows: doble clic en `ejecutar.bat`.
