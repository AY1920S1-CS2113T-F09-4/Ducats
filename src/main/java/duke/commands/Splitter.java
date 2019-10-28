package duke.commands;

import java.util.ArrayList;
import java.util.Arrays;

import duke.DukeException;
import duke.Storage;
import duke.Ui;
import duke.components.Bar;
import duke.components.Group;
import duke.components.Note;
import duke.components.Song;
import duke.components.Chord;
import duke.components.SongList;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

/**
 * A class that splits an object to the bars and then returns an arraylist of the bars to the function.
 */
public class Splitter  {
    public String message;
    /**
     * Constructor for the command to add a new bar to the current song.
     * @param message the input message that resulted in the creation of the duke.Commands.Command
     */

    public Splitter(String message) {
        this.message = message;
    }

    /**
     * Combines two chords.
     *
     * @param chordBeCopiedFrom the chord that is being copied from
     * @param chordCopiedTo the chord that is being copied to
     */

    public void combineChord(Chord chordBeCopiedFrom, Chord chordCopiedTo) {

        //ArrayList<Note>noteArrayCopyFrom  = chordBeCopiedFrom.getNotes();
        //Iterator<Note> iterator1 = noteArrayCopyFrom.iterator();
        //while()
        chordCopiedTo.getNotes().addAll(chordBeCopiedFrom.getNotes());
    }
    /**
     * Splits a song object into an array list of bars.
     *
     * @param object the duke.components.Song is a Song object that needs to be split.
     * @return the the arraylist of bars
     */

    public ArrayList<Bar> splitObject(Song object) {
        return object.getBars();
    }
    /**
     * Splits a Group object into an array list of bars.
     *
     * @param object the duke.components.Grouo is a Group object that needs to be split.
     * @return the the arraylist of bars
     */

    public ArrayList<Bar> splitObject(Group object) {
        return object.getBars();
    }

    /**
     * Returns a boolean value representing whether the program will terminate or not, used in
     * duke.Duke to reassign a boolean variable checked at each iteration of a while loop.
     *
     * @return a boolean value that represents whether the program will terminate after the command
     */
    
    public boolean isExit() {
        return false;
    }
}
