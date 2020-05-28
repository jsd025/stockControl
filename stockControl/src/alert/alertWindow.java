package alert;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class alertWindow extends JFrame {
	
	private JPanel panel;
	private int windowHeight;
	
	public alertWindow(String content, String textButton) {
		panel = new JPanel(null);
		this.add(panel);
		
		buildLabel(content);
		buildButtonAccept(textButton);
		
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(400, windowHeight);
		this.setLocationRelativeTo(null);
		this.setVisible(true);

	}
//Añadir set bound o layout del panel.
	private void buildLabel(String content) {
		JLabel label = new JLabel("Error 404: " + content);
		panel.add(label);
	}
	
	private void buildButtonAccept(String textButton) {
		JButton button = new JButton(textButton);
		panel.add(button);
	}
}
