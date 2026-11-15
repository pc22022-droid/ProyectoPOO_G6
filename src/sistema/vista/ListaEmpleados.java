package sistema.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import sistema.dao.EmpleadoDAO;
import sistema.modelo.Empleado;

public class ListaEmpleados extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private EmpleadoDAO dao;
    private JComboBox<String> cmbFiltro;
    private JTextField txtBuscar;
    private JLabel lblTotal;

    public ListaEmpleados() {
        dao = new EmpleadoDAO();

        setTitle("Listado de Empleados");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(245, 245, 250));

        // ========== PANEL SUPERIOR ==========
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setBackground(new Color(245, 245, 250));

        JLabel lblTitulo = new JLabel("LISTADO DE EMPLEADOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createVerticalStrut(15));

        // Filtros y búsqueda
        JPanel panelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelFiltros.setBackground(new Color(245, 245, 250));

        panelFiltros.add(new JLabel("Buscar:"));
        txtBuscar = new JTextField(15);
        panelFiltros.add(txtBuscar);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnBuscar);

        panelFiltros.add(new JLabel("  Tipo:"));
        cmbFiltro = new JComboBox<>(new String[]{
            "Todos", "Administrativo", "Por Hora", "Por Comisión"
        });
        panelFiltros.add(cmbFiltro);

        JButton btnFiltrar = new JButton("Filtrar");
        btnFiltrar.addActionListener(e -> cargarDatos());
        panelFiltros.add(btnFiltrar);

        panelSuperior.add(panelFiltros);
        panel.add(panelSuperior, BorderLayout.NORTH);

        // ========== TABLA ==========
        String[] columnas = {"ID", "Nombre", "Documento", "Tipo", "Cargo", "Salario Calculado", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);

        // ========== PANEL INFERIOR ==========
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(new Color(245, 245, 250));

        // Total de nómina
        lblTotal = new JLabel("Total Nómina (activos): $0.00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInferior.add(lblTotal, BorderLayout.WEST);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        panelBotones.setBackground(new Color(245, 245, 250));

        JButton btnActualizar = crearBoton("Actualizar", new Color(0, 123, 255));
        JButton btnModificar = crearBoton("Modificar", new Color(255, 193, 7));
        JButton btnCambiarEstado = crearBoton("Activar / Inactivar", new Color(108, 117, 125));
        JButton btnCerrar = crearBoton("Cerrar", new Color(220, 53, 69));

        btnActualizar.addActionListener(e -> cargarDatos());
        btnModificar.addActionListener(e -> modificarEmpleado());
        btnCambiarEstado.addActionListener(e -> cambiarEstado());
        btnCerrar.addActionListener(e -> dispose());

        panelBotones.add(btnActualizar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnCambiarEstado);
        panelBotones.add(btnCerrar);

        panelInferior.add(panelBotones, BorderLayout.EAST);
        panel.add(panelInferior, BorderLayout.SOUTH);

        add(panel);
        cargarDatos();
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(150, 38));
        return btn;
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Empleado> lista = dao.listarTodos();
        String filtro = (String) cmbFiltro.getSelectedItem();
        String busqueda = txtBuscar.getText().trim().toLowerCase();

        double totalNomina = 0;

        for (Empleado emp : lista) {
            // Filtro por tipo
            if (!filtro.equals("Todos") && !emp.getTipo().equals(filtro)) {
                continue;
            }

            // Búsqueda por ID o nombre
            if (!busqueda.isEmpty()) {
                boolean coincide = emp.getId().toLowerCase().contains(busqueda)
                        || emp.getNombre().toLowerCase().contains(busqueda);
                if (!coincide) {
                    continue;
                }
            }

            Object[] fila = {
                emp.getId(),
                emp.getNombre(),
                emp.getDocumento(),
                emp.getTipo(),
                emp.getCargo(),
                String.format("$%.2f", emp.calcularSalario()),
                emp.isActivo() ? "Activo" : "Inactivo"
            };
            modelo.addRow(fila);

            // Sumar solo los activos
            if (emp.isActivo()) {
                totalNomina += emp.calcularSalario();
            }
        }

        lblTotal.setText("Total Nómina (activos): $" + String.format("%.2f", totalNomina));
    }

    private void modificarEmpleado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla.");
            return;
        }

        String id = modelo.getValueAt(fila, 0).toString();
        Empleado emp = dao.buscarPorId(id);

        if (emp == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el empleado.");
            return;
        }

        FormularioEmpleado form = new FormularioEmpleado(emp);
        form.setVisible(true);

        form.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                cargarDatos();
            }
        });
    }

    private void cambiarEstado() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado de la tabla.");
            return;
        }

        String id = modelo.getValueAt(fila, 0).toString();
        String nombre = modelo.getValueAt(fila, 1).toString();
        String estadoActual = modelo.getValueAt(fila, 6).toString();

        boolean activar = estadoActual.equals("Inactivo");
        String accion = activar ? "activar" : "inactivar";

        int confirmar = JOptionPane.showConfirmDialog(this,
                "¿Desea " + accion + " al empleado \"" + nombre + "\"?",
                "Confirmar cambio de estado",
                JOptionPane.YES_NO_OPTION);

        if (confirmar == JOptionPane.YES_OPTION) {
            Empleado emp = dao.buscarPorId(id);
            if (emp != null) {
                emp.setActivo(activar);
                if (dao.actualizar(emp)) {
                    JOptionPane.showMessageDialog(this,
                            "Empleado " + (activar ? "activado" : "inactivado") + " correctamente.");
                    cargarDatos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al cambiar el estado.");
                }
            }
        }
    }
}
