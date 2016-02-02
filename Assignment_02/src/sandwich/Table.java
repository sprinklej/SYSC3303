package sandwich;

//import java.lang.Thread.State;
//import java.lang.Thread;

public class Table {
	private String[] ingredients = new String[] {null, null};
	private boolean emptyTable = true; // if true then the Agent can add ingredients to the table

	
	// put ingredients onto the table
	public synchronized void addIngredients(String[] ingrdnts) {		
		// while NOT emptyTable - wait for it to be empty
		while (!emptyTable) {
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
		}

		ingredients = ingrdnts;
		emptyTable = false;
		System.out.println(Thread.currentThread().getName() + " - Ingredients put on table.");
		notifyAll();
	}

	
	// remove ingredients from the table
	public synchronized String[] takeIngredients(String uIngredient, Thread agent) {
		String[] returnIngrdnts = new String[2];		
		// while emptyTable - wait for it to be NOT empty
		while (emptyTable) {			
			// check if agent is alive - if not kill this thread
			if (!agent.isAlive()) {
				returnIngrdnts[0] = "dead";
				return returnIngrdnts;
			}
			
			try {
				System.out.println(Thread.currentThread().getName() + " - waiting...");
				wait();
			} catch (InterruptedException e) {
				System.err.println(e);
			}
			

		}
		
		
		
		if ((uIngredient == ingredients[0]) || (uIngredient == ingredients[1])) {
			returnIngrdnts[0] = null;
			returnIngrdnts[1] = null;
	
			notifyAll();
			return returnIngrdnts;
		}
		
		returnIngrdnts = ingredients;
		System.out.println(Thread.currentThread().getName() + " - Took ingredients: " + returnIngrdnts[0] 
				+ " and " + returnIngrdnts[1] + ", made a sandwich and ate it.");
	
		ingredients[0] = null; // remove the ingredients from the table
		ingredients[1] = null;
		
		emptyTable = true;
		notifyAll();
		return returnIngrdnts;	
	}
	
}


