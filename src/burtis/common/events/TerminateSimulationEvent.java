/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package burtis.common.events;

/**
 *
 * @author Mikołaj Sowiński <mikolaj.sowinski@gmail.com>
 */
public class TerminateSimulationEvent extends SimulationEvent {

    public TerminateSimulationEvent(String sender) {
        super(sender);
    }
    
}
