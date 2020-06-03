package listeners;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import mainWindow.content;

//Class for listen the event for table change.
public class tableChangesListener implements ListSelectionListener {

	content invokerClass;
	
	public tableChangesListener(content invokerClass) {
		
		this.invokerClass = invokerClass;
		
	}
	
	//On valueChanged event make buttons visible or invisible.
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (!event.getValueIsAdjusting()) {
			
			if (invokerClass.getTable().getSelectedRow() != -1 && invokerClass.getTable().getSelectedRow() != 0) {
				invokerClass.changeButtonNewOrModifyText(true);
				invokerClass.setButtonDeleteVisibility(true);
				if (invokerClass.getConsultedItem().toUpperCase().equals("ORDERS")) {
					invokerClass.setButtonConfirmOrderReceivedVisibility(true);
				}
			} else {
				invokerClass.changeButtonNewOrModifyText(false);
				invokerClass.setButtonDeleteVisibility(false);
				if (invokerClass.getConsultedItem().toUpperCase().equals("ORDERS")) {
					invokerClass.setButtonConfirmOrderReceivedVisibility(false);
				}
			}
			
		}
		
	}

}
