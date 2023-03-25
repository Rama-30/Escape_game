package tools;

import java.awt.Color;

import javax.swing.*;

public class Design extends JFrame {
	
    private static final long serialVersionUID = -7262919384067264046L;

	public void mettreEnGras(String text) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 200);
        setLocationRelativeTo(null);

        // Créer un JLabel et ajouter du texte en gras
        JLabel label = new JLabel("<html><b>"+text+"</b></html>");
        add(label);

        setVisible(true);
    }
    
    public void changerCouleurTexte(String text) {
    	// Créer un JLabel et changer la couleur du texte en rouge
        JLabel label = new JLabel(text);
        label.setForeground(Color.CYAN);
        add(label);

        setVisible(true);
    }

    
}
