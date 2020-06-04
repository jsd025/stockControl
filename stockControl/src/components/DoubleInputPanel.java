package components;	//The "components" package stores component sets to reuse code.

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import form.adaptableFormWindow;

//class that includes a ComboBox on the left and a TextField on the right
public class DoubleInputPanel extends JPanel {
	
	//CLASS VARIABLES
	private adaptableFormWindow invoker;	//This variable is of type "adaptableFormWindow" because that is the only class that calls it. If there were more it would have to be of type "Object".
	private JComboBox<String> comboBox;
	private JTextField textField;
	
	//CONSTRUCTORS
	public DoubleInputPanel(String[] comboBoxData, String selectedComboBoxData, String textFieldText, Dimension dimension) {
		
		this.invoker = null;
		buildFrame(selectedComboBoxData, dimension);
		createComboBox(comboBoxData);
		setComboBoxSelectedItem(selectedComboBoxData);
		createTextField(textFieldText);
		
	}
	
	public DoubleInputPanel(adaptableFormWindow invoker, String[] comboBoxData, String selectedComboBoxData, String textFieldText, Dimension dimension) {
		
		this.invoker = invoker;
		buildFrame(selectedComboBoxData, dimension);
		createComboBox(comboBoxData);
		setComboBoxSelectedItem(selectedComboBoxData);
		createTextField(textFieldText);
		
	}
	
	public DoubleInputPanel() {
		
		this.setSize(380, 30);;
		this.setLayout(null);
		this.setBackground(setting.programSettings.getBackgroundContentColor());
		
		createComboBox(null);
		createTextField(null);
		
	}
	
	//METHODS
	private void buildFrame(String selectedComboBoxData, Dimension dimension) {
		//Necesito: nombre del producto y cantidad del producto.
		this.setSize(dimension);
		this.setLayout(null);
		this.setBackground(setting.programSettings.getBackgroundContentColor());

	}
	
	//Add ComboBox at left side.
	private void createComboBox(String[] comboBoxData) {
		comboBox = new JComboBox<String>();
		
		if (comboBoxData != null) {
			setComboBoxData(comboBoxData);
		}
		
		comboBox.setBounds(10, 5, 100, 20);
		comboBox.addItemListener (new ItemListener() {
		    public void itemStateChanged(ItemEvent event) { 
		    	if (event.getStateChange() == 1) {
		    		if (invoker != null) {
		    			invoker.newComboBox();
		    		}
		    	}
		    }
		});
		this.add(comboBox);
	}
	
	//Add TextField at right side.
	private void createTextField(String textFieldText) {
		
		if (textFieldText != null) {
			textField = new JTextField(textFieldText);
		} else {
			textField = new JTextField();
		}
		
		textField.setBounds(180, 0, 170, 30);
		this.add(textField);
		
	}
	
	//GETTERS & SETTERS
	public void setComboBoxSelectedItem(String newSelectedData) {
		
		for (int i=0; i<comboBox.getItemCount(); i++) {
			if (comboBox.getItemAt(i).toString().equals(newSelectedData)) {
				comboBox.setSelectedIndex(i);
			}
		}
		
	}
	
	public String getItemSelected() {
		String itemSelected;
		try {
			itemSelected = comboBox.getSelectedItem().toString();
		} catch(NullPointerException e) {
			itemSelected = null;
		}
		return itemSelected;
	}
	
	public String getIndexSelected() {		
		return Integer.toString(comboBox.getSelectedIndex());		
	}
	
	public void unselectAllComboBoxItems() {
		comboBox.setSelectedIndex(-1);
	}
	
	public void setComboBoxData(String[] comboBoxData) {
		for (int i=0; i<comboBoxData.length; i++) {
			comboBox.addItem(comboBoxData[i]);
		}
	}
	
	public void setTextFieldText(String textFieldText) {
		textField.setText(textFieldText);
	}
	
	public String getTextFieldText() {
		String textFieldText;
		try {
			textFieldText = textField.getText();
			if (textFieldText.equals("")) {
				textFieldText = null;
			}
		} catch(NullPointerException e) {
			textFieldText = null;
		}
		return textFieldText;
	}
}
