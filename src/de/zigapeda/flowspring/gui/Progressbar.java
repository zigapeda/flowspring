package de.zigapeda.flowspring.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import de.zigapeda.flowspring.Main;

public class Progressbar extends JPanel implements MouseListener{
	private static final long	serialVersionUID	= -123412974012047134L;
	private long milliseconds;
	private JProgressBar progress;
	private JLabel played;
	private JLabel left;
	
	public Progressbar() {
		super();
		this.setLayout(null);
		this.progress = new JProgressBar();
		this.progress.setBounds(0, 14, 500, 14);
		this.add(this.progress);
		this.progress.setMaximum(1000);
		this.progress.addMouseListener(this);
		this.played = new JLabel("played");
		this.left = new JLabel("left");
		this.played.setBounds(5, 0, 245, 14);
		this.left.setBounds(250, 0, 245, 14);
		this.played.setHorizontalAlignment(JLabel.LEFT);
		this.left.setHorizontalAlignment(JLabel.RIGHT);
		this.add(this.played);
		this.add(this.left);
	}
	
	public void setProgress(long total, int playedseconds) {
		this.progress.setValue((int)(playedseconds * this.progress.getMaximum() / total));
		this.played.setText(this.getTimeFromSecs(playedseconds));
		if(milliseconds != -1) {
			this.left.setText(this.getTimeFromSecs((int)(this.milliseconds / 1000)- playedseconds));
		}
		this.repaint();
	}

	public void mouseClicked(MouseEvent e) {
		double clk = e.getX();
		double wdt = this.getWidth();
		Main.getWindow().getPlayercontroller().seek(wdt / clk);
	}

	public void mousePressed(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	public void setDuration(long milliseconds) {
		this.milliseconds = milliseconds;
	}
	
	private String getTimeFromSecs(int secs) {
        int secondD = 0, second = 0, minuteD = 0, minute = 0;
        int seconds = (int) secs;
        int minutes = (int) Math.floor(seconds / 60);
        int hours = (int) Math.floor(minutes / 60);
        minutes = minutes - hours * 60;
        seconds = seconds - minutes * 60 - hours * 3600;
        if (seconds < 10)
        {
            secondD = 0;
            second = seconds;
        }
        else
        {
            secondD = ((int) seconds / 10);
            second = ((int) (seconds - (((int) seconds / 10)) * 10));
        }
        if (minutes < 10)
        {
            minuteD = 0;
            minute = minutes;
        }
        else
        {
            minuteD = ((int) minutes / 10);
            minute = ((int) (minutes - (((int) minutes / 10)) * 10));
        }
        String timestring = String.valueOf(minuteD) + String.valueOf(minute) + ":" + String.valueOf(secondD) + String.valueOf(second);
		return timestring;
	}
}
