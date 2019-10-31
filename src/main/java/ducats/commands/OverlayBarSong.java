package ducats.commands;

import java.util.ArrayList;

import ducats.DucatsException;
import ducats.Storage;
import ducats.Ui;
import ducats.components.Bar;
import ducats.components.Note;
import ducats.components.Song;
import ducats.components.Chord;
import ducats.components.SongList;
import java.util.Iterator;

/**
 * A class that splits an object to the bars and then returns an arraylist of the bars to the function.
 */
public class OverlayBarSong  extends Command<SongList>  {
    public String message;
    private int songIndex;


    /**
     * Constructor for the command to add a new bar to the current song.
     * @param message the input message that resulted in the creation of the ducats.Commands.Command
     */
    public OverlayBarSong(String message) {
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
     * Combines two bars.
     *
     * @param barToBeCopied the bar that is being copied from
     * @param barToCopyTo the bar that is being copied to
     */

    public void combineBar(Bar barToBeCopied, Bar barToCopyTo) {
        //we need copy the chords from bar1 into bar 2
        ArrayList<Chord> chordBeCopiedFrom = barToBeCopied.getChords();
        ArrayList<Chord> chordCopiedTo = barToCopyTo.getChords();
        //System.out.println("here i after the chord from bar");
        Iterator<Chord> iterator1 = chordBeCopiedFrom.iterator();
        int i = 0;
        while (iterator1.hasNext()) {
            Chord chordAdd = iterator1.next();
            combineChord(chordAdd,chordCopiedTo.get(i));
            i += 1;
        }
    }

    /**
     * Modifies the song in the song list and returns the messages intended to be displayed.
     *
     * @param songList the ducats.components.SongList object that contains the song list
     * @param ui the Ui object responsible for the reading of user input and the display of
     *           the responses
     * @param storage the Storage object used to read and manipulate the .txt file
     * @return the string to be displayed in ducats.Duke
     * @throws DucatsException if an exception occurs in the parsing of the message or in IO
     */

    public String execute(SongList songList, Ui ui, Storage storage) throws DucatsException {
        Note note1;
        Note note2;
        Note note3;
        Note note4;
        int barNo;

        if (message.length() < 17 || !message.substring(0, 16).equals("overlay_bar_song")) {
            //exception if not fully spelt
            throw new DucatsException(message);
        }

        try {
            //the command consists of overlay_bar_song 1 2 1 1 refers to overlay song 1 bar 2 onto song 1 bar 1
            //the command consists of overlay_bar_song 1 2 1 1 refers to overlay song 1 bar 2 onto song 1 bar 1
            String[] sections = message.substring(17).split(" ");
            int songIndexToAddFrom = Integer.parseInt(sections[0]) - 1;
            int barIndexToAddFrom = Integer.parseInt(sections[1]) - 1;
            int songIndexToAddTo = Integer.parseInt(sections[2]) - 1;
            int barIndexToAddTo = Integer.parseInt(sections[3]) - 1;
            //System.out.println(barIndexToAdd);
            if (songList.getSize() > songIndexToAddFrom && songList.getSize() > songIndexToAddTo) {
                Song songAddFrom = songList.getSongIndex(songIndexToAddFrom);
                //System.out.println("adjjdsa1213");
                Song songAddTo =  songList.getSongIndex(songIndexToAddTo);
                ArrayList<Bar> barListAddFrom = songAddFrom.getBars();
                ArrayList<Bar> barListAddTo = songAddTo.getBars();
                Bar overlayingBarToBeCopied = barListAddFrom.get(barIndexToAddFrom);
                Bar overlayingBar = overlayingBarToBeCopied.copy(overlayingBarToBeCopied);

                if (sections.length > 5 && sections[4].equals("repeat")) {
                    Iterator<Bar> iterator1 = barListAddTo.iterator();
                    int i = 0;
                    while (iterator1.hasNext()) {
                        Bar temp = iterator1.next();
                        if (i >= barIndexToAddTo) {
                            combineBar(overlayingBar, temp);
                        }
                        i += 1;
                    }
                } else {
                    //System.out.println("no repeat found");
                    Bar barToBeCopiedTo = barListAddTo.get(barIndexToAddTo);
                    combineBar(overlayingBar, barToBeCopiedTo);
                    //System.out.println("bar temp gotten");
                }
                //add the bar to the song in the songlist
                storage.updateFile(songList);
                return ui.formatAddOverlay(songList.getSongList(), barIndexToAddFrom,songAddTo);
            } else {
                System.out.println("no such index");
                //System.out.println(songList.getSize());
                throw new DucatsException(message, "no_index");
            }

        } catch (Exception e) {
            System.out.println(e);
            throw new DucatsException(message, "no_index");
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
