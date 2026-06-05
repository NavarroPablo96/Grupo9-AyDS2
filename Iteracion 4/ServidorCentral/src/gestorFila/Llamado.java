package gestorFila;

import java.io.Serializable;

import eventos.Turno;

public class Llamado implements Serializable {
	
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		private Turno turno;

	    private int cantidadVecesLlamado;

	    private int numeroTerminal;
	    
	    public Llamado(Turno t,int cantidad, int numeroTerminal){
	    	this.turno=t;
	    	this.cantidadVecesLlamado=cantidad;
	    	this.numeroTerminal=numeroTerminal;
	    }

		public Turno getTurno() {
			return turno;
		}

		public void setTurno(Turno turno) {
			this.turno = turno;
		}

		public int getCantidadVecesLlamado() {
			return cantidadVecesLlamado;
		}

		public void setCantidadVecesLlamado(int cantidadVecesLlamado) {
			this.cantidadVecesLlamado = cantidadVecesLlamado;
		}

		public int getNumeroTerminal() {
			return numeroTerminal;
		}

		public void setNumeroTerminal(int numeroTerminal) {
			this.numeroTerminal = numeroTerminal;
		}
	    
	    

}
