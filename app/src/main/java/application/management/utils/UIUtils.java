package application.management.utils;

import javafx.animation.FadeTransition;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.control.Control;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import application.management.interfaces.IBook;
import application.management.interfaces.ICore;
import application.management.interfaces.ILoan;
import application.management.interfaces.ISystemUtils;
import application.management.interfaces.IUIUtils;
import application.management.interfaces.IUser;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import javafx.scene.control.TableColumn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UIUtils implements IUIUtils {

    @Override
    public GridPane createGridPane() {
        ColumnConstraints columnAlignment  = new ColumnConstraints();
        columnAlignment.setHalignment(HPos.RIGHT);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.getColumnConstraints().add(columnAlignment);
        gridPane.setPadding(new Insets(50, 0, 0, 100));
        return gridPane;
    }

    @Override
    public void cleanManagementScreen(VBox vBox) {
        vBox.getChildren().removeIf(node -> 
            node instanceof Label || node instanceof TextField ||
            node instanceof Button || node instanceof GridPane ||
            node instanceof TableView
        );
    }

    @Override
    public Label createTitleLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font(20));
        label.setAlignment(Pos.CENTER_LEFT);
        label.setMaxWidth(Double.MAX_VALUE);
        return label;
    }

    @Override
    public void showFeedbackLabel(Label feedbackLabel, String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setOpacity(1.0);
        feedbackLabel.setAlignment(Pos.CENTER_LEFT);
        feedbackLabel.setMaxWidth(Double.MAX_VALUE);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(10), feedbackLabel);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.setCycleCount(1);
        fadeTransition.play();
    }

    @Override
    public ObservableList<IBook> filterBooksOnReports(String searchTerm, List<IBook> books, List<ILoan> loans) {
        ObservableList<IBook> filteredBooks = FXCollections.observableArrayList();
        searchTerm = searchTerm.toLowerCase();

        for (IBook book : books) {
            String bookTitle = book.getBookTitle().toLowerCase();
            String loanDate = "N/A";
            
            for (ILoan loan : loans) {
                if (loan.getLoanBookId() == book.getBookId()) {
                    loanDate = loan.getLoanDate();
                    break;
                }
            }

            if (bookTitle.contains(searchTerm) || loanDate.contains(searchTerm))
                filteredBooks.add(book);
        }
        return filteredBooks;
    }

    @Override
    public ObservableList<IBook> searchOnListBooks(String searchTerm, List<IBook> booksList) {
        ObservableList<IBook> filteredBooks = FXCollections.observableArrayList();
    
        String lowerCaseSearchTerm = searchTerm.toLowerCase();
    
        for (IBook book : booksList) {
            String bookTitle = book.getBookTitle().toLowerCase();
            String author = book.getBookAuthor().toLowerCase();
            String publicationDate = book.getBookPublishedDate().toLowerCase();
    
            if (bookTitle.contains(lowerCaseSearchTerm) || author.contains(lowerCaseSearchTerm) || publicationDate.contains(lowerCaseSearchTerm))
                filteredBooks.add(book);
        }
    
        return filteredBooks;
    }

    @Override
    public ObservableList<IUser> searchOnListUsers(String searchTerm, List<IUser> users) {
        ObservableList<IUser> filteredUsers = FXCollections.observableArrayList();
        searchTerm = searchTerm.toLowerCase();
    
        for (IUser user : users) {
            String userName = user.getUserName().toLowerCase();
            String userId = String.valueOf(user.getUserId());
    
            if (userName.contains(searchTerm) || userId.contains(searchTerm))
                filteredUsers.add(user);
        }
    
        return filteredUsers;
    }

    @Override
    public <T> void filterComboBoxItemsOnKeyReleased(ComboBox<T> comboBox, List<T> items) {
        comboBox.setEditable(true);
        comboBox.setPrefWidth(250);
        comboBox.setOnKeyReleased(event -> {
            String input = comboBox.getEditor().getText().toLowerCase();
            if (!input.isEmpty()) {
                List<T> filteredItems = items.stream()
                    .filter(item -> item.toString().toLowerCase().contains(input))
                    .collect(Collectors.toList());
                comboBox.getItems().setAll(filteredItems);
            } else
                comboBox.getItems().setAll(items);
        });
    }

    @Override
    public <T> TableView<T> createTableView(List<T> items, List<TableColumn<T, ?>> columns, String placeholderText) {
        TableView<T> tableView = new TableView<>();
        tableView.setStyle("-fx-border-color: gray; -fx-focus-color: gray; -fx-selection-bar: #d3d3d3; -fx-background-color: white; -fx-control-inner-background-alt: -fx-control-inner-background;");
        tableView.setPlaceholder(new Label(placeholderText));
        tableView.setFixedCellSize(30);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(6));
        tableView.setPrefWidth(700);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().addAll(columns);
        tableView.getItems().addAll(items);
        columns.forEach(column -> column.setStyle("-fx-alignment: CENTER;"));

        return tableView;
    }

    @Override
    public <T, U> TableColumn<T, U> createColumn(String title, Function<T, U> valueExtractor) {
        TableColumn<T, U> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(valueExtractor.apply(cellData.getValue())));
        return column;
    }

    @Override
    public void configureDatePickerToPreventFutureDates(DatePicker datePicker) {
        datePicker.setPrefWidth(250);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #d3d3d3;");
                }
            }
        });
    
        TextField datePickerEditor = datePicker.getEditor();
        datePickerEditor.setOnKeyReleased(event -> {
            String input = datePickerEditor.getText().trim();
            try {
                if (!input.isEmpty()) {
                    LocalDate parsedDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    
                    if (parsedDate.isAfter(LocalDate.now())) { 
                        datePickerEditor.setStyle("-fx-border-color: red;");
                        datePicker.setValue(null);
                    } else {
                        datePicker.setValue(parsedDate);
                        datePickerEditor.setStyle("");
                    }
                }
            } catch (DateTimeParseException e) {
                datePickerEditor.setStyle("-fx-border-color: red;");
                datePicker.setValue(null);
            }
        });
    }
    
    @Override
    public void addTooltip(Control control, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setShowDelay(Duration.seconds(1));
        tooltip.setAutoHide(true);
        control.setTooltip(tooltip);
    }

    @Override
    public ListView<String> createBookListView(TextField searchField, boolean isLoan) {
        ListView<String> bookListView = new ListView<>();
        bookListView.setPrefHeight(95);
        bookListView.setPrefWidth(500);
        bookListView.setStyle("-fx-border-color: gray; -fx-focus-color: gray; -fx-selection-bar: #d3d3d3; -fx-background-color: white; -fx-control-inner-background-alt: -fx-control-inner-background;");
        bookListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    
        Label placeholderLabel = new Label("No books available.");
        placeholderLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: gray;");
        bookListView.setPlaceholder(placeholderLabel);
        
        IBook bookController = ICore.getInstance().getBookController();
        ObservableList<String> bookTitles = FXCollections.observableArrayList();

        if (isLoan) {
            for (IBook book : bookController.getListBookMap().values()) {
                if (!book.isBorrowed())
                    bookTitles.add(book.getBookTitle());
            }
        } else {
            for (IBook book : bookController.getListBookMap().values()) {
                if (book.isBorrowed())
                    bookTitles.add(book.getBookTitle());
            }
        }
        bookListView.setItems(bookTitles);
    
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<String> filteredBooks = FXCollections.observableArrayList();
            for (IBook book : bookController.getListBookMap().values()) {
                if (isLoan) {
                    if (!book.isBorrowed() && book.getBookTitle().toLowerCase().contains(newValue.toLowerCase()))
                        filteredBooks.add(book.getBookTitle());
                } else {
                    if (book.isBorrowed() && book.getBookTitle().toLowerCase().contains(newValue.toLowerCase()))
                        filteredBooks.add(book.getBookTitle());
                }
            }
            
            bookListView.setItems(filteredBooks);
        });
    
        return bookListView;
    }    

    @Override
    public boolean validateTransactionsData(String userIdText, ObservableList<String> selectedBooks, LocalDate dateValue, Label feedbackLabel) {
        IUser userController = ICore.getInstance().getUserController();
        ISystemUtils systemUtilsController = ICore.getInstance().getSystemUtilsController();
    
        if (userIdText.isEmpty() || selectedBooks.isEmpty() || dateValue == null) {
            showFeedbackLabel(feedbackLabel, "Fill in all fields correctly.");
            return false;
        }
        int userId = Integer.parseInt(userIdText);
        if (!systemUtilsController.isValidUserId(userId)) {
            showFeedbackLabel(feedbackLabel, "There is no user with a negative Id.");
            return false;
        }
        if (!systemUtilsController.isUserRegistered(userId, userController)) {
            showFeedbackLabel(feedbackLabel, "The user is not registered in the system.");
            return false;
        }
    
        return true;
    }

    @Override
    public DatePicker createDatePicker() {
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(150);
        addTooltip(datePicker, "Future dates and invalid 'dd/MM/yyyy' format will not be accepted.");
        configureDatePickerToPreventFutureDates(datePicker);
        return datePicker;
    }

    @Override
    public void createSearchField(TextField searchField, String message) {
        searchField.setFocusTraversable(false);
        searchField.setPromptText(message);
        searchField.setMaxWidth(400);
    }
    
}