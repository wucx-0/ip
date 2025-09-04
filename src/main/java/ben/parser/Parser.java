package ben.parser;

import ben.BenException;
import ben.command.AddCommand;
import ben.command.Command;
import ben.command.DeleteCommand;
import ben.command.DueCommand;
import ben.command.ExitCommand;
import ben.command.ListCommand;
import ben.command.MarkCommand;

import ben.command.*;

public class Parser {

    public static Command parse(String fullCommand) throws BenException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new BenException("Enter a ben.command!");
        }

        String command = fullCommand.trim();
        String[] parts = command.split("\\s+", 2); // Split into ben.command and arguments
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        switch (commandWord) {
            case "bye":
                return new ExitCommand();

            case "list":
                return new ListCommand();

            case "mark":
                return new MarkCommand(arguments, true);

            case "unmark":
                return new MarkCommand(arguments, false);

            case "delete":
                return new DeleteCommand(arguments);

            case "todo":
                if (arguments.isEmpty()) {
                    throw new BenException("The description cannot be empty! Please specify what you want to todo.");
                }
                return new AddCommand("todo", arguments);

            case "deadline":
                if (arguments.isEmpty()) {
                    throw new BenException("The description cannot be empty! Please specify what you want to deadline.");
                }
                return new AddCommand("deadline", arguments);

            case "event":
                if (arguments.isEmpty()) {
                    throw new BenException("The description cannot be empty! Please specify what you want to event.");
                }
                return new AddCommand("event", arguments);

            case "due":
                return new DueCommand(arguments);

            case "find":
                if (arguments.isEmpty()) {
                    throw new BenException("Please specify a keyword to search for! Format: find <keyword>");
                }
                return new FindCommand(arguments);


            default:
                throw new BenException("I'm sorry, say that again?");
        }
    }

    public static int parseTaskNumber(String arguments, String commandType) throws BenException {
        if (arguments.trim().isEmpty()) {
            throw new BenException("Please specify which ben.task to " + commandType + " (e.g., '" + commandType + " 2').");
        }

        try {
            int taskNumber = Integer.parseInt(arguments.trim());
            if (taskNumber <= 0) {
                throw new BenException("Task number must be a positive number!");
            }
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new BenException("ben.task.Task number must be a valid number!");
        }
    }
}