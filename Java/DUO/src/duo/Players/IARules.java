package duo.Players;

import duo.Cartes.AbstractCard;
import duo.Cartes.AllCards;
import duo.Cartes.specialCard;
import duo.UI.Window;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IARules {

    private final int hauteurImage = 170;
    private final static int longueurImage = 100;

    /**
     * @param fen Fenetre
     */
    public static void traitementIA(Window fen) {
        /**
         * TODO: Implement showing button + Check for correct user ?
         * Detect 2 cards **/
        /** DEBUG
         if(fen.partie.getJou() instanceof IA) {
         System.out.println("Test IA OK");
         } else if(fen.partie.getJou() instanceof realPlayer) {
         System.out.println("Vrai Joueur");
         } **/
        if(fen.partie.checkCardsNumber(fen.partie.getJou().getCardsPlayer())) {
            System.out.println(fen.partie.getJou().getName());
            fen.duoButton.setVisible(true);
            fen.repaint();
        } else {
            fen.duoButton.setVisible(false);
            fen.repaint();
        }
        try {
            System.out.println("ICI");
            IAjouer(fen); // méthode suivante
        } catch (final IOException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    /**
     *
     * Algorythme de jeu de l'IA
     *
     * @param fen Fenetre
     * @throws IOException
     */
    private static void IAjouer(Window fen) throws IOException {
        // Retourne toutes les possibilités par rapport à la dernière carte
        final AllCards possibilites = fen.partie.getJou().getCardsPlayer().possibilitesJouerIA(fen.partie.getDiscard()); // ensemble de toutes les cartes qu'il peut jouer

        // S'il y a une ou plus possibilités et que la dernière carte est un passe ton tour
        if (fen.passeTonTour && possibilites.size() > 0) {
            switch (fen.partie.getJou().getName()) {
                case "Ordi 1":
                    fen.imOrdi1 = Rules.jouerCarte(fen, possibilites.renvoie1stPasseTonTour()); // essayer de jouer une carte d'effet PTT
                    break;

                case "Ordi 2":
                    fen.imOrdi2 = Rules.jouerCarte(fen, possibilites.renvoie1stPasseTonTour()); // essayer de jouer une carte d'effet PTT
                    break;

                case "Ordi 3":
                    fen.imOrdi3 = Rules.jouerCarte(fen, possibilites.renvoie1stPasseTonTour()); // essayer de jouer une carte d'effet PTT
                    break;
            }
            Rules.changePriority(fen);

            if (possibilites.renvoie1stPasseTonTour() instanceof specialCard) {
                Rules.doSpecialities(fen, (specialCard) possibilites.renvoie1stPasseTonTour()); // faire les changements necessaires
            }

        // S'il y a une ou plus possibilités et que la dernière carte est un +2
        } else if (fen.prend2 && possibilites.size() > 0) {
            switch (fen.partie.getJou().getName()) {
                case "Ordi 1":
                    fen.imOrdi1 = Rules.jouerCarte(fen, possibilites.renvoie1stPrend2()); // essayer de jouer une carte d'effet prend2
                    break;

                case "Ordi 2":
                    fen.imOrdi2 = Rules.jouerCarte(fen, possibilites.renvoie1stPrend2()); // essayer de jouer une carte d'effet prend2
                    break;

                case "Ordi 3":
                    fen.imOrdi3 = Rules.jouerCarte(fen, possibilites.renvoie1stPrend2()); // essayer de jouer une carte d'effet prend2
                    break;
            }
            Rules.changePriority(fen);

            if (possibilites.renvoie1stPrend2() instanceof specialCard) {
                Rules.doSpecialities(fen,(specialCard) possibilites.renvoie1stPrend2());  // faire les changements necessaires
            }

        // S'il y a une ou plus possibilités
        } else {
            if (possibilites.size() > 0) {
                // Recuperation de la meilleure carte possible à jouer
                final AbstractCard carteAJouer = fen.partie.meilleurCarteAJouer(possibilites);

                switch (fen.partie.getJou().getName()) {
                    case "Ordi 1":
                        fen.imOrdi1 = Rules.jouerCarte(fen, carteAJouer); //jouer la meilleure carte possible
                        break;

                    case "Ordi 2":
                        fen.imOrdi2 = Rules.jouerCarte(fen, carteAJouer); //jouer la meilleure carte possible
                        break;

                    case "Ordi 3":
                        fen.imOrdi3 = Rules.jouerCarte(fen, carteAJouer); //jouer la meilleure carte possible
                        break;
                }

                Rules.changePriority(fen);

                // Applique l'effet de la carte spéciale
                if (carteAJouer instanceof specialCard) {
                    final specialCard cS = (specialCard) carteAJouer;

                    Rules.doSpecialities(fen,cS);
                }
            // Si aucune carte n'est possible
            } else {
                Rules.piocher(fen);
                Rules.changePriority(fen);
            }
        }
    }

    static void piocherIA(Window fen) {
        switch (fen.partie.getJou().getName()) {
            case "Ordi 1":
                try {
                    fen.imOrdi1.add(ImageIO.read(new File("assets/ordiCards/cartesNord.png")));
                } catch (final IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }

                //Calcul pour mettres les photos, pareil que pour le joueur a part le Y qui est fixe
                final int sizeImagesOrdi1 = (fen.imOrdi1.size() * longueurImage / 2) + 150;
                fen.startXPhotoOrdi1 = (Window.getWindowWidth(fen) - sizeImagesOrdi1) / 2;
            break;

            case "Ordi 2":
                try {
                    fen.imOrdi2.add(ImageIO.read(new File("assets/ordiCards/cartesOuest.png")));
                } catch (final IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }

                final int sizeImagesOrdi2 = (fen.imOrdi2.size() * longueurImage / 2) + 200;
                fen.startYPhotoOrdi2 = ((Window.getWindowHeight(fen) + 50) - sizeImagesOrdi2) / 2;
            break;

            case "Ordi 3":
                try {
                    fen.imOrdi3.add(ImageIO.read(new File("assets/ordiCards/cartesEst.png")));
                } catch (final IOException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }

                final int sizeImagesOrdi3 = (fen.imOrdi3.size() * longueurImage / 2) + 200;
                fen.startYPhotoOrdi3 = ((Window.getWindowHeight(fen) + 50) - sizeImagesOrdi3) / 2;
            break;
        }
    }

    /**
     *
     * @param fen
     * @param vide
     * @throws IOException
     */
    static void jouerIA(Window fen, final ArrayList<Image> vide) throws IOException {
        switch (fen.partie.getJou().getName()) {
            case "Ordi 1":
                for (int i = 0; i < fen.partie.getJou().getCardsPlayer().size(); i++) {
                    vide.add(ImageIO.read(new File("assets/ordiCards/cartesNord.png")));
                }
                break;

            case "Ordi 2":
                for (int i = 0; i < fen.partie.getJou().getCardsPlayer().size(); i++) {
                    vide.add(ImageIO.read(new File("assets/ordiCards/cartesOuest.png")));
                }
                break;

            case "Ordi 3":
                for (int i = 0; i < fen.partie.getJou().getCardsPlayer().size(); i++) {
                    vide.add(ImageIO.read(new File("assets/ordiCards/cartesEst.png")));
                }
                break;

        }
    }

    /**
     * @param fen
     * @param c
     */
    public static void choixOrdiCoul(Window fen, final AbstractCard c) {
        final specialCard cS = (specialCard) c;
        final String coulChoisi = fen.partie.getJou().getCardsPlayer().chooseColorIA();

        cS.setColor(coulChoisi);
        Window.jokerJoue = true;
    }
}
