package vista_controlador;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import eventos.Turno;

public class VistaOperador extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblHeader;
    private JButton btnLlamar;
    private JButton btnNotificar;
    
    public JButton getBtnLlamar() {
		return btnLlamar;
	}
    public JButton getBtnNotificar() {
        return btnNotificar;
    }
	private JLabel lblTitulo;
    private JLabel lblDni;

    private JPanel panelStats;
    private JLabel lblEnEspera;
    private JLabel lblLlamados;
    
    public VistaOperador() {
        setTitle("Panel de Operador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 650, 345);

        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        
        lblHeader = new JLabel("Sistema de Turnos");
        lblHeader.setFont(new Font("Arial", Font.BOLD, 24));
        lblHeader.setHorizontalAlignment(SwingConstants.CENTER);
        lblHeader.setForeground(Color.WHITE);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(33, 150, 243)); // azul
        panelHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelHeader.add(lblHeader, BorderLayout.CENTER);

        contentPane.add(panelHeader, BorderLayout.NORTH);
        
        // PANEL PRINCIPAL (vertical)
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        contentPane.add(panelCentral, BorderLayout.CENTER);

     // Panel para los botones (1 fila, 2 columnas)
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));

        // Botón llamar
        btnLlamar = new JButton("Llamar siguiente");
        btnLlamar.setFont(new Font("Arial", Font.BOLD, 20));
        btnLlamar.setBackground(new Color(255, 87, 34));
        btnLlamar.setForeground(Color.WHITE);
        btnLlamar.setFocusPainted(false);
        btnLlamar.setEnabled(false);

        // Botón notificar
        btnNotificar = new JButton("Notificar de nuevo");
        btnNotificar.setFont(new Font("Arial", Font.BOLD, 20));
        btnNotificar.setBackground(new Color(255, 87, 34));
        btnNotificar.setForeground(Color.WHITE);
        btnNotificar.setFocusPainted(false);
        btnNotificar.setEnabled(false);

        // Agregar al panel
        panelBotones.add(btnLlamar);
        panelBotones.add(btnNotificar);

        // Ajustar tamaño
        panelBotones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        panelCentral.add(panelBotones);

        panelCentral.add(Box.createVerticalStrut(15));

        // PANEL DE CUADRADOS (2 columnas)
        panelStats = new JPanel(new GridLayout(1, 2, 15, 0));
        lblEnEspera = new JLabel("0");
        lblLlamados = new JLabel("0");
        
        panelStats.add(crearCuadrado(lblEnEspera, "En espera", new Color(76, 175, 80))); // azul
        panelStats.add(crearCuadrado(lblLlamados, "Cantidad de Veces Llamado", new Color(76, 175, 80))); // verde
        panelCentral.add(panelStats);

        panelCentral.add(Box.createVerticalStrut(15));

        // ÚLTIMO CLIENTE
        JPanel panelUltimo = new JPanel();
        panelUltimo.setLayout(new BoxLayout(panelUltimo, BoxLayout.Y_AXIS));
        panelUltimo.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
        panelUltimo.setBackground(new Color(33, 33, 33));

        lblTitulo = new JLabel("Último cliente llamado");
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setForeground(Color.LIGHT_GRAY);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 16));

        lblDni = new JLabel("-");
        lblDni.setFont(new Font("Arial", Font.BOLD, 36));
        lblDni.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDni.setForeground(Color.WHITE);

        panelUltimo.add(Box.createVerticalStrut(10));
        panelUltimo.add(lblTitulo);
        panelUltimo.add(Box.createVerticalStrut(5));
        panelUltimo.add(lblDni);
        panelUltimo.add(Box.createVerticalStrut(10));

        panelCentral.add(panelUltimo);

        panelCentral.add(Box.createVerticalStrut(15));
    }

    // Método para crear los cuadraditos con color
    private JPanel crearCuadrado(JLabel lblNumero, String texto, Color colorFondo) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(colorFondo);

        lblNumero.setFont(new Font("Arial", Font.BOLD, 28));
        lblNumero.setForeground(Color.WHITE);
        lblNumero.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setForeground(Color.WHITE);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(lblNumero);
        panel.add(Box.createVerticalStrut(5));
        panel.add(lblTexto);
        panel.add(Box.createVerticalStrut(10));

        return panel;
    }
    
    public void actualizar(Turno ultimoTurno,int enEspera,int CantLlamados) {

        //Ultimo turno llamado
        if (ultimoTurno != null) {
            lblTitulo.setText("Último cliente llamado (Turno #" + ultimoTurno.getNumero() + ")");
            lblDni.setText(ultimoTurno.getDocumento());
        }

        //Cantidad de Atendidos
        lblEnEspera.setText(String.valueOf(enEspera));
        lblLlamados.setText(String.valueOf(CantLlamados));
    }

	public void ActualizarTitulo(int numero) {
        setTitle("Puesto de Atencion "+numero);
        lblHeader.setText("Puesto de Atencion " + numero);
	}

}