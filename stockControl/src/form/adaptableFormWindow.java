package form;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import alert.alertWindow;
import components.DoubleInputPanel;
import components.TextInputPanel;
import mainWindow.content;

public class adaptableFormWindow extends JFrame {
	
	String[][] columnsList;
	JPanel panel;
	ArrayList<Component> elements = new ArrayList<Component>();
	String[] products;
	String consultedItem;
	String nameForModifyObject;
	content invoker;
	
	public adaptableFormWindow(content invoker, String consultedItem) {
		
		this.invoker = invoker;
		this.nameForModifyObject = null;
		initializeVariables(consultedItem);
		buildFrame();
	}
	
	public adaptableFormWindow(content invoker, String consultedItem, String nameForModifyObject) {
		
		this.invoker = invoker;
		this.nameForModifyObject = nameForModifyObject;		
		initializeVariables(consultedItem);
		buildFrame();
		
	}
	
	private void initializeVariables(String consultedItem) {
		if (consultedItem.toUpperCase().equals("PLATES")) {
			//Almaceno el texto del label, y el nombre de la columna en la base de datos,
			columnsList = new String[][] {
				{"Nombre", "plates.name"},
				{null, "products.spanish_name", "products_plates_relationship.amount"}
			};
		} else if (consultedItem.toUpperCase().equals("ORDERS")) {
			//Almaceno el texto del label, y el nombre de la columna en la base de datos,
			columnsList = new String[][] {
				{"Proveedor", "providers.name"},
				{"Fecha", "orders.date"},
				{null, "products.spanish_name", "products_orders_relationship.ordered_amount"}
			};
		}
		
		this.consultedItem = consultedItem;
		products = getAllProducts();
		panel = new JPanel(null);
	}
	
	private void buildFrame() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		buildComponents();
		
