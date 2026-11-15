package sistema.dao;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import sistema.modelo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de las operaciones con empleados
 */
public class EmpleadoDAO {

    private MongoCollection<Document> coleccion;

    public EmpleadoDAO() {
        MongoDatabase db = ConexionMongo.getDatabase();
        if (db != null) {
            coleccion = db.getCollection("empleados");
        }
    }

    public boolean insertar(Empleado emp) {
        try {
            Document doc = convertirADocumento(emp);
            coleccion.insertOne(doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public List<Empleado> listarTodos() {
        List<Empleado> lista = new ArrayList<>();
        try {
            for (Document doc : coleccion.find()) {
                Empleado emp = convertirAEmpleado(doc);
                if (emp != null) {
                    lista.add(emp);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public Empleado buscarPorId(String id) {
        try {
            Document doc = coleccion.find(Filters.eq("id", id)).first();
            if (doc != null) {
                return convertirAEmpleado(doc);
            }
        } catch (Exception e) {
            System.err.println("Error al buscar: " + e.getMessage());
        }
        return null;
    }

    public boolean existeId(String id) {
        try {
            return coleccion.find(Filters.eq("id", id)).first() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean actualizar(Empleado emp) {
        try {
            Document doc = convertirADocumento(emp);
            coleccion.replaceOne(Filters.eq("id", emp.getId()), doc);
            return true;
        } catch (Exception e) {
            System.err.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean inactivar(String id) {
        try {
            coleccion.updateOne(
                    Filters.eq("id", id),
                    new Document("$set", new Document("activo", false))
            );
            return true;
        } catch (Exception e) {
            System.err.println("Error al inactivar: " + e.getMessage());
            return false;
        }
    }

    private Document convertirADocumento(Empleado emp) {
        Document doc = new Document();
        doc.append("id", emp.getId());
        doc.append("nombre", emp.getNombre());
        doc.append("documento", emp.getDocumento());
        doc.append("cargo", emp.getCargo());
        doc.append("fechaIngreso", emp.getFechaIngreso());
        doc.append("activo", emp.isActivo());
        doc.append("tipo", emp.getTipo());

        if (emp instanceof EmpleadoAdministrativo) {
            EmpleadoAdministrativo a = (EmpleadoAdministrativo) emp;
            doc.append("salarioMensual", a.getSalarioMensual());
        } else if (emp instanceof EmpleadoPorHora) {
            EmpleadoPorHora h = (EmpleadoPorHora) emp;
            doc.append("tarifaPorHora", h.getTarifaPorHora());
            doc.append("horasTrabajadas", h.getHorasTrabajadas());
        } else if (emp instanceof EmpleadoPorComision) {
            EmpleadoPorComision c = (EmpleadoPorComision) emp;
            doc.append("porcentajeComision", c.getPorcentajeComision());
            doc.append("montoVentas", c.getMontoVentas());
        }
        return doc;
    }

    private Empleado convertirAEmpleado(Document doc) {
        String tipo = doc.getString("tipo");
        String id = doc.getString("id");
        String nombre = doc.getString("nombre");
        String documento = doc.getString("documento");
        String cargo = doc.getString("cargo");
        String fecha = doc.getString("fechaIngreso");
        boolean activo = doc.getBoolean("activo", true);

        Empleado emp = null;

        switch (tipo) {
            case "Administrativo":
                emp = new EmpleadoAdministrativo(id, nombre, documento, cargo, fecha,
                        doc.getDouble("salarioMensual"));
                break;
            case "Por Hora":
                emp = new EmpleadoPorHora(id, nombre, documento, cargo, fecha,
                        doc.getDouble("tarifaPorHora"),
                        doc.getDouble("horasTrabajadas"));
                break;
            case "Por Comisión":
                emp = new EmpleadoPorComision(id, nombre, documento, cargo, fecha,
                        doc.getDouble("porcentajeComision"),
                        doc.getDouble("montoVentas"));
                break;
        }

        if (emp != null) {
            emp.setActivo(activo);
        }
        return emp;
    }
}
