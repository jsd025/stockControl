package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import alert.alertWindow;
import components.TextInputPanel;
import components.TextPasswordPanel;
import setting.userSettings;

//Class for a login form
public class loginWindow extends JFrame {
	
	//VARIABLES CLASS
	private TextInputPanel inputUser;
	private TextPasswordPanel inputPassword;
	private JPanel panel;
	
	//CONSTRUCTORS
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
	
	//METHODS
	private void buildComponents() {
		buildUsernameInput();
		buildPasswordInput();		
		buildButtonCancel();
		buildButtonAccept();
	}
	
	//Add username Input.
	private void buildUsernameInput() {
		inputUser = new TextInputPanel("Usuario: ", null);
		inputUser.setBounds(10, 10, 390, 30);
		panel.add(inputUser);
	}
	
	//Add password Input.
	private void buildPasswordInput() {
		inputPassword = new TextPasswordPanel("Contraseña: ");
		inputPassword.setBounds(10, 50, 390, 30);
		panel.add(inputPassword);
	}
	
	private void buildButtonCancel() {

		JButton buttonCancel = new JButton("Cancelar");
		buttonCancel.setBounds(10, 90, 170, 30);
		//Button Cancel close the program.
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
		//Button Accept check if user and password are Ok.
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
					new alertWindow("Error 320: No se ha podido acceder a verificar el usuario y contraseña", "Aceptar");
					//e1.printStackTrace();
				}
			}
		});
		panel.add(buttonAccept);
	}
	
	private void incorrectUserPassword() {
		printAlert("Error 505: Usuario o contraseña incorrectos");
	}
	
	private void correctUserPassword(String username, String permissions) {
		
		String alertBody = "Bienvenid@ de nuevo " + username;
		printAlert(alertBody);
		//This string will contain 0 and 1 depending on whether you have the permission or not.
		userSettings.setUserPermissions(permissions);
		userSettings.setUsername(username);
		
		disposeFrame();
	}
	
	private void internalError() {
		printAlert("Error 321: Error interno");
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
