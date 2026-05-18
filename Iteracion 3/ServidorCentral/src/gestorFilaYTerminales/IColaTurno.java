package gestorFilaYTerminales;

import java.io.Serializable;
import java.util.List;

import eventos.Turno;

public interface IColaTurno extends Serializable {
	void inicia();
	void pone(Turno t);
	Turno saca();
	
	List<Turno> getListaTurnos();
	int getCantidad();
	void ordenar();
	boolean DniRegistrado(String documento);
	void mostrarCola();
	IColaTurno generarCopia();

}
