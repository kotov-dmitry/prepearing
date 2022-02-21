package moneytransfer;

import moneytransfer.impl.MoneyTransferImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public class MoneyTransferTest {
    private MoneyTransfer moneyTransfer;

    @BeforeEach
    public void setUp() {
        moneyTransfer = new MoneyTransferImpl();
    }

    @Test
    public void testCreateAccountSuccessfully() {
        Assertions.assertTrue(moneyTransfer.createAccount("account", 10L));
    }

    @Test
    public void testCreateExistingAccount() {
        moneyTransfer.createAccount("account", 10L);
        try {
            moneyTransfer.createAccount("account", 10L);
        } catch (RuntimeException e) {
            Assertions.assertEquals("Exist", e.getMessage());
        }
    }

    @Test
    public void testCorrectTransfer() {
        String fromAccount = "account";
        String toAccount = "account1";
        moneyTransfer.createAccount(fromAccount, 10L);
        moneyTransfer.createAccount(toAccount, 10L);
        Assertions.assertTrue(moneyTransfer.transfer(fromAccount, toAccount, 5L));
    }

    @Test
    public void testFromAccountNotExist() {
        String fromAccount = "account";
        String toAccount = "account1";
        moneyTransfer.createAccount(toAccount, 10L);
        try {
            moneyTransfer.transfer(fromAccount, toAccount, 5L);
        } catch (RuntimeException e) {
            Assertions.assertEquals("fromAccount not exist", e.getMessage());
        }
    }

    @Test
    public void testToAccountNotExist() {
        String fromAccount = "account";
        String toAccount = "account1";
        moneyTransfer.createAccount(fromAccount, 10L);
        try {
            moneyTransfer.transfer(fromAccount, toAccount, 5L);
        } catch (RuntimeException e) {
            Assertions.assertEquals("toAccount not exist", e.getMessage());
        }
    }

    @Test
    public void testNotEnoughMoney() {
        String fromAccount = "account";
        String toAccount = "account1";
        moneyTransfer.createAccount(fromAccount, 10L);
        moneyTransfer.createAccount(toAccount, 10L);
        try {
            moneyTransfer.transfer(fromAccount, toAccount, 11L);
        } catch (RuntimeException e) {
            Assertions.assertEquals("not enough money", e.getMessage());
        }
    }
}
