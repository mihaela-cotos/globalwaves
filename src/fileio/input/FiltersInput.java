package fileio.input;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class FiltersInput {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear; // pentru search song/episode -> releaseYear
    private String artist;
    private String owner; // pentru search playlist si podcast
    private String followers; // pentru search playlist -> followers
    private String description; // pentru search album -> description

    public FiltersInput() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    @Override
    public String toString() {
        return "FilterInput{" +
                ", name='" + name + '\'' +
                ", album='" + album + '\'' +
                ", tags=" + tags +
                ", lyrics='" + lyrics + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseYear='" + releaseYear + '\'' +
                ", artist='" + artist + '\'' +
                ", owner='" + owner + '\'' +
                ", followers='" + followers + '\'' +
                '}';
    }
}
