public class MyThread2 implements Runnable {


    private int i ;

    MyThread2(int i){
        this.i = i;
    }
    public void run() {
        i++;

        System.out.println(i);
    }
}
