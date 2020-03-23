package duo.Cartes;

public class specialCard extends AbstractCard {

    // Attributs
    private final String effet;

    // constructeur d'une carte spéciale
    public specialCard(String coul, String lien, String eff) {
        super(coul, lien);
        this.effet = eff;
    }

    // Getters
    public String getEffect() {
        return this.effet;
    }

    // Affichage
    public void show() {
        System.out.println("Couleur: " + this.getColor() + ", Lien: " + this.getLink() + ", Effet: " + this.effet + "");
    }

    public boolean jokerOrSuperJoker() {
        boolean found = false;

        if (this.getEffect().equals("Joker") || this.getEffect().equals("SuperJoker")) {
            found = true;
        }

        return found;

    }

    public boolean estSuperJoker() {
        return this.getEffect().equals("SuperJoker");
    }

    public boolean isMoreTwo() {
        return this.getEffect().equals("Prend2");
    }

    public boolean isSkipRound() {
        return this.getEffect().equals("PasseTonTour");
    }

    public boolean isInvert() {
        return this.getEffect().equals("Inversion");
    }
}
