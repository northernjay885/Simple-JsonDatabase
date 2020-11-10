public class Main {

    public static void main(String[] args) {
        int i = 0;
        Thread t1 = new Thread(new RunnableWorker(), String.format("worker-%d", i++));
        Thread t2 = new Thread(new RunnableWorker(), String.format("worker-%d", i++));
        Thread t3 = new Thread(new RunnableWorker(), String.format("worker-%d", i));
        t1.start();
        t2.start();
        t3.start();
        // create threads and start them using the class RunnableWorker
    }
}

// Don't change the code below       
class RunnableWorker implements Runnable {

    @Override
    public void run() {
        final String name = Thread.currentThread().getName();

        if (name.startsWith("worker-")) {
            System.out.println("too hard calculations...");
        } else {
            return;
        }
    }
}