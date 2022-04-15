/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import org.cloudbus.cloudsim.core.CloudSim;

public class LargeScaleAttackSimulation {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
       control.ratingLimit=2;
       control.commonRaters=4;

 
        Random rand = new Random();
       Entity randomEntity1,randomEntity2;
       int randRatingValue;
       Double newTrustScore=0.0;
       RatingEvent event;
       Entity posValidator;
       
       
       
       
   
        for(int i=1;i<=50;i++){
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
            } 
        }else{
            //System.out.println("common raters: "+BC.getCommonRaters(randomEntity1.id,randomEntity2.id));
            //ArrayList suspectedClusterIDs=new ArrayList();
            //ArrayList<Entity> commonratersentities=BC.getCommonRaters(randomEntity1.id,randomEntity2.id);

            //System.out.println(control.verifyMaliciousCluster(BC.getCommonRaters(randomEntity1.id,randomEntity2.id)));
            /*for(Entity e:commonratersentities){
            suspectedClusterIDs.add(e.id);
            }
            ArrayList suspendedClusterIDs=control.verifyMaliciousCluster(suspectedClusterIDs);
            System.out.println("Cluster "+suspendedClusterIDs+"have been suspended");
            */
        //System.out.println("Ancestry raters of node "+randomEntity1.id+": "+BC.getAncestryRaters(randomEntity1.id,2)); 
        //System.out.println("Ancestry raters of node "+randomEntity2.id+": "+BC.getAncestryRaters(randomEntity2.id,2)); 
        }
         
        }      
            
        
        control.commonRaters=100;//just a high number so the our proposed method won't be activiated, and we can observe how the attack work
        //Example of large-scale self-promoting attack 
        //in this scenario we select a group of malicious node that will try to inflate the rating of each others
        System.out.println();
        System.out.println();
        System.out.println("********************************************************************************");
        System.out.println("large scale slef-promoting attack simulation");
        
        posValidator=posCommittee.get(rand.nextInt(posCommittee.size()));
        int ratingValue=10;
        ArrayList<Entity> attackers=new ArrayList<Entity>();
        
        for(int i=10;i<=20;i++){
          attackers.add(allEntities.get(i));
        }

        for(Entity e1:attackers){//each attacker rates all the other attackers
         for(Entity e2:attackers){
        if(e1.id!=e2.id){//just to avoid an entity rating itself
        tempTrans= e1.rateEntity(e2.id,ratingValue);
         event =new RatingEvent(tempTrans,BC);
         event.setSource(tempTrans.getRaterEntityID());
         event.setDestination(posValidator.getId());
         newTrustScore=posValidator.processEvent(event); 
         e2.updateTrustScore(newTrustScore);
         //System.out.println("Entity "+e1.id+" have rated entity "+e2.id+" and the new trust score is "+newTrustScore);
        }
         }
        }
        
        
        
          System.out.println();
        System.out.println();
        System.out.println("********************************************************************************");
        System.out.println("large scale slef-promoting attack mitigation simulation");
        control.commonRaters=6;
        boolean maliciousClusterDetected=false;
      
        
        attackers.clear();
        
        for(int i=21;i<=31;i++){
          attackers.add(allEntities.get(i));
        }

        ArrayList suspectedClusterIDs = new ArrayList();
        for(Entity e1:attackers){//each attacker rates all the other attackers
        
        if((!maliciousClusterDetected)&&(!suspectedClusterIDs.contains(e1))){
         for(Entity e2:attackers){
        if(e1.id!=e2.id){//just to avoid an entity rating itself
        tempTrans= e1.rateEntity(e2.id,ratingValue);
         event =new RatingEvent(tempTrans,BC);
         event.setSource(tempTrans.getRaterEntityID());
         event.setDestination(posValidator.getId());
         newTrustScore=posValidator.processEvent(event); 
         
          if(newTrustScore!= -1){//no suspected cluster
           e2.updateTrustScore(newTrustScore);
        }else{
              suspectedClusterIDs.addAll(BC.getCommonRaters(e1.id,e2.id));
             maliciousClusterDetected=true;
             // System.out.println("Verify Cluster"+control.verifyMaliciousCluster(BC.getCommonRaters(e1.id,e2.id)));
               System.out.println("common raters: "+BC.getCommonRaters(e1.id,e2.id));
            //System.out.println("common raters: "+BC.getCommonRaters(e1.id,e2.id));
           // suspectedClusterIDs=new ArrayList();
            //ArrayList<Entity> commonratersentities=BC.getCommonRaters(randomEntity1.id,randomEntity2.id);
         }    

         //System.out.println("Entity "+e1.id+" have rated entity "+e2.id+" and the new trust score is "+newTrustScore);
             }
         }   
        }
         
        }
         
         
         //terminate the simulation       
       CloudSim.stopSimulation();
         
        
        
        
        /*System.out.println(BC.getAncestryRaters(3, 0));
        System.out.println(BC.getAncestryRaters(3, 1));
        System.out.println(BC.getAncestryRaters(3, 2));
        System.out.println(BC.getAncestryRaters(3, 3));*/
        
    }
    
}
