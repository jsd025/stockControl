package mainWindow;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import alert.alertWindow;
import components.LabelComboBoxPanel;
import components.TextInputPanel;

public class contentTakeOrders extends JPanel {
	
	Dimension panelDimension;
	LabelComboBoxPanel inputPlate;
	TextInputPanel inputAmount;
	
	contentTakeOrders(Dimension panelDimension) {
		
		this.panelDimension = panelDimension;
		this.setBackground(setting.programSettings.getBackgroundContentColor());
		this.setLayout(null);
		buildComponents();
	}
	
	private void buildComponents() {
		buildTitle();
		buildComboBox();
		buildTextField();
		buildButtonClear();
		buildButtonAccept();
	}
	
	private void buildTitle() {
		JLabel title = new JLabel("Comandero");
		title.setBounds((int)((panelDimension.getWidth()/2)-150), 10, 300, 30);
		title.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(title);
	}
	
	private void buildComboBox() {
		String[] allPlates = getAllPlatesFromDatabase();
		inputPlate = new LabelComboBoxPanel(this, "Plato: ", allPlates, null);
		inputPlate.setBounds((int)((panelDimension.getWidth()/2)-190), 50, 380, 30);
		this.add(inputPlate);
	}
	
	private void buildTextField() {
		inputAmount = new TextInputPanel("Cantidad: ", null);
		inputAmount.setBounds((int)((panelDimension.getWidth()/2)-190), 90, 380, 30);
		this.add(inputAmount);
	}
	
	private void buildButtonClear() {
		JButton buttonClear = new JButton("Limpiar");
		buttonClear.setBounds((int)((panelDimension.getWidth()/2)-190), 130, 170, 30);
		buttonClear.setBackground(setting.programSettings.getButtonCancelColor());
		buttonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				clearInput();
			}
		});
		this.add(buttonClear);
	}
	
	private void buildButtonAccept() {
		JButton buttonAccept = new JButton("Enviar comanda");
		buttonAccept.setBounds((int)((panelDimension.getWidth()/2)-10), 130, 170, 30);
		buttonAccept.setBackground(setting.programSettings.getButtonColor());
		buttonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				String[][] productsAndAmounts = getProductsAndAmountsFromDatabase();
				
				if (isNumber(inputAmount.getTextFieldText())) {
					Boolean everythingOK = true;
					for (int i=0; i<productsAndAmounts.length; i++) {
						productsAndAmounts[i][1] = Integer.toString(Integer.parseInt(productsAndAmounts[i][1]) * Integer.parseInt(inputAmount.getTextFieldText()));
						if (!updateProductAmount(productsAndAmounts[i])) {
							everythingOK = false;
						}
					}
					
					if (everythingOK) {
						new alertWindow("Comanda tomada correctamente", "Aceptar");
					} else {
						new alertWindow("Error 601: Algo salió mal tomando la comanda", "Aceptar");
					}
					
					clearInput();
				}
			}
		});
		this.add(buttonAccept);
	}
	
	private String[] getAllPlatesFromDatabase() {
		String[] platesList = null;
		
		try {
			String[] columns = new String[] {"plates.name"};
			String[] tables = new String[] {"plates"};
			ResultSet users = dao.dbio.select(columns, tables, null);
			
			int numberOfUsers = 0;
			
			while (users.next()) {
				numberOfUsers++;
			}
			
			platesList = new String[numberOfUsers];
			users.first();
			
			for (int i=0; i<platesList.length; i++) {
				platesList[i] = users.getString(1);
				users.next();
			}
		} catch (SQLException e) {
			new alertWindow("Error 301: Fallo al leer la base de datos", "Aceptar");
			e.printStackTrace();
		}
		
		return platesList;
	}
	
	private String[][] getProductsAndAmountsFromDatabase() {
		String[][] productsAndAmounts = null;
		try {
			String[] columns = new String[] {"pr.spanish_name", "ppr.amount"};
			String[] tables = new String[] {"products pr", "products_plates_relationship ppr", "plates pl"};
			String[][] inputWhere = new String[][] {
				{"pr.id_product", "ppr.id_product", "AND"},
				{"ppr.id_plate", "ppr.id_plate", "AND"},
				{"pl.name", "'" + inputPlate.getComboBoxSelectedItem() + "'"}
			};
			
			ResultSet resultSetProductsAndAmounts = dao.dbio.select(columns, tables, inputWhere);
			int countProductsAndAmounts = 0;
			
			while (resultSetProductsAndAmounts.next()) {
				countProductsAndAmounts++;
			}
			productsAndAmounts = new String[countProductsAndAmounts][2];
			
			resultSetProductsAndAmounts.first();
			for (int i=0; i<productsAndAmounts.length; i++) {
				productsAndAmounts[i][0] = resultSetProductsAndAmounts.getString(1);
				productsAndAmounts[i][1] = resultSetProductsAndAmounts.getString(2);
				resultSetProductsAndAmounts.next();
			}
		} catch (SQLException e) {
			new alertWindow("Error 301: Fallo leyendo el resultado de la base de datos", "Aceptar");
			e.printStackTrace();
		}
		return productsAndAmounts;
	}
	
	private Boolean updateProductAmount(String[] productsData) {
		//productsData[x][0] = id_product
		//productsData[x][1] = amount.
		String[][] data = new String[][] {{"products.current_amount", "(products.current_amount - " + productsData[1] + ")"}};
		String[] tables = new String[] {"products"};
		String[][] inputWhere = new String[][] {{"products.spanish_name", "'" + productsData[0] + "'"}};
		
		int result = dao.dbio.update(data, tables, inputWhere);
		System.out.println(result);
		if (result == 1) {
			return true;
		} else {
			return false;
		}
		
	}
	
	private Boolean isNumber(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch(ClassCastException e) {
			return false;
		}
	}
	
	private void clearInput() {
		inputPlate.changeComboBoxSelectedItem(null);
		inputAmount.changeTextFieldText("");
	}
	
	//Titulo: Comandero.
	//Label: "Plato: ". Y ComboBox con todos los platos (select sin inputWhere).
	//Label: "Cantidad: ". input para poner un texto convertible a número.
	//Botón "Limpiar" que llame un método "reiniciarInput", que ponga el comboBox Selected en -1 y setee el texto del input en "".
	//Boton Enviar que acceda a la base de datos con:
	//		- Comprobar que el input es correcto.
	//		- Un select: Obtenga todos los "id_product y amount" que haya en ppr que coincidan con el plate.name
	//		- Un bucle de update: que actualice products donde "id_product" sea igual al recuperado en el select[i][0].
	// 		Y current_amount sea igual igual a si mismo menos amount[i][1] recuperado en el select.
	//Si el update se hace de forma correcta imprimir un alert con los cambios. ("Comanda tomada". "Aceptar"). Si no, alert de error.
	//Y borra todos los datos llamando a "vaciarInput".
}
