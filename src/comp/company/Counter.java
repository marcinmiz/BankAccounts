package comp.company;

import static java.lang.Thread.sleep;

public class Counter implements Runnable {
    private int seconds = 0, limit;

    public Counter(int limit) {
        this.limit = limit;
    }

    @Override
    public void run() {
        while (seconds <= (limit/1000)){
            System.out.println(seconds + " second(s) from start:");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds++;
        }
    }
}
