package form;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import window.content;

public class DoubleInputPanel extends JPanel {
	
	private String selectedComboBoxData;
	
	private JComboBox comboBox;
	private JTextField textField;
	
	public DoubleInputPanel(String[] comboBoxData, String selectedComboBoxData, String textFieldText,  Dimension dimension) {
		
		//Necesito: nombre del producto y cantidad del producto.
		this.selectedComboBoxData = selectedComboBoxData;
		this.setSize(dimension);
		this.setLayout(null);
		
		createComboBox(comboBoxData);
		setComboBoxSelectedItem(selectedComboBoxData);
		createTextField(textFieldText);
		
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
		return comboBox.getSelectedItem().toString();
	}
	
	public String getIndexSelected() {		
		return Integer.toString(comboBox.getSelectedIndex());		
	}
	
	public void setComboBoxData(String[] comboBoxData) {
		for (int i=0; i<comboBoxData.length; i++) {
			comboBox.addItem(comboBoxData[i]);
		}
	}
	
	public void setTextFieldText(String textFieldText) {
		textField.setText(textFieldText);
	}
}
