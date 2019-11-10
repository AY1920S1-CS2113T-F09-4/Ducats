package ducats.commands;

import ducats.DucatsException;
import ducats.Storage;
import ducats.Ui;
import ducats.components.SongList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class NewCommandTest {

    private static SongList dummySongList;
    private static Ui dummyUi;
    private static String dummyFileDelimiter;
    private static Storage dummyStorage;

    /**
     * The method to be executed before each test in the class to ensure that the dummy song list, ui,
     * file delimiter and storage is set up appropriately before testing the command.
     * @throws DucatsException if an exception occurs in the parsing of the message or in IO
     */
    @BeforeEach
    public void executedBeforeEach() throws DucatsException {
        try {
            dummySongList = new SongList();
            dummyUi = new Ui();
            dummyFileDelimiter = System.getProperty("file.separator");
            dummyStorage = new Storage(System.getProperty("user.dir") + dummyFileDelimiter + "test");
            dummyStorage.updateFile(dummySongList);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void execute_normalInput_success() throws DucatsException {
        NewCommand newTest1 = new NewCommand("new test aminor 4/4 120");
        String testOutput1 = newTest1.execute(dummySongList, dummyUi, dummyStorage);
        String expectedOutput1 = "\n" + "_____________________________________________\n"
                + "Got it. I've added this song:\n"
                + "  test\n"
                + "Now you have 1 song in the list.\n"
                + "_____________________________________________"
                + "\n";

        //Song dummySong = new Song("test", "aminor", 120);
        //String expectedOutput1 = dummyUi.formatNewSong(dummySongList.getSongList(), dummySong);
        assertEquals(expectedOutput1, testOutput1);
    }

    @Test
    public void execute_emptyInput_exceptionThrown() throws DucatsException {
        NewCommand newTest2 = new NewCommand("new ");
        DucatsException testDucatsException2 = assertThrows(DucatsException.class, () -> {
            newTest2.execute(dummySongList, dummyUi, dummyStorage);
        });
        DucatsException expectedDucatsException2 = new DucatsException("new ");
        assertEquals(expectedDucatsException2.getMessage(), testDucatsException2.getMessage());
    }

    @Test
    public void execute_repeatedSongName_exceptionThrown() throws DucatsException {
        NewCommand newTest3 = new NewCommand("new test aminor 4/4 120");
        newTest3.execute(dummySongList, dummyUi, dummyStorage);
        NewCommand newTest4 = new NewCommand("new test cmajor 3/4 60");
        DucatsException testDucatsException4 = assertThrows(DucatsException.class, () -> {
            newTest4.execute(dummySongList, dummyUi, dummyStorage);
        });
        DucatsException expectedDucatsException4 = new DucatsException("new test cmajor 3/4 60", "song name");
        assertEquals(expectedDucatsException4.getMessage(), testDucatsException4.getMessage());
    }

    @Test
    public void execute_invalidKey_exceptionThrown() throws DucatsException {
        //to be completed
    }

    @Test
    public void execute_invalidTime_exceptionThrown() throws DucatsException {
        //to be completed
    }

    @Test
    public void isExit_normalInput_success() {
        NewCommand newTest1 = new NewCommand("new test aminor 4/4 120");
        assertEquals(false, newTest1.isExit());
    }
}
