import java.sql.*;

public class Bdd {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
        String user = args[0];
        String password = args[1];

        if (connexion(url, user, password)) {
            ResultSet listeVehic = listeVehicules("c1", "2015-10-01", "2015-10-07");
            while (listeVehic.next()) {
                System.out.println(listeVehic.getString(1) + ' ' + listeVehic.getString(2));
            }
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
     * @return TRUE si la deconnexiona reussie, FALSE sinon
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
     * donne les resultats pour la question 1
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
     */
    public static ResultSet listeVehicules(String categorie, String dateDebut, String dateFin) throws SQLException {
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

        return stt.executeQuery();
    }
}
