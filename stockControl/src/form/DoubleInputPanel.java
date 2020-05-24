package form;

import java.awt.Dimension;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import window.content;

public class DoubleInputPanel extends JPanel {
	
	private String selectedComboBoxData;
	
	private JComboBox comboBox;
	private JTextField textBox;
	
	public DoubleInputPanel(String[] comboBoxData, String selectedComboBoxData, String textFieldText,  Dimension dimension) {
		
		//Necesito: nombre del producto y cantidad del producto.
		this.selectedComboBoxData = selectedComboBoxData;
		this.setSize(dimension);
		
		createComboBox(comboBoxData);
		setComboBoxIndex(selectedComboBoxData);
		createTextField(textFieldText);
		
	}

	private void createComboBox(String[] comboBoxData) {
		comboBox = new JComboBox();
		
		for (int i=0; i<comboBoxData.length; i++) {
			comboBox.addItem(comboBoxData[i]);
		}
		
		this.add(comboBox);
	}
	
	private void setComboBoxIndex(String newSelectedData) {
		
		for (int i=0; i<comboBox.getItemCount(); i++) {
			if (comboBox.getItemAt(i).toString().equals(newSelectedData)) {
				comboBox.setSelectedIndex(i);
			}
		}
		
	}
	
	private void createTextField(String textFieldText) {
		
		textBox = new JTextField(textFieldText);
		this.add(textBox);
		
	}
	
	public String getItemSelected() {
		
		return comboBox.getSelectedItem().toString();
		
	}
	
	public String getIndexSelected() {
		
		return Integer.toString(comboBox.getSelectedIndex());
		
	}
}
