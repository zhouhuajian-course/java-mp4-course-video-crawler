package test;

import java.util.LinkedList;
import java.util.List;

public class LinkedListMultiThreadTest implements Runnable {
    // 非线程安全
    List<String> list = null;

    public LinkedListMultiThreadTest() {
        list = new LinkedList<>();
    }

    public void run() {
        try {
            Thread.sleep((int) (Math.random() * 2));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        list.add(Thread.currentThread().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadGroup group = new ThreadGroup("group");
        LinkedListMultiThreadTest linkedListMultiThreadTest = new LinkedListMultiThreadTest();
        for (int index = 0; index < 10000; index++) {
            Thread thread = new Thread(group, linkedListMultiThreadTest, String.valueOf(index));
            thread.start();
        }

        while (group.activeCount() > 0) {
            Thread.sleep(10);
        }

        System.out.println();
        // 线程安全，就是1万。否则，可能不是10000。
        // 9570
        System.out.println(linkedListMultiThreadTest.list.size());
    }
}
