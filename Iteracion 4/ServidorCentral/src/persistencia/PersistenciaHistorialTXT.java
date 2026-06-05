package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import eventos.Turno;
import gestorFila.Historial;

public class PersistenciaHistorialTXT implements IPersistenciaHistorial {

	@Override
	public void guardarHistorial(Historial historial) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("historial.txt"))) {
        	bw.write("HISTORIAL");
            bw.newLine();
            if (historial != null) {
                // ACTUAL
                Turno actual = historial.getTurnoActual();
                if (actual != null) {
                    String horaLlamado = "";
                    if (actual.getHoraHoraDeLlamado() != null) {
                        horaLlamado = String.valueOf(actual.getHoraHoraDeLlamado().getTime());
                    }
                    bw.write("ACTUAL;"
                            + actual.getNumero() + ";"
                            + actual.getDocumento() + ";"
                            + actual.getHoraRegistro() + ";"
                            + horaLlamado + ";"
                            + actual.getTerminal());
                    bw.newLine();
                }

                // HISTORIAL (últimos 4)
                for (Turno t : historial.getHistorial()) {
                    String horaLlamado = "";
                    if (t.getHoraHoraDeLlamado() != null) {
                        horaLlamado = String.valueOf(t.getHoraHoraDeLlamado().getTime());
                    }
                    bw.write("HIST;"
                            + t.getNumero() + ";"
                            + t.getDocumento() + ";"
                            + t.getHoraRegistro() + ";"
                            + horaLlamado + ";"
                            + t.getTerminal());
                    bw.newLine();
                }
            	System.out.println("PersistenciaHistorialTXT-52"+historial.getHistorial().size()); 
            }
            else {
            	System.out.println("PersistenciaHistorialTXT-55-estado.getHistorial()==NULL");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("PersistenciaHistorialTXT-60- Fin-guardarHistorial");
	}

	
	@Override
	public Historial cargarHistorial() {
		File archivo = new File("historial.txt");
		Historial historial = new Historial();
		if (!archivo.exists()) {
		    return historial;
		}
		else {
			try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
			    String linea = br.readLine();
			    if (linea == null || !linea.equals("HISTORIAL")) {
			        return historial;
			    }
			    while ((linea = br.readLine()) != null) {
			        String[] datos = linea.split(";");
			        String tipo = datos[0];
			        int numero = Integer.parseInt(datos[1]);
			        String documento = datos[2];
			        String horaRegistro = datos[3];
			        Date horaLlamado = null;
			        if (!datos[4].isEmpty()) {
			            horaLlamado = new Date(Long.parseLong(datos[4]));
			        }
			        int terminal = Integer.parseInt(datos[5]);
			        Turno turno = new Turno(numero, documento, horaRegistro, horaLlamado);
			        turno.setNumeroTerminal(terminal);
			        if ("ACTUAL".equals(tipo)) {
			            historial.setTurnoActual(turno);
			        } else if ("HIST".equals(tipo)) {
			            historial.getHistorial().add(turno);
			        }
			    }
			}catch (IOException e) {
				//e.printStackTrace();
				System.out.println("PersistenciaHistorialTXT-98-HUBO ERROR AL CARGAR HISTORIAL");
			}
		}
		System.out.println("PersistenciaHistorialTXT-101historial cargado");
		return historial;
	}
}
