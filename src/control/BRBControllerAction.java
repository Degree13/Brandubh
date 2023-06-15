package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerAction;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import view.BRBView;

/**
 * A basic action controller that only manages menu actions
 * Action events are mostly generated when there are user interactions with widgets like
 * buttons, checkboxes, menus, ...
 */
public class BRBControllerAction extends ControllerAction implements EventHandler<ActionEvent> {

    // to avoid lots of casts, create an attribute that matches the instance type.
    private BRBView BRBView;
    public int mode = 0;

    public BRBControllerAction(Model model, View view, Controller control) {
        super(model, view, control);
        // take the view parameter ot define a local view attribute with the real instance type, i.e. HoleView.
        BRBView = (BRBView) view;
        // set handlers dedicated to menu items
        setMenuHandlers();

        // If needed, set the general handler for widgets that may be included within the scene.
        // In this case, the current gamestage view must be retrieved and casted to the right type
        // in order to have an access to the widgets, and finally use setOnAction(this).
        // For example, assuming the current gamestage view is an instance of MyGameStageView, which
        // creates a Button myButton :
        // ((MyGameStageView)view.getCurrentGameStageView()).getMyButton().setOnAction(this).

    }

    private void setMenuHandlers() {
        // set event handler on the MenuIntro item
        BRBView.getMenuIntro().setOnAction(e -> {
            control.stopGame();
            BRBView.resetView();
        });
        // set event handler on the MenuQuit item
        BRBView.getMenuQuit().setOnAction(e -> {
            System.exit(0);
        });
        // set event handler on the MenuPvP item
        BRBView.getMenuPvP().setOnAction(e -> {
            mode = 0;
            startNewGame();
        });
        // set event handler on the MenuPvE item
        BRBView.getMenuPvE().setOnAction(e -> {
            mode = 2;
            startNewGame();
        });
        // set event handler on the MenuEvP item
        BRBView.getMenuEvP().setOnAction(e -> {
            mode = 1;
            model.setNextPlayer();
            startNewGame();
        });
        // set event handler on the MenuEvE item
        BRBView.getMenuEvE().setOnAction(e -> {
            mode = 3;
            startNewGame();
        });
        // set event handler on the MenuAI1 item
        BRBView.getMenuAI1().setOnAction(e -> {
            ActionPlayer.typeAI = 1;
            String message = "AI successfully set to Random";
            createAlert(message);
        });
        // set event handler on the MenuAI2 item
        BRBView.getMenuAI2().setOnAction(e -> {
            ActionPlayer.typeAI = 2;
            String message = "AI successfully set to Smart";
            createAlert(message);
        });
        // set event handler on the MenuAI3 item
        BRBView.getMenuAI3().setOnAction(e -> {
            ActionPlayer.typeAI = 3;
            String message = "AI successfully set to EAT";
            createAlert(message);
        });
    }

    public void createAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        // remove the frame around the dialog
        alert.initStyle(StageStyle.UNDECORATED);
        // make it a children of the main game window => it appears centered
        // make it appear on the top right
        alert.setX(view.getStage().getX() + view.getStage().getWidth() - alert.getWidth());
        alert.initOwner(view.getStage());
        // set the message displayed
        alert.setHeaderText(message);
        // display the dialog and wait for the user to close it
        alert.showAndWait();
    }

    public void startNewGame() {
        model.removeAllPlayers();
        if (mode == 0) {
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        }
        else if (mode == 1) {
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
        }
        else if (mode == 2) {
            model.addComputerPlayer("computer");
            model.addHumanPlayer("player");
        }
        else if (mode == 3) {
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }
        try {
            control.startGame();
            control.nextPlayer();
        }
        catch(GameException err) {
            System.err.println(err.getMessage());
            System.exit(1);
        }
    }

    /**
     * The general handler for action events.
     * this handler should be used if the code to process a particular action event is too long
     * to fit in an arrow function (like with menu items above). In this case, this handler must be
     * associated to a widget w, by calling w.setOnAction(this) (see constructor).
     *
     * @param event An action event generated by a widget of the scene.
     */
    public void handle(ActionEvent event) {

        if (!model.isCaptureActionEvent()) return;
    }
}

