package vista_controlador;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;

public class ConexionFrame extends JFrame {    private static final long serialVersionUID = 1L;


private JTextField txtEmisorIP, txtEmisorPuerto;
private JTextField txtSecundarioIP, txtSecundarioPuerto;
private JRadioButton rbCesar, rbDES, rbXOR;
private ButtonGroup bgEncriptado;
private JTextField txtClave;

private JButton btnConectar;

public ConexionFrame() {
    setTitle("Conexión - Terminal Notificacion");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 450, 280);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setContentPane(contentPane);
    // -------------------
    // PANEL EMISOR
    // -------------------
    JPanel panelEmisor = new JPanel();
    panelEmisor.setBorder(BorderFactory.createTitledBorder("Terminal de Notificacion - Conectar"));
    panelEmisor.setLayout(new GridBagLayout());

    // --- Inicialización Servidor Primario ---
    txtEmisorIP = new JTextField(12);
    txtEmisorIP.setText("127.0.0.1");
    //((AbstractDocument) txtEmisorIP.getDocument()).setDocumentFilter(new IPFilter());
    txtEmisorPuerto = new JTextField(5);
    txtEmisorPuerto.setText("1234");
    //((AbstractDocument) txtEmisorPuerto.getDocument()).setDocumentFilter(new PuertoFilter());

    // --- Inicialización Servidor Secundario ---
    txtSecundarioIP = new JTextField(12);
    txtSecundarioIP.setText("127.0.0.1");
    //((AbstractDocument) txtSecundarioIP.getDocument()).setDocumentFilter(new IPFilter());
    txtSecundarioPuerto = new JTextField(5);
    txtSecundarioPuerto.setText("1235");
    //((AbstractDocument) txtSecundarioPuerto.getDocument()).setDocumentFilter(new PuertoFilter());

    // --- Inicialización Encriptación ---
    rbCesar = new JRadioButton("César");
    rbDES = new JRadioButton("DES");
    rbXOR = new JRadioButton("XOR");
    rbCesar.setSelected(true); // default
    
    bgEncriptado = new ButtonGroup();
    bgEncriptado.add(rbCesar);
    bgEncriptado.add(rbDES);
    bgEncriptado.add(rbXOR);
    
    txtClave = new JTextField(15);
    txtClave.setText("messi");
    btnConectar = new JButton("Conectar");

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(6, 6, 6, 6);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // FILA 0: Servidor Primario
    gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
    panelEmisor.add(new JLabel("Serv. Primario:"), gbc);
    gbc.gridx = 1; gbc.weightx = 1.0;
    panelEmisor.add(txtEmisorIP, gbc);
    gbc.gridx = 2; gbc.weightx = 0.0;
    panelEmisor.add(new JLabel(":"), gbc);
    gbc.gridx = 3; gbc.weightx = 0.3;
    panelEmisor.add(txtEmisorPuerto, gbc);

    // FILA 1: Servidor Secundario
    gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
    panelEmisor.add(new JLabel("Serv. Secundario:"), gbc);
    gbc.gridx = 1; gbc.weightx = 1.0;
    panelEmisor.add(txtSecundarioIP, gbc);
    gbc.gridx = 2; gbc.weightx = 0.0;
    panelEmisor.add(new JLabel(":"), gbc);
    gbc.gridx = 3; gbc.weightx = 0.3;
    panelEmisor.add(txtSecundarioPuerto, gbc);

    // FILA 2: Opciones de Encriptado
    gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
    panelEmisor.add(new JLabel("Encriptado:"), gbc);
    JPanel pnlRadios = new JPanel();
    pnlRadios.add(rbCesar);
    pnlRadios.add(rbDES);
    pnlRadios.add(rbXOR);
    gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
    panelEmisor.add(pnlRadios, gbc);

    // FILA 3: Clave de Encriptado
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 1; gbc.weightx = 0.0;
    panelEmisor.add(new JLabel("Clave:"), gbc);
    gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
    panelEmisor.add(txtClave, gbc);

    // FILA 4: Botón Conectar
    gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4; gbc.weightx = 1.0;
    gbc.insets = new Insets(12, 6, 6, 6);
    panelEmisor.add(btnConectar, gbc);

    contentPane.add(panelEmisor);
}

public JButton getBtnConectar() {
    return btnConectar;
}

public JTextField getTxtEmisorIP() {
    return txtEmisorIP;
}

public JTextField getTxtEmisorPuerto() {
	return txtEmisorPuerto;
}

public String getIp(){
    return txtEmisorIP.getText();
}

public int getPuerto(){
    return Integer.parseInt(txtEmisorPuerto.getText());
}

public String getIpSecundario() {
    return txtSecundarioIP.getText();
}

public int getPuertoSecundario() {
    return Integer.parseInt(txtSecundarioPuerto.getText());
}

public String getTipoEncriptado() {
    if (rbDES.isSelected()) return "DES";
    if (rbXOR.isSelected()) return "XOR";
    return "César";
}

public String getClave() {
    return txtClave.getText();
}

}