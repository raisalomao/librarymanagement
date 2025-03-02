package application.management.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public interface IUIUtils {

    public abstract GridPane createGridPane();
    public abstract void cleanManagementScreen(VBox vBox);
    public abstract Label createTitleLabel(String text);
    public abstract <T> void filterComboBoxItemsOnKeyReleased(ComboBox<T> comboBox, List<T> items);
    public abstract <T> TableView<T> createTableView(List<T> items, List<TableColumn<T, ?>> columns, String placeholderText);
    public abstract <T, U> TableColumn<T, U> createColumn(String title, Function<T, U> valueExtractor);
    public abstract void configureDatePickerToPreventFutureDates(DatePicker datePicker);
    public abstract void showFeedbackLabel(Label feedbackLabel, String message);
    public abstract void addTooltip(Control control, String message);
    public abstract ListView<String> createBookListView(TextField searchField, boolean isLoan);
    public abstract DatePicker createDatePicker();
    public abstract ObservableList<IBook> searchOnListBooks(String searchTerm, List<IBook> booksList);
    public abstract ObservableList<IUser> searchOnListUsers(String searchTerm, List<IUser> users);
    public abstract ObservableList<IBook> filterBooksOnReports(String searchTerm, List<IBook> books, List<ILoan> loans);
    public abstract boolean validateTransactionsData(String userIdText, ObservableList<String> selectedBooks, LocalDate dateValue, Label feedbackLabel);
    public abstract void createSearchField(TextField searchField, String message);
    
}