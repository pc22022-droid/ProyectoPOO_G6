package sistema.modelo;

/**
 * EWmpleado por hora
 * Salario = tarifa por hora × horas trabajadas
 */
public class EmpleadoPorHora extends Empleado {

    private double tarifaPorHora;
    private double horasTrabajadas;

    public EmpleadoPorHora(String id, String nombre, String documento,
            String cargo, String fechaIngreso, double tarifaPorHora, double horasTrabajadas) {
        super(id, nombre, documento, cargo, fechaIngreso);
        this.tarifaPorHora = tarifaPorHora;
        this.horasTrabajadas = horasTrabajadas;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public void setTarifaPorHora(double tarifaPorHora) {
        this.tarifaPorHora = tarifaPorHora;
    }

    public double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    @Override
    public double calcularSalario() {
        return tarifaPorHora * horasTrabajadas;
    }

    @Override
    public String getTipo() {
        return "Por Hora";
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Tarifa/Hora: $" + String.format("%.2f", tarifaPorHora)
                + " | Horas: " + horasTrabajadas;
    }
}
