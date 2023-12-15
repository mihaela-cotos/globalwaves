package main;

import app.CommandRunner;
import com.fasterxml.jackson.databind.node.ArrayNode;
import fileio.input.CommandInput;

public final class Action {

    private Action() {

    }

    /**
     * Executes commands.
     * @param command current command
     * @param outputs array node add the output
     */
    public static void run(final CommandInput command, final ArrayNode outputs) {

        String commandName = command.getCommand();

        switch (commandName) {
            case "search" -> outputs.add(CommandRunner.search(command));
            case "select" -> outputs.add(CommandRunner.select(command));
            case "load" -> outputs.add(CommandRunner.load(command));
            case "playPause" -> outputs.add(CommandRunner.playPause(command));
            case "repeat" -> outputs.add(CommandRunner.repeat(command));
            case "shuffle" -> outputs.add(CommandRunner.shuffle(command));
            case "forward" -> outputs.add(CommandRunner.forward(command));
            case "backward" -> outputs.add(CommandRunner.backward(command));
            case "like" -> outputs.add(CommandRunner.like(command));
            case "next" -> outputs.add(CommandRunner.next(command));
            case "prev" -> outputs.add(CommandRunner.prev(command));
            case "createPlaylist" -> outputs.add(CommandRunner.createPlaylist(command));
            case "addRemoveInPlaylist" -> outputs.add(CommandRunner
                                                             .addRemoveInPlaylist(command));
            case "switchVisibility" -> outputs.add(CommandRunner.switchVisibility(command));
            case "showPlaylists" -> outputs.add(CommandRunner.showPlaylists(command));
            case "follow" -> outputs.add(CommandRunner.follow(command));
            case "status" -> outputs.add(CommandRunner.status(command));
            case "showPreferredSongs" -> outputs.add(CommandRunner.showLikedSongs(command));
            case "getPreferredGenre" -> outputs.add(CommandRunner.getPreferredGenre(command));
            case "getTop5Songs" -> outputs.add(CommandRunner.getTop5Songs(command));
            case "getTop5Playlists" -> outputs.add(CommandRunner.getTop5Playlists(command));
            case "getTop5Albums" -> outputs.add(CommandRunner.getTop5Albums(command));
            case "getTop5Artists" -> outputs.add(CommandRunner.getTop5Artists(command));
            case "switchConnectionStatus" -> outputs.add(CommandRunner
                                                      .switchConnectionStatus(command));
            case "getOnlineUsers" -> outputs.add(CommandRunner.getOnlineUsers(command));
            case "addUser" -> outputs.add(CommandRunner.addUser(command));
            case "addAlbum" -> outputs.add(CommandRunner.addAlbum(command));
            case "addPodcast" -> outputs.add(CommandRunner.addPodcast(command));
            case "showAlbums" -> outputs.add(CommandRunner.showAlbums(command));
            case "showPodcasts" -> outputs.add(CommandRunner.showPodcasts(command));
            case "printCurrentPage" -> outputs.add(CommandRunner.printCurrentPage(command));
            case "addEvent" -> outputs.add(CommandRunner.addEvent(command));
            case "addMerch" -> outputs.add(CommandRunner.addMerch(command));
            case "getAllUsers" -> outputs.add(CommandRunner.getAllUsers(command));
            case "deleteUser" -> outputs.add(CommandRunner.deleteUser(command));
            case "addAnnouncement" -> outputs.add(CommandRunner.addAnnouncement(command));
            case "removeAnnouncement" -> outputs.add(CommandRunner.removeAnnouncement(command));
            case "removeAlbum" -> outputs.add(CommandRunner.removeAlbum(command));
            case "changePage" -> outputs.add(CommandRunner.changePage(command));
            case "removePodcast" -> outputs.add(CommandRunner.removePodcast(command));
            case "removeEvent" -> outputs.add(CommandRunner.removeEvent(command));
            default -> System.out.println("Invalid command " + commandName);
        }
    }
}
