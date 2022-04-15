/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrustFramwork;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEvent;

/**
 *
 * @author 
 */

public class FaultRecictenceExample {

    /**
     * @param args the command line arguments
     */
public static void main(String[] args) throws InterruptedException {
 //initlize the simulation parameterss    
int num_user = 1;
Calendar calendar = Calendar.getInstance();
boolean trace_flag = false;
CloudSim.init(num_user, calendar, trace_flag);


Blockchain BC=new Blockchain();
Random rand = new Random();

/*
ArrayList<Entity>  allEntitiesCentrlized=new ArrayList<Entity>();
ArrayList<Entity>  allEntitiesDistributed=new ArrayList<Entity>();
ArrayList<Entity>  allEntitiesBlockchain=new ArrayList<Entity>();

//centrlized system, where the trustscore is stored in a centrlized server
for(int i=1;i<=100;i++){
allEntitiesCentrlized.add(new Entity(i, 0.0, false));
}
*/


/*
ArrayList distributedServersIDs=new ArrayList();//Randomly select 10 nodes to act as the distributed servers
for(int i=1;i<=10;i++){
distributedServersIDs.add(rand.nextInt(100)+1);
}
*/
int centlizedServerID=rand.nextInt(100)+1;//Randomly select a node to act as the centrlized server 
double averageValue=0;
int count=0;
int downEntityId=0;
ArrayList alreadyDown=new ArrayList();


System.out.println("*****************************Centrlized Servers****************************"); 

for(int j=1;j<=1000;j++){
    alreadyDown.clear();
for(int i=1;i<=100;i++){
   downEntityId=rand.nextInt(100)+1;
   if(!alreadyDown.contains(downEntityId)){
     if(downEntityId!=centlizedServerID){
      System.out.println("Entity "+downEntityId+" is down"); 
      //allEntitiesCentrlized.remove();
      alreadyDown.add(downEntityId);
      }else{
       System.out.println("Centrlized system is down after "+i+" entities are down"); 
       averageValue=averageValue+i;
       break;
      }  
   }
}
}
averageValue=averageValue/1000;
System.out.println("the average is: "+averageValue); 





System.out.println("*****************************Distributed Servers****************************"); 
ArrayList distibutedServerIDs=new ArrayList();
averageValue=0;

for(int j=1;j<=1000;j++){
    alreadyDown.clear();
    
for(int i=1;i<=2;i++){
    int serverID=rand.nextInt(100)+1;
    distibutedServerIDs.add(serverID);
    System.out.println("Entity "+serverID+" is running as a server");  
}    
    //count=0;
for(int i=1;i<=100;i++){
   downEntityId=rand.nextInt(100)+1;
   if(!alreadyDown.contains(downEntityId)){
       alreadyDown.add(downEntityId);
     System.out.println("Entity "+downEntityId+" is down");  
     if(distibutedServerIDs.contains(downEntityId)){
         distibutedServerIDs.remove(Integer.valueOf(downEntityId));
     System.out.println("Server "+downEntityId+" is down, but there still "+distibutedServerIDs.size()+" servers running "+distibutedServerIDs);  
      }
     if(distibutedServerIDs.isEmpty()){
       System.out.println("Distributed system is down after "+alreadyDown.size()+" entities are down"); 
       averageValue=averageValue+alreadyDown.size();
       count++;
       break;
      }
     
   }
}

}

averageValue=averageValue/count;
System.out.println("the average is: "+averageValue);


System.out.println(); System.out.println(); 
System.out.println("*****************************Distributed Servers****************************"); 


for(int i=1;i<=100;i++){
downEntityId=rand.nextInt(100)+1;    
if(!alreadyDown.contains(downEntityId)){
System.out.println("Centrlized system is down after "+i+" entities are down"); 
 averageValue=averageValue+i;    
}
}
System.out.println("Blockchain system is Not down, unless all the entities are down"); 








}
    
}
