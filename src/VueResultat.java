import javax.swing.*;
import java.awt.*;

/**
 * vue permettant d'afficher les resultats
 */
public class VueResultat extends JTextArea implements Observateur {
    /**
     * constructeur de la vue
     */
    public VueResultat() {
        super("résultats...");
        this.setPreferredSize(new Dimension(500, 200));
        this.setEditable(false);

    }

    @Override
    public void actualiser(Sujet sujet) {
        this.setText(((Modele)sujet).getResultats());
    }
}