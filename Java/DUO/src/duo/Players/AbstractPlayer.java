package duo.Players;

import duo.Cartes.AllCards;

/**
 * @author DELGADO
 */
public abstract class AbstractPlayer {

    // Attributs
    private String name;
    private int prio; // priorité du joueur
    final AllCards cardsPlayer; // la cardsPlayer du joueur

    AbstractPlayer(String name) {
        this.name = name;
        this.prio = 0;
        this.cardsPlayer = new AllCards();
    }

    // Setters
    public void setPrio(int prio) {
        this.prio = prio;
    }

    public void setName(String s) {
        this.name = s;
    }

    // Getters
    public int getPrio() {
        return this.prio;
    }

    public String getName() {
        return this.name;
    }

    public AllCards getCardsPlayer() {
        return this.cardsPlayer;
    }
}

