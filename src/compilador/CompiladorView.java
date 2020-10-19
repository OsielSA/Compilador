package compilador;

import compilador.Analizadores.AnalizadorLexico;
import compilador.Analizadores.AnalizadorSemantico;
import compilador.Analizadores.AnalizadorSintactico;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class CompiladorView extends JFrame{

    public AnalizadorSemantico analizadorSemantico;

    public static JTextArea area, consola;
    public JButton btnCompilar, btnAbrir, btnTablaSimbolos, btnCerrar;

    public CompiladorView() {
        hazInterfaz();
    }

    private void hazInterfaz() {
        setLayout(null);
        setUndecorated(true);
        setSize(1000, 800);
        setLocationRelativeTo(null);
        //setShape(new RoundRectangle2D.Double(0, 0, 500, 700, 20, 20));

        PanelGradiente panel = new PanelGradiente();
        panel.setBounds(0, 0, 1000, 800);

        area = new JTextArea();
        area.setFont(new Font("Consolas", Font.BOLD, 18));


        consola = new JTextArea();
        consola.setEnabled(false);
        consola.setDisabledTextColor(Color.BLACK);

        btnCompilar = new JButton("Compilar");
        btnCompilar.setBounds(40, 5, 100, 40);

        btnAbrir = new JButton("Abrir archivo");
        btnAbrir.setBounds(150, 5, 100, 40);

        btnTablaSimbolos = new JButton("Tabla de simbolos");
        btnTablaSimbolos.setBounds(263, 5, 150, 40);
        btnTablaSimbolos.setEnabled(false);

        btnCerrar = new JButton("X");
        btnCerrar.setBounds(940, 5, 40, 40);
        btnCerrar.setBackground(new Color(127, 30, 22));

        JScrollPane scrollPaneArea = new JScrollPane(area);
        scrollPaneArea.setBounds(30, 50, 940, 390);
        JScrollPane scrollPaneConsola = new JScrollPane(consola);
        scrollPaneConsola.setBounds(30, 450, 940, 330);

        add(scrollPaneArea);
        add(scrollPaneConsola);
        add(btnAbrir);
        add(btnCompilar);
        add(btnTablaSimbolos);
        add(btnCerrar);
        add(panel);
        setVisible(true);
    }

    public void generarArchivo() {
        String ruta = "codigo.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(area.getText());

            bw.close();
        } catch (Exception ex) {

        }
    }

    public void compilar() {
        if (area.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Favor de escribir su código");
            area.requestFocus();
            return;
        }

        AnalizadorLexico analizadorLexico = new AnalizadorLexico("codigo.txt");
        ArrayList<String> erroresLexicos = analizadorLexico.getErroresLexicos();
        
        consola.setText("");

        for (int i = 0; i < erroresLexicos.size(); i++) {
            consola.append(erroresLexicos.get(i) + "\n");
        }

        AnalizadorSintactico analizadorSintactico = null;
        if (!analizadorLexico.getHayErrores()) {
            analizadorSintactico = new AnalizadorSintactico(analizadorLexico.getTokenRC());
        }

        //if (!analizadorSintactico.getHayErrores()){
            analizadorSemantico = new AnalizadorSemantico(area.getText());
        //}
    }

    public void mostrarTablaSimbolos(){
        analizadorSemantico.getTablaSimbolos();
    }

    class PanelGradiente extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new Color(23, 30, 36  ));
            g2.fillRect(0, 0, getWidth(), 50);
            g2.setPaint(new Color(28, 42, 53));
            g2.fillRect(0, 50, getWidth(), 800);
        }
    }

}
