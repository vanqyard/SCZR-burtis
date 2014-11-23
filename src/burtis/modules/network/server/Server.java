package burtis.modules.network.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import order.ServerOrder;
import burtis.common.events.EventProcessor;
import burtis.common.events.SimulationEvent;
import burtis.common.events.TerminateSimulationEvent;
import burtis.modules.network.ModuleConfig;
import burtis.modules.network.NetworkConfig;
import burtis.modules.network.server.impl.ServerSender;

/**
 * Performs traffic forwarding according to configuration provided. All traffic
 * from given module is forwarded to all recipients of that module, except of
 * {@link ServerOrder}s which are executed and not forwarded.
 * 
 * Each connection listens on separate thread for incoming traffic. All outgoing
 * traffic is sent through another thread with blocking queue.
 * 
 * @author Amadeusz Sadowski
 *
 */
public class Server extends EventProcessor
{
    private final static Logger logger = Logger.getLogger(Server.class
            .getName());
    private final ServerSender sender = new ServerSender();
    private final Collection<ModuleConnection> moduleConnections;
    private final Map<String, ModuleConnection> moduleMap = new HashMap<>();

    public Server(final NetworkConfig netConfig)
    {
        final Collection<ModuleConfig> configs = netConfig.getModuleConfigs();
        this.moduleConnections = new ArrayList<>(configs.size());
        final ModuleConnectionFactory factory = new ModuleConnectionFactory(
                moduleConnections, moduleMap, this::receive);
        factory.readConfig(netConfig);
    }

    public void run()
    {
        logger.log(Level.INFO, "Server preparing to run");
        sender.startSending();
        for (ModuleConnection moduleConnection : moduleConnections)
        {
            moduleConnection.connect();
        }
        logger.log(Level.INFO, "Server running");
    }

    public void stop()
    {
        logger.log(Level.INFO, "Server stopping...");
        for (ModuleConnection moduleConnection : moduleConnections)
        {
            moduleConnection.close();
        }
        sender.stopSending();
        logger.log(Level.INFO, "Server stopped");
    }

    private void receive(Object receivedObject)
    {
        if (receivedObject instanceof SimulationEvent)
        {
            SimulationEvent event = (SimulationEvent) receivedObject;
            logger.finer("Przesyłam dalej obiekt " + event.getClass().getName());
            process(event);
        }
        else
        {
            logger.warning("Odebrałem nieprawidłowy obiekt: "
                    + receivedObject.getClass().getName());
        }
    }

    @Override
    public void process(SimulationEvent event)
    {
        for (String recipientName : event.getRecipients())
        {
            forward(event, recipientName);
        }
    }

    @Override
    public void process(TerminateSimulationEvent event)
    {
        for (ModuleConnection moduleConnection : moduleConnections)
        {
            sender.send(event, moduleConnection);
        }
        stop();
    }

    private void forward(SimulationEvent event, String recipientName)
    {
        if (moduleMap.containsKey(recipientName))
        {
            sender.send(event, moduleMap.get(recipientName));
        }
        else
        {
            logger.warning("Brak takiego modułu - nie można przesłać zdarzenia do "
                    + recipientName);
        }
    }
}
