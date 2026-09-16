package users;

public class Users {
	public int id;
	public String name;
	public String email;
	
	public Users() {
	}

	public Users (int id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
