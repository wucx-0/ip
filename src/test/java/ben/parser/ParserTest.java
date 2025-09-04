package ben.parser;

import ben.BenException;
import ben.command.AddCommand;
import ben.command.Command;
import ben.command.DeleteCommand;
import ben.command.DueCommand;
import ben.command.ExitCommand;
import ben.command.ListCommand;
import ben.command.MarkCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @Test
    public void parse_exitCommand_returnsExitCommand() throws BenException {
        Command command = Parser.parse("bye");
        assertInstanceOf(ExitCommand.class, command);
        assertTrue(command.isExit());
    }

    @Test
    public void parse_listCommand_returnsListCommand() throws BenException {
        Command command = Parser.parse("list");
        assertInstanceOf(ListCommand.class, command);
    }

    @Test
    public void parse_markCommandWithNumber_returnsMarkCommand() throws BenException {
        Command command = Parser.parse("mark 1");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_unmarkCommandWithNumber_returnsMarkCommand() throws BenException {
        Command command = Parser.parse("unmark 2");
        assertInstanceOf(MarkCommand.class, command);
    }

    @Test
    public void parse_deleteCommandWithNumber_returnsDeleteCommand() throws BenException {
        Command command = Parser.parse("delete 3");
        assertInstanceOf(DeleteCommand.class, command);
    }

    @Test
    public void parse_todoCommandWithDescription_returnsAddCommand() throws BenException {
        Command command = Parser.parse("todo read book");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_deadlineCommandWithValidFormat_returnsAddCommand() throws BenException {
        Command command = Parser.parse("deadline return book /by 2019-12-25");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_eventCommandWithValidFormat_returnsAddCommand() throws BenException {
        Command command = Parser.parse("event meeting /from 2019-12-25 1400 /to 2019-12-25 1600");
        assertInstanceOf(AddCommand.class, command);
    }

    @Test
    public void parse_dueCommandWithDate_returnsDueCommand() throws BenException {
        Command command = Parser.parse("due 2019-12-25");
        assertInstanceOf(DueCommand.class, command);
    }

    // Edge cases and error conditions
    @Test
    public void parse_emptyString_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse(""));
    }

    @Test
    public void parse_nullString_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse(null));
    }

    @Test
    public void parse_whitespaceOnly_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse("   "));
    }

    @Test
    public void parse_invalidCommand_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse("invalidcommand"));
    }

    @Test
    public void parse_emptyTodoDescription_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse("todo"));
    }

    @Test
    public void parse_emptyDeadlineDescription_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse("deadline"));
    }

    @Test
    public void parse_emptyEventDescription_throwsException() {
        assertThrows(BenException.class, () -> Parser.parse("event"));
    }

    // Test parseTaskNumber method
    @Test
    public void parseTaskNumber_validNumber_returnsCorrectNumber() throws BenException {
        assertEquals(5, Parser.parseTaskNumber("5", "mark"));
        assertEquals(1, Parser.parseTaskNumber("1", "delete"));
        assertEquals(10, Parser.parseTaskNumber("10", "unmark"));
    }

    @Test
    public void parseTaskNumber_invalidNumber_throwsException() {
        assertThrows(BenException.class, () -> Parser.parseTaskNumber("abc", "mark"));
        assertThrows(BenException.class, () -> Parser.parseTaskNumber("1.5", "delete"));
        assertThrows(BenException.class, () -> Parser.parseTaskNumber("-1", "unmark"));
    }

    @Test
    public void parseTaskNumber_emptyString_throwsException() {
        assertThrows(BenException.class, () -> Parser.parseTaskNumber("", "mark"));
        assertThrows(BenException.class, () -> Parser.parseTaskNumber("   ", "delete"));
    }

    // Test case sensitivity
    @Test
    public void parse_commandsAreCaseInsensitive_worksCorrectly() throws BenException {
        assertInstanceOf(ExitCommand.class, Parser.parse("BYE"));
        assertInstanceOf(ExitCommand.class, Parser.parse("Bye"));
        assertInstanceOf(ListCommand.class, Parser.parse("LIST"));
        assertInstanceOf(AddCommand.class, Parser.parse("TODO read book"));
    }
}