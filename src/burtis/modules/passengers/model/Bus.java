package burtis.modules.passengers.model;

import java.util.ArrayList;
import java.util.List;

import burtis.common.constants.SimulationModuleConsts;
import burtis.modules.passengers.Managers;

/**
 * Representation of bus in passengers module.
 * 
 * @author Mikołaj Sowiński
 */
public class Bus
{
    private final int id;
    /**
     * List of passengers traveling with this bus.
     */
    private final List<Passenger> passengers = new ArrayList<>();
    /**
     * Reference to the next bus stop requested by passengers.
     */
    private BusStop requestedBusStop;
    /**
     * Current bus state.
     */
    private State state;

    public Bus(int id, Managers managers)
    {
        this.id = id;
        // this.managers = managers;
    }

    /**
     * Changes state of the bus to {@link State#ATBUSSTOP}.
     */
    public void arrive()
    {
        state = State.ATBUSSTOP;
    }

    /**
     * Order bus to depart from the bus stop.
     * 
     * Changes state of the bus to {@link State#RUNNING} and sets field
     * {@link Bus#requestedBusStop}.
     */
    public void depart()
    {
        requestedBusStop = getNearestRequestedBusStop();
        state = State.RUNNING;
    }

    /**
     * Returns number of free places in the bus.
     * 
     * @return number of free places
     */
    public int getFreePlaces()
    {
        return SimulationModuleConsts.BUS_CAPACITY - passengers.size();
    }

    public int getId()
    {
        return id;
    }

    public List<Passenger> getPassengers()
    {
        return passengers;
    }

    public BusStop getRequestedBusStop()
    {
        return requestedBusStop;
    }

    public State getState()
    {
        return state;
    }

    @Override
    public String toString()
    {
        String passengersString = "";
        for (Passenger passenger : passengers)
        {
            passengersString += passenger.getId() + " ";
        }
        return "Bus: " + id + " passengers: " + passengersString + "\n";
    }

    /**
     * Changes state of the bus to {@link State#WAITING}.
     */
    public void waiting()
    {
        state = State.WAITING;
    }

    /**
     * Gets nearest bus stop requested by the passengers.
     * 
     * @return nearest requested {@link BusStop}
     */
    private BusStop getNearestRequestedBusStop()
    {
        BusStop nearestRequestedBusStop = null;
        if (passengers.size() > 0)
        {
            nearestRequestedBusStop = passengers.get(0).getDestination();
        }
        if (passengers.size() > 1)
        {
            for (int i = 1; i < passengers.size(); i++)
            {
                if (passengers.get(i).getDestination().getPosition() < nearestRequestedBusStop
                        .getPosition())
                {
                    nearestRequestedBusStop = passengers.get(i)
                            .getDestination();
                }
            }
        }
        return nearestRequestedBusStop;
    }

    /**
     * Removes all passengers from the bus (killing them!) and puts bus into
     * {@link State#RUNNING}.
     */
    void resetBus()
    {
        passengers.clear();
        state = State.RUNNING;
    }

    /**
     * Bus states.
     */
    private enum State
    {
        /**
         * When at the bus stop.
         */
        ATBUSSTOP,
        /**
         * When in between bus stops.
         */
        RUNNING,
        /**
         * When waiting in the queue at the bus stop.
         */
        WAITING
    }
}
