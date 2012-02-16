package de.zigapeda.flowspring.gui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import de.zigapeda.flowspring.Main;
import de.zigapeda.flowspring.data.DataNode;
import de.zigapeda.flowspring.interfaces.TreeRow;

public class Controlllayout extends JPanel  implements ItemListener {
	private static final long	serialVersionUID	= 1194771405588627883L;
	private Object lastcomponent;
	
	public Controlllayout() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        JComboBox<TreeRow.Type> box = new JComboBox<>(TreeRow.classes);
        box.addItemListener(this);
        this.add(box);
	}

	@SuppressWarnings("unchecked")
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange() == ItemEvent.SELECTED) {
			if(this.lastcomponent == e.getSource()) {
				JComboBox<TreeRow.Type> box =((JComboBox<TreeRow.Type>)e.getSource());
				LinkedList<Component> del = null;
				for(int i = 0; i < this.getComponentCount(); i++) {
					if(del != null) {
						del.add(this.getComponent(i));
					}
					if(this.getComponent(i) == box) {
						del = new LinkedList<>();
					}
				}
				if(del != null) {
					if(del.isEmpty() == false) {
						for(Component c: del) {
							this.remove(c);
						}
					}
				}
				if(((TreeRow.Type)e.getItem()).getType() != 1) {
					if(this.getComponentCount() < 4) {
						JComboBox<TreeRow.Type> newbox = new JComboBox<>();
						newbox.addItemListener(this);
						for(int i = 0; i < box.getItemCount(); i++) {
							if(e.getItem() != box.getItemAt(i)) {
								newbox.addItem(box.getItemAt(i));
							}
						}
						this.add(newbox);
					}
				}
				DataNode.refreshMedialib(((TreeRow.Type)((JComboBox<TreeRow.Type>)this.getComponent(0)).getSelectedItem()).getType());
				Main.getWindow().refreshMedialib();
				this.repaint();
			}
		} else if(e.getStateChange() == ItemEvent.DESELECTED) {
			this.lastcomponent = e.getSource();
		}
	}
	
	@SuppressWarnings("unchecked")
	public Integer getNextType(Integer type) {
		for(int i = 0; i < this.getComponentCount(); i++) {
			if(((TreeRow.Type)((JComboBox<TreeRow.Type>)this.getComponent(i)).getSelectedItem()).getType() == type) {
				if(i < this.getComponentCount() - 1) {
					return ((TreeRow.Type)((JComboBox<TreeRow.Type>)this.getComponent(i + 1)).getSelectedItem()).getType();
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<Integer> getTypeOrder() {
		LinkedList<Integer> list = new LinkedList<>();
		for(int i = 0; i < this.getComponentCount(); i++) {
			list.add(((TreeRow.Type)((JComboBox<TreeRow.Type>)this.getComponent(i)).getSelectedItem()).getType());
		}
		return list;
	}
}
