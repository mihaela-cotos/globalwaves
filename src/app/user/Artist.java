package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
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
import java.util.List;

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

    public ArrayList<Song> getSongs(List<SongInput> songInputList) {
        ArrayList<Song> songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
        return songs;
    }

    public String addAlbum(CommandInput commandInput) {

        if (albums.stream().anyMatch(album -> album.getName().equals(commandInput.getName())))
            return this.getUsername() + " has another album with the same name.";

        // create a new album
        Album newAlbum = new Album(commandInput.getName(), commandInput.getUsername(),
                                   commandInput.getSongs(), commandInput.getTimestamp(),
                                   commandInput.getDescription(), commandInput.getReleaseYear());
        this.albums.add(newAlbum);
//        setSongs(commandInput.getSongs());

        for (SongInput song : commandInput.getSongs()) {
            boolean contains = newAlbum.containsSong(song);
            if (contains) {
                return getUsername() + " has the same song at least twice in this album.";
            }
        }
        System.out.println("nume " + newAlbum.getName());
        Admin.addSongs(getSongs(commandInput.getSongs()));
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

    public boolean checkValidDeletion(Album album) {
        List<Song> playingSongs = this.allPlayingSongs();
        return hasSongsFromAlbum(album, playingSongs);
    }

    public boolean hasSongsFromAlbum(Album album, List<Song> allPlayingSongs) {
        for (Song song : album.getSongs()) {
            if (allPlayingSongs.contains(song)) {
                return true;
            }
        }
        return false;
    }


}
