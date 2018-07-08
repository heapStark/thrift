package heap.stark.netty;

import java.util.concurrent.TimeUnit;

public class DemonThread {
    public static void main(String[] args) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(false);
        thread.start();
        System.out.println("123");
    }
}
