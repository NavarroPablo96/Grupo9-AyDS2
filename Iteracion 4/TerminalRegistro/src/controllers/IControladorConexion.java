package controllers;

public interface IControladorConexion {

    void iniciar();

    void establecerConexion();
    
    void finalizar();

    String getTipoEncriptado();
    String getClave();

}
