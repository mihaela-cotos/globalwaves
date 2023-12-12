package app.pages;

import app.audio.Collections.Album;
import app.pages.utils.*;
import app.user.Artist;
import lombok.Getter;

import java.util.ArrayList;
@Getter
public class ArtistPage extends Page {
    private ArrayList<Album> albums;
    private ArrayList<Event> events;
    private ArrayList<Merch> merch;

    public ArtistPage(final Artist currentUser) {
        this.name = "artistPage";
        albums = currentUser.getAlbums();
        events = currentUser.getEvents();
        merch = currentUser.getMerch();
    }
}
