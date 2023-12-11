package app.user;

import app.audio.Collections.Album;
import app.audio.Collections.AlbumOutput;
import app.audio.Collections.Playlist;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.Song;
import app.user.factory.ArtistFactory;
import app.user.factory.HostFactory;
import app.user.factory.SimpleUserFactory;
import app.utils.Enums;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Artist extends User {
    private ArrayList<Album> albums;
    private ArrayList<Song> songs;
    public Artist(String username, int age, String city) {
        super(username, age, city);
        albums = new ArrayList<>();
        setUserType(Enums.UserType.ARTIST);
    }

    public void setSongs(List<SongInput> songInputList) {
        this.songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public String addAlbum(CommandInput commandInput) {

        if (albums.stream().anyMatch(album -> album.getName().equals(commandInput.getName())))
            return this.getUsername() + " has another album with the same name.";

        // create a new album
        Album newAlbum = new Album(commandInput.getName(), commandInput.getUsername(),
                                   songs, commandInput.getTimestamp(),
                                   commandInput.getDescription(), commandInput.getReleaseYear());
        albums.add(newAlbum);


        for (Song song : songs) {
            boolean contains = newAlbum.containsSong(song);
            if (contains) {
                return getUsername() + " has the same song at least twice in this album.";
            }
        }
        return commandInput.getUsername() + " has added new album successfully.";
    }

    public ArrayList<AlbumOutput> showAlbums() {
        ArrayList<AlbumOutput> albumOutputs = new ArrayList<>();
        for (Album album : albums) {
            albumOutputs.add(new AlbumOutput(album));
        }

        return albumOutputs;
    }
}
