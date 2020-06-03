package components;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mainWindow.contentUserPermissionManager;

public class LabelComboBoxPanel extends JPanel {
	
	private JLabel label;
	private JComboBox comboBox;
	private Object invoker;
	
	public LabelComboBoxPanel(Object invoker, String labelText, String[] comboBoxList, String comboBoxSelected) {
		
		this.invoker = invoker;
		this.setLayout(null);
		this.setBackground(setting.programSettings.getBackgroundContentColor());
		buildComponents(labelText, comboBoxList, comboBoxSelected);
		
	}

	private void buildComponents(String labelText, String[] comboBoxList, String comboBoxSelected) {
		
		buildLabel(labelText);
		buildComboBox(comboBoxList, comboBoxSelected);
		
	}

	private void buildLabel(String labelText) {
		label = new JLabel(labelText);
		label.setBounds(0, 0, 170, 30);
		this.add(label);
	}
	
	private void buildComboBox(String[] comboBoxList, String comboBoxSelected) {
		comboBox = new JComboBox();
		
		for (int i=0; i<comboBoxList.length; i++) {
			comboBox.addItem(comboBoxList[i]);
			if (comboBoxSelected != null) {
				if (comboBoxList[i].equals(comboBoxSelected)) {
					comboBox.setSelectedItem(comboBoxList[i]);
				}
			} else {
				changeComboBoxSelectedItem(null);
			}
		}
		
		comboBox.addItemListener (new ItemListener() {
		    public void itemStateChanged(ItemEvent event) { 
		    	if (event.getStateChange() == 1) {
		    		try {
		    			((contentUserPermissionManager) invoker).refreshPermissions(getComboBoxSelectedItem());
		    		} catch(ClassCastException e) {
		    			//Esto se ejecuta cuando la clase no es de tipo contentUserPermissionsManager.
		    			//Lo ignoro porque no hay nada que quiera hacer en este caso.
		    		}
		    		comboBox.hidePopup();
		      	}
		    }
		});
		
		comboBox.setBounds(180, 0, 170, 30);
		this.add(comboBox);
	}
	
	public void changeLabelText(String labelText) {
		label.setText(labelText);
	}
	
	public String getLabelText() {
		return label.getText();
	}
	
	public void addComboBoxItem(String item) {
		comboBox.addItem(item);
	}
	
	public void changeComboBoxSelectedItem(String item) {
		if (item == null) {
			comboBox.setSelectedIndex(-1);
		} else {
			for (int i=0; i<comboBox.getItemCount(); i++) {
				if (comboBox.getItemAt(i).equals(item)) {
					
					//invoker.refreshPermissions(item);
					comboBox.setSelectedIndex(i);
				}
			}
		}
	}
	
	public String getComboBoxSelectedItem() {
		return comboBox.getSelectedItem().toString();
	}
	
	
}
