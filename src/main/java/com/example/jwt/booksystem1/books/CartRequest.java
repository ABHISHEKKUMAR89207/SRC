package com.example.jwt.booksystem1.books;

import java.util.List;

public class CartRequest {

    private List<String> cart;

    public List<String> getCart() {
        return cart;
    }

    public void setCart(List<String> cart) {
        this.cart = cart;
    }
}

