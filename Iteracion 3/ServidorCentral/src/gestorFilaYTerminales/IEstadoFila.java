package gestorFilaYTerminales;

public interface IEstadoFila {
	IColaTurno getCola();
	void setEstado(IColaTurno c,int cantidadTurnos, int cantidadPone, int cantidadSaca);
	int getCantidadSaca();
	int getCantidadPone();
	int getCantidadTurnos();
}
