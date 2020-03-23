package duo.Cartes;

public class normalCard extends AbstractCard {

    private final int value;

    // constructeur d'une carte normale
    public normalCard(String color, String link, int val) {
        super(color, link);
        this.value = val;
    }

    // Affichage
    public void show() {
        System.out.println("Couleur: " + this.getColor() + ", Lien: " + this.getLink() + ", Valeur: " + this.getValue() + "");
    }

    // Getters
    public int getValue() {
        return this.value;
    }

    public boolean jokerOrSuperJoker() {
        return false;
    }

    public boolean isMoreTwo() {
        return false;
    }

    public boolean isSkipRound() {
        return false;
    }

    public boolean isInvert() {
        return false;
    }

    public boolean estSuperJoker() {
        return false;
    }
}
