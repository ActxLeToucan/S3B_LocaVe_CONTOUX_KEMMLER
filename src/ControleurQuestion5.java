import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * controleur pour la question 5
 */
public class ControleurQuestion5 extends JPanel implements ActionListener {
    /**
     * modele a controler
     */
    private Modele modele;

    /**
     * constrcution du controleur
     * @param modele
     *          modele a controler
     */
    public ControleurQuestion5(Modele modele) {
        this.modele = modele;

        JPanel panelMain = new JPanel();

        JButton executer = new JButton("Exécuter");

        executer.addActionListener(this);

        panelMain.setLayout(new GridLayout(1,0));
        panelMain.add(executer);

        this.add(panelMain);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Exécuter") == 0) {
            try {
                this.modele.setResultats(this.modele.clientsPlusieursModeles());
            } catch (SQLException ex) {
                ex.printStackTrace();
                this.modele.setResultats(ex.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                this.modele.setResultats("Erreur durant l'exécution.\n" + ex);
            }
            this.modele.notifierObservateurs();
        }
    }
}
