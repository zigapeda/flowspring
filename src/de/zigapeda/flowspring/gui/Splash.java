package de.zigapeda.flowspring.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

import de.zigapeda.flowspring.Main;

public class Splash extends JWindow {
	private static final long serialVersionUID = 8352843980562896006L;

	public Splash() {
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    int w = 302;
	    int h = 202;
	    int x = (dim.width-w)/2;
	    int y = (dim.height-h)/2; 
		this.setBounds(x, y, w, h);
		this.setVisible(true);
	}
	
    public void paint(Graphics g) {
        super.paint(g);
        BufferedImage image;
        try {
            image = ImageIO.read(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/splashscreen.png"));
            g.drawImage(image, 1, 1, null);
            g.drawRect(0, 0, 301, 201);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
