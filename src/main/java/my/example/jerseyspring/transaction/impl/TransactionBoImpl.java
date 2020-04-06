package my.example.jerseyspring.transaction.impl;

import my.example.jerseyspring.transaction.TransactionBo;

public class TransactionBoImpl implements TransactionBo {
    public String save() {
        return "Jersey + Spring example";
    }
}