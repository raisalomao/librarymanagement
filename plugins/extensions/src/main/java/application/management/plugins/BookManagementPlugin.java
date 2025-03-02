package application.management.plugins;

import application.management.interfaces.IPlugin;
import application.management.interfaces.ISystemUtils;
import application.management.interfaces.ICore;
import application.management.interfaces.IUIController;
import application.management.interfaces.IBook;
import application.management.interfaces.IUIUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.MenuItem;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class BookManagementPlugin implements IPlugin {

    private static final IBook bookController = ICore.getInstance().getBookController();
    private static final IUIUtils uiUtilsController = ICore.getInstance().getUIUtilsController();

    @Override
    public boolean init() {
        return init(null);
    }

    @Override
    public boolean init(Stage stage) {
        showBookManagementScreen(stage);
        return true;
    }

    public void showListBookScreen(Stage stage) {
        Label bookListingLabel = uiUtilsController.createTitleLabel("List of books");

        List<IBook> bookList = new ArrayList<>(bookController.getListBookMap().values());
        List<TableColumn<IBook, ?>> columns = new ArrayList<>();
        columns.add(uiUtilsController.createColumn("id", IBook::getBookId));
        columns.add(uiUtilsController.createColumn("book_title", IBook::getBookTitle));
        columns.add(uiUtilsController.createColumn("author", IBook::getBookAuthor));
        columns.add(uiUtilsController.createColumn("pub_date", IBook::getBookPublishedDate));
        columns.add(uiUtilsController.createColumn("genre", IBook::getBookGenre));
        columns.add(uiUtilsController.createColumn("quantity", IBook::getQuantityInTheLibrary));
        columns.add(uiUtilsController.createColumn("status", book -> book.isBorrowed() ? "borrowed" : "not borrowed"));
        TableView<IBook> bookListingTable = uiUtilsController.createTableView(bookList, columns, "No books available.");
    
        TextField searchField = new TextField();
        uiUtilsController.createSearchField(searchField, "🔍 Search by book_title, author, or pub_date");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<IBook> filteredBooks = uiUtilsController.searchOnListBooks(newValue, bookList);
            bookListingTable.setItems(filteredBooks);
        });

        GridPane gridPaneForBookDisplayScreen = uiUtilsController.createGridPane();
        gridPaneForBookDisplayScreen.add(bookListingLabel, 0, 0, 2, 1);
        gridPaneForBookDisplayScreen.add(searchField, 0, 1, 3, 1);
        gridPaneForBookDisplayScreen.add(bookListingTable, 0, 2, 3, 1);

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForBookDisplayScreen);
    }

    public void showBookAddScreen(Stage stage) {
        Label bookAddingLabel = uiUtilsController.createTitleLabel("Add a book");

        String[] bookFieldLabels = {"Enter the ISBN:", "Enter the title:", "Enter the author:", "Enter the quantity:"};
        TextField[] bookInputFields = new TextField[bookFieldLabels.length];
        for (int i = 0; i < bookInputFields.length; i++)
            bookInputFields[i] = new TextField();
        bookInputFields[1].setPrefWidth(250);
        uiUtilsController.addTooltip(bookInputFields[0], "Do not use hyphens, only numbers.");

        Label genreLabel = new Label("Enter the genre:");
        ComboBox<String> genreComboBox = new ComboBox<>();
        
        ISystemUtils systemUtilsController = ICore.getInstance().getSystemUtilsController();
        List<String> listOfGenres = systemUtilsController.loadTextFile("plugins\\extensions\\src\\resources\\genrelist.txt");
        genreComboBox.getItems().addAll(listOfGenres);
        genreComboBox.setPromptText("Select the genre");
        uiUtilsController.filterComboBoxItemsOnKeyReleased(genreComboBox, listOfGenres);

        DatePicker publishedDatePicker = uiUtilsController.createDatePicker();

        Button addBookButton = new Button("Add");
        addBookButton.setMaxWidth(250);
        Label addBookFeedbackLabel = new Label();

        addBookButton.setOnAction(event -> {
            String isbn = bookInputFields[0].getText().trim();
            String title = bookInputFields[1].getText().trim();
            String author = bookInputFields[2].getText().trim();
            LocalDate publishedDate = publishedDatePicker.getValue();
            String genre = genreComboBox.getValue();
            String quantityText = bookInputFields[3].getText().trim();
        
            try {
                if (!title.isEmpty() && genre != null && !isbn.isEmpty() && publishedDate != null && !quantityText.isEmpty()) {
                    boolean added = bookController.addBookToLibrary(title, Long.parseLong(isbn), author, publishedDate.toString(), genre, Integer.parseInt(quantityText));
                    uiUtilsController.showFeedbackLabel(addBookFeedbackLabel, added ? "Done! The book was added: " + title + "." : "Oops! Register a book with a different ISBN.");
                    for (TextField field : bookInputFields) 
                        field.clear();
                    genreComboBox.setValue(null);
                    publishedDatePicker.setValue(null);
                } else                             
                    uiUtilsController.showFeedbackLabel(addBookFeedbackLabel, "Fill in all fields.");
            } catch (NumberFormatException e) {
                uiUtilsController.showFeedbackLabel(addBookFeedbackLabel, "The Id and ISBN entry must be a number.");
            }
        });
        bookInputFields[3].setOnAction(event -> addBookButton.fire());
        
        GridPane gridPaneForAddBookScreen = uiUtilsController.createGridPane();
        for (int i = 0; i < bookInputFields.length; i++) {
            gridPaneForAddBookScreen.add(new Label(bookFieldLabels[i]), 0, i + 1);
            gridPaneForAddBookScreen.add(bookInputFields[i], 1, i + 1);
        }

        gridPaneForAddBookScreen.add(new Label("Enter the published date:"), 0, bookInputFields.length + 1);
        gridPaneForAddBookScreen.add(publishedDatePicker, 1, bookInputFields.length + 1);
        gridPaneForAddBookScreen.add(bookAddingLabel, 0, 0, 2, 1);
        gridPaneForAddBookScreen.add(genreLabel, 0, bookInputFields.length + 2);
        gridPaneForAddBookScreen.add(genreComboBox, 1, bookInputFields.length + 2);
        gridPaneForAddBookScreen.add(addBookButton, 1, bookInputFields.length + 3);
        gridPaneForAddBookScreen.add(addBookFeedbackLabel, 0, bookInputFields.length + 5, 2, 2); 

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForAddBookScreen);
    }

    public void showRemoveBookScreen(Stage stage) {
        Label titleLabel = uiUtilsController.createTitleLabel("Remove a book");
        Label bookIdLabel = new Label("Enter book ID:");
        TextField bookIdField  = new TextField();
        Button deleteBookButton = new Button("Remove");
        Label removeBookFeedbackLabel = new Label();

        deleteBookButton.setOnAction(event -> {
            String idText = bookIdField.getText().trim();
            if (!idText.isEmpty()) {
                try {
                    if (bookController.removeBookFromLibrary(Integer.parseInt(idText)))
                        uiUtilsController.showFeedbackLabel(removeBookFeedbackLabel, "Done! The Book with Id " + idText + " was deleted.");
                    else
                        uiUtilsController.showFeedbackLabel(removeBookFeedbackLabel, "This book is not registered in the system.");
                    bookIdField.clear();
                } catch (NumberFormatException e) {
                    uiUtilsController.showFeedbackLabel(removeBookFeedbackLabel, "You must pass only one number");
                }
            } else
                uiUtilsController.showFeedbackLabel(removeBookFeedbackLabel, "You must enter an Id to remove a book.");
        });

        bookIdField.setOnAction(event -> deleteBookButton.fire());

        GridPane gridPaneForRemoveBookScreen = uiUtilsController.createGridPane();
        gridPaneForRemoveBookScreen.add(titleLabel, 0, 0, 2, 1);
        gridPaneForRemoveBookScreen.add(bookIdLabel, 0, 1);
        gridPaneForRemoveBookScreen.add(bookIdField, 1, 1);
        gridPaneForRemoveBookScreen.add(deleteBookButton, 2, 1);
        gridPaneForRemoveBookScreen.add(removeBookFeedbackLabel, 0, 3, 2, 1);

        VBox vBox = (VBox) stage.getScene().getRoot();
        uiUtilsController.cleanManagementScreen(vBox);
        vBox.getChildren().add(gridPaneForRemoveBookScreen);
    }

    public void showBookManagementScreen(Stage stage) {
        IUIController uiController = ICore.getInstance().getUIController();
        List<MenuItem> bookItems = uiController.createMenuItem("Books", "List", "Add", "Remove");
        bookItems.get(0).setOnAction(e -> showListBookScreen(stage));
        bookItems.get(1).setOnAction(e -> showBookAddScreen(stage));
        bookItems.get(2).setOnAction(e -> showRemoveBookScreen(stage));
    }
    
}