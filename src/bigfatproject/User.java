package bigfatproject;

public class User {
int id;
String username;
String password;
String role;


public User(int id,String username, String password, String role) {
	this.id=id;
	this.username = username;
	this.password = password;
	this.role = role;
}


public String getUsername() {
	return username;
}


public void setUsername(String username) {
	this.username = username;
}


public String getPassword() {
	return password;
}


public void setPassword(String password) {
	this.password = password;
}


public String getRole() {
	return role;
}


public void setRole(String role) {
	this.role = role;
}

public void viewInbox(String username) {
	Database.viewInbox(username);
	
}
public void viewOutbox(String username) {
	Database.viewOutbox(username);
}


public void sendMessage(String username) {
	Database.sendMessage(username);
}
}
