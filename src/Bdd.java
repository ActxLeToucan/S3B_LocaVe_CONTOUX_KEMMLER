import javax.swing.*;
import java.awt.*;

public class Bdd {

    public static void main(String[] args) {
        Modele modele = new Modele();

        VueResultat vueResultat = new VueResultat();
        JScrollPane scrollPane = new JScrollPane (vueResultat, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        modele.enregistrerObservateur(vueResultat);

        modele.notifierObservateurs();

        ControleurConnexion controleurConnexion = new ControleurConnexion(modele);
        ControleurQuestion1 controleurQuestion1 = new ControleurQuestion1(modele);
        ControleurQuestion2 controleurQuestion2 = new ControleurQuestion2(modele);
        ControleurQuestion3 controleurQuestion3 = new ControleurQuestion3(modele);
        ControleurQuestion4 controleurQuestion4 = new ControleurQuestion4(modele);
        ControleurQuestion5 controleurQuestion5 = new ControleurQuestion5(modele);
        ControleurQuestion6 controleurQuestion6 = new ControleurQuestion6(modele);
        ControleurResultat controleurResultat = new ControleurResultat(modele);

        JTabbedPane onglets = new JTabbedPane();
        onglets.add("Q1 : Vérifier disponibilité", controleurQuestion1);
        onglets.add("Q2 : MAJ calendrier", controleurQuestion2);
        onglets.add("Q3 : Calculer montant", controleurQuestion3);
        onglets.add("Q4 : Agences avec toutes les categ", controleurQuestion4);
        onglets.add("Q5 : Clients avec plusieurs modèles", controleurQuestion5);
        onglets.add("Q6 : Triggers", controleurQuestion6);

        JPanel resultat = new JPanel(new BorderLayout());
        resultat.add(scrollPane, BorderLayout.CENTER);
        resultat.add(controleurResultat, BorderLayout.EAST);

        JFrame frame = new JFrame("LocaVe");
        frame.setLayout(new BorderLayout());
        frame.add(resultat, BorderLayout.SOUTH);
        frame.add(controleurConnexion, BorderLayout.NORTH);
        frame.add(onglets, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
