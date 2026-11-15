package sistema.vista;

import javax.swing.*;
import java.awt.*;
import sistema.dao.EmpleadoDAO;
import sistema.modelo.*;

public class FormularioEmpleado extends JFrame {

    private JTextField txtId, txtNombre, txtDocumento, txtCargo, txtFecha;
    private JComboBox<String> cmbTipo;
    private JTextField txtDato1, txtDato2;
    private JLabel lblDato1, lblDato2;
    private JButton btnGuardar, btnCancelar;
    private EmpleadoDAO dao;
    private boolean modoEdicion = false;
    private Empleado empleadoActual = null;

    // Constructor para registrar nuevo
    public FormularioEmpleado() {
        this(null);
    }

    // Constructor para modificar
    public FormularioEmpleado(Empleado empleado) {
        dao = new EmpleadoDAO();
        this.empleadoActual = empleado;
        this.modoEdicion = (empleado != null);

        setTitle(modoEdicion ? "Modificar Empleado" : "Registrar Empleado");
        setSize(500, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(245, 245, 250));

        JLabel lblTitulo = new JLabel(modoEdicion ? "MODIFICAR EMPLEADO" : "REGISTRAR EMPLEADO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setBounds(50, 15, 400, 30);
        panel.add(lblTitulo);

        int y = 60;
        panel.add(crearLabel("ID:", 40, y));
        txtId = crearTextField(160, y);
        panel.add(txtId);

        y += 40;
        panel.add(crearLabel("Nombre:", 40, y));
        txtNombre = crearTextField(160, y);
        panel.add(txtNombre);

        y += 40;
        panel.add(crearLabel("Documento:", 40, y));
        txtDocumento = crearTextField(160, y);
        panel.add(txtDocumento);

        y += 40;
        panel.add(crearLabel("Cargo:", 40, y));
        txtCargo = crearTextField(160, y);
        panel.add(txtCargo);

        y += 40;
        panel.add(crearLabel("Fecha Ingreso:", 40, y));
        txtFecha = crearTextField(160, y);
        panel.add(txtFecha);

        y += 40;
        panel.add(crearLabel("Tipo Empleado:", 40, y));
        cmbTipo = new JComboBox<>(new String[]{
            "Administrativo", "Por Hora", "Por Comisión"
        });
        cmbTipo.setBounds(160, y, 260, 28);
        panel.add(cmbTipo);

        y += 45;
        lblDato1 = crearLabel("Salario Mensual:", 40, y);
        panel.add(lblDato1);
        txtDato1 = crearTextField(160, y);
        panel.add(txtDato1);

        y += 40;
        lblDato2 = crearLabel("", 40, y);
        panel.add(lblDato2);
        txtDato2 = crearTextField(160, y);
        txtDato2.setVisible(false);
        panel.add(txtDato2);

        cmbTipo.addActionListener(e -> actualizarCampos());

        btnGuardar = new JButton(modoEdicion ? "Actualizar" : "Guardar");
        btnGuardar.setBounds(110, 420, 120, 35);
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        panel.add(btnGuardar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBounds(260, 420, 120, 35);
        btnCancelar.setBackground(new Color(108, 117, 125));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(e -> guardarEmpleado());
        btnCancelar.addActionListener(e -> dispose());

        add(panel);

        if (modoEdicion) {
            cargarDatosEmpleado();
        } else {
            txtFecha.setText("2025-01-01");
            actualizarCampos();
        }
    }

    private void cargarDatosEmpleado() {
        txtId.setText(empleadoActual.getId());
        txtId.setEditable(false);
        txtNombre.setText(empleadoActual.getNombre());
        txtDocumento.setText(empleadoActual.getDocumento());
        txtCargo.setText(empleadoActual.getCargo());
        txtFecha.setText(empleadoActual.getFechaIngreso());
        cmbTipo.setSelectedItem(empleadoActual.getTipo());
        cmbTipo.setEnabled(false);

        if (empleadoActual instanceof EmpleadoAdministrativo) {
            EmpleadoAdministrativo a = (EmpleadoAdministrativo) empleadoActual;
            txtDato1.setText(String.valueOf(a.getSalarioMensual()));
        } else if (empleadoActual instanceof EmpleadoPorHora) {
            EmpleadoPorHora h = (EmpleadoPorHora) empleadoActual;
            txtDato1.setText(String.valueOf(h.getTarifaPorHora()));
            txtDato2.setText(String.valueOf(h.getHorasTrabajadas()));
        } else if (empleadoActual instanceof EmpleadoPorComision) {
            EmpleadoPorComision c = (EmpleadoPorComision) empleadoActual;
            txtDato1.setText(String.valueOf(c.getPorcentajeComision()));
            txtDato2.setText(String.valueOf(c.getMontoVentas()));
        }
        actualizarCampos();
    }

    private void actualizarCampos() {
        String tipo = (String) cmbTipo.getSelectedItem();
        if (tipo.equals("Administrativo")) {
            lblDato1.setText("Salario Mensual:");
            lblDato2.setText("");
            txtDato2.setVisible(false);
        } else if (tipo.equals("Por Hora")) {
            lblDato1.setText("Tarifa por Hora:");
            lblDato2.setText("Horas Trabajadas:");
            txtDato2.setVisible(true);
        } else {
            lblDato1.setText("% Comisión:");
            lblDato2.setText("Monto Ventas:");
            txtDato2.setVisible(true);
        }
    }

    private void guardarEmpleado() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String documento = txtDocumento.getText().trim();
            String cargo = txtCargo.getText().trim();
            String fecha = txtFecha.getText().trim();
            String tipo = (String) cmbTipo.getSelectedItem();

            // Validaciones de campos vacíos
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID es obligatorio.");
                txtId.requestFocus();
                return;
            }
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre es obligatorio.");
                txtNombre.requestFocus();
                return;
            }
            if (documento.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El documento es obligatorio.");
                txtDocumento.requestFocus();
                return;
            }
            if (cargo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El cargo es obligatorio.");
                txtCargo.requestFocus();
                return;
            }
            if (fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "La fecha de ingreso es obligatoria.");
                txtFecha.requestFocus();
                return;
            }
            if (txtDato1.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el valor numérico requerido.");
                txtDato1.requestFocus();
                return;
            }

            if (!modoEdicion && dao.existeId(id)) {
                JOptionPane.showMessageDialog(this, "Ya existe un empleado con ese ID.");
                txtId.requestFocus();
                return;
            }

            double valor1 = Double.parseDouble(txtDato1.getText().trim());
            if (valor1 < 0) {
                JOptionPane.showMessageDialog(this, "Los valores numéricos no pueden ser negativos.");
                txtDato1.requestFocus();
                return;
            }

            Empleado emp = null;

            if (tipo.equals("Administrativo")) {
                emp = new EmpleadoAdministrativo(id, nombre, documento, cargo, fecha, valor1);
            } else if (tipo.equals("Por Hora")) {
                if (txtDato2.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar las horas trabajadas.");
                    txtDato2.requestFocus();
                    return;
                }
                double valor2 = Double.parseDouble(txtDato2.getText().trim());
                if (valor2 < 0) {
                    JOptionPane.showMessageDialog(this, "Las horas no pueden ser negativas.");
                    txtDato2.requestFocus();
                    return;
                }
                emp = new EmpleadoPorHora(id, nombre, documento, cargo, fecha, valor1, valor2);
            } else {
                if (txtDato2.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar el monto de ventas.");
                    txtDato2.requestFocus();
                    return;
                }
                double valor2 = Double.parseDouble(txtDato2.getText().trim());
                if (valor1 < 0 || valor1 > 100) {
                    JOptionPane.showMessageDialog(this, "El porcentaje de comisión debe estar entre 0 y 100.");
                    txtDato1.requestFocus();
                    return;
                }
                if (valor2 < 0) {
                    JOptionPane.showMessageDialog(this, "El monto de ventas no puede ser negativo.");
                    txtDato2.requestFocus();
                    return;
                }
                emp = new EmpleadoPorComision(id, nombre, documento, cargo, fecha, valor1, valor2);
            }

            if (modoEdicion) {
                emp.setActivo(empleadoActual.isActivo());
            }

            boolean resultado = modoEdicion ? dao.actualizar(emp) : dao.insertar(emp);

            if (resultado) {
                JOptionPane.showMessageDialog(this,
                        modoEdicion ? "Empleado actualizado correctamente." : "Empleado registrado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el empleado.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Los valores numéricos no son válidos. Ingrese solo números.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
    private JLabel crearLabel(String texto, int x, int y) {
        JLabel lbl = new JLabel(texto);
        lbl.setBounds(x, y, 120, 25);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return lbl;
    }

    private JTextField crearTextField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 260, 28);
        return txt;
    }
}
