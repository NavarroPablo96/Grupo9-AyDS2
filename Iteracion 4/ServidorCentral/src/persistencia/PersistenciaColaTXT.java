package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import eventos.Turno;
import gestorFila.ColaTurno;
import gestorFila.EstadoCola;

public class PersistenciaColaTXT implements IPersistenciaCola {

	@Override
	public void guardarCola(EstadoCola cola) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("cola.txt"))) {

            bw.write("COLA");
            bw.newLine();
            // Metadatos
            bw.write(cola.getCantidadPone() + ";" +
            		cola.getCantidadSaca() + ";" +
            		cola.getNumeroTurnoSiguiente());
            bw.newLine();
            ColaTurno colaReal = cola.getCola();

            for (Turno t : colaReal.getListaTurnos()) {

                String horaLlamado = "";

                if (t.getHoraHoraDeLlamado() != null) {
                    horaLlamado = String.valueOf(t.getHoraHoraDeLlamado().getTime());
                }

                bw.write(
                    t.getNumero() + ";" +
                    t.getDocumento() + ";" +
                    t.getHoraRegistro() + ";" +
                    horaLlamado
                );

                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("PersistenciaColaTXT-51 Se guardo la cola");
	}

	@Override
	public EstadoCola cargarCola() {
		File archivo = new File("cola.txt");
		EstadoCola aux = new EstadoCola();
        if (!archivo.exists()) {
            return aux;
        }
        else {
        	try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        		String linea;
                linea = br.readLine();	
                if (linea == null || !linea.equals("COLA")) {
                    return aux;
                }
                linea = br.readLine();
                String[] meta = linea.split(";");
                int cantidadPone = Integer.parseInt(meta[0]);
                int cantidadSaca = Integer.parseInt(meta[1]);
                int numeroTurnoSiguiente = Integer.parseInt(meta[2]);

                ColaTurno cola = new ColaTurno();

                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(";");
                    int numero = Integer.parseInt(datos[0]);
                    String documento = datos[1];
                    String horaRegistro = datos[2];
                    Date horaLlamado = null;
                    if (datos.length > 3 && !datos[3].isEmpty()) {
                        horaLlamado = new Date(Long.parseLong(datos[3]));
                    }
                    Turno turno = new Turno(
                            numero,
                            documento,
                            horaRegistro,
                            horaLlamado
                    );
                    cola.pone(turno);
                }//FIN WHILE
                aux.setCola(cola, numeroTurnoSiguiente, cantidadPone, cantidadSaca);
        	}
        	catch (IOException e) {
                //e.printStackTrace();
            	System.out.println("PersistenciaColaTXT-97 HUBO ERROR AL CARGAR COLA");
            }
            System.out.println("PersistenciaColaTXT-99 Cola Cargada");
        	return aux;
        }
	}
	
}
