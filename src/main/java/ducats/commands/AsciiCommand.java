package ducats.commands;

import ducats.DucatsException;
import ducats.Storage;
import ducats.Ui;
import ducats.components.Bar;
import ducats.components.Chord;
import ducats.components.Group;
import ducats.components.Note;
import ducats.components.Pitch;
import ducats.components.Song;
import ducats.components.SongList;
import ducats.components.VerseList;

import java.util.ArrayList;

public class AsciiCommand extends Command<SongList> {

    //@@author Samuel787

    private String message;
    private Song song;

    /**
     * Constructor for the command to print out a bar, group or song in ASCII.
     * @param message the input message that resulted in the creation of the duke.Commands.Command
     */
    public AsciiCommand(String message) {
        this.message = message.trim();
    }

    /**
     * Prints out a bar, group or song in ASCII to represent the song sheet for that component.
     * @param songList the duke.TaskList or duke.components.SongList object that contains the task list in use
     * @param ui the Ui object responsible for the reading of user input and the display of
     *           the responses
     * @param storage the Storage object used to read and manipulate the .txt file
     * @return the string corresponding to the ASCII song sheet as requested by user
     * @throws DucatsException if an exception occurs in the parsing of the message or in IO
     */
    @Override
    public String execute(SongList songList, Ui ui, Storage storage) throws DucatsException {
        String result = "";
        if (message.length() < 6 || !message.substring(0, 6).equals("ascii ")) {
            throw new DucatsException(message);
        }
        try {
            message = message.substring(6).trim();
            String command = message.split(" ", 2)[0];
            if (command.equals("bar")) {
                int barNum = Integer.parseInt(message.split(" ", 2)[1].trim());
                //get the current song out
                Song song = new Song("Test song", "C-Major", 120);
                //bar index for user is assumed to start from 1
                if (barNum > song.getNumBars() || barNum < 1) {
                    throw new DucatsException(message, "AsciiCommand");
                }
                Bar bar = song.getBars().get(barNum - 1);
                result = printBarAscii(bar);
            } else if (command.equals("group")) {
                String groupName = message.split(" ", 2)[1].trim();
                //Get the verseList from storage
                VerseList verseList = new VerseList();
                Group group = verseList.find(groupName);
                if (group == null) {
                    throw new DucatsException(message, "AsciiCommand");
                } else {
                    result = printGroupAscii(group);
                }
            } else if (command.equals("song")) {
                String songName = message.split(" ", 2)[1].trim();

                //Get the song from the storage
                //Song song = new Song("Test song", "C-Major", 120);
                ArrayList<Song> songs = songList.findSong(songName);
                Song song;
                if (songs.size() == 1) {
                    song = songs.get(0);
                } else {
                    //song does not exist or query returned more than 1 result
                    throw new DucatsException(message, "AsciiCommand");
                }
                //if song exists
                result = printSongAscii(song);
            } else {
                //wrong command
                throw new DucatsException(message, "AsciiCommand");
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new DucatsException(message, "AsciiCommand");
        }
        return result;

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

    private static final String MUSIC_8 = "*";
    private static final String MUSIC_6 = "$.";
    private static final String MUSIC_4 = "$";
    private static final String MUSIC_3 = "@.";
    private static final String MUSIC_2 = "@";
    private static final String MUSIC_1 = "!";
    private static final String REST_8 = "#";
    private static final String REST_6 = "%.";
    private static final String REST_4 = "%";
    private static final String REST_3 = "^.";
    private static final String REST_2 = "^";
    private static final String REST_1 = "&";
    private static final String CONT = "-";

    /**
     * Prints out the selected bar in ASCII format to represnt the song sheet.
     * @param bar the bar that user wants to print in ASCII
     */
    public static String printBarAscii(Bar bar) {
        Song tempSong = new Song("Test Song", "C-Major", 120);
        tempSong.addBar(bar);
        return printSongAscii(tempSong);
    }

    /**
     * Prints out the selected verse in ASCII format to represent the song sheet.
     * @param group the verse that user wants to print in ASCII
     */
    public static String printGroupAscii(Group group) {
        Song tempSong = new Song("Test Song", "C-Major", 120);
        for (int i = 0; i < group.size(); i++) {
            tempSong.addBar(group.get(i));
        }
        return printSongAscii(tempSong);
    }

    /**
     * Prints out the selected song in ASCII format to represent the song sheet.
     * @param song the song that user wants to print in ASCII
     */
    public static String printSongAscii(Song song) {
        ArrayList<ArrayList<String>> songAscii = parseSongAscii(getSongAscii(song));
        StringBuilder stringResult = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < songAscii.get(i).size(); j++) {
                if (j > 8 && (j - 1) % 8 == 0) {
                    if ((i > 2 && i < 12)) {
                        //System.out.print("|");
                        stringResult.append("|");
                    } else {
                        //System.out.print(" ");
                        stringResult.append(" ");
                    }
                }
                //System.out.print(songAscii.get(i).get(j));
                stringResult.append(songAscii.get(i).get(j));
            }
            //System.out.println();
            stringResult.append("\n");
        }
        return stringResult.toString();
    }


