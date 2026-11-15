package conexionbd.vista;

import conexionbd.controlador.ConexionController;

import javax.swing.*;              // Importa las clases de Swing (JFrame, JLabel, JTextField...)
import java.awt.*;                  // Importa los layouts y colores
import java.awt.event.ActionEvent;  // Evento que se produce al hacer clic en un botón
import java.awt.event.ActionListener;
import java.sql.SQLException;

// La clase principal extiende JFrame para ser la ventana
public class VentanaConexion extends JFrame implements ActionListener {

    // --- Componentes ---
    private JTextField txtUrl;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnProbar;
    private JButton btnLimpiar;
    private JLabel lblEstado;
    private JTextArea areaResultados;

    // Controlador al que la vista le pide probar la conexión (patrón MVC)
    private ConexionController controller;

    public VentanaConexion() {
        controller = new ConexionController();
        configurarVentana();
        inicializarComponentes();
    }

    // 1. Configuración general de la ventana
    private void configurarVentana() {
        setTitle("EA3 - Probar conexión a la base de datos");
        setSize(520, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);          // Centra la ventana en la pantalla
        setLayout(new BorderLayout(10, 10));  // BorderLayout para la ventana principal
        // Margen de 10 píxeles alrededor de todo el contenido
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // 2. Creación y organización de los componentes
    private void inicializarComponentes() {
        // Norte: título
        JLabel lblTitulo = new JLabel("Configuración de la base de datos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // Formulario: las etiquetas a la izquierda (WEST) y los campos en el centro (CENTER).
        // Cada columna usa GridLayout de 3 filas para que queden alineadas.
        JPanel panelFormulario = new JPanel(new BorderLayout(10, 0));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Datos de conexión"));
        JPanel panelEtiquetas = new JPanel(new GridLayout(3, 1, 5, 5));
        JPanel panelCampos = new JPanel(new GridLayout(3, 1, 5, 5));

        panelEtiquetas.add(new JLabel("URL JDBC:"));
        txtUrl = new JTextField("jdbc:mysql://localhost:3306/iud_academico");
        panelCampos.add(txtUrl);

        panelEtiquetas.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField("root");
        panelCampos.add(txtUsuario);

        panelEtiquetas.add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        panelCampos.add(txtContrasena);

        panelFormulario.add(panelEtiquetas, BorderLayout.WEST);
        panelFormulario.add(panelCampos, BorderLayout.CENTER);

        // Resultados: etiqueta de estado y JTextArea con barra de desplazamiento
        JPanel panelResultados = new JPanel(new BorderLayout(5, 5));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        lblEstado = new JLabel("Esperando prueba de conexión...");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 13));
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setLineWrap(true);
        areaResultados.setWrapStyleWord(true);
        panelResultados.add(lblEstado, BorderLayout.NORTH);
        panelResultados.add(new JScrollPane(areaResultados), BorderLayout.CENTER);

        // Centro: formulario arriba y resultados abajo
        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        panelCentro.add(panelFormulario, BorderLayout.NORTH);
        panelCentro.add(panelResultados, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Sur: botones con FlowLayout
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnProbar = new JButton("Probar conexión");
        btnLimpiar = new JButton("Limpiar");
        btnProbar.addActionListener(this);    // La misma ventana escucha los clics
        btnLimpiar.addActionListener(this);
        panelBotones.add(btnProbar);
        panelBotones.add(btnLimpiar);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // 3. Manejador de eventos: se ejecuta cuando se hace clic en un botón
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnProbar) {
            probarConexion();
        } else if (e.getSource() == btnLimpiar) {
            areaResultados.setText("");
            lblEstado.setText("Esperando prueba de conexión...");
            lblEstado.setForeground(Color.BLACK);
        }
    }

    // 4. Lee los campos, se los pasa al controlador y muestra el resultado o el error
    private void probarConexion() {
        // Se toman los datos que escribió el usuario
        String url = txtUrl.getText().trim();
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        try {
            String mensaje = controller.probarConexion(url, usuario, contrasena);
            mostrarResultado(true, mensaje);

        } catch (IllegalArgumentException ex) {
            // El controlador encontró datos vacíos
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);

        } catch (ClassNotFoundException ex) {
            // Falta el .jar del driver en el proyecto
            mostrarResultado(false, "No se encontró el driver JDBC: " + ex.getMessage()
                    + "\nAgregue el archivo mysql-connector-j-8.4.0.jar al proyecto (carpeta lib).");

        } catch (SQLException ex) {
            // Error de JDBC: servidor apagado, base de datos inexistente, clave incorrecta...
            mostrarResultado(false, "Error de JDBC: " + ex.getMessage()
                    + "\nSQLState: " + ex.getSQLState()
                    + "\nCódigo de error: " + ex.getErrorCode());
        }
    }

    // 5. Muestra el mensaje en el JTextArea y cambia el color del estado
    private void mostrarResultado(boolean exitoso, String mensaje) {
        if (exitoso) {
            lblEstado.setText("Conexión exitosa");
            lblEstado.setForeground(new Color(0, 128, 0));   // Verde
        } else {
            lblEstado.setText("No fue posible conectar");
            lblEstado.setForeground(Color.RED);
        }
        areaResultados.append(mensaje + "\n\n");
    }
}
