import java.sql.*;
import java.util.HashMap;
import java.util.Hashtable;

public class Bdd {
    private static Connection connection;

    public static void main(String[] args) throws SQLException, DateInvalidFormatException {
        String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
        String user = args[0];
        String password = args[1];

        if (connexion(url, user, password)) {
            System.out.println(listeVehicules("c1", "2015-10-07", "2015-10-01"));
            //majCalendrierReserv("2015-10-01", "2015-10-07", "1234ya54");
        }
        deconnexion();
    }

    /**
     * se connecte a la base de donnees
     * @param url
     * @param user
     * @param password
     * @return TRUE si la connexion a pu etre etablie, FALSE sinon
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
     * se deconnecte de la base de donnees
     * @return TRUE si la deconnexion a reussie, FALSE sinon
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
     * @return
     */
    public static boolean estDate(String date) {
        return (date.matches("^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$"));
    }

    /**
     * donne la liste des vehicules disponibles
     * question 1
     * Détermination de la liste des véhicules (immatriculation, modèle), d’une
     * catégorie donnée, disponibles pendant une période donnée (date début, date
     * fin).
     * @param categorie
     *          categorie du vehicule recherche
     * @param dateDebut
     *          date de debut de la periode de recherche
     * @param dateFin
     *          date de fin de la periode de recherche
     * @return resultats, liste des vehicules (immatriculation, modèle)
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

            stt.close();
            return res;
        } else {
            throw new DateInvalidFormatException();
        }
    }

    /**
     * met a jour la table calendrier a partir des reservations (table dossier)
     * pour l'immatriculation donnee, sur la periode donnee
     * question 2
     * Mise à jour du calendrier des réservations pour une période donnée (date de
     * début et de fin d’une demande de location) et un numéro d’immatriculation
     * d’un véhicule.
     * @param dateDebut
     *          date de debut de la periode de mise a jour
     * @param dateFin
     *          date de fin de la periode de mise a jour
     * @param immatriculation
     *          immatriculation du vehicule a mettre a jour
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
}
