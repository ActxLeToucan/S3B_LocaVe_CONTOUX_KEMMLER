import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.*;

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

        ChampsConnexion champsConnexion = new ChampsConnexion();
        ControleurConnexion controleurConnexion = new ControleurConnexion(modele, champsConnexion);
        JButton buttonConnexion = new JButton("Se connecter");
        buttonConnexion.addActionListener(controleurConnexion);

        JFrame frame = new JFrame("LocaVe");
        frame.add(buttonConnexion, BorderLayout.NORTH);
        frame.add(vueResultat, BorderLayout.SOUTH);
        frame.add(champsConnexion, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
