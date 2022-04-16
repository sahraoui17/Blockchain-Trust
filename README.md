# Introcution
Blockchain-Trust is Trust management for IoT devices based on Blockchain. 
Devices' trust ratings and reputation scores are scored in persistent Blockchain.
The project contains several classes that can run on top of iFogSim simulator to simulate trust management in IoT enviroment.

##Supported trust attacks
You can use this project to simulate trust based attack such as good-mouthing attack and bad-mouthing attack.

#Examples

```java
int num_user = 1;
Calendar calendar = Calendar.getInstance();
boolean trace_flag = false;
CloudSim.init(num_user, calendar, trace_flag);


        ArrayList<Entity>  allEntities=new ArrayList<Entity>();//this store all the entities currently running
        ArrayList<Entity>  posCommittee=new ArrayList<Entity>();//this will dynamically store the ids of pos commtee nodes        
        
       Blockchain BC=new Blockchain();
       RatingControl control=new RatingControl(BC);
       //int blockSize=500;
       int simulatedEntitiesCount=100;
       Block firstblock=new Block(new ArrayList<Transaction>());
       
       //initilize the first entity as a PoS entity to start the system running
       Double PoSthreshold=9.0;
       Entity firstPosNode=new Entity(1,10.0,true);//the first PoS entity
       allEntities.add(firstPosNode);
       posCommittee.add(firstPosNode);
       Transaction tempTrans=new Transaction(0,1,10,1,10.0);
       firstblock.addTransaction(tempTrans);
       
       
       Double intial_trust_score=5.0;
       
       for(int i=2;i<=simulatedEntitiesCount;i++){
       allEntities.add(new Entity(i,intial_trust_score,false));
       tempTrans=new Transaction(0,i,intial_trust_score.intValue(),firstPosNode.id,intial_trust_score);
       firstblock.addTransaction(tempTrans);
       //System.out.println(tempTrans);    
       }
       BC.addBlock(firstblock);
       
       
       //this to simulate the system start
       Random rand = new Random();
       Entity randomEntity1,randomEntity2;
       int randRatingValue;
       Double newTrustScore=0.0;
       
        for(int i=1;i<=110;i++){
         randomEntity1=allEntities.get(rand.nextInt(97)+2);  
          randomEntity2=allEntities.get(rand.nextInt(97)+2);
          randRatingValue=rand.nextInt(10)+1;
         tempTrans=randomEntity1.rateEntity(randomEntity2.id,randRatingValue);
         tempTrans=posCommittee.get(0).validateTransaction(tempTrans, BC);
         newTrustScore=posCommittee.get(0).addTransaction(BC,tempTrans);  
         randomEntity2.updateTrustScore(newTrustScore);
         
         
        }
        
        //Example of self-promoting attack mitigation
        //in this scenario we randomly select malicious node that will try to inflate the rating to other nodes
        //for(int i=1;i<=10;i++){
        //this paramater controls, after how many rating the system start applying controld rating
        
        
        System.out.println("rating limit is: "+control.ratingLimit);
        randomEntity1=allEntities.get(rand.nextInt(99)+1);  
        randomEntity2=allEntities.get(rand.nextInt(99)+1);
        ArrayList<Transaction> inflatedRatingTrans=randomEntity1.ratingInflation(randomEntity2.id, 10);
        Transaction temp;
        System.out.println("Entity "+randomEntity1.id+" will perform inflate rating to entity "+ randomEntity2.id);
        System.out.println("Entity "+randomEntity2.id+" initial trust score was: "+randomEntity2.trustScore);
        
        CloudSim.addEntity(randomEntity1);
        CloudSim.addEntity(randomEntity2);
        RatingEvent event;
        Entity posNode;
 
        for(Transaction t:randomEntity1.ratingInflation(randomEntity2.id, 10)){
          event =new RatingEvent(t,BC);
          posNode=posCommittee.get(0);
          event.setSource(t.getRaterEntityID());
          event.setDestination(posNode.getId());
          double newscore=posNode.processEvent(event);
          System.out.println(newscore);


      //System.out.println(event.trans);
       temp=posCommittee.get(0).validateTransaction(t,BC);  
       newTrustScore=posCommittee.get(0).addTransaction(BC,temp);
       randomEntity2.updateTrustScore(newTrustScore);
       System.out.println(randomEntity2);
      // }
        
       System.out.println(CloudSim.startSimulation()); 
       
       CloudSim.stopSimulation();
        
        
       System.out.println("*************************************Cluster detection***************************************************");
    System.out.println(randomEntity2.id+" depth 0 ancestry "+BC.getAncestryRaters(randomEntity2.id,0));
    System.out.println(randomEntity2.id+" depth 1 ancestry "+BC.getAncestryRaters(randomEntity2.id,1));   
    System.out.println(randomEntity2.id+" depth 2 ancestry "+BC.getAncestryRaters(randomEntity2.id,2));   
       
       
       
      
        //show blocks
       for(Transaction t:BC.getLastBlock().transactions){
        System.out.println(t);
       }
       for(Entity e:allEntities){
        System.out.println(e);   
       }

      for(Transaction t:firstblock.transactions){
       System.out.println(t);    
       }
        Blockchain BC=new Blockchain();
       RatingControl control=new RatingControl(BC);
       int blockSize=5;
       ArrayList pos=new ArrayList();//this will dynamically store the ids of pos commtee nodes
       Block newblock=new Block(new ArrayList<Transaction>(),blockSize);
       BC.addBlock(newblock);
       Transaction tempTrans=new Transaction(0,1,9,0,9.0);
//       newblock.addTransaction(tempTrans);
       Transaction temp2Trans=new Transaction(0,2,5,0,5.0);
       newblock.addTransaction(temp2Trans);
       Transaction temp3Trans=new Transaction(0,3,5,0,5.0);
       newblock.addTransaction(temp3Trans);
     
       Transaction temp4Trans=control.addControledRating(2, 3, 10,1);
       newblock.addTransaction(temp4Trans);
       System.out.println(BC.getLastTrustScore(3));
       
       Transaction temp5Trans=control.addControledRating(2, 4, 10,1);
       newblock.addTransaction(temp5Trans);
        System.out.println(BC.getLastTrustScore(3));
        
       Transaction temp6Trans=control.addControledRating(2, 5, 10,1);
       newblock.addTransaction(temp6Trans);
        System.out.println(BC.getLastTrustScore(3));
       
        Transaction temp7Trans=control.addControledRating(3, 2, 10,1);
        newblock.addTransaction(temp7Trans);
        System.out.println(BC.getLastTrustScore(3));
       
       Transaction temp8Trans=control.addControledRating(6, 3, 10,1);
        newblock.addTransaction(temp8Trans);
        System.out.println(BC.getLastTrustScore(3));
        
        Transaction temp9Trans=control.addControledRating(6, 1, 10,1);
        newblock.addTransaction(temp9Trans);
        
        Transaction temp10Trans=control.addControledRating(6, 3, 10,1);
        newblock.addTransaction(temp10Trans);
       System.out.println(BC.getLastTrustScore(3));
      
       
       
       //System.out.println(BC.getLastTrustScore(3));
       //System.out.println(BC.getAllRatingCount(3));
       //System.out.println(BC.getPreviousRatingCountbyRater(0,3));
       //System.out.println(BC.getAncestryRaters(3, 3));
       //System.out.println(BC.getCommonRaters(3, 5));
       //System.out.println(BC.outClusterRatingCount(6, Arrays.asList(2,5)));
       System.out.println(control.verifyMaliciousCluster((ArrayList<Integer>) Arrays.asList(2,5)));
    }
    
        //Transaction(int raterEntityID, int ratedEntityID, int ratingValue, int validtingEntityID,double newTrustScore) {
        Transaction trans1=new Transaction(1,2,5,0,9.0);
         Transaction trans2=new Transaction(5,6,9,0,9.0);
         Transaction trans3=new Transaction(5,6,9,0,9.0);
         Transaction trans4=new Transaction(2,6,9,0,9.0);
         
        ArrayList translist=new ArrayList();
        translist.add(trans1);
        translist.add(trans2);
        translist.add(trans3);
        translist.add(trans4);
        
         Block b=new Block(translist,10);
         Blockchain BC=new Blockchain();
         BC.addBlock(b);
        
// trans.setRaterEntityID(2);
        System.out.println(BC.getRatersFromLedger(6));
'''