    private static ArrayList<ArrayList<String>> getSongAscii(Song song) {
        ArrayList<ArrayList<String>> songAscii = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            songAscii.add(i, new ArrayList<>());
        }
        songAscii.get(0).add("UC: ");
        songAscii.get(1).add("UB: ");
        songAscii.get(2).add("UA: ");
        songAscii.get(3).add("UG: ");
        songAscii.get(4).add("UF: ");
        songAscii.get(5).add("UE: ");
        songAscii.get(6).add("UD: ");
        songAscii.get(7).add("MC: ");
        songAscii.get(8).add("LB: ");
        songAscii.get(9).add("LA: ");
        songAscii.get(10).add("LG: ");
        songAscii.get(11).add("LF: ");
        songAscii.get(12).add("LE: ");
        songAscii.get(13).add("LD: ");
        songAscii.get(14).add("LC: ");
        ArrayList<Bar> bars = song.getBars();
        for (Bar bar : bars) {
            ArrayList<ArrayList<String>> barAscii = getBarAscii(bar);
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < barAscii.get(i).size(); j++) {
                    songAscii.get(i).add(barAscii.get(i).get(j));
                }
            }
        }
        return songAscii;
    }

    private static ArrayList<ArrayList<String>> parseSongAscii(ArrayList<ArrayList<String>> rawSongAscii) {
        ArrayList<ArrayList<String>> resultAscii = new ArrayList<>();
        for (ArrayList<String> arr: rawSongAscii) {
            resultAscii.add(new ArrayList<>(arr));
        }
        for (int i = 0; i < 15; i++) {
            int length = rawSongAscii.get(i).size();
            if (i == 0 || i == 12) {
                for (int j = 1; j < length; j++) {
                    if (rawSongAscii.get(i).get(j).equals("@")) {
                        int startPos = j;
                        int counter = 1;
                        j++;
                        while (j < length && rawSongAscii.get(i).get(j).equals("p")) {
                            resultAscii.get(i).set(j, " ");
                            counter++;
                            j++;
                        }
                        String symbol = getSymbol(false, counter);
                        if (counter == 3 || counter == 6) {
                            String part1 = Character.toString(symbol.charAt(0));
                            String part2 = Character.toString(symbol.charAt(1));
                            resultAscii.get(i).set(startPos, part1);
                            resultAscii.get(i).set(startPos + 1, part2);
                        } else {
                            resultAscii.get(i).set(startPos, symbol);
                        }
                        if (startPos != 1 && (startPos - 1) % 8 != 0) {
                            resultAscii.get(i + 1).set(startPos - 1, "-");
                        }
                        resultAscii.get(i + 1).set(startPos, "-");
                        if (startPos != length - 1 && (startPos + 1) % 8 != 1) {
                            resultAscii.get(i + 1).set(startPos + 1, "-");
                        }
                        j--;
                    }
                }
            } else if (i == 1 || i == 13) {
                for (int j = 1; j < length; j++) {
                    if (rawSongAscii.get(i).get(j).equals("@")) {
                        int startPos = j;
                        int counter = 1;
                        j++;
                        while (j < length && rawSongAscii.get(i).get(j).equals("p")) {
                            if (resultAscii.get(i).get(j).equals("p")) {
                                resultAscii.get(i).set(j, " ");
                            }
                            counter++;
                            j++;
                        }
                        String symbol = getSymbol(false, counter);
                        if (counter == 3 || counter == 6) {
                            String part1 = Character.toString(symbol.charAt(0));
                            String part2 = Character.toString(symbol.charAt(1));
                            resultAscii.get(i).set(startPos, part1);
                            resultAscii.get(i).set(startPos + 1, part2);
                        } else {
                            resultAscii.get(i).set(startPos, symbol);
                        }
                        if (startPos % 8 != 1 && resultAscii.get(i).get(startPos - 1).equals(" ")) {
                            resultAscii.get(i).set(startPos - 1, "-");
                        }
                        if (startPos != length - 1 && resultAscii.get(i).get(startPos + 1).equals(" ")) {
                            resultAscii.get(i).set(startPos + 1, "-");
                        }
                        j--;
                    }
                }
            } else if (i == 7) {
                for (int j = 1; j < length; j++) {
                    if (rawSongAscii.get(i).get(j).equals("@")) {
                        int startPos = j;
                        int counter = 1;
                        j++;
                        while (j < length && rawSongAscii.get(i).get(j).equals("p")) {
                            resultAscii.get(i).set(j, "-");
                            counter++;
                            j++;
                        }
                        String symbol = getSymbol(false, counter);
                        if (counter == 3 || counter == 6) {
                            String part1 = Character.toString(symbol.charAt(0));
                            String part2 = Character.toString(symbol.charAt(1));
                            resultAscii.get(i).set(startPos, part1);
                            resultAscii.get(i).set(startPos + 1, part2);
                        } else {
                            resultAscii.get(i).set(startPos, symbol);
                        }
                        j--;
                    } else if (rawSongAscii.get(i).get(j).equals("R")) {
                        int startPos = j;
                        int counter = 1;
                        j++;
                        while (j < length && rawSongAscii.get(i).get(j).equals("X")) {
                            resultAscii.get(i).set(j, "-");
                            counter++;
                            j++;
                        }
                        String symbol = getSymbol(true, counter);
                        if (counter == 3 || counter == 6) {
                            String part1 = Character.toString(symbol.charAt(0));
                            String part2 = Character.toString(symbol.charAt(1));
                            resultAscii.get(i).set(startPos, part1);
                            resultAscii.get(i).set(startPos + 1, part2);
                        } else {
                            resultAscii.get(i).set(startPos, symbol);
                        }
                        j--;
                    }
                }
            } else if (i == 3 || i == 5 || i == 9 || i == 11) {
                for (int j = 1; j < length; j++) {
                    if (rawSongAscii.get(i).get(j).equals("@")) {
                        int startPos = j;
                        int counter = 1;
                        j++;
                        while (j < length && rawSongAscii.get(i).get(j).equals("p")) {
                            resultAscii.get(i).set(j, "-");
                            counter++;
                            j++;
                        }
                        String symbol = getSymbol(false, counter);
                        if (counter == 3 || counter == 6) {
                            String part1 = Character.toString(symbol.charAt(0));
                            String part2 = Character.toString(symbol.charAt(1));
                            resultAscii.get(i).set(startPos, part1);
                            resultAscii.get(i).set(startPos + 1, part2);
                        } else {
                            resultAscii.get(i).set(startPos, symbol);
                        }
                        j--;
                    }
                }
            } else { //i == 2, 4, 6, 8, 10, 14
                for (int j = 1; j < length; j++) {
                    if (rawSongAscii.get(i).get(j).equals("@")) {
                        int startPos = j;
                        int counter = 1;
                        j++;
                        while (j < length && rawSongAscii.get(i).get(j).equals("p")) {
                            resultAscii.get(i).set(j, " ");
                            counter++;
                            j++;
                        }
                        String symbol = getSymbol(false, counter);
                        if (counter == 3 || counter == 6) {
                            String part1 = Character.toString(symbol.charAt(0));
                            String part2 = Character.toString(symbol.charAt(1));
                            resultAscii.get(i).set(startPos, part1);
                            resultAscii.get(i).set(startPos + 1, part2);
                        } else {
                            resultAscii.get(i).set(startPos, symbol);
                        }
                        j--;
                    }
                }
            }
        }
        return resultAscii;
    }

    private static ArrayList<ArrayList<String>> getBarAscii(Bar bar) {
        ArrayList<Chord> chords = bar.getChords();
        //ArrayList<String> barAscii = new ArrayList<>(15);
        ArrayList<ArrayList<String>> barAscii = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            barAscii.add(new ArrayList<>());
            if (i == 3 || i == 5 || i == 7 || i == 9 || i == 11) {
                for (int j = 0; j < 8; j++) {
                    barAscii.get(i).add(j, CONT);
                }
            } else {
                for (int j = 0; j < 8; j++) {
                    barAscii.get(i).add(j, " ");
                }
            }
        }


        for (int i = 0; i < chords.size(); i++) {
            ArrayList<Note> notes = chords.get(i).getNotes();
            for (Note note : notes) {
                Pitch pitch = note.getPitch();
                if (note.isStart()) {
                    switch (pitch) {
                    case UPPER_C:
                        barAscii.get(0).set(i, "@");
                        break;
                    case UPPER_B:
                        barAscii.get(1).set(i, "@");
                        break;
                    case UPPER_A:
                        barAscii.get(2).set(i, "@");
                        break;
                    case UPPER_G:
                        barAscii.get(3).set(i, "@");
                        break;
                    case UPPER_F:
                        barAscii.get(4).set(i, "@");
                        break;
                    case UPPER_E:
                        barAscii.get(5).set(i, "@");
                        break;
                    case UPPER_D:
                        barAscii.get(6).set(i, "@");
                        break;
                    case MIDDLE_C:
                        barAscii.get(7).set(i, "@");
                        break;
                    case LOWER_B:
                        barAscii.get(8).set(i, "@");
                        break;
                    case LOWER_A:
                        barAscii.get(9).set(i, "@");
                        break;
                    case LOWER_G:
                        barAscii.get(10).set(i, "@");
                        break;
                    case LOWER_F:
                        barAscii.get(11).set(i, "@");
                        break;
                    case LOWER_E:
                        barAscii.get(12).set(i, "@");
                        break;
                    case LOWER_D:
                        barAscii.get(13).set(i, "@");
                        break;
                    case LOWER_C:
                        barAscii.get(14).set(i, "@");
                        break;
                    case REST:
                        for (int k = 0; k < 15; k++) {
                            if (k == 7) {
                                barAscii.get(7).set(i, "R");
                            } else if (k == 3 || k == 5 || k == 9 || k == 11) {
                                barAscii.get(k).set(i, "-");
                            } else {
                                barAscii.get(k).set(i, " ");
                            }
                        }
                        break;
                    default:
                        break;
                    }
                } else {
                    switch (pitch) {
                    case UPPER_C:
                        barAscii.get(0).set(i, "p");
                        break;
                    case UPPER_B:
                        barAscii.get(1).set(i, "p");
                        break;
                    case UPPER_A:
                        barAscii.get(2).set(i, "p");
                        break;
                    case UPPER_G:
                        barAscii.get(3).set(i, "p");
                        break;
                    case UPPER_F:
                        barAscii.get(4).set(i, "p");
                        break;
                    case UPPER_E:
                        barAscii.get(5).set(i, "p");
                        break;
                    case UPPER_D:
                        barAscii.get(6).set(i, "p");
                        break;
                    case MIDDLE_C:
                        barAscii.get(7).set(i, "p");
                        break;
                    case LOWER_B:
                        barAscii.get(8).set(i, "p");
                        break;
                    case LOWER_A:
                        barAscii.get(9).set(i, "p");
                        break;
                    case LOWER_G:
                        barAscii.get(10).set(i, "p");
                        break;
                    case LOWER_F:
                        barAscii.get(11).set(i, "p");
                        break;
                    case LOWER_E:
                        barAscii.get(12).set(i, "p");
                        break;
                    case LOWER_D:
                        barAscii.get(13).set(i, "p");
                        break;
                    case LOWER_C:
                        barAscii.get(14).set(i, "p");
                        break;
                    case REST:
                        for (int k = 0; k < 15; k++) {
                            if (k == 7) {
                                barAscii.get(7).set(i, "X");
                            } else if (k == 3 || k == 5 || k == 9 || k == 11) {
                                barAscii.get(k).set(i, "-");
                            } else {
                                barAscii.get(k).set(i, " ");
                            }
                        }
                        break;
                    default:
                        break;
                    }

                }
            }
        }
        return barAscii;
    }

    private static String getSymbol(boolean isRest, int duration) {
        if (isRest) {
            switch (duration) {
            case 1:
                return REST_1;
            case 2:
                return REST_2;
            case 3:
                return REST_3;
            case 4:
                return REST_4;
            case 6:
                return REST_6;
            case 8:
                return REST_8;
            default:
                return "";
            }
        } else {
            switch (duration) {
            case 1:
                return MUSIC_1;
            case 2:
                return MUSIC_2;
            case 3:
                return MUSIC_3;
            case 4:
                return MUSIC_4;
            case 6:
                return MUSIC_6;
            case 8:
                return MUSIC_8;
            default:
                return "";
            }
        }
    }
}
