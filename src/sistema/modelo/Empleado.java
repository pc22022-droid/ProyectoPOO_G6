package sistema.modelo;

/**
 * Clase abstracta para todo los empleados
 * Aplica el principio de herencia y polimorfismo de la tarea de investigacion que hice con el grupo
 */
public abstract class Empleado {

    private String id;
    private String nombre;
    private String documento;
    private String cargo;
    private String fechaIngreso;
    private boolean activo;

    public Empleado(String id, String nombre, String documento, String cargo, String fechaIngreso) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.cargo = cargo;
        this.fechaIngreso = fechaIngreso;
        this.activo = true;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Método abstracto que cada tipo de empleado debe implementar.
     * Aquí se aplica el polimorfismo.
     */
    public abstract double calcularSalario();

    /**
     * Devuelve el tipo de empleado (Administrativo, Por Hora, Por Comisión)
     */
    public abstract String getTipo();

    @Override
    public String toString() {
        return "ID: " + id + " | Nombre: " + nombre + " | Tipo: " + getTipo()
                + " | Activo: " + (activo ? "Sí" : "No");
    }
}
