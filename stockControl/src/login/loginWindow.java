package login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import alert.alertWindow;
import setting.userSettings;

public class loginWindow extends JFrame {
	
	private TextInputPanel inputUser;
	private TextInputPanel inputPassword;
	JPanel panel;
	
	public loginWindow() {
		
		panel = new JPanel(null);
		this.add(panel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(380, 130);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		buildComponents();
	}

	private void buildComponents() {
		
		//Añadir dos form.textInputPanel.
		
		inputUser = new TextInputPanel("Usuario: ", null);
		inputUser.setBounds(0, 10, 380, 30);
		
		//JPasswordField
		inputPassword = new TextInputPanel("Contraseña: ", null);
		inputPassword.setBounds(0, 50, 380, 30);
		
		buildButtonCancel();
		buildButtonAccept();
	}
	
	private void buildButtonCancel() {

		JButton buttonCancel = new JButton("Cancelar");
		buttonCancel.setBounds(10, 90, 100, 30);
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		panel.add(buttonCancel);		
	}
	
	private void buildButtonAccept() {
		
		JButton buttonAccept = new JButton("Aceptar");
		buttonAccept.setBounds(180, 90, 100, 30);
		buttonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String[] columns = new String[] {"u.username", "u.permissions"};
				String[] tables = new String[] {"users u"};
				String[][] inputWhere = new String[][] {{"u.username", "'" + inputUser.getTextFieldData() + "'", "AND"}, {"u.password", "'" + inputPassword.getTextFieldData() + "'"}};
				ResultSet dbUserPassword = dao.dbio.select(columns, tables, inputWhere);
				
				int countUserPassword = 0;
				
				while (dbUserPassword.next()) {	
					countUserPassword++;
				}
				
				dbUserPassword.first();
				
				switch (countUserPassword) {
				case 0: incorrectUserPassword(); break;
				case 1: correctUserPassword(dbUserPassword.getString(1), dbUserPassword.getString(2)); break;
				default: internalError(); break;
				}
				
			}
		});
		panel.add(buttonAccept);
	}
	
	private void incorrectUserPassword() {
		//Imprimir alerta (usuario o contraseña incorrectos).
		alertWindow incorrectUserPassword = new alertWindow("Usuario o contraseña incorrectos", "Internarlo de vuelta");
	}
	
	private void correctUserPassword(String username, String permissions) {
	
		//Esto solo se ejecuta si hay algún usuario que coincida con el usuario y contraseña introducidos.
		
		String alertBody = "Bienvenid@ de nuevo " + username;
		alertWindow successLogin = new alertWindow(alertBody, "Aceptar");
		//Emitir alerta: Bienvenido "username"
	
		//Este string contendrá 0 y 1 en función de si tiene el permiso o no.
		userSettings.setUserPermissions(permissions);
		
		userSettings.setUsername(username);
	}
	
	private void internalError() {
		//Imprimir alerta (multiples resultados que coinciden con el usuario y la contraseña).
		alertWindow internalError = new alertWindow("Internal error", "Cerrar");
	}


}
