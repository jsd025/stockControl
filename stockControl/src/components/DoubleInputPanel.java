package components;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import form.adaptableFormWindow;
import mainWindow.content;

public class DoubleInputPanel extends JPanel {
	
	private adaptableFormWindow invoker;
	private String selectedComboBoxData;
	private JComboBox comboBox;
	private JTextField textField;
	
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
	
	private void buildFrame(String selectedComboBoxData, Dimension dimension) {
		//Necesito: nombre del producto y cantidad del producto.
		this.selectedComboBoxData = selectedComboBoxData;
		this.setSize(dimension);
		this.setLayout(null);

	}
	
	public DoubleInputPanel() {
		
		this.selectedComboBoxData = null;
		this.setSize(380, 30);;
		this.setLayout(null);
		
		createComboBox(null);
		createTextField(null);
		
	}
	
	private void createComboBox(String[] comboBoxData) {
		comboBox = new JComboBox();
		
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
	
	private void createTextField(String textFieldText) {
		
		if (textFieldText != null) {
			textField = new JTextField(textFieldText);
		} else {
			textField = new JTextField();
		}
		
		textField.setBounds(180, 0, 170, 30);
		this.add(textField);
		
	}
	
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
