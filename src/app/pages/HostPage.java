package app.pages;

import app.audio.Collections.Podcast;
import app.pages.utils.Announcement;
import app.user.Artist;
import app.user.Host;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class HostPage extends Page {
    // podcasturi, anunturi
    private ArrayList<Podcast> podcasts;
    private ArrayList<Announcement> announcements;
    public HostPage(final Host currentUser) {
        this.name = "hostPage";
        podcasts = ((Host)currentUser).getPodcasts();
        announcements = ((Host)currentUser).getAnnouncements();
        //update(currentUser);
    }

//    public void update(final Host currentUser) {
//        podcasts = ((Host)currentUser).getPodcasts();
//        announcements = ((Host)currentUser).getAnnouncements();
//    }
}
