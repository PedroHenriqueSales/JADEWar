package soldiers;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Warrior extends Agent{
	
	private String myKing;
	
	
	protected void setup() {
        try {
            // create the agent descrption of itself
            ServiceDescription sd = new ServiceDescription();
            sd.setType( "Warrior" );
            sd.setName( "Soldier" );
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            dfd.addServices( sd );

            // register the description with the DF
            DFService.register( this, dfd );           
            
            String kingName = (String)this.getArguments()[0];
            ACLMessage hello = new ACLMessage( ACLMessage.INFORM);
            hello.setContent(kingName + "?");
            hello.addReceiver( new AID(kingName, AID.ISLOCALNAME ));
            send( hello );
            
            // add a Behaviour to process incoming messages
            addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                            	ACLMessage msg = receive( MessageTemplate.MatchPerformative( ACLMessage.INFORM ) );       	
                            	 
                            }
                        } );
        }
        catch (Exception e) {
            System.out.println( "Saw exception in GuestAgent: " + e );
            e.printStackTrace();
        }

    }


}
