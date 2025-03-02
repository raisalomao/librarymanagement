package application.management.plugins;

import application.management.interfaces.IPlugin;
import application.management.interfaces.ISystemUtils;
import application.management.interfaces.ICore;
import application.management.interfaces.ILoan;
import application.management.interfaces.IUIController;
import application.management.interfaces.IBook;
import application.management.interfaces.IUIUtils;
import application.management.interfaces.IUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;

public class ReportManagementPlugin implements IPlugin {

    private static final IBook bookController = ICore.getInstance().getBookController();
    private static final ILoan loanController = ICore.getInstance().getLoanController();
    private static final IUser userController = ICore.getInstance().getUserController();
    private static final IUIUtils uiUtilsController = ICore.getInstance().getUIUtilsController();

    @Override
    public boolean init() {
        return init(null);
    }

    @Override
    public boolean init(Stage stage) {
        showReportManagementScreen(stage);
        return true;
    }

    public void showBorrowedBookScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Books currently on loan");

        List<ILoan> activeLoans = loanController.getActiveLoans();
        List<IBook> borrowedBooks = new ArrayList<>();

        for (ILoan loan : activeLoans) {
            IBook book = bookController.getListBookMap().get(loan.getLoanBookId());
            if (book != null && !borrowedBooks.contains(book))
                borrowedBooks.add(book);
        }

        List<TableColumn<IBook, ?>> columns = new ArrayList<>();
        columns.add(uiUtilsController.createColumn("book_title", IBook::getBookTitle));

        columns.add(uiUtilsController.createColumn("loan_date", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId())
                    return loan.getLoanDate();
            }
            return "N/A";
        }));

        columns.add(uiUtilsController.createColumn("return_date", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId())
                    return loan.getReturnDate();
            }
            return "N/A";
        }));

        columns.add(uiUtilsController.createColumn("user_name", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId())
                    return userController.getUserName(loan.getLoanUserId());
            }
            return "N/A";
        }));

        TableView<IBook> borrowedTableView = uiUtilsController.createTableView(borrowedBooks, columns, "No books on loan at this time.");

        TextField searchField = new TextField();
        uiUtilsController.createSearchField(searchField, "🔍 Search by book title or loan date");
    
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<IBook> filteredBooks = uiUtilsController.filterBooksOnReports(newValue, borrowedBooks, activeLoans);
            borrowedTableView.setItems(filteredBooks);
        });

        GridPane gridPaneForBorrowedBook = uiUtilsController.createGridPane();
        gridPaneForBorrowedBook.add(titleLabel, 0, 0, 2, 1);
        gridPaneForBorrowedBook.add(searchField, 0, 1);
        gridPaneForBorrowedBook.add(borrowedTableView, 0, 2);

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForBorrowedBook);
    }
         
    public void showOverdueLoanBookScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Books with fines at the moment");
    
        List<ILoan> activeLoans = loanController.getActiveLoans();
        List<IBook> overdueBooks = new ArrayList<>();
        
        for (ILoan loan : activeLoans) {
            IBook book = bookController.getListBookMap().get(loan.getLoanBookId());
            if (book != null && !overdueBooks.contains(book)) {
                LocalDate loanDate = LocalDate.parse(loan.getLoanDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                LocalDate dueDate = loanDate.plusDays(14);
                LocalDate currentDate = LocalDate.now();
    
                if (currentDate.isAfter(dueDate))
                    overdueBooks.add(book);
            }
        }
    
        List<TableColumn<IBook, ?>> columns = new ArrayList<>();
        columns.add(uiUtilsController.createColumn("book_title", IBook::getBookTitle));

        columns.add(uiUtilsController.createColumn("loan_date", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId())
                    return loan.getLoanDate(); 
            }
            return "N/A"; 
        }));
    
        columns.add(uiUtilsController.createColumn("return_date", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId())
                    return loan.getReturnDate();
            }
            return "N/A";
        }));
    
        columns.add(uiUtilsController.createColumn("user_name", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId())
                    return userController.getUserName(loan.getLoanUserId()); 
            }
            return "N/A";
        }));
    
        columns.add(uiUtilsController.createColumn("late_fee", book -> {
            for (ILoan loan : activeLoans) {
                if (loan.getLoanBookId() == book.getBookId()) {
                    LocalDate loanDate = LocalDate.parse(loan.getLoanDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate dueDate = loanDate.plusDays(14);
                    LocalDate currentDate = LocalDate.now();
    
                    if (currentDate.isAfter(dueDate)) {
                        ISystemUtils systemUtilsController = ICore.getInstance().getSystemUtilsController();
                        long daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, currentDate);
                        return String.format("R$ %.2f", systemUtilsController.calculateFine(daysLate));
                    } else
                        return "R$ 0.00";
                }
            }
            return "R$ 0.00";
        }));
    
        TableView<IBook> overdueBooksTableView = uiUtilsController.createTableView(overdueBooks, columns, "No books with late fines at this time.");

        TextField searchField = new TextField();
        uiUtilsController.createSearchField(searchField, "🔍 Search by book title or loan date");
    
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<IBook> filteredBooks = uiUtilsController.filterBooksOnReports(newValue, overdueBooks, activeLoans);
            overdueBooksTableView.setItems(filteredBooks);
        });

        GridPane gridPaneForOverdueLoanBook = uiUtilsController.createGridPane();
        gridPaneForOverdueLoanBook.add(titleLabel, 0, 0, 2, 1);
        gridPaneForOverdueLoanBook.add(searchField, 0, 1);
        gridPaneForOverdueLoanBook.add(overdueBooksTableView, 0, 2);
    
        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForOverdueLoanBook);
    }    
    
    public void showReportManagementScreen(Stage stage) {
        IUIController uiController = ICore.getInstance().getUIController();
        List<MenuItem> reportItems = uiController.createMenuItem("Reports", "Borrowed books", "Overdue loan books");

        reportItems.get(0).setOnAction(e -> showBorrowedBookScreen(stage));
        reportItems.get(1).setOnAction(e -> showOverdueLoanBookScreen(stage));
    }
    
}