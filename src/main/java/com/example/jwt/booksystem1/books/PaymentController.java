package com.example.jwt.booksystem1.books;



public class PaymentController {

    private String paymentId;
    private int amount;  // Assuming the amount is an integer, adjust as needed
    public PaymentController() {
        // Default constructor
    }

    public PaymentController(String paymentId, int amount) {
        this.paymentId = paymentId;
        this.amount = amount;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
