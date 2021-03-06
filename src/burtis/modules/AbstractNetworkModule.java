package burtis.modules;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import burtis.common.events.AbstractEventHandler;
import burtis.common.events.SimulationEvent;
import burtis.modules.network.ModuleConfig;
import burtis.modules.network.client.ClientModule;

/**
 * Template class for creating network-connected modules.
 * 
 * It provides main method, so in your main method you can just write:<br>
 * <code>
 * MyImplementation app = new MyImplementation();<br>
 * app.main();<br>
 * </code>
 * 
 * @author Amadeusz Sadowski
 *
 */
public abstract class AbstractNetworkModule
{
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ExecutorService listenerExecutor = Executors
            .newSingleThreadExecutor();
    /**
     * Controls listener loop.
     */
    private boolean isRunning = true;
    /**
     * Controls loop checking for input.
     */
    private boolean isShutdownDemanded = false;
    private final BlockingQueue<SimulationEvent> queue;
    protected final ClientModule client;
    /**
     * Must return handler for your implementation, before {@link #main} call.
     */
    protected AbstractEventHandler eventHandler = null;
    protected final ModuleConfig moduleConfig;

    /**
     * Creates module ready to have {@link #main()} called.
     * 
     * @param config
     *            - the configuration to be used for connection.
     */
    protected AbstractNetworkModule(ModuleConfig config)
    {
        this.moduleConfig = config;
        client = new ClientModule(moduleConfig);
        queue = client.getIncomingQueue();
        logger.info("Created " + moduleConfig.getModuleName());
    }

    /**
     * Interrupts main method. In effect, {@link #terminate()} will be called.
     */
    public void shutdown()
    {
        logger.info("Shutting down " + moduleConfig.getModuleName());
        isShutdownDemanded = true;
    }

    public void send(SimulationEvent event)
    {
        client.send(event);
    }

    private boolean checkInputAndSleep()
    {
        try
        {
            Thread.sleep(500);
            return System.in.available() != 0;
        }
        catch (Exception e)
        {
            logger.log(Level.FINE, null, e);
        }
        return false;
    }

    protected void closeModule()
    {
        logger.info("Closing resources of " + moduleConfig.getModuleName());
        client.close();
        isRunning = false;
        listenerExecutor.shutdownNow();
    }

    protected void initializeModule() throws IOException
    {
        initializeModule(true);
    }

    protected void initializeModule(boolean startConnection) throws IOException
    {
        logger.info("Initializing resources in " + moduleConfig.getModuleName());
        listenerExecutor.execute(this::listenOnClient);
        if (startConnection)
        {
            client.connect();
        }
    }

    private void listenOnClient()
    {
        logger.info("Listening started.");
        while (isRunning)
        {
            try
            {
                SimulationEvent event = queue.take();
                event.visit(eventHandler);
            }
            catch (InterruptedException e)
            {
                logger.warning("Listening interrupted!");
            }
        }
        logger.info("Listening finished.");
    }

    /**
     * Called at the beginning of {@link #main}. The module is already connected
     * and {@link #send(SimulationEvent)} works.
     */
    protected abstract void init();

    /**
     * IMPORTANT: set protected field {@link #eventHandler} before calling this
     * method.
     * 
     * It provides main method, so in your main method you can just write:<br>
     * <code>
     * MyImplementation app = new MyImplementation();<br>
     * app.main();<br>
     * </code>
     * 
     * It first initializes this base class, then calls {@link #init()}. After
     * console gets any input, {@link #terminate()} is called, after which this
     * base class terminates itself and main returns.
     */
    protected void main()
    {
        try
        {
            initializeModule();
            init();
        }
        catch (IOException ex)
        {
            logger.log(Level.SEVERE, "Błąd inicjalizacji.");
            terminate();
            closeModule();
            return;
        }
        System.out.println("Naciśnij enter any zakończyć.");
        boolean isInputAvailable = false;
        while (!isInputAvailable && !isShutdownDemanded)
        {
            isInputAvailable = checkInputAndSleep();
        }
        terminate();
        closeModule();
    }

    /**
     * Called automatically at the end of {@link #main}. It's guaranteed to be
     * called. Use it to release used resources.
     * 
     * It doesn't interrupt {@link #main()}. Don't call this manually, if using
     * 'main'.
     */
    protected abstract void terminate();
}
