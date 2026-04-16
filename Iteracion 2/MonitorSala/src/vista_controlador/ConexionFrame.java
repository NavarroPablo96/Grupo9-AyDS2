package vista_controlador;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.text.AbstractDocument;

public class ConexionFrame extends JFrame {    private static final long serialVersionUID = 1L;


private JTextField txtEmisorIP, txtEmisorPuerto;
private JButton btnConectar;

public ConexionFrame() {
    setTitle("Conexión - Terminal Registro");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 400, 190);

    JPanel contentPane = new JPanel();
    contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
    contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    setContentPane(contentPane);
    // -------------------
    // PANEL EMISOR
    // -------------------
    JPanel panelEmisor = new JPanel();
    panelEmisor.setBorder(BorderFactory.createTitledBorder("Terminal de Registro - Conectar"));
    panelEmisor.setLayout(new GridBagLayout());

    txtEmisorIP = new JTextField(15);
    txtEmisorIP.setText("127.0.0.1");
    ((AbstractDocument) txtEmisorIP.getDocument()).setDocumentFilter(new IPFilter());
    txtEmisorPuerto = new JTextField(15);
    txtEmisorPuerto.setText("1234");
    ((AbstractDocument) txtEmisorPuerto.getDocument()).setDocumentFilter(new PuertoFilter());
    btnConectar = new JButton("Conectar");

    GridBagConstraints gbcLabelIPEmisor = new GridBagConstraints();
    gbcLabelIPEmisor.gridx = 0;
    gbcLabelIPEmisor.gridy = 0;
    gbcLabelIPEmisor.insets = new Insets(5, 5, 5, 5);
    gbcLabelIPEmisor.fill = GridBagConstraints.HORIZONTAL;
    panelEmisor.add(new JLabel("IP:"), gbcLabelIPEmisor);

    GridBagConstraints gbcTxtIPEmisor = new GridBagConstraints();
    gbcTxtIPEmisor.gridx = 1;
    gbcTxtIPEmisor.gridy = 0;
    gbcTxtIPEmisor.insets = new Insets(5, 5, 5, 5);
    gbcTxtIPEmisor.fill = GridBagConstraints.HORIZONTAL;
    panelEmisor.add(txtEmisorIP, gbcTxtIPEmisor);

    GridBagConstraints gbcLabelPuertoEmisor = new GridBagConstraints();
    gbcLabelPuertoEmisor.gridx = 0;
    gbcLabelPuertoEmisor.gridy = 1;
    gbcLabelPuertoEmisor.insets = new Insets(5, 5, 5, 5);
    gbcLabelPuertoEmisor.fill = GridBagConstraints.HORIZONTAL;
    panelEmisor.add(new JLabel("Puerto:"), gbcLabelPuertoEmisor);

    GridBagConstraints gbcTxtPuertoEmisor = new GridBagConstraints();
    gbcTxtPuertoEmisor.gridx = 1;
    gbcTxtPuertoEmisor.gridy = 1;
    gbcTxtPuertoEmisor.insets = new Insets(5, 5, 5, 5);
    gbcTxtPuertoEmisor.fill = GridBagConstraints.HORIZONTAL;
    panelEmisor.add(txtEmisorPuerto, gbcTxtPuertoEmisor);

    GridBagConstraints gbcBtnConectar = new GridBagConstraints();
    gbcBtnConectar.gridx = 0;
    gbcBtnConectar.gridy = 2;
    gbcBtnConectar.gridwidth = 2;
    gbcBtnConectar.insets = new Insets(5, 5, 5, 5);
    gbcBtnConectar.fill = GridBagConstraints.HORIZONTAL;
    panelEmisor.add(btnConectar, gbcBtnConectar);

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
}