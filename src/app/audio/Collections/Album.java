package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Album.
 */
@Getter
@Setter
public final class Album extends AudioCollection {
    private int timestamp;
    private String description;
    private Integer releaseYear;
    private ArrayList<Song> songs;

    public Album(final String name, final String owner, final ArrayList<SongInput> songs,
                 final int timestamp, final String description, final Integer releaseYear) {
        super(name, owner);
        this.setSongs(songs);
        this.timestamp = timestamp;
        this.description = description;
        this.releaseYear = releaseYear;
    }

    /**
     * Gets total number of likes.
     * @return number of likes
     */
    public int getNumberOfLikes() {
        int likes = 0;
        for (Song song : songs) {
            likes += song.getLikes();
        }
        return likes;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    /**
     * Sets album's songs from input list.
     * @param songInputList the songs input list
     */
    public void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Matches album description boolean.
     * @param albumDescription the album description
     * @return if the user is the owner
     */
    @Override
    public boolean matchesDescription(final String albumDescription) {
        return getDescription().toLowerCase().startsWith(albumDescription.toLowerCase());
    }
}
