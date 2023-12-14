package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Song;
import app.pages.utils.Event;
import app.pages.utils.Merch;
import app.utils.Enums;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Artist extends User {
    private ArrayList<Album> albums;
    private ArrayList<Event> events;
    private ArrayList<Merch> merch;

    public Artist(String username, int age, String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        events = new ArrayList<>();
        merch = new ArrayList<>();
        setPageName(Enums.PageType.ARTIST);
        setUserType(Enums.UserType.ARTIST);
    }

    public int getNumberOfLikes() {
        int likes = 0;
        List<Integer> albumsLikes = albums.stream().map(Album::getNumberOfLikes).toList();
        for (Integer nrLikes : albumsLikes) {
            likes += nrLikes;
        }
        return likes;
    }

    public String addAlbum(CommandInput commandInput) {

        if (albums.stream().anyMatch(album -> album.getName().equals(commandInput.getName())))
            return this.getUsername() + " has another album with the same name.";

        for (int i = 0; i < commandInput.getSongs().size(); i++) {
            int count = 0;
            for (int j = 0; j < commandInput.getSongs().size(); j++) {
                SongInput songI = commandInput.getSongs().get(i);
                SongInput songJ = commandInput.getSongs().get(j);
                if (songI.getName().equals(songJ.getName())) {
                    count++;
                }
            }

            if (count > 1) {
                return commandInput.getUsername() + " has the same song at least twice in this album.";
            }
        }

        // create a new album
        Album newAlbum = new Album(commandInput.getName(), commandInput.getUsername(),
                commandInput.getSongs(), commandInput.getTimestamp(),
                commandInput.getDescription(), commandInput.getReleaseYear());

        albums.add(newAlbum);
        Admin.addSongs(newAlbum.getSongs());
        Admin.addAlbumToLib(newAlbum);
        return commandInput.getUsername() + " has added new album successfully.";
    }

    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }

        return albumOutputs;
    }

    public String addMerch(CommandInput commandInput) {
        if (merch.stream().anyMatch(item -> item.getName().equals(commandInput.getName()))) {
            return getUsername() + " has merchandise with the same name.";
        } else if (commandInput.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }

        Merch newMerch = new Merch(commandInput.getName(), commandInput.getDescription(), commandInput.getPrice());
        merch.add(newMerch);
        return getUsername() + " has added new merchandise successfully.";
    }

    public boolean checkValidDate(String eventDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {
            LocalDate date = LocalDate.parse(eventDate, formatter);
            int day = date.getDayOfMonth();
            int month = date.getMonthValue();
            int year = date.getYear();

            if (day >= 1 && day <= 31 && month >= 1 && month <= 12 && year >= 1900 && year <= 2023) {
                if (month == 2) {
                    if (day > 28) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (DateTimeException exception) {
            return false;
        }
    }

    public String addEvent(CommandInput commandInput) {
        if (events.stream().anyMatch(event -> event.getName()
                .equals(commandInput.getName()))) {
            return getUsername() + " has another event with the same name.";
        } else if (!checkValidDate(commandInput.getDate())) {
            return "Event for " +getUsername() + " does not have a valid date.";
        }

        Event newEvent = new Event(commandInput.getName(), commandInput.getDescription(), commandInput.getDate());
        events.add(newEvent);
        return getUsername() + " has added new event successfully.";
    }

    public Album getAlbumFromList (String name) {
        for (Album album : this.albums) {
            if (album.getName().equals(name)) {
                return album;
            }
        }
        return null;
    }

    public Event getEvent(String name) {
        for (Event event : events) {
            if (event.getName().equals(name)) {
                return event;
            }
        }

        return null;
    }

    public String removeAlbum(CommandInput commandInput) {
        if (albums.stream().noneMatch(album -> album.getName()
                .equals(commandInput.getName()))) {
            return getUsername() + " doesn't have an album with the given name.";
        } else if (checkValidDeletion(getAlbumFromList(commandInput.getName()))) {
            System.out.println("can't delete album " + commandInput.getName());
            return getUsername() + " can't delete this album.";
        }

        Album albumToRemove = getAlbumFromList(commandInput.getName());
        albums.remove(albumToRemove);
        Admin.updateAlbums(albumToRemove);
        return getUsername() + " deleted the album successfully.";
    }

    public List<Song> allPlayingSongs() {
        List<Song> playingSongs = new ArrayList<>();
        List<SimpleUser> users = new ArrayList<>();
        users.addAll(Admin.getSimpleUsers());

        for (SimpleUser user : users) {
            if (user.getPlayer().getCurrentAudioFile() != null && !user.getPlayer().getType().equals("podcast")) {
                playingSongs.add((Song) user.getPlayer().getCurrentAudioFile());
            }
        }
        return playingSongs;
    }

    public List<Song> allPlaylistSongs() {
        List<SimpleUser> users = Admin.getSimpleUsers();

        return users.stream().map(user -> user.getPlaylists()).flatMap(Collection::stream)
                             .map(playlist -> playlist.getSongs()).flatMap(Collection::stream)
                             .collect(Collectors.toList());

    }

    public boolean checkValidDeletion(Album album) {
        for (Song song : allPlayingSongs()) {
            if (album.getSongs().contains(song)) {
                return true;
            }
        }

        for (Song song : allPlaylistSongs()) {
            if (album.getSongs().contains(song)) {
                return true;
            }
        }

        return false;
    }

    public List<Song> mySongs() {
        return albums.stream().map(album -> album.getSongs())
                              .flatMap(Collection::stream)
                              .collect(Collectors.toList());
    }

    public String deleteArtist() {
        if (mySongIsPlaying() || pageIsVisited()) {
            return getUsername() + " can't be deleted.";
        }

        for (SimpleUser user : Admin.getSimpleUsers()) {
            ArrayList<Song> updateUserSongs = user.getLikedSongs();
            updateUserSongs.removeAll(mySongs());
            user.setLikedSongs(updateUserSongs);
        }

        Admin.updateArtists(this);

        for (Album album : albums) {
            Admin.updateAlbums(album);
        }

        Admin.updateSongs(mySongs());
        return getUsername() + " was successfully deleted.";
    }

    public boolean pageIsVisited() {
        for (SimpleUser user : Admin.getSimpleUsers()) {
            if (user.getSelectedUser() != null
                    && user.getSelectedUser().equals(this)
                    && user.getPageName().equals(Enums.PageType.ARTIST)) {
                return true;
            }
        }
        return false;
    }

    public boolean mySongIsPlaying() {
        for (Song song : mySongs()) {
            if (allPlayingSongs().contains(song)) {
                return true;
            }
        }
        return false;
    }

}
