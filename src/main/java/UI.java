import java.util.Scanner;

public class UI {
    private Scanner scanner;
    private static final String DIVIDER = "____________________________________________________________";

    public UI() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome(String name) {
        System.out.println(DIVIDER);
        System.out.printf(" Hello! I'm %s%n", name);
        System.out.println(" What can I do for you?");
        System.out.println(DIVIDER);
    }

    public void showGoodbye() {
        System.out.println(DIVIDER);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(DIVIDER);
    }

    public void showMessage(String message) {
        System.out.println(DIVIDER);
        System.out.println(" " + message);
        System.out.println(DIVIDER);
    }

    public void showLine() {
        System.out.println(DIVIDER);
    }

    public void showTaskList(TaskList tasks) {
        System.out.println(tasks);
    }

    public void showTaskMarkedDone(Task task) {
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
    }

    public void showTaskAdded(Task task, int totalTasks) {
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + totalTasks + " tasks in the list.");
    }

    public void showTaskMarkedNotDone(Task task) {
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
    }

    public void showTaskDeleted(Task task, int remainingTasks) {
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + task);
        System.out.println(" Now you have " + remainingTasks + " tasks in the list.");
    }

    public void showError(String errorMessage) {
        System.out.println(" OOPS!!! " + errorMessage);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }
}