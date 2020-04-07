package my.example.jerseyspring.repository.transaction.impl;


import my.example.jerseyspring.repository.transaction.TransactionBo;

public class TransactionBoImpl implements TransactionBo {
    public String save() {
        return "Jersey + Spring example";
    }
}