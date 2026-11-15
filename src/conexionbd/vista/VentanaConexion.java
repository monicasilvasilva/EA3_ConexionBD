package conexionbd.vista;

import conexionbd.modelo.ConfiguracionConexion;
import conexionbd.modelo.MotorBaseDatos;
import conexionbd.modelo.ResultadoConexion;
import conexionbd.servicio.ServicioConexion;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

/**
 * Ventana principal (vista). Solo construye la interfaz, lee los campos y muestra
 * resultados; la conexión la hace ServicioConexion.
 */
public class VentanaConexion extends JFrame {

    private static final Color COLOR_EXITO = new Color(0, 128, 0);
    private static final Color COLOR_ERROR = new Color(180, 0, 0);
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ServicioConexion servicioConexion;

    private JComboBox<MotorBaseDatos> cmbMotor;
    private JTextField txtServidor;
    private JTextField txtPuerto;
    private JTextField txtBaseDatos;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JTextField txtUrlGenerada;
    private JButton btnProbarConexion;
    private JButton btnLimpiar;
    private JLabel lblEstado;
    private JTextArea txtResultados;

    public VentanaConexion(ServicioConexion servicioConexion) {
        this.servicioConexion = servicioConexion;
        configurarVentana();
        inicializarComponentes();
        registrarEventos();
        actualizarUrlGenerada();
    }

    private void configurarVentana() {
        setTitle("EA3 - Validador de conexión JDBC");
        setSize(640, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
    }

    private void inicializarComponentes() {
        JLabel lblTitulo = new JLabel("Configuración de la base de datos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        panelCentral.add(crearPanelFormulario(), BorderLayout.NORTH);
        panelCentral.add(crearPanelResultados(), BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    /** Formulario con GridBagLayout: etiquetas a la izquierda y campos que se estiran. */
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos de conexión"));

        cmbMotor = new JComboBox<>(MotorBaseDatos.values());
        txtServidor = new JTextField("localhost");
        txtPuerto = new JTextField(String.valueOf(MotorBaseDatos.MYSQL.getPuertoPorDefecto()));
        txtBaseDatos = new JTextField("iud_academico");
        txtUsuario = new JTextField("root");
        txtContrasena = new JPasswordField();
        txtUrlGenerada = new JTextField();
        txtUrlGenerada.setEditable(false);

        agregarFila(panel, 0, "Motor:", cmbMotor);
        agregarFila(panel, 1, "Servidor (host):", txtServidor);
        agregarFila(panel, 2, "Puerto:", txtPuerto);
        agregarFila(panel, 3, "Base de datos:", txtBaseDatos);
        agregarFila(panel, 4, "Usuario:", txtUsuario);
        agregarFila(panel, 5, "Contraseña:", txtContrasena);
        agregarFila(panel, 6, "URL JDBC:", txtUrlGenerada);
        return panel;
    }

    private void agregarFila(JPanel panel, int fila, String textoEtiqueta, java.awt.Component campo) {
        GridBagConstraints restricciones = new GridBagConstraints();
        restricciones.insets = new Insets(4, 6, 4, 6);
        restricciones.gridy = fila;

        restricciones.gridx = 0;
        restricciones.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(textoEtiqueta), restricciones);

        restricciones.gridx = 1;
        restricciones.weightx = 1.0;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, restricciones);
    }

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Resultados"));

        lblEstado = new JLabel("Esperando prueba de conexión...");
        lblEstado.setFont(new Font("Arial", Font.BOLD, 13));

        txtResultados = new JTextArea(10, 40);
        txtResultados.setEditable(false);
        txtResultados.setLineWrap(true);
        txtResultados.setWrapStyleWord(true);
        txtResultados.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        panel.add(lblEstado, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtResultados), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnProbarConexion = new JButton("Probar conexión");
        btnLimpiar = new JButton("Limpiar");
        panel.add(btnProbarConexion);
        panel.add(btnLimpiar);
        return panel;
    }

    private void registrarEventos() {
        cmbMotor.addActionListener(e -> cambiarMotor());
        btnProbarConexion.addActionListener(e -> probarConexion());
        btnLimpiar.addActionListener(e -> limpiarResultados());

        javax.swing.event.DocumentListener actualizarUrl = new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarUrlGenerada();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarUrlGenerada();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarUrlGenerada();
            }
        };
        txtServidor.getDocument().addDocumentListener(actualizarUrl);
        txtPuerto.getDocument().addDocumentListener(actualizarUrl);
        txtBaseDatos.getDocument().addDocumentListener(actualizarUrl);
    }

    private void cambiarMotor() {
        MotorBaseDatos motor = (MotorBaseDatos) cmbMotor.getSelectedItem();
        txtPuerto.setText(String.valueOf(motor.getPuertoPorDefecto()));
        actualizarUrlGenerada();
    }

    private void actualizarUrlGenerada() {
        MotorBaseDatos motor = (MotorBaseDatos) cmbMotor.getSelectedItem();
        try {
            int puerto = Integer.parseInt(txtPuerto.getText().trim());
            txtUrlGenerada.setText(motor.construirUrl(txtServidor.getText().trim(), puerto,
                    txtBaseDatos.getText().trim()));
        } catch (NumberFormatException e) {
            txtUrlGenerada.setText("(puerto no válido)");
        }
    }

    /** Lee los campos de la ventana y arma el objeto de configuración. */
    private ConfiguracionConexion leerConfiguracion() {
        int puerto;
        try {
            puerto = Integer.parseInt(txtPuerto.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El puerto debe ser un número entero.");
        }
        return new ConfiguracionConexion(
                (MotorBaseDatos) cmbMotor.getSelectedItem(),
                txtServidor.getText().trim(),
                puerto,
                txtBaseDatos.getText().trim(),
                txtUsuario.getText().trim(),
                txtContrasena.getPassword());
    }

    private void probarConexion() {
        ConfiguracionConexion configuracion;
        try {
            configuracion = leerConfiguracion();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Dato no válido",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        btnProbarConexion.setEnabled(false);
        lblEstado.setForeground(Color.DARK_GRAY);
        lblEstado.setText("Conectando a " + configuracion.getUrl() + " ...");

        // La conexión se hace en segundo plano para que la ventana no se congele.
        new SwingWorker<ResultadoConexion, Void>() {
            @Override
            protected ResultadoConexion doInBackground() {
                return servicioConexion.probarConexion(configuracion);
            }

            @Override
            protected void done() {
                try {
                    mostrarResultado(get());
                } catch (InterruptedException | ExecutionException e) {
                    mostrarResultado(ResultadoConexion.error("Error inesperado: " + e.getMessage()));
                } finally {
                    btnProbarConexion.setEnabled(true);
                }
            }
        }.execute();
    }

    private void mostrarResultado(ResultadoConexion resultado) {
        if (resultado.isExitoso()) {
            lblEstado.setForeground(COLOR_EXITO);
            lblEstado.setText("Conexión exitosa");
        } else {
            lblEstado.setForeground(COLOR_ERROR);
            lblEstado.setText("No fue posible conectar");
        }
        txtResultados.append("[" + LocalTime.now().format(FORMATO_HORA) + "] "
                + resultado.getDetalle() + "\n\n");
        txtResultados.setCaretPosition(txtResultados.getDocument().getLength());
    }

    private void limpiarResultados() {
        txtResultados.setText("");
        lblEstado.setForeground(Color.DARK_GRAY);
        lblEstado.setText("Esperando prueba de conexión...");
    }
}
