package duo.Controller;

import duo.Cartes.AbstractCard;
import duo.Cartes.AllCards;
import duo.Cartes.specialCard;
import duo.Players.AbstractPlayer;
import duo.Players.AllPlayers;
import duo.Players.IA;
import duo.Players.realPlayer;

import java.io.IOException;

/**
 * @author DELGADO
 */
public class Partie {

    /**
     * Nombre de joueurs, pile et discard
     */
    private final AllPlayers players;
    private AllCards pile;
    private final AllCards discard;

    /**
     * @param nbPlayers
     * @throws IOException
     */
    public Partie(int nbPlayers) throws IOException {
        pile = new AllCards();
        discard = new AllCards();
        players = new AllPlayers();

        pile.loadCard("assets/txt/normalCards.json");
        pile.loadSpecialCard("assets/txt/specialCards.json");
        pile.mix();

        players.add(new realPlayer("Moi")); // Creation du AbstractPlayer

        // Création des (nbPlayer-1) IA
        for (int i = 1; i < nbPlayers; i++) {
            players.add(new IA("Ordi " + i));
        }

        // Pour ajouter des 7 cartes selon le nombre des joueurs à la cardsPlayer du joueur
        for (int i = 0; i < nbPlayers; i++) {
            for (int j = 0; j < 7; j++) {
                AbstractPlayer jou = players.get();
                jou.getCardsPlayer().addCard(pile.get());

                players.add(jou);
            }
        }

        // Ajoute la première carte de la pioche à la défausse pour pouvoir commencer
        discard.addCard(pile.get());

    }

    /**
     * S'il ne s'agit pas d'une nouvelle partie, alors on restart
     *
     * @param nbPlayers
     */
    public void reStart(int nbPlayers) {
        pile = recupCarte();
        pile.mix();

        while (players.estVide()) {
            players.get();
        }

        players.add(new realPlayer("Moi"));

        // Create IA (-1 to exclude real player)
        for (int i = 1; i < nbPlayers; i++) {
            players.add(new IA("Ordi " + i));
        }
        // Add 7 Cards to All Players
        for (int i = 0; i < nbPlayers; i++) {
            for (int j = 0; j < 7; j++) {
                players.getI(i).getCardsPlayer().addCard(pile.get());
            }
        }

        // On ajoute la première carte de la pile à le défausse pour commencer
        discard.addCard(pile.get());

    }

    private AllCards recupCarte() { // Récupération de toutes les cartes du jeu dans un paquet
        AllCards paquet = new AllCards();
        for (int i = 0; i < players.size(); i++) {
            AllCards cartePlayer = players.get().getCardsPlayer();
            paquet.concatEC(cartePlayer);
        }
        paquet.concatEC(discard);
        paquet.concatEC(pile);
        return paquet;
    }

    public void play(AbstractCard c) {
        // ACTIONS DES CARTES
        if (c instanceof specialCard) {
            specialCard cS = (specialCard) c;
        }

        discard.addCard(c); // déplacement de la carte jouée de la cardsPlayer du joueur à la discard
    }

    public void piocher() {
        players.getI(0).getCardsPlayer().addCard(pile.get());
    }

    public boolean emptyPile() {
        return pile.size() == 0;
    }

    public void melangerDefausseDansPioche() {

        // Tester s'il y a au moins 2 cartes dans la discard pour mettre minimum 1 dans la pile
        if (discard.size() > 1) {
            AbstractCard premCarteDefausse = discard.getCard(0); // Récupération de la dernière carte jouée

            // Supprimer la premiere carte de la discard pour ne pas la mettre dans la pile
            discard.get();

            for (int i = 0; i < discard.size(); i++) {
                pile.addCard(discard.get());
            }

            // Remettre la derniere carte jouée dans la discard
            discard.addCard(premCarteDefausse);

            // Et on melange la nouvelle pile
            pile.mix();
        }

    }

