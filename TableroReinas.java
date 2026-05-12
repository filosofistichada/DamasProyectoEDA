package Proyecto;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class TableroReinas extends JFrame {

    // Componentes de Configuración
    private JSpinner spinN;
    private JRadioButton rbPrimera, rbTodas;
    private JCheckBox chkFija;
    private JTextField txtFila, txtCol;
    private JButton btnResolver;

    // Componentes de Visualización
    private JPanel pnlTablero;
    private JPanel pnlNavegacion;
    private JButton btnAnterior, btnSiguiente;
    private JLabel lblStatus;

    // Almacenamiento de soluciones (Integración con tu lógica)
    private ArrayList<int[][]> solucionesEncontradas = new ArrayList<int[][]>();
    private int indiceSolucionActual = 0;

    public TableroReinas() {
        setTitle("Solucionador N-Reinas - Estructuras de Datos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        initComponents();
        pack();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        // --- PANEL DE CONFIGURACIÓN (NORTH) ---
        JPanel pnlConfig = new JPanel(new GridLayout(2, 1));
        pnlConfig.setBorder(BorderFactory.createTitledBorder("Configuración de Búsqueda"));

        JPanel fila1 = new JPanel(new FlowLayout());
        fila1.add(new JLabel("N:"));
        spinN = new JSpinner(new SpinnerNumberModel(4, 4, 15, 1));
        fila1.add(spinN);

        rbPrimera = new JRadioButton("Primera solución", true);
        rbTodas = new JRadioButton("Todas las soluciones");
        ButtonGroup grupoModo = new ButtonGroup();
        grupoModo.add(rbPrimera);
        grupoModo.add(rbTodas);
        fila1.add(rbPrimera);
        fila1.add(rbTodas);

        JPanel fila2 = new JPanel(new FlowLayout());
        chkFija = new JCheckBox("Casilla Fija (F, C):");
        txtFila = new JTextField(2);
        txtCol = new JTextField(2);
        txtFila.setEnabled(false);
        txtCol.setEnabled(false);
        
        chkFija.addActionListener(e -> {
            txtFila.setEnabled(chkFija.isSelected());
            txtCol.setEnabled(chkFija.isSelected());
        });

        btnResolver = new JButton("Resolver");
        btnResolver.addActionListener(e -> ejecutarBusqueda());

        fila2.add(chkFija);
        fila2.add(txtFila);
        fila2.add(txtCol);
        fila2.add(btnResolver);

        pnlConfig.add(fila1);
        pnlConfig.add(fila2);
        add(pnlConfig, BorderLayout.NORTH);

        // --- PANEL DEL TABLERO (CENTER) ---
        pnlTablero = new JPanel();
        pnlTablero.setPreferredSize(new Dimension(450, 450));
        pnlTablero.setBorder(BorderFactory.createLoweredBevelBorder());
        add(pnlTablero, BorderLayout.CENTER);

        // --- PANEL DE NAVEGACIÓN (SOUTH) ---
        pnlNavegacion = new JPanel(new FlowLayout());
        btnAnterior = new JButton("< Anterior");
        btnSiguiente = new JButton("Siguiente >");
        lblStatus = new JLabel("Esperando parámetros...");
        
        pnlNavegacion.add(btnAnterior);
        pnlNavegacion.add(lblStatus);
        pnlNavegacion.add(btnSiguiente);
        pnlNavegacion.setVisible(false); // Oculto hasta que se necesite
        
        btnAnterior.addActionListener(e -> navegarSolucion(-1));
        btnSiguiente.addActionListener(e -> navegarSolucion(1));
        
        add(pnlNavegacion, BorderLayout.SOUTH);
    }

    private void ejecutarBusqueda() {
        // 1. Captura de parámetros
        int n = (int) spinN.getValue();
        boolean soloUna = rbPrimera.isSelected();
        boolean tieneFija = chkFija.isSelected();
        
        // Limpiar estados previos
        solucionesEncontradas.clear();
        indiceSolucionActual = 0;

        // --- PUNTO DE INTEGRACIÓN CON TU ALGORITMO ---
        /*
         * AQUÍ DEBES INSTANCIAR TU CLASE DE BACKTRACKING.
         * Ejemplo:*/
         Tablero tablero = new Tablero(n);
         if(tieneFija) tablero.setCasillaFija(Integer.parseInt(txtCol.getText()));
         
         if(soloUna) {
        	 solucionesEncontradas.add(tablero.inicializarPrimera());
         } else {
        	 solucionesEncontradas = tablero.inicializarTodas();
         }
         
        // ---------------------------------------------

        // Simulación de visualización inicial
        actualizarInterfaz(n, soloUna);
    }

    private void actualizarInterfaz(int n, boolean soloUna) {
        pnlTablero.removeAll();
        pnlTablero.setLayout(new GridLayout(n, n));
        
        if (solucionesEncontradas.isEmpty()) {
            lblStatus.setText("No se encontraron soluciones.");
            pnlNavegacion.setVisible(false);
        } else {
            dibujarTablero(solucionesEncontradas.get(0), n);
            if (!soloUna && solucionesEncontradas.size() > 1) {
                pnlNavegacion.setVisible(true);
                actualizarLabelNavegacion();
            } else {
                pnlNavegacion.setVisible(false);
            }
        }
        
        pnlTablero.revalidate();
        pnlTablero.repaint();
        pack();
    }

    private void dibujarTablero(int[][] tablero, int n) {
        pnlTablero.removeAll();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                JPanel cell = new JPanel();
                // Alternar colores de ajedrez
                cell.setBackground((i + j) % 2 == 0 ? Color.WHITE : Color.GRAY);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                
                // Si hay una reina en esta posición (asumiendo que tu matriz usa 1 para reina)
                if (tablero[i][j] == 1) {
                    JLabel reina = new JLabel("♛"); // Puedes usar un ImageIcon aquí
                    reina.setFont(new Font("Serif", Font.BOLD, 30));
                    reina.setForeground(Color.RED);
                    cell.add(reina);
                }
                pnlTablero.add(cell);
            }
        }
        pnlTablero.revalidate();
        pnlTablero.repaint();
    }

    private void navegarSolucion(int direccion) {
        int nuevoIndice = indiceSolucionActual + direccion;
        if (nuevoIndice >= 0 && nuevoIndice < solucionesEncontradas.size()) {
            indiceSolucionActual = nuevoIndice;
            dibujarTablero(solucionesEncontradas.get(indiceSolucionActual), (int) spinN.getValue());
            actualizarLabelNavegacion();
        }
    }

    private void actualizarLabelNavegacion() {
        lblStatus.setText("Solución " + (indiceSolucionActual + 1) + " de " + solucionesEncontradas.size());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TableroReinas().setVisible(true));
    }
}

