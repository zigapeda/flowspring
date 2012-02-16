package de.zigapeda.flowspring.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JTextField;

public class Searchbar extends JTextField {
	private static final long serialVersionUID = -6346234334374694735L;
	private JButton deletebutton;
	
	public Searchbar () {
		super();
		this.deletebutton = new JButton("✕");
		this.setLayout(null);
		this.deletebutton.setBounds(161, 1, 23, 23);
		this.deletebutton.setBorder(null);
		this.add(this.deletebutton);
		this.deletebutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Searchbar.this.setText("");
			}
		});
		this.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
	}
}
