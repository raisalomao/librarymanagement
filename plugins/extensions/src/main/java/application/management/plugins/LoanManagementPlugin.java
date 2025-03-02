package application.management.plugins;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import application.management.interfaces.IBook;
import application.management.interfaces.ICore;
import application.management.interfaces.ILoan;
import application.management.interfaces.IPlugin;
import application.management.interfaces.ISystemUtils;
import application.management.interfaces.IUIController;
import application.management.interfaces.IUIUtils;
import application.management.interfaces.IUser;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoanManagementPlugin implements IPlugin {

    private static final IBook bookController = ICore.getInstance().getBookController();
    private static final ILoan loanController = ICore.getInstance().getLoanController();
    private static final IUser userController = ICore.getInstance().getUserController();
    private static final IUIUtils uiUtilsController = ICore.getInstance().getUIUtilsController();
    private static final ISystemUtils systemUtilsController = ICore.getInstance().getSystemUtilsController();

    @Override
    public boolean init() {
        return init(null);
    }

    @Override
    public boolean init(Stage stage) {
        showLoanManagementScreen(stage);
        return true;
    }

    public void showLoanScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Loan a book");
    
        String[] fieldLabels = {"Enter the user Id:", "Enter the loan date:"};
        TextField userIdField = new TextField();
        userIdField.setAlignment(Pos.CENTER_LEFT);
        userIdField.setMaxWidth(250);

        DatePicker loanDatePicker = uiUtilsController.createDatePicker();
        Label bookLabel = new Label("Select the book(s):");

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search for book(s)");
        ListView<String> bookListView = uiUtilsController.createBookListView(searchField, true);
        uiUtilsController.addTooltip(bookListView, "Press CTRL to select more than one book.");
    
        Button loanBookButton = new Button("Loan");
        loanBookButton.setMaxWidth(500);
        Label feedbackLabel = new Label();
    
        loanBookButton.setOnAction(event -> {
            String userIdText = userIdField.getText().trim();
            ObservableList<String> selectedBooks = bookListView.getSelectionModel().getSelectedItems();
            LocalDate loanDateValue = loanDatePicker.getValue();
    
            try {
                int userId = Integer.parseInt(userIdText);

                if (!uiUtilsController.validateTransactionsData(userIdText, selectedBooks, loanDateValue, feedbackLabel))
                    return;
                if (selectedBooks.size() > 5) {
                    uiUtilsController.showFeedbackLabel(feedbackLabel, "A user can only borrow a maximum of 5 books per loan.");
                    return;
                }
    
                List<String> booksToRemove = new ArrayList<>(selectedBooks);
    
                for (String bookTitle : booksToRemove) {
                    int bookId = bookController.getListBookMap().values().stream().filter(book -> book.getBookTitle().equals(bookTitle))
                    .mapToInt(IBook::getBookId).findFirst().orElse(-1);
    
                    String loanDate = loanDateValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    ILoan loan = loanController.createLoan(bookId, userId, loanDate, "N/A");
                    loanController.addLoanToHistory(userId, loan);
                    bookController.setBookBorrowedStatus(bookId, true);
                    bookListView.getItems().remove(bookTitle);
                }
    
                uiUtilsController.showFeedbackLabel(feedbackLabel, "Done! Books were loaned to user: '" + userController.getUserName(userId) + "'");
    
                userIdField.clear();
                loanDatePicker.setValue(null);
                bookListView.getSelectionModel().clearSelection();
    
            } catch (NumberFormatException e) {
                uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter a valid number.");
            }
        });
        bookListView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                loanBookButton.fire();
        });

        GridPane gridPaneForLoanBook = uiUtilsController.createGridPane();
        gridPaneForLoanBook.add(new Label(fieldLabels[0]), 0, 1);
        gridPaneForLoanBook.add(userIdField, 1, 1);
        gridPaneForLoanBook.add(new Label(fieldLabels[1]), 0, 2);
        gridPaneForLoanBook.add(loanDatePicker, 1, 2);
        gridPaneForLoanBook.add(bookLabel, 0, 4);
        gridPaneForLoanBook.add(titleLabel, 0, 0, 4, 1);
        gridPaneForLoanBook.add(searchField, 1, 3);
        gridPaneForLoanBook.add(bookListView, 1, 4);
        gridPaneForLoanBook.add(loanBookButton, 1, 5);
        gridPaneForLoanBook.add(feedbackLabel, 0, 6, 4, 1);        
        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForLoanBook);
    }

    public void showReturnBookScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Return a book");
    
        String[] fieldLabels = {"Enter the user Id:", "Enter the return date:"};
        TextField userIdField = new TextField();
        userIdField.setAlignment(Pos.CENTER_LEFT);
        userIdField.setMaxWidth(250);
    
        DatePicker returnDatePicker = uiUtilsController.createDatePicker();
        Label bookLabel = new Label("Select the book(s):");
    
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search for book(s)");
        ListView<String> bookListView = uiUtilsController.createBookListView(searchField, false);
        uiUtilsController.addTooltip(bookListView, "Press CTRL to select more than one book.");
    
        Button returnBookButton = new Button("Return");
        returnBookButton.setMaxWidth(500);
        Label feedbackLabel = new Label();
    
        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
    
        returnBookButton.setOnAction(event -> {
            String userIdText = userIdField.getText().trim();
            ObservableList<String> selectedBooks = bookListView.getSelectionModel().getSelectedItems();
            LocalDate returnDateValue = returnDatePicker.getValue();
    
            try {
                if (userIdText.isEmpty() || selectedBooks.isEmpty() || returnDateValue == null) {
                    uiUtilsController.showFeedbackLabel(feedbackLabel, "Fill in all fields.");
                    return;
                }
                int userId = Integer.parseInt(userIdText);

                if (!uiUtilsController.validateTransactionsData(userIdText, selectedBooks, returnDateValue, feedbackLabel))
                    return;
    
                List<String> booksToRemove = new ArrayList<>();
    
                for (String bookTitle : selectedBooks) {
                    int bookId = bookController.getListBookMap().values().stream()
                    .filter(book -> book.getBookTitle().equals(bookTitle))
                    .mapToInt(IBook::getBookId).findFirst().orElse(-1);
    
                    List<ILoan> loanHistory = loanController.getLoanHistory(userId);
                    ILoan existingLoan = null;
    
                    for (ILoan loan : loanHistory) {
                        if (loan.getLoanBookId() == bookId && loan.getReturnDate().equals("N/A")) {
                            existingLoan = loan;
                            break;
                        }
                    }
    
                    if (existingLoan == null) {
                        uiUtilsController.showFeedbackLabel(feedbackLabel, "No active loan found for this.");
                        return;
                    }
                    LocalDate loanDate = LocalDate.parse(existingLoan.getLoanDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    if (!systemUtilsController.isValidReturnDate(loanDate, returnDateValue)) {
                        uiUtilsController.showFeedbackLabel(feedbackLabel, "The return date cannot be before the loan date.");
                        return;
                    }
    
                    String returnDate = returnDateValue.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    
                    existingLoan.setReturnDate(returnDate);
                    loanController.updateLoanHistory(userId, existingLoan);
                    bookController.setBookBorrowedStatus(bookId, false);
                    booksToRemove.add(bookTitle);
                }
    
                bookListView.getItems().removeAll(booksToRemove);
                uiUtilsController.showFeedbackLabel(feedbackLabel, "Done! Book(s) returned by: '" + userController.getUserName(userId) + "'.");
    
                userIdField.clear();
                returnDatePicker.setValue(null);
                bookListView.getSelectionModel().clearSelection();
    
            } catch (NumberFormatException e) {
                uiUtilsController.showFeedbackLabel(feedbackLabel, "You must enter a valid number.");
            }
        });
        bookListView.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER))
                returnBookButton.fire();
        });

        GridPane gridPaneForReturnBook = uiUtilsController.createGridPane();
        gridPaneForReturnBook.add(new Label(fieldLabels[0]), 0, 1);
        gridPaneForReturnBook.add(userIdField, 1, 1);
        gridPaneForReturnBook.add(new Label(fieldLabels[1]), 0, 2);
        gridPaneForReturnBook.add(returnDatePicker, 1, 2);
        gridPaneForReturnBook.add(bookLabel, 0, 4);
        gridPaneForReturnBook.add(titleLabel, 0, 0, 4, 1);
        gridPaneForReturnBook.add(searchField, 1, 3);
        gridPaneForReturnBook.add(bookListView, 1, 4);
        gridPaneForReturnBook.add(returnBookButton, 1, 5);
        gridPaneForReturnBook.add(feedbackLabel, 0, 6, 4, 1);
    
        vBox.getChildren().add(gridPaneForReturnBook);
    }    
    
    public void showLoanManagementScreen(Stage stage) {
        IUIController uiController = ICore.getInstance().getUIController();
        List<MenuItem> loanManagement = uiController.createMenuItem("Loan Transactions", "Loan", "Return");
        loanManagement.get(0).setOnAction(e -> showLoanScreen(stage));
        loanManagement.get(1).setOnAction(e -> showReturnBookScreen(stage));
    }

}