/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.util.ArrayList;
import org.cloudbus.cloudsim.core.*;

public class RatingControl {
private Blockchain blockchain;
public int ratingLimit;//this to check if an entity is inflating the rating of another entity (self-promotion, bad-mouthing)
public int commonRaters;//the limit of number of common rater to classify as malicious cluster
  
    public RatingControl(Blockchain blockchain) {
        this.blockchain = blockchain;
        //this.ratingLimit=5;
    }


public boolean checkSelfPromotion(int raterID,int ratedID){
    int ratingCount=blockchain.getPreviousRatingCountbyRater(raterID, ratedID);
    if(ratingCount >= ratingLimit){
        System.out.println("Self-promoting mitigation scheme activated as enitiy passed the rating limit= "+ratingLimit);
        return true;
    }else{
       return false; 
    }
}


public boolean checkClusterSelfPromotion(int raterID,int ratedID){
    int ratingCount=blockchain.getPreviousRatingCountbyRater(raterID, ratedID);
    
    if(blockchain.getCommonRaters(raterID, ratedID).size() >= commonRaters){
        System.out.println("Cluster verification scheme activated as enitiy "+raterID+" and Entity "+ratedID+" has more than "+blockchain.getCommonRaters(raterID, ratedID).size()+" common raters");
        return true;
    }else{
       return false; 
    }
}


public Transaction addRating(int raterEntityID,int ratedEntityID,int ratingValue,int validtingEntityID){  
double trustScore=blockchain.getLastTrustScore(ratedEntityID);
int ratingCount=blockchain.getAllRatingCount(ratedEntityID);
double newTrustScore=trustScore+((ratingValue-trustScore)/(ratingCount+1));
//System.out.println("new value "+newTrustScore);
Transaction new_transaction= new Transaction(raterEntityID,ratedEntityID,ratingValue,validtingEntityID,newTrustScore);
return new_transaction;
}

public Transaction addControledRating(int raterEntityID,int ratedEntityID,int ratingValue,int validtingEntityID){
double trustScore=blockchain.getLastTrustScore(ratedEntityID);
int ratingCount=blockchain.getAllRatingCount(ratedEntityID);
int ratingCountbyThisRater=blockchain.getPreviousRatingCountbyRater(raterEntityID,ratedEntityID);
//System.out.println("Rold="+trustScore+" Rycnt="+ratingCountbyThisRater+" Rcnt="+ratingCount);
double newTrustScore=trustScore+( ((ratingValue-trustScore)/(ratingCountbyThisRater+1))/(ratingCount+1) );
//double newTrustScore=((trustScore*ratingCount*ratingCountbyThisRater)+ratingValue)/(ratingCount+1);
Transaction new_transaction= new Transaction(raterEntityID,ratedEntityID,ratingValue,validtingEntityID,newTrustScore);
return new_transaction;
}


public ArrayList verifyMaliciousCluster(ArrayList<Integer> Cluster){
   ArrayList maliciousCluster = new ArrayList();
   //System.out.println("cluster "+Cluster);
   for(int i:Cluster){
     //  System.out.println("Entity "+i+" incluster="+blockchain.inClusterRatingCount(i, Cluster));
      // System.out.println("Entity "+i+" outcluster="+blockchain.outClusterRatingCount(i, Cluster));
       if(blockchain.inClusterRatingCount(i, Cluster) > blockchain.outClusterRatingCount(i, Cluster)){
        maliciousCluster.add(i);
       }
   }
   
   return maliciousCluster;
}

    public void setRatingLimit(int ratingLimit) {
        this.ratingLimit = ratingLimit;
    }

}
