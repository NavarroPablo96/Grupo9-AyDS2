package persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gestorFila.Historial;

public class PersistenciaHistorialJSON implements IPersistenciaHistorial {

	private String ARCHIVO = "";

    private Gson gson;

    public PersistenciaHistorialJSON(String archivo) {
    	this.ARCHIVO=archivo;
    	this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void guardarHistorial(Historial historial) {

        try (FileWriter writer = new FileWriter(ARCHIVO)) {

            gson.toJson(historial, writer);

            System.out.println("PersistenciaHistorialJSON - Historial guardado correctamente");

        } catch (IOException e) {
            System.out.println("Error al guardar historial JSON");
            e.printStackTrace();
        }
    }

    @Override
    public Historial cargarHistorial() {

        try (FileReader reader = new FileReader(ARCHIVO)) {

            Historial historial = gson.fromJson(reader, Historial.class);

            System.out.println("PersistenciaHistorialJSON - Historial cargado correctamente");

            return historial;

        } catch (IOException e) {
            System.out.println("No se pudo cargar historial JSON (No existe el archivo)");
            return null;
        }
    }
}