package sandwich;

import java.util.Random;

public class Agent implements Runnable {
	private Table table;

	
	// constructor
	public Agent(Table table) {
		this.table = table;
	}
	
	
	// return a random ingredient
	private String generateIngredient() {
		//http://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
		Random random = new Random();
		int value = random.nextInt(3);
		
		// generate ingredient from random value
		String ingredient;
		if (value == 0) {
			ingredient = "BREAD";
		} else if ( value == 1) {
			ingredient = "PEANUT BUTTER";
		} else {
			ingredient = "JAM";
		}
		
		return ingredient;
	}
	
	
	// run the thread
	public void run() {
		// add ingredients to the table 20 times
		for (int i = 1; i < 21; i++) { 
			System.out.println("\n---------------------------------------------\nAgent - Round: " + i);
			
			// decide which ingredients to put on the table
			String[] ingredients = new String[2];
			ingredients[0] = generateIngredient();
			while (true) { // ensure both ingredients are different
				ingredients[1] = generateIngredient();
				if (ingredients[0] != ingredients[1]) {
					break;
				}
			}
			
			// put the ingredients on the table
			System.out.println(Thread.currentThread().getName() + " - Waiting to put " + ingredients[0] + " and " + ingredients[1] + " on the table.");
			table.addIngredients(ingredients);
		}
		
		System.out.println(Thread.currentThread().getName() + " - Thread ending... Good bye\n");
	}
	
}
