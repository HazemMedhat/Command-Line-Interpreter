import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Parser parser = new Parser();
        Terminal terminal = new Terminal(parser);

        boolean flag = true;

        String[] commands = {"echo", "pwd", "cd", "cp", "cp-r", "1s", "1s-r", "mkdir", "rm", "cat",
                "touch", "wc", "history", "exit"};

        System.out.println("Welcome to CLI :)");
        System.out.println("Available commands");
        System.out.println("1-echo\n" + "2-pwd\n" + "3-cd\n" + "4-cp\n" + "5-cp-r\n" + "6-1s\n" + "7-1s-r\n" + "8-mkdir\n" + "9-rm\n" + "10-cat\n"
                + "11-touch\n" + "12-wc\n" + "13-history\n" + "14-exit");
        while (flag) {
            System.out.print("Enter a command: ");
            String input = scanner.nextLine();
            parser.parse(input);
            boolean validCommand = false;
            for (int i = 0; i < commands.length; i++) {
                if (parser.getCommandName().equals(commands[i])) {
                    validCommand = true;
                    break;
                }
            }
            if (!validCommand) {
                System.out.println("Invalid command");
            } else if (parser.getCommandName().equals("exit")) {
                System.out.println("Thank you for using our CLI , See you soon :) ");
                flag = false; // Exit the loop when the user enters "exit"
            } else {
                terminal.chooseCommandAction();
            }
        }
    }
}
