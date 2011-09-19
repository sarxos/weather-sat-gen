package com.sarxos.sat24.swing;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sarxos.sat24.Sat24Image;


public class Sat24Animator extends JFrame implements WindowListener {

	private static final long serialVersionUID = 1L;
	private List<Sat24Image> images = new LinkedList<Sat24Image>();
	private Sat24DrawingPanel panel = null;
	private boolean running = false;

	public Sat24Animator() {
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Weather Simula");
		setLayout(new FlowLayout());
		
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		
		JMenu menu = new JMenu("Action");
		bar.add(menu);
		
		JMenuItem run = new JMenuItem(
				new AbstractAction("Run") {
					@Override
					public void actionPerformed(ActionEvent e) {
						start();
					}
				}
		);
		menu.add(run);
		
		panel = new Sat24DrawingPanel();
		add(panel);
		
		pack();
		setVisible(true);
	}

	public void start() {
		
		running = true;
		
		Calendar calendar = new GregorianCalendar();
		calendar.add(Calendar.HOUR_OF_DAY, -24);
		int minute = calendar.get(Calendar.MINUTE);
		calendar.set(Calendar.MINUTE, minute - minute % 15);
		
		Calendar now = Calendar.getInstance();

		while (calendar.before(now) && running) {
			Date d = calendar.getTime();
			Sat24Image img = new Sat24Image(d, "eu");
			images.add(img);
			System.out.println(img.getURL());
			calendar.add(Calendar.MINUTE, 15);
		}
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (running) {
					panel.setImage(images.get(i));
					i++;
					if (i >= images.size()) {
						i = 0;
					}
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		Thread runner = new Thread(r, "Animator");
		runner.setDaemon(true);
		runner.start();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						try {
							UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
		);
		SwingUtilities.invokeLater(
				new Runnable() {
					public void run() {
						new Sat24Animator();
					}
				}
		);
	}

	public void windowClosed(WindowEvent e) {
		running = false;
		if (images != null) {
			for (int i = 0; i < images.size(); i++) {
				images.get(i).flush();
			}
		}
	}
	
	public void windowClosing(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
}
