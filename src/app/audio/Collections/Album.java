package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Album extends AudioCollection {
    private int timestamp;
    private String description;
    private Integer releaseYear;
    private ArrayList<Song> songs;

    public Album(String name, String owner, ArrayList<SongInput> songs, int timestamp,
                                            String description, Integer releaseYear) {
        super(name, owner);
        this.setSongs(songs);
        this.timestamp = timestamp;
        this.description = description;
        this.releaseYear = releaseYear;
    }


    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }

    public boolean containsSong(Song song) {
        int count = 0;
        for (Song iterSong : songs) {
            if (iterSong.getName().equals(song.getName())) {
                count++;
            }
        }
        return count > 1;
    }

    public void setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }
}
