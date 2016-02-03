package sandwich;

public class Kitchen {

	public static void main(String[] args) {
		Thread agent, breadChef, pnutButterChef, jamChef;
		Table table;
		
		table = new Table();
		
		// create threads
		agent = new Thread(new Agent(table), "Agent");
		breadChef = new Thread(new Chef(table, "BREAD"), "Bread Chef");
		pnutButterChef = new Thread(new Chef(table, "PEANUT BUTTER"), "Peanut Butter Chef");
		jamChef = new Thread(new Chef(table, "JAM"), "Jam Chef");
		
		// run threads
		System.out.println("Agent and Chef threads ready to go...");
		System.out.println("Lets make some sandwiches... nom nom nom");
		agent.start();
		breadChef.start();
		pnutButterChef.start();
		jamChef.start(); 

		// wait for agent to put ingredients on the table 20 times 
		while (agent.isAlive()) {
			; 
		}
		
		// agent terminated, so terminate chefs!
		breadChef.interrupt();
		pnutButterChef.interrupt();
		jamChef.interrupt();
	}

}
