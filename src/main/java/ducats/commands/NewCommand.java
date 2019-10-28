package ducats.commands;

import ducats.DucatsException;
import ducats.Storage;
import ducats.Ui;
import ducats.components.Song;
import ducats.components.SongList;
import java.util.Arrays;

//@@author jwyf
/**
 * A class representing the command to add a new song to the song list.
 */
public class NewCommand extends Command<SongList> {

    /**
     * Constructor for the command to add a task to the task list.
     *
     * @param message the input message that resulted in the creation of the ducats.Commands.Command
     */
    public NewCommand(String message) {
        this.message = message;
    }

    /**
     * Modifies the song list and returns the messages intended to be displayed.
     *
     * @param songList the ducats.components.SongList object that contains the song list
     * @param ui the Ui object responsible for the reading of user input and the display of
     *           the responses
     * @param storage the Storage object used to read and manipulate the .txt file
     * @return the string to be displayed in ducats.Duke
     * @throws DucatsException if an exception occurs in the parsing of the message or in IO
     */
    public String execute(SongList songList, Ui ui, Storage storage) throws DucatsException {
        String songName;
        String key;
        String timeSignature;
        int tempo;
        if (message.length() < 4 || !message.substring(0, 4).equals("new ")) { //exception if not fully spelt
            throw new DucatsException(message);
        }
        Song song;
        try {
            String[] sections = message.substring(4).split(" ");

            songName = sections[0];
            key = sections[1];
            System.out.println(Arrays.toString(sections));
            timeSignature = sections[2];
            tempo = Integer.parseInt(sections[3]);
            song = new Song(songName, key, tempo);

            songList.add(song);
            storage.updateFile(songList);
            System.out.println("after creating song");
            return ui.formatNewSong(songList.getSongList(), song);
        } catch (Exception e) {
            throw new DucatsException(message, "new");
        }
    }

    /**
     * Returns a boolean value representing whether the program will terminate or not, used in
     * ducats.Duke to reassign a boolean variable checked at each iteration of a while loop.
     *
     * @return a boolean value that represents whether the program will terminate after the command
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
