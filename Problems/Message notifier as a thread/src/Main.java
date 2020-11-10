class MessageNotifier extends Thread {

    int repeats;
    String msg;
    // write fields to store variables here

    public MessageNotifier(String msg, int repeats) {
        // implement the constructor
        this.msg = msg;
        this.repeats = repeats;
    }

    @Override
    public void run() {
        for (int i = 0; i < repeats; ++i) {
            System.out.println(msg);
        }
    }
}