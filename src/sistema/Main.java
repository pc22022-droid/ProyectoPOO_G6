package sistema;

import sistema.vista.LoginFrame;

/**
 *Clase principal de administracion de empleados
 */
public class Main {

    public static void main(String[] args) {
        // Crear usuario de prueba para poder usar el ingreso
        LoginFrame.crearUsuarioPrueba();

        // Abrir la pantalla de Login.
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
