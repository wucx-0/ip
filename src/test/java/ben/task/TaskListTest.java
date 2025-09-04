package ben.task;

import ben.BenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {
    private TaskList taskList;
    private Task todoTask;
    private Task deadlineTask;
    private Task eventTask;

    @BeforeEach
    public void setUp() throws BenException {
        taskList = new TaskList();
        todoTask = new ToDo("read book");
        deadlineTask = new Deadline("return book", "2019-12-25");
        eventTask = new Event("team meeting", "2019-12-25 1400", "2019-12-25 1600");
    }

    // Test addTask method
    @Test
    public void addTask_singleTask_increasesSize() throws BenException {
        assertEquals(0, taskList.getSize());
        taskList.addTask(todoTask);
        assertEquals(1, taskList.getSize());
    }

    @Test
    public void addTask_multipleTasks_correctSize() throws BenException {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        taskList.addTask(eventTask);
        assertEquals(3, taskList.getSize());
    }

    // Test getTask method
    @Test
    public void getTask_validIndex_returnsCorrectTask() throws BenException {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);

        Task retrieved1 = taskList.getTask(1);
        Task retrieved2 = taskList.getTask(2);

        assertEquals(todoTask, retrieved1);
        assertEquals(deadlineTask, retrieved2);
    }

    @Test
    public void getTask_indexTooLow_throwsException() throws BenException {
        taskList.addTask(todoTask);
        assertThrows(BenException.class, () -> taskList.getTask(0));
        assertThrows(BenException.class, () -> taskList.getTask(-1));
    }

    @Test
    public void getTask_indexTooHigh_throwsException() throws BenException {
        taskList.addTask(todoTask);
        assertThrows(BenException.class, () -> taskList.getTask(2));
        assertThrows(BenException.class, () -> taskList.getTask(100));
    }

    @Test
    public void getTask_emptyList_throwsException() {
        assertThrows(BenException.class, () -> taskList.getTask(1));
    }

    // Test deleteTask method
    @Test
    public void deleteTask_validIndex_removesAndReturnsTask() throws BenException {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        assertEquals(2, taskList.getSize());

        Task deleted = taskList.deleteTask(1);
        assertEquals(todoTask, deleted);
        assertEquals(1, taskList.getSize());

        // Remaining task should now be at index 1
        assertEquals(deadlineTask, taskList.getTask(1));
    }

    @Test
    public void deleteTask_lastTask_emptiesList() throws BenException {
        taskList.addTask(todoTask);
        taskList.deleteTask(1);
        assertEquals(0, taskList.getSize());
    }

    @Test
    public void deleteTask_invalidIndex_throwsException() throws BenException {
        taskList.addTask(todoTask);
        assertThrows(BenException.class, () -> taskList.deleteTask(0));
        assertThrows(BenException.class, () -> taskList.deleteTask(2));
        assertThrows(BenException.class, () -> taskList.deleteTask(-1));
    }

    // Test mark method
    @Test
    public void mark_validIndex_marksTaskComplete() throws BenException {
        taskList.addTask(todoTask);
        assertFalse(todoTask.isComplete());

        boolean result = taskList.mark(1);
        assertTrue(result);
        assertTrue(todoTask.isComplete());
    }

    @Test
    public void mark_alreadyCompleteTask_remainsComplete() throws BenException {
        taskList.addTask(todoTask);
        todoTask.markComplete();
        assertTrue(todoTask.isComplete());

        taskList.mark(1);
        assertTrue(todoTask.isComplete());
    }

    @Test
    public void mark_invalidIndex_throwsException() throws BenException {
        taskList.addTask(todoTask);
        assertThrows(BenException.class, () -> taskList.mark(0));
        assertThrows(BenException.class, () -> taskList.mark(2));
    }

    // Test unmark method
    @Test
    public void unmark_completedTask_marksIncomplete() throws BenException {
        taskList.addTask(todoTask);
        todoTask.markComplete();
        assertTrue(todoTask.isComplete());

        boolean result = taskList.unmark(1);
        assertTrue(result);
        assertFalse(todoTask.isComplete());
    }

    @Test
    public void unmark_alreadyIncompleteTask_remainsIncomplete() throws BenException {
        taskList.addTask(todoTask);
        assertFalse(todoTask.isComplete());

        taskList.unmark(1);
        assertFalse(todoTask.isComplete());
    }

    @Test
    public void unmark_invalidIndex_throwsException() throws BenException {
        taskList.addTask(todoTask);
        assertThrows(BenException.class, () -> taskList.unmark(0));
        assertThrows(BenException.class, () -> taskList.unmark(2));
    }

    // Test getSize method
    @Test
    public void getSize_emptyList_returnsZero() {
        assertEquals(0, taskList.getSize());
    }

    @Test
    public void getSize_withTasks_returnsCorrectCount() throws BenException {
        taskList.addTask(todoTask);
        assertEquals(1, taskList.getSize());

        taskList.addTask(deadlineTask);
        assertEquals(2, taskList.getSize());

        taskList.addTask(eventTask);
        assertEquals(3, taskList.getSize());
    }

    @Test
    public void getSize_afterDeletion_updatesCorrectly() throws BenException {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);
        assertEquals(2, taskList.getSize());

        taskList.deleteTask(1);
        assertEquals(1, taskList.getSize());
    }

    // Test toString method
    @Test
    public void toString_emptyList_returnsNoTasksMessage() {
        String result = taskList.toString();
        assertTrue(result.contains("No tasks"));
    }

    @Test
    public void toString_withTasks_returnsFormattedList() throws BenException {
        taskList.addTask(todoTask);
        taskList.addTask(deadlineTask);

        String result = taskList.toString();
        assertTrue(result.contains("1."));
        assertTrue(result.contains("2."));
        assertTrue(result.contains("read book"));
        assertTrue(result.contains("return book"));
    }

    @Test
    public void toString_withCompletedTasks_showsCorrectStatus() throws BenException {
        taskList.addTask(todoTask);
        todoTask.markComplete();

        String result = taskList.toString();
        assertTrue(result.contains("[X]")); // Completed task marker
    }

    // Test constructor with ArrayList
    @Test
    public void constructor_withExistingTasks_loadsCorrectly() throws BenException {
        ArrayList<Task> existingTasks = new ArrayList<>();
        existingTasks.add(todoTask);
        existingTasks.add(deadlineTask);

        TaskList newTaskList = new TaskList(existingTasks);
        assertEquals(2, newTaskList.getSize());
        assertEquals(todoTask, newTaskList.getTask(1));
        assertEquals(deadlineTask, newTaskList.getTask(2));
    }
}