package soldiers;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.df;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class King extends Agent{

	protected void setup() {
        try {
            // create the agent descrption of itself

            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            DFService.register(this, dfd);
            // register the description with the DF
            
            createWarrior(4);
        }catch (Exception e) {
            System.out.println( "Saw exception in GuestAgent: " + e );
            e.printStackTrace();
        }
        
        addBehaviour( new CyclicBehaviour( this ) {
        	 public void action() {
        		 ACLMessage msg = receive();
        		 String myName = this.getAgent().getName().split("@")[0];
                 if (msg != null) {
                	if(msg.getContent().equals(myName+"?") ){
                		System.out.println("Recebeu!!");
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
		try {
            for (int i = 0;  i < num;  i++) {
                // create a new agent
		String localName = "warrior"+this.getName().toString()+i;
		
		args[0] = this.getName();
		AgentController guest = container.createNewAgent(localName, "soldiers.Warrior", args);
		guest.start();
            }
        }
        catch (Exception e) {
            System.err.println( "Exception while adding guests: " + e );
            e.printStackTrace();
        }
	}

}
