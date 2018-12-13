package bigfatproject;

public class User3 extends User2 {

	public User3(int id,String username, String password, String role) {
		super(id,username, password, "RED");
	}

	

	public void DeleteInbox(String username) {
		Database.deleteFromInbox( username);
	}
	public void DeleteOutbox(String username) {
		Database.deleteFromOutbox( username);
	}

}
