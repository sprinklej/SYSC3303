package sandwich;

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
		//System.out.println(Thread.currentThread().getName() + " - Put " + ingredients[0] + " and " + ingredients[1] + " on the table.");
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
		
		returnIngrdnts[0] = ingredients[0];
		returnIngrdnts[1] = ingredients[1];
		System.out.println(Thread.currentThread().getName() + " - Took ingredients: " + returnIngrdnts[0] 
				+ " and " + returnIngrdnts[1] + " off of the table.");
	
		ingredients[0] = null; // remove the ingredients from the table
		ingredients[1] = null;
		
		emptyTable = true;
		notifyAll();
		return returnIngrdnts;	
	}
	
}


