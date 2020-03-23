package duo.UI;

import duo.Misc.Helpers;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("DanglingJavadoc")
class Welcome {
    static int nbPlayer;
    static String namePlayer;
    private static JPanel paintArea;

    private final static ImageIcon duo = new ImageIcon("assets/imgMenu/duo.png");

    /**
     * Affiche l'écran d'accueil
     */
    public static void welcome() {

        //Charger un icon pour le JOptionPane
        final ImageIcon icon = new ImageIcon("assets/imgMenu/commencer.png");

        final Object[] begin = {"Commencer la partie"};

        Component paintArea = null;
        JOptionPane.showOptionDialog(paintArea, "", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, begin, "Commencer la partie");
    }

    public static void showHelp() {
        Component paintArea = null;
        JOptionPane.showMessageDialog(paintArea, "<html><u><b> Bienvenue sur notre DUO </b></u></html>"
                + "\n\n\n\n \tComment jouer : "
                + "\n<html><ul><li><b>1.</b> Appuyer sur la carte souhaitée pour la <b>sélectionner</b></li>"
                + "<li><b>2.</b> Re-cliquer dessus pour la <b>jouer</b></li>"
                + "<li><b>3.</b> Cliquer sur la pioche pour <b>piocher</b></li>"
                + "<li>Les règles sont expliquées dans le bouton <b>Règle</b></li></ul></html>", "~~  QBIT Crew ~~", JOptionPane.YES_OPTION, duo);
    }

    /**
     * Affiche l'écran de sélection du nombre de joueurs
     */
    public static int selectPlayers() {

        // Charger un icon pour le jOptionPane
        final ImageIcon icon2 = new ImageIcon("assets/imgMenu/joueurs.jpg");

        // Tableau du nombre des joueurs possible
        final String[] nombreJoueurs = {"2", "3", "4"};

        // Sortie du string choisi et le mettre en int puis initialiser le nombre de joueur au nombre choisi
        final int num = JOptionPane.showOptionDialog(paintArea, "Nombre de joueurs", "Nombre de Joueurs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, icon2, nombreJoueurs, "4");

        /**
         * On init le nombre de joueur par rapport à la case appuyée
         * Le numéro de case commence à 0 qui correspond à 2 joueurs...
         */
        switch (num) {
            case 0:
                nbPlayer =2;
                break;

            case 1:
                nbPlayer =3;
                break;

            case 2:
                nbPlayer =4;
                break;

            default:
                Helpers.leave();
        }
        return nbPlayer;
    }

    /**
     * Demander au joueur son nom
     * @return
     */
    public static String askPlayerName() {
        final ImageIcon iconName = new ImageIcon("assets/imgMenu/name.png");

        final String name = (String) JOptionPane.showInputDialog(paintArea, "Saisissez votre nom: ", "", JOptionPane.WARNING_MESSAGE, iconName, null, null);

        if (name == null) {
            Helpers.leave();
        }

        if (name.isEmpty()) {
            askPlayerName();
        } else {
            namePlayer = name;
        }
        return namePlayer;
    }


}