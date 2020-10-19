package compilador;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Controller implements ActionListener {
    CompiladorView v;
    public Controller( CompiladorView v){
        this.v = v;
        addListeners();
    }

    private void addListeners() {
        v.btnAbrir.addActionListener(this);
        v.btnCerrar.addActionListener(this);
        v.btnCompilar.addActionListener(this);
        v.btnTablaSimbolos.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == v.btnCerrar) {
            System.exit(0);
        }

        if (e.getSource() == v.btnCompilar) {
            v.generarArchivo();
            v.compilar();
            v.btnTablaSimbolos.setEnabled(true);
            return;
        }

        if(e.getSource() == v.btnTablaSimbolos){
            v.mostrarTablaSimbolos();
            return;
        }

        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showSaveDialog(v);
        if (opcion == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();

            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                String lineaActual;
                while ((lineaActual = br.readLine()) != null) {
                    v.area.append(lineaActual + "\n");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }
}
