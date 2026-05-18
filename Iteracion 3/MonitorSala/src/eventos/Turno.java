package eventos;


import java.io.Serializable;
import java.util.Date;

public class Turno implements Serializable {

    private static final long serialVersionUID = 1L;
	private String Documento,HoraReg;
	private int Numero;
	private Date HoraDeLlamado;
	private int NumeroTerminalQueLLama;
	public Turno(int numero, String documento, String horaReg,Date horaDeLlamado) {
		Numero = numero;
		Documento = documento;
		HoraReg = horaReg;
		HoraDeLlamado=horaDeLlamado;
	}

	public int getNumero() {
		return Numero;
	}

	public String getDocumento() {
		return Documento;
	}

	public String getHoraRegistro() {
		return HoraReg;
	}
	
	public Date getHoraHoraDeLlamado() {
		return HoraDeLlamado;
	}
	
	public void setHoraDeLlamado(Date date) {
		this.HoraDeLlamado=date;
	}

	public int getTerminal() {
		return this.NumeroTerminalQueLLama;
	}

	public void setNumeroTerminal(int numeroTerminalQueLLama) {
		NumeroTerminalQueLLama = numeroTerminalQueLLama;
	}
}
