package my.example.jerseyspring.transaction.impl;

import my.example.jerseyspring.transaction.TransactionBo;

public class TransactionBoMock implements TransactionBo {
    public String save() {
        return "This is mock.";
    }
}