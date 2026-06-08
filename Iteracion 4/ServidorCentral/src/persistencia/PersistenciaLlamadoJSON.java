package persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gestorFila.RegistroRellamar;

public class PersistenciaLlamadoJSON implements IPersistenciaLlamado {

	private String ARCHIVO = "";

    private Gson gson;

    public PersistenciaLlamadoJSON(String archivo) {
    	this.ARCHIVO=archivo;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void guardarLlamados(RegistroRellamar llamados) {

        try (FileWriter writer = new FileWriter(ARCHIVO)) {

            gson.toJson(llamados, writer);

            System.out.println("PersistenciaLlamadoJSON - Llamados guardados correctamente");

        } catch (IOException e) {
            System.out.println("Error al guardar llamados JSON");
            e.printStackTrace();
        }
    }

    @Override
    public RegistroRellamar cargarLlamados() {

        try (FileReader reader = new FileReader(ARCHIVO)) {

            RegistroRellamar registro =
                    gson.fromJson(reader, RegistroRellamar.class);

            System.out.println("PersistenciaLlamadoJSON - Llamados cargados correctamente");

            return registro;

        } catch (IOException e) {
            System.out.println("No se pudo cargar llamados JSON (No existe el archivo)");
            return null;
        }
    }
}