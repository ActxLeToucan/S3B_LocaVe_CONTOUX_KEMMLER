import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * organise et controle les differents champs necessaires a la connexion
 */
public class ControleurConnexion extends JPanel implements ActionListener {
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
     * modele a controlerr
     */
    private Modele modele;

    /**
     * construction du controleur
     * @param modele
     *          modele a controler
     */
    public ControleurConnexion(Modele modele) {
        this.modele = modele;


        JLabel labelUser = new JLabel("Nom d'utilisateur");
        user = new JTextField("", 15);

        JLabel labelPassword = new JLabel("Mot de passe");
        password = new JPasswordField("", 15);

        JLabel labelUrl = new JLabel("URL de la base de données");
        url = new JTextField("jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb", 50);


        JPanel panelUser = new JPanel();
        panelUser.add(labelUser);
        panelUser.add(user);

        JPanel panelPassword = new JPanel();
        panelPassword.add(labelPassword);
        panelPassword.add(password);

        JPanel panelUrl = new JPanel();
        panelUrl.add(labelUrl);
        panelUrl.add(url);

        JButton buttonConnexion = new JButton("Se connecter");
        buttonConnexion.addActionListener(this);


        this.setLayout(new BorderLayout());
        this.add(panelUser, BorderLayout.WEST);
        this.add(panelPassword, BorderLayout.CENTER);
        this.add(buttonConnexion, BorderLayout.NORTH);
        this.add(panelUrl, BorderLayout.EAST);
    }

    /**
     * donne le nom d'utilisateur rentre dans le champ correspondant
     * @return nom d'utilisateur
     */
    private String getUser() {
        return this.user.getText();
    }

    /**
     * donne le mot de passe rentre dans le champ correspondant
     * @return mot de passe
     */
    private String getPassword() {
        return new String(this.password.getPassword());
    }

    /**
     * donne l'url rentree dans le champ correspondant
     * @return url
     */
    private String getUrl() {
        return this.url.getText();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Se connecter") == 0) {
            if (this.modele.connexion(getUrl(), getUser(), getPassword())) {
                ((JButton)e.getSource()).setBackground(Color.GREEN);
                ((JButton)e.getSource()).setText("Se déconnecter");
            } else {
                ((JButton)e.getSource()).setBackground(Color.RED);
            }
            this.modele.notifierObservateurs();
        } else if (e.getActionCommand().compareTo("Se déconnecter") == 0) {
            if (this.modele.deconnexion()) {
                ((JButton)e.getSource()).setBackground(Color.GREEN);
                ((JButton)e.getSource()).setText("Se connecter");
            } else {
                ((JButton)e.getSource()).setBackground(Color.RED);
            }
        }
    }
}
