package mainWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class menuPanel extends JPanel{
	
	//JList listButtonsMenu;
	Color menuBackgroundColor, menuButtonColor, menuButtonSelectedColor;
	ArrayList<JButton> buttonList = new ArrayList<JButton>();
	int indexSelectedButton;
	Dimension panelDimension;
	int buttonPositionX, buttonPositionY, buttonWidth, buttonHeight, topMargin;
	
	menuPanel(Dimension panelDimension) {
		
		this.panelDimension = panelDimension;
		
		menuBackgroundColor = new Color(201, 169, 203);
		menuButtonColor = new Color(169, 83, 160);
		menuButtonSelectedColor = setting.programSettings.getButtonColor();
		
		this.setLayout(null);
		
		//Background color.
		this.setBackground(menuBackgroundColor);
		
		//Fill the list of buttons that will display.
		fillList();
		
		//Create components for the panel "menu".
		createComponents();
		
		indexSelectedButton = 0;
		buttonList.get(0).setBackground(menuButtonSelectedColor);
		
	}
	
	public ArrayList<JButton> getButtonList() {
		return buttonList;
	}

	public void setButtonList(ArrayList<JButton> buttonList) {
		this.buttonList = buttonList;
	}

	public int getIndexSelectedButton() {
		return indexSelectedButton;
	}

	public void setIndexSelectedButton(int indexSelectedButton) {
		this.indexSelectedButton = indexSelectedButton;
	}
	
	public Color getMenuBackgroundColor() {
		return menuBackgroundColor;
	}

	public void setMenuBackgroundColor(Color menuBackgroundColor) {
		this.menuBackgroundColor = menuBackgroundColor;
	}

	public Color getMenuButtonColor() {
		return menuButtonColor;
	}

	public void setMenuButtonColor(Color menuButtonColor) {
		this.menuButtonColor = menuButtonColor;
	}

	public Color getMenuButtonSelectedColor() {
		return menuButtonSelectedColor;
	}

	public void setMenuButtonSelectedColor(Color menuButtonSelectedColor) {
		this.menuButtonSelectedColor = menuButtonSelectedColor;
	}

	private void fillList() {
		
		String list[] = {"Comandero", "Productos", "Proveedores", "Platos", "Pedidos", "Administrar permisos de usuarios"};		
		String[][] userPermissions = setting.userSettings.getUserPermissions();
		
		JButton buttonN;
		
		for (int i=0; i<list.length; i++) {
			if (userPermissions[i][0].equals(list[i]) && userPermissions[i][1].equals("true")) {
				buttonN = new JButton();
				buttonN.setText(list[i]);
				buttonN.setMinimumSize(new Dimension(50, 30));
				buttonN.setBorderPainted(false);
				buttonN.setBackground(menuButtonColor);
				buttonN.setFocusPainted(false);
				buttonN.setHorizontalAlignment(SwingConstants.LEFT);
				
				buttonList.add(buttonN);
			}
		}
	}
	
	private void createComponents() {
		
		setButtonsSize();
		
		for (int i=0; i<buttonList.size(); i++) {
			
			buttonPositionY = (40*i) + topMargin;
			
			buttonList.get(i).setBounds(buttonPositionX, buttonPositionY, buttonWidth, buttonHeight);
			
			this.add(buttonList.get(i));
		}
		
	}
	
	private void setButtonsSize() {
		
		buttonHeight = 30;
		
		if (this.panelDimension.getHeight() > 1300) {
			topMargin = 100;
		} else if (this.panelDimension.getHeight() < 300) {
			topMargin = 0;
		} else {
			topMargin = ((int)this.panelDimension.getHeight()-300)/10;
		}
		
		buttonWidth = (int)Math.round(this.panelDimension.getWidth() * 0.7);
		
		buttonPositionX = (((int)this.panelDimension.getWidth() - buttonWidth)/2);
		
	}
	
}
