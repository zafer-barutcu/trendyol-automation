package framework.utils;


public final class WrappedSleep {
    private WrappedSleep() {}

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

