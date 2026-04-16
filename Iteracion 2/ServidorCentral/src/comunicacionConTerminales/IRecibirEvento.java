package comunicacionConTerminales;

public interface IRecibirEvento {
    void suscribirse(IReceptorEvento receptor);

    void desuscribirse(IReceptorEvento receptor);
}
