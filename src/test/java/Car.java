import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier cB;
    private static boolean winner;


    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cB) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        this.cB = cB;
    }

    public static int getCarsCount() {
        return CARS_COUNT;
    }

    public static void setCarsCount(int carsCount) {
        CARS_COUNT = carsCount;
    }

    @Override
    public void run() {
        try {
            long timeStart = System.currentTimeMillis();
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            cB.await();
            System.out.println(this.name + " готов");
            cB.await();
            for (int i = 0; i < race.getStages().size(); i++) {
                race.getStages().get(i).go(this);
            }
            Lock lock = new ReentrantLock();
            lock.lock();
            checkWinner(this, lock, timeStart);
            cB.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void checkWinner(Car c, Lock lock, long timeStart) {
        long timeStop = System.currentTimeMillis();
        if (!winner) {
            System.out.println("ПОБЕДИТЕЬ: " + c.name.toUpperCase() + "  " + "ВРЕМЯ: " + (timeStop - timeStart) + "ms");
            winner = true;
        }
        lock.unlock();
    }
}
