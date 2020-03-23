package duo;

import duo.UI.Accueil;

import javax.swing.*;
import java.io.IOException;

/**
 * @author DELGADO
 */
class Duo {

    /**
     * @param args the command line arguments
     */
    public static void main(final String[] args) throws IOException {
        JFrame frame = new Accueil();
        frame.setTitle("DUO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocation(400, 10);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
    }

}
