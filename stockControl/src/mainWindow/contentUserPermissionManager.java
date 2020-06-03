package mainWindow;

import java.awt.Color;
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
import dao.dbio;

public class contentUserPermissionManager extends JPanel{
	 
	String selectedUsername;
	String selectedPermissions;
	
	Dimension panelDimension;
	
	ArrayList<Component> componentsList = new ArrayList<Component>();
	
	contentUserPermissionManager(Dimension panelDimension) {		
		//Añadir un titulo.
		//Añadir un formulario con nombre de usuario y sus permisos sacados de la longitud de "userPermissions[i][0]"
		//y sus permisos actuales en "true" o "false", con un CheckBox de "Sí" o "No". Al finalizar hacer update.
		
		this.panelDimension = panelDimension;
		this.setBackground(setting.programSettings.getBackgroundContentColor());
		this.setLayout(null);
		
		selectedUsername = setting.userSettings.getUsername();
		
		buildComponents();
	}
	
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
			new alertWindow("Error 301: Fallo al leer la base de datos", "Aceptar");
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
			//Crear clase "LabelRadioButtonPanel".
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
					//Update a base de datos
					String[][] data = new String[][] {{"u.permissions", "'" + inputPermissions + "'"}};
					String[] tables = new String[] {"users u"};
					String[][] inputWhere = new String[][] {{"u.username", "'" + getInputUsername() + "'"}};
					dao.dbio.update(data, tables, inputWhere);
					
					//Si ha cambiado sus propios permisos pedir que reinicie la aplicación.
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
	
	private String getInputPermissions() {
		String inputPermissions = "";
		for (int i=0; i<componentsList.size(); i++) {
			try {
				if (((LabelRadioButtonPanel) componentsList.get(i)).getRadioButtonTextSelected().equals("Sí")) {
					inputPermissions += "1";
				} else if (((LabelRadioButtonPanel) componentsList.get(i)).getRadioButtonTextSelected().equals("No")) {
					inputPermissions += "0";
				} else {
					//Error. Cancelar operación y poner mensaje de alerta.
				}
			} catch(ClassCastException e) {
				//Llego aquí cuando el componente leido es un JButton o un LabelComboBoxPanel.
				//Lo ignoro.
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
			new alertWindow("Error 501: Fallo en recuperar los permisos guardados de un usuario", "Aceptar");
		}
		return permissions;
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
}
