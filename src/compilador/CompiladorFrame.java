package compilador;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
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


public class CompiladorFrame extends JFrame implements ActionListener {

    public static JTextArea area, consola;
    private JButton btnCompilar, btnAbrir, btnTablaSimbolos, btnCerrar;

    public CompiladorFrame() {
        hazInterfaz();
    }

    private void hazInterfaz() {
        setLayout(null);
        setUndecorated(true);
        setSize(500, 700);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 700, 20, 20));

        PanelGradiente panel = new PanelGradiente();
        panel.setBounds(0, 0, 500, 700);

        area = new JTextArea();
        consola = new JTextArea();
        consola.setEnabled(false);
        consola.setDisabledTextColor(Color.BLACK);
        btnCompilar = new JButton("Compilar");
        btnCompilar.setBounds(40, 5, 100, 40);
        btnCompilar.addActionListener(this);

        btnAbrir = new JButton("Abrir archivo");
        btnAbrir.setBounds(150, 5, 100, 40);
        btnAbrir.addActionListener(this);

        btnTablaSimbolos = new JButton("Tabla de simbolos");
        btnTablaSimbolos.setBounds(263, 5, 150, 40);
        btnTablaSimbolos.addActionListener(this);

        btnCerrar = new JButton("X");
        btnCerrar.setBounds(440, 5, 40, 40);
        btnCerrar.setBackground(Color.decode("#65417A"));
        btnCerrar.addActionListener(this);

        JScrollPane scrollPaneArea = new JScrollPane(area);
        scrollPaneArea.setBounds(30, 50, 440, 300);
        JScrollPane scrollPaneConsola = new JScrollPane(consola);
        scrollPaneConsola.setBounds(30, 350, 440, 330);

        add(scrollPaneArea);
        add(scrollPaneConsola);
        add(btnAbrir);
        add(btnCompilar);
        add(btnTablaSimbolos);
        add(btnCerrar);
        add(panel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btnCerrar) {
            System.exit(0);
        }

        if (e.getSource() == btnCompilar) {
            generarArchivo();
            compilar();
            return;
        }

        if(e.getSource() == btnTablaSimbolos){


        }

        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showSaveDialog(this);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();

            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String lineaActual;
                while ((lineaActual = br.readLine()) != null) {
                    area.append(lineaActual + "\n");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    private void generarArchivo() {
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

    private void compilar() {
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

        AnalizadorSemantico analizadorSemantico;
        //if (!analizadorSintactico.getHayErrores()){
            analizadorSemantico = new AnalizadorSemantico(area.getText());
        //}
    }

    class PanelGradiente extends JPanel {

        private static final long serialVersionUID = 1L;

        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(new Color(56, 56, 56 ));
            g2.fillRect(0, 0, getWidth(), 50);
            g2.setPaint(new Color(105, 105, 105));
            g2.fillRect(0, 50, getWidth(), 670);
        }
    }

}
