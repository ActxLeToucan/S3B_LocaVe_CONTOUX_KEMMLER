import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Bdd {

    public static void main(String[] args) throws SQLException, DateInvalidFormatException {
        Modele modele = new Modele();
/*
        if (modele.connexion(url, user, password)) {
            resetTriggers();
            System.out.println(modele.listeVehicules("c1", "2015-10-07", "2015-10-01"));
            majCalendrierReserv("2015-10-01", "2015-10-07", "1234ya54");
            System.out.println(calculerMontant("saxo1.1", 8));
            System.out.println(agencesAvecToutesCateg());
            System.out.println(clientsPlusieursModeles());
            try {
                setTriggerStatus(TR_MAJKM, true);
                System.out.println(getTriggerStatus(TR_MAJKM));

                setTriggerStatus(TR_AUDIT, true);
                System.out.println(getTriggerStatus(TR_AUDIT));
            } catch (TriggerInvalidException e) {
                e.printStackTrace();
            }
        }
*/
        VueResultat vueResultat = new VueResultat();
        modele.enregistrerObservateur(vueResultat);

        modele.notifierObservateurs();

        ControleurConnexion controleurConnexion = new ControleurConnexion(modele);
        ControleurQuestion1 controleurQuestion1 = new ControleurQuestion1(modele);
        ControleurQuestion2 controleurQuestion2 = new ControleurQuestion2(modele);
        ControleurQuestion3 controleurQuestion3 = new ControleurQuestion3(modele);

        JTabbedPane onglets = new JTabbedPane();
        onglets.add("Question 1", controleurQuestion1);
        onglets.add("Question 2", controleurQuestion2);
        onglets.add("Question 3", controleurQuestion3);

        JFrame frame = new JFrame("LocaVe");
        frame.setLayout(new BorderLayout());
        frame.add(vueResultat, BorderLayout.SOUTH);
        frame.add(controleurConnexion, BorderLayout.NORTH);
        frame.add(onglets, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
