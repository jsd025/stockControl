package form;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

public class TextViewInputPanel extends JPanel {
	
	String[] panelData;
	ArrayList<Object> elements = new ArrayList<Object>();
	
	public TextViewInputPanel(String[] panelData) {
		this.panelData = panelData;
		this.setLayout(null);
		
		buildComponents();
	}

	private void buildComponents() {
		
		JLabel label = new JLabel(panelData[0]);
		label.setBounds(10, 0, 170, 30);
		elements.add(label);
		this.add(label);
		
		JTextField textField = new JTextField();
		//textField.setText(panelData[]);
		textField.setBounds(180, 0, 170, 30);
		elements.add(textField);
		this.add(textField);
	}
	
	public void setTextFieldValue(String newTextFieldValue) {
		
		for (int i=0; i<elements.size(); i++) {
			try {
				((JTextComponent) elements.get(i)).setText(newTextFieldValue);
			} catch (ClassCastException e) {
				//No error. Ignore this.
			}
		}
	}
	
	public String getTextFieldValue() {
		
		for (int i=0; i<elements.size(); i++) {
			try {
				JTextComponent textComponent = ((JTextComponent) elements.get(i));
				return textComponent.getText();
			} catch (ClassCastException e) {
				//No error. Ignore this.
			}
		}
		
		return null;
	}
	
}
