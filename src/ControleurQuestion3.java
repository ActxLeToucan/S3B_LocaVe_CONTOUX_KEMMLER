import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * controleur pour la question 3
 */
public class ControleurQuestion3 extends JPanel implements ActionListener {
    /**
     * spinner pour le nombre de jours
     */
    private JSpinner spinner;
    /**
     * menu deroulant contenant les modeles des vehicules
     */
    private JComboBox<String> comboBox;
    /**
     * modele a controler
     */
    private Modele modele;

    /**
     * construction du controleur
     * @param modele
     *          modele a controler
     */
    public ControleurQuestion3(Modele modele) {
        this.modele = modele;

        JPanel panelMain = new JPanel();

        JButton reloadList = new JButton("Rafraichir liste");
        JButton executer = new JButton("Exécuter");
        comboBox = new JComboBox<>();

        SpinnerModel spinnerModel = new SpinnerNumberModel(1, 0, Integer.MAX_VALUE, 1);
        spinner = new JSpinner(spinnerModel);

        reloadList.addActionListener(this);
        executer.addActionListener(this);

        JPanel panelComboBox = new JPanel();
        JLabel labelComboBox = new JLabel("Modèle");
        panelComboBox.add(labelComboBox);
        panelComboBox.add(comboBox);
        panelComboBox.add(reloadList);

        JPanel panelSpinner = new JPanel();
        JLabel labelSpinner = new JLabel("Nombre de jours de location");
        panelSpinner.add(spinner);
        panelSpinner.add(labelSpinner);

        panelMain.setLayout(new GridLayout(3,0));
        panelMain.add(panelComboBox);
        panelMain.add(panelSpinner);
        panelMain.add(executer);

        this.add(panelMain);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Exécuter") == 0) {
            String modeleVehic = String.valueOf(comboBox.getSelectedItem());

            int nbJours = (int)this.spinner.getValue();
            try {
                this.modele.setResultats(this.modele.calculerMontant(modeleVehic, nbJours));
            } catch (SQLException ex) {
                ex.printStackTrace();
                this.modele.addResultat(ex.toString());
            } catch (Exception ex) {
                ex.printStackTrace();
                this.modele.addResultat("Erreur durant l'exécution.\n" + ex);
            }

            this.modele.notifierObservateurs();

        } else if (e.getActionCommand().compareTo("Rafraichir liste") == 0) {
            this.comboBox.removeAllItems();
            try {
                for (String mod : this.modele.getModeles()) {
                    this.comboBox.addItem(mod);
                }
            } catch (SQLException | NullPointerException ex) {
                ex.printStackTrace();
                this.modele.addResultat("Impossible de mettre à jour la liste des modèles. Vérifiez que vous êtes connecté.\n" + ex);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.modele.addResultat("Erreur durant la mise à jour.\n" + ex);
            }

            this.modele.notifierObservateurs();

        }
    }
}
