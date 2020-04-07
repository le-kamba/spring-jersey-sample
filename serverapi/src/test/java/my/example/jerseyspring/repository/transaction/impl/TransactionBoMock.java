package my.example.jerseyspring.repository.transaction.impl;

import my.example.jerseyspring.repository.transaction.TransactionBo;

public class TransactionBoMock implements TransactionBo {
    public String save() {
        return "This is mock.";
    }
}