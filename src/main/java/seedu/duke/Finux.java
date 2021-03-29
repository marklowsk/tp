package seedu.duke;

import seedu.duke.command.CreditScoreMap;
import seedu.duke.command.Command;
import seedu.duke.command.CommandHandler;
import seedu.duke.command.ExitCommand;
import seedu.duke.exception.FileLoadingException;
import seedu.duke.parser.ParserHandler;
import seedu.duke.record.RecordList;
import seedu.duke.storage.Storage;
import seedu.duke.ui.Ui;

import java.util.ArrayList;

public class Finux {
    private Ui ui;
    private RecordList records;
    private Storage storage;
    private CommandHandler commandHandler;
    private ParserHandler parserHandler;
    private CreditScoreMap creditScoreMap;

    /**
     * Main entry-point for Finux application.
     */
    public static void main(String[] args) {
        new Finux().run();
    }

    /**
     * Runner for the FINUX Application.
     */
    private void run() {
        start();
        commandLooper();
        end();
    }

    /**
     * Starts the main application.
     */
    private void start() {
        try {
            ui = new Ui();
            storage = new Storage();
            parserHandler = new ParserHandler();
            commandHandler = new CommandHandler();
            storage.loadFile();
            records = new RecordList(storage.getRecordListData());
            creditScoreMap = new CreditScoreMap(
                    storage.getCreditScoreHashMapData());
            ui.printWelcomeMessage();
        } catch (FileLoadingException e) {
            Ui.printInitError();
            System.exit(-1);
        }
    }

    /**
     * Loops the application until an EXIT command is parsed.
     */
    private void commandLooper() {
        while (!commandHandler.isExit()) {
            String rawInput = ui.getUserInput();
            ArrayList<String> parsedStringList = parserHandler.getParseInput(rawInput);
            assert parsedStringList.size() != 0 : "Empty Parser Error";
            Command command = commandHandler.parseCommand(parsedStringList, records);
            if (command == null) {
                continue;
            }
            command.execute(records, ui, storage, creditScoreMap);
        }
    }

    /**
     * Exits the application.
     */
    private void end() {
        ui.printGoodByeMessage();
        System.exit(0);
    }
}
