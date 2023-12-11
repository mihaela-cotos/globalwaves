package app.user.factory;

import app.user.User;

public abstract class UserFactory {
    public abstract User create(String username, int age, String city);
}
