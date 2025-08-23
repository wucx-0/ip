import java.util.Scanner;

public class Ben {
    public static void main(String[] args) {
        //variables
        Scanner scanner = new Scanner(System.in);
        String name = "Ben";
        String lineBreak = "____________________________________________________________";

        //Print Welcome Msg
        System.out.println(lineBreak);
        System.out.printf(" Hello! I'm %s%n", name);
        System.out.println(" What can I do for you?");
        System.out.println(lineBreak);

        //while loop that echoes input

        String input;
        while (true) {
            input = scanner.nextLine();

            if (input.equals("bye")) {
                break;
            }

            System.out.println(lineBreak);
            System.out.printf(" %s%n", input);
            System.out.println(lineBreak);

        }

        //Print Goodbye Msg
        System.out.println(lineBreak);
        System.out.println(" Bye! Hope to see you again soon!");
        System.out.println(lineBreak);

        scanner.close();
    }
}
