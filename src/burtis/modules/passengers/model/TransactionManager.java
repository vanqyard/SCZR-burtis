package burtis.modules.passengers.model;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import burtis.modules.passengers.Managers;

/**
 * Transaction manager.
 * 
 * @author Mikołaj Sowiński
 *
 */
public class TransactionManager
{
    /**
     * List of active transactions.
     */
    private final List<Transaction> transactions = new LinkedList<>();

    /**
     * Reference to managers.
     */
    private final Managers managers;
    
    /**
     * Constructor.
     * 
     * @param managers
     */
    public TransactionManager(Managers managers) {
        this.managers = managers;
        this.managers.setTransactionManager(this);       
    }
    
    /**
     * Ticks all transactions, departs buses and removes finished transactions.
     */
    public void tickTransactions() {
        
        for(Transaction transaction : transactions) {
            
            managers.getLogger().info(transaction.toString());
            
            transaction.nextIteration();
            
            if(transaction.isFinished()) {
                transaction.getBus().depart();
                transaction.getBusStop().departBus();
            }
        }
                
        removeFinishedTransactions();
        
    }
    
    /**
     * Removes finished transactions.
     * @param transactions list of transactions
     */
    public void removeFinishedTransactions() {
        
        for(Transaction transaction : transactions) {
            if(transaction.isFinished()) {
                managers.getLogger().info("TREM: " + transaction);
                transactions.remove(transaction);
            }
        }
        
    }
    
    /**
     * Adds new transaction to the transactions list.
     * 
     * @param transaction transaction to be added
     */
    public void addTransaction(Transaction transaction) {
        managers.getLogger().info("ADD: " + transaction);
        transactions.add(transaction);
    }
    
}
