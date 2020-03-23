package duo.Cartes;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @author DELGADO
 */
public class AllCards {

    private final ArrayList<AbstractCard> cards;

    /**
     * Initialise l'ensemble de carte
     */
    public AllCards() {
        this.cards = new ArrayList<>();
    }

    /**
     * Verifie si nous n'avons plus de carte
     *
     * @return 0 si vide, 1 si non
     */
    private boolean isEmpty() {
        return !this.cards.isEmpty();
    }

    /**
     * @return Nombre de cards restantes
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Ajoute une carte à notre collection
     *
     * @param c Card
     */
    public void addCard(AbstractCard c) {
        this.cards.add(c);
    }

    /**
     * Enlève une carte à notre collection
     *
     * @param c Card
     */
    public void remove(AbstractCard c) {
        this.cards.remove(c);
    }

    /**
     * Melange nos cards de l'ArrayList
     */
    public void mix() {
        Collections.shuffle(this.cards);
    }

    public void concatEC(AllCards eC2) {
        while (eC2.isEmpty()) {
            this.addCard(eC2.get());
        }
    }

    /**
     * Retourne une carte
     *
     * @param i Index de la carte
     * @return AbstractCard
     */
    public AbstractCard getCard(int i) {
        return this.cards.get(i);

    }

    /**
     * @return null si ArrayList empty, sinon la carte supprimée
     */
    public AbstractCard get() {
        if (isEmpty()) {
            return this.cards.remove(0);
        }
        return null;
    }

    /**
     *
     * C'est ici qu'on implémente les règles
     *
     * @param c Carte
     * @return Si le joueur peut jouer la carte ou non
     */
    public boolean canPlayCard(AbstractCard c) {
        // Par défaut, on part du principe qu'on ne peut pas jouer une carte
        boolean canPlay = false;

        // On récupère la dernière carte de la défausse, et on analyse
        AbstractCard lastDiscardCard = this.getCard(this.size() - 1);

        // Si la defausse est une normalCard
        if (lastDiscardCard instanceof normalCard) {
            normalCard cD = (normalCard) lastDiscardCard;

            // Si la carte de la cardsPlayer est une normalCard
            if (c instanceof normalCard) {
                normalCard cN = (normalCard) c;

                if (cN.getColor().equals(cD.getColor()) || cN.getValue() == cD.getValue()) {
                    canPlay = true;
                }

                // Si la carte de la cardsPlayer est une specialCard
            } else {
                specialCard cS = (specialCard) c;

                if (cS.getColor().equals(cD.getColor()) || cS.getEffect().equals("Joker")
                        || cS.getEffect().equals("SuperJoker")) {
                    canPlay = true;
                }
            }

        } else {
            specialCard cD = (specialCard) lastDiscardCard;

            // Si la carte de la cardsPlayer est une normalCard
            if (c instanceof normalCard) {
                normalCard cN = (normalCard) c;

                if (cN.getColor().equals(cD.getColor())) {
                    canPlay = true;
                }
            } else {
                // Si la carte de la cardsPlayer est une specialCard
                specialCard cS = (specialCard) c;

                // Si la defausse est un SuperJoker
                if (cD.getEffect().equals("SuperJoker")) {

                    if ((cS.getEffect().equals("Joker"))
                            || (cS.getColor().equals(cD.getColor()) && !cS.getEffect().equals("SuperJoker"))) {
                        canPlay = true;
                    }

                } else {

                    if (cS.getColor().equals(cD.getColor()) || cS.getEffect().equals(cD.getEffect())
                            || cS.getEffect().equals("Joker") || cS.getEffect().equals("SuperJoker")) {
                        canPlay = true;
                    }
                }
            }
        }
        return canPlay;
    }

    /**
     *
     * Trouve toutes les cartes que l'IA peut jouer
     *
     * @param discard Defausse
     * @return Un ensemble de cartes que l'IA peut jouée
     */
    public AllCards possibilitesJouerIA(AllCards discard) {
        AllCards possibilities = new AllCards();
        AbstractCard cDefausse = discard.getCard(discard.size() - 1);

        for (int i = 0; i < this.size(); i++) {

            // Si la discard est une normalCard
            if (cDefausse instanceof normalCard) {
                normalCard cD = (normalCard) cDefausse;

                // Si la carte de la cardsPlayer est une normalCard
                if (this.getCard(i) instanceof normalCard) {
                    normalCard cN = (normalCard) this.getCard(i);

                    if (cN.getColor().equals(cD.getColor()) || cN.getValue() == cD.getValue()) {
                        possibilities.addCard(cN);
                    }

                } else // Si la carte de la cardsPlayer est une specialCard
                {
                    specialCard cS = (specialCard) this.getCard(i);

                    if (cS.getColor().equals(cD.getColor()) || cS.getEffect().equals("Joker")
                            || cS.getEffect().equals("SuperJoker")) {
                        possibilities.addCard(cS);
                    }
                }

            } else {
                // Si la discard est une carte speciale
                specialCard cD = (specialCard) cDefausse;

                // Si la carte de la cardsPlayer est une normalCard
                if (this.getCard(i) instanceof normalCard) {
                    normalCard cN = (normalCard) this.getCard(i);

                    if (cN.getColor().equals(cD.getColor())) {
                        possibilities.addCard(cN);
                    }

                } else {
                    // Si la carte de la cardsPlayer est une specialCard
                    specialCard cS = (specialCard) this.getCard(i);

                    // Si la discard est un SuperJoker
                    if (cD.getEffect().equals("SuperJoker")) {

                        if ((cS.getEffect().equals("Joker"))
                                || (cS.getColor().equals(cD.getColor()) && !cS.getEffect().equals("SuperJoker"))) {
                            possibilities.addCard(cS);
                        }
                    } else {
                        if (cS.getColor().equals(cD.getColor()) || cS.getEffect().equals(cD.getEffect())
                                || cS.getEffect().equals("SuperJoker") || cS.getEffect().equals("Joker")) {
                            possibilities.addCard(cS);
                        }
                    }
                }
            }
        }

        return possibilities;
    }

