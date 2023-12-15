package app.user.factory;

import app.user.Artist;
import app.user.User;

public final class ArtistFactory extends UserFactory {
    @Override
    public User create(final String username, final int age, final String city) {
        return new Artist(username, age, city);
    }
}
