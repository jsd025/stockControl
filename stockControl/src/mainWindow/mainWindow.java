package mainWindow;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

//Class for show main JFrame
public class mainWindow extends JFrame{
	
	//CLASS VARIABLES
	private Dimension mainWindowDimension;
	private JSplitPane splitPane;
	private ArrayList<JButton> menuButtonsList;
	
	//CONSTRUCTORS
	public mainWindow(int width, int height) {
		
		//Position the frame at center of screen.
		this.setLocationRelativeTo(null);
		
		//Set size of the frame.
		mainWindowDimension = new Dimension(width, height);

		buildFrame();
		
	}
	
	public mainWindow() {
		
		//Set size of the frame.
		mainWindowDimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Set frame maximized.
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		buildFrame();
		
	}

	//METHODS
	private void buildFrame() {
		
		//Set CloseOperation.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Size the frame.
		this.setSize(mainWindowDimension);
		
		//Components for the frame.
		createComponents();
		
		menuButtonsList = ((menuPanel)splitPane.getLeftComponent()).getButtonList();
		
		createMenuButtonsEvent();
		
		//Show frame.
		this.setVisible(true);
		
	}

	private void createComponents() {
		
		//Create menu, default content and split pane.
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new menuPanel(new Dimension(((int)mainWindowDimension.getWidth()/5),(int)mainWindowDimension.getHeight())), null);
		updateSplitPane(new contentTakeOrders(new Dimension(((int)mainWindowDimension.getWidth()/5*4), (int)mainWindowDimension.getHeight())));
		splitPane.setDividerLocation((int)mainWindowDimension.getWidth()/5);
		
		this.getContentPane().add(splitPane);
	}

	private void createMenuButtonsEvent() {
		JButton buttonN;
		for (int i=0; i<menuButtonsList.size(); i++) {
			buttonN = menuButtonsList.get(i);
			buttonN.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					menuButtonClickEvent((JButton)e.getSource());
				}
			});
		}
	}
	
	public void refresh() {
		mainWindowDimension = this.getContentPane().getSize();
		//updateComponents();
	}
	
	/*
	private void updateComponents() {
		if (rightComponentOfSplit.getClass() == content.class) {
			((content) rightComponentOfSplit).refreshContent();
		}
	}
	*/
	
	private void menuButtonClickEvent(JButton pressedButton) {
		
		//Disable all buttons
		for (int i=0; i<menuButtonsList.size(); i++) {
			menuButtonsList.get(i).setBackground(((menuPanel) splitPane.getLeftComponent()).getMenuButtonColor());
		}
		
		//Activate the corresponding button
		for (int i=0; i<menuButtonsList.size(); i++) {
		
			if (pressedButton.equals(menuButtonsList.get(i))) {
				if (((menuPanel)splitPane.getLeftComponent()).getIndexSelectedButton() != i) {
					((menuPanel)splitPane.getLeftComponent()).setIndexSelectedButton(i);
					menuButtonsList.get(i).setBackground(((menuPanel) splitPane.getLeftComponent()).getMenuButtonSelectedColor());
					
					Dimension contentDimension = new Dimension(((int)mainWindowDimension.getWidth()/5*4), (int)mainWindowDimension.getHeight());
					
					switch (menuButtonsList.get(i).getText()) {
						case "Comandero": updateSplitPane(new contentTakeOrders(contentDimension)); break;
						case "Productos": updateSplitPane(new content(contentDimension, "products")); break;
						case "Proveedores": updateSplitPane(new content(contentDimension, "providers")); break;
						case "Platos": updateSplitPane(new content(contentDimension, "plates")); break;
						case "Pedidos": updateSplitPane(new content(contentDimension, "orders")); break;
						//case "Generador de informes": updateSplitPane(new contentReportsGenerator(contentDimension)); break;
						case "Administrar permisos de usuarios": updateSplitPane(new contentUserPermissionManager(contentDimension)); break;
						//case "Configuraci�n": splitPane.setRightComponent(new contentUserConfiguration(contentDimension)); break;
						default: System.out.println("Error 301!"); break;//Imprimir aqu� mensaje de alerta.
					}
					
				}
			}
		}
		
		
	}
	
	private void updateSplitPane(Object rightComponent) {	
		splitPane.setRightComponent((Component) rightComponent);
		splitPane.setDividerLocation(splitPane.getDividerLocation());
		
	}
	
}
