/**
 * 
 */
package com.oauth.example.modal;

/**
 * Defines all the categories used to observe trend
 */
public enum Roles {

	ROLE_ADMIN("ROLE_ADMIN", 1), ROLE_USER("ROLE_USER", 2), ROLE_GUEST("ROLE_GUEST", 3);

	private String displayName;

	private int id;

	/**
	 * used to construct the enum.
	 * 
	 * @param displayName
	 * @param id
	 */
	private Roles(final String displayName, final int id) {
		this.displayName = displayName;
		this.id = id;

	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return Category name
	 */
	public String getName() {
		return this.name();
	}
}
