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

/**
 * Parses user input strings into executable Command objects.
 * Handles command syntax validation and creates appropriate command instances.
 */
public class Parser {

    /**
     * Parses a complete user input string into an executable Command object.
     * Validates command syntax and creates the appropriate command instance.
     *
     * @param fullCommand the complete user input string to parse
     * @return a Command object representing the parsed user input
     * @throws BenException if the command is invalid, unrecognized, or missing required arguments
     */
    public static Command parse(String fullCommand) throws BenException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new BenException("Enter a ben.command!");
        }

        assert fullCommand != null : "Command should not be null after null check";

        String command = fullCommand.trim();
        String[] parts = command.split("\\s+", 2); // Split into ben.command and arguments
        String commandWord = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        assert parts.length >= 1 : "Split command should have at least one part";

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

    /**
     * Parses and validates a task number from a string argument.
     * Ensures the parsed number is a positive integer suitable for task indexing.
     *
     * @param arguments the string containing the task number to parse
     * @param commandType the command type requesting the parsing (used in error messages)
     * @return the parsed task number as a positive integer
     * @throws BenException if the string is not a valid positive integer or is empty
     */
    public static int parseTaskNumber(String arguments, String commandType) throws BenException {
        if (arguments.trim().isEmpty()) {
            throw new BenException("Please specify which task to " + commandType);
        }

        assert arguments != null : "Arguments should not be null after empty check";
        assert commandType != null : "Command type should not be null";

        try {
            int taskNumber = Integer.parseInt(arguments.trim());
            if (taskNumber <= 0) {
                throw new BenException("Task number must be a positive number!");
            }

            assert taskNumber > 0 : "Parsed task number should be positive";
            return taskNumber;
        } catch (NumberFormatException e) {
            throw new BenException("Task number must be a valid number!");
        }
    }
}