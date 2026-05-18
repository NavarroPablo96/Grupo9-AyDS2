package gestorFilaYTerminales;

public class Terminal {
    private String tipo;
    private int numero;

    public Terminal(String tipo, int numero) {
        this.tipo = tipo;
        this.numero = numero;
    }
    
    public int getNumero() {
    	return this.numero;
    }
    
    public String getTipo() {
    	return this.tipo;
    }

    @Override
    public String toString() {
        return tipo + " " + numero;
    }
}
