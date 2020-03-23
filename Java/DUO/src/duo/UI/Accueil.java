package duo.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Accueil extends JFrame {
	
	public static JFrame frame;
	
	public Accueil() throws IOException {
		super();
		frame = this;
		
		setLayout(new BorderLayout());
		
		final JPanel panel = new JPanel();
        BufferedImage image = ImageIO.read(new File("assets/imgMenu/commencer.png"));
        JLabel label = new JLabel(new ImageIcon(image));
        JButton button = new JButton("Continuer");
        
        panel.setLayout(new BorderLayout());
        panel.add(label);
        panel.add(button, BorderLayout.SOUTH);
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	try {
            		panel.removeAll();
            		panel.setBorder(BorderFactory.createLineBorder(Color.black));
            		GestionJoueur gestionjoueurs = new GestionJoueur();
            		panel.add(gestionjoueurs);
					System.out.println("repaint");
					panel.revalidate();
					panel.repaint();
					pack();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
        });
        
        add(panel);
	}
}
