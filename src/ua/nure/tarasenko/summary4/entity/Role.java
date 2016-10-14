package ua.nure.tarasenko.summary4.entity;

public enum Role {

	USER(1), ADMIN(2);

	int id;

	Role(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static Role getRoleById(int id) {
		for(Role role : Role.values()) {
			if (role.id == id) {
				return role;
			}
		}
		return null;
	}
	
	

}
