package app.user.factory;

import app.user.Artist;
import app.user.User;

public class ArtistFactory extends UserFactory {
    @Override
    public User create(String username, int age, String city) {
        return new Artist(username, age, city);
    }
}
