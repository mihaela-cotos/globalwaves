package app.pages.Strategy;


import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.pages.utils.Announcement;
import app.user.Host;
import app.user.User;


import java.util.List;

public class HostPageStrategy implements PrintPageStrategy {

    @Override
    public String print(User currentUser) {
        List<Podcast> podcasts = ((Host)currentUser).getPodcasts();
        List<Announcement> announcements = ((Host)currentUser).getAnnouncements();

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
