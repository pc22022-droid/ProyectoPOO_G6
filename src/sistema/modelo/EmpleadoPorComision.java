package sistema.modelo;

/**
 *Empleado por comiisión
 * Salario = monto de ventas × (porcentaje de comisión / 100)
 */
public class EmpleadoPorComision extends Empleado {

    private double porcentajeComision;
    private double montoVentas;

    public EmpleadoPorComision(String id, String nombre, String documento,
            String cargo, String fechaIngreso, double porcentajeComision, double montoVentas) {
        super(id, nombre, documento, cargo, fechaIngreso);
        this.porcentajeComision = porcentajeComision;
        this.montoVentas = montoVentas;
    }

    public double getPorcentajeComision() {
        return porcentajeComision;
    }

    public void setPorcentajeComision(double porcentajeComision) {
        this.porcentajeComision = porcentajeComision;
    }

    public double getMontoVentas() {
        return montoVentas;
    }

    public void setMontoVentas(double montoVentas) {
        this.montoVentas = montoVentas;
    }

    @Override
    public double calcularSalario() {
        return montoVentas * (porcentajeComision / 100.0);
    }

    @Override
    public String getTipo() {
        return "Por Comisión";
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Comisión: " + porcentajeComision + "%"
                + " | Ventas: $" + String.format("%.2f", montoVentas);
    }
}
