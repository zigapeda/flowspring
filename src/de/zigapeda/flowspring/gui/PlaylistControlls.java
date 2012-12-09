package de.zigapeda.flowspring.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.data.PlaylistTrack;

public class PlaylistControlls extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7928305413964579181L;
	public static final int REPEAT_NO = 0;
	public static final int REPEAT_ALL = 1;
	public static final int REPEAT_ONE = 2;
	
	private Playlist playlist;
	private int repeat = 0;
	
	public PlaylistControlls(Playlist playlist) {
		this.playlist = playlist;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		Insets i = new Insets(0, -4, 0, -4);
		JButton shuffle = new JButton();
		shuffle.setActionCommand("s");
		shuffle.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/shuffle.png")));
		shuffle.addActionListener(this);
		shuffle.setMargin(i);
		this.add(shuffle);
		JButton repeat = new JButton();
		repeat.setActionCommand("r");
		repeat.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/repeatno.png")));
		repeat.addActionListener(this);
		repeat.setMargin(i);
		this.add(repeat);
		JButton addStream = new JButton();
		addStream.setActionCommand("stream");
		addStream.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/stream.png")));
		addStream.addActionListener(this);
		addStream.setMargin(i);
		this.add(addStream);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "s": //shuffle
				this.shuffle();
				break;
			case "r": //repeat
				this.repeat((JButton)e.getSource());
				break;
			case "stream":
				this.addStream();
				break;
		}
	}
	
	private void shuffle() {
		this.playlist.shuffle();
	}
	
	private void repeat(JButton button) {
		switch(this.repeat) {
			case REPEAT_NO:
				this.repeat = REPEAT_ALL;
				button.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/repeatall.png")));
				break;
			case REPEAT_ALL:
				this.repeat = REPEAT_ONE;
				button.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/repeatone.png")));
				break;
			case REPEAT_ONE:
				this.repeat = REPEAT_NO;
				button.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/repeatno.png")));
				break;
		}
	}

	public int getRepeat() {
		return this.repeat;
	}
	
	private void addStream() {
		this.playlist.addTrack(new PlaylistTrack("Technobase.fm", 0, "http://listen.technobase.fm/tunein-mp3-pls"));
	}
}
