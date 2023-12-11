package app.user.factory;

import app.user.SimpleUser;
import app.user.User;

public class SimpleUserFactory extends UserFactory {
    @Override
    public User create(String username, int age, String city) {
        return new SimpleUser(username, age, city);
    }
}
