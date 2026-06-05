package views;

import eventos.Turno;
import java.util.List;

public interface IVistaServidor {
	public void mostrar();
	
	public void cerrar();
	public void setTituloServidor(String titulo);
	
/*	public void actualizar( List<Turno> listaTurnos,
			List<String> listaTerminalesRegistro,
			List<String> listaTerminalesAtencion,
			List<String> listaTerminalesNotificacion,
			String escuchando);*/

	public void actualizarTurnosVistaServidor(List<Turno> turnos);

	public void actualizarTerminalesVistaServidor(List<String> terminalesRegistro, List<String> terminalesAtencion,
			List<String> terminalesNotificacion);
	
	
}
















































