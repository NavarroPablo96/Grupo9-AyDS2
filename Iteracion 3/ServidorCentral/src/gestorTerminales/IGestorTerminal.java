package gestorTerminales;

import eventos.ConexionTerminal;

public interface IGestorTerminal {
	public int AgregarTerminal(ConexionTerminal primerEvento,EscuchadorTerminal term);
	public void BajaTerminal(String tipo, int numero) ;
	public void TerminalAgregadaConExito();
}
