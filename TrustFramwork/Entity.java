/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.util.ArrayList;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.*;
/**
 * @author 
 */


public class Entity extends SimEntity{
    public int id;
    public double trustScore;
    public boolean pos;
    Blockchain BC;
    
    public Entity(int id, Double trustScore, boolean pos) {
        super(Integer.toString(id));
        this.id = id;
        this.trustScore = trustScore;
        this.pos = pos; 
    }
    
    public ArrayList<Transaction> ratingInflation(int entityID,int ratingCount){
     ArrayList<Transaction> trans=new ArrayList<Transaction>();       
     for(int i=0;i<=ratingCount;i++){
      trans.add(new Transaction(this.id,entityID,10,-1,0));
     }
        return trans;
    }
    
    public ArrayList<Transaction> unfairRating(int entityID,int ratingCount){
     ArrayList<Transaction> trans=new ArrayList<Transaction>();       
     for(int i=0;i<=ratingCount;i++){
      trans.add(new Transaction(this.id,entityID,0,-1,0));
     }
        return trans;
    }
    
    public Transaction rateEntity(int entityID,int ratingValue){
     Transaction trans=new Transaction(this.id,entityID,ratingValue,-1,0);
     return trans;
    }
    
    public Transaction validateTransaction(Transaction trans,Blockchain BC){
        Transaction validatedTrans = null;

        trans.setValidtingEntityID(this.id);
        RatingControl control=BC.control;
        if(control.checkSelfPromotion(trans.getRaterEntityID(),trans.getRatedEntityID()) ){
        validatedTrans=control.addControledRating(trans.getRaterEntityID(),trans.getRatedEntityID(), trans.getRatingValue(), this.id);
        }else{
        validatedTrans=control.addRating(trans.getRaterEntityID(),trans.getRatedEntityID(), trans.getRatingValue(), this.id);
        }

        if(control.checkClusterSelfPromotion(trans.getRaterEntityID(), trans.getRatedEntityID())){
          validatedTrans.validtingEntityID=-1;//suspected cluster //to be verified in addtransaction procedure 
        }
        
       // System.out.println(validatedTrans);
        return validatedTrans;
    }

    
    
    public double addTransaction(Blockchain BC,Transaction trans){
    
    if(trans.validtingEntityID==-1){//if validting entity ID=-1 means that there is suspected cluster //check valdate transaction
    return -1;    
    }else{
     Block lastBlock=BC.getLastBlock();
    lastBlock.addTransaction(trans);
    if(lastBlock.transactions.size()==lastBlock.getBlockSize()){//the the block reached the blockSize
      Block newBlock=new Block();
        BC.addBlock(newBlock);
    }
    return trans.getNewTrustScore();
    }   
    }
    

    
    public void updateTrustScore(double newTrustScore){
     this.trustScore=newTrustScore;
    }
    
    
    
   
    @Override
    public String toString() {
        return "Entity{" + "id=" + id + ", trustScore=" + trustScore + ", pos=" + pos + '}';
    }

    
    @Override
    public void startEntity() {
        System.out.println("Simulator: Entity "+this.id+" is running");
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void processEvent(SimEvent ev) {
        System.out.println("Hi from entity "+id+" processevent entity "+ev.getSource());
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double processEvent(RatingEvent ev) {
        
        this.BC=ev.BC;
        Transaction validatedTrans=validateTransaction(ev.trans,BC);
        double newScore=addTransaction(ev.BC,validatedTrans);
        System.out.println("This is entity "+id+" after ProcessRatingEvent from entity "+ev.getSource()+" the rating is "+ev.trans.getRatingValue()+", and the new trustScore of entity "+ev.trans.getRatedEntityID()+" is "+newScore);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    return newScore;
    }
    
    @Override
    public void shutdownEntity() {
        System.out.println("Simulator: Entity "+this.id+" had stopped");
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entity other = (Entity) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
  
}
