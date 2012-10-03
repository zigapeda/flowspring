package de.zigapeda.flowspring.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;

import de.zigapeda.flowspring.data.PlaylistTrack;

public class PlaylistControlls extends JPanel implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7928305413964579181L;
	private DefaultListModel<PlaylistTrack> playlistmodel;
	
	public PlaylistControlls(DefaultListModel<PlaylistTrack> playlistmodel) {
		this.playlistmodel = playlistmodel;
		JButton button = new JButton("Shuffle");
		button.addActionListener(this);
		this.add(button);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
}
