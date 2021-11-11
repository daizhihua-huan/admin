import java.util.Arrays;
import java.util.List;

public class MyThread1 implements Runnable {

    // 这里将lock对象换成 Lock(ReentrantLock) 进行lock/unlock也是可以的
    private static final Object lock = new Object();
    private static final int MAX = 20;
    private static int current = 0;

    private int index;
    public MyThread1(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        while (current <= MAX) {
            synchronized (lock) {
                if ((current <= MAX) && (current % 3 == index)) {
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(current++);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        List<Thread> threadList = Arrays.asList(
                new Thread(new MyThread1(0)),
                new Thread(new MyThread1(1)),
                new Thread(new MyThread1(2))
        );

        threadList.forEach(Thread::start);
        for (Thread thread : threadList) {
            thread.join();
        }
    }


}
