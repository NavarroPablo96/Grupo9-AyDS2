package controllers;

import eventos.Turno;

public interface IControladorRegistro {
    
	void iniciar(int numeroTerminal);

    void registrarTurno();

	void errorDniExistente(String dni);

	void turnoCreado(Turno nuevo);

	void setEncriptadorApi(String tipo, String clave);
}
