package vista_controlador;


import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import eventos.Turno;

public class InterfazGraficaServidor extends JFrame {
	private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JPanel panelReceptor;
    private JLabel lblTitulo;
    
    private DefaultListModel<String> modeloRegistro;
    private DefaultListModel<String> modeloAtencion;
    private DefaultListModel<String> modeloNotificacion;

    private JList<String> listaRegistro;
    private JList<String> listaAtencion;
    private JList<String> listaNotificacion;

    private JTable tabla;
    private DefaultTableModel modeloTabla;

    private JTextArea consola;

    public InterfazGraficaServidor() {
        setTitle("Servidor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 600);

        contentPane = new JPanel(new BorderLayout(10,10));
        contentPane.setBorder(new EmptyBorder(10,10,10,10));
        setContentPane(contentPane);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        contentPane.add(panelCentral, BorderLayout.CENTER);

        // -------------------
        // PANEL SERVIDOR (TITULO DINÁMICO)
        // -------------------
        lblTitulo = new JLabel("Servidor");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentral.add(lblTitulo);

        panelCentral.add(Box.createVerticalStrut(10));

        // -------------------
        // LISTAS DE TERMINALES (3 columnas)
        // -------------------
        JPanel panelListas = new JPanel(new GridLayout(1,3,10,10));

        modeloRegistro = new DefaultListModel<>();
        modeloAtencion = new DefaultListModel<>();
        modeloNotificacion = new DefaultListModel<>();

        listaRegistro = new JList<>(modeloRegistro);
        listaAtencion = new JList<>(modeloAtencion);
        listaNotificacion = new JList<>(modeloNotificacion);

        panelListas.add(crearPanelLista("Registro", listaRegistro));
        panelListas.add(crearPanelLista("Atención", listaAtencion));
        panelListas.add(crearPanelLista("Notificación", listaNotificacion));

        panelCentral.add(panelListas);

        panelCentral.add(Box.createVerticalStrut(10));

        // -------------------
        // TABLA DE TURNOS
        // -------------------
        String[] columnas = {"#", "Documento", "Hora Reg.", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);

        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(25);

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(800, 150));
        scrollTabla.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        panelCentral.add(scrollTabla);

        panelCentral.add(Box.createVerticalStrut(10));

        // -------------------
        // CONSOLA
        // -------------------
        consola = new JTextArea(8, 50);
        consola.setEditable(false);
        consola.setBackground(Color.BLACK);
        consola.setForeground(Color.GREEN);

        JScrollPane scrollConsola = new JScrollPane(consola);
        scrollConsola.setBorder(BorderFactory.createTitledBorder("Consola"));

        panelCentral.add(scrollConsola);

        redirigirSystemOut();
    }

    // -------------------
    // SET TITLE DINÁMICO
    // -------------------
    public void setTituloServidor(String titulo) {
        lblTitulo.setText(titulo);
    }

    // -------------------
    // ACTUALIZAR TODO
    // -------------------
    public void actualizar(List<Turno> listaTurnos,
                           List<String> listaTerminalesRegistro,
                           List<String> listaTerminalesAtencion,
                           List<String> listaTerminalesNotificacion,
                           String escuchando) {

        // Título
        setTituloServidor(escuchando);

        // Listas
        actualizarLista(modeloRegistro, listaTerminalesRegistro);
        actualizarLista(modeloAtencion, listaTerminalesAtencion);
        actualizarLista(modeloNotificacion, listaTerminalesNotificacion);

        // Tabla turnos
        modeloTabla.setRowCount(0);

        for (int i = 0; i < listaTurnos.size(); i++) {
            Turno t = listaTurnos.get(i);

            String estado = (i == 0) ? "Próximo" : "Espera";

            modeloTabla.addRow(new Object[]{
                    t.getNumero(),
                    t.getDocumento(),
                    t.getHoraRegistro(),
                    estado
            });
        }
    }

    // -------------------
    // HELPERS
    // -------------------
    private JPanel crearPanelLista(String titulo, JList<String> lista) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        return panel;
    }

    private void actualizarLista(DefaultListModel<String> modelo, List<String> datos) {
        modelo.clear();
        for (String s : datos) {
            modelo.addElement(s);
        }
    }

    // -------------------
    // CONSOLA REDIRECCION
    // -------------------
    private void redirigirSystemOut() {
        PrintStream printStream = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) {
                consola.append(String.valueOf((char) b));
                consola.setCaretPosition(consola.getDocument().getLength());
            }
        });

        System.setOut(printStream);
        System.setErr(printStream);
    }
}