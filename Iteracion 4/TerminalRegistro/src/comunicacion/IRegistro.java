package comunicacion;

import seguridad.ISeguridadStrategy;

public interface IRegistro {

	void nuevoTurno(String dni,int NumeroTerminal);

	void setEncriptador(ISeguridadStrategy crypt);

}
