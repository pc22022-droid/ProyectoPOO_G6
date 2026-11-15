package sistema.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * 
 */
public class ConexionMongo {

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    private static final String URI = "mongodb://localhost:27017";
    private static final String NOMBRE_BD = "sistema_empleados";

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(NOMBRE_BD);
                System.out.println("Conexión a MongoDB exitosa.");
            } catch (Exception e) {
                System.err.println("Error al conectar con MongoDB: " + e.getMessage());
            }
        }
        return database;
    }

    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println("Conexión cerrada.");
        }
    }
}
