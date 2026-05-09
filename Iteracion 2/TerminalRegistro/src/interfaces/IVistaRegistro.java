package interfaces;

import eventos.Turno;

public interface IVistaRegistro {

    void setController(IControladorRegistro c);

    void abrir();

    void cerrar();

    void errorDniExistente(String dni);

    void turnoCreado(Turno turno);

    String getDni();

    void ActualizarTitulo(int numero);
    
}