    public void inversion(int nbJoueurs) {
        if (nbJoueurs == 4) {
            // Changer l'ordre de la priorite
            AbstractPlayer j1 = getPlayers().getIRemove(0);
            AbstractPlayer j2 = getPlayers().getIRemove(0);
            AbstractPlayer j3 = getPlayers().getIRemove(0);
            AbstractPlayer j4 = getPlayers().getIRemove(0);

            j1.setPrio(2);
            j2.setPrio(1);
            j3.setPrio(0);
            j4.setPrio(3);

            getPlayers().add(j3);
            getPlayers().add(j2);
            getPlayers().add(j1);
            getPlayers().add(j4);
        }

        if (nbJoueurs == 3) {
            // Changer l'ordre de la priorite
            AbstractPlayer j1 = getPlayers().getIRemove(0);
            AbstractPlayer j2 = getPlayers().getIRemove(0);
            AbstractPlayer j3 = getPlayers().getIRemove(0);

            j1.setPrio(1);
            j2.setPrio(0);
            j3.setPrio(2);

            getPlayers().add(j2);
            getPlayers().add(j1);
            getPlayers().add(j3);
        }

        if (nbJoueurs == 2) {
            // Changer l'ordre de la priorite (pouvoir rejouer)
            AbstractPlayer j = getPlayers().getIRemove(0);
            j.setPrio(3);
            getPlayers().add(j);
        }
    }

    public void jeuAQuatre() {
        AbstractPlayer j1 = getPlayers().getIRemove(0);
        AbstractPlayer j2 = getPlayers().getIRemove(0);
        AbstractPlayer j3 = getPlayers().getIRemove(0);
        AbstractPlayer j4 = getPlayers().getIRemove(0);
        getPlayers().add(j1); // joueur réel
        getPlayers().add(j4); // Ordi 3
        getPlayers().add(j2); // Ordi 1
        getPlayers().add(j3); // Ordi 2
    }

    /**
     * @param possib Toutes les cartes que l'IA possède
     * @return Carte à jouer
     */
    public AbstractCard meilleurCarteAJouer(AllCards possib) {
        boolean found = false;

        if (getJou().getCardsPlayer().size() > 3) {

            // Si prochain joueur a plus que 2 cartes ou moins il essaie de joueur un joker
            if (players.getI(1).getCardsPlayer().size() < 2) {
                int i = 0;

                return this.getCarte(possib, found, i);

                // Si prochain joueur a plus que 4 cartes ou moins l'IA va essayer de joueur une
                // carte speciale
            } else if (players.getI(1).getCardsPlayer().size() < 5) {
                int i = 0;

                while (i < possib.size() && !found) {
                    if (possib.getCard(i) instanceof specialCard) {
                        found = true;
                        return possib.getCard(i);
                    }

                    i++;
                }

                return possib.getCard(0);

                // Sinon l'IA joue une carte avec une couleur dont il a beaucoup (meme couleur)
            } else {
                return getJou().getCardsPlayer().jouerCartePlusCouleur(possib);
            }

            // Si IA a plus que 3 cartes il veut jouer un joker si possible sinon la carte
            // avec plus de couleur
        } else {
            int i = 0;
            if (possib.contientJokerOuSuperJoker()) {
                return this.getCarte(possib, found, i);
            } else {
                // Essayer de jouer la couleur la plus presente sinon premier possibilite
                return getJou().getCardsPlayer().jouerCartePlusCouleur(possib);
            }
        }

    }

    private AbstractCard getCarte(AllCards possib, boolean trouve, int i) {
        while (i < possib.size() && !trouve) {
            if (possib.getCard(i) instanceof specialCard) {
                specialCard cS = (specialCard) possib.getCard(i);
                if (cS.jokerOrSuperJoker()) {
                    trouve = true;
                    return possib.getCard(i);
                }
            }

            i++;
        }

        return possib.getCard(0);
    }

    /**
     * @return Toute la pioche
     */
    public AllCards getPile() {
        return pile;
    }

    /**
     * @return Toute la défausse
     */
    public AllCards getDiscard() {
        return discard;
    }

    /**
     * @return ArrayList de tous les joueurs
     */
    public AllPlayers getPlayers() {
        return players;
    }

    /**
     * @return le joueur réel
     */
    public realPlayer getPlayer() {
        return (realPlayer) players.getI(players.positionJoueurReel());
    }

    /**
     * @param i Index de l'IA
     * @return Une IA
     */
    public IA getIA(int i) {
        return (IA) players.joueursOrdi().getJoueur("Ordi " + i);
    }

    public AbstractPlayer getJou() {
        return players.peek();
    }

    /**
     * Detect number of cards
     * @param cards
     * @return 1 : 2 Cards | 0 : More than 2 cards
     */
    public boolean checkCardsNumber(AllCards cards) {
        return cards.size() == 2;
    }
}
