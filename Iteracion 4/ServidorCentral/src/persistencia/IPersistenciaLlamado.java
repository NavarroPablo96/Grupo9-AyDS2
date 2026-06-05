package persistencia;

import gestorFila.RegistroRellamar;

public interface IPersistenciaLlamado {
	
    void guardarLlamados(RegistroRellamar llamados);

    RegistroRellamar cargarLlamados();

}
