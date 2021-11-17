import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * permet de controler la connexion et la deconexion a la bdd
 */
public class ControleurConnexion implements ActionListener {
    /**
     * modele a controler
     */
    private Modele modele;
    /**
     * champs de connexion
     */
    private ChampsConnexion champsConnexion;

    /**
     * constrcution du controleur
     * @param modele
     *          modele a controlerr
     * @param champsConnexion
     *          champs de connexion
     */
    public ControleurConnexion(Modele modele, ChampsConnexion champsConnexion) {
        this.modele = modele;
        this.champsConnexion = champsConnexion;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Se connecter") == 0) {
            if (this.modele.connexion(this.champsConnexion.getUrl(), this.champsConnexion.getUser(), this.champsConnexion.getPassword())) {
                ((JButton)e.getSource()).setBackground(Color.GREEN);
                ((JButton)e.getSource()).setText("Se déconnecter");
            } else {
                ((JButton)e.getSource()).setBackground(Color.RED);
            }
            this.modele.notifierObservateurs();
        } else if (e.getActionCommand().compareTo("Se déconnecter") == 0) {
            if (this.modele.deconnexion()) {
                ((JButton)e.getSource()).setBackground(Color.GREEN);
                ((JButton)e.getSource()).setText("Se connecter");
            } else {
                ((JButton)e.getSource()).setBackground(Color.RED);
            }
        }
    }
}
