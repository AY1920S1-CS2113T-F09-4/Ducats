package ducats.commands;

import ducats.DucatsException;
import ducats.Storage;
import ducats.Ui;
import ducats.components.Bar;
import ducats.components.Song;
import ducats.components.SongList;

import java.util.ArrayList;

//@@author jwyf
/**
 * A class representing the command to edit a bar of notes in the current song.
 */
public class EditBarCommand extends Command<SongList> {

    private int songIndex;

    /**
     * Constructor for the command to edit a bar in the current song.
     * @param message the input message that resulted in the creation of the duke.Commands.Command
     */
    public EditBarCommand(String message) {
        this.message = message;
        this.songIndex = 0;
    }

    /**
     * Modifies a song in the song list by editing an existing bar and
     * returns the messages intended to be displayed.
     *
     * @param songList the duke.components.SongList object that contains the song list
     * @param ui the Ui object responsible for the reading of user input and the display of
     *           the responses
     * @param storage the Storage object used to read and manipulate the .txt file
     * @return the string to be displayed in duke.Duke
     * @throws DucatsException if an exception occurs in the parsing of the message or in IO
     */
    public String execute(SongList songList, Ui ui, Storage storage) throws DucatsException {
        int barNo;
        try {
            songIndex = songList.getActiveIndex();
            Song song = songList.getSongIndex(songIndex);

            String[] sections = message.substring(8).split(" ");
            barNo = Integer.parseInt(sections[0].substring(4));
            int notesIndex = message.indexOf(sections[1]);
            Bar newBar = new Bar(barNo, message.substring(notesIndex));

            song.getBars().add(barNo - 1, newBar);
            Bar oldBar = song.getBars().get(barNo);
            song.getBars().remove(barNo);

            storage.updateFile(songList);
            ArrayList<Song> temp = songList.getSongList();
            return ui.formatEdit(oldBar, newBar, song);
        } catch (Exception e) {
            throw new DucatsException(message, "edit");
        }
    }

    /**
     * Returns a boolean value representing whether the program will terminate or not, used in
     * duke.Duke to reassign a boolean variable checked at each iteration of a while loop.
     *
     * @return a boolean value that represents whether the program will terminate after the command
     */
    @Override
    public boolean isExit() {
        return false;
    }

    //@@author rohan-av
    /**
     * Returns an integer corresponding to the duration, tempo and time signature if the command starts a metronome.
     * Else, returns an array containing -1.
     *
     * @return the integer array corresponding to the parameters of the Metronome class
     */
    @Override
    public int[] startMetronome() {
        return new int[]{-1, -1, -1, -1};
    }
}
