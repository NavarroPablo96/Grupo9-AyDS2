package persistencia;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eventos.Turno;
import gestorFila.ColaTurno;
import gestorFila.EstadoCola;

public class PersistenciaColaJSON implements IPersistenciaCola {

    private static final String ARCHIVO = "cola.json";

    private Gson gson;

    public PersistenciaColaJSON() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void guardarCola(EstadoCola cola) {

        try (FileWriter writer = new FileWriter(ARCHIVO)) {

            gson.toJson(cola, writer);

            System.out.println("PersistenciaColaJSON - Cola guardada correctamente");

        } catch (IOException e) {
            System.out.println("Error al guardar cola JSON");
            e.printStackTrace();
        }
    }

    @Override
    public EstadoCola cargarCola() {

        try (FileReader reader = new FileReader(ARCHIVO)) {

            EstadoCola estado = gson.fromJson(reader, EstadoCola.class);
            System.out.println( "Turnos cargados JSON = " +estado.getCola().getCantidadTurnos());
            if (estado != null && estado.getCola() != null) {

                // IMPORTANTE: reconstruir implementación concreta
                ColaTurno cola = new ColaTurno();

                List<Turno> lista = estado.getCola().getListaTurnos();

                if (lista != null) {
                    for (Turno t : lista) {
                        cola.pone(t);
                    }
                }

                estado.setCola(
                        cola,
                        estado.getNumeroTurnoSiguiente(),
                        estado.getCantidadPone(),
                        estado.getCantidadSaca()
                );
            }

            System.out.println("PersistenciaColaJSON - Cola cargada correctamente");

            return estado;

        } catch (IOException e) {
            System.out.println("No se pudo cargar cola JSON (No existe el archivo)");
            return null;
        }
    }
}