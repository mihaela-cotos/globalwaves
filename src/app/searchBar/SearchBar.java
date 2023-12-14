package app.searchBar;

import app.Admin;
import app.audio.LibraryEntry;
import app.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.*;

@Getter
@Setter
public class SearchBar {
    private List<LibraryEntry> results;
    private List<User> userResults;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    private String lastSearchType;
    private LibraryEntry lastSelected;
    private User lastSelectedUser;


    public SearchBar(String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
        lastSelectedUser = null;
    }
    public List<LibraryEntry> search(Filters filters, String type) {
        List<LibraryEntry> entries = new ArrayList<>();

        switch (type) {
            case "song":
                entries = new ArrayList<>(Admin.getSongs());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getAlbum() != null) {
                    entries = filterByAlbum(entries, filters.getAlbum());
                }

                if (filters.getTags() != null) {
                    entries = filterByTags(entries, filters.getTags());
                }

                if (filters.getLyrics() != null) {
                    entries = filterByLyrics(entries, filters.getLyrics());
                }

                if (filters.getGenre() != null) {
                    entries = filterByGenre(entries, filters.getGenre());
                }

                if (filters.getReleaseYear() != null) {
                    entries = filterByReleaseYear(entries, filters.getReleaseYear());
                }

                if (filters.getArtist() != null) {
                    entries = filterByArtist(entries, filters.getArtist());
                }

                break;
            case "playlist":
                entries = new ArrayList<>(Admin.getPlaylists());

                entries = filterByPlaylistVisibility(entries, user);

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getFollowers() != null) {
                    entries = filterByFollowers(entries, filters.getFollowers());
                }

                break;
            case "podcast":
                entries = new ArrayList<>(Admin.getPodcasts());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                break;
            case "album":
                entries = new ArrayList<>(Admin.getAlbums());

                if (filters.getName() != null) {
                    entries = filterByName(entries, filters.getName());
                }

                if (filters.getOwner() != null) {
                    entries = filterByOwner(entries, filters.getOwner());
                }

                if (filters.getDescription() != null) {
                    entries = filterByDescription(entries, filters.getDescription());
                }

                break;
            default:
                entries = new ArrayList<>();
        }


        while (entries.size() > MAX_RESULTS) {
            entries.remove(entries.size() - 1);
        }

        this.results = entries;
        this.lastSearchType = type;
        return this.results;
    }

    public List<User> searchUser(Filters filters, String type) {
        List<User> hosts = new ArrayList<>(Admin.getHosts());
        List<User> artists = new ArrayList<>(Admin.getArtists());

        switch (type) {
            case "artist" :

                if (filters.getName() != null) {
                    artists = filterUsersByName(artists, filters.getName());
                }

                break;
            case "host" :

                if (filters.getName() != null) {
                    hosts = filterUsersByName(hosts, filters.getName());
                }

                break;
            default:
                hosts = new ArrayList<>();
                artists = new ArrayList<>();
        }


        if (type.equals("artist")) {
            while (artists.size() > MAX_RESULTS) {
                artists.remove(artists.size() - 1);
            }
            this.userResults = artists;
        } else {
            while (hosts.size() > MAX_RESULTS) {
                hosts.remove(hosts.size() - 1);
            }
            this.userResults = hosts;
        }

        this.lastSearchType = type;
        return this.userResults;
    }

    public LibraryEntry select(Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected = this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

    public User selectUser(Integer itemNumber) {
        if (this.userResults.size() < itemNumber) {
            userResults.clear();

            return null;
        } else {
            lastSelectedUser =  this.userResults.get(itemNumber - 1);
            results.clear();

            return lastSelectedUser;
        }
    }
}
