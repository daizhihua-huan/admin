import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        Object o = new Object();

        new Thread(new PrintRunnable(o, 1)).start();
        new Thread(new PrintRunnable(o, 2)).start();
        new Thread(new PrintRunnable(o, 3)).start();



    }


}
class PrintRunnable implements Runnable {
    private final Object o;
    private int threadId;
    private static volatile int num = 1;

    PrintRunnable(Object o, int threadId) {
        this.o = o;
        this.threadId = threadId;
    }

    public void run() {
        while (num < 20) synchronized (o) {
                while (num / 5 % 3 + 1 != threadId) {
                    try {
                        o.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            System.out.print("线程" + threadId + ": ");
            for (int i = 0; i < 5; i++, num++)
                System.out.print(num + ",");
            System.out.println();

            o.notifyAll();
        }
    }

}