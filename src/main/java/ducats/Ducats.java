package ducats;


import ducats.commands.AddBarCommand;
import ducats.commands.AddOverlayCommand;
import ducats.commands.AsciiCommand;
import ducats.commands.Command;
import ducats.commands.CopyCommand;
import ducats.commands.DeleteBarCommand;
import ducats.commands.DeleteCommand;
import ducats.commands.EditCommand;
import ducats.commands.GroupCommand;
import ducats.commands.HelpCommand;
import ducats.commands.ListCommand;
import ducats.commands.NewCommand;
import ducats.commands.OpenCommand;
import ducats.commands.OverlayBarGroup;
import ducats.commands.OverlayBarSong;
import ducats.commands.OverlayGroupGroup;
import ducats.commands.RedoCommand;
import ducats.commands.UndoCommand;
import ducats.commands.ViewCommand;
import ducats.components.SongList;
import ducats.components.UndoRedoStack;

import java.nio.file.Paths;

public class Ducats {
    /**
     * Chat bot cum task management application that can handle events, deadlines and normal to-do tasks,
     * as well as basic exception handling.
     */
    private Storage storage;
    private SongList songs;
    private Ui ui;
    private UndoRedoStack undoRedoStack;
    private Metronome metronome;

    /**
     * Constructor for the duke.Duke object, which initializes the UI, duke.TaskList and duke.Storage in
     * order to carry out its functions.
     */
    public Ducats() {
        ui = new Ui();
        songs = new SongList();

        //storage = new Storage(Paths.get("/home/rishi/Desktop/cs2113t/team/main/data/todo_list.txt"));
        storage = new Storage(Paths.get("data", "songlist.txt"));
        metronome = new Metronome();
        try {
            storage.loadToList(songs);
        } catch (DucatsException e) {
            System.out.println(ui.showError(e));
            songs = new SongList();
        }
        undoRedoStack = new UndoRedoStack(songs);
    }

    /**
     * Runs the program, constantly asking for and responding to user input, finally terminating.
     * upon the word "Bye".
     */

    private void run() {
        System.out.println(ui.showWelcomeMessage());
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                //if the command uses the SongList
                String output;
                if (c instanceof AddBarCommand
                        || c instanceof ViewCommand
                        || c instanceof NewCommand
                        || c instanceof DeleteCommand
                        || c instanceof DeleteBarCommand
                        || c instanceof EditCommand
                        || c instanceof HelpCommand
                        || c instanceof GroupCommand
                        || c instanceof CopyCommand
                        || c instanceof AddOverlayCommand
                        || c instanceof ListCommand
                        || c instanceof OverlayBarGroup
                        || c instanceof OverlayBarSong
                        || c instanceof OpenCommand
                        || c instanceof AsciiCommand
                        || c instanceof OverlayGroupGroup) {
                    output = c.execute(songs, ui, storage);
                    if (!(c instanceof HelpCommand
                        || c instanceof ViewCommand
                        || c instanceof ListCommand)) {
                        undoRedoStack.add(songs);
                    }
                } else if (c instanceof UndoCommand || c instanceof RedoCommand) {
                    output = c.execute(songs, ui, storage, undoRedoStack);
                    songs = undoRedoStack.getCurrentVersion();
                } else {
                    output = c.execute(songs, ui, storage);
                }
                System.out.println(output);
                metronome.start(c.startMetronome());
                isExit = c.isExit();
            } catch (DucatsException e) {
                System.out.println(ui.showError(e));
            }
        }
    }

    /**
     * duke.Main function for duke.Duke, which creates a new duke.Duke object and runs it.
     * @param args Standard Java arguments for a main function, in this case, not used
     */
    public static void main(String[] args) {
        new Ducats().run();
    }


    public SongList getSongs() {
        return this.songs;
    }

    //@@author
}
