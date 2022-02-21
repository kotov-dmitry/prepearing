package moneytransfer;

/**
 * @author dkotov
 * @since 21.02.2022
 */
public interface MoneyTransfer {
    boolean createAccount(String number, Long value);

    boolean transfer(String fromNumber, String toNumber, Long value);
}
