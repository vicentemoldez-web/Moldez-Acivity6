public class Transaction {
    public String transactionId;
    public String userId;
    public String bookId;
    public String dateBorrowed;
    public String dateReturned;

    public Transaction(String transactionId, String userId, String bookId, String dateBorrowed, String dateReturned) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.bookId = bookId;
        this.dateBorrowed = dateBorrowed;
        this.dateReturned = dateReturned;
    }

    public void displayTransaction() {
        System.out.println(transactionId + " | " + userId + " | " + bookId + " | " + dateBorrowed + " | " + dateReturned);
    }

    public String getUserId() { return userId; }
    public String getBookId() { return bookId; }
}