package eventos;

public class ConexionTerminal extends Evento{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tipoTerminal;

    public ConexionTerminal(String origen, String destino,String tipo) {
		super(origen, destino);
        this.tipoTerminal=tipo;
	}


    public String getTipoTerminal() {
        return tipoTerminal;
    }


}
