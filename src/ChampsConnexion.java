import javax.swing.*;
import java.awt.*;

/**
 * organise les differrents champs necessaires a la connexion
 */
public class ChampsConnexion extends JPanel {
    /**
     * champs du nom d'utilisateur
     */
    private JTextField user;
    /**
     * champ de l'url
     */
    private JTextField url;
    /**
     * champ du mot de passe
     */
    private JPasswordField password;

    /**
     * constructeur des champs
     */
    public ChampsConnexion() {
        JLabel labelUser = new JLabel("Nom d'utilisateur");
        user = new JTextField("", 15);
        JLabel labelPassword = new JLabel("Mot de passe");
        password = new JPasswordField("", 15);
        JLabel labelUrl = new JLabel("URL de la base de données");
        url = new JTextField("jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb", 50);

        JPanel panelUser = new JPanel();
        panelUser.add(labelUser, BorderLayout.WEST);
        panelUser.add(user, BorderLayout.CENTER);

        JPanel panelPassword = new JPanel();
        panelPassword.add(labelPassword, BorderLayout.WEST);
        panelPassword.add(password, BorderLayout.CENTER);

        JPanel panelUrl = new JPanel();
        panelUrl.add(labelUrl, BorderLayout.WEST);
        panelUrl.add(url, BorderLayout.CENTER);

        this.add(panelUser, BorderLayout.NORTH);
        this.add(panelPassword, BorderLayout.CENTER);
        this.add(panelUrl, BorderLayout.SOUTH);
    }

    /**
     * donne le nom d'utilisateur rentre dans le champ correspondant
     * @return nom d'utilisateur
     */
    public String getUser() {
        return this.user.getText();
    }

    /**
     * donne le mot de passe rentre dans le champ correspondant
     * @return mot de passe
     */
    public String getPassword() {
        return new String(this.password.getPassword());
    }

    /**
     * donne l'url rentree dans le champ correspondant
     * @return url
     */
    public String getUrl() {
        return this.url.getText();
    }
}
