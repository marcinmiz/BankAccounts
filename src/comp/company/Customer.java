package comp.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Customer implements Runnable, Comparable {
    private String name;
    private List<Account> accounts;
    private Random random = new Random();
    private int amount;
    private Account chosenAccount;
    private String to;

    public Customer(String name) {
        this.name = name;
        accounts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public void addCustomerAccount(Account account) {
        if ((account != null) && (account.getOwner().getName().equals(name))) {
            accounts.add(account);
        }
    }

    @Override
    public void run() {
        while (true) {
            amount = random.nextInt(10_000);
            System.out.println(amount);
            chosenAccount = accounts.get(random.nextInt(accounts.size()));
            int receiver = random.nextInt(chosenAccount.getBank().getBankAccountsList().size());
            to = chosenAccount.getBank().getBankAccountsList().get(receiver).getAccountNumber();
            for (Account account: accounts){
                if (account.getAccountNumber().equals(to)){
                    while (account.getAccountNumber().equals(to)){
                        to = chosenAccount.getBank().getBankAccountsList().get(receiver).getAccountNumber();
                    }
                }
            }
            Runnable bankTransfer = () -> chosenAccount.getBank().transfer(chosenAccount.getAccountNumber(), to, amount);
            Main.executorService.execute(bankTransfer);
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ((Main.executorService.isShutdown()) || (Thread.interrupted())) break;
        }
    }

    public int getAmount() {
        return amount;
    }

    public Account getChosenAccount() {
        return chosenAccount;
    }

    public String getTo() {
        return to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return name.equals(customer.name) &&
                accounts.equals(customer.accounts);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Object o) {
        if (o == null) return 1;
        Customer other = (Customer) o;
        int nameComparison = (int) Math.signum(this.name.compareTo(other.name));
        if (nameComparison != 0) return nameComparison;
        int firstCustomerMoney = accounts.stream().mapToInt(Account::getValue).sum();
        int secondCustomerMoney = other.accounts.stream().mapToInt(Account::getValue).sum();
        int moneyComparison = (int) Math.signum(firstCustomerMoney - secondCustomerMoney);
        return moneyComparison;
    }
}
