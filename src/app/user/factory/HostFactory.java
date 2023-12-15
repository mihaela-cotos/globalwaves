package app.user.factory;

import app.user.Host;
import app.user.User;

public final class HostFactory extends UserFactory {

    @Override
    public User create(final String username, final int age, final String city) {
        return new Host(username, age, city);
    }
}
