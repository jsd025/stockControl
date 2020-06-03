package login;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import alert.alertWindow;
import components.TextInputPanel;
import components.TextPasswordPanel;
import setting.userSettings;

public class loginWindow extends JFrame {
	
	private TextInputPanel inputUser;
	private TextPasswordPanel inputPassword;
	JPanel panel;
	
	public loginWindow() {
		
		panel = new JPanel(null);
		this.add(panel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(390, 180);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		panel.setBackground(setting.programSettings.getBackgroundContentColor());
		
		buildComponents();
	}

	private void buildComponents() {
		
		buildUsernameInput();
		buildPasswordInput();		
		buildButtonCancel();
		buildButtonAccept();
	}
	
	private void buildUsernameInput() {
		//JTextInputPanel
		inputUser = new TextInputPanel("Usuario: ", null);
		inputUser.setBounds(10, 10, 390, 30);
		panel.add(inputUser);
	}
	
	private void buildPasswordInput() {
		//JPasswordField
		inputPassword = new TextPasswordPanel("Contraseña: ");
		inputPassword.setBounds(10, 50, 390, 30);
		panel.add(inputPassword);
	}
	
	private void buildButtonCancel() {

		JButton buttonCancel = new JButton("Cancelar");
		buttonCancel.setBounds(10, 90, 170, 30);
		buttonCancel.setBackground(setting.programSettings.getButtonCancelColor());
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(buttonCancel);
	}
	
	private void buildButtonAccept() {
		
		JButton buttonAccept = new JButton("Aceptar");
		buttonAccept.setBounds(190, 90, 170, 30);
		buttonAccept.setBackground(setting.programSettings.getButtonColor());
		buttonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String[] columns = new String[] {"u.username", "u.permissions"};
				String[] tables = new String[] {"users u"};
				String[][] inputWhere = new String[][] {{"u.username", "'" + inputUser.getTextFieldText() + "'", "AND"}, {"u.password", "'" + inputPassword.getPassword() + "'"}};
				ResultSet dbUserPassword = dao.dbio.select(columns, tables, inputWhere);
				
				int countUserPassword = 0;
				
				try {
					while (dbUserPassword.next()) {	
						countUserPassword++;
					}
					dbUserPassword.first();
					
					switch (countUserPassword) {
						case 0: incorrectUserPassword(); break;
						case 1: correctUserPassword(dbUserPassword.getString(1), dbUserPassword.getString(2)); break;
						default: internalError(); break;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel.add(buttonAccept);
	}
	
	private void incorrectUserPassword() {
		//Imprimir alerta (usuario o contraseña incorrectos).
		printAlert("Usuario o contraseña incorrectos");
	}
	
	private void correctUserPassword(String username, String permissions) {
		//Esto solo se ejecuta si hay algún usuario que coincida con el usuario y contraseña introducidos.
		
		//Emitir alerta: Bienvenido "username"
		String alertBody = "Bienvenid@ de nuevo " + username;
		printAlert(alertBody);
		//Este string contendrá 0 y 1 en función de si tiene el permiso o no.
		userSettings.setUserPermissions(permissions);
		userSettings.setUsername(username);
		
		disposeFrame();
	}
	
	private void internalError() {
		//Imprimir alerta (multiples resultados que coinciden con el usuario y la contraseña).
		printAlert("Internal error");
	}
	
	private void printAlert(String alertBody) {
		Runnable runnable = new Runnable(){
	        public void run(){
	        	try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        	new alertWindow(alertBody, "Aceptar");
	        }
	    };
	    Thread thread = new Thread(runnable);
	    thread.start();
	}
	
	public void refresh() {
		panel.revalidate();
		panel.repaint();
	}
	
	public void disposeFrame() {
		this.dispose();
	}

}
