/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.sql.Timestamp;  
import java.time.Instant;  
import org.cloudbus.cloudsim.core.*;

public class Transaction {
//private int ID;    
private int raterEntityID;
private int ratedEntityID;
private int ratingValue;
private Timestamp ratingTime;
public int validtingEntityID;
//private String validatingEntitySignature;
private double newTrustScore;

    public Transaction(int raterEntityID, int ratedEntityID, int ratingValue, int validtingEntityID,double newTrustScore) {
        //this.ID=ID;
        this.raterEntityID = raterEntityID;
        this.ratedEntityID = ratedEntityID;
        this.ratingValue = ratingValue;
        this.validtingEntityID = validtingEntityID;
        //this.validatingEntitySignature = validatingEntitySignature;
        this.newTrustScore = newTrustScore;
        this.ratingTime=new Timestamp(System.currentTimeMillis());
    }

    public void setNewTrustScore(double newTrustScore) {
        this.newTrustScore = newTrustScore;
    }

    public double getNewTrustScore() {
        return newTrustScore;
    }

    public int getRaterEntityID() {
        return raterEntityID;
    }

    public int getRatedEntityID() {
        return ratedEntityID;
    }

    public int getRatingValue() {
        return ratingValue;
    }

    public Timestamp getRatingTime() {
        return ratingTime;
    }

    public void setRaterEntityID(int raterEntityID) {
        this.raterEntityID = raterEntityID;
    }

    public void setRatedEntityID(int ratedEntityID) {
        this.ratedEntityID = ratedEntityID;
    }

    public void setRatingValue(int ratingValue) {
        this.ratingValue = ratingValue;
    }

    public void setRatingTime(Timestamp ratingTime) {
        this.ratingTime = ratingTime;
    }

    public void setValidtingEntityID(int validtingEntityID) {
        this.validtingEntityID = validtingEntityID;
    }

    @Override
    public String toString() {
        return "Transaction{" + "raterEntityID=" + raterEntityID + ", ratedEntityID=" + ratedEntityID + ", ratingValue=" + ratingValue + ", ratingTime=" + ratingTime + ", validtingEntityID=" + validtingEntityID + ", newTrustScore=" + newTrustScore + '}';
    }

}
