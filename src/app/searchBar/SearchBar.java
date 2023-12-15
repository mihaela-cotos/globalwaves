package app.searchBar;

import app.Admin;
import app.audio.LibraryEntry;
import app.user.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static app.searchBar.FilterUtils.filterByAlbum;
import static app.searchBar.FilterUtils.filterByArtist;
import static app.searchBar.FilterUtils.filterByFollowers;
import static app.searchBar.FilterUtils.filterByGenre;
import static app.searchBar.FilterUtils.filterByLyrics;
import static app.searchBar.FilterUtils.filterByName;
import static app.searchBar.FilterUtils.filterByOwner;
import static app.searchBar.FilterUtils.filterByPlaylistVisibility;
import static app.searchBar.FilterUtils.filterByReleaseYear;
import static app.searchBar.FilterUtils.filterByTags;
import static app.searchBar.FilterUtils.filterByDescription;
import static app.searchBar.FilterUtils.filterUsersByName;

/**
 * The type Search bar.
 */
@Getter
public final class SearchBar {
    private List<LibraryEntry> results;
    private final String user;
    private static final Integer MAX_RESULTS = 5;
    private String lastSearchType;
    private LibraryEntry lastSelected;
    private User lastSelectedUser;
    private List<User> userResults;

    /**
     * Instantiates a new Search bar.
     *
     * @param user the user
     */
    public SearchBar(final String user) {
        this.results = new ArrayList<>();
        this.user = user;
    }

    /**
     * Clear selection.
     */
    public void clearSelection() {
        lastSelected = null;
        lastSearchType = null;
        lastSelectedUser = null;
    }

    /**
     * Search a library entry.
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<LibraryEntry> search(final Filters filters, final String type) {
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

    /**
     * Search a user (an artist or a host).
     *
     * @param filters the filters
     * @param type    the type
     * @return the list
     */
    public List<User> searchUser(final Filters filters, final String type) {
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

    /**
     * Select library entry.
     *
     * @param itemNumber the item number
     * @return the library entry
     */
    public LibraryEntry select(final Integer itemNumber) {
        if (this.results.size() < itemNumber) {
            results.clear();

            return null;
        } else {
            lastSelected = this.results.get(itemNumber - 1);
            results.clear();

            return lastSelected;
        }
    }

    /**
     * Select a user from search result.
     *
     * @param itemNumber the item number
     * @return the selected user
     */
    public User selectUser(final Integer itemNumber) {
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
