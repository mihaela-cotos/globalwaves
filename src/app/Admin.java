package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.HomePage;
import app.pages.Page;
import app.pages.Strategy.HomePageStrategy;
import app.user.Artist;
import app.user.SimpleUser;
import app.user.User;
import app.user.factory.ArtistFactory;
import app.user.factory.HostFactory;
import app.user.factory.SimpleUserFactory;
import app.utils.Enums;
import app.utils.MagicNumbers;
import fileio.input.*;

import java.util.*;

public class Admin {
    private static List<User> users = new ArrayList<>();
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
            users.add(newUser);
            newUser.setCurrentPage(new HomePage());
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

    public static User getUser(String username) {
        for (User user : users) {
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
            if (user.getUserType().equals(Enums.UserType.NORMAL))
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

    public static String addUser(CommandInput commandInput) {
        if (users.stream().anyMatch(user -> user.getUsername().equals(commandInput.getUsername())))
            return "The username " + commandInput.getUsername() + " is already taken.";

        // create new user
        if (commandInput.getType().equals("user")) {
            // normal/simple user
            SimpleUserFactory factory = new SimpleUserFactory();
            User user = factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            users.add(user);
            HomePage homePage = new HomePage();
            homePage.updateHomePage(commandInput);
            user.setCurrentPage(homePage);
        } else if (commandInput.getType().equals("artist")) {
            // artist
            ArtistFactory factory = new ArtistFactory();
            User artist = factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            users.add(artist);
        } else {
            // host
            HostFactory factory = new HostFactory();
            User host = factory.create(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            users.add(host);
        }

        return "The username " + commandInput.getUsername() + " has been added successfully.";
    }

    public static String addAlbum(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (users.stream().noneMatch(iterUser -> iterUser.getUsername()
                        .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.ARTIST))
            return commandInput.getUsername() + " is not an artist.";

        ((Artist)user).setSongs(commandInput.getSongs());
        songs.addAll(((Artist)user).getSongs());
        albums.addAll(((Artist)user).getAlbums());
        return ((Artist)user).addAlbum(commandInput);
    }

    public static String printCurrentPage(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());
        HomePageStrategy printStrategy = new HomePageStrategy();
        Page usersPage = user.getCurrentPage();
        ((HomePage)usersPage).updateHomePage(commandInput);
        return printStrategy.print(user.getCurrentPage());
    }


}
