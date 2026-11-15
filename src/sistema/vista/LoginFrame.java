package sistema.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import sistema.dao.ConexionMongo;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.security.MessageDigest;

/**
 * Pantalla de inicio de sesión.
 */
public class LoginFrame extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIngresar;
    private JButton btnSalir;

    public LoginFrame() {
        setTitle("Sistema de Administración de Empleados - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel de fondo
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(new Color(150, 150,150));
        // Panel del formulario (centrado)
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(420, 320));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Título
        JLabel lblTitulo = new JLabel("INICIO DE SESIÓN", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setBounds(50, 30, 320, 35);
        panel.add(lblTitulo);

        // Usuario
        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUsuario.setBounds(50, 100, 100, 25);
        panel.add(lblUsuario);

        txtUsuario = new JTextField();
        txtUsuario.setBounds(150, 100, 200, 30);
        panel.add(txtUsuario);

        // Contraseña
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(50, 150, 100, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 150, 200, 30);
        panel.add(txtPassword);

        // Botón Ingresar
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBounds(80, 220, 120, 38);
        btnIngresar.setBackground(new Color(0, 123, 255));
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(btnIngresar);

        // Botón Salir
        btnSalir = new JButton("Salir");
        btnSalir.setBounds(220, 220, 120, 38);
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(btnSalir);

        fondo.add(panel); // Esto centra el panel
        add(fondo);

        // Eventos
        btnIngresar.addActionListener(e -> validarLogin());
        btnSalir.addActionListener(e -> System.exit(0));

        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    validarLogin();
                }
            }
        });
    }
    private void validarLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = new String(txtPassword.getPassword());

        // Validaciones básicas
        if (usuario.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe completar todos los campos.",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            MongoDatabase db = ConexionMongo.getDatabase();
            if (db == null) {
                JOptionPane.showMessageDialog(this,
                        "No se pudo conectar a la base de datos.",
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            MongoCollection<Document> usuarios = db.getCollection("usuarios");
            Document user = usuarios.find(Filters.eq("usuario", usuario)).first();

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos.",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String passwordAlmacenada = user.getString("password");
            String passwordIngresadaHash = encriptarPassword(password);

            if (passwordAlmacenada.equals(passwordIngresadaHash)) {
                JOptionPane.showMessageDialog(this,
                        "¡Bienvenido " + usuario + "!",
                        "Acceso correcto",
                        JOptionPane.INFORMATION_MESSAGE);

                // Cerrar login y abrir menú principal
                this.dispose();
                new MenuPrincipalFrame().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Usuario o contraseña incorrectos.",
                        "Error de autenticación",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al validar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Encripta la contraseña con SHA-256 (no se guarda en texto plano)
     */
    public static String encriptarPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            return null;
        }
    }

    // Aqui creo el usuario de prueba
    public static void crearUsuarioPrueba() {
        try {
            MongoDatabase db = ConexionMongo.getDatabase();
            MongoCollection<Document> usuarios = db.getCollection("usuarios");

            // Verificar si ya existe
            Document existe = usuarios.find(Filters.eq("usuario", "victor")).first();
            if (existe == null) {
                Document nuevo = new Document("usuario", "victor")
                        .append("password", encriptarPassword("victor123"));
                usuarios.insertOne(nuevo);
                System.out.println("Usuario de prueb: victor / victor123");
            }
        } catch (Exception e) {
            System.err.println("Error al crear usuario de prueba: " + e.getMessage());
        }
    }
}
