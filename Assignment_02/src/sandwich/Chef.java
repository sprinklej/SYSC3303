package sandwich;

public class Chef implements Runnable {
	private Table table;
	private String unlimitedIngredient;
	
	// constructor
	public Chef(Table table, String unlimitedIngredient) {
		this.table = table;
		this.unlimitedIngredient = unlimitedIngredient;
	}
	
	
	// run the thread
	public void run() {
		boolean running = true;
		while (running) {
			String[] ingredients = new String[] {null, null};
			
			ingredients = table.takeIngredients(unlimitedIngredient);
			
			if ((ingredients[0] != null) && (ingredients[1] != null)) {
				System.out.println(Thread.currentThread().getName() + " - USED ingredients: " + ingredients[0] 
						+ " and " + ingredients[1] + " to make a sandwich and eat it.");
			}
			
			if (ingredients[0] == "kill") {
				running = false;
			}
		}
		
		System.out.println(Thread.currentThread().getName() + " - Thread ending... Good bye");
		return;
	}
}
