package sistema.modelo;

/**
 *Empleado tipo administrativo
 * Su salario es un monto fijo mensual.
 */
public class EmpleadoAdministrativo extends Empleado {

    private double salarioMensual;

    public EmpleadoAdministrativo(String id, String nombre, String documento,
            String cargo, String fechaIngreso, double salarioMensual) {
        super(id, nombre, documento, cargo, fechaIngreso);
        this.salarioMensual = salarioMensual;
    }

    public double getSalarioMensual() {
        return salarioMensual;
    }

    public void setSalarioMensual(double salarioMensual) {
        this.salarioMensual = salarioMensual;
    }

    @Override
    public double calcularSalario() {
        return salarioMensual;
    }

    @Override
    public String getTipo() {
        return "Administrativo";
    }

    @Override
    public String toString() {
        return super.toString() + " | Salario Mensual: $" + String.format("%.2f", salarioMensual);
    }
}
