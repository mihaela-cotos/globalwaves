package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import lombok.Getter;

import java.util.List;

/**
 * The type Podcast.
 */
@Getter
public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;

    /**
     * Instantiates a new Podcast.
     *
     * @param name      the name
     * @param owner     the owner
     * @param episodes  the list of episodes
     */
    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }

    /**
     * Contains episode boolean.
     *
     * @param episode the episode
     * @return the boolean
     */
    public boolean containsEpisode(final Episode episode) {
        int count = 0;
        for (Episode iterEpisode : episodes) {
            if (iterEpisode.getName().equals(episode.getName())) {
                count++;
            }
        }
        return count > 1;
    }
}
