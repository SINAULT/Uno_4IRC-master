package duo.Players;

import duo.Cartes.AbstractCard;
import duo.Cartes.specialCard;
import duo.Misc.Helpers;
import duo.UI.Window;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rules {

    private final int hauteurImage = 170;
    private final static int longueurImage = 100;

    public static void Joueurjouer(Window fen, final AbstractCard cAJouer) {
        try {
            //On joue la carte et passe le parametre a joué (true)
            fen.imJoueur = jouerCarte(fen,cAJouer);
        } catch (final IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    private static void jouerJoueur(Window fen, final ArrayList<Image> vide) throws IOException {
        //On ajoute tous les cartes de la mains du joueur a l'arraylist des images de la cardsPlayer du joueur
        for (int i = 0; i < fen.partie.getPlayer().getCardsPlayer().size(); i++) {
            vide.add(ImageIO.read(new File(fen.partie.getJou().getCardsPlayer().getCard(i).getLink())));
        }
    }
    private static void piocherJoueur(Window fen) {
        try {
            fen.imJoueur.add(ImageIO.read(new File(fen.partie.getJou().getCardsPlayer().getCard(fen.partie.getJou().getCardsPlayer().size() - 1).getLink())));
        } catch (final IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Remettre les images au milieu
        final int sizeImagesJoueur = (fen.imJoueur.size() * longueurImage / 2) + 100;

        fen.startXPhoto = (Window.getWindowWidth(fen) - sizeImagesJoueur) / 2;

        fen.curseur = fen.imJoueur.size() / 2;

    }

    public static ArrayList<Image> jouerCarte(Window fen, final AbstractCard c) throws IOException {

        fen.partie.play(c);

        if (Window.jokerJoue && !fen.partie.getDiscard().getCard(fen.partie.getDiscard().size() - 1).jokerOrSuperJoker()) {
            fen.comptJokerJoue++;
        }

        if (fen.partie.getJou() instanceof realPlayer) {
            if (c.jokerOrSuperJoker()) {
                fen.showColorChoose(c);
                recupererIndiceCouleur(fen);
            }
            //Remettre bien le point de depart X des images
            fen.startXPhoto += 25;

            //Si le curseur est placé sur la derniere carte et on joue cette carte donc
            //  le curseur va se mettre sur la derniere carte restant
            if (fen.imJoueur.size() - 1 < fen.curseur) {
                fen.curseur -= 1;
            } else {
                fen.curseur = fen.imJoueur.size() / 2;
            }

        } else // si c'est un IA
        {
            if (c.jokerOrSuperJoker()) {
                IARules.choixOrdiCoul(fen, c);
                recupererIndiceCouleur(fen);
            }

            switch (fen.partie.getJou().getName()) {
                case "Ordi 1":
                    fen.startXPhotoOrdi1 += 25;
                    break;

                case "Ordi 2":
                    fen.startYPhotoOrdi2 += 25;
                    break;

                case "Ordi 3":
                    fen.startYPhotoOrdi3 += 25;
                    break;
            }
        }

        //Supprimer la carte jouer de la cardsPlayer du joueur
        fen.partie.getJou().getCardsPlayer().remove(c);

        final ArrayList<Image> vide = new ArrayList<>();

        if (fen.partie.getJou() instanceof realPlayer) {
            Rules.jouerJoueur(fen,vide);
        } else {
            IARules.jouerIA(fen, vide);
        }

        //Dessine la carte jouer sur la defausse
        try {
            //Mettre la photo de la defausse mise a jour
            fen.imDefausse = ImageIO.read(new File(fen.partie.getDiscard().getCard(fen.partie.getDiscard().size() - 1).getLink()));

        } catch (final IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }

        HandleWinner.testWinner(fen);

        return vide;
    }

    /**
     * Piocher une carte
     *
     * @param fen
     */
    public static void piocher(Window fen) {
        //Si la pioche est 0 (vide) donc il faut mix la defausse dans la pioche
        if (fen.partie.emptyPile()) {
            fen.partie.melangerDefausseDansPioche();
        }

        //Si la pioche au moins 1 carte donc on peut piocher
        if (fen.partie.getPile().size() > 0) {
            fen.partie.piocher();

            if (fen.partie.getJou() instanceof realPlayer) {
                piocherJoueur(fen);
            } else {
                IARules.piocherIA(fen);
            }
        }

        fen.repaint();
    }

    /**
     *
     * Méthode générique pour piocher X cartes
     *
     * @param fen
     * @param nb Nombre de cartes à piocher
     */
    private static void piocherN(Window fen, final int nb) {
        for (int i=0;i<nb;i++) {
            piocher(fen);
        }
    }


    public static void recupererIndiceCouleur(Window fen) {
        fen.colorJoker = "" + fen.partie.getDiscard().getCard(fen.partie.getDiscard().size() - 1).getColor();
    }

    /**
     * Changer l'ordre de jeu
     * @param fen
     */
    public static void changePriority(Window fen) {
        //Changer l'ordre de la priorite
        final AbstractPlayer j = fen.partie.getPlayers().getIRemove(0);
        j.setPrio(3);
        fen.partie.getPlayers().add(j);
    }

    /**
     *
     * Applique les effets des cartes spéciales
     *
     * @param fen Window
     * @param cS Special Card
     */
    public static void doSpecialities(Window fen, final specialCard cS) {

        // Si on joue un "+4"
        if (cS.getEffect().equals("SuperJoker")) {
            fen.superjokerJoue = true;
            piocherN(fen, 4);
            changePriority(fen);
        }

        // Si on joue un "+2"
        if (cS.getEffect().equals("Prend2")) {
            fen.compteurPrend2 += 2;

            if (!fen.partie.getJou().getCardsPlayer().isMoreTwo()) {
                /////////////////
                fen.dessineNbPrend2 = fen.compteurPrend2;
                piocherN(fen, fen.compteurPrend2);
                fen.prend2 = false;
                fen.compteurPrend2 = 0;
                changePriority(fen);
            } else {
                fen.prend2 = true;
            }
        }

        // Si on joue un "Inversion"
        if (cS.getEffect().equals("Inversion")) {
            fen.partie.inversion(fen.nbPlayer);
        }

        // Si un joue un "Passe ton tour"
        if (cS.getEffect().equals("PasseTonTour")) {
            if (!fen.partie.getJou().getCardsPlayer().isSkipRound()) {
                fen.passeTonTour = false;
                fen.dessinePasse = true;
                changePriority(fen);
            } else {
                fen.passeTonTour = true;
            }
        }

    }

    /**
     * Montrer les règles in-Game
     * @param fen
     */
    public static void showRules(Window fen) {
        //Charger un icon pour le joptionpane
        final ImageIcon icon = new ImageIcon("assets/imgMenu/regles.jpg");
        String reglesString = "";

        if (fen.nbPlayer == 2) // si 2 joueurs
        {
            try {
                reglesString = Helpers.readFile("assets/txt/regles2.txt"); // les rules pour 2 joueurs
            } catch (final IOException ex) {
                Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
            }

            JOptionPane.showMessageDialog(fen.getPaintArea(), reglesString, "Les règles du DUO", JOptionPane.YES_OPTION, icon);

        } else // si 3 ou 4 joueurs
        {
            try {
                reglesString = Helpers.readFile("assets/txt/regles34.txt"); // les rules pour 3 ou 4 joueurs
            } catch (final IOException ex) {
                Logger.getLogger(fen.getName()).log(Level.SEVERE, null, ex);
            }

            JOptionPane.showMessageDialog(fen.getPaintArea(), reglesString, "Les règles du DUO", JOptionPane.YES_OPTION, icon);
        }
    }
}
