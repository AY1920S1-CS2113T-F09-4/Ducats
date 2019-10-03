package duke.components;

import java.util.ArrayList;

public class Bar {

    private ArrayList<Chord> chords;
    private int id;

    /**
     * Constructor takes in a String representing a list of notes.
     *
     * @param id the ID of the Bar in the Song
     * @param notes the String representing the list of notes that compose a bar
     */
    public Bar(int id, String notes) {
        this.id = id;
        this.chords = compileNotesToChords(convertStringToNotes(notes));
    }

    /**
     * Returns an ArrayList of Note objects from the input String of the constructor.
     *
     * @param notes the input String representing the list of notes that compose a bar
     * @return an ArrayList of Note objects corresponding to the above notes
     */
    public ArrayList<Note> convertStringToNotes(String notes) {
        ArrayList<Note> result = new ArrayList<>();
        String[] notesArray = notes.split(" ");
        for (String note: notesArray) {
            result.add(new Note(note));
        }
        return result;
    }

    /**
     * Compiles an ArrayList of Note objects together to create an ArrayList of Chord
     * objects that compose the Bar.
     *
     * @param noteList an ArrayList of Note objects, which can be of different durations
     * @return an ArrayList of Chord objects with the specified duration of an 1/8th note
     */
    public ArrayList<Chord> compileNotesToChords(ArrayList<Note> noteList) {
        ArrayList<Chord> result = new ArrayList<>();
        for (Note note: noteList) {
            for (int i = 0; i < note.getRelativeUnitDuration(); i++) {
                Chord chord = new Chord();
                Note note1 = note.getUnitNote();
                if (i != 0) {
                    note1.setStart(false);
                }
                chord.addToChord(note1);
            }
        }
        return result;
    }

    public ArrayList<Chord> getChords() {
        return chords;
    }

    public void setId(int id) {
        this.id = id;
    }
}
