package app.pages.Strategy;

import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.pages.utils.Announcement;
import app.user.Host;
import app.user.User;

import java.util.List;

public final class HostPageStrategy implements PrintPageStrategy {

    @Override
    public String print(final User currentUser) {
        List<Podcast> podcasts = ((Host) currentUser).getPodcasts();
        List<Announcement> announcements = ((Host) currentUser).getAnnouncements();

        String pageLog = "Podcasts:\n\t[";
        for (int i = 0; i < podcasts.size(); i++) {
            List<Episode> podcastEpisodes = podcasts.get(i).getEpisodes();

            pageLog += podcasts.get(i).getName();
            pageLog += ":\n\t[";

            for (int j = 0; j < podcastEpisodes.size(); j++) {
                pageLog += podcastEpisodes.get(j).getName();
                pageLog += " - ";
                pageLog += podcastEpisodes.get(j).getDescription();

                if (j != podcastEpisodes.size() - 1) {
                    pageLog += ", ";
                }
            }
            pageLog += "]\n";

            if (i != podcasts.size() - 1) {
                pageLog += ", ";
            }
        }

        pageLog += "]\n\nAnnouncements:\n\t[";
        for (int i = 0; i < announcements.size(); i++) {
            pageLog += announcements.get(i).getName();
            pageLog += ":\n\t";
            pageLog += announcements.get(i).getDescription();

            if (i != announcements.size() - 1) {
                pageLog += ", ";
            }
        }
        pageLog += "\n]";
        return pageLog;
    }
}
