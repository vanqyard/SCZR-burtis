package burtis.modules.gui.simpleview;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import burtis.common.mockups.Mockup;
import burtis.common.mockups.MockupBus;
import burtis.common.mockups.MockupBusStop;
import burtis.common.mockups.MockupPassenger;
import burtis.modules.gui.controller.ActionExecutor;
import burtis.modules.gui.controller.Controller;
import burtis.modules.gui.events.ProgramEvent;
import burtis.modules.gui.view.View;
import burtis.modules.network.ModuleConfig;
import burtis.modules.network.NetworkConfig;
import burtis.modules.network.client.ClientModule;

/**
 * Runs GUI with some example data. No network connection.
 * 
 * @author vanqyard
 */
public class GuiTest
{
    static LinkedBlockingQueue<ProgramEvent> bQueue;
    static SimpleView view;
    static Controller controller;

    public static void main(String[] args)
    {
        bQueue = new LinkedBlockingQueue<ProgramEvent>();
        view = new SimpleView(bQueue);
        
        //NetworkConfig netConfig = NetworkConfig.defaultConfig();
        //ModuleConfig config = netConfig.getModuleConfigs().get(NetworkConfig.GUI_MODULE);
        
        //controller = new Controller(view, bQueue, new ActionExecutor(
        //        new ClientModule(config), netConfig));
        
        controller = new Controller(view, bQueue, null);
        
        try
        {
            ArrayList<MockupBus> buses = new ArrayList<MockupBus>();
            ArrayList<MockupBusStop> schedule = new ArrayList<MockupBusStop>();
            ArrayList<MockupPassenger> passengerList = new ArrayList<MockupPassenger>();
            
            for(int i=0; i!=6; i++)
                passengerList.add(new MockupPassenger(i, "Wloclawek" + i, "Wloclawek" + i+1));
                
            for(int i=0; i!=6; i++) {
                MockupBus tmp = new MockupBus(i, 90 );
                tmp.setPassengerList(passengerList);
                buses.add(tmp);
            }
            
            for(int i=0; i!=6; i++) {
                MockupBusStop tmp = new MockupBusStop("Wloclawek" + i);
                tmp.setPassengerList(passengerList);
                schedule.add(tmp);
            }
            
            long currentTime = 360000002;
            
            Mockup mockup = new Mockup(buses, schedule, currentTime);
            view.refresh(mockup);
        }
        catch (Exception e)
        {
            System.out.println("Fatal error, closing application ... ");
            e.printStackTrace();
            System.exit(1);
        }
        controller.start();
    }
}