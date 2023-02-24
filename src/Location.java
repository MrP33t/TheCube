import java.io.Serializable;

public class Location implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8141015445426284168L;
	int x, y;
	int UID;
	
	public Location(int x, int y, int UID) {
		this.x = x;
		this.y = y;
		this.UID = UID;
	}
}
