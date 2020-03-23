package duo.Cartes;

/**
 * @author DELGADO
 */
public abstract class AbstractCard {

    /**
     * Couleur de la carte
     */
    private String color;

    /**
     * Lien vers l'image
     */
    private final String link;

    /**
     * @param color Couleur
     * @param link  Lien vers l'image
     */
    AbstractCard(String color, String link) {
        this.color = color;
        this.link = "assets/imgCards/" + link;
    }

    /**
     * @return color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * @return link
     */
    public String getLink() {
        return this.link;
    }

    // Setters
    public void setColor(String color) {
        this.color = color;
    }

    // Affichage
    public abstract void show();

    public abstract boolean jokerOrSuperJoker();

    public abstract boolean isMoreTwo();

    public abstract boolean isSkipRound();

    public abstract boolean isInvert();

    public abstract boolean estSuperJoker();

}

