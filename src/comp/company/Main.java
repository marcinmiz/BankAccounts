package comp.company;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    static ExecutorService executorService = Executors.newFixedThreadPool(20);
    public static void main(String[] args) {
        Bank bank1 = new Bank("Lion Bank");
        Bank bank2 = new Bank("Tiger Bank");
        int limit = 20000;
        Customer Max = new  Customer("Max");
        Customer Sophia = new  Customer("Sophia");
        Customer Olivia = new  Customer("Olivia");
        Customer Carlos = new  Customer("Carlos");
        Customer Sean = new  Customer("Sean");

        Thread threadMax = new Thread(Max);
        Thread threadSophia = new Thread(Sophia);
        Thread threadOlivia = new Thread(Olivia);
        Thread threadCarlos = new Thread(Carlos);
        Thread threadSean = new Thread(Sean);
        Thread Counter = new Thread(new Counter(limit));

        Account bank1MaxAccount = new Account(Max, bank1);
        Account bank2SophiaAccount = new Account(Sophia, bank1);
        Account bank1OliviaAccount = new Account(Olivia, bank1);
        Account bank1CarlosAccount = new Account(Carlos, bank1);
        Account bank2SeanAccount = new Account(Sean, bank1);

        for (Account account : bank1.getBankAccountsList()) {
            bank1.deposit(account.getAccountNumber(), 100_000);
        }
        for (Account account : bank2.getBankAccountsList()) {
            bank1.deposit(account.getAccountNumber(), 100_000);
        }
        Counter.start();
        threadMax.start();
        threadOlivia.start();
        threadCarlos.start();
        threadSean.start();
        threadSophia.start();

        long startTime = System.currentTimeMillis(), endTime;
        while (true) {
            endTime = System.currentTimeMillis();

            if (endTime - startTime >= limit) {
                executorService.shutdown();
                try {
                    executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);
                    List<Account> concatenatedAccountsList = new ArrayList<>();
                    concatenatedAccountsList.addAll(bank1.getBankAccountsList());
                    concatenatedAccountsList.addAll(bank2.getBankAccountsList());
                    concatenatedAccountsList.sort(Account::compareTo);
                    for (Account account : concatenatedAccountsList) {
                        System.out.println(account.toString());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.shutdownNow();
                break;
            }
        }
    }
}



