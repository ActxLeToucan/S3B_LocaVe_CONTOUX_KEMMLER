package LocaVe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * controleur des resultats
 */
public class ControleurResultat extends JPanel implements ActionListener {
    /**
     * modele a controler
     */
    private Modele modele;

    /**
     * construction du controleur
     * @param modele
     *          modele a controler
     */
    public ControleurResultat(Modele modele) {
        this.modele = modele;

        JButton clear = new JButton("Vider l'affichage");
        clear.addActionListener(this);

        this.add(clear);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Vider l'affichage") == 0) {
            this.modele.setResultats("");
            this.modele.notifierObservateurs();
        }
    }
}
