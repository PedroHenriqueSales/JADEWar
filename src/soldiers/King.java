package soldiers;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class King extends Agent{
	protected void setup() {
        try {
            // create the agent descrption of itself
            ServiceDescription sd = new ServiceDescription();
            sd.setType( "King" );
            sd.setName( "King of North" );
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            dfd.addServices( sd );

            // register the description with the DF
            DFService.register( this, dfd );

            // notify the host that we have arrived
            
            createWarrior(4);
        }catch (Exception e) {
            System.out.println( "Saw exception in GuestAgent: " + e );
            e.printStackTrace();
        }
        
        addBehaviour( new CyclicBehaviour( this ) {
            public void action() {
                // listen if a greetings message arrives
            	 ACLMessage hello = new ACLMessage( ACLMessage.INFORM);
                 hello.setContent( "North" );
                 hello.addReceiver( new AID( "host", AID.ISLOCALNAME ) );
                 send( hello );
            }
        } );

    }
	
	private void createWarrior(int num){
		PlatformController container = getContainerController();
		
		try {
            for (int i = 0;  i < num;  i++) {
                // create a new agent
		String localName = "warrior"+i;
		AgentController guest = container.createNewAgent(localName, "soldiers.Warrior", null);
		guest.start();
            }
        }
        catch (Exception e) {
            System.err.println( "Exception while adding guests: " + e );
            e.printStackTrace();
        }
	}

}
