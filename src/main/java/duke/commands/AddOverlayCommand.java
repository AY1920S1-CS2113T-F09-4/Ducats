package duke.commands;

import java.util.ArrayList;
import java.util.Arrays;

import duke.DukeException;
import duke.Storage;
import duke.Ui;
import duke.components.Bar;
import duke.components.Note;
import duke.components.Song;
import duke.components.Chord;
import duke.components.SongList;

/**
 * A class representing the command to add a new bar of notes to the current song.
 */
public class AddOverlayCommand extends Command<SongList> {

    private int songIndex;

    /**
     * Constructor for the command to add a new bar to the current song.
     * @param message the input message that resulted in the creation of the duke.Commands.Command
     */
    public AddOverlayCommand(String message) {
        this.message = message;
    }

    /**
     * Modifies the song in the song list and returns the messages intended to be displayed.
     *
     * @param songList the duke.components.SongList object that contains the song list
     * @param ui the Ui object responsible for the reading of user input and the display of
     *           the responses
     * @param storage the Storage object used to read and manipulate the .txt file
     * @return the string to be displayed in duke.Duke
     * @throws DukeException if an exception occurs in the parsing of the message or in IO
     */
    public String execute(SongList songList, Ui ui, Storage storage) throws DukeException {
        Note note1;
        Note note2;
        Note note3;
        Note note4;
        int barNo;
        if (message.length() < 8 || !message.substring(0, 8).equals("overlay ")) { //exception if not fully spelt
            throw new DukeException(message);
        }
        try {
            //the command consists of overlay 10 repeat
            String[] sections = message.substring(8).split(" ");
            int barIndexToAdd = Integer.parseInt(sections[0]);
            System.out.println(barIndexToAdd);
            //System.out.println(Arrays.toString(sections));
            //System.out.println(songIndex);
            if (songList.getSize() > songIndex) {
                Song song = songList.getSongIndex(songIndex);
                ArrayList<Bar> barList = song.getBars();

                Bar overlayingBar = barList.get(barIndexToAdd);
                System.out.println("adjjdsa");
                ArrayList<Chord> chordsToAdd = overlayingBar.getChords();
                System.out.print("sections length ");
                System.out.println(sections.length);
                if (sections.length > 2 && sections[2].equals("repeat")) {
                    for (Bar temp : barList) {
                        ArrayList<Chord> tempChordList = temp.getChords();
                        for (int i = 0; i < tempChordList.size(); i++) {
                            Chord chordAdd = chordsToAdd.get(i);
                            for (Note noteTemp : chordAdd.getNotes()) {
                                tempChordList.get(i).addToChord(noteTemp);
                            }
                        }
                    }
                } else {
                    System.out.println("no repeat found");
                    Bar temp = barList.get(0);

                    ArrayList<Chord> tempChordList = temp.getChords();
                    for (int i = 0; i < tempChordList.size(); i++) {
                        Chord chordAdd = chordsToAdd.get(i);
                        for (Note noteTemp : chordAdd.getNotes()) {
                            tempChordList.get(i).addToChord(noteTemp);
                        }
                    }
                }
                return ui.formatAddOverlay(songList.getSongList(), barIndexToAdd,song);
            } else {
                System.out.println("no such index");
                //System.out.println(songList.getSize());
                throw new DukeException(message, "no_index");
            }
            //add the bar to the song in the songlist
            //storage.updateFile(taskList);

        } catch (Exception e) {
            throw new DukeException(message, "no_index");
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
}
