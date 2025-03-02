package application.management.shell;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import application.management.interfaces.ILoan;

public class Loan implements ILoan, Serializable {

    private static final long serialVersionUID = 1L;

    private int bookId;
    private int userId;
    private String loanDate;
    private String returnDate;
    private static HashMap<Integer, ArrayList<ILoan>> loanHistory = new HashMap<>();

    public Loan() {}

    public Loan(int bookId, int userId, String loanDate, String returnDate) {
        this.bookId = bookId;
        this.userId = userId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    @Override
    public int getLoanBookId() {
        return bookId;
    }

    @Override
    public int getLoanUserId() {
        return userId;
    }

    @Override
    public String getLoanDate() {
        return loanDate;
    }

    public static HashMap<Integer, ArrayList<ILoan>> getLoanHistory() {
        return loanHistory;
    }

    @Override
    public String getReturnDate() {
        if ("N/A".equals(returnDate) || returnDate == null) {
            return returnDate;
        } else {
            try {
                LocalDate parsedDate = LocalDate.parse(returnDate, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                return parsedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (DateTimeParseException e) {
                System.out.println("Error on format: " + returnDate);
                return "Invalid Date";
            }
        }
    }

    @Override
    public ArrayList<ILoan> getLoanHistory(int userId) {
        return new ArrayList<>(loanHistory.getOrDefault(userId, new ArrayList<>())); 
    }

    @Override
    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    @Override
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public ILoan createLoan(int bookId, int userId, String loanDate, String returnDate) {
        return new Loan(bookId, userId, loanDate, returnDate);
    }

    @Override
    public void addLoanToHistory(int userId, ILoan loan) {
        if (loanHistory.containsKey(userId)) {
            List<ILoan> userLoans = loanHistory.get(userId);
            if (!userLoans.stream().anyMatch(l -> l.getLoanBookId() == loan.getLoanBookId() && "N/A".equals(l.getReturnDate()))) {
                loanHistory.get(userId).add(loan);
            }
        } else {
            loanHistory.put(userId, new ArrayList<>(List.of(loan)));
        }
    }

    @Override
    public void updateLoanHistory(int userId, ILoan updatedLoan) {
        List<ILoan> loans = loanHistory.get(userId);
        if (loans != null) {
            for (int i = 0; i < loans.size(); i++) {
                ILoan loan = loans.get(i);
                if (loan.getLoanBookId() == updatedLoan.getLoanBookId() && "N/A".equals(loan.getReturnDate())) {
                    loans.set(i, updatedLoan);
                    break;
                }
            }
        }
    }

    @Override
    public List<ILoan> getActiveLoans() {
        List<ILoan> activeLoans = new ArrayList<>();
        for (ArrayList<ILoan> loans : loanHistory.values()) {
            for (ILoan loan : loans) {
                if ("N/A".equals(loan.getReturnDate()) || loan.getReturnDate() == null) {
                    activeLoans.add(loan);
                }
            }
        }
        return activeLoans;
    }

}