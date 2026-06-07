package controller;


public interface IControladorOperador {
    
    void llamarCliente(); 

    void rellamarCliente(); 

    void estadoFilaVacia();
    
    void estadoFilaNoVacia();

    void CartelFilaVacia();

	void abrir();
	
	void cerrar();

    void setEncriptadorApi(String tipo, String clave);
}
