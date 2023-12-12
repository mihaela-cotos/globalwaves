package app.pages.Strategy;


import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.pages.ArtistPage;
import app.pages.HostPage;
import app.pages.Page;
import app.pages.utils.Announcement;


import java.util.ArrayList;
import java.util.List;

public class HostPageStrategy implements PrintPageStrategy {

    @Override
    public String print(Page page) {
        List<Podcast> podcasts = ((HostPage)page).getPodcasts();
        List<Announcement> announcements = ((HostPage)page).getAnnouncements();


        String pageLog = "Podcasts:\n\t[";
        for (int i = 0; i < podcasts.size(); i++) {
            pageLog += podcasts.get(i).getName();
            pageLog += ":\n\t[";
            List<Episode> episodes = podcasts.get(i).getEpisodes();
            for (int j = 0; j < episodes.size(); j++) {
                pageLog += episodes.get(i).getName();
                pageLog += " - ";
                pageLog += episodes.get(i).getDescription();

                if (j != episodes.size() - 1) {
                    pageLog += ", ";
                }
            }
            pageLog += ']';

            if (i != podcasts.size() - 1) {
                pageLog += ", ";
            }
        }

        pageLog += "]\n\nAnnouncements\n\t[";
        for (int i = 0; i < announcements.size(); i++) {
            pageLog += announcements.get(i).getName();
            pageLog += " - ";
            pageLog += announcements.get(i).getDescription();

            if (i != announcements.size() - 1) {
                pageLog += ", ";
            } else {
                pageLog += "]";
            }
        }

        return pageLog;
    }
}
