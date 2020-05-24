package window;

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
	Color colorPersonalizadoFondo, colorPersonalizadoTexto, colorSelectedButton;
	ArrayList<JButton> buttonList = new ArrayList<JButton>();
	int indexSelectedButton;
	Dimension panelDimension;
	int buttonPositionX, buttonPositionY, buttonWidth, buttonHeight, topMargin;
	
	menuPanel(Dimension panelDimension) {
		
		System.out.println("Dimension menu: \nAncho: "+panelDimension.getWidth()+"\nAlto: "+panelDimension.getHeight()+"\n");
		
		this.panelDimension = panelDimension;
		
		colorPersonalizadoFondo = new Color(208, 253, 105);
		colorPersonalizadoTexto = new Color(249, 90, 90);
		colorSelectedButton = new Color(255, 255, 255);
		
		this.setLayout(null);
		
		//Background color.
		this.setBackground(colorPersonalizadoFondo);
		
		//Fill the list of buttons that will display.
		fillList();
		
		//Create components for the panel "menu".
		createComponents();
		
		indexSelectedButton = 0;
		buttonList.get(0).setContentAreaFilled(true);
		
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

	private void fillList() {
		
		String list[] = {"Comandero", "Productos", "Proveedores", "Platos", "Registro de comandas", "Generador de informes", "Administrar permisos de usuarios"};		
		
		JButton buttonN;
		
		for (int i=0; i<list.length; i++) {
			
			buttonN = new JButton();
			buttonN.setText(list[i]);
			buttonN.setMinimumSize(new Dimension(50, 30));
			buttonN.setBorderPainted(false);
			buttonN.setContentAreaFilled(false);
			buttonN.setFocusPainted(false);
			buttonN.setHorizontalAlignment(SwingConstants.LEFT);
			buttonN.setBackground(colorSelectedButton);
			buttonList.add(buttonN);
			
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
