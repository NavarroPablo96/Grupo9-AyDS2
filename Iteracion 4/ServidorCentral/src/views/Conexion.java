package views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import controllers.IControladorConexion;


public class Conexion extends JFrame implements IVistaConexion {

    private static final long serialVersionUID = 1L;

    // Usamos arreglos para manejar los 4 pares de manera indexada
    private JTextField[] txtIPs;
    private JTextField[] txtPuertos;
    private JButton btnEscuchar;
    private IControladorConexion controlador;

    // Constantes para identificar qué índice corresponde a cada rol
    private static final int INDEX_CP = 0;
    private static final int INDEX_SP = 1;
    private static final int INDEX_CS = 2;
    private static final int INDEX_SS = 3;
    
 // Persistencia - Recuperar
    private JRadioButton rbRecTxt;
    private JRadioButton rbRecXml;
    private JRadioButton rbRecJson;

    // Persistencia - Guardar
    private JRadioButton rbGuaTxt;
    private JRadioButton rbGuaXml;
    private JRadioButton rbGuaJson;

    public Conexion() {
        setTitle("Conexión - Servidor primario y secundario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Aumentamos el tamaño de la ventana para que entren cómodamente los nuevos campos
        setBounds(100, 100, 450, 400); 

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // -------------------
        // PANEL SERVIDOR - ESCUCHA (Rediseñado para 4 direcciones)
        // -------------------
        JPanel panelReceptor = new JPanel();
        panelReceptor.setBorder(BorderFactory.createTitledBorder("Configuración de (IP : Puerto)"));
        panelReceptor.setLayout(new GridBagLayout());

        // Inicializamos los arreglos para los 4 elementos
        txtIPs = new JTextField[4];
        txtPuertos = new JTextField[4];

        // Etiquetas sugeridas para guiar al usuario sobre qué es cada IP:Puerto
        String[] nombresRoles = { "Clientes Primario:", "Sincronizacion Primario:", "Clientes Secundario:", "Sincronizacion Secundario:" };
        String[] ipsPorDefecto = { "127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1" };
        String[] puertosPorDefecto = { "1234", "2234", "1235", "2235" };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Bucle para construir la grilla dinámicamente sin repetir código
        for (int i = 0; i < 4; i++) {
            // 1. Columna 0: Etiqueta del Rol
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.0;
            panelReceptor.add(new JLabel(nombresRoles[i]), gbc);

            // 2. Columna 1: Campo de texto para la IP
            txtIPs[i] = new JTextField(12);
            txtIPs[i].setText(ipsPorDefecto[i]);
            ((AbstractDocument) txtIPs[i].getDocument()).setDocumentFilter(new IPFilter());
            
            gbc.gridx = 1;
            gbc.weightx = 1.0;
            panelReceptor.add(txtIPs[i], gbc);

            // 3. Columna 2: Separador visual ":"
            gbc.gridx = 2;
            gbc.weightx = 0.0;
            panelReceptor.add(new JLabel(":"), gbc);

            // 4. Columna 3: Campo de texto para el Puerto
            txtPuertos[i] = new JTextField(5);
            txtPuertos[i].setText(puertosPorDefecto[i]);
            ((AbstractDocument) txtPuertos[i].getDocument()).setDocumentFilter(new PuertoFilter());
            
            gbc.gridx = 3;
            gbc.weightx = 0.3;
            panelReceptor.add(txtPuertos[i], gbc);
        }

        // Botón Escuchar al final de la grilla (ocupa toda la fila inferior)
        btnEscuchar = new JButton("Escuchar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 4; // Cruza las 4 columnas
        gbc.weightx = 1.0;
        gbc.insets = new Insets(15, 6, 6, 6);
        panelReceptor.add(btnEscuchar, gbc);

        // ==============================
		 // MÉTODO DE RECUPERACIÓN
		 // ==============================
		
		 JPanel panelRecuperar = new JPanel();
		
		 rbRecTxt = new JRadioButton("TXT");
		 rbRecXml = new JRadioButton("XML");
		 rbRecJson = new JRadioButton("JSON");
		
		 ButtonGroup grupoRecuperar = new ButtonGroup();
		 grupoRecuperar.add(rbRecTxt);
		 grupoRecuperar.add(rbRecXml);
		 grupoRecuperar.add(rbRecJson);
		
		 // Selección por defecto
		 rbRecTxt.setSelected(true);
		
		 panelRecuperar.setBorder(
		     BorderFactory.createTitledBorder("Método para cargar")
		 );
		
		 panelRecuperar.add(rbRecTxt);
		 panelRecuperar.add(rbRecXml);
		 panelRecuperar.add(rbRecJson);
		
		 gbc.gridx = 0;
		 gbc.gridy = 5;
		 gbc.gridwidth = 4;
		 gbc.insets = new Insets(10, 6, 6, 6);
		
		 panelReceptor.add(panelRecuperar, gbc);
		
		 // ==============================
		 // MÉTODO DE GUARDADO
		 // ==============================
		
		 JPanel panelGuardar = new JPanel();
		
		 rbGuaTxt = new JRadioButton("TXT");
		 rbGuaXml = new JRadioButton("XML");
		 rbGuaJson = new JRadioButton("JSON");
		
		 ButtonGroup grupoGuardar = new ButtonGroup();
		 grupoGuardar.add(rbGuaTxt);
		 grupoGuardar.add(rbGuaXml);
		 grupoGuardar.add(rbGuaJson);
		
		 // Selección por defecto
		 rbGuaTxt.setSelected(true);
		
		 panelGuardar.setBorder(
		     BorderFactory.createTitledBorder("Método para guardar")
		 );
		
		 panelGuardar.add(rbGuaTxt);
		 panelGuardar.add(rbGuaXml);
		 panelGuardar.add(rbGuaJson);
		
		 gbc.gridx = 0;
		 gbc.gridy = 6;
		 gbc.gridwidth = 4;
		 gbc.insets = new Insets(6, 6, 6, 6);
		
		 panelReceptor.add(panelGuardar, gbc);
	     
        contentPane.add(panelReceptor);
    }

    @Override
    public void mostrar() {
        this.setVisible(true);
    }

    @Override
    public void cerrar() {
        this.setVisible(false);
    }

    @Override
    public void setController(IControladorConexion c) {
        this.controlador = c;
        setActionListener();
    }

    private void setActionListener() {
        this.btnEscuchar.addActionListener(e -> controlador.establecerConexion());
    }

    // --- Getters de IPs vinculados a los campos de la UI ---

    @Override
    public String getIP_Cp() {
        return txtIPs[INDEX_CP].getText().trim();
    }

    @Override
    public String getIP_Sp() {
        return txtIPs[INDEX_SP].getText().trim();
    }

    @Override
    public String getIP_Cs() {
        return txtIPs[INDEX_CS].getText().trim();
    }

    @Override
    public String getIP_Ss() {
        return txtIPs[INDEX_SS].getText().trim();
    }

    // --- Getters de Puertos vinculados a los campos de la UI con conversión a entero ---

    @Override
    public int getPuerto_Cp() {
        try {
            return Integer.parseInt(txtPuertos[INDEX_CP].getText().trim());
        } catch (NumberFormatException e) {
            return 1234; // Fallback por seguridad
        }
    }

    @Override
    public int getPuerto_Sp() {
        try {
            return Integer.parseInt(txtPuertos[INDEX_SP].getText().trim());
        } catch (NumberFormatException e) {
            return 2234;
        }
    }

    @Override
    public int getPuerto_Cs() {
        try {
            return Integer.parseInt(txtPuertos[INDEX_CS].getText().trim());
        } catch (NumberFormatException e) {
            return 1235;
        }
    }

    @Override
    public int getPuerto_Ss() {
        try {
            return Integer.parseInt(txtPuertos[INDEX_SS].getText().trim());
        } catch (NumberFormatException e) {
            return 2235;
        }
    }

    @Override
    public void desactivarBoton(String escuchandoEn) {
        this.btnEscuchar.setEnabled(false);
        this.btnEscuchar.setText(escuchandoEn);
    }

    @Override
    public String getFabricaRecuperar() {
        if (rbRecTxt.isSelected())
            return "TXT";
        if (rbRecXml.isSelected())
            return "XML";
        return "JSON";
    }

    @Override
    public String getFabricaGuardar() {
        if (rbGuaTxt.isSelected())
            return "TXT";
        if (rbGuaXml.isSelected())
            return "XML";
        return "JSON";
    }
}