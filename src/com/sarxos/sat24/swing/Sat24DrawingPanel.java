package com.sarxos.sat24.swing;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.sarxos.sat24.Sat24Image;


/**
 * Panel used to render Sat24 images.
 * 
 * @author Bartosz Firyn
 */
public class Sat24DrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private Sat24Image image = null;

	
	public Sat24DrawingPanel() {
		Dimension dim = new Dimension(640, 480);
		setSize(dim);
		setPreferredSize(dim);
	}
	
	/**
	 * Set new Sat24 image to render.
	 * 
	 * @param image
	 */
	public void setImage(Sat24Image image) {
		this.image = image;
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (image != null) {
			g2.drawImage(image.getImage(), null, 0, 0);
		} else {
			super.paint(g2);
		}
	}
}
