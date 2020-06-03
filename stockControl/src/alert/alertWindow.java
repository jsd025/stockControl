package alert;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class alertWindow extends JFrame {
	
	private JPanel panel;
	private int windowHeight = 50;
	
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
		//Añadir set bound o layout del panel.
	private void buildLabel(String content) {
		JLabel label = new JLabel(content);
		label.setBounds(10, 10, 560, 30);
		windowHeight += 40;
		panel.add(label);
	}
	
	private void buildButtonAccept(String textButton) {
		JButton button = new JButton(textButton);
		button.addActionListener(new ActionListener() {
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
