package ben.ui;

import ben.task.Task;
import ben.task.TaskList;

import java.util.Scanner;

import ben.task.Task;
import ben.task.TaskList;

/**
 * Handles user interface interactions including input/output formatting.
 */
public class UI {
    private Scanner scanner;
    private static final String DIVIDER = "____________________________________________________________";

    public UI() {
        scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message with the chatbot's name.
     *
     * @param name the name of the chatbot to display in the welcome message
     */
    public void showWelcome(String name) {
        System.out.println(DIVIDER);
        System.out.printf(" Hello! I'm %s%n", name);
        System.out.println(" What can I do for you?");
        System.out.println(DIVIDER);
    }

    /**
     * Displays the goodbye message when the application terminates.
     */
    public void showGoodbye() {
        System.out.println(DIVIDER);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    /**
     * Displays a general message to the user with proper formatting.
     *
     * @param message the message content to display to the user
     */
    public void showMessage(String message) {
        System.out.println(DIVIDER);
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    /**
     * Displays the current list of tasks with proper numbering and formatting.
     *
     * @param tasks the TaskList containing all tasks to display
     */
    public void showTaskList(TaskList tasks) {
        System.out.println(tasks);
    }

    public void showTaskMarkedDone(Task task) {
        System.out.println(" Nice! I've marked this ben.task as done:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task has been successfully added.
     *
     * @param task the task that was added
     * @param totalTasks the new total number of tasks after addition
     */
    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this ben.task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    public void showTaskMarkedNotDone(Task task) {
        System.out.println(" OK, I've marked this ben.task as not done yet:");
        System.out.println("   " + task);
    }

    public void showTaskDeleted(Task task, int remainingTasks) {
        System.out.println(" Noted. I've removed this ben.task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + remainingTasks + " tasks in the list.");
    }

    /**
     * Displays an error message to the user with proper formatting.
     *
     * @param errorMessage the error message to display
     */
    public void showError(String errorMessage) {
        System.out.println(" OOPS!!! " + errorMessage);
    }

    /**
     * Reads a line of user input from the console.
     *
     * @return the user's input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Closes the input scanner to free system resources.
     * Should be called when the application terminates.
     */
    public void close() {
        scanner.close();
    }
}