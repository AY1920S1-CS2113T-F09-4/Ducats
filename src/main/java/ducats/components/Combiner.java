package ducats.components;

import java.util.ArrayList;
import java.io.Serializable;
import ducats.components.Bar;
import ducats.components.Group;
import ducats.components.Note;
import ducats.components.Song;
import ducats.components.Chord;
import ducats.components.SongList;
import java.util.Iterator;
import ducats.DucatsLogger;

/*
 *This function combines 2 components of songs such as group-group, chord-chord,bar-bar
 */
public class Combiner implements Serializable {

    /**
     * Combines two chords.
     *
     * @param chordBeCopiedFrom the chord that is being copied from
     * @param chordCopiedTo the chord that is being copied to
     */

    public void combineChord(Chord chordBeCopiedFrom, Chord chordCopiedTo) {
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
        while (iterator1.hasNext() && i < chordCopiedTo.size()) {
            Chord chordAdd = iterator1.next();
            combineChord(chordAdd,chordCopiedTo.get(i));
            i += 1;
        }
        DucatsLogger.fine("combiner combined 2 bars together");
    }
    /**
     * Combines two Groups. When they are of unequal length the smaller one repeats
     * over the larger one or the larger one overlay n number of bars where n
     * is the size of the smaller group.
     *
     * @param groupToBeCopied the group that is being copied from
     * @param groupToCopyTo the group that is being copied to
     */

    public void combineGroup(Group groupToBeCopied, Group groupToCopyTo) {
        ArrayList<Bar> barBeCopiedFrom = groupToBeCopied.getBars();
        ArrayList<Bar> barCopiedTo = groupToCopyTo.getBars();

        int k = 0;
        int numberOfTimes = (int) Math.ceil(barCopiedTo.size() / (barBeCopiedFrom.size() * 1.0));

        int i = 0;
        while (k < numberOfTimes) {
            Iterator<Bar> iterator1 = barBeCopiedFrom.iterator();
            while (iterator1.hasNext() && i < barCopiedTo.size()) {
                Bar barAdd = iterator1.next();
                combineBar(barAdd, barCopiedTo.get(i));
                i += 1;
            }
            k += 1;
        }
        DucatsLogger.fine("combiner combined 2 groups together");
    }
}
