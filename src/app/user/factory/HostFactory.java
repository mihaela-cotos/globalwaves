package app.user.factory;

import app.user.Host;
import app.user.User;

public class HostFactory extends UserFactory {

    @Override
    public User create(String username, int age, String city) {
        return new Host(username, age, city);
    }
}
