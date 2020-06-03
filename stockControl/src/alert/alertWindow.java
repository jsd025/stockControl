package alert;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//class to display error messages or welcome message
public class alertWindow extends JFrame {
	
	private JPanel panel;
	private int windowHeight = 50;	//This variable does not currently make sense since it will always be equal to 130. However, it is for the future of the program.
	
	public alertWindow(String content, String textButton) {
		panel = new JPanel(null);
		this.add(panel);
		
		buildLabel(content);
		buildButtonAccept(textButton);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, windowHeight);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		panel.setBackground(setting.programSettings.getAlertBackgroundColor());
	}
	
	//Add text string for alert
	private void buildLabel(String content) {
		JLabel label = new JLabel(content);
		label.setBounds(10, 10, 560, 30);
		windowHeight += 40;
		panel.add(label);
	}
	
	//Add a button.
	private void buildButtonAccept(String textButton) {
		JButton button = new JButton(textButton);
		button.addActionListener(new ActionListener() {
			//The button only closes the alert
			@Override
			public void actionPerformed(ActionEvent event) {
				disposeWindow();
			}
		});
		button.setBackground(setting.programSettings.getButtonColor());
		button.setBounds(250, 50, 100, 30);
		windowHeight += 40;
		panel.add(button);
	}
	
	private void disposeWindow() {
		this.dispose();
	}
}
