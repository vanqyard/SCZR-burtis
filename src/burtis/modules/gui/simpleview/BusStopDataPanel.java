package burtis.modules.gui.simpleview;

import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import burtis.common.mockups.MockupPassenger;

class BusStopDataPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private JTable table;
    private ArrayList<MockupPassenger> passengerList;
    private JLabel title = new JLabel();
    private JLabel busStop = new JLabel();
    String[] columnNames = { "Id", "Depot", "Destination" };
    Object[][] data = new Object[30][3];

    public BusStopDataPanel()
    {
        setLayout(new BorderLayout());
        add(title, BorderLayout.PAGE_START);
        table = new JTable(data, columnNames);
        table.setEnabled(false);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

	public void setCurrentBusStop(String s) {
	    this.passengerList = passengerList;
	    title.setText("Bus Stop Name: " + s);
	}
	
	public void setCurrentBus(Integer i) {
	    this.passengerList = passengerList;
		title.setText("Bus Id: " + i.toString());
	}
	
	public void setCurrentBusStop(String s, ArrayList<MockupPassenger> passengerList) {
	    this.passengerList = passengerList;
	    
	    title.setText("Bus Stop Name: " + s);
	    int count = 0;
	    
	    if(passengerList == null)
	        System.out.println("passengerList is null");
	    
	    for(MockupPassenger mp : passengerList) {
	        table.setValueAt(mp.getId(), count , 0);
	        table.setValueAt(mp.getDepot(), count , 1);
	        table.setValueAt(mp.getDestination(), count , 2);
	        
	        count++;
	    }
	}
	
	public void setCurrentBus(Integer i, String currentBusStop, ArrayList<MockupPassenger> passengerList) {
	    this.passengerList = passengerList;
	    title.setText("Bus Id: " + i.toString());
	    busStop.setText(currentBusStop);
	    int count = 0;
	    
	    for(MockupPassenger mp : passengerList) {
	        table.setValueAt(mp.getId(), count , 0);
	        table.setValueAt(mp.getDepot(), count , 1);
	        table.setValueAt(mp.getDestination(), count , 2);
	        
	        count++;
	        //System.out.println(mp.getId() + ", " + mp.getDepot() + ", " + mp.getDestination());   
	    }
	}
}
