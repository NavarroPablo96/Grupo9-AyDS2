package gestorEventos;

import eventos.Evento;

public interface IReceptorEvento {
    void recibirEvento(Evento e);
}
