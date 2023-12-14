package app.user;

import app.Admin;
import app.audio.Collections.*;
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

    public String removePodcast(CommandInput commandInput) {
        if (podcasts.stream().noneMatch(podcast -> podcast.getName()
                .equals(commandInput.getName()))) {
            return getUsername() + " doesn't have a podcast with the given name.";
        } else if (checkValidDeletion(getPodcastFromList(commandInput.getName()))) {
            return getUsername() + " can't delete this podcast.";
        }

        Podcast podcastToRemove = getPodcastFromList(commandInput.getName());
        podcasts.remove(podcastToRemove);
        Admin.updatePodcasts(podcastToRemove);
        return getUsername() + " deleted the podcast successfully.";
    }

    public List<Episode> allPlayingEpisodes() {
        List<Episode> playingEpisodes = new ArrayList<>();
        List<SimpleUser> users = new ArrayList<>();
        users.addAll(Admin.getSimpleUsers());

        for (SimpleUser user : users) {
            if (user.getPlayer().getCurrentAudioFile() != null && user.getPlayer().getType().equals("podcast")) {
                playingEpisodes.add((Episode) user.getPlayer().getCurrentAudioFile());
            }
        }
        return playingEpisodes;
    }

    public Podcast getPodcastFromList (String name) {
        for (Podcast podcast : this.podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    public boolean checkValidDeletion(Podcast podcast) {
        List<Episode> playingEpisodes = this.allPlayingEpisodes();
        return hasEpisodesFromPodcast(podcast, playingEpisodes);
    }

    public boolean hasEpisodesFromPodcast(Podcast podcast, List<Episode> allPlayingEpisodes) {
        for (Episode episode : podcast.getEpisodes()) {
            if (allPlayingEpisodes().contains(episode)) {
                return true;
            }
        }
        return false;
    }

    public String deleteHost(List<SimpleUser> listeners) {
        if (myPodcastIsPlaying() || pageIsVisited(listeners)) {
            return getUsername() + " can't be deleted.";
        }

        for (Podcast podcast : podcasts) {
            Admin.updatePodcasts(podcast);
        }
        Admin.updateHosts(this);
        return getUsername() + " was successfully deleted.";
    }

    public List<Podcast> allPlayingPodcasts() {
        List<Podcast> playingPodcasts = new ArrayList<>();
        List<SimpleUser> users = new ArrayList<>();
        users.addAll(Admin.getSimpleUsers());

        for (SimpleUser user : users) {
            if (user.getPlayer().getCurrentAudioFile() != null && user.getPlayer().getType().equals("podcast")) {
                playingPodcasts.add((Podcast) user.getPlayer().getSource().getAudioCollection());
            }
        }
        return playingPodcasts;
    }

    public boolean myPodcastIsPlaying() {
        for (Podcast podcast : allPlayingPodcasts()) {
            if (podcasts.stream().anyMatch(podcast1 -> podcast1.getName().equals(podcast.getName()))
                    && podcast.getOwner().equals(getUsername())) {
                System.out.println("cineva ma ascultaaa ");
                return true;
            }
        }
        return false;
    }

    public boolean pageIsVisited(List<SimpleUser> listeners) {
        System.out.println("here i ammmmm");
        for (SimpleUser listener : listeners) {
            if (listener.getSelectedUser() != null && listener.getSelectedUser().equals(this)) {
                System.out.println("cineva ma viziteaza   ");
                return true;
            }
        }
        return false;
    }


}
