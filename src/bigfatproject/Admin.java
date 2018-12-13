package bigfatproject;

public class Admin extends User3 {

	public Admin(int id, String username, String password, String role) {
		super(id, "admin", "admin", "admin");

	}

	public void creAteUser() {
		Database.createUser();
	}

	public void editUserRole() {
		Database.editUserRole();
	}

	public void deleteUser() {
		Database.deleteUser();
	}
}