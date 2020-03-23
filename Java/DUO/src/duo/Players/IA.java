package duo.Players;

import duo.Cartes.normalCard;
import duo.Cartes.specialCard;

/**
 * IA
 */
public class IA extends AbstractPlayer {

    // Constructeur
    public IA(String name) {
        super(name);
    }

    public boolean includeSpecialCard() {
        int i = 0;
        boolean include = false;
        while ((i < this.cardsPlayer.size()) && !include) {
            if (this.cardsPlayer.getCard(i) instanceof specialCard) {
                include = true;
            }
            i++;
        }
        return include;
    }

    public boolean includeNormalCard() {
        int i = 0;
        boolean include = false;
        while ((i < this.cardsPlayer.size()) && !include) {
            if (this.cardsPlayer.getCard(i) instanceof normalCard) {
                include = true;
            }
            i++;
        }
        return include;
    }

}
