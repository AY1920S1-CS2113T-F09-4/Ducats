package duke.commands;

import duke.DukeException;
import duke.Storage;
import duke.TaskList;
import duke.Ui;
import duke.tasks.Task;

import java.util.ArrayList;

/**
 * A class representing the command to display help: the command list.
 */
public class HelpCommand extends Command {

    /**
     * Constructor for the command to display help.
     * @param message the input message that resulted in the creation of the duke.Commands.Command
     */
    public HelpCommand(String message) {
        this.message = message;
    }

    /**
     * Displays the command list in use; returns the help messages intended to be displayed.
     *
     * @param taskList the duke.TaskList object that contains the task list
     * @param ui the Ui object responsible for the reading of user input and the display of
     *           the responses
     * @param storage the Storage object used to read and manipulate the .txt file
     * @return the string to be displayed in duke.Duke
     * @throws DukeException if an exception occurs in the parsing of the message or in IO
     */
    public String execute(TaskList taskList, Ui ui, Storage storage) throws DukeException {
        String helpMessage;
        helpMessage = message.substring(5).trim();
        if (message.isBlank()) {
            return ui.formatHelp(taskList.getTaskList());
        } else {
            ArrayList<Task> helpList = new ArrayList<>();
            helpList = taskList.findTask(helpMessage);
            return ui.formatHelp(helpList);
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