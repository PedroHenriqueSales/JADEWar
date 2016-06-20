package soldiers;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class King extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int myWarriors;
	
	protected void setup() {
        try {
            // create the agent descrption of itself
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            DFService.register(this, dfd);
            // register the description with the DF
            
            String myName = this.getName().split("@")[0];
            int num = init(myName);
            createWarrior(num);
        }catch (Exception e) {
            System.out.println( "Saw exception in GuestAgent: " + e );
            e.printStackTrace();
        }
        
        addBehaviour(new CyclicBehaviour(this) {
        	 /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action() {
        		 ACLMessage msg = receive();
        		 String myName = this.getAgent().getName().split("@")[0];
                 if (msg != null) {
                	if(msg.getContent().equals(myName+"?") ){
                		ACLMessage response = msg.createReply();
                			
                		if(myName.equals("KingNorth")){
                    		response.setContent("The winter is coming! Welcome Sir.");
                		}else{
                			response.setContent("Fire and Blood! Welcome Sir.");
                		}
                		
                		if(msg.getSender().getName().contains("warrior")){
                			myWarriors++;
                			System.out.println(myName+ " warriors: " + myWarriors);
                		}
           
                		send(response);
                	}
                 }
                    
                 else {
                     // if no message is arrived, block the behaviour
                     block();
                 }
             }
        } );
        
    }
	
	private void createWarrior(int num){
		PlatformController container = getContainerController();
		Object[] args = new Object[1];
		String kingName = this.getName().split("@")[0];
		try {
            for (int i = 0;  i < num;  i++) {
                // create a new agent
		String localName = "warrior"+kingName+i;
		args[0] = kingName;
		AgentController guest = container.createNewAgent(localName, "soldiers.Warrior", args);
		guest.start();
            }
        }
        catch (Exception e) {
            System.err.println( "Exception while adding guests: " + e );
            e.printStackTrace();
        }
	}
	
	private int init(String kingName) throws IOException{
		System.out.println("Quantos guerreiros deseja para " + kingName + "?");
		
	    try{
	        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	        String s = bufferRead.readLine();

	        return Integer.parseInt(s);
	    }
	    catch(IOException e)
	    {
	        e.printStackTrace();
	    }
	    
	    return 0;
	}

}
