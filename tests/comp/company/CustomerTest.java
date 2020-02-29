package comp.company;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    Bank bank;
    Customer Monica;
    Customer Albert;

    @BeforeEach
    void setUp() {
        bank = new Bank("Jaguar Bank");
        Monica = new Customer("Monica");
        Albert = new Customer("Albert");

    }

    @Test
    void shouldAddAccount() {
        Account bankMonicaAccount = new Account(Monica, bank);
        assertTrue(Monica.getAccounts().contains(bankMonicaAccount));
    }

    @Test
    void shouldChooseRandomAccountAndTransferSomeMoneyTo() {

        Account bankMonicaAccount = new Account(Monica, bank);
        Account bankAlbertAccount = new Account(Albert, bank);
        bank.deposit(bankMonicaAccount.getAccountNumber(), 50_000);
        bank.deposit(bankAlbertAccount.getAccountNumber(), 50_000);
        Thread threadMonica = new Thread(Monica);
        int expectedMonica = 50_000;
        int expectedAlbert = 50_000;
        threadMonica.start();
        for (int i = 0; i < 2; i++) {
//            System.out.println(bankMonicaAccount.getValue());
//            System.out.println(bankAlbertAccount.getValue());
            expectedMonica -= Monica.getAmount();
            expectedAlbert += Monica.getAmount();
            int finalExpectedMonica = expectedMonica;
            int finalExpectedAlbert = expectedAlbert;
            assertAll(
                    () -> assertEquals(finalExpectedMonica, bankMonicaAccount.getValue()),
                    () -> assertEquals(finalExpectedAlbert, bankAlbertAccount.getValue())
            );

            try {
                TimeUnit.MILLISECONDS.sleep(6500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threadMonica.interrupt();
    }
}