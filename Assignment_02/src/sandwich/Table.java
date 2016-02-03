package sandwich;

public class Table {
	private String[] ingredients = new String[] {null, null};
	private boolean emptyTable = true; // if true then the Agent can add ingredients to the table

	
	// put ingredients onto the table
	public synchronized void addIngredients(String[] ingrdnts) {		
		// check the agent is not trying to add 2 of the same ingredients
		if (ingrdnts[0] == ingrdnts[1]) {
			System.out.println("Error - Supplied ingredients are the same. Ingredients not put on the table.");
			return;
		}
		
		// check to make sure the agent is only adding bread, peanut butter, or jam
		// put into 2 if's to remove complexity of 1 giant if statement
		if ((ingrdnts[0] != "BREAD") && (ingrdnts[0] != "PEANUT BUTTER") && (ingrdnts[0] != "JAM")) {
			System.out.println("Error - Supplied ingredient 1 of wrong type. Ingredients not put on the table");
			return;
		}
		if ((ingrdnts[1] != "BREAD") && (ingrdnts[1] != "PEANUT BUTTER") && (ingrdnts[1] != "JAM")) {
			System.out.println("Error - Supplied ingredient 2 of wrong type. Ingredients not put on the table");
			return;
		}
		
		// while NOT emptyTable - wait for it to be empty
		while (!emptyTable) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}

		ingredients[0] = ingrdnts[0];
		ingredients[1] = ingrdnts[1];
		emptyTable = false;
		System.out.println(Thread.currentThread().getName() + " - Ingredients put on table.");
		notifyAll();
	}

	
	// remove ingredients from the table
	public synchronized String[] takeIngredients(String uIngredient) {
		String[] returnIngrdnts = new String[] {null, null};		
		
		// while emptyTable - wait for it to be NOT empty
		while (emptyTable) {		 
			try {
				//System.out.println(Thread.currentThread().getName() + " - waiting..."); // see who is waiting when
				this.wait();
			} catch (InterruptedException e) {
				returnIngrdnts[0] = "kill";
				return returnIngrdnts;
			}
			

		}
		
		// Check if the current chef can take the ingredients off the table
		if ((uIngredient == ingredients[0]) || (uIngredient == ingredients[1])) {	
			notifyAll();
			return returnIngrdnts;
		}
		
		// check takes ingredients off the table 
		returnIngrdnts[0] = ingredients[0];
		returnIngrdnts[1] = ingredients[1];
		System.out.println(Thread.currentThread().getName() + " - TOOK ingredients: " + returnIngrdnts[0] 
				+ " and " + returnIngrdnts[1] + " off of the table.");
	
		ingredients[0] = null;
		ingredients[1] = null;
		
		emptyTable = true;
		notifyAll();
		return returnIngrdnts;	
	}
	
}


