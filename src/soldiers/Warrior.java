package soldiers;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Warrior extends Agent{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void setup() {
		
		
		Object[] args = getArguments();
		if(args != null && args.length > 0){
				ServiceDescription servico = new ServiceDescription();
				servico.setType("WAR");
				busca(servico, "ENEMY");
			
			addBehaviour(new CyclicBehaviour(this){
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void action(){
					
					ACLMessage msg = receive();
					if (msg != null){
						System.out.println(msg.getSender() + " : " + msg.getContent());
					} else block();
				
				}
				
			});
			
		}
		
        try {
            // create the agent description of itself
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Warrior");
            sd.setName("Soldier");
            
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            dfd.addServices(sd);
            
            String kingName = (String)this.getArguments()[0];
            ACLMessage hello = new ACLMessage( ACLMessage.INFORM);
            hello.setContent(kingName + "?");
            hello.addReceiver(new AID(kingName, AID.ISLOCALNAME));
            send( hello );
       
        }
       
        catch (Exception e) {
            System.out.println( "Saw exception in GuestAgent: " + e );
            e.printStackTrace();
        }
        
        ServiceDescription servico = new ServiceDescription();
        servico.setType("WAR");
        servico.setName(this.getLocalName());
        registraServico(servico);
        RecebeMensagens("ENEMY", "MORTE");
        
        addBehaviour(new CyclicBehaviour(this){
        	/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action(){
				ACLMessage msg = receive();
				String myking = this.getAgent().getName().split("@")[0];
				
				myking = myking.replaceAll("[0-9]", "");
				System.out.println(myking);
				if(msg != null){
					ACLMessage response = msg.createReply();
					if (!msg.getSender().getName().equalsIgnoreCase(myking) && !msg.getSender().equals(this.getAgent())){
						response.setContent("DIE");
						System.out.println(myking);
						doDelete();
						block();
					}else{
						response.setContent("HAYA");
						System.out.println(myking);
						block();
					}
					
					send(response);
				}
				
				else {
                // if no message is arrived, block the behaviour
                block();
				}
			}	
        	
        });
        
    }

	private void RecebeMensagens(final String mensagem, final String resp) {
		addBehaviour(new CyclicBehaviour(this){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void action(){
				ACLMessage msg = receive();
				if(msg != null){
					if(msg.getContent().equalsIgnoreCase(mensagem)){
						ACLMessage reply = msg.createReply();
						reply.setContent(resp);
						myAgent.send(reply);
					}
				}else block();
			}
		});
		
		
	}

	private void registraServico(ServiceDescription sd) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);
		try{
			DFService.register(this, dfd);
		}catch(FIPAException e){
			e.printStackTrace();
		}
		
	}

	private void busca(final ServiceDescription sd, final String Pedido) {
		
		addBehaviour(new TickerBehaviour(this, 8000){			

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onTick() {
				
				DFAgentDescription dfd = new DFAgentDescription();
				dfd.addServices(sd);
				System.out.println("BATALHA\n");
				try{
					DFAgentDescription[] resultado = DFService.search(myAgent, dfd);
					if(resultado.length != 0){
						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.addReceiver(resultado[0].getName());
						msg.setContent(Pedido);
						myAgent.send(msg);
					}
				} catch(FIPAException e){
					e.printStackTrace();
				}
				
			} 
			
		});
		
		
	}

}
	
	

