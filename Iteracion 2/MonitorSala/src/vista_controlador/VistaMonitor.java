package vista_controlador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import eventos.Turno;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class VistaMonitor extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblFechaHora;
	private JLabel lblDniActual;
	private JLabel lblTurnoActual;
	private JPanel panelLista;
	private JLabel lblTitulo;
	
	public VistaMonitor() {
		setTitle("Monitor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 500);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(245, 245, 245));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		// ================= HEADER =================
		JPanel panelHeader = new JPanel(new BorderLayout());
		panelHeader.setBackground(new Color(33, 150, 243)); 
		panelHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		lblTitulo = new JLabel("Terminal de Notificación");
		lblTitulo.setForeground(Color.WHITE);
		lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));

		lblFechaHora = new JLabel();
		lblFechaHora.setForeground(Color.WHITE);
		lblFechaHora.setFont(new Font("Arial", Font.PLAIN, 14));
		lblFechaHora.setHorizontalAlignment(SwingConstants.RIGHT);

		panelHeader.add(lblTitulo, BorderLayout.WEST);
		panelHeader.add(lblFechaHora, BorderLayout.EAST);

		contentPane.add(panelHeader, BorderLayout.NORTH);

		// ================= CENTRO =================
		JPanel panelCentro = new JPanel();
		panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
		contentPane.add(panelCentro, BorderLayout.CENTER);

		// ---- TURNO ACTUAL ----
		JLabel lblTituloTurno = new JLabel("TURNO DE ATENCIÓN");
		lblTituloTurno.setFont(new Font("Arial", Font.BOLD, 16));
		lblTituloTurno.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblDniActual = new JLabel("00.000.000");
		lblDniActual.setFont(new Font("Arial", Font.BOLD, 32));
		lblDniActual.setAlignmentX(Component.CENTER_ALIGNMENT);

		lblTurnoActual = new JLabel("Turno #000 - Diríjase al mostrador");
		lblTurnoActual.setFont(new Font("Arial", Font.PLAIN, 14));
		lblTurnoActual.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel panelTurno = new JPanel();
		panelTurno.setLayout(new BoxLayout(panelTurno, BoxLayout.Y_AXIS));
		panelTurno.setBackground(new Color(33, 33, 33)); 
		panelTurno.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));

		lblTituloTurno.setForeground(Color.LIGHT_GRAY);
		lblDniActual.setForeground(Color.WHITE);
		lblTurnoActual.setForeground(Color.WHITE);

		panelTurno.add(Box.createVerticalStrut(10));
		panelTurno.add(lblTituloTurno);
		panelTurno.add(Box.createVerticalStrut(10));
		panelTurno.add(lblDniActual);
		panelTurno.add(Box.createVerticalStrut(5));
		panelTurno.add(lblTurnoActual);
		panelTurno.add(Box.createVerticalStrut(10));

		panelCentro.add(panelTurno);
		panelCentro.add(Box.createRigidArea(new Dimension(0, 20)));

		// ---- LISTA TURNOS ----
		JLabel lblListaTitulo = new JLabel("ULTIMOS 5 TURNOS LLAMADOS");
		lblListaTitulo.setFont(new Font("Arial", Font.BOLD, 14));
		lblListaTitulo.setForeground(new Color(33, 33, 33));
		
		panelCentro.add(lblListaTitulo);
		panelCentro.add(Box.createRigidArea(new Dimension(0, 10)));

		panelLista = new JPanel();
		panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

		panelCentro.add(panelLista);
		panelCentro.add(Box.createVerticalStrut(10));
		
		// Timer para actualizar hora en vivo
		new Timer(1000, e -> actualizarFechaHora()).start();
	}

	// ================= ACTUALIZAR =================
	public void actualizar(List<Turno> ultimosTurnos, Turno ultimoTurnoLlamado) {

		// Actualizar turno actual
		if (ultimoTurnoLlamado != null) {
			lblDniActual.setText(formatearDni(ultimoTurnoLlamado.getDocumento()));
			lblTurnoActual.setText("Turno #" +
					String.format("%03d", ultimoTurnoLlamado.getNumero()) +
					" - Diríjase al mostrador");
		}

		// Limpiar lista
		panelLista.removeAll();

		// Mostrar ultimos 5 turnos con el mas reciente en la primera posicion.
		for (int i = 0; i < ultimosTurnos.size() && i < 5; i++) {
			Turno t = ultimosTurnos.get(i);

			JPanel fila = new JPanel(new BorderLayout());
			fila.setBackground(i == 0 ? new Color(232, 245, 255) : new Color(255, 255, 255));
			fila.setBorder(BorderFactory.createCompoundBorder(
			        BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
			        new EmptyBorder(8, 10, 8, 10)
			));

			JLabel lblNumero = new JLabel("#" + String.format("%03d", t.getNumero()));
			lblNumero.setForeground(new Color(33, 150, 243)); // azul
			lblNumero.setFont(new Font("Arial", Font.BOLD, 14));

			JLabel lblDni = new JLabel(formatearDni(t.getDocumento()));
			lblDni.setFont(new Font("Arial", Font.BOLD, 14));
			JLabel lblTiempo = new JLabel(calcularHace(t.getHoraHoraDeLlamado()));
			lblTiempo.setForeground(Color.GRAY);

			lblNumero.setPreferredSize(new Dimension(60, 20));
			lblTiempo.setHorizontalAlignment(SwingConstants.RIGHT);

			fila.add(lblNumero, BorderLayout.WEST);
			fila.add(lblDni, BorderLayout.CENTER);
			fila.add(lblTiempo, BorderLayout.EAST);

			panelLista.add(fila);
			panelLista.add(Box.createRigidArea(new Dimension(0, 5)));
		}

		panelLista.revalidate();
		panelLista.repaint();
	}

	// ================= HELPERS =================

	private void actualizarFechaHora() {
		Date ahora = new Date();

		SimpleDateFormat horaFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat fechaFormat = new SimpleDateFormat("EEE dd/MM/yyyy");

		String hora = horaFormat.format(ahora);
		String fecha = fechaFormat.format(ahora);

		lblFechaHora.setText(hora + " hs  - " + capitalizar(fecha));
	}

	private String capitalizar(String texto) {
		return texto.substring(0, 1).toUpperCase() + texto.substring(1);
	}

	private String formatearDni(String dni) {
	    if (dni == null || !dni.matches("\\d+")) {
	        return dni; // devuelve tal cual si no es válido
	    }

	    int numero = Integer.parseInt(dni);
	    return String.format("%,d", numero).replace(",", ".");
	}

	private String calcularHace(Date fechaTurno) {
		long diff = new Date().getTime() - fechaTurno.getTime();
		long minutos = diff / (1000 * 60);

		if (minutos <= 0) return "hace instantes";
		return "hace " + minutos + " min";
	}

	public void ActualizarTitulo(int numero) {
		lblTitulo.setText("Terminal de Notificacion "+numero);
	}
}