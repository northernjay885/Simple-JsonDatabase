class SimpleCounter {
    // write your code here
    private static SimpleCounter instance = new SimpleCounter();
    public int counter;

    private SimpleCounter() { }

    public static SimpleCounter getInstance() {
        return instance;
    }

}