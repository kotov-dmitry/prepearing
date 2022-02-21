package moneytransfer.impl;

import moneytransfer.MoneyTransfer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public class MoneyTransferImpl implements MoneyTransfer {

    private final ConcurrentHashMap<String, Account> accountBuNumber = new ConcurrentHashMap<>();

    @Override
    public boolean createAccount(String number, Long value) {
        Account account = new Account();
        account.value = value;
        if (accountBuNumber.putIfAbsent(number, account) != null) {
            throw new RuntimeException("Exist");
        }
        return true;
    }

    @Override
    public boolean transfer(String fromNumber, String toNumber, Long value) {
        Account from = accountBuNumber.get(fromNumber);
        Account to = accountBuNumber.get(toNumber);
        if (from == null) {
            throw new RuntimeException("fromAccount not exist");
        }
        if (to == null) {
            throw new RuntimeException("toAccount not exist");
        }
        synchronized (from) {
            if (from.value < value) {
                throw new RuntimeException("not enough money");
            }
            from.value = from.value - value;
        }

        synchronized (to) {
            to.value = to.value + value;
        }

        return true;
    }

    private static class Account {
        private Long value;
    }
}
