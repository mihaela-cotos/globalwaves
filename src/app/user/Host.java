package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.utils.Announcement;
import app.utils.Enums;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Host extends User {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;
    public Host(String username, int age, String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        setUserType(Enums.UserType.HOST);
        setPageName(Enums.PageType.HOST);
    }

    public ArrayList<Episode> getEpisodes(List<EpisodeInput> episodeInputList) {
        ArrayList<Episode> episodes = new ArrayList<>();

        for (EpisodeInput episode : episodeInputList) {
            System.out.println("de citit citim : " + episode.getName() + " " + episode.getDuration() + "  " + episode.getDescription());
            episodes.add(new Episode(episode.getName(), episode.getDuration(),
                                                    episode.getDescription()));
        }

        return episodes;
    }

    public String addPodcast(CommandInput commandInput) {

        if (podcasts.stream().anyMatch(podcast -> podcast.getName().equals(commandInput.getName())))
            return this.getUsername() + " has another podcast with the same name.";

        // create a new album
        Podcast newPodcast = new Podcast(commandInput.getName(), commandInput.getUsername(),
                this.getEpisodes(commandInput.getEpisodes()));
//        podcasts.add(newPodcast);

        for (Episode episode : newPodcast.getEpisodes()) {
            System.out.println("                episodele        ");
            System.out.println(episode.getName());
            System.out.println("                        ");
            boolean contains = newPodcast.containsEpisode(episode);
            if (contains) {
                //newPodcast.getEpisodes().remove(episode);
                return getUsername() + " has the same episode at least twice in this podcast.";
            }
        }
        podcasts.add(newPodcast);
        Admin.addPodcastToLib(newPodcast);
        return commandInput.getUsername() + " has added new podcast successfully.";
    }

    public String addAnnouncement(CommandInput commandInput) {

        if (announcements.stream().anyMatch(announcement
                                 -> announcement.getName().equals(commandInput.getName())))
            return this.getUsername() + " has already added an announcement with this name.";

        // create a new album
        Announcement newAnnounce = new Announcement (commandInput.getName(), commandInput.getDescription());
        announcements.add(newAnnounce);
        return commandInput.getUsername() + " has successfully added new announcement.";
    }

    public String removeAnnouncement(CommandInput commandInput) {

        if (announcements.stream().noneMatch(announcement
                -> announcement.getName().equals(commandInput.getName())))
            return this.getUsername() + " has no announcement with the given name.";

        Announcement announcement = getAnnouncement(commandInput.getName());
        announcements.remove(announcement);
        return commandInput.getUsername() + " has successfully deleted the announcement.";
    }

    public Announcement getAnnouncement(String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return announcement;
            }
        }
        return null;
    }

    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }

        return podcastOutputs;
    }

}
