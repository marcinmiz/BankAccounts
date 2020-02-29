package comp.company;

import java.util.Objects;
import java.util.Random;

public class Account implements Comparable {
    private Bank bank;
    private String accountNumber;
    private Customer owner;
    private int value;
    private static int id = 0;
    public static final int accountLenght = 26;
    private Random random = new Random();

    public Account(Customer owner, Bank bank) {
        this.bank = bank;
        this.owner = owner;
        this.owner.addCustomerAccount(this);
        id++;
        value = 0;
        this.accountNumber = String.valueOf(id);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < accountLenght - accountNumber.length() - 2; i++) {
            int cipher = random.nextInt(10);
            stringBuilder.append(cipher);
        }
        stringBuilder.append(accountNumber);
        for (int i = 0; i < 30; i += 5) {
            stringBuilder.insert(i, " ");
        }
        stringBuilder.insert(0, random.nextInt(10));
        stringBuilder.insert(1, random.nextInt(10));
        accountNumber = stringBuilder.toString();
        bank.addBankAccount(this);
    }

    public Bank getBank() {
        return bank;
    }

    int getValue() {
        return value;
    }

    void setValue(int value, Bank bank, Account bankAccount) {
        if (bank.getBankAccountsList().contains(bankAccount)) {
            this.value = value;
        }
    }

    String getAccountNumber() {
        return accountNumber;
    }

    Customer getOwner() {
        return owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getValue() == account.getValue() &&
                getAccountNumber().equals(account.getAccountNumber()) &&
                getOwner().equals(account.getOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountNumber(), getOwner(), getValue());
    }

    @Override
    public String toString() {
        return this.getAccountNumber() + ": " + this.getValue() + ", " + this.getOwner().getName();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return 1;
        Account account = (Account) o;
        return (int) Math.signum(this.getValue() - account.getValue());
    }
}
