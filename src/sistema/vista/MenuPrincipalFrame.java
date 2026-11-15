package sistema.vista;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipalFrame extends JFrame {

    public MenuPrincipalFrame() {
        setTitle("Sistema de Administración de Empleados - Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Fondo
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(new Color(150, 150, 150));

        // Panel del menú
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(450, 420));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        // Título
        JLabel lblTitulo = new JLabel("MENÚ PRINCIPAL", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setBounds(50, 30, 350, 40);
        panel.add(lblTitulo);

        JLabel lblSubtitulo = new JLabel("Sistema de Administración de Empleados", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setBounds(50, 70, 350, 25);
        panel.add(lblSubtitulo);

        // Botones
        JButton btnRegistrar = crearBoton("Registrar Empleado", 100, 130);
        JButton btnListar = crearBoton("Listar Empleados", 100, 190);
        JButton btnCalcular = crearBoton("Calcular Remuneraciones", 100, 250);
        JButton btnCerrarSesion = crearBoton("Cerrar Sesión", 100, 320);
        btnCerrarSesion.setBackground(new Color(139, 0, 0));

        panel.add(btnRegistrar);
        panel.add(btnListar);
        panel.add(btnCalcular);
        panel.add(btnCerrarSesion);

        // Eventos
        btnRegistrar.addActionListener(e -> new FormularioEmpleado().setVisible(true));
        btnListar.addActionListener(e -> new ListaEmpleados().setVisible(true));
        btnCalcular.addActionListener(e -> new ListaEmpleados().setVisible(true));

        btnCerrarSesion.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Desea cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginFrame().setVisible(true);
            }
        });

        fondo.add(panel);
        add(fondo);
    }

    private JButton crearBoton(String texto, int x, int y) {
        JButton boton = new JButton(texto);
        boton.setBounds(x, y, 250, 42);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(new Color(0, 123, 255));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }
}
