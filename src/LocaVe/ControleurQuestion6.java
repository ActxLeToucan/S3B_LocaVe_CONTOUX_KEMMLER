package LocaVe;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * controleur pour la question 6
 * fonctionne pour tout trigger dans LocaVe.Modele.TRIGGERS
 */
public class ControleurQuestion6 extends JPanel implements ActionListener {
    /**
     * modele a controler
     */
    private Modele modele;
    /**
     * liste des checkBox
     */
    private ArrayList<JCheckBox> checkBoxList;

    /**
     * construction du controleur
     * @param modele
     *          modele a controler
     */
    public ControleurQuestion6(Modele modele) {
        this.modele = modele;
        this.checkBoxList = new ArrayList<>();

        JPanel panelMain = new JPanel();
        JPanel panelCheckBox = new JPanel();

        for (String trigger : Modele.TRIGGERS) {
            JCheckBox checkBox = new JCheckBox(trigger);
            checkBox.addActionListener(this);
            checkBox.setEnabled(false);
            this.checkBoxList.add(checkBox);
            panelCheckBox.add(checkBox);
        }

        JButton actualiser = new JButton("Actualiser les triggers");
        actualiser.addActionListener(this);

        panelMain.add(actualiser);
        panelMain.add(panelCheckBox);

        this.add(panelMain);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (Modele.TRIGGERS.contains(e.getActionCommand())) {
            JCheckBox checkBox = (JCheckBox)e.getSource();
            if (checkBox.isSelected()) {
                try {
                    this.modele.setTriggerStatus(e.getActionCommand(), true);
                } catch (NullPointerException | TriggerInvalidException | SQLException ex) {
                    ex.printStackTrace();
                    this.modele.addResultat("Impossible de changer l'état du trigger " + checkBox.getText() + ".\n" + ex);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.modele.addResultat("Erreur durant la mise à jour du trigger.\n" + ex);
                }
            } else {
                try {
                    this.modele.setTriggerStatus(e.getActionCommand(), false);
                } catch (NullPointerException | TriggerInvalidException | SQLException ex) {
                    ex.printStackTrace();
                    this.modele.addResultat("Impossible de changer l'état du trigger " + checkBox.getText() + ".\n" + ex);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.modele.addResultat("Erreur durant la mise à jour du trigger.\n" + ex);
                }
            }

            this.modele.notifierObservateurs();

        } else if (e.getActionCommand().compareTo("Actualiser les triggers") == 0) {
            for (JCheckBox checkBox : this.checkBoxList) {
                try {
                    int status = this.modele.getTriggerStatus(checkBox.getText());
                    if (status == 0) {
                        checkBox.setSelected(false);
                        checkBox.setEnabled(true);
                    } else if (status == 1) {
                        checkBox.setSelected(true);
                        checkBox.setEnabled(true);
                    } else {
                        this.modele.addResultat("Impossible de récupérer l'état actuel du trigger " + checkBox.getText() + ".");
                    }
                } catch (NullPointerException | TriggerInvalidException | SQLException ex) {
                    ex.printStackTrace();
                    this.modele.addResultat("Impossible de récupérer l'état actuel du trigger " + checkBox.getText() + ".\n" + ex);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    this.modele.addResultat("Erreur durant l'actualisation.\n" + ex);
                }
            }

            this.modele.notifierObservateurs();

        }
    }
}
