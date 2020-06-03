package mainWindow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import alert.alertWindow;
import components.LabelComboBoxPanel;
import components.LabelRadioButtonPanel;

//Class for manage permissions of users
public class contentUserPermissionManager extends JPanel{
	
	//CLASS VARIABLES
	private String selectedUsername;
	private Dimension panelDimension;
	private ArrayList<Component> componentsList = new ArrayList<Component>();
	
	//CONSTRUCTOR
	contentUserPermissionManager(Dimension panelDimension) {				
		this.panelDimension = panelDimension;
		this.setBackground(setting.programSettings.getBackgroundContentColor());
		this.setLayout(null);
		
		selectedUsername = setting.userSettings.getUsername();
		
		buildComponents();
	}
	
	//METHODS
	private void buildComponents() {
		
		buildTitle();
		buildLabelComboBoxPanel();
		buildLabelRadioButtonPanel();
		buildButtonAccept();
	}
	
	private void buildTitle() {
		JLabel title = new JLabel("Administrar permisos de usuario");
		title.setBounds((int)((panelDimension.getWidth()/2)-150), 10, 300, 30);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(title);
	}
	
	private void buildLabelComboBoxPanel() {	
		try {
			String[] columns = new String[] {"u.username"};
			String[] tables = new String[] {"users u"};
			ResultSet users = dao.dbio.select(columns, tables, null);
			
			int numberOfUsers = 0;
			
			while (users.next()) {
				numberOfUsers++;
			}
			String[] usersList = new String[numberOfUsers];
			users.first();
			
			for (int i=0; i<usersList.length; i++) {
				usersList[i] = users.getString(1);
				users.next();
			}
			
			LabelComboBoxPanel inputUsername = new LabelComboBoxPanel(this, "Usuario", usersList, selectedUsername);
			inputUsername.setBounds((int)((panelDimension.getWidth()/2)-190), 50, 380, 30);
			componentsList.add(inputUsername);
			this.add(inputUsername);
		} catch (SQLException e) {
			new alertWindow("Error 325: Fallo al leer la base de datos", "Aceptar");
			e.printStackTrace();
		}
	}
	
	private void buildLabelRadioButtonPanel() {
		String[][] userPermissions = setting.userSettings.getUserPermissions();
		String[] radioButtonList = new String[] {"No", "Sí"};
		
		for (int i=0; i<userPermissions.length; i++) {
			int indexRadioButtonSelected = -1;
			
			if (userPermissions[i][1].equals("false")) {
				indexRadioButtonSelected = 0;
			} else if (userPermissions[i][1].equals("true")) {
				indexRadioButtonSelected = 1;
			}

			LabelRadioButtonPanel inputPermission = new LabelRadioButtonPanel(userPermissions[i][0], radioButtonList, indexRadioButtonSelected);
			inputPermission.setBounds((int)((panelDimension.getWidth()/2)-190), 90+(i*40), 380, 30);
			componentsList.add(inputPermission);
			this.add(inputPermission);
		}
	}
	
	private void buildButtonAccept() {
		JButton buttonSave = new JButton("Guardar");
		buttonSave.setBounds((int)((panelDimension.getWidth()/2)-85), 50+(componentsList.size()*40), 170, 30);
		buttonSave.setBackground(setting.programSettings.getButtonColor());
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String inputPermissions = getInputPermissions();
				
				if (checkInputFormat(inputPermissions)) {
					//Making update whit new user permissions.
					String[][] data = new String[][] {{"u.permissions", "'" + inputPermissions + "'"}};
					String[] tables = new String[] {"users u"};
					String[][] inputWhere = new String[][] {{"u.username", "'" + getInputUsername() + "'"}};
					dao.dbio.update(data, tables, inputWhere);
					
					//If you have changed your own permissions, say to restart the app.
					if (getInputUsername().equals(setting.userSettings.getUsername())) {
						new alertWindow("Ha modificado los permisos de su propio usuario. Reinicie la aplicación para guardar los cambios", "Aceptar");
					}
				}
				
			}
		});
		componentsList.add(buttonSave);
		this.add(buttonSave);
	}
	
	private Boolean checkInputFormat(String inputPermissions) {
		if (isNumber(inputPermissions)) {
			return true;
		} else {
			return false;
		}
	}
	
	public void refreshPermissions(String username) {
		
		String userPermissions = getUserPermissions(username);
		System.out.println(username);
		int permissionsCounter = 0;				
		for (int i=0; i<componentsList.size(); i++) {
			try {
				if (permissionsCounter <6) {
					int permission = Character.getNumericValue(userPermissions.charAt(permissionsCounter));
					((LabelRadioButtonPanel) componentsList.get(i)).setRadioButtonSelected(permission);
					permissionsCounter++;
				}
			} catch(ClassCastException e) {
				//Llego aquí cuando el componente leido es un JButton o un LabelComboBoxPanel.
				//Lo ignoro.
			}
		}
	}
	
	//GETTERS & SETTERS	
	private String getInputPermissions() {
		String inputPermissions = "";
		for (int i=0; i<componentsList.size(); i++) {
			try {
				if (((LabelRadioButtonPanel) componentsList.get(i)).getRadioButtonTextSelected().equals("Sí")) {
					inputPermissions += "1";
				} else if (((LabelRadioButtonPanel) componentsList.get(i)).getRadioButtonTextSelected().equals("No")) {
					inputPermissions += "0";
				} else {
					new alertWindow("Error 402: Error interno", "Aceptar");
				}
			} catch(ClassCastException e) {
				//I get here when the read component is a JButton or a LabelComboBoxPanel.
				//Just ignore it.
			}
		}
		return inputPermissions;
	}
	
	private String getInputUsername() {
		return  ((LabelComboBoxPanel) componentsList.get(0)).getComboBoxSelectedItem();
	}
	
	private Boolean isNumber(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	private String getUserPermissions(String username) {
		String[] columns = new String[] {"u.permissions"};
		String[] tables = new String[] {"users u"};
		String[][] inputWhere = new String[][] {{"u.username", "'" + username + "'"}};
		
		ResultSet resultSetPermissions = dao.dbio.select(columns, tables, inputWhere);
		
		String permissions;
		try {
			resultSetPermissions.first();
			permissions = resultSetPermissions.getString(1);
		} catch (SQLException e) {
			e.printStackTrace();
			permissions = "000000";
			new alertWindow("Error 326: Fallo en recuperar los permisos guardados de un usuario", "Aceptar");
		}
		return permissions;
	}
}
