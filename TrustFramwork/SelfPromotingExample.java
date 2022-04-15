/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 *
 * @author 
 */
public class SelfPromotingExample {

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) throws InterruptedException {
 
//initlize the simulation parameterss    
int num_user = 1;
Calendar calendar = Calendar.getInstance();
boolean trace_flag = false;
CloudSim.init(num_user, calendar, trace_flag);



//create an object of the blockchain class and ratingControl class
Blockchain BC=new Blockchain();
RatingControl control=new RatingControl(BC);
BC.control=control;
//simulated entities list, and PoS list
ArrayList<Entity>  allEntities=new ArrayList<Entity>();//this store all the entities currently running
ArrayList<Entity>  posCommittee=new ArrayList<Entity>();//this will dynamically store the ids of pos commttee nodes        
//initialize the simulated entities
int simulatedEntitiesCount=100;//this is the number of simulated entites
Block firstblock=new Block(new ArrayList<Transaction>());
 //initilize the first entity as a PoS entity to start the system running
       Double PoSthreshold=9.0;
       Entity firstPosNode=new Entity(1,10.0,true);//the first PoS entity
       allEntities.add(firstPosNode);
       posCommittee.add(firstPosNode);
       Transaction tempTrans=new Transaction(0,1,10,1,10.0);
       firstblock.addTransaction(tempTrans);
       CloudSim.addEntity(firstPosNode);
//
Entity newEntity;
Double intial_trust_score=5.0;//the initial trust score that all the entities will have
       for(int i=2;i<=simulatedEntitiesCount;i++){
           newEntity=new Entity(i,intial_trust_score,false);
       CloudSim.addEntity(newEntity);
       allEntities.add(newEntity);
       tempTrans=new Transaction(0,i,intial_trust_score.intValue(),firstPosNode.id,intial_trust_score);
       firstblock.addTransaction(tempTrans);
       //System.out.println(tempTrans);    
       }
       BC.addBlock(firstblock);

       //start the simulation and proccess all the queued events
       System.out.println(CloudSim.startSimulation()); 
       
 //this to simulate the system start, where entites randomaly rate other entities
       Random rand = new Random();
       Entity randomEntity1,randomEntity2;
       int randRatingValue;
       Double newTrustScore=0.0;
       RatingEvent event;
       Entity posValidator;
       
        control.ratingLimit=100;
        control.commonRaters=100;
        for(int i=1;i<=150;i++){
         randomEntity1=allEntities.get(rand.nextInt(97)+2);  
          randomEntity2=allEntities.get(rand.nextInt(97)+2);
          randRatingValue=rand.nextInt(10)+1;
         tempTrans=randomEntity1.rateEntity(randomEntity2.id,randRatingValue);
         
         posValidator=posCommittee.get(rand.nextInt(posCommittee.size()));
         
         event =new RatingEvent(tempTrans,BC);
         event.setSource(tempTrans.getRaterEntityID());
         event.setDestination(posValidator.getId());
         newTrustScore=posValidator.processEvent(event);
         //tempTrans=posCommittee.get(0).validateTransaction(tempTrans, BC);
         //newTrustScore=posCommittee.get(0).addTransaction(BC,tempTrans);  
         
         if(newTrustScore!= -1){//no suspected cluster
           randomEntity2.updateTrustScore(newTrustScore);
            if(newTrustScore>PoSthreshold){
             posCommittee.add(randomEntity2);
             System.out.println(randomEntity2+ "have been added to the Pos commitee");
            } 
        }
        }      
        
        
        
        
        control.ratingLimit=100;
        
        //Example of self-promoting attack 
        //in this scenario we randomly select malicious node that will try to inflate the rating to other nodes
        System.out.println();
        System.out.println();
        System.out.println("********************************************************************************");
        System.out.println("Self-promoting attack simulation");
        
        
        ArrayList<Transaction> inflatedRatingTrans;
        Entity posNode;
        randomEntity1=allEntities.get(rand.nextInt(99)+1);  
        randomEntity2=allEntities.get(rand.nextInt(99)+1);
        inflatedRatingTrans=randomEntity1.ratingInflation(randomEntity2.id, 50);
        Transaction temp;
        System.out.println("Entity "+randomEntity1.id+" will perform inflate rating to entity "+ randomEntity2.id);
        System.out.println("Entity "+randomEntity2.id+" initial trust score was: "+randomEntity2.trustScore);
        
        for(Transaction t:inflatedRatingTrans){
          event =new RatingEvent(t,BC);
          posNode=posCommittee.get(0);
          event.setSource(t.getRaterEntityID());
          event.setDestination(posNode.getId());
          newTrustScore=posNode.processEvent(event);
          randomEntity2.updateTrustScore(newTrustScore);
        }
        
        
        System.out.println("********************************************************************************");
        System.out.println("********************************************************************************");
        System.out.println("********************************************************************************");
        System.out.println("Self-promoting attack when applying our mitigation method simulation");
        //Example of self-promoting attack mitigation using rating control
        //in this scenario we randomly select malicious node that will try to inflate the rating to other nodes
        //this paramater controls, after how many rating the system start applying controld rating
        //System.out.println("rating limit is: "+control.ratingLimit);
        control.ratingLimit=1;
        
        
         //int dishonestNodes=50;//the number of dishonest nodes
        double temp_success_rate=0.0;
        double total_success_rate=0.0;
        int count=0;
        double successRateAvrage=0;
        
                
        for(int i=1;i<=50;i++){
        randomEntity1=allEntities.get(rand.nextInt(99)+1);  
        randomEntity2=allEntities.get(rand.nextInt(99)+1);
        inflatedRatingTrans=randomEntity1.ratingInflation(randomEntity2.id, 50);
        System.out.println("Entity "+randomEntity1.id+" will perform inflate rating to entity "+ randomEntity2.id);
        System.out.println("Entity "+randomEntity2.id+" initial trust score was: "+randomEntity2.trustScore);
        for(Transaction t:inflatedRatingTrans){
          event =new RatingEvent(t,BC);
          posNode=posCommittee.get(0);
          event.setSource(t.getRaterEntityID());
          event.setDestination(posNode.getId());
          newTrustScore=posNode.processEvent(event);
          
          temp_success_rate=temp_success_rate+(newTrustScore-randomEntity2.trustScore);
          System.out.println(temp_success_rate);
          count++;
          randomEntity2.updateTrustScore(newTrustScore);
        }
        System.out.println("the attack performed by node "+randomEntity1.id+", success rate="+temp_success_rate/count);
       total_success_rate=total_success_rate+temp_success_rate/count;
        temp_success_rate=0;                
        count=0;        
       }
        System.out.println("Total attack success rate="+total_success_rate);
//System.out.println(newscore);
      //System.out.println(event.trans);
       /*temp=posCommittee.get(0).validateTransaction(t,BC);  
       newTrustScore=posCommittee.get(0).addTransaction(BC,temp);
       randomEntity2.updateTrustScore(newTrustScore);
       System.out.println(randomEntity2);*/
       
        
        
        
        
        
        
        
        
        
        
        
        
//terminate the simulation       
       CloudSim.stopSimulation();
    }
    
}
