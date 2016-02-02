package sandwich;

public class Chef implements Runnable {
	private Table table;
	private String unlimitedIngredient;
	private Thread agent; // used to track when to close the current thread
	
	// constructor
	public Chef(Table table, String unlimitedIngredient, Thread agent) {
		this.table = table;
		this.unlimitedIngredient = unlimitedIngredient;
		this.agent = agent; 
	}
	
	
	// run the thread
	public void run() {
		boolean running = true;
		while (running) {
			String[] ingredients = new String[] {null, null};
			
			ingredients = table.takeIngredients(unlimitedIngredient, agent);
			
			if ((ingredients[0] != null) && (ingredients[1] != null)) {
				System.out.println(Thread.currentThread().getName() + " - Used ingredients: " + ingredients[0] 
						+ " and " + ingredients[1] + " to make a sandwich and eat it.");
			}
			
			if (ingredients[0] == "dead") {
				System.out.println(Thread.currentThread().getName() + " - Thread ending... Good bye");
				running = false;
			}
		}
		
		return;
	}
}
