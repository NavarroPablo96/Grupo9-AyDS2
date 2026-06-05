package controller;

public interface IControladorConexion {
    void iniciar();

    void establecerConexion();
    
    void finalizar();
    
    void setControlador(IControladorOperador controladorOperador);
}
