package vista_controlador;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;

import comuEntreProcesos.Turno;

public class TerminalRegistro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtDNI;
    private JLabel lblTurnoNumero;
    private JLabel lblDetalleTurno;
    private JButton btnRegistrar;

    /**
     * Create the frame.
     */
    public TerminalRegistro() {
        setTitle("Terminal de Registro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 245));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        // Panel superior con título
        JLabel lblTitulo = new JLabel("Sistema de Turnos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(33, 150, 243)); // azul
        panelHeader.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelHeader.add(lblTitulo, BorderLayout.CENTER);

        contentPane.add(panelHeader, BorderLayout.NORTH);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        // Panel central con instrucciones y registro
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(new Color(245, 245, 245));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        JLabel lblInstrucciones = new JLabel("Ingrese su documento para registrarse en la fila (solo dígitos)");
        lblInstrucciones.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInstrucciones.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInstrucciones.setForeground(Color.DARK_GRAY);
        panelCentral.add(lblInstrucciones);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 10)));

        // Panel con campo de texto y botón en la misma línea
        JPanel panelRegistro = new JPanel();
        panelRegistro.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        txtDNI = new JTextField();
        txtDNI.setColumns(10);
        txtDNI.setFont(new Font("Arial", Font.PLAIN, 16));
        txtDNI.setPreferredSize(new Dimension(150, 35));
        ((AbstractDocument) txtDNI.getDocument()).setDocumentFilter(new DniFilter());
        txtDNI.addActionListener(e -> btnRegistrar.doClick());
        
        this.btnRegistrar = new JButton("Registrarme");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBackground(new Color(255, 87, 34)); // naranja
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        
        panelRegistro.add(txtDNI);
        panelRegistro.add(btnRegistrar);
        panelCentral.add(panelRegistro);
        panelCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        panelCentral.add(Box.createVerticalStrut(15));
        
        // Cuadro de turno
        JPanel panelTurno = new JPanel();
        panelTurno.setBackground(new Color(33, 33, 33)); // oscuro
        panelTurno.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2, true));
        panelTurno.setLayout(new BoxLayout(panelTurno, BoxLayout.Y_AXIS));
        panelTurno.setPreferredSize(new Dimension(300, 80));
        panelTurno.setMaximumSize(new Dimension(300, 80));
        panelTurno.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblTurnoNumero = new JLabel("Turno -");
        lblTurnoNumero.setForeground(Color.WHITE);
        lblTurnoNumero.setFont(new Font("Arial", Font.BOLD, 28));
        lblTurnoNumero.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblDetalleTurno = new JLabel("Documento: xxxxxxxx - Registrado a las xx:xx hs");
        lblDetalleTurno.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDetalleTurno.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDetalleTurno.setForeground(Color.LIGHT_GRAY);

        panelTurno.add(Box.createVerticalGlue());
        panelTurno.add(lblTurnoNumero);
        panelTurno.add(Box.createRigidArea(new Dimension(0, 5)));
        panelTurno.add(lblDetalleTurno);
        panelTurno.add(Box.createVerticalGlue());

        panelCentral.add(panelTurno);
    }

    
    public JTextField getTxtDNI() {
		return txtDNI;
	}


	// Método para registrar un turno
    public void ActualizaVista(Turno t) {
    	lblTurnoNumero.setText(String.format("Turno #%03d", t.getNumero()));
        lblDetalleTurno.setText("Documento: " + t.getDocumento() + " - Registrado a las " + t.getHoraRegistro() + " hs");
    }
    

	public JButton getBtnRegistrar() {
		return btnRegistrar;
	}
    
    
}