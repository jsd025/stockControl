package window;

import java.awt.Component;
import java.awt.Dimension;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class contentUserPermissionManager extends JPanel{
	
	//Comprobar que 
	String selectedUsername;
	String selectedPermissions;
	
	Dimension panelDimension;
	
	ArrayList<Component> componentsList = new ArrayList<Component>();
	
	contentUserPermissionManager(Dimension panelDimension) {		
		//Añadir un titulo.
		//Añadir un formulario con nombre de usuario y sus permisos sacados de la longitud de "userPermissions[i][0]"
		//y sus permisos actuales en "true" o "false", con un CheckBox de "Sí" o "No". Al finalizar hacer update.
		
		this.panelDimension = panelDimension;
		
		this.setLayout(null);
		
		selectedUsername = setting.userSettings.getUsername();
		
		buildComponents();
	}
	
	private void buildComponents() {
		
		buildTitle();
		
		buildLabelComboBoxPanel();
		
		buildLabelRadioButtonPanel();
	}
	
	private void buildTitle() {
		JLabel title = new JLabel("Administrar permisos de usuario");
		title.setBounds((int)((panelDimension.getWidth()/2)-50), 10, 100, 30);
		this.add(title);
	}
	
	private void buildLabelComboBoxPanel() {
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
			usersList[i] = users.getString(0);
			users.next();
		}
		
		//Crear clase "LabelComboBoxPanel".
		LabelComboBoxPanel inputUsername = new LabelComboBoxPanel("Usuario", usersList, selectedUsername);
		inputUsername.setBounds(10, 50, 380, 30);
		componentsList.add(inputUsername);
		this.add(inputUsername);
	}
	
	private void buildLabelRadioButtonPanel() {
		String[][] userPermissions = setting.userSettings.getUserPermissions();
		String[] radioButtonList = new String[] {"No", "Sí"};
		
		for (int i=0; i<userPermissions.length; i++) {
			int indexRadioButtonSelected;
			
			if (userPermissions[i][1].equals("false")) {
				indexRadioButtonSelected = 0;
			} else if (userPermissions[i][1].equals("true")) {
				indexRadioButtonSelected = 1;
			}
			//Crear clase "LabelRadioButtonPanel".
			LabelRadioButtonPanel inputPermission = new LabelRadioButtonPanel(userPermissions[i][0], radioButtonsList, indexRadioButtonSelected);
			inputPermission.setBounds(10, 90+(i*40), 380, 30);
			componentsList.add(inputPermission);
			this.add(inputPermission);
		}
	}
	
}