		this.setSize(380, ((40*(elements.size()+1))+10));
		this.setVisible(true);
	}
	
	private void buildComponents() {
		
		buildInputComponents();
		buildButtons();
		updateElementsPosition();
		this.add(panel);
		
	}
	
	private void buildInputComponents() {
		for (int i=0; i<columnsList.length; i++) {
						
			if (columnsList[i][0] != null) {
				String textFieldValue = nameForModifyObject;
				
				if (consultedItem.toUpperCase().equals("ORDERS")) {
					String[] orderData = getDateFromDatabase(nameForModifyObject);
					if (nameForModifyObject != null) {
						if (columnsList[i][1].contains("date")) {
							textFieldValue = orderData[1];
						} else {
							textFieldValue = orderData[0];
						}
					} else {
						textFieldValue = null;
					}
					
				}
				
				TextInputPanel inputPanel = new TextInputPanel(columnsList[i][0], textFieldValue);
				addElements(inputPanel);
				
			} else {
				
				String[][] selectedProducts = getSelectedProducts(nameForModifyObject);
				
				if (selectedProducts.length>0) {
					for (int j=0; j<selectedProducts.length; j++) {
						DoubleInputPanel dobleInputPanel = new DoubleInputPanel(products, selectedProducts[j][0], selectedProducts[j][1], new Dimension(380, 30));
						addElements(dobleInputPanel);
					}
				}
				
				newComboBox();
			}
		}
	}
	
	private void buildButtons() {
		JButton buttonCancel = new JButton("Cancelar");
		buttonCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				disposeWindow();
			}
		});
		addElements(buttonCancel);
		
		//Añadir que tiene que hacer si estoy creando un nuevo registro.
		
		JButton buttonAccept = new JButton("Aceptar");
		buttonAccept.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String[][] inputData = getInputData();

				if (checkInputData(inputData)) {
					if (nameForModifyObject == null) {
						insertInDatabase(inputData);
					} else {
						updateDatabase(inputData);
					}
					invoker.refreshTable();
					disposeWindow();
				} else {
					new alertWindow("Error: 602: Los datos introducidos no son correctos o no se han podido leer", "Aceptar");
				}
			}
		});
		addElements(buttonAccept);
	}
	
	private String[][] getInputData() {
		String[][] inputData = new String[elements.size()-2][2];
		
		for (int i=0; i<elements.size(); i++) {
			//Si son botones no hago nada.
			switch (elements.get(i).getClass().toString()) {
				case "class form.TextViewInputPanel": 
					inputData[i][0] = ((TextInputPanel) elements.get(i)).getTextFieldText();
					break;
				case "class form.DoubleInputPanel":
					String itemSelected = ((DoubleInputPanel) elements.get(i)).getItemSelected();
					String textFieldText = ((DoubleInputPanel) elements.get(i)).getTextFieldText();
					inputData[i][0] = itemSelected;
					inputData[i][1] = textFieldText;
					break;
			}
		}
		return inputData;
	}
	
	private Boolean checkInputData(String[][] inputData) {
		Boolean everythingOK = true;		
		ArrayList<String> productsList = new ArrayList<String>();
		
		for (int i=0; i<inputData.length; i++) {
			if (inputData[i][0] != null) {
				if (inputData[i][1] == null) {
					//Date:
					if (consultedItem.toUpperCase().equals("ORDERS") && i == 1) {
						String date = inputData[i][0];
						if (!(isNumber(date.substring(0, 4)) && isNumber(date.substring(5, 7)) && isNumber(date.substring(8, 10))) || (!(date.substring(4, 5).equals("-") && date.substring(7, 8).equals("-")))) {
							everythingOK = false;
							new alertWindow("Error 308: No se ha actualizado la base de datos. El formato correcto para la fecha es: aaaa-mm-dd", "Aceptar");
						}
					}
				} else {
					//Comprobar que no se repitan productso
					for (int j=0; j<productsList.size(); j++) {
						if (productsList.get(j).equals(inputData[i][0])) {
							everythingOK = false;
							new alertWindow("Error 309: No se ha actualizado la base de datos. Hay productos repetidos", "Aceptar");
						}
					}
					productsList.add(inputData[i][0]);
					
					if (!isNumber(inputData[i][1])) {
						everythingOK = false;
						new alertWindow("Error 310: No se ha actualizado la base de datos. Formato incorrecto de entrada", "Aceptar");
					}
				}
			}
		}
		return everythingOK;
	}
	
	private void insertInDatabase(String[][] inputData) {
		
		//En caso de insert plate:
		//Si inputData contiene valores null, null. Descartarlos al principio.
		//Si inputdata [i][1] es null. inputData[i][0] contiene el nombre del plato.
		//**Debo insertar el plates con plates.name y luego recuperar el ID del plate que acabo de insertar:
		//	INSERT INTO plates VALUES(plates.name = inputData[i][0]);
		//  SELECT plates.id_plate FROM plates WHERE plates.name = inputData[i][0];	//Obtengo el id de plate.
		//
		//Bucle: Para todos los casos donde inputData[i][1] no sea null:
		//	Select id_produc from products where products.spanish_name = inputData[i][0];
		//	Ahora debo insertar en ppr: INSERT INTO ppr VALUES(id_plate = [id_plate que acabo de conseguir], id_product = [id_product que acabo de conseguir ], amount = [i][1]);
		
		try {
			switch(consultedItem.toUpperCase()) {
			case "PLATES":
				String idPlate = null;
				for (int i=0; i<inputData.length; i++) {
					if (!(inputData[i][0] == null && inputData[i][1] == null)) {				
						if (inputData[i][1] == null) {
							String[][] data = new String[][] {{"plates.name", "'" + inputData[i][0] + "'"}};
							String table = "plates";
							dao.dbio.insert(table, data);
							
							if (idPlate == null) {
								String[] columns = new String[] {"plates.id_plate"};
								String[] tables = new String[] {"plates"};
								String[][] inputWhere = new String[][] {{"plates.name", "'" + inputData[i][0] + "'"}};
								ResultSet resultSetIdPlate = dao.dbio.select(columns, tables, inputWhere);
								resultSetIdPlate.first();
								idPlate = resultSetIdPlate.getString(1);
							}
						} else {
							String[] columns = new String[] {"products.id_product"};
							String[] tables = new String[] {"products"};
							String[][] inputWhere = new String[][] {{"products.spanish_name", "'" + inputData[i][0] + "'"}};
							ResultSet resultSetIdProduct = dao.dbio.select(columns, tables, inputWhere);
							resultSetIdProduct.first();
							String idProduct = resultSetIdProduct.getString(1);
							
							String[][] data2 = new String[][] {{"products_plates_relationship.id_plate", "'" + idPlate + "'"}, {"products_plates_relationship.id_product", "'" + idProduct + "'"}, {"products_plates_relationship.amount", "'" + inputData[i][1] + "'"}};
							String table2 = "products_plates_relationship";
							dao.dbio.insert(table2, data2);
						}
					}
				}
				break;
			case "ORDERS":
				/*
				for (int i=0; i<inputData.length; i++) {
					for (int j=0; j<inputData[i].length; j++) {
						System.out.print(inputData[i][j] + ", ");
					}
					System.out.println("");
				}
				*/
				Boolean isProvider = true;
				String idOrder = null;
				String idProvider = null;
				for (int i=0; i<inputData.length; i++) {
					if (!(inputData[i][0] == null && inputData[i][1] == null)) {				
						if (inputData[i][1] == null) {
							if (isProvider) {
								//Obtener id del proveedor
								String[] columns = new String[] {"providers.id_provider"};
								String[] tables = new String[] {"providers"};
								String[][] inputWhere = new String[][] {{"providers.name", "'" + inputData[i][0] + "'"}};
								ResultSet resultSetIdProvider = dao.dbio.select(columns, tables, inputWhere);
								resultSetIdProvider.first();
								idProvider = resultSetIdProvider.getString(1);
								isProvider = false;
							} else {
								//Insertar order y luego seleccionar su id.
								String[][] data = new String[][] {{"orders.date", "'" + inputData[i][0] + "'"}, {"orders.id_provider", "'" + idProvider + "'"}};
								String table = "orders";
								dao.dbio.insert(table, data);
								
								String[] columns = new String[] {"orders.id_order"};
								String[] tables = new String[] {"orders"};
								String[][] inputWhere = new String[][] {{"orders.date", "'" + inputData[i][0] + "'", "AND"}, {"orders.id_provider", "'" + idProvider + "'"}};
								ResultSet resultSetIdOrder = dao.dbio.select(columns, tables, inputWhere);
								resultSetIdOrder.first();
								idOrder = resultSetIdOrder.getString(1);
							}
						} else {
							String[] columns = new String[] {"products.id_product"};
							String[] tables = new String[] {"products"};
							String[][] inputWhere = new String[][] {{"products.spanish_name", "'" + inputData[i][0] + "'"}};
							ResultSet resultSetIdProduct = dao.dbio.select(columns, tables, inputWhere);
							resultSetIdProduct.first();
							String idProduct = resultSetIdProduct.getString(1);
							
							String[][] data2 = new String[][] {{"products_orders_relationship.id_order", "'" + idOrder + "'"}, {"products_orders_relationship.id_product", "'" + idProduct + "'"}, {"products_orders_relationship.ordered_amount", "'" + inputData[i][1] + "'"}, {"products_orders_relationship.received_amount", "0"}};
							String table2 = "products_orders_relationship";
							dao.dbio.insert(table2, data2);
						}
					}
				}
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateDatabase(String[][] inputData) {
		
		//En caso de update plate:
		
		//inputData contiene valores null, null. Descartarlos al principio.
		//inputData contiene en [i][0] el nombre cuando [i][1] sea null.
		//En ese caso "UPDATE plates SET plates.name = inputData[0][0] WHERE plates.name = nameForModifyObject"
		//Y, en caso de selectedItem.equals("ORDERS"):
		//...
		//
		//Cuando el inputData[i][1] no es null: 
		//Primero SELECT plates.id_plate FROM plates WHERE plates.name = nameForModifyObject"
		//****Usar el id_plate para el siguiente SELECT (existentProducts): SELECT products.name, ppr.amount FROM products, ppr, plates WHERE (los id coincidan);
		//****Almacenar el resultado.
		//Generar bucle de insert para todos los resultados en inputData.length:
		//if (inputData[i][0].equals([cualquier resultado en existentProducts[0]]), entonces hacer UPDATE en ppr. Sino, hacer insert en ppr.
		//UPDATE en ppr:
		//	if (!inputData[i][1].equals(([cualquier resultado en existentProducts[1]])), entonces hacer UPDATE: 
		//		UPDATE ppr SET(ppr.amount = "inputData[i][1]) WHERE ppr.id_product = inputData[i][0] AND ppr.id_plate = [id_plate seleccionado fuera el bucle];
		//	else: No hacer nada (se dejó el valor igual que estaba antes).
		//INSERT en ppr:
		//	SELECT products.id_product FROM products WHERE products.spanish_name = inputData[i][0];
		//	**Hacer un select de id_plate para todos los insert, pero un select de id_product para cada insert.
		//	INSERT INTO products_plates_relationship SET VALUES(id_plate = [id plate seleccionado], id_product = [id product seleccionado], amount = inputData[i][1])
		try {
			switch(consultedItem.toUpperCase()) {
			case "PLATES":
				String[] columns1 = new String[] {"plates.id_plate"};
				String[] tables1 = new String[] {"plates"};
				String[][] inputWhere1 = new String[][] {{"plates.name", "'" + nameForModifyObject + "'"}};
				ResultSet resultSetIdPlate = dao.dbio.select(columns1, tables1, inputWhere1);
				resultSetIdPlate.first();
				String idPlate = resultSetIdPlate.getString(1);
				 
				String[] columns2 = new String[] {"products.spanish_name", "products_plates_relationship.amount"};
				String[] tables2 = new String[] {"plates", "products_plates_relationship", "products"};
				String[][] inputWhere2 = new String[][] {{"plates.id_plate", "products_plates_relationship.id_plate", "AND"}, {"products_plates_relationship.id_product", "products.id_product"}};
				ResultSet resultSetExistentProducts = dao.dbio.select(columns2, tables2, inputWhere2);
				int countExistentProducts = 0;
				while (resultSetExistentProducts.next()) {
					countExistentProducts++;
				}
				String[][] existentProducts = new String[countExistentProducts][2];
				resultSetExistentProducts.first();
				for (int j=0; j<existentProducts.length; j++) {
					existentProducts[j][0] = resultSetExistentProducts.getString(1);
					existentProducts[j][1] = resultSetExistentProducts.getString(2);
					resultSetExistentProducts.next();
				}
				
				for (int i=0; i<inputData.length; i++) {
				
					if (!(inputData[i][0] == null && inputData[i][1] == null)) {
	
						if (inputData[i][1] == null) {
							String[][] data = new String[][] {{"plates.name", "'" + inputData[i][0] + "'"}};
							String[] tables = new String[] {"plates"};
							String[][] inputWhere = new String[][] {{"plates.name", "'" + nameForModifyObject + "'"}};
							nameForModifyObject = inputData[i][0];
							dao.dbio.update(data, tables, inputWhere);
						} else {
							Boolean productExistsInThisPlate = false;
							for (int j=0; j<existentProducts.length; j++) {
								System.out.println(inputData[i][0] + " | " + existentProducts[j][0]);
								if (inputData[i][0].equals(existentProducts[j][0])) {
									System.out.println("Entró!");
									productExistsInThisPlate = true;
								}
							}
							
							String[] columns3 = new String[] {"products.id_product"};
							String[] tables3 = new String[] {"products"};
							String[][] inputWhere3 = new String[][] {{"products.spanish_name", "'" + inputData[i][0] +"'"}};
							ResultSet resultSetSelectedProductID = dao.dbio.select(columns3, tables3, inputWhere3);
							resultSetSelectedProductID.first();
							String selectedProductID = resultSetSelectedProductID.getString(1);
							
							if (productExistsInThisPlate) {
								//Update en ppr.
								String[][] data = new String[][] {{"products_plates_relationship.amount", "'" + inputData[i][1] + "'"}};
								String[] tables = new String[] {"products_plates_relationship"};
								String[][] inputWhere = new String[][] {{"products_plates_relationship.id_plate", "'" + idPlate + "'", "AND"}, {"products_plates_relationship.id_product", "'" + selectedProductID + "'"}};
								dao.dbio.update(data, tables, inputWhere);
							} else {
								//Insert en ppr.
								String table = "products_plates_relationship";
								String[][] data = new String[][] {{"products_plates_relationship.id_plate", "'" + idPlate + "'"}, {"products_plates_relationship.id_product", "'" + selectedProductID + "'"}, {"products_plates_relationship.amount", inputData[i][1]}};
								dao.dbio.insert(table, data);
							}
						}
					}
				}
				break;		
			case "ORDERS":
				Boolean isProvider = true;
				String idProvider = null;

				//Select pr.spanish_name, por.ordered_amount WHERE (id equals)
				String[] columnsOrdersProducts = new String[] {"products.spanish_name", "products_orders_relationship.received_amount"};
				String[] tablesOrdersProducts = new String[] {"products", "products_orders_relationship", "orders"};
				String[][] inputWhereOrdersProducts = new String[][] {{"products.id_product", "products_orders_relationship.id_product", "AND"}, {"products_orders_relationship.id_order", "'" + nameForModifyObject + "'"}};
				ResultSet resultSetProductsAndAmounts = dao.dbio.select(columnsOrdersProducts, tablesOrdersProducts, inputWhereOrdersProducts);
				int counterProductsAndAmounts=0;
				while (resultSetProductsAndAmounts.next()) {
					counterProductsAndAmounts++;
				}
				String[][] productsAndAmounts = new String[counterProductsAndAmounts][2];
				resultSetProductsAndAmounts.first();
				for (int i=0; i<productsAndAmounts.length; i++) {
					productsAndAmounts[i][0] = resultSetProductsAndAmounts.getString(1);
					productsAndAmounts[i][1] = resultSetProductsAndAmounts.getString(2);
				}
				
				for (int i=0; i<inputData.length; i++) {
					if (!(inputData[i][0] == null && inputData[i][1] == null)) {
						if (inputData[i][1] == null) {
							if (isProvider) {
								//Select id_provider Where provider_name = inputData[i][0]. Luego update orders.id_provider
								String[] columns = new String[] {"providers.id_provider"};
								String[] tables = new String[] {"providers"};
								String[][] inputWhere = new String[][] {{"providers.name", "'" + inputData[i][0] + "'"}};
								ResultSet resultSetIdProvider = dao.dbio.select(columns, tables, inputWhere);
								resultSetIdProvider.first();
								idProvider = resultSetIdProvider.getString(1);
								
								String[][] data2orders = new String[][] {{"orders.id_provider", "'" + idProvider + "'"}};
								String[] tables2orders = new String[] {"orders"};
								String[][] inputWhere2orders = new String[][] {{"orders.id_order", "'" + nameForModifyObject + "'"}};
								dao.dbio.update(data2orders, tables2orders, inputWhere2orders);
								
								isProvider = false;
							} else {
								//Update orders.date
								String[][] data2ordersDate = new String[][] {{"orders.date", "'" + inputData[i][0] + "'"}};
								String[] tables2ordersDate = new String[] {"orders"};
								String[][] inputWhere2ordersDate = new String[][] {{"orders.id_order", "'" + nameForModifyObject + "'"}};
								dao.dbio.update(data2ordersDate, tables2ordersDate, inputWhere2ordersDate);
							}
						} else {
							Boolean productExists = false;
							Boolean productAmountChange = true;
							for (int j=0; j<productsAndAmounts.length; j++) {
								if (inputData[i][0].equals(productsAndAmounts[j][0])) {
									productExists = true;
									if (inputData[i][1].equals(productsAndAmounts[j][1])) {
										productAmountChange = false;
									}
								}
							}
							//If product exists and amount change, make an update:
							if (productExists && productAmountChange) {		
								//Actualizar por.amount WHERE (ids), products.spanish_name = inputData[i][0], orders.id_order = nameForModifyObject
								String[][] data = new String[][] {{"products_orders_relationship.amount", "'" + inputData[i][1] + "'"}};
								String[] tables = new String[] {"products", "products_orders_relationship"};
								String[][] inputWhere = new String[][] {{"products.id_product", "products_orders_relationship.id_product", "AND"}, {"products_orders_relationship.id_order", "'" + nameForModifyObject + "'", "AND"}, {"products.spanish_name", "'" + inputData[i][0] + "'"}};
								dao.dbio.update(data, tables, inputWhere);
							} else if(!productExists) {	//If product dont exists, make an insert:
								//Get product id from product name
								String[] columns = new String[] {"products.id_product"};
								String[] tables = new String[] {"products"};
								String[][] inputWhere = new String[][] {{"products.spanish_name", "'" + inputData[i][0] + "'"}};
								ResultSet resultSetIdProduct = dao.dbio.select(columns, tables, inputWhere);
								resultSetIdProduct.first();
								String idProduct = resultSetIdProduct.getString(1);
								//Insert new product in products_orders_relationship.
								String[][] data = new String[][] {{"products_orders_relationship.id_order", "'" + nameForModifyObject + "'"}, {"products_orders_relationship.id_product", "'" + idProduct + "'"}, {"products_orders_relationship.ordered_amount", "'" + inputData[i][1] + "'"}, {"products_orders_relationship.received_amount", "'0'"}};
								String table = "products_orders_relationship";
								dao.dbio.insert(table, data);
							}
						}
					}
				}
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
				
	}
	
	private String[] getAllProducts() {
		try {
			String columns[] = new String[] {"products.spanish_name"};
			String tables[] = new String[] {"products"};
			
			ResultSet resultSetProducts = dao.dbio.select(columns, tables, null);
			int counterProducts = 0;
		
			while (resultSetProducts.next()) {
				counterProducts++;
			}
			
			resultSetProducts.first();
			String[] products = new String[counterProducts];
			
			for (int i=0; i<counterProducts; i++) {
				products[i] = resultSetProducts.getString(1);
				resultSetProducts.next();
			}
			
			return products;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private String[][] getSelectedProducts(String nameForModifyObject) {
		try {
			
			String[] columns, tables;
			String[][] inputWhere;
			//Obtener el item seleccionado y el nombre del producto (si no hay resultado es uno nuevo).
			if (consultedItem.equals("PLATES")) {
				columns = new String[] {"products.spanish_name", "products_plates_relationship.amount"};
				tables = new String[] {"products", "products_plates_relationship", "plates"};
				inputWhere = new String[][] {{"products.id_product", "products_plates_relationship.id_product", "AND"}, {"products_plates_relationship.id_plate", "plates.id_plate", "AND"}, {"plates.name", "'" + nameForModifyObject + "'"}};
			} else {
				columns = new String[] {"products.spanish_name", "products_orders_relationship.ordered_amount"};
				tables = new String[] {"products", "products_orders_relationship", "providers", "orders"};
				inputWhere = new String[][] {{"products.id_product", "products_orders_relationship.id_product", "AND"}, {"products_orders_relationship.id_order", "orders.id_order", "AND"}, {"orders.id_provider", "providers.id_provider", "AND"}, {"orders.id_order", "'" + nameForModifyObject + "'"}};
			}
			
			ResultSet resultSetProducts = dao.dbio.select(columns, tables, inputWhere);
			int counterProducts = 0;
		
			while (resultSetProducts.next()) {
				counterProducts++;
			}
			
			resultSetProducts.first();
			String[][] selectedProducts = new String[counterProducts][2];
			
			for (int i=0; i<counterProducts; i++) {
				selectedProducts[i][0] = resultSetProducts.getString(1);
				selectedProducts[i][1] = resultSetProducts.getString(2);
				resultSetProducts.next();
			}
			
			return selectedProducts;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	private String[] getDateFromDatabase(String nameForModifyObject) {
		
		if (nameForModifyObject == null) {
			return null;
		}
		
		String date = "1970-01-01";;
		String providerName = null;
		
		try {
			String[] columns = new String[] {"providers.name", "orders.date"};
			String[] tables = new String[] {"orders", "providers"};
			String[][] inputWhere = new String[][] {{"orders.id_order", "'" + nameForModifyObject + "'", "AND"}, {"orders.id_provider", "providers.id_provider"}};
		
			ResultSet resultSetDate = dao.dbio.select(columns, tables, inputWhere);
			int counterDate = 0;
			
			while (resultSetDate.next()) {
				providerName = resultSetDate.getString(1);
				date = resultSetDate.getString(2);
				counterDate++;
			}
			
			if (counterDate>1) {
				new alertWindow("Error 301. Demasiados resultados para una consulta en pedidos", "Aceptar");
			} else if (counterDate == 0) {
				new alertWindow("Error 302. No se ha podido leer la fecha del pedido", "Aceptar");
			}
		} catch (SQLException e) {
			new alertWindow("Error 404. Ha ocurrido un problema accediendo a la base de datos", "Aceptar");
		}
		
		String[] result = new String[] {providerName, date};
		return result;
	}
	
	public void newComboBox() {		
		//Llamar a este método cuando se completa algo en un comboBox nuevo.
		DoubleInputPanel dobleInputPanel = new DoubleInputPanel(this, products, null, null, new Dimension(380, 30));
		dobleInputPanel.unselectAllComboBoxItems();
		addElements(dobleInputPanel);
		
		updateElementsPosition();
	}
	
	private void addElements(Component component) {
		
		int numberOfButtonsInElements = 0;
		if (!component.getClass().toString().equals("class javax.swing.JButton")) {
			for (int i=0; i<elements.size(); i++) {
				if (elements.get(i).getClass().toString().equals("class javax.swing.JButton")) {
					numberOfButtonsInElements++;
				}
			}
		}
		elements.add((elements.size()-numberOfButtonsInElements), component);
		panel.add(component);
		
	}
	
	public void updateElementsPosition() {
		int buttonCount = 0;
		for (int i=0; i<elements.size(); i++) {
			if (elements.get(i).getClass().toString().equals("class javax.swing.JButton")) {
				elements.get(i).setBounds(((buttonCount*170)+10), (((elements.size()-2)*40)+10), 160, 30);
				buttonCount++;
			} else {
				elements.get(i).setBounds(0, ((i*40)+10), 380, 30);
			}
		}
		this.setSize(380, (40*(elements.size())+10));
	}
	
	private Boolean isNumber(String text) {
		try {
			Integer.parseInt(text);
			return true;
		} catch (NumberFormatException e) {
			System.out.println("Se esperaba un número y se obtuvo " + text);
			return false;
		}
	}
	
	private void disposeWindow() {
		this.dispose();
	}
	
	
}
