package burtis.common.events;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The abstract superclass of all events being sent between modules during
 * simulation, representing all kinds of communication. Each event describes at
 * least two fields: the sender and list of recipients.
 * 
 * @author Amadeusz Sadowski
 *
 */
public abstract class SimulationEvent implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final String sender;
    private final String[] recipients;

    public SimulationEvent(String sender)
    {
        this(sender, new String[0]);
    }

    public SimulationEvent(String sender, String[] recipients)
    {
        this.sender = sender;
        this.recipients = recipients;
    }

    public String sender()
    {
        return sender;
    }

    public List<String> getRecipients()
    {
        List<String> result = new ArrayList<String>(recipients.length);
        for (String recipient : recipients)
        {
            result.add(recipient);
        }
        return result;
    }

    public abstract void visit(AbstractEventHandler eventProcessor);
}
