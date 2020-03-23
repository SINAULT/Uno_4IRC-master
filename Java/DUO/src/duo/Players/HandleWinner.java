package duo.Players;

import duo.Cartes.normalCard;
import duo.Cartes.specialCard;
import duo.Misc.Helpers;
import duo.UI.Window;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;

public class HandleWinner {

    private final Window fen;

    public HandleWinner(Window fen) {
        this.fen = fen;
    }

    public static void testWinner(Window fen) {
        for (int i = 0; i < fen.partie.getPlayers().size(); i++) {
            if (fen.partie.getPlayers().getI(i).getCardsPlayer().size() == 0) {
                fen.setHasWon(true);
            }
        }

    }

    /**
     * Test le winner à travers les différentes parties
     * @return boolean
     */
    private boolean testRealWinner() {
        final int scoreJoueur;
        final int scoreIA1;
        final int scoreIA2;
        final int scoreIA3;

        switch (this.fen.getNbPlayer()) {
            case 2:
                scoreJoueur = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0));
                scoreIA1 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 1));

                if (scoreJoueur >= this.fen.getFinalScore()) {
                    return true;
                } else {
                    return scoreIA1 >= this.fen.getFinalScore();
                }

            case 3:
                scoreJoueur = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0));
                scoreIA1 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 1));
                scoreIA2 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 2));

                return getBestScore(scoreJoueur, scoreIA1, scoreIA2);

            case 4:
                scoreJoueur = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0));
                scoreIA1 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 1));
                scoreIA2 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 2));
                scoreIA3 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 3));

                if (scoreJoueur >= this.fen.getFinalScore()) {
                    return true;
                } else {
                    return getBestScore(scoreIA1, scoreIA2, scoreIA3);
                }

            default:
                return false;
        }
    }

    private boolean getBestScore(int scoreJoueur, int scoreIA1, int scoreIA2) {
        if (scoreJoueur >= this.fen.getFinalScore()) {
            return true;
        } else {
            if (scoreIA1 >= this.fen.getFinalScore()) {
                return true;
            } else {
                return scoreIA2 >= this.fen.getFinalScore();
            }
        }
    }

    private void showRealWinner() {
        String winner = "";
        final int scorePlayer;
        final int scoreIA1;
        final int scoreIA2;

        switch (this.fen.getNbPlayer()) {
            case 2:
                scorePlayer = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0));

                if (scorePlayer >= this.fen.getFinalScore()) {
                    winner = this.fen.getScore().getColumnName(0);
                } else {
                    winner = this.fen.getScore().getColumnName(1);
                }
                break;

            case 3:
                scorePlayer = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0));
                scoreIA1 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 1));

                if (scorePlayer >= this.fen.getFinalScore()) {
                    winner = this.fen.getScore().getColumnName(0);
                } else if (scoreIA1 >= this.fen.getFinalScore()) {
                    winner = this.fen.getScore().getColumnName(2);
                } else {
                    winner = this.fen.getScore().getColumnName(3);
                }
                break;

            case 4:
                scorePlayer = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0));
                scoreIA1 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 1));
                scoreIA2 = Integer.parseInt((String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 2));

                if (scorePlayer >= this.fen.getFinalScore()) {
                    winner = this.fen.getScore().getColumnName(0);
                } else if (scoreIA1 >= this.fen.getFinalScore()) {
                    winner = this.fen.getScore().getColumnName(1);
                } else if (scoreIA2 >= this.fen.getFinalScore()) {
                    winner = this.fen.getScore().getColumnName(2);
                } else {
                    winner = this.fen.getScore().getColumnName(3);
                }
                break;
        }

        final ImageIcon perdu = new ImageIcon("assets/imgMenu/perdu.jpg");
        final ImageIcon gagne = new ImageIcon("assets/imgMenu/gagne.jpg");

        final Object[] abientot = {"A Bientôt & Quitter"};

        if (winner.equals(this.fen.getName())) {
            System.out.println("JOUEUR");
            final int res = JOptionPane.showOptionDialog(this.fen.getPaintArea(),
                    "************** FELICITATIONS **************"
                            + "\n\n\nVous avec gagné la partie complète", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, gagne, abientot, null);
            if (res == 0) {
                showScore();
                Helpers.leave();
            } else {
                Helpers.leave();
            }

        } else {
            System.out.println("IA :(");
            final int res = JOptionPane.showOptionDialog(this.fen.getPaintArea(),
                    "***************** DOMMAGE *****************"
                            + "\n\n\nVous avez malheursement perdu la partie complète", "", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, perdu, abientot, null);
            if (res == 0) {
                showScore();
                Helpers.leave();
            } else {
                Helpers.leave();
            }
        }

    }

    public void showScore() {
        final ImageIcon icon = new ImageIcon("assets/imgMenu/score.png");
        JOptionPane.showMessageDialog(this.fen.getPaintArea(), this.fen.getMenuScore(), "LES SCORES", JOptionPane.YES_OPTION, icon);
    }

    public void handleWinner() {
        if (this.fen.isHasWon()) {

            handleScore();

            if (testRealWinner()) {
                showRealWinner();
            } else {
                AbstractPlayer vainqueur = this.fen.getPartie().getJou();

                for (int i = 0; i < this.fen.getPartie().getPlayers().size(); i++) {
                    if (this.fen.getPartie().getPlayers().getI(i).getCardsPlayer().size() == 0) {
                        vainqueur = this.fen.getPartie().getPlayers().getI(i);
                    }
                }

                // nom du gagnant
                final String gagnant;
                ImageIcon icon;
                if (vainqueur instanceof IA) {
                    gagnant = "Vous Avez Perdu !\nLe gagnant est:\n" + vainqueur.getName();

                    //Charger l'icon "perdant" pour le joptionpane
                    icon = new ImageIcon("assets/imgMenu/perdre.jpg");
                } else {
                    gagnant = "FELICITATIONS\n" + vainqueur.getName() + "\nVous Avez Gagné !";

                    //Charger l'icon "gagnant" pour le joptionpane
                    icon = new ImageIcon("assets/imgMenu/gagne.jpg");
                }

                final int reponse = JOptionPane.showOptionDialog(null, gagnant, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        icon, new String[]{"Rejouer", "Quitter"}, "default");

                if (reponse == JOptionPane.YES_OPTION) {

                    showScore();

                    Window.erase(this.fen);
                    try {
                        this.fen.init(this.fen.getLongueur(), this.fen.getHauteur(), false);
                    } catch (final IOException ignored) {
                    }

                } else {
                    showScore();
                    Helpers.leave();
                }
            }
        }
    }

    private void handleScore() {
        String scoreJoueur = (String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 0);
        String scoreIA1 = (String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 1);
        String scoreIA2 = "0";
        String scoreIA3 = "0";

        switch (this.fen.getNbPlayer()) {
            case 3:
                scoreIA2 = (String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 2);
                break;

            case 4:
                scoreIA2 = (String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 2);
                scoreIA3 = (String) this.fen.getScore().getValueAt(this.fen.getScore().getModel().getRowCount() - 1, 3);
                break;
        }

        // Calcule du score
        for (int i = 0; i < this.fen.getPartie().getPlayers().size(); i++) {
            switch (this.fen.getPartie().getPlayers().getI(i).getName()) {
                case "Ordi 1":
                    scoreIA1 = calculScore(this.fen.getPartie().getPlayers().getI(i), scoreIA1);
                    break;

                case "Ordi 2":
                    scoreIA2 = calculScore(this.fen.getPartie().getPlayers().getI(i), scoreIA2);
                    break;

                case "Ordi 3":
                    scoreIA3 = calculScore(this.fen.getPartie().getPlayers().getI(i), scoreIA3);
                    break;

                default:
                    scoreJoueur = calculScore(this.fen.getPartie().getPlayers().getI(i), scoreJoueur);
                    break;
            }
        }

        //Ajouter le score a notre tableau de score
        final DefaultTableModel model = (DefaultTableModel) this.fen.getScore().getModel();
        switch (this.fen.getNbPlayer()) {
            case 2:
                model.addRow(new Object[]{scoreJoueur, scoreIA1});
                break;

            case 3:
                model.addRow(new Object[]{scoreJoueur, scoreIA1, scoreIA2});
                break;

            case 4:
                model.addRow(new Object[]{scoreJoueur, scoreIA1, scoreIA2, scoreIA3});
                break;

        }
    }

    private String calculScore(final AbstractPlayer j, final String score) {
        int compt = Integer.parseInt(score);

        if (j.getCardsPlayer().size() == 0) {
            for (int i = 0; i < this.fen.getPartie().getPlayers().size(); i++) {
                if (this.fen.getPartie().getPlayers().getI(i).getCardsPlayer().size() != 0) {

                    for (int k = 0; k < this.fen.getPartie().getPlayers().getI(i).getCardsPlayer().size(); k++) {
                        if (this.fen.getPartie().getPlayers().getI(i).getCardsPlayer().getCard(k) instanceof specialCard) {
                            final specialCard cS = (specialCard) this.fen.getPartie().getPlayers().getI(i).getCardsPlayer().getCard(k);

                            if (cS.jokerOrSuperJoker()) {
                                compt += 50;
                            } else {
                                compt += 20;
                            }
                        } else {
                            final normalCard cN = (normalCard) this.fen.getPartie().getPlayers().getI(i).getCardsPlayer().getCard(k);

                            compt += cN.getValue();
                        }
                    }
                }
            }

            return "" + compt;
        } else {
            return "" + compt;
        }
    }

}