package application.management.plugins;

import application.management.interfaces.IPlugin;
import application.management.interfaces.IBook;
import application.management.interfaces.ICore;
import application.management.interfaces.ILoan;
import application.management.interfaces.IUIController;
import application.management.interfaces.IUser;
import application.management.interfaces.IUIUtils;

import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class UserManagementPlugin implements IPlugin {

    private static final IUser userController = ICore.getInstance().getUserController();
    private static final IUIUtils uiUtilsController = ICore.getInstance().getUIUtilsController();
    private static final IBook bookController = ICore.getInstance().getBookController();

    @Override
    public boolean init() {
        return init(null);
    }

    @Override
    public boolean init(Stage stage) {
        showUserManagementScreen(stage);
        return true;
    }

    public void showUserLoanTransactionScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("User loan transactions");
    
        Label userIdLabel = new Label("Enter user ID:");
        TextField userIdField = new TextField();
        userIdField.setPrefWidth(200);
        Button searchButton = new Button("Search");
    
        ILoan loanController = ICore.getInstance().getLoanController();
        List<ILoan> userLoanHistory = new ArrayList<>();
    
        List<TableColumn<ILoan, ?>> columns = new ArrayList<>();
        columns.add(uiUtilsController.createColumn("book_id", ILoan::getLoanBookId));
        columns.add(uiUtilsController.createColumn("book_title", loan -> {
            IBook book = bookController.getBookById(loan.getLoanBookId());
            return book != null ? book.getBookTitle() : "N/A";
        }));
        columns.add(uiUtilsController.createColumn("loan_date", ILoan::getLoanDate));
        columns.add(uiUtilsController.createColumn("return_date", ILoan::getReturnDate));
        columns.add(uiUtilsController.createColumn("user_name", loan -> {return userController.getUserName(loan.getLoanUserId());
        }));
    
        TableView<ILoan> userHistoryTableView = uiUtilsController.createTableView(userLoanHistory, columns, "Search for a user to see their loan history");
        Label feedbackLabel = new Label();
    
        searchButton.setOnAction(event -> {
            String idText = userIdField.getText().trim();
    
            if (!idText.isEmpty()) {
                try {
                    List<ILoan> loanHistory = loanController.getLoanHistory(Integer.parseInt(idText));
                    if (loanHistory.isEmpty()) {
                        uiUtilsController.showFeedbackLabel(feedbackLabel, "No transactions found for this user.");
                        userHistoryTableView.getItems().clear();
                    } else {
                        userLoanHistory.clear();
                        userLoanHistory.addAll(loanHistory);
                        userHistoryTableView.setItems(FXCollections.observableArrayList(userLoanHistory));
                        uiUtilsController.showFeedbackLabel(feedbackLabel, "");
                    }
                } catch (NumberFormatException e) {
                    uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter a valid number.");
                }
            } else
                uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter an ID to search for a user.");
        });
        userIdField.setOnAction(event -> searchButton.fire());
    
        GridPane gridPaneForUserLoanHistory = uiUtilsController.createGridPane();
        gridPaneForUserLoanHistory.add(titleLabel, 0, 0, 2, 1);
        gridPaneForUserLoanHistory.add(userIdLabel, 0, 1);
        gridPaneForUserLoanHistory.add(userIdField, 1, 1);
        gridPaneForUserLoanHistory.add(searchButton, 2, 1);
        gridPaneForUserLoanHistory.add(userHistoryTableView, 0, 3, 3, 1);
        gridPaneForUserLoanHistory.add(feedbackLabel, 0, 4, 3, 1);
    
        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForUserLoanHistory);
    }    

    public void showListUserScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("List of users");
    
        List<IUser> users = new ArrayList<>(userController.getListUserMap().values());
        List<TableColumn<IUser, ?>> columns = new ArrayList<>();
        columns.add(uiUtilsController.createColumn("user_name", IUser::getUserName));
        columns.add(uiUtilsController.createColumn("user_id", IUser::getUserId));
        TableView<IUser> listUsersTableView = uiUtilsController.createTableView(users, columns, "No users available.");

        TextField searchField = new TextField();
        uiUtilsController.createSearchField(searchField, "🔍 Search by user_name or user_id");
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<IUser> filteredUsers = uiUtilsController.searchOnListUsers(newValue, users); 
            listUsersTableView.setItems(filteredUsers);
        });

        Label listUserTableInfoLabel = new Label(users.size() + " rows x " + columns.size() + " columns.");

        GridPane gridPaneForListUserScreen = uiUtilsController.createGridPane();
        gridPaneForListUserScreen.add(titleLabel, 0, 0, 2, 1);
        gridPaneForListUserScreen.add(searchField, 0, 1);
        gridPaneForListUserScreen.add(listUsersTableView, 0, 2);
        gridPaneForListUserScreen.add(listUserTableInfoLabel, 0, 3);

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForListUserScreen);
    }
     
    public void showCreateUserScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Create user");

        Label nameLabel = new Label("Enter user name:");
        TextField nameField = new TextField();
        nameField.setPrefWidth(200);
        Button createUserButton = new Button("Create");

        Label feedbackLabel = new Label();

        createUserButton.setOnAction(event -> {
            String userName = nameField.getText().trim();
            if (!userName.isEmpty()) {
                userController.addUserToLibrary(userName);
                uiUtilsController.showFeedbackLabel(feedbackLabel, "User created: " + userName + ".");
                nameField.clear();
            } else
                uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter a name to create a user.");
        });
        nameField.setOnAction(event -> createUserButton.fire());

        GridPane gridPaneForCreatUser = uiUtilsController.createGridPane();
        gridPaneForCreatUser.add(titleLabel, 0, 0, 2, 1);
        gridPaneForCreatUser.add(nameLabel, 0, 1);
        gridPaneForCreatUser.add(nameField, 1, 1);
        gridPaneForCreatUser.add(createUserButton, 2, 1); 
        gridPaneForCreatUser.add(feedbackLabel, 0, 3, 2, 2);

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForCreatUser);
    }

    public void showRemoveUserScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Remove a user");

        Label idLabel = new Label("Enter user ID:");
        TextField idField = new TextField();
        idField.setPrefWidth(200);
        Button deleteUserButton = new Button("Remove");

        Label feedbackLabel = new Label();

        deleteUserButton.setOnAction(event -> {
            String idText = idField.getText().trim();
            if (!idText.isEmpty()) {
                try {
                    if (userController.removeUserFromLibrary(Integer.parseInt(idText)))
                        uiUtilsController.showFeedbackLabel(feedbackLabel, "User with Id " + idText + " was deleted.");
                    else
                        uiUtilsController.showFeedbackLabel(feedbackLabel, "The user is not registered in the system.");
                    idField.clear();
                } catch (NumberFormatException e) {
                    uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter a valid number.");
                }
            } else
                uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter an Id to remove a user.");
        });
        idField.setOnAction(event -> deleteUserButton.fire());

        GridPane gridPaneForRemoveUser = uiUtilsController.createGridPane();
        gridPaneForRemoveUser.add(titleLabel, 0, 0, 2, 1);
        gridPaneForRemoveUser.add(idLabel, 0, 1);
        gridPaneForRemoveUser.add(idField, 1, 1);
        gridPaneForRemoveUser.add(deleteUserButton, 2, 1);
        gridPaneForRemoveUser.add(feedbackLabel, 0, 3, 2, 2);

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForRemoveUser);
    }

    public void showUserManagementScreen(Stage stage) {
        IUIController uiController = ICore.getInstance().getUIController();
        List<MenuItem> userManagement = uiController.createMenuItem("Users", "List", "Create", "Remove", "Loan History");

        userManagement.get(0).setOnAction(e -> showListUserScreen(stage));
        userManagement.get(1).setOnAction(e -> showCreateUserScreen(stage));
        userManagement.get(2).setOnAction(e -> showRemoveUserScreen(stage));
        userManagement.get(3).setOnAction(e -> showUserLoanTransactionScreen(stage));
    }

}