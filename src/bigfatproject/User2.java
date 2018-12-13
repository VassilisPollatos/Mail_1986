package bigfatproject;

public class User2 extends User{

	public User2(int id,String username, String password, String role) {
		super(id,username, password, role);
		
	}

	
	public void EditUsername(String username) {
		Database.editUsername(username);
}
	public void EditPassword(String username) {
		Database.editPassword( username);
	
	}
}