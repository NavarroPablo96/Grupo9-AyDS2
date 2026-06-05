package vista;

import controller.IControladorOperador;
import eventos.Turno;

public interface IVistaOperador {
    void abrir();
    void estadoLlamando();
    void setControlador(IControladorOperador c);



    
	void CartelFilaVacia();
	void CartelSeDebeLlamarSiguiente();
	void ActivarBotonNotificar(boolean b);
	void actualizar(Turno ultimoTurnoLlamado, int cantidadEnEspera, int cantidadDeVecesLlamado);
	void ActualizarVistaNumero(int numeroTerminal);
	void estadoFilaNoVacia();
	void cerrar();
    

	
	//void actualizarVistaOperador();
	//void cerrar();
    //void estadoRellamado();
    //void estadoFilaVacia();
    //void mostrarDocumentoCliente(String dni);

}
