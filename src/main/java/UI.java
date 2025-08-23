import java.util.Scanner;

public class UI {
    private Scanner scanner;

    public UI() {
        scanner = new Scanner(System.in);
    }

    public void showWelcome(String name) {
        System.out.println("____________________________________________________________");
        System.out.printf(" Hello! I'm %s%n", name);
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
    }

    public void showGoodbye() {
        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");
    }

    public void showMessage(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(" " + message);
        System.out.println("____________________________________________________________");
    }

    public void showTaskList(TaskList tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(tasks);
        System.out.println("____________________________________________________________");
    }

    public void showTaskMarkedDone(Task task) {
        System.out.println("____________________________________________________________");
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + task);
        System.out.println("____________________________________________________________");
    }

    public void showTaskMarkedNotDone(Task task) {
        System.out.println("____________________________________________________________");
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + task);
        System.out.println("____________________________________________________________");
    }

    public void showInvalidTaskNumber() {
        System.out.println("____________________________________________________________");
        System.out.println(" OOPS!!! Invalid task number.");
        System.out.println("____________________________________________________________");
    }

    public void showInvalidMarkCommand() {
        System.out.println("____________________________________________________________");
        System.out.println(" OOPS!!! Please specify which task to mark (e.g., 'mark 2').");
        System.out.println("____________________________________________________________");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void close() {
        scanner.close();
    }


}
