package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * FCFS     done
 * SSTF     ?
 * LOOK     done
 * C-LOOK   done
 * SCAN     ?
 * C-SCAN   ?
 *
 * Disk with 5000 cylinders numbered from 0 to 4999.
 * Generate 50 random numbers in that range - it will be my queue of numbers to process.
 * Accept starting position passed to me from command line as a parameter.
 * Perform ANY 3 algorithms and calculate total amount of head movement required for each algorithm.
 */
public class DiskScheduler {

    private static DiskScheduler instance = null;
    private static Random random = new Random();

    private ArrayList<Integer> queueOfProcesses = new ArrayList<>();
    private ArrayList<Integer> minSubArray = new ArrayList<>();
    private ArrayList<Integer> maxSubArray = new ArrayList<>();

    private static final int DISK_CYLINDERS_MIN = 0;
    private static final int DISK_CYLINDERS_MAX = 4999;
    private static int headPointer;

    private DiskScheduler() {  }

    public static DiskScheduler getInstance() {
        return instance == null ? instance = new DiskScheduler() : instance;
    }

    public static int getHeadPointer() {
        return headPointer;
    }

    public static void setHeadPointer(int headPointer) {
        DiskScheduler.headPointer = headPointer;
    }

    public void populateQueueOfProcesses() {
        queueOfProcesses.add(0, getHeadPointer());

        int randomNumber;
        for (int i = 0; i < 5; i++) {
            randomNumber = random.nextInt(DISK_CYLINDERS_MAX);

            //Generates a number between 0 and 4999 that represents the queue of processes.
            queueOfProcesses.add(randomNumber);

            //also populate min and max sub-arrays
            if(randomNumber <= headPointer)
                minSubArray.add(randomNumber);
            else
                maxSubArray.add(randomNumber);
        }
    }

    /**
     *
     * @return totalHeadMovement
     */
    public int performFCFS(){
        int totalHeadMovement = 0;

        totalHeadMovement += Math.abs(queueOfProcesses.get(1) - getHeadPointer());
        for (int i = 2; i < queueOfProcesses.size(); i++)
            totalHeadMovement+= Math.abs(queueOfProcesses.get(i) - queueOfProcesses.get(i-1));

        return totalHeadMovement;
    }

    /**
     *
     * @return
     */
    public int performLOOK(){
        int totalHeadMovement = 0;

        minSubArray.add(headPointer);
        Collections.sort(minSubArray, Collections.reverseOrder());
        Collections.sort(maxSubArray);
        minSubArray.addAll(maxSubArray);

        for (int i = 1; i < minSubArray.size(); i++)
            totalHeadMovement+= Math.abs(minSubArray.get(i) - minSubArray.get(i-1));

        minSubArray.clear();
        maxSubArray.clear();
        return totalHeadMovement;
    }

    /**
     *
     * @return
     */
    public int performCLOOK(){
        int totalHeadMovement = 0;

        minSubArray.add(headPointer);
        Collections.sort(minSubArray, Collections.reverseOrder());
        Collections.sort(maxSubArray, Collections.reverseOrder());
        minSubArray.addAll(maxSubArray);

        for (int i = 1; i < minSubArray.size(); i++)
            totalHeadMovement+= Math.abs(minSubArray.get(i) - minSubArray.get(i-1));

        minSubArray.clear();
        maxSubArray.clear();
        return totalHeadMovement;
    }

    /**
     * TESTING
     */
    public void printQueue(){
        for (int i = 0; i < queueOfProcesses.size(); i++) {
            System.out.println(queueOfProcesses.get(i));
        }
    }
}