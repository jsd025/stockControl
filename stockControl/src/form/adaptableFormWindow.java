package form;

import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class adaptableFormWindow extends JFrame {
	
	//String[][] objectData;
	String[][] columnsList;
	JPanel panel;
	ArrayList<Component> elements = new ArrayList<Component>();
	
	public adaptableFormWindow(String consultedItem) {
		
		if (consultedItem.toUpperCase().equals("PLATES")) {
			//Almaceno el texto del label, y el nombre de la columna en la base de datos,
			columnsList = new String[][] {
				{"Nombre", "plates.name"},
				{null, "products.spanish_name", "products_plates_relationship.amount"}
			};
		} else if (consultedItem.toUpperCase().equals("ORDERS")) {
			
		}
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(380, ((40*(columnsList.length+1))+10));
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		buildComponents();
	}
	
	private void buildComponents() {
		
		panel = new JPanel(null);
		
		for (int i=0; i<columnsList.length; i++) {
			
			if (columnsList[i][0] != null) {
				TextViewInputPanel inputPanel = new TextViewInputPanel(columnsList[i]);
				addElements(inputPanel);
			} else {
				DoubleInputPanel dobleInputPanel = new DoubleInputPanel();
				addElements(dobleInputPanel);
				//Añadir los datos en caso de que ya existiera el elemento.
			}
			
		}
		
		//Añadir boton aceptar y cancelar
		
		updateElementsPosition();
		
		this.add(panel);
		
	}
	
	private void newComboBox() {
		//Llamar a este método cuando se completa algo en un comboBox nuevo.
		DoubleInputPanel dobleInputPanel = new DoubleInputPanel();
		addElements(dobleInputPanel);
		
		updateElementsPosition();
	}
	
	private void addElements(Component component) {
		
		elements.add(component);
		panel.add(component);
		
	}
	
	public void updateElementsPosition() {
		for (int i=0; i<elements.size(); i++) {
			elements.get(i).setBounds(0, ((i*40)+10), 380, 30);
		}
	}
	
	
	
}
