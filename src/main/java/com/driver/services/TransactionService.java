package com.driver.services;

import com.driver.models.*;
import com.driver.repositories.BookRepository;
import com.driver.repositories.CardRepository;
import com.driver.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    @Autowired
    BookRepository bookRepository5;

    @Autowired
    CardRepository cardRepository5;

    @Autowired
    TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    public int max_allowed_books;

    @Value("${books.max_allowed_days}")
    public int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    public int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception {
        //check whether bookId and cardId already exist

        //conditions required for successful transaction of issue book:
        //1. book is present and available

        // If it fails: throw new Exception("Book is either unavailable or not present");

        //2. card is present and activated

        // If it fails: throw new Exception("Card is invalid");
        //3. number of books issued against the card is strictly less than max_allowed_books

        // If it fails: throw new Exception("Book limit has reached for this card");
        //If the transaction is successful, save the transaction to the list of transactions and return the id

        //Note that the error message should match exactly in all cases
        Book book;
        Card card;
        book = bookRepository5.findById(bookId).get();
        card = cardRepository5.findById(cardId).get();

        if(card == null || card.getCardStatus().equals(CardStatus.DEACTIVATED)){
            throw new Exception("Card is invalid");
        }
        if(card.getBooks().size() >= max_allowed_books){
            throw new Exception("Book limit has reached for this card");
        }
        if(book==null || !book.isAvailable()){
            throw new Exception("Book is either unavailable or not present");
        }

        else {
            book.setCard(card);
            card.getBooks().add(book);

            Transaction transaction = Transaction.builder().
                    book(book).card(card)
                    .transactionId(String.valueOf(UUID.randomUUID()))
                    .isIssueOperation(true)
                    .fineAmount(0).transactionStatus(TransactionStatus.SUCCESSFUL).build();

            book.setAvailable(false);
            if(book.getTransactions() == null){
                ArrayList<Transaction>TList=new ArrayList<>();
                TList.add(transaction);
                book.setTransactions(TList);
            }
            else {
                book.getTransactions().add(transaction);
            }
            transactionRepository5.save(transaction);

            return transaction.toString();
        }
         //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size() - 1);

        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        //  Date before = transaction.getTransactionDate();
        // Date before = (Date) transaction.getTransactionDate();
        Date presentDate = transaction.getTransactionDate();


        Date currDate = new Date();


        long timeDifference = currDate.getTime() - presentDate.getTime();

        long DifferenceinDays = (timeDifference / (1000 * 60 * 60 * 24)) % 365 ;

        int fine =0;

        if(DifferenceinDays > getMax_allowed_days) {

            int fineDays = (int) (getMax_allowed_days - DifferenceinDays);
            fine = -1 * fineDays * fine_per_day;
        }

        //make the book available for other users
        Book b = transaction.getBook();
        b.setAvailable(true);

        //make a new transaction for return book which contains the fine amount as well
        Transaction newTransaction = Transaction.builder().transactionId(UUID.randomUUID().toString())
                .book(transaction.getBook())
                .card(transaction.getCard())
                .fineAmount(fine)
                .transactionStatus(TransactionStatus.SUCCESSFUL)
                .isIssueOperation(false)
                .build();

        transactionRepository5.save(newTransaction);


        b.setCard(null);
        bookRepository5.save(b);

        return newTransaction; //





//        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
//        Transaction transaction = transactions.get(transactions.size() - 1);
//
//
//        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
//
//
//
//
//        //make the book available for other users
//        //make a new transaction for return book which contains the fine amount as well
//
//
//        Transaction newTransaction = Transaction.builder().transactionId(UUID.randomUUID().toString())
//                .book(transaction.getBook())
//                .card(transaction.getCard())
//                .fineAmount(0).transactionStatus(TransactionStatus.SUCCESSFUL)
//                .isIssueOperation(false).build();
//
//        transactionRepository5.save(transaction);
//
//        Book book = transaction.getBook();
//        book.setAvailable(true);
//        book.setCard(null);
//        bookRepository5.save(book);
//
//       // Transaction returnBookTransaction  = null;
//        return newTransaction; //return the transaction after updating all details
    }
}