    /**
     *
     * Fonction qui permet de choisir la couleur du joker de l'IA
     *
     * @return string Couleur du Joker choisie
     */
    public String chooseColorIA() {
        int nbRed = 0;
        int nbBlue = 0;
        int nbGreen = 0;
        int nbYellow = 0;

        for (AbstractCard card : this.cards) {
            switch (card.getColor()) {
                case "Bleue":
                    nbBlue++;
                    break;

                case "Rouge":
                    nbRed++;
                    break;

                case "Vert":
                    nbGreen++;
                    break;

                case "Jaune":
                    nbYellow++;
                    break;
            }
        }

        if (nbRed >= nbBlue) {
            if (nbRed >= nbGreen) {
                if (nbRed >= nbYellow) {
                    return "Rouge";
                } else {
                    return "Jaune";
                }
            } else {
                if (nbGreen >= nbYellow) {
                    return "Vert";
                } else {
                    return "Jaune";
                }
            }
        } else {
            if (nbBlue >= nbYellow) {
                if (nbBlue >= nbGreen) {
                    return "Bleue";
                } else {
                    return "Vert";
                }
            } else {
                if (nbYellow >= nbGreen) {
                    return "Jaune";
                } else {
                    return "Vert";
                }
            }
        }
    }

    public boolean isSkipRound() {
        boolean found = false;
        int i = 0;

        while (i < this.cards.size() && !found) {
            if (this.cards.get(i).isSkipRound()) {
                found = true;
            }
            i++;
        }

        return found;
    }

    public AbstractCard renvoie1stPasseTonTour() {
        boolean found = false;
        AbstractCard c = this.cards.get(0);
        int i = 0;

        while (i < this.cards.size() && !found) {
            if (this.cards.get(i).isSkipRound()) {
                c = this.cards.get(i);
                found = true;
            }
            i++;
        }

        return c;
    }

    public boolean isMoreTwo() {
        boolean contient = false;
        int i = 0;

        while (i < this.cards.size() && !contient) {
            if (this.cards.get(i).isMoreTwo()) {
                contient = true;
            }
            i++;
        }

        return contient;
    }

    public boolean contientJokerOuSuperJoker() {
        boolean contient = false;
        int i = 0;

        while (i < this.cards.size() && !contient) {
            if (this.cards.get(i).jokerOrSuperJoker()) {
                contient = true;
            }
            i++;
        }

        return contient;
    }

    public AbstractCard renvoie1stPrend2() {
        boolean found = false;
        AbstractCard c = this.cards.get(0);
        int i = 0;

        while (i < this.cards.size() && !found) {
            if (this.cards.get(i).isMoreTwo()) {
                c = this.cards.get(i);
                found = true;
            }
            i++;
        }

        return c;
    }

    public AbstractCard jouerCartePlusCouleur(AllCards possib) {
        switch (this.chooseColorIA()) {
            case "Bleue":
                for (int j = 0; j < possib.size(); j++) {
                    if (possib.getCard(j).getColor().equals("Bleue")) {
                        return possib.getCard(j);
                    }
                }
                break;

            case "Rouge":
                for (int j = 0; j < possib.size(); j++) {
                    if (possib.getCard(j).getColor().equals("Rouge")) {
                        return possib.getCard(j);
                    }
                }
                break;

            case "Vert":
                for (int j = 0; j < possib.size(); j++) {
                    if (possib.getCard(j).getColor().equals("Vert")) {
                        return possib.getCard(j);
                    }
                }
                break;

            case "Jaune":
                for (int j = 0; j < possib.size(); j++) {
                    if (possib.getCard(j).getColor().equals("Jaune")) {
                        return possib.getCard(j);
                    }
                }
                break;
        }

        return possib.getCard(0);
    }

    // Affichage
    public void show() {
        for (AbstractCard card : this.cards) {
            card.show();
        }
    }

    public void loadCard(String fileName) throws IOException {
        AbstractCard uneCarte;

        for (Object res : (JSONArray) JSONValue.parse(new FileReader(fileName))) {
            JSONObject card = (JSONObject) res;

            String color = (String) card.get("color");
            String link = (String) card.get("link");
            String value = (String) card.get("value");

            int intValue = Integer.parseInt(value);

            uneCarte = new normalCard(color, link, intValue); // nouvelle carte
            this.cards.add(uneCarte); // ajouter la carte dans l'ensemble de cards

        }
    }

    public void loadSpecialCard(String fileName) throws IOException {
        AbstractCard uneCarte;

        for (Object res : (JSONArray) JSONValue.parse(new FileReader(fileName))) {
            JSONObject spCard = (JSONObject) res;

            String color = (String) spCard.get("color");
            String link = (String) spCard.get("link");
            String effect = (String) spCard.get("effect");

            uneCarte = new specialCard(color, link, effect); // nouvelle carte
            this.cards.add(uneCarte); // ajouter la carte dans l'ensemble de cards

        }
    }

}
