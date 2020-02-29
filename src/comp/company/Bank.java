package comp.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bank implements Comparable {
    String name;
    private List<Account> bankAccountsList;

    public Bank(String name) {
        this.name = name;
        bankAccountsList = new ArrayList<>();
    }

    void deposit(String to, int amount) {
        Account toBankAccount = bankAccountsList.stream().filter(m -> m.getAccountNumber().equals(to)).findAny().orElse(null);
        if (toBankAccount != null) {
            synchronized (toBankAccount) {
                toBankAccount.setValue(toBankAccount.getValue() + amount, this, toBankAccount);
            }
        }
    }

    void transfer(String from, String to, int amount) {
        Account fromBankAccount = bankAccountsList.stream().filter(m -> m.getAccountNumber().equals(from)).findAny().orElse(null);
        Account toBankAccount = bankAccountsList.stream().filter(m -> m.getAccountNumber().equals(to)).findAny().orElse(null);
        String first = null, second = null;
        for (int i = 0; i < Account.accountLenght; i++) {
            Character character = from.charAt(i);
            if (character.equals(" ")) continue;
            if (from.charAt(i) < to.charAt(i)) {
                first = from;
                second = to;
            } else {
                first = to;
                second = from;
            }
        }

        synchronized (first) {
            synchronized (second) {
                if (fromBankAccount != null) {
                    if (fromBankAccount.getValue() >= amount) {
                        System.out.println("From Account number: " + fromBankAccount.getAccountNumber() + ", start amount: " + fromBankAccount.getValue() + ", owner: " + fromBankAccount.getOwner().getName());
                        fromBankAccount.setValue(fromBankAccount.getValue() - amount, this, toBankAccount);
                        System.out.println("From Account number: " + fromBankAccount.getAccountNumber() + ", end amount: " + fromBankAccount.getValue() + ", owner: " + fromBankAccount.getOwner().getName());
                        if (toBankAccount != null) {
                            System.out.println("To Account number: " + toBankAccount.getAccountNumber() + ", start amount: " + toBankAccount.getValue() + ", owner: " + toBankAccount.getOwner().getName());
                        }
                        toBankAccount.setValue(toBankAccount.getValue() + amount, this, toBankAccount);
                        System.out.println("To Account number: " + toBankAccount.getAccountNumber() + ", end amount: " + toBankAccount.getValue() + ", owner: " + toBankAccount.getOwner().getName());

                    } else {
                        System.err.println("You don't have money enough to transfer from your account");
                    }
                }

            }
        }
    }

    void addBankAccount(Account account) {
        bankAccountsList.add(account);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank)) return false;
        Bank bank = (Bank) o;
        return name.equals(bank.name) &&
                bankAccountsList.equals(bank.bankAccountsList);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public List<Account> getBankAccountsList() {
        return Collections.unmodifiableList(bankAccountsList);
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return 1;
        Bank other = (Bank) o;
        int banknamesComparison = (int) Math.signum(this.name.compareTo(other.name));
        if (banknamesComparison != 0) return banknamesComparison;
        int firstBankMoney = bankAccountsList.stream().mapToInt(Account::getValue).sum();
        int secondBankMoney = other.bankAccountsList.stream().mapToInt(Account::getValue).sum();
        int moneyBankComparison = (int) Math.signum(firstBankMoney - secondBankMoney);
        return moneyBankComparison;
    }
}
