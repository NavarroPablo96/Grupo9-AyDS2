package interfaces;

import eventos.EventoLlamarSiguiente;
import eventos.EventoRellamar;

public interface IAtencion {

	 public void LlamarSiguiente(EventoLlamarSiguiente E, String tipoTerminal, int numeroTerminal);
	 public void Rellamar(EventoRellamar Renoti);
}
