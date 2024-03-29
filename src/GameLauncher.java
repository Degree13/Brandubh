import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.BRBController;
import control.BRBDecider;
import javafx.application.Application;
import javafx.stage.Stage;
import control.BRBController;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;
import view.BRBRootPane;
import view.BRBView;
import java.util.Scanner;
import boardifier.control.*;

import static boardifier.view.ConsoleColor.BLACK;
import static boardifier.view.ConsoleColor.WHITE;

public class GameLauncher extends Application {
    public static Thread loadFilesThread;

    public static void main(String[] args) {
        loadFilesThread = new Thread(() -> BRBDecider.loadData("dataMap.bin"));
        loadFilesThread.start();

        // TODO; temporary, remove later
        args = new String[]{"graphical"};

        System.out.println("Choose the game version:");
        System.out.println("1. Console Version");
        System.out.println("2. Graphical Version");
        if (args.length > 0 && args[0].equals("console".toLowerCase())) {
            // Execute the console version
            BrandubhConsole game = new BrandubhConsole();
            game.start(args);
        } else if (args.length > 0 && args[0].equals("graphical".toLowerCase())) {
            // Execute the graphical version
            Controller.gVersion = true;
            launch(args);
        } else {
            String choice = BRBController.input.nextLine().replaceAll("\\D+", "");
            boolean continueLoop = true;
            int choiceInt = 0;
            while (continueLoop) {
                try {
                    choiceInt = Integer.parseInt(choice);
                    if (choiceInt == 1 || choiceInt == 2) {
                        continueLoop = false;
                    } else {
                        System.out.println("Invalid choice. Please enter 1 or 2.");
                        choice = BRBController.input.nextLine().replaceAll("\\D+", "");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                    choice = BRBController.input.nextLine().replaceAll("\\D+", "");
                }
            }

            if (choiceInt == 1) {
                // Execute the console version
                BrandubhConsole game = new BrandubhConsole();
                game.start(args);
            } else if (choiceInt == 2) {
                // Execute the graphical version
                Controller.gVersion = true;
                launch(args);
            } else {
                System.out.println("Invalid choice. Exiting the game.");
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        if (GameLauncher.loadFilesThread.isAlive()) {
            Thread loadBarThread = new Thread(() -> BrandubhConsole.loadBar(500, "Data still loading, please wait"));
            loadBarThread.start();
            try {
                GameLauncher.loadFilesThread.join();
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            loadBarThread.interrupt();
            Thread.sleep(50);
            if (BRBDecider.consoleMessages != "") {
                System.out.println(BRBDecider.consoleMessages);
                BRBDecider.consoleMessages = "";
                System.out.println(BLACK);
            }
        }
        // create the global model
        Model model = new Model();
        int mode = 0;
        // add some players taking mode into account
        if (mode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        }
        else if (mode == 1) {
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
        }
        else if (mode == 2) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }
        // register a single stage for the game, called hole
        StageFactory.registerModelAndView("BRB", "model.BRBStageModel", "view.BRBStageView");
        // create the root pane, using the subclass HoleRootPane
        BRBRootPane rootPane = new BRBRootPane();
        // create the global view.
        BRBView view = new BRBView(model, stage, rootPane);
        // create the controllers.
        BRBController control = new BRBController(model,view);
        // set the name of the first stage to create when the game is started
        control.setFirstStageName("BRB");
        // set the stage title
        stage.setTitle("Brandubh - A game of the Dark Ages of Ireland");
        // show the JavaFx main stage
        stage.show();
        //view.resetView();
    }
}
