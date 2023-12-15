package app.user.factory;

import app.user.SimpleUser;
import app.user.User;

public final class SimpleUserFactory extends UserFactory {
    @Override
    public User create(final String username, final int age, final String city) {
        return new SimpleUser(username, age, city);
    }
}
