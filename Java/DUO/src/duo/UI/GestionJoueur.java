package duo.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GestionJoueur extends JPanel implements ActionListener {
	private JPanel zone, zoneNom, zoneNB;
	private static int nbJoueurs;
	private static String nomJoueur;
	public JFrame frame;
	private JRadioButton choix2,choix3,choix4;
	private ButtonGroup bg;
	
	public GestionJoueur() throws IOException {
		super();
		gestion();
	}
    
	public void gestion() throws IOException {
		JButton button = new JButton("Démarrer !");
		setLayout(new BorderLayout());
		setSize(200, 200);
		zone = new JPanel(new GridLayout(2, 0));
		zoneNom = new JPanel(new FlowLayout(FlowLayout.LEFT));
		zoneNB = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		BufferedImage image1 = ImageIO.read(new File("assets/imgMenu/name.png"));
        JLabel labelimage1 = new JLabel(new ImageIcon(image1));
		JLabel label1 = new JLabel("Entrer son prénom : ");
		final JTextField userName = new JTextField(20);
		
		zoneNom.add(labelimage1);
		zoneNom.add(label1);
		zoneNom.add(userName);
		
		BufferedImage image2 = ImageIO.read(new File("assets/imgMenu/joueurs.jpg"));
        JLabel labelimage2 = new JLabel(new ImageIcon(image2));
		JLabel label2 = new JLabel("Nombre de joueurs : ");
		
		choix2 = new JRadioButton("2");
		choix3 = new JRadioButton("3");
		choix4 = new JRadioButton("4");
		bg = new ButtonGroup();
		choix2.setSelected(true);
		
		choix2.addActionListener(this);
		choix3.addActionListener(this);
		choix4.addActionListener(this);
		
		bg.add(choix2);
		bg.add(choix3);
		bg.add(choix4);
		
		zoneNB.add(labelimage2);
		zoneNB.add(label2);
		zoneNB.add(choix2);
		zoneNB.add(choix3);
		zoneNB.add(choix4);
		
		zone.add(zoneNom);
		zone.add(zoneNB);
		
		add(zone);
		add(button, BorderLayout.SOUTH);
        
    	button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		if (!userName.getText().toString().isEmpty()){
	            		setNomJoueur(userName.getText().toString());
	            		setNbJoueurs(nbJoueurs);
	            		Accueil.frame.setVisible(false);
	            		new Window(1260, 680, true);
            		}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
	}
	
    public static int getNbJoueurs() {
		return nbJoueurs;
	}

	public void setNbJoueurs(int nbJoueurs) {
		switch (nbJoueurs){
	        case 0: // [0] => 2
	            GestionJoueur.nbJoueurs = 2;
	        break;
	
	        case 1: // [1] => 3
	            GestionJoueur.nbJoueurs = 3;
	        break;
	
	        case 2: // [2] => 4
	        	GestionJoueur.nbJoueurs = 4;
	        break;
		}
	}

	public static String getNomJoueur() {
		return nomJoueur;
	}

	public void setNomJoueur(String nomJoueur) throws IOException {
		
		if (nomJoueur.isEmpty()) {
			System.out.println(getNomJoueur());
            gestion();
        } else {
        	System.out.println(nomJoueur);
        	GestionJoueur.nomJoueur = nomJoueur;
        }
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
        if(event.getSource() == choix2){
            setNbJoueurs(0);
        } else if(event.getSource() == choix3){
        	setNbJoueurs(1);
        } else if(event.getSource() == choix4){
        	setNbJoueurs(2);
        }
        System.out.println("Nombre de joueur : " + getNbJoueurs());
	}
	
}
