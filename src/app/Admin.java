package app;

import app.audio.Collections.Album;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.pages.Strategy.*;
import app.player.PlayerSource;
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

public class Admin {
    private static List<SimpleUser> users = new ArrayList<>();
    @Getter
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
            ((SimpleUser) newUser).setPageName(Enums.PageType.HOME);
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


    public static void addPodcastToLib(Podcast podcast) {
        Admin.podcasts.add(podcast);
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

    public static User getUser(String username) {
        ArrayList<User> allUsers = gatherAllUsers();

        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static SimpleUser getSimpleUser(String username) {

        for (SimpleUser user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static Album getAlbum(String name, String username) {
        List<Album> allAlbums = getAlbums();

        for (Album album : allAlbums) {
            if (album.getName().equals(name) && album.getOwner().equals(username)) {
                return album;
            }
        }
        return null;
    }

    public static Song getSong(String name, String username) {
        List<Song> allSongs = getSongs();

        for (Song song : songs) {
            if (song.getName().equals(name) && song.getArtist().equals(username)) {
                return song;
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
        albums = new ArrayList<>();
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

    public static String deleteSimpleUser (SimpleUser userToDelete) {
        List<SimpleUser> users = getSimpleUsers();

        if (users.stream().noneMatch(user -> user.getUsername().equals(userToDelete.getUsername()))) {
            return "The username " + userToDelete.getUsername() + " doesn't exist.";
        }

        return userToDelete.deleteUser(users);
    }

    public static String deleteArtist (Artist artistToDelete) {
        List<String> onlineUsersToString = getOnlineUsers();
        List<SimpleUser> onlineUsers = new ArrayList<>();

        for (String user : onlineUsersToString) {
            User iterUser = getUser(user);
            if (iterUser.getUserType().equals(Enums.UserType.NORMAL))
                onlineUsers.add((SimpleUser) iterUser);
        }

        for (SimpleUser user : onlineUsers) {
            PlayerSource source = user.getPlayer().getSource();
            if (source != null && source.getType().equals(Enums.PlayerSourceType.ALBUM)) {
                Album listenedAlbum = (Album) source.getAudioCollection();
                if (listenedAlbum.getOwner().equals(artistToDelete.getUsername())) {
                    return artistToDelete.getUsername() + " can't be deleted.";
                }
            } else if (source != null && source.getType().equals(Enums.PlayerSourceType.LIBRARY)) {
                Song listenedSong = (Song) source.getAudioFile();
                if (listenedSong.getArtist().equals(artistToDelete.getUsername())) {
                    return artistToDelete.getUsername() + " can't be deleted.";
                }
            }
        }

        for (SimpleUser user : users) {
            // delete playlist from followed
            ArrayList<Song> likedSongs = user.getLikedSongs();
            ArrayList<Song> deleteSongs = getSongsFromArtist(artistToDelete);
            likedSongs.removeAll(deleteSongs);
            //            a.removeAll(artistToDelete.getSongs());
//            user.getLikedSongs().removeAll(getSongsFromArtist(artistToDelete));
            user.setLikedSongs(likedSongs);
        }

        songs.removeAll(getSongsFromArtist(artistToDelete));
        albums.removeAll(artistToDelete.getAlbums());
        artists.remove(artistToDelete);
        return artistToDelete.getUsername() + " was successfully deleted.";
    }

    public static ArrayList<Song> getSongsFromArtist(Artist artist) {
        ArrayList<Song> artistSongs = new ArrayList<>();
        for (Song song : songs) {
            if (song.getArtist().equals(artist.getUsername())) {
                artistSongs.add(song);
            }
        }
        return artistSongs;
    }

    public static String deleteHost (Host hostToDelete) {
        List<SimpleUser> listeners = getSimpleUsers();
        List<Host> hosts = getHosts();

        if (hosts.stream().noneMatch(host -> host.getUsername().equals(hostToDelete.getUsername()))) {
            return "The username " + hostToDelete.getUsername() + " doesn't exist.";
        }

        return hostToDelete.deleteHost(listeners);
    }

    public static String deleteUser(CommandInput commandInput) {
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

    public static String addAlbum(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                        .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.ARTIST))
            return commandInput.getUsername() + " is not an artist.";


        return ((Artist)user).addAlbum(commandInput);
    }

    public static String removeAlbum(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.ARTIST))
            return commandInput.getUsername() + " is not an artist.";


        return ((Artist)user).removeAlbum(commandInput);
    }

    public static void updateAlbums(Album album) {
        albums.remove(album);
    }

    public static void updatePodcasts(Podcast podcast) {
        podcasts.remove(podcast);
    }

    public static void updateUsers(SimpleUser user) {
        users.remove(user);
    }

    public static void updateHosts(Host user) {
        hosts.remove(user);
    }

    public static String addPodcast(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).addPodcast(commandInput);
    }

    public static String removePodcast(CommandInput commandInput) {
        User user = getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)

            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).removePodcast(commandInput);
    }

    public static String printCurrentPage(CommandInput commandInput) {
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

    public static String addAnnouncement(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)
            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).addAnnouncement(commandInput);
    }

    public static String removeAnnouncement(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        if (getAllUsers().stream().noneMatch(iterUser -> iterUser
                .equals(commandInput.getUsername())) || user == null)
            return "The username " + commandInput.getUsername() + " doesn't exist.";

        if (!user.getUserType().equals(Enums.UserType.HOST))
            return commandInput.getUsername() + " is not a host.";


        return ((Host)user).removeAnnouncement(commandInput);
    }

    public static String changePage(CommandInput commandInput) {
        SimpleUser user = getSimpleUser(commandInput.getUsername());
        Enums.PageType currentPage = user.getPageName();
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
