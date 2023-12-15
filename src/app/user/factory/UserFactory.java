package app.user.factory;

import app.user.User;

/**
 * The Factory for creating users.
 */
public abstract class UserFactory {
    /**
     * Creates a user using specific factory.
     * @param username username
     * @param age      user's age
     * @param city     user's city
     * @return
     */
    public abstract User create(String username, int age, String city);
}
