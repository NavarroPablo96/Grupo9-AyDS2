package persistencia;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import eventos.Turno;
import gestorFila.Llamado;
import gestorFila.RegistroRellamar;

public class PersistenciaLlamadoTXT implements IPersistenciaLlamado {
	
	private String NombreArchivo = "";
	
	public PersistenciaLlamadoTXT(String nombreArchivo){
		this.NombreArchivo=nombreArchivo;
	}

	@Override
	public void guardarLlamados(RegistroRellamar llamados) {
        File archivo = new File(this.NombreArchivo);

        if (archivo.exists()) {
            archivo.delete();
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(NombreArchivo))) {
            bw.write("LLAMADOS");
            bw.newLine();
            if (llamados != null) {

                for (Llamado l : llamados.getLlamados()) {

                    Turno t = l.getTurno();

                    String horaRegistro = t.getHoraRegistro();

                    String horaLlamado = "";
                    if (t.getHoraHoraDeLlamado() != null) {
                        horaLlamado = String.valueOf(t.getHoraHoraDeLlamado().getTime());
                    }

                    bw.write(
                            t.getNumero() + ";" +
                            t.getDocumento() + ";" +
                            horaRegistro + ";" +
                            l.getCantidadVecesLlamado() + ";" +
                            l.getNumeroTerminal() + ";" +
                            horaLlamado
                    );

                    bw.newLine();
                }
            }
            
        }catch (IOException e) {
            e.printStackTrace();
        }

	}

	@Override
	public RegistroRellamar cargarLlamados() {
		File archivo = new File(NombreArchivo);
		RegistroRellamar registro= new RegistroRellamar();
        if (!archivo.exists()) {
            return registro;
        }
        else {
        	try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        		String linea = br.readLine();
                if (linea == null || !linea.equals("LLAMADOS")) {
                    return registro;
                }
                while ((linea = br.readLine()) != null) {
                    String[] datos = linea.split(";");
                    int numero = Integer.parseInt(datos[0]);
                    String documento = datos[1];
                    String horaRegistro = datos[2];
                    int cantidadVeces = Integer.parseInt(datos[3]);
                    int terminal = Integer.parseInt(datos[4]);
                    Date horaLlamado = null;
                    if (datos.length > 5 && !datos[5].isEmpty()) {
                        horaLlamado = new Date(Long.parseLong(datos[5]));
                    }
                    Turno turno = new Turno(numero, documento, horaRegistro, horaLlamado);
                    turno.setNumeroTerminal(terminal);
                    Llamado llamado = new Llamado(turno, cantidadVeces, terminal);
                    registro.getLlamados().add(llamado);
                }
        	}catch (IOException e) {
                //e.printStackTrace();
            	System.out.println("PersistenciaLlamadoTXT 85 HUBO ERROR AL CARGAR LLAMADOS");
            }
        	System.out.println("PersistenciaLlamadoTXT 87 llamados cargados");
        	return registro;
        }
	}

}
