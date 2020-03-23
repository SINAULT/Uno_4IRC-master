package duo.Players;

import java.util.ArrayList;

/**
 * @author DELGADO
 */
public class AllPlayers {

    /**
     * Initialise l'ArrayList de joueurs
     */
    private final ArrayList<AbstractPlayer> abstractPlayers;

    public AllPlayers() {
        this.abstractPlayers = new ArrayList<>();
    }

    public AbstractPlayer getEnd() {
        return abstractPlayers.remove(this.abstractPlayers.size() - 1);
    }

    public int getPrioTete() {
        return abstractPlayers.get(0).getPrio();
    }

    /**
     * @return Le nombre de joueurs
     */
    public boolean estVide() {
        return !this.abstractPlayers.isEmpty();
    }

    public AbstractPlayer getJoueur(String s) {
        AbstractPlayer j = this.abstractPlayers.get(0);
        int i = 0;
        boolean found = false;

        while (i < this.abstractPlayers.size() && !found) {
            if (s.equals(this.abstractPlayers.get(i).getName())) {
                found = true;
                j = this.abstractPlayers.get(i);
            }
            i++;
        }
        return j;
    }

    public AbstractPlayer peek() {
        return this.abstractPlayers.get(0);
    }

    /**
     * @param i Index du joueur
     * @return Un joueur unique
     */
    public AbstractPlayer getI(int i) {
        return this.abstractPlayers.get(i);
    }

    public AbstractPlayer getIRemove(int i) {
        return this.abstractPlayers.remove(i);
    }

    public AbstractPlayer get() {
        if (estVide()) {
            return this.abstractPlayers.remove(0);
        }
        return null;
    }

    /**
     * @return Nombre de joueurs
     */
    public int size() {
        return this.abstractPlayers.size();
    }

    /**
     * @param p Joueur
     *          Rajoute un joueur à la collection
     */
    public void add(AbstractPlayer p) {
        int i = 0;
        while ((i < abstractPlayers.size()) && (abstractPlayers.get(i).getPrio() <= p.getPrio())) {
            i++;
        }
        abstractPlayers.add(i, p);

    }

    public int positionJoueurReel() {
        boolean found = false;
        int position = 0;

        for (int i = 0; i < this.abstractPlayers.size(); i++) {
            if (this.abstractPlayers.get(i) instanceof realPlayer) {
                found = true;
                position = i;
            }
        }
        return position;
    }

    public AllPlayers joueursOrdi() {
        AllPlayers e = new AllPlayers();

        for (int i = 0; i < this.abstractPlayers.size(); i++) {
            if (this.abstractPlayers.get(i) instanceof IA) {
                e.add(this.abstractPlayers.get(i));
            }
        }

        return e;
    }

    public int nbIAaJouer() {
        int compt = 0;
        int i = 0;
        boolean stop = false;

        while (i < this.abstractPlayers.size() && !stop) {
            if (this.abstractPlayers.get(i) instanceof IA) {
                compt++;
            } else {
                stop = true;
            }

            i++;
        }

        return compt;
    }
}
