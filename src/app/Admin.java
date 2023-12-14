package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.Strategy.*;
import app.user.Artist;
import app.user.Host;
import app.user.SimpleUser;
import app.user.User;
import app.user.factory.ArtistFactory;
import app.user.factory.HostFactory;
import app.user.factory.SimpleUserFactory;
import app.utils.Enums;
import fileio.input.*;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Admin.
 */
public class Admin {
    private static List<SimpleUser> users = new ArrayList<>();
    @Getter
    private static List<Artist> artists = new ArrayList<>();
    private static List<Host> hosts = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;

    /**
     * Sets users.
     *
     * @param userInputList the user input list
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        SimpleUserFactory factory = new SimpleUserFactory();
        for (UserInput userInput : userInputList) {
            User newUser = factory.create(userInput.getUsername(),
                                          userInput.getAge(), userInput.getCity());
            users.add((SimpleUser) newUser);
            newUser.setPageName(Enums.PageType.HOME);
        }
    }

    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public static void addSongs(final List<Song> songs) {
        Admin.songs.addAll(songs);
    }

    public static void addAlbumToLib(final Album album) {
        Admin.albums.add(album);
    }


    public static void addPodcastToLib(final Podcast podcast) {
        Admin.podcasts.add(podcast);
    }

    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public static List<SimpleUser> getSimpleUsers() {
        //return new ArrayList<>(users);
        return users;
    }

    public static List<Host> getHosts() {
        return new ArrayList<>(hosts);
    }

    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    public static List<Album> getAlbums() {
        return new ArrayList<>(albums);
    }


    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (SimpleUser user : users) {
            if (user.getUserType().equals(Enums.UserType.NORMAL)) {
                playlists.addAll(user.getPlaylists());
            }
        }
        return playlists;
    }

    public static ArrayList<User> gatherAllUsers() {
        ArrayList<User> allUsers = new ArrayList<>();
        allUsers.addAll(users);
        allUsers.addAll(artists);
        allUsers.addAll(hosts);

        return allUsers;
    }

    public static User getUser(final String username) {
        ArrayList<User> allUsers = gatherAllUsers();

        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static SimpleUser getSimpleUser(final String username) {

        for (SimpleUser user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            if (user.getUserType().equals(Enums.UserType.NORMAL) && ((SimpleUser)user).isOnlineStatus())
                ((SimpleUser)user).simulateTime(elapsed);
        }
    }

    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= 5) break;
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());
        sortedAlbums.sort(Comparator.comparingInt(Album::getNumberOfLikes).reversed()
                .thenComparing(Album::getName, Comparator.naturalOrder()));
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= 5) break;
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }

    public static List<String> getTop5Artists() {
        List<Artist> sortedArtists = new ArrayList<>(getArtists());
        sortedArtists.sort(Comparator.comparingInt(Artist::getNumberOfLikes).reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (Artist artist : sortedArtists) {
            if (count >= 5) break;
            topArtists.add(artist.getUsername());
            count++;
        }
        return topArtists;
    }

    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
        albums = new ArrayList<>();
        timestamp = 0;
    }

    public static String switchConnectionStatus (final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (user == null) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        if (user.getUserType() == Enums.UserType.NORMAL) {
            if (((SimpleUser)user).isOnlineStatus()) {
                ((SimpleUser)user).setOnlineStatus(false);
            } else {
                ((SimpleUser)user).setOnlineStatus(true);
            }
            return user.getUsername() + " has changed status successfully.";
        } else {
            return user.getUsername() + " is not a normal user.";
        }
    }

    public static List<String> getOnlineUsers() {
        List<User> onlineUsers = new ArrayList<>();

        for (User user : users) {
            if (user.getUserType() == Enums.UserType.NORMAL) {
                if (((SimpleUser) user).isOnlineStatus()) {
                    onlineUsers.add(user);
                }
            }
        }

        List<String> onlineUsersToString = new ArrayList<>();
        for (User user : onlineUsers) {
            onlineUsersToString.add(user.getUsername());
        }

        return onlineUsersToString;
    }

    public static List<String> getAllUsers() {
        List<User> allUsers = gatherAllUsers();

        return allUsers.stream().map(User::getUsername).collect(Collectors.toList());
    }

    public static String addUser(CommandInput commandInput) {
        if (getAllUsers().stream().anyMatch(user -> user.equals(commandInput.getUsername())))
            return "The username " + commandInput.getUsername() + " is already taken.";

        // create new user
        if (commandInput.getType().equals("user")) {
            // normal/simple user
            SimpleUserFactory factory = new SimpleUserFactory();
            SimpleUser user = (SimpleUser) factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            users.add(user);
            user.setPageName(Enums.PageType.HOME);
        } else if (commandInput.getType().equals("artist")) {
            // artist
            ArtistFactory factory = new ArtistFactory();
            User artist = factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            artists.add((Artist) artist);
        } else {
            // host
            HostFactory factory = new HostFactory();
            User host = factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            hosts.add((Host) host);
        }

        return "The username " + commandInput.getUsername() + " has been added successfully.";
    }

    public static String deleteSimpleUser (final SimpleUser userToDelete) {
        List<SimpleUser> users = getSimpleUsers();

        if (users.stream().noneMatch(user -> user.getUsername().equals(userToDelete.getUsername()))) {
            return "The username " + userToDelete.getUsername() + " doesn't exist.";
        }

        return userToDelete.deleteUser(users);
    }

    public static String deleteHost (final Host hostToDelete) {
        List<SimpleUser> listeners = getSimpleUsers();
        List<Host> hosts = getHosts();

        if (hosts.stream().noneMatch(host -> host.getUsername().equals(hostToDelete.getUsername()))) {
            return "The username " + hostToDelete.getUsername() + " doesn't exist.";
        }

        return hostToDelete.deleteHost(listeners);
    }

    public static String deleteArtist (final Artist artistToDelete) {
        List<Artist> artists = getArtists();

        if (artists.stream().noneMatch(artist -> artist.getUsername().equals(artistToDelete.getUsername()))) {
            return "The username " + artistToDelete.getUsername() + " doesn't exist.";
        }

        return artistToDelete.deleteArtist();
    }

    public static String deleteUser(final CommandInput commandInput) {
        String searchedUser = commandInput.getUsername();
        List<User> allUsers = gatherAllUsers();

        if (allUsers.stream().noneMatch(user -> user.getUsername().equals(searchedUser))) {
            return "The username " + searchedUser + " doesn't exist.";
        }

        User userToDelete = getUser(commandInput.getUsername());
        Enums.UserType type = userToDelete.getUserType();

        return switch (type) {
            case NORMAL -> deleteSimpleUser((SimpleUser) userToDelete);
            case ARTIST -> deleteArtist((Artist) userToDelete);
            case HOST -> deleteHost((Host) userToDelete);
        };
    }

    public static String addAlbum(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                        .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.ARTIST))
            return commandInput.getUsername() + " is not an artist.";


        return ((Artist)user).addAlbum(commandInput);
    }

    public static String removeAlbum(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.ARTIST))
            return commandInput.getUsername() + " is not an artist.";


        return ((Artist)user).removeAlbum(commandInput);
    }

    public static void updateAlbums(final Album album) {
        albums.remove(album);
    }

    public static void updateArtists(final Artist artist) {
        artists.remove(artist);
    }

    public static void updateSongs(final List<Song> removedSongs) {
        songs.removeAll(removedSongs);
    }

    public static void updatePodcasts(final Podcast podcast) {
        podcasts.remove(podcast);
    }

    public static void updateUsers(final SimpleUser user) {
        users.remove(user);
    }

    public static void updateHosts(final Host user) {
        hosts.remove(user);
    }

    public static String addPodcast(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).addPodcast(commandInput);
    }

    public static String removePodcast(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).removePodcast(commandInput);
    }

    public static String printCurrentPage(final CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (user.getUserType().equals(Enums.UserType.NORMAL) && !((SimpleUser)user).isOnlineStatus()) {
            return user.getUsername() + " is offline.";
        }

        String printMessage;

        Enums.PageType pageName = user.getPageName();

        switch (pageName) {
            case HOME:
                HomePageStrategy homePage = new HomePageStrategy();
                printMessage = homePage.print(user);
                break;
            case LIKED:
                LikedContentPageStrategy likedPage = new LikedContentPageStrategy();
                printMessage = likedPage.print(user);
                break;
            case ARTIST:
                ArtistPageStrategy artistPage = new ArtistPageStrategy();
                if (((SimpleUser)user).getSelectedUser() != null && ((SimpleUser)user).getSelectedUser().getUserType().equals(Enums.UserType.ARTIST))
                    printMessage = artistPage.print(((SimpleUser)user).getSelectedUser());
                else
                    printMessage = null;
                break;
            case HOST:
                HostPageStrategy hostPage = new HostPageStrategy();
                if (((SimpleUser)user).getSelectedUser() != null && ((SimpleUser)user).getSelectedUser().getUserType().equals(Enums.UserType.HOST))
                    printMessage = hostPage.print(((SimpleUser)user).getSelectedUser());
                else
                    printMessage = null;
                break;
            default:
                printMessage = null;
        }

        return printMessage;
    }

    public static String checkArtist(final CommandInput commandInput) {
        ArrayList<User> allUsers = gatherAllUsers();
        if (allUsers.stream().noneMatch(user -> user.getUsername()
                .equals(commandInput.getUsername()))) {
            return "The username " + commandInput.getUsername() + " doesn't exist.";

        } else if (artists.stream().noneMatch(artist -> artist.getUsername()
                .equals(commandInput.getUsername()))) {
            return commandInput.getUsername() + " is not an artist.";
        } else {
            return "Valid artist.";
        }
    }

    public static String addMerch(final CommandInput commandInput) {
        String checkArtist = checkArtist(commandInput);
        if (checkArtist.equals("Valid artist.")) {
            Artist currentArtist = (Artist) Admin.getUser(commandInput.getUsername());
            return currentArtist.addMerch(commandInput);
        }

        return checkArtist;
    }

    public static String addEvent(final CommandInput commandInput) {
        String checkArtist = checkArtist(commandInput);
        if (checkArtist.equals("Valid artist.")) {
            Artist currentArtist = (Artist) Admin.getUser(commandInput.getUsername());
            return currentArtist.addEvent(commandInput);
        }

        return checkArtist;
    }

    public static String removeEvent(final CommandInput commandInput) {
        String checkArtist = checkArtist(commandInput);
        if (checkArtist.equals("Valid artist.")) {
            Artist currentArtist = (Artist) Admin.getUser(commandInput.getUsername());
            if (currentArtist.getEvents().stream().noneMatch(event -> event.getName()
                                                    .equals(commandInput.getName()))) {
                return commandInput.getUsername() + " doesn't have an event with the given name.";
            }
            // get event from event list
            currentArtist.getEvents().remove(currentArtist.getEvent(commandInput.getName()));
            return commandInput.getUsername() + " deleted the event successfully.";
        }

        return checkArtist;
    }

    public static String addAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)
            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).addAnnouncement(commandInput);
    }

    public static String removeAnnouncement(final CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)
            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).removeAnnouncement(commandInput);
    }

    public static String changePage(final CommandInput commandInput) {
        SimpleUser user = getSimpleUser(commandInput.getUsername());
        String nextPage = commandInput.getNextPage();

        switch (nextPage) {
            case "Home":
                user.setPageName(Enums.PageType.HOME);
                break;
            case "LikedContent":
                user.setPageName(Enums.PageType.LIKED);
                break;
            case "Artist":
                user.setPageName(Enums.PageType.ARTIST);
                break;
            case "Host" :
                user.setPageName(Enums.PageType.HOST);
                break;
            default:
                return user.getUsername() + " is trying to access a non-existent page.";
        }
        return user.getUsername() + " accessed " + nextPage + " successfully.";
    }
}
