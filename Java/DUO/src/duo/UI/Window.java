package duo.UI;

import duo.Cartes.AbstractCard;
import duo.Cartes.specialCard;
import duo.Controller.Partie;
import duo.Players.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Window extends JFrame implements MouseListener {

    private final HandleWinner winner;

    private JPanel paintArea;            // Zone de dessin central ou on va dessiner
    private Robot r;

    /**
     * Tableau Score
     */
    private JTable score;


    /**
     * Menu Score
     */
    private JScrollPane menuScore;

    private JButton rulesButton;
    private JButton scoreButton;
    public JButton duoButton;

    /**
     * Contient tout. Infos pioche, defausse, joueur et leur mains
     */
    public Partie partie;

    /**
     * Image Defausse/Pioche
     */
    public Image imDefausse;
    private Image imPioche;

    /**
     * @ArrayList<> Stock les images de cartes pour les différents joueurs/ordinateurs
     */
    public ArrayList<Image> imJoueur;
    public ArrayList<Image> imOrdi1;
    public ArrayList<Image> imOrdi2;
    public ArrayList<Image> imOrdi3;

    /**
     * @int Point de départ des différents joueurs/ordinateurs
     */
    public int startXPhoto;
    public int startXPhotoOrdi1;
    public int startYPhotoOrdi2;
    public int startYPhotoOrdi3;

    /**
     * @int Taille des images
     */
    private final int hauteurImage = 170;
    private final int longueurImage = 100;

    public int curseur; // AbstractCard Sélectionnée

    private JButton miscRulesButton;

    public int getNbPlayer() {
        return this.nbPlayer;
    }

    /**
     * @int Score max possible (Après fin de partie)
     */
    public int getFinalScore() {
        return 500;
    }

    /**
     * @int Nombre de joueur
     */
    public final int nbPlayer;

    /**
     * @int Nombre de carte à piocher
     */
    private int nbAPiocher;                 // Nombre de cartes a piocher

    /**
     * @bool Est-ce que le joueur a joué ?
     */
    private boolean hasPlayed;

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }

    /**
     * @bool Joueur a gangé ?
     */
    private boolean hasWon;


    /**
     * Effets
     */
    public int compteurPrend2;
    public int dessineNbPrend2;
    public boolean passeTonTour;
    public boolean superjokerJoue;
    public boolean dessinePasse;
    public boolean prend2;

    /**
     * Taille de la carte
     */
    private final int hauteur;              //hauteur de la fenetre

    private final int longueur;             //longueur de la fenetre

    private boolean jouerOuPiocher;         //le joueur a joué ou pioché (ou pas)

    public static boolean jokerJoue;              //un joker a ete joué

    public String colorJoker;            //la couleur souhaite avec le joker

    public int comptJokerJoue;             //compteur des joker joué

    private final String namePlayer;              //le nom du joueur (initialisé au debut avec l'optionpane)

    private ImageIcon icon;                 // image de fin de partie : "gagnant" ou "perdu"
    private int maxX;
    private int maxY;

    /**
     * Getters
     */
    public JPanel getPaintArea() {
        return this.paintArea;
    }

    public JTable getScore() {
        return this.score;
    }

    public JScrollPane getMenuScore() {
        return this.menuScore;
    }

    public int getHauteur() {
        return this.hauteur;
    }

    public int getLongueur() {
        return this.longueur;
    }

    public Partie getPartie() {
        return this.partie;
    }

    public Graphics getzoneDessin() {
        return this.paintArea.getGraphics();
    }

    public boolean isHasWon() {
        return this.hasWon;
    }


    /**
     * Constructeur
     * @param width
     * @param height
     * @param firstGame
     * @throws IOException
     */
    public Window(final int width, final int height, final boolean firstGame) throws IOException {

        // Titre de la fenêtre
        super("DUO");
        
        
        this.winner = new HandleWinner(this);

        this.hauteur = height;
        this.longueur = width;
        
        /* Ask player's name */
        //this.namePlayer = Welcome.askPlayerName();
        this.namePlayer = GestionJoueur.getNomJoueur();
        /* Choose number of players */
        this.nbPlayer = GestionJoueur.getNbJoueurs();

        /* Ici on va initialiser la fenetre du jeu DUO */
        getContentPane().setLayout(new BorderLayout());
        setResizable(true);

        /* Ferme proprement le jeu quand on appuie sur la croix */
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setDuoLayout(width, height);    // on place les boutons et la zone de dessin

        addMouseListener(this);

        setFocusable(true);

        initArrayListsImages();

        // Initialisation du tableau de scores
        initTableScore();

        //On lance la fonction init() pour initialiser le jeu
        init(width, height, firstGame);

        setLocationRelativeTo(null);
        setVisible(true);

        revalidate();
        repaint();
        pack();
    }



    private void setDuoLayout(final int maxX, final int maxY) throws IOException {
        //--------------------------------------------------------------------
        //--------------------------------------------------------------------
        //--------------------------------------------------------------------
        this.rulesButton = new JButton();  // bouton Aide
        this.rulesButton.setLayout(null);

        this.scoreButton = new JButton(); // bouton Score
        this.scoreButton.setLayout(null);

        // Duo Button
        this.duoButton = new JButton(); // bouton Score
        this.duoButton.setLayout(null);
        this.duoButton.setVisible(false); // Opaque disable : opaque if there's 2 cards

        // Duo Button
        this.miscRulesButton = new JButton(); // bouton Score
        this.miscRulesButton.setLayout(null);

        // Zone de dessin
        this.paintArea = new JPanel();
        this.paintArea.setLayout(null);
        this.paintArea.setSize(maxX, maxY);
        this.paintArea.setPreferredSize(new Dimension(maxX, maxY));

        getContentPane().add(this.paintArea, "Center");  // panel pour zoneDessiner au milieu

        //--------------------------------------------------------------------
        // Listeners Buttons
        //--------------------------------------------------------------------
        this.scoreButton.addActionListener(evt -> {
            Window.this.winner.showScore(); // Affichage du score
        });

        this.rulesButton.addActionListener(evt -> Rules.showRules(Window.this));

        this.miscRulesButton.addActionListener(evt -> Welcome.showHelp());


        pack();

    }

    /**
     * Ajouter le bouton score
     */
    private void addScoreButton() {
        this.paintArea.add(scoreButton);
        this.scoreButton.setText("Score");
        this.scoreButton.setBounds(0, 5, 80, 20);
    }

    /**
     * Ajouter le bouton aide
     */
    private void addRulesButton() {
        this.paintArea.add(rulesButton);
        this.rulesButton.setText("Règles");
        this.rulesButton.setBounds(0, 50, 80, 20);
    }

    /**
     * Ajouter le bouton Duo
     */
    private void addDuoButton() {
        this.paintArea.add(duoButton);
        this.duoButton.setText("DUO !");
        this.duoButton.setBounds(0, 100, 80, 20);
    }

    /**
     * Ajouter le bouton MiscRules
     */
    private void addMiscRulesButton() {
        this.paintArea.add(miscRulesButton);
        this.miscRulesButton.setText("Aide");
        this.miscRulesButton.setBounds(100, 50, 80, 20);
    }

    /**
     * Afficher le score
     */
    void showScore() {
        final ImageIcon icon = new ImageIcon("assets/imgMenu/score.png");
        JOptionPane.showMessageDialog(paintArea, menuScore, "LES SCORES", JOptionPane.YES_OPTION, icon);
    }

    /**
     *
     * Demande la couleur lorsqu'on pose un Joker ou un Super Joker
     *
     * @param c Card
     */
    public void showColorChoose(final AbstractCard c) {
        final specialCard cS = (specialCard) c;

        final ImageIcon icon = new ImageIcon("assets/imgMenu/choixCouleurs.jpg");

        //Tableau du nombre des joueurs possible
        final Object[] couleurs = {"Bleue", "Vert", "Rouge", "Jaune"};

        //Sortie du string choisi et le mettre en int puis initialiser le nombre de joueur au nombre choisi
        final int resultat = JOptionPane.showOptionDialog(paintArea, "Choissisez votre couleur", "Choisir couleur", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, icon, couleurs, null);

        jokerJoue = true;

        switch (resultat) {
            case 0:
                cS.setColor("Bleue");
                break;

            case 1:
                cS.setColor("Vert");
                break;

            case 2:
                cS.setColor("Rouge");
                break;

            case 3:
                cS.setColor("Jaune");
                break;

            default:
                showColorChoose(c);

        }

    }

    /**
     * Initialisation de la fenêtre, des variables de jeu...
     *
     * @param largeur
     * @param hauteur
     * @param firstGame
     * @throws IOException
     */
    public void init(final int largeur, final int hauteur, final boolean firstGame) throws IOException {

        initArrayListsImages();

        this.hasWon = false;
        this.colorJoker = "";
        this.hasPlayed = false;
        this.superjokerJoue = false;
        this.compteurPrend2 = 0;
        this.dessineNbPrend2 = 0;
        this.passeTonTour = false;
        this.prend2 = false;
        this.jouerOuPiocher = false;
        jokerJoue = false;
        this.dessinePasse = false;
        this.comptJokerJoue = 0;
        this.imDefausse = null;
        this.imPioche = null;

        if (firstGame) {
            //On cree une partie avec this.nbPlayer joueurs
            this.partie = new Partie(this.nbPlayer);
        } else {
            //On réinitiallise la partie précédente
            this.partie.reStart(this.nbPlayer);
        }

        try {
            //On definit l'image de la defausse et de la pioche
            this.imDefausse = ImageIO.read(new File(this.partie.getDiscard().getCard(0).getLink()));

            //Si la premiere carte est un superJoker ou un Joker donc on va donner une couleur par defaut
            if (this.partie.getDiscard().getCard(0).jokerOrSuperJoker()) {
                this.partie.getDiscard().getCard(0).setColor("Bleue");
                Rules.recupererIndiceCouleur(this);
                jokerJoue = true;
            }

            this.imPioche = ImageIO.read(new File("assets/imgCards/pioche.png"));

            //On ajoute tous les cartes de la mains du joueur a l'arraylist des images de la cardsPlayer du joueur
            for (int i = 0; i < this.partie.getPlayer().getCardsPlayer().size(); i++) {
                this.imJoueur.add(ImageIO.read(new File(this.partie.getPlayer().getCardsPlayer().getCard(i).getLink())));
            }

            //On ajoute tous les cartes de la cardsPlayer de l'IA a l'arraylist des images de la cardsPlayer de l'IA
            switch (this.nbPlayer) {
                case 2:

                    //images pour Ordi 1
                    for (int i = 0; i < this.partie.getIA(0).getCardsPlayer().size(); i++) {
                        this.imOrdi1.add(ImageIO.read(new File("assets/ordiCards/cartesNord.png")));
                    }

                    //Calculer la longueur des images du Ordi 1
                    int sizeImagesOrdi1 = (this.imOrdi1.size() * longueurImage / 2) + 150;

                    //Point de depart de X de la premiere image pour Ordi 1
                    this.startXPhotoOrdi1 = (largeur - sizeImagesOrdi1) / 2;

                    break;

                case 3:

                    //images pour Ordi 1
                    for (int i = 0; i < this.partie.getIA(0).getCardsPlayer().size(); i++) {
                        this.imOrdi1.add(ImageIO.read(new File("assets/ordiCards/cartesNord.png")));
                    }

                    //images pour Ordi 2
                    for (int i = 0; i < this.partie.getIA(1).getCardsPlayer().size(); i++) {
                        this.imOrdi2.add(ImageIO.read(new File("assets/ordiCards/cartesEst.png")));
                    }

                    sizeImagesOrdi1 = (this.imOrdi1.size() * longueurImage / 2) + 150;  //Calculer la longueur des images du Ordi 1
                    this.startXPhotoOrdi1 = (largeur - sizeImagesOrdi1) / 2;            //Point de depart de X de la premiere image pour Ordi 1

                    int sizeImagesOrdi2 = (this.imOrdi2.size() * longueurImage / 2) + longueurImage;    //Calculer la longueur des images du Ordi 2
                    this.startYPhotoOrdi2 = ((hauteur + 50) - sizeImagesOrdi2) / 2;                     //Point de depart de X de la premiere image pour Ordi 2

                    break;

                case 4:

                    //images pour Ordi 1
                    for (int i = 0; i < this.partie.getIA(0).getCardsPlayer().size(); i++) {
                        this.imOrdi1.add(ImageIO.read(new File("assets/ordiCards/cartesNord.png")));
                    }

                    //images pour Ordi 2
                    for (int i = 0; i < this.partie.getIA(1).getCardsPlayer().size(); i++) {
                        this.imOrdi2.add(ImageIO.read(new File("assets/ordiCards/cartesOuest.png")));
                    }

                    //images pour Ordi 3
                    for (int i = 0; i < this.partie.getIA(2).getCardsPlayer().size(); i++) {
                        this.imOrdi3.add(ImageIO.read(new File("assets/ordiCards/cartesEst.png")));
                    }

                    sizeImagesOrdi1 = (this.imOrdi1.size() * 50) + 150;         //Calculer la longueur des images du Ordi 1
                    this.startXPhotoOrdi1 = (largeur - sizeImagesOrdi1) / 2;    //Point de depart de X de la premiere image pour Ordi 1

                    sizeImagesOrdi2 = (this.imOrdi2.size() * longueurImage / 2) + longueurImage;    //Calculer la longueur des images du Ordi 2
                    this.startYPhotoOrdi2 = (hauteur - sizeImagesOrdi2) / 2;                        //Point de depart de X de la premiere image pour Ordi 2

                    final int sizeImagesOrdi3 = (this.imOrdi3.size() * longueurImage / 2) + longueurImage;    //Calculer la longueur des images du Ordi "
                    this.startYPhotoOrdi3 = (hauteur - sizeImagesOrdi3) / 2;                            //Point de depart de X de la premiere image pour Ordi 3

                    break;
            }
        } catch (final IOException ignored) {
        }

        //On initialise la carte selectionné a la moitié des cartes du joueur
        this.curseur = this.imJoueur.size() / 2;

        //Calculer la longueur des images du joueur
        final int sizeImagesJoueur = (this.imJoueur.size() * longueurImage / 2) + longueurImage;

        //Point de depart de X de la premiere image
        this.startXPhoto = (largeur - sizeImagesJoueur) / 2;

        this.partie.getPlayer().setName(namePlayer);

        //changer priorite de Ordi 3 (car il est à gauche) donc le premier qui joue après les joueur réel
        if (nbPlayer == 4) {
            this.partie.jeuAQuatre();
        }

        // Setup Buttons on Layout
        addRulesButton();
        addScoreButton();
        addDuoButton();
        addMiscRulesButton();

        // Setup final layout
        repaint();

    }

    /**
     * Creer les ArrayList qui vont contenir les images des différents joueurs
     */
    private void initArrayListsImages() {
        this.imJoueur = new ArrayList<>();
        this.imOrdi1 = new ArrayList<>();
        this.imOrdi2 = new ArrayList<>();
        this.imOrdi3 = new ArrayList<>();
    }

    /**
     * Initialise le tableau des scores
     */
    private void initTableScore() {
        String nom = this.namePlayer;

        // on intialise le tableau par raport au nombre de joueurs
        switch (nbPlayer) {
            case 2: // pour 2 joueurs
                this.score = new JTable(new DefaultTableModel(new Object[]{"<html><b>" + nom + "</b></html>", "<html><b>Ordi 1</b></html>"}, 0)); //initialisation du tableau du score
                DefaultTableModel model = (DefaultTableModel) this.score.getModel();

                model.addRow(new Object[]{"0", "0"});
                break;

            case 3: // pour 3 joueurs
                this.score = new JTable(new DefaultTableModel(new Object[]{nom, "Ordi 1", "Ordi 2"}, 0)); //initialisation du tableau du score
                model = (DefaultTableModel) this.score.getModel();

                model.addRow(new Object[]{"0", "0", "0"});
                break;

            case 4: //pour 4 joueurs
                this.score = new JTable(new DefaultTableModel(new Object[]{nom, "Ordi 1", "Ordi 2", "Ordi 3"}, 0)); //initialisation du tableau du score
                model = (DefaultTableModel) this.score.getModel();

                model.addRow(new Object[]{"0", "0", "0", "0"});
                break;
        }


        this.score.setDefaultEditor(Object.class, null);
        this.menuScore = new JScrollPane(this.score);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////// DESSINER lA FENETRE ////////////// DESSINER lA FENETRE ////////////// DESSINER lA FENETRE ////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void paint(Graphics g) {
        if (this.nbPlayer > 1 && this.nbPlayer < 5) {
            g = this.paintArea.getGraphics(); // on redessine dans le panel de dessin
            erase(this);

            paintComponent(g);
        }
    }

    /**
     * On redefine la méthode par défaut
     * @param g
     */
    private void paintComponent(final Graphics g) {
        int xPhoto = this.startXPhoto;

        // On dessine une premiere carte (la defausse) et on dessine la pioche
        g.drawImage(imDefausse, (getWidth() / 2), (getHeight() / 2 - (hauteurImage / 2)) - 25, longueurImage, hauteurImage, paintArea);
        g.drawImage(imPioche, ((getWidth() / 2) - longueurImage), (getHeight() / 2 - (hauteurImage / 2)) - 25, longueurImage, hauteurImage, paintArea);

        if (jokerJoue) {
            drawJokerColor(g);

            if (comptJokerJoue == 1) {
                jokerJoue = false;
                comptJokerJoue = 0;
            }
        }

        g.setColor(Color.BLACK);

        //Dessiner les images des cartes de l'ordinateur (IA) et les noms des joueurs
        dessinerCartesIA(g);

        //Dessiner les images des cartes du joueur (avec le curseur donc la photo selectionné)
        for (int i = 0; i < this.imJoueur.size(); i++) {
            xPhoto += 50;
            if (this.curseur == i) {
                g.drawImage(this.imJoueur.get(i), xPhoto, getHeight() - 150, longueurImage, hauteurImage, paintArea);
            } else {
                g.drawImage(this.imJoueur.get(i), xPhoto, getHeight() - 130, longueurImage, hauteurImage, paintArea);
            }
        }

        drawSpeciality(g);
        this.rulesButton.paintImmediately(0, 0, 80, 20);
        this.scoreButton.paintImmediately(0, 0, 80, 20);
        this.miscRulesButton.paintImmediately(0, 0, 80, 20);
    }

    /**
     * Dessine un rectangle autour de la carte pour indiquer sa couleur
     *
     * @param g
     */
    private void drawJokerColor(final Graphics g) {
        int x1 = getWidth() / 2;
        int y1 = (getHeight() / 2 - (hauteurImage / 2)) - 25;
        int x2 = x1 + longueurImage;
        int y2 = y1 + hauteurImage;

        switch (this.colorJoker) {
            case "Bleue":
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.setColor(new Color(0, 178, 220));
                g.drawLine(x1, y1, x2, y1);
                g.drawLine(x2, y1, x2, y2);
                g.drawLine(x2, y2, x1, y2);
                g.drawLine(x1, y2, x1, y1);
                break;

            case "Rouge":
                g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.setColor(new Color(239, 65, 56));
                g.drawLine(x1, y1, x2, y1);
                g.drawLine(x2, y1, x2, y2);
                g.drawLine(x2, y2, x1, y2);
                g.drawLine(x1, y2, x1, y1);
                break;

            case "Vert":
                g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.setColor(new Color(7, 171, 85));
                g.drawLine(x1, y1, x2, y1);
                g.drawLine(x2, y1, x2, y2);
                g.drawLine(x2, y2, x1, y2);
                g.drawLine(x1, y2, x1, y1);
                break;

            case "Jaune":
                g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(5));
                g.setColor(new Color(235, 233, 52));
                g.drawLine(x1, y1, x2, y1);
                g.drawLine(x2, y1, x2, y2);
                g.drawLine(x2, y2, x1, y2);
                g.drawLine(x1, y2, x1, y1);
                break;
        }
    }

    /**
     * Affiche carte IA + nom joueur
     * @param g
     */
    private void dessinerCartesIA(Graphics g) {
        int xPhotoOrdi1 = this.startXPhotoOrdi1;
        int yPhotoOrdi2 = this.startYPhotoOrdi2;
        int yPhotoOrdi3 = this.startYPhotoOrdi3;

        switch (this.nbPlayer) {
            case 2: // partie à 2
                for (Image image : this.imOrdi1) {
                    xPhotoOrdi1 += 50;
                    g.drawImage(image, xPhotoOrdi1, -60, longueurImage, hauteurImage, paintArea); //images des cartes de l'IA numéro 1
                }

                //Dessiner les noms des 2 joueurs
                if (this.partie.getJou().getName().equals(this.namePlayer)) {
                    drawPlayer(g);
                } else if ((this.partie.getJou().getName().equals("Ordi 1"))) {
                    drawComputer1(g);
                }

                break;

            case 3: //partie à 3
                for (Image image3 : this.imOrdi1) {
                    xPhotoOrdi1 += 50;
                    g.drawImage(image3, xPhotoOrdi1, -60, longueurImage, hauteurImage, paintArea);   //images des cartes de l'IA numéro 1
                }

                for (Image image : this.imOrdi2) {
                    yPhotoOrdi2 += 50;
                    g.drawImage(image, getWidth() - 120, yPhotoOrdi2, hauteurImage, longueurImage, paintArea);  //images des cartes de l'IA numéro 2
                }

                //Dessiner les noms des 3 joueurs
                switch (this.partie.getJou().getName()) {
                    case "Ordi 1":
                        drawComputer1(g);
                        break;
                    case "Ordi 2":
                        drawComputer2(g);
                        break;
                    default:
                        drawPlayer(g);
                        break;
                }

                break;

            case 4:
                for (Image image2 : this.imOrdi1) {
                    xPhotoOrdi1 += 50;
                    g.drawImage(image2, xPhotoOrdi1, -60, longueurImage, hauteurImage, paintArea);   //images des cartes de l'IA numéro 1
                }

                for (Image image1 : this.imOrdi2) {
                    yPhotoOrdi2 += 50;
                    g.drawImage(image1, getWidth() - 120, yPhotoOrdi2, hauteurImage, longueurImage, paintArea);  //images des cartes de l'IA numéro 2
                }

                for (Image image : this.imOrdi3) {
                    yPhotoOrdi3 += 50;
                    g.drawImage(image, -60, yPhotoOrdi3, hauteurImage, longueurImage, paintArea);  //images des cartes de l'IA numéro 3
                }

                //Dessiner les noms des 4 joueurs
                switch (this.partie.getJou().getName()) {
                    case "Ordi 1":
                        drawComputer1(g);
                        break;
                    case "Ordi 2":
                        drawComputer2(g);
                        break;
                    case "Ordi 3":
                        drawComputer3(g);
                        break;
                    default:
                        drawPlayer(g);
                        break;
                }

        }
    }

    /**
     * Affiche nom du joueur + Effet de la carte posée
     * @param g
     */
    private void drawSpeciality(Graphics g) {
        if (this.partie != null) {
            if (this.partie.getDiscard().getCard(this.partie.getDiscard().size() - 1) instanceof specialCard
                    && this.partie.getDiscard().size() > 1) {
                final specialCard cS = (specialCard) this.partie.getDiscard().getCard(this.partie.getDiscard().size() - 1);

                g.setFont(new Font("default", Font.ITALIC, 18));

                int height = (getHeight() / 2) - 130;
                int widthPrendEtJoker = (getWidth() / 2) - 20;
                int widthInversion = (getWidth() / 2) - 80;
                int widthPasse = (getWidth() / 2) - 60;

                if (this.dessinePasse) {
                    g.drawString("Passe Ton Tour", widthPasse, height);
                    this.dessinePasse = false;

                } else if (this.dessineNbPrend2 > 0) {
                    g.drawString("Pioche " + this.dessineNbPrend2, widthPrendEtJoker, height);
                    this.dessineNbPrend2 = 0;

                } else if (cS.isInvert()) {
                    g.drawString("Changement de sens", widthInversion, height);

                } else if (this.superjokerJoue) {
                    g.drawString("Prend 4", widthPrendEtJoker, height);
                    this.superjokerJoue = false;
                }
            }
        }
    }


    /**
     * Dessine le joueur
     * @param g
     */
    private void drawPlayer(Graphics g) {
        if (nbPlayer >= 2) {
            int nbLettreNom = this.partie.getPlayer().getName().length();

            g.setFont(new Font("default", Font.BOLD, 18));
            g.drawString("" + this.partie.getPlayer().getName(), (getWidth() / 2) - nbLettreNom * 2, getHeight() - hauteurImage - 10);    //nom du joueur EN GRAS

            g.setFont(new Font("default", Font.PLAIN, 16));
            g.drawString("" + this.partie.getIA(1).getName(), getWidth() / 2, hauteurImage - 30);    // nom de l'IA numéro 1

            if (nbPlayer >= 3) {
                g.drawString("" + this.partie.getIA(2).getName(), getWidth() - hauteurImage - 20, getHeight() / 2);    // nom de l'IA numéro 2
                if (nbPlayer >= 4) {
                    g.drawString("" + this.partie.getIA(3).getName(), hauteurImage - 30, getHeight() / 2);    // nom de l'IA numéro 3
                }
            }
        }
    }


    /**
     * Dessine l'IA 1
     * @param g
     */
    private void drawComputer1(Graphics g) {
        if (nbPlayer >= 2) {
            final int nbLettreNom = this.partie.getPlayer().getName().length();

            g.setFont(new Font("default", Font.PLAIN, 16));
            g.drawString("" + this.partie.getPlayer().getName(), (getWidth() / 2) - nbLettreNom * 2, getHeight() - hauteurImage);    //nom du joueur

            g.setFont(new Font("default", Font.BOLD, 18));
            g.drawString("" + this.partie.getIA(1).getName(), getWidth() / 2, hauteurImage - 30);    // nom de l'IA numéro 1 EN GRAS

            if (nbPlayer >= 3) {
                g.setFont(new Font("default", Font.PLAIN, 16));
                g.drawString("" + this.partie.getIA(2).getName(), getWidth() - hauteurImage - 20, getHeight() / 2);    // nom de l'IA numéro 2
                if (nbPlayer >= 4) {
                    g.drawString("" + this.partie.getIA(3).getName(), hauteurImage - 30, getHeight() / 2);    // nom de l'IA numéro 3
                }
            }
        }
    }

    /**
     * Dessine l'IA 2
     * @param g
     */
    private void drawComputer2(Graphics g) {
        if (nbPlayer >= 2) {
            final int nbLettreNom = this.partie.getPlayer().getName().length();

            g.setFont(new Font("default", Font.PLAIN, 16));
            g.drawString("" + this.partie.getPlayer().getName(), (getWidth() / 2) - nbLettreNom * 2, getHeight() - hauteurImage);    //nom du joueur
            g.drawString("" + this.partie.getIA(1).getName(), getWidth() / 2, hauteurImage - 30);    // nom de l'IA numéro 1

            if (nbPlayer >= 3) {
                g.setFont(new Font("default", Font.BOLD, 18));
                g.drawString("" + this.partie.getIA(2).getName(), getWidth() - hauteurImage - 20, getHeight() / 2);    // nom de l'IA numéro 2 EN GRAS
                if (nbPlayer >= 4) {
                    g.setFont(new Font("default", Font.PLAIN, 16));
                    g.drawString("" + this.partie.getIA(3).getName(), hauteurImage - 30, getHeight() / 2);    // nom de l'IA numéro 3
                }
            }
        }
    }

    /**
     * Dessine l'IA 3
     * @param g
     */
    private void drawComputer3(final Graphics g) {
        if (nbPlayer >= 2) {
            final int nbLettreNom = this.partie.getPlayer().getName().length();

            g.setFont(new Font("default", Font.PLAIN, 16));
            g.drawString("" + this.partie.getPlayer().getName(), (getWidth() / 2) - nbLettreNom * 2, getHeight() - hauteurImage);    //nom du joueur
            g.drawString("" + this.partie.getIA(1).getName(), getWidth() / 2, hauteurImage - 30);    // nom de l'IA numéro 1
            if (nbPlayer >= 3) {
                g.drawString("" + this.partie.getIA(2).getName(), getWidth() - hauteurImage - 20, getHeight() / 2);    // nom de l'IA numéro 2

                if (nbPlayer >= 4) {
                    g.setFont(new Font("default", Font.BOLD, 18));
                    g.drawString("" + this.partie.getIA(3).getName(), hauteurImage - 30, getHeight() / 2);    // nom de l'IA numéro 3 EN GRAS
                }
            }
        }
    }

    /**
     * Efface l'élément
     */
    static public void erase(Window fen) {
        final Graphics g = fen.paintArea.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, fen.getWidth(), fen.getHeight());
    }


    /**
     * Listeners
     * @param e Evenement
     */
    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mousePressed(final MouseEvent e) {

        if (jouerOuPiocher) {
            if (this.partie.getJou() instanceof IA || this.prend2) {
                try {
                    sleep(1700);
                } catch (final InterruptedException ex) {
                    Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        jouerOuPiocher = false;

        try {
            r = new Robot();
        } catch (final AWTException ex) {
            Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Si le joueur a jouer
        if (this.hasPlayed) {
            int nbIAaJouer = this.partie.getPlayers().nbIAaJouer();

            if (nbIAaJouer > 0) {
                IARules.traitementIA(this);
                jouerOuPiocher = true;

                nbIAaJouer = this.partie.getPlayers().nbIAaJouer();

                if (nbIAaJouer == 0) {
                    this.hasPlayed = false;
                    jouerOuPiocher = false;
                    toggleButtons(true);
                }
            } else {
                toggleButtons(true);
                this.hasPlayed = false;
                jouerOuPiocher = false;
            }
        } else {
            /*
             * Piocher si on appuie sur la pioche
             * Calcul des positions de la pioche
             */
            if (((e.getX() > getWidth() / 2 - longueurImage && e.getX() < getWidth() / 2) && (e.getY() > (getHeight() / 2 - (hauteurImage / 2)) && e.getY() < ((getHeight() / 2) + (hauteurImage / 2))))
                    && !this.passeTonTour && !this.prend2) {
                Rules.piocher(this);

                jouerOuPiocher = true;
                Rules.changePriority(this);
                this.hasPlayed = true;
            }

            final int xPremiereImage = this.startXPhoto + 50;
            boolean found = false;
            boolean deuxfois = true;

            int i = 0;
            final int largeurImage = 50;

            /*
             * Selectionner une carte et la jouer avec la souris
             * Parcourir tous les images
             */
            while (this.imJoueur.size() > i && !found && !hasPlayed) {

                //Calculer le xMin et xMax de l'image ou on a cliqué
                final int xMinImage = xPremiereImage + (i * largeurImage);
                int xMaxImage = xPremiereImage + largeurImage + (i * largeurImage);
                final int yMax = getHeight() + 30;
                final int yMin = getHeight() - 100;

                if (i == this.imJoueur.size() - 1) {
                    xMaxImage += 50;
                }

                //Comparer le X cliqué avec le xMin et xMax
                if (((e.getX() > xMinImage) && (e.getX() < xMaxImage)) && ((e.getY() > yMin) && (e.getY() < yMax))) {

                    //Si la carte selectionne avant correspond a la meme carte clique donc on va la jouer
                    if (this.curseur != i) {
                        //On met le curseur a l'indice de l'image
                        this.curseur = i;

                        //On arrete la boucle while dès qu'on a trouvé la bonne image
                        found = true;
                        deuxfois = false;
                    }

                }
                if (((e.getX() > xMinImage) && (e.getX() < xMaxImage)) && ((e.getY() > yMin - 15) && (e.getY() < yMax)) && deuxfois) {
                    //Si la carte selectionne avant correspond a la meme carte clique donc on va la jouer
                    if (this.curseur == i) {

                        final int tailleCartes = this.partie.getJou().getCardsPlayer().size();

                        if (this.partie.getJou() instanceof realPlayer) {

                            final AbstractCard cAJouer = this.partie.getPlayer().getCardsPlayer().getCard(this.curseur);

                            //Tester si on peut jouer la carte choisi
                            if (this.partie.getDiscard().canPlayCard(cAJouer)) {
                                if (this.passeTonTour) {
                                    if (cAJouer instanceof specialCard) {
                                        final specialCard cS = (specialCard) cAJouer;

                                        if (cS.isSkipRound()) {
                                            Rules.Joueurjouer(this,cAJouer);

                                            Rules.changePriority(this);

                                            Rules.doSpecialities(this, (specialCard) cAJouer);
                                            this.hasPlayed = true;
                                        }
                                    }

                                } else if (this.prend2) {
                                    if (cAJouer instanceof specialCard) {
                                        final specialCard cS = (specialCard) cAJouer;

                                        if (cS.isMoreTwo()) {
                                            Rules.Joueurjouer(this,cAJouer);

                                            Rules.changePriority(this);

                                            Rules.doSpecialities(this, (specialCard) cAJouer);
                                            this.hasPlayed = true;
                                        }
                                    }
                                } else {
                                    Rules.Joueurjouer(this,cAJouer);

                                    Rules.changePriority(this);

                                    if (cAJouer instanceof specialCard) {
                                        Rules.doSpecialities(this,(specialCard) cAJouer);
                                    }

                                    this.hasPlayed = true;
                                }

                                jouerOuPiocher = true;
                            }

                        }
                    }

                }

                i++;

            }

        }

        repaint();

        if (hasWon) {
            this.winner.handleWinner();
        }

        if (jouerOuPiocher) {

            toggleButtons(false);


            r.mousePress(InputEvent.BUTTON1_MASK);
            r.mouseRelease(InputEvent.BUTTON1_MASK);

        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
    }

    @Override
    public void mouseExited(final MouseEvent e) {
    }
    public static int getWindowWidth(Window fen){
        return fen.getWidth();

    }

    public static int getWindowHeight(Window fen){
        return fen.getHeight();

    }

    private void toggleButtons(boolean value) {
        this.rulesButton.setEnabled(value);
        this.scoreButton.setEnabled(value);
        this.miscRulesButton.setEnabled(value);
    }

}
