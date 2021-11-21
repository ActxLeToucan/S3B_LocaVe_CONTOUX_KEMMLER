package LocaVe;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * controleur pour la question 1
 */
public class ControleurQuestion1 extends JPanel implements ActionListener {
    /**
     * menu deroulant contenant les categories de vehicules
     */
    private JComboBox<String> comboBox;
    /**
     * selectionneurs des dates
     */
    private JDateChooser dateChooserDebut, dateChooserFin;
    /**
     * modele a controler
     */
    private Modele modele;

    /**
     * construction du controleur
     * @param modele
     *          modele a controler
     */
    public ControleurQuestion1(Modele modele) {
        this.modele = modele;

        JPanel panelMain = new JPanel();

        JButton reloadList = new JButton("Rafraichir liste");
        JButton executer = new JButton("Exécuter");
        comboBox = new JComboBox<>();

        dateChooserDebut = new JDateChooser(new Date());
        dateChooserDebut.setPreferredSize(new Dimension(120, 20));
        dateChooserFin = new JDateChooser(new Date());
        dateChooserFin.setPreferredSize(new Dimension(120, 20));

        reloadList.addActionListener(this);
        executer.addActionListener(this);

        JPanel panelComboBox = new JPanel();
        JLabel labelComboBox = new JLabel("Catégorie");
        panelComboBox.add(labelComboBox);
        panelComboBox.add(comboBox);
        panelComboBox.add(reloadList);

        JPanel panelDateChooserDebut = new JPanel();
        JLabel labelDateChooserDebut = new JLabel("Date de début");
        panelDateChooserDebut.add(labelDateChooserDebut);
        panelDateChooserDebut.add(dateChooserDebut);

        JPanel panelDateChooserFin = new JPanel();
        JLabel labelDateChooserFin = new JLabel("Date de fin");
        panelDateChooserFin.add(labelDateChooserFin);
        panelDateChooserFin.add(dateChooserFin);

        panelMain.setLayout(new GridLayout(4,0));
        panelMain.add(panelComboBox);
        panelMain.add(panelDateChooserDebut);
        panelMain.add(panelDateChooserFin);
        panelMain.add(executer);

        this.add(panelMain);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Exécuter") == 0) {
            String categ = String.valueOf(comboBox.getSelectedItem());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateD = dateChooserDebut.getDate();
            Date dateF = dateChooserFin.getDate();

            String dateDebut = simpleDateFormat.format(dateD);
            String dateFin = simpleDateFormat.format(dateF);
            try {
                this.modele.setResultats(this.modele.listeVehicules(categ, dateDebut, dateFin));
            } catch (SQLException ex) {
                ex.printStackTrace();
                this.modele.addResultat(ex.toString());
            } catch (DateInvalidFormatException ex) {
                ex.printStackTrace();
                this.modele.addResultat("Format de date invalide.\n" + ex);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.modele.addResultat("Erreur durant l'exécution.\n" + ex);
            }

            this.modele.notifierObservateurs();

        } else if (e.getActionCommand().compareTo("Rafraichir liste") == 0) {
            this.comboBox.removeAllItems();
            try {
                for (String categ : this.modele.getCategories()) {
                    this.comboBox.addItem(categ);
                }
            } catch (SQLException | NullPointerException ex) {
                ex.printStackTrace();
                this.modele.addResultat("Impossible de mettre à jour la liste des catégories. Vérifiez que vous êtes connecté.\n" + ex);
            } catch (Exception ex) {
                ex.printStackTrace();
                this.modele.addResultat("Erreur durant la mise à jour.\n" + ex);
            }

            this.modele.notifierObservateurs();

        }
    }
}
