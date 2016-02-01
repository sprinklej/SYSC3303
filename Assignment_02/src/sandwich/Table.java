package sandwich;

public class Table {
	private boolean bread = false;
	private boolean peanutButter = false;
	private boolean jam = false;
	
	// getters
	public void setBread(boolean addBread) {
		this.bread = addBread;
	}
	
	public void setPeanutButter(boolean addPeanutButter) {
		this.peanutButter = addPeanutButter;
	}
	
	public void setJam(boolean addJam) {
		this.jam = addJam;
	}
	
	// setters
	public boolean getBread() {
		return this.bread;
	}
	
	public boolean getPeanutButter() {
		return this.peanutButter;
	}
	
	public boolean getJam() {
		return this.jam;
	}
	
	
	public synchronized void addIngredients(boolean ingrdnt1, boolean ingrdnt2) {
	
	}
	
}


