/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.cloudbus.cloudsim.core.*;



public class Blockchain {
    private ArrayList<Block> blocks;
    public RatingControl control;
    public Blockchain() {
        this.blocks=new ArrayList();
    }

    //
    public double getLastTrustScore(int entityID){
        int blocks_count=blocks.size();
    for (int i = blocks.size() - 1 ; i >= 0 ; i--) {
         Block currentBlock=blocks.get(i);
         for (int j = currentBlock.transactions.size() - 1 ; j >= 0 ; j--) {
             if(currentBlock.transactions.get(j).getRatedEntityID()== entityID){
             return currentBlock.transactions.get(j).getNewTrustScore();    
             }
         }
    }
    return 0;
}
    //this method returns the number of ratings given to this node
    public int getAllRatingCount(int entityID){
        int blocks_count=blocks.size();
        int ratingCount=0;
    for (int i = blocks.size()-1 ; i >= 0 ; i--) {
         Block currentBlock=blocks.get(i);
         for (int j = currentBlock.transactions.size() - 1 ; j >= 0 ; j--) {
             if(currentBlock.transactions.get(j).getRatedEntityID()== entityID){
             ratingCount++;    
             }
         }
    }
    return ratingCount;
}
    //this return the number of ratings given by this node
    public int getRaterCount(int entityID){
        int blocks_count=blocks.size();
        int ratingCount=0;
    for (int i = blocks.size() - 1 ; i >= 0 ; i--) {
         Block currentBlock=blocks.get(i);
         for (int j = currentBlock.transactions.size() - 1 ; j >= 0 ; j--) {
             if(currentBlock.transactions.get(j).getRaterEntityID()== entityID){
             ratingCount++;    
             }
         }
    }
    return ratingCount;
}
    
    
    public int getPreviousRatingCountbyRater(int raterEntityID,int ratedEntityID){    
      int blocks_count=blocks.size();
        int ratingCount=0;
    for (int i = blocks.size() - 1 ; i >= 0 ; i--) {
         Block currentBlock=blocks.get(i);
         for (int j = currentBlock.transactions.size() - 1 ; j >= 0 ; j--) {
             if((currentBlock.transactions.get(j).getRatedEntityID() == ratedEntityID)&&
                (currentBlock.transactions.get(j).getRaterEntityID() == raterEntityID)) {
             ratingCount++;    
             }
         }
    }  
      return ratingCount;  
    }

    
    public ArrayList getRatersFromLedger(int entityID){
    ArrayList ratersList=new ArrayList();
    
    int blocks_count=blocks.size();
        int raterID;
    for (int i = blocks.size() - 1 ; i >= 0 ; i--) {
         Block currentBlock=blocks.get(i);
         for (int j = currentBlock.transactions.size() - 1 ; j >= 0 ; j--) {
             if(currentBlock.transactions.get(j).getRatedEntityID()== entityID){
                 raterID=currentBlock.transactions.get(j).getRaterEntityID();
                  //System.out.println("raterID= "+raterID);
                 if( (raterID != 0)&&(!ratersList.contains(raterID)) ){
                  ratersList.add(raterID);   
                 }
             }
         }
    }
    
    return ratersList;
}
    
    
    //
    public ArrayList getAncestryRaters(int entityID,int depth){
    ArrayList<Integer> ratersList=new ArrayList();
    ArrayList<Integer> tempraters=new ArrayList();
    ArrayList<Integer> nextLevelRaters=new ArrayList<Integer>();
    
    tempraters.add(entityID);
    for(int i=0;i<=depth;i++){
        for(int raterID:tempraters){
            if(!ratersList.contains(raterID)){
              ratersList.add(raterID);
            }
            if(tempraters.contains(raterID)){
             nextLevelRaters.addAll(getRatersFromLedger(raterID));   
            }
        }
        tempraters.addAll(nextLevelRaters);
        nextLevelRaters.clear();
    }
    
    return ratersList;
}
    
    public ArrayList getCommonRaters(int entity1,int entity2){
    HashSet commonRatersSet=new HashSet();   
    commonRatersSet.addAll(getRatersFromLedger(entity1));
    commonRatersSet.retainAll(getRatersFromLedger(entity2));
     return new ArrayList<Entity>(commonRatersSet);
    }
    
    
    
    
    public int inClusterRatingCount(int entityID,List<Integer> cluster){
    int inCusterCount=0;
    for(int i:cluster){
       //System.out.println(entityID+"has "+getPreviousRatingCountbyRater(entityID,i)+" rating to "+i); 
       inCusterCount=inCusterCount+getPreviousRatingCountbyRater(entityID,i);
    }
     return inCusterCount;
    }
    
    public int outClusterRatingCount(int entityID,List<Integer> cluster){
    int outCusterCount=0;
    outCusterCount=getRaterCount(entityID)-inClusterRatingCount(entityID,cluster);
     return outCusterCount;
    }
    
    public Block getLastBlock(){
    return this.blocks.get(this.blocks.size()-1); 
    }
    
    
    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    public void addBlock(Block newBlock){
        this.blocks.add(newBlock);
    }
    
    
}
