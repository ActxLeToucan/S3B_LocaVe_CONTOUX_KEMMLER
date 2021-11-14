import java.sql.*;
import java.util.*;

public class Bdd {
    private static Connection connection;
    private static final String TR_MAJKM = "tr_maj_km_vehicule";
    private static final String TR_AUDIT = "tr_audit";
    private static final Set<String> TRIGGERS = new HashSet<>(Arrays.asList(TR_MAJKM, TR_AUDIT));

    public static void main(String[] args) throws SQLException, DateInvalidFormatException {
        String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
        String user = args[0];
        String password = args[1];

        if (connexion(url, user, password)) {
            resetTriggers();
            System.out.println(listeVehicules("c1", "2015-10-07", "2015-10-01"));
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
        deconnexion();
    }

    /**
     * se connecte à la base de donnees
     * @param url
     *          url de la base de donnees
     * @param user
     *          nom d'utilisateur
     * @param password
     *          mot de passe
     * @return TRUE si la connexion a pu être établie, FALSE sinon
     */
    public static boolean connexion(String url, String user, String password) {
        boolean connecte = true;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            connecte = false;
        }
        return connecte;
    }

    /**
     * se déconnecte de la base de donnees
     * @return TRUE si la déconnexion a réussie, FALSE sinon
     */
    public static boolean deconnexion() {
        boolean deconnecte = true;
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            deconnecte = false;
        }
        return deconnecte;
    }

    /**
     * indique si la date est bien au format YYYY-MM-DD
     * @param date
     *          chaine de caractères contenant une date
     * @return true si bon format
     */
    public static boolean estDate(String date) {
        return (date.matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$"));
    }

    /**
     * donne la liste des véhicules disponibles
     * question 1
     * Détermination de la liste des véhicules (immatriculation, modèle), d’une
     * catégorie donnée, disponibles pendant une période donnée (date début, date
     * fin).
     * @param categorie
     *          catégorie du véhicule recherche
     * @param dateDebut
     *          date de debut de la période de recherche
     * @param dateFin
     *          date de fin de la période de recherche
     * @return résultats, liste des véhicules (immatriculation, modèle)
     * @throws SQLException
     * @throws DateInvalidFormatException
     */
    public static String listeVehicules(String categorie, String dateDebut, String dateFin) throws SQLException, DateInvalidFormatException {
        if (estDate(dateDebut) && estDate(dateFin)) {
            if (dateDebut.compareTo(dateFin)>0) {
                String temp = dateDebut;
                dateDebut = dateFin;
                dateFin = temp;
            }
            PreparedStatement stt = connection.prepareStatement(
                    "SELECT DISTINCT vehicule.no_imm, vehicule.modele" +
                            " FROM vehicule" +
                            " INNER JOIN dossier ON vehicule.no_imm = dossier.no_imm" +
                            " WHERE code_categ = ?" +
                            " MINUS" +
                            " SELECT DISTINCT vehicule.no_imm, vehicule.modele" +
                            " FROM vehicule" +
                            " INNER JOIN dossier ON vehicule.no_imm = dossier.no_imm" +
                            " WHERE code_categ = ?" +
                            " AND ((date_retrait >= ? AND date_retrait <= ?)" +
                            " OR (date_retour >= ? AND date_retour <= ?))");
            stt.setString(1, categorie);
            stt.setString(2, categorie);
            stt.setDate(3, java.sql.Date.valueOf(dateDebut));
            stt.setDate(4, java.sql.Date.valueOf(dateDebut));
            stt.setDate(5, java.sql.Date.valueOf(dateFin));
            stt.setDate(6, java.sql.Date.valueOf(dateFin));

            ResultSet resultSet = stt.executeQuery();
            String res = "";
            int lignes = 0;
            while (resultSet.next()) {
                lignes++;
                res += "Une " + resultSet.getString(2) + " immatriculée " + resultSet.getString(1) + " est disponible.\n";
            }
            if (lignes == 0) {
                res = "Aucun véhicule disponible.";
            }

            resultSet.close();
            stt.close();
            return res;
        } else {
            throw new DateInvalidFormatException();
        }
    }

    /**
     * met à jour la table calendrier à partir des reservations (table dossier)
     * pour l'immatriculation donnee, sur la période donnee
     * question 2
     * Mise à jour du calendrier des réservations pour une période donnée (date de
     * début et de fin d’une demande de location) et un numéro d’immatriculation
     * d’un véhicule.
     * @param dateDebut
     *          date de debut de la période de mise à jour
     * @param dateFin
     *          date de fin de la période de mise à jour
     * @param immatriculation
     *          immatriculation du véhicule à mettre a jour
     * @throws SQLException
     * @throws DateInvalidFormatException
     */
    public static void majCalendrierReserv(String dateDebut, String dateFin, String immatriculation) throws SQLException, DateInvalidFormatException {
        if (estDate(dateDebut) && estDate(dateFin)) {
            if (dateDebut.compareTo(dateFin)>0) {
                String temp = dateDebut;
                dateDebut = dateFin;
                dateFin = temp;
            }
            CallableStatement stt = connection.prepareCall("{call proc_majCalendrier(?, ?, ?)}");
            stt.setDate(1, java.sql.Date.valueOf(dateDebut));
            stt.setDate(2, java.sql.Date.valueOf(dateFin));
            stt.setString(3, immatriculation);

            stt.execute();

            stt.close();
        } else {
            throw new DateInvalidFormatException();
        }
    }

    /**
     * donne le montant d'une location
     * question 3
     * Calcul du montant d’une location à partir du modèle de véhicule et du
     * nombre de jours de location.
     * @param modele
     *          modèle de véhicule demande
     * @param nbJours
     *          nombre de jours
     * @return montant ou message indiquant pas de tarif
     * @throws SQLException
     */
    public static String calculerMontant(String modele, int nbJours) throws SQLException {
        PreparedStatement stt = connection.prepareStatement("SELECT DISTINCT tarif.tarif_hebdo, tarif.tarif_jour" +
                " FROM vehicule" +
                " INNER JOIN categorie ON categorie.code_categ = vehicule.code_categ" +
                " INNER JOIN tarif ON tarif.code_tarif = categorie.code_tarif" +
                " WHERE VEHICULE.MODELE = ?");
        stt.setString(1, modele);
        ResultSet resultSet = stt.executeQuery();

        String res;
        if (resultSet.next()) {
            double tarifJour = resultSet.getDouble("tarif_jour");
            double tarifHebdo = resultSet.getDouble("tarif_hebdo");
            res = nbJours%7 * tarifJour + Math.floor(nbJours/7.) * tarifHebdo + "€";
        } else {
            res = "Aucun tarif pour ce modèle.";
        }

        resultSet.close();
        stt.close();

        return res;
    }

    /**
     * donne les agences qui ont toutes les categories de véhicule
     * question 4
     * Affichage de la liste des agences (code de l’agence) qui possèdent toutes les
     * catégories de véhicules.
     * @return codes des agences
     * @throws SQLException
     */
    public static String agencesAvecToutesCateg() throws SQLException {
        CallableStatement stt = connection.prepareCall("{? = call f_agenceAvecToutesCateg()}");
        stt.registerOutParameter(1, Types.VARCHAR);

        stt.execute();
        String res = stt.getString(1);

        stt.close();
        return res;
    }

    /**
     * donne les clients qui ont loué au moins deux modèles différents
     * question 5
     * Affichage de la liste des clients (nom, ville, code postal) qui ont loué deux
     * modèles différents de voiture (par exemple xsara et twingo).
     * @return liste des clients
     * @throws SQLException
     */
    public static String clientsPlusieursModeles() throws SQLException {
        CallableStatement stt = connection.prepareCall("{? = call f_clientPlusieursModeles()}");
        stt.registerOutParameter(1, Types.VARCHAR);

        stt.execute();
        String res = stt.getString(1);

        stt.close();
        return res;
    }

    /**
     * change l'état d'un trigger existant
     * @param triggerName
     *          nom du trigger
     * @param etat
     *          état demandé (true = actif, false = désactivé)
     * @throws SQLException
     * @throws TriggerInvalidException
     */
    public static void setTriggerStatus(String triggerName, boolean etat) throws SQLException, TriggerInvalidException {
        if (TRIGGERS.contains(triggerName)) {
            String status;
            if (etat) status = "ENABLE";
            else status = "DISABLE";
            PreparedStatement stt = connection.prepareStatement("ALTER TRIGGER " + triggerName + " " + status);
            stt.executeUpdate();
            stt.close();
        } else {
            throw new TriggerInvalidException(triggerName);
        }
    }

    /**
     * récupère l'état d'un trigger
     * @param triggerName
     *          nom du trigger
     * @return 1 si le trigger est actif
     *          0 si le trigger est désactivé
     *          -1 en cas d'erreur
     * @throws TriggerInvalidException
     * @throws SQLException
     */
    public static int getTriggerStatus(String triggerName) throws TriggerInvalidException, SQLException {
        int res = -1;
        if (TRIGGERS.contains(triggerName)) {
            PreparedStatement stt = connection.prepareStatement("SELECT STATUS FROM USER_TRIGGERS WHERE TRIGGER_NAME = ?");
            stt.setString(1, triggerName.toUpperCase());
            ResultSet resultSet = stt.executeQuery();
            if (resultSet.next()) {
                String status = resultSet.getString("STATUS");
                if (status.compareTo("ENABLED") == 0) res = 1;
                else if (status.compareTo("DISABLED") == 0) res = 0;
            }
            resultSet.close();
            stt.close();
        } else {
            throw new TriggerInvalidException(triggerName);
        }
        return res;
    }

    /**
     * reinitialise l'état des triggers a actif
     * @throws SQLException
     */
    public static void resetTriggers() throws SQLException {
        for (String trigger : TRIGGERS) {
            try {
                setTriggerStatus(trigger, true);
                System.out.println("trigger " + trigger + " : " + getTriggerStatus(trigger));
            } catch (TriggerInvalidException e) {
                e.printStackTrace();
            }
        }
    }
}
