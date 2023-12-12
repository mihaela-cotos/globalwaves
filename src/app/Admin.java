package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.ArtistPage;
import app.pages.HomePage;
import app.pages.LikedContentPage;
import app.pages.Page;
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

import java.util.*;
import java.util.stream.Collectors;

public class Admin {
    private static List<SimpleUser> users = new ArrayList<>();
    private static List<Artist> artists = new ArrayList<>();
    private static List<Host> hosts = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static List<Album> albums = new ArrayList<>();
    private static int timestamp = 0;

    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        SimpleUserFactory factory = new SimpleUserFactory();
        for (UserInput userInput : userInputList) {
            User newUser = factory.create(userInput.getUsername(), userInput.getAge(),
                                                                    userInput.getCity());
            users.add(((SimpleUser) newUser));
            newUser.setCurrentPage(new HomePage((SimpleUser) newUser));
        }
    }

    public static void setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public static void addSongs(List<Song> songs) {
        Admin.songs.addAll(songs);
    }

    public static void addAlbumToLib(Album album) {
        Admin.albums.add(album);
    }

    public static void setPodcasts(List<PodcastInput> podcastInputList) {
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

    public static List<Artist> getArtists() {
        return new ArrayList<>(artists);
    }

    public static List<Host> getHosts() {
        return new ArrayList<>(hosts);
    }

    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            if (user.getUserType().equals(Enums.UserType.NORMAL)) {
                playlists.addAll(((SimpleUser)user).getPlaylists());
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

    public static User getUser(String username) {
        ArrayList<User> allUsers = gatherAllUsers();

        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static void updateTimestamp(int newTimestamp) {
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

    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        artists = new ArrayList<>();
        hosts = new ArrayList<>();
        timestamp = 0;
    }

    public static String switchConnectionStatus (CommandInput commandInput) {
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
        if (users.stream().anyMatch(user -> user.getUsername().equals(commandInput.getUsername())))
            return "The username " + commandInput.getUsername() + " is already taken.";

        // create new user
        if (commandInput.getType().equals("user")) {
            // normal/simple user
            SimpleUserFactory factory = new SimpleUserFactory();
            SimpleUser user = (SimpleUser) factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            users.add(user);
            HomePage homePage = new HomePage(user);
            user.setCurrentPage(homePage);
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

    public static String addAlbum(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (artists.stream().noneMatch(iterUser -> iterUser.getUsername()
                        .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.ARTIST))
            return commandInput.getUsername() + " is not an artist.";


        return ((Artist)user).addAlbum(commandInput);
    }

    public static String removeAlbum(CommandInput commandInput) {
        return null;
    }

    public static String printCurrentPage(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (user.getUserType().equals(Enums.UserType.NORMAL) && !((SimpleUser)user).isOnlineStatus()) {
            return user.getUsername() + " is offline.";
        }

        Page currentPage = user.getCurrentPage();
        String printMessage;

        if (currentPage == null) {
            return null;
        }

        String pageName = currentPage.getName();

        switch (pageName) {
            case "homePage":
                HomePageStrategy homePage = new HomePageStrategy();
                printMessage = homePage.print(currentPage);
                break;
            case "likedPage":
                LikedContentPageStrategy likedPage = new LikedContentPageStrategy();
                printMessage = likedPage.print(currentPage);
                break;
            case "artistPage":
                ArtistPageStrategy artistPage = new ArtistPageStrategy();
                printMessage = artistPage.print(currentPage);
                break;
            case "hostPage":
                HostPageStrategy hostPage = new HostPageStrategy();
                printMessage = hostPage.print(currentPage);
                break;
            default:
                printMessage = null;
        }

        return printMessage;
    }

    public static String checkArtist(CommandInput commandInput) {
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

    public static String addMerch(CommandInput commandInput) {
        String checkArtist = checkArtist(commandInput);
        if (checkArtist.equals("Valid artist.")) {
            Artist currentArtist = (Artist) Admin.getUser(commandInput.getUsername());
            return currentArtist.addMerch(commandInput);
        }

        return checkArtist;
    }

    public static String addEvent(CommandInput commandInput) {
        String checkArtist = checkArtist(commandInput);
        if (checkArtist.equals("Valid artist.")) {
            Artist currentArtist = (Artist) Admin.getUser(commandInput.getUsername());
            return currentArtist.addEvent(commandInput);
        }

        return checkArtist;
    }

    public static String removeEvent(CommandInput commandInput) {
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
}
