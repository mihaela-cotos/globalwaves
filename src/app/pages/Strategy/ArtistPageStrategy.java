package app.pages.Strategy;

import app.audio.Collections.Album;
import app.pages.utils.Event;
import app.pages.utils.Merch;
import app.user.Artist;
import app.user.User;

import java.util.ArrayList;
import java.util.List;

public class ArtistPageStrategy implements PrintPageStrategy {

    @Override
    public String print(User currentUser) {
        List<Album> albums = new ArrayList<>();
        albums.addAll(((Artist)currentUser).getAlbums());
        List<Merch> merch = ((Artist)currentUser).getMerch();
        List<Event> events = ((Artist)currentUser).getEvents();

        String pageLog = "Albums:\n\t[";
        for (int i = 0; i < albums.size(); i++) {
            pageLog += albums.get(i).getName();
            if (i != albums.size() - 1) {
                pageLog += ", ";
            }
        }

        pageLog += "]\n\nMerch:\n\t[";
        for (int i = 0; i < merch.size(); i++) {
            pageLog += merch.get(i).getName();
            pageLog += " - ";
            pageLog += merch.get(i).getPrice();
            pageLog += ":\n\t";
            pageLog += merch.get(i).getDescription();
            if (i != merch.size() - 1) {
                pageLog += ", ";
            }
        }

        pageLog += "]\n\nEvents:\n\t[";
        for (int i = 0; i < events.size(); i++) {
            pageLog += events.get(i).getName();
            pageLog += " - ";
            pageLog += events.get(i).getDate();
            pageLog += ":\n\t";
            pageLog += events.get(i).getDescription();
            if (i != events.size() - 1) {
                pageLog += ", ";
            }
        }
        pageLog += "]";
        return pageLog;
    }

}
