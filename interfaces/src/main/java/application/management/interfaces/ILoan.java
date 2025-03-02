package application.management.interfaces;

import java.util.ArrayList;
import java.util.List;

public interface ILoan {

    public abstract int getLoanBookId();
    public abstract int getLoanUserId();
    public abstract String getLoanDate();
    public abstract String getReturnDate();
    public abstract void setLoanDate(String loanDate);
    public abstract void setReturnDate(String returnDate);
    public abstract ILoan createLoan(int bookId, int userId, String loanDate, String returnDate);
    public abstract void addLoanToHistory(int userId, ILoan loan);
    public abstract ArrayList<ILoan> getLoanHistory(int userId);
    public abstract void updateLoanHistory(int userId, ILoan updatedLoan);
    public abstract List<ILoan> getActiveLoans();

}
