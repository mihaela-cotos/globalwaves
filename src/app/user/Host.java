package app.user;

import app.Admin;
import app.audio.Collections.Podcast;
import app.audio.Collections.PodcastOutput;
import app.audio.Files.Episode;
import app.pages.utils.Announcement;
import app.utils.Enums;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Host type.
 */
@Getter
public final class Host extends User {
    private final ArrayList<Podcast> podcasts;
    private final ArrayList<Announcement> announcements;

    /**
     * Instantiates a new Host.
     */
    public Host(final String username, final int age, final String city) {
        super(username, age, city);
        podcasts = new ArrayList<>();
        announcements = new ArrayList<>();
        setUserType(Enums.UserType.HOST);
        setPageName(Enums.PageType.HOST);
    }

    /**
     * Gets episodes from input format.
     * @param episodeInputList episode input list
     * @return the arraylist of episodes
     */
    public ArrayList<Episode> getEpisodes(final List<EpisodeInput> episodeInputList) {
        ArrayList<Episode> episodes = new ArrayList<>();

        for (EpisodeInput episode : episodeInputList) {
            episodes.add(new Episode(episode.getName(), episode.getDuration(),
                                                    episode.getDescription()));
        }

        return episodes;
    }

    /**
     * Adds podcast to all host's podcasts string.
     * @param commandInput command
     * @return the string
     */
    public String addPodcast(final CommandInput commandInput) {

        if (podcasts.stream().anyMatch(podcast -> podcast.getName()
                                                .equals(commandInput.getName()))) {
            return this.getUsername() + " has another podcast with the same name.";
        }

        for (int i = 0; i < commandInput.getEpisodes().size(); i++) {
            int count = 0;
            for (int j = 0; j < commandInput.getEpisodes().size(); j++) {
                EpisodeInput episodeI = commandInput.getEpisodes().get(i);
                EpisodeInput episodeJ = commandInput.getEpisodes().get(j);
                if (episodeI.getName().equals(episodeJ.getName())) {
                    count++;
                }
            }

            if (count > 1) {
                return commandInput.getUsername()
                        + " has the same episode at least twice in this podcast.";
            }
        }

        // create a new album
        Podcast newPodcast = new Podcast(commandInput.getName(), commandInput.getUsername(),
                this.getEpisodes(commandInput.getEpisodes()));

        podcasts.add(newPodcast);
        Admin.addPodcastToLib(newPodcast);
        return commandInput.getUsername() + " has added new podcast successfully.";
    }

    /**
     * Adds announcement to host page string.
     * @param commandInput command
     * @return the string
     */
    public String addAnnouncement(final CommandInput commandInput) {

        if (announcements.stream().anyMatch(announcement
                                 -> announcement.getName().equals(commandInput.getName()))) {
            return this.getUsername() + " has already added an announcement with this name.";
        }

        // create a new album
        Announcement newAnnounce = new Announcement(commandInput.getName(),
                                                    commandInput.getDescription());
        announcements.add(newAnnounce);
        return commandInput.getUsername() + " has successfully added new announcement.";
    }

    /**
     * Removes announcement from host page.
     * @param commandInput command
     * @return the string
     */
    public String removeAnnouncement(final CommandInput commandInput) {

        if (announcements.stream().noneMatch(announcement
                -> announcement.getName().equals(commandInput.getName()))) {
            return this.getUsername() + " has no announcement with the given name.";
        }

        Announcement announcement = getAnnouncement(commandInput.getName());
        announcements.remove(announcement);
        return commandInput.getUsername() + " has successfully deleted the announcement.";
    }

    /**
     * Searches announcement in all host's announcements.
     * @param name announcement name
     * @return the announcement
     */
    public Announcement getAnnouncement(final String name) {
        for (Announcement announcement : announcements) {
            if (announcement.getName().equals(name)) {
                return announcement;
            }
        }
        return null;
    }

    /**
     * Shows host's podcasts arraylist.
     * @return the arraylist of podcasts
     */
    public ArrayList<PodcastOutput> showPodcasts() {
        ArrayList<PodcastOutput> podcastOutputs = new ArrayList<>();
        for (Podcast podcast : podcasts) {
            podcastOutputs.add(new PodcastOutput(podcast));
        }

        return podcastOutputs;
    }

    /**
     * Removes podcast from all podcasts
     * @param commandInput command
     * @return the string
     */
    public String removePodcast(final CommandInput commandInput) {
        if (podcasts.stream().noneMatch(podcast -> podcast.getName()
                .equals(commandInput.getName()))) {
            return getUsername() + " doesn't have a podcast with the given name.";
        } else if (hasEpisodesFromPodcast(getPodcastFromList(commandInput.getName()))) {
            return getUsername() + " can't delete this podcast.";
        }

        Podcast podcastToRemove = getPodcastFromList(commandInput.getName());
        podcasts.remove(podcastToRemove);
        Admin.updatePodcasts(podcastToRemove);
        return getUsername() + " deleted the podcast successfully.";
    }

    /**
     * Gets all playing now episodes.
     * @return list of episodes
     */
    public List<Episode> allPlayingEpisodes() {
        List<Episode> playingEpisodes = new ArrayList<>();
        List<SimpleUser> users = new ArrayList<>();
        users.addAll(Admin.getSimpleUsers());

        for (SimpleUser user : users) {
            if (user.getPlayer().getCurrentAudioFile() != null
                                   && user.getPlayer().getType().equals("podcast")) {
                playingEpisodes.add((Episode) user.getPlayer().getCurrentAudioFile());
            }
        }
        return playingEpisodes;
    }

    /**
     * Searches a podcast with given name in all podcasts.
     * @param name podcast name
     * @return the podcast
     */
    public Podcast getPodcastFromList(final String name) {
        for (Podcast podcast : this.podcasts) {
            if (podcast.getName().equals(name)) {
                return podcast;
            }
        }
        return null;
    }

    /**
     * Checks if some user has loaded episode from podcast.
     * @param podcast the podcast to be deleted
     * @return boolean
     */
    public boolean hasEpisodesFromPodcast(final Podcast podcast) {
        for (Episode episode : podcast.getEpisodes()) {
            if (allPlayingEpisodes().contains(episode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes host string
     * @param allUsers all existing simple users
     * @return the string
     */
    public String deleteHost(final List<SimpleUser> allUsers) {
        if (myPodcastIsPlaying() || pageIsVisited(allUsers)) {
            return getUsername() + " can't be deleted.";
        }

        for (Podcast podcast : podcasts) {
            Admin.updatePodcasts(podcast);
        }
        Admin.updateHosts(this);
        return getUsername() + " was successfully deleted.";
    }

    /**
     * Gets all playing podcasts.
     * @return list of podcasts
     */
    public List<Podcast> allPlayingPodcasts() {
        List<Podcast> playingPodcasts = new ArrayList<>();
        List<SimpleUser> users = new ArrayList<>();
        users.addAll(Admin.getSimpleUsers());

        for (SimpleUser user : users) {
            if (user.getPlayer().getCurrentAudioFile() != null
                                  && user.getPlayer().getType().equals("podcast")) {
                playingPodcasts.add((Podcast) user.getPlayer().getSource()
                                                              .getAudioCollection());
            }
        }
        return playingPodcasts;
    }

    /**
     * Checks if one of host's podcasts is playing now.
     * @return boolean
     */
    public boolean myPodcastIsPlaying() {
        for (Podcast podcast : allPlayingPodcasts()) {
            if (podcasts.stream().anyMatch(podcast1 -> podcast1.getName().equals(podcast.getName()))
                    && podcast.getOwner().equals(getUsername())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if page is visited by some users.
     * @param allUsers all existing simple users
     * @return boolean
     */
    public boolean pageIsVisited(final List<SimpleUser> allUsers) {
        for (SimpleUser user : allUsers) {
            if (user.getSelectedUser() != null
                    && user.getSelectedUser().equals(this)
                    && user.getPageName().equals(Enums.PageType.HOST)) {
                return true;
            }
        }
        return false;
    }
}
