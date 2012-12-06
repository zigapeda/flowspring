package de.zigapeda.flowspring.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
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
	private static final int REPEAT_NO = 0;
	private static final int REPEAT_ALL = 1;
	private static final int REPEAT_ONE = 2;
	
	private DefaultListModel<PlaylistTrack> playlistmodel;
	private int repeat = 0;
	
	public PlaylistControlls(DefaultListModel<PlaylistTrack> playlistmodel) {
		this.playlistmodel = playlistmodel;
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		JButton shuffle = new JButton();
		shuffle.setActionCommand("s");
		shuffle.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/shuffle.png")));
		shuffle.addActionListener(this);
		this.add(shuffle);
		JButton repeat = new JButton();
		repeat.setActionCommand("r");
		repeat.setIcon(new ImageIcon(Main.class.getClass().getResource("/de/zigapeda/flowspring/res/repeatno.png")));
		repeat.addActionListener(this);
		this.add(repeat);
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
		}
	}
	
	private void shuffle() {
		ArrayList<PlaylistTrack> list = new ArrayList<>();
		for(int i = 0; i < this.playlistmodel.getSize(); i++) {
			list.add(this.playlistmodel.get(i));
		}
		Collections.shuffle(list);
		this.playlistmodel.clear();
		for(int i = 0; i < list.size(); i++) {
			this.playlistmodel.addElement(list.get(i));
		}
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
}
