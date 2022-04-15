/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.sql.Timestamp;
import java.util.ArrayList;
import org.cloudbus.cloudsim.core.*;

public class Block {
    public ArrayList<Transaction> transactions;
    private int blockSize=100;
    private Timestamp blockCreationTime;
  //  private int validatingEntityID;
  //  private String validatingEntitySignature;

    public Block(){
        this.transactions =new ArrayList<Transaction>();
        //this.blockSize = blockSize;
        this.blockCreationTime = new Timestamp(System.currentTimeMillis());
    }
    
     public Block(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        //this.blockSize = blockSize;
        this.blockCreationTime = new Timestamp(System.currentTimeMillis());
     //   this.validatingEntityID = validatingEntityID;
    }
    
    
public void addTransaction(Transaction trans){
  this.transactions.add(trans);
}

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public Timestamp getBlockCreationTime() {
        return blockCreationTime;
    }

}
