package components;	//The "components" package stores component sets to reuse code.

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import mainWindow.contentUserPermissionManager;

//class that includes a Label on the left and a ComboBox on the right
public class LabelComboBoxPanel extends JPanel {
	
	//CLASS VARIABLES
	private JLabel label;
	private JComboBox<String> comboBox;
	private Object invoker;
	
	//CONSTRUCTORS
	public LabelComboBoxPanel(Object invoker, String labelText, String[] comboBoxList, String comboBoxSelected) {
		
		this.invoker = invoker;
		this.setLayout(null);
		this.setBackground(setting.programSettings.getBackgroundContentColor());
		buildComponents(labelText, comboBoxList, comboBoxSelected);
		
	}
	
	//METHODS
	private void buildComponents(String labelText, String[] comboBoxList, String comboBoxSelected) {
		
		buildLabel(labelText);
		buildComboBox(comboBoxList, comboBoxSelected);
		
	}
	
	//Add a Label at left side.
	private void buildLabel(String labelText) {
		label = new JLabel(labelText);
		label.setBounds(0, 0, 170, 30);
		this.add(label);
	}
	
	//Add a ComboBox at right side.
	private void buildComboBox(String[] comboBoxList, String comboBoxSelected) {
		comboBox = new JComboBox<String>();
		
		for (int i=0; i<comboBoxList.length; i++) {
			comboBox.addItem(comboBoxList[i]);
			if (comboBoxSelected != null) {
				if (comboBoxList[i].equals(comboBoxSelected)) {
					comboBox.setSelectedItem(comboBoxList[i]);
				}
			} else {
				setComboBoxSelectedItem(null);
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
	
	public void addComboBoxItem(String item) {
		comboBox.addItem(item);
	}
	
	//GETTERS & SETTERS
	public void setLabelText(String labelText) {
		label.setText(labelText);
	}
	
	public String getLabelText() {
		return label.getText();
	}
	
	public void setComboBoxSelectedItem(String item) {
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
