package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author Alexander Kayuda
 * Class: CISC 3320 ET6
 * Last Date Modified: 12/14/2018
 *
 * Description:   In Operating Systems, seek time is very important. Since all device requests are linked in queues,
 *                the seek time is increased causing the system to slow down. Disk Scheduling Algorithms are used to
 *                reduce the total seek time of any request.
 *
 * Disk with 5000 cylinders numbered from 0 to 4999.
 * Generate 50 random numbers in that range - it will be my queue of numbers to process.
 * Accept starting position passed to me from the command line as a parameter.
 * Perform ANY 3 algorithms and calculate the total amount of head movement required for each algorithm.
 */
public class DiskScheduler{

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

//        populateQueue();   //testing

        int randomNumber;
        for (int i = 0; i < 50; i++) {
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
     * This function simulates the behavior of FCFS (First Come First Served) algorithm.
     * All incoming requests are placed at the end of the queue. Whatever number that is
     * next in the queue will be the next number served.
     *
     * @return totalHeadMovement - the total amount of head movement required performing this algorithm.
     */
    public int performFCFS(){
        int totalHeadMovement = 0;

        for (int i = 1; i < queueOfProcesses.size(); i++)
            totalHeadMovement+= Math.abs(queueOfProcesses.get(i) - queueOfProcesses.get(i-1));

        minSubArray.clear();
        maxSubArray.clear();
        return totalHeadMovement;
    }

    /**
     * This function simulates the behavior of SSTF (Shortest Seek Time First) algorithm.
     * All incoming requests will be serviced according to next shortest distance.
     *
     * @return totalHeadMovement - the total amount of head movement required performing this algorithm.
     */
    public int performSSTF(){
        int totalHeadMovement = 0;

        minSubArray.addAll(maxSubArray);
        int currentProcessID = headPointer;

        ArrayList<myNode> dynamicMatrix = new ArrayList<>();
        for (int i = 0; i < minSubArray.size(); i++)
            dynamicMatrix.add(new myNode(minSubArray.get(i), Math.abs(headPointer - minSubArray.get(i))));

        while(dynamicMatrix.size() > 0){
            for (int i = 0; i < dynamicMatrix.size(); i++)
                dynamicMatrix.get(i).setCurrentDifference(Math.abs(currentProcessID - dynamicMatrix.get(i).getProcessID()));

            Collections.sort(dynamicMatrix);
            currentProcessID = dynamicMatrix.get(0).getProcessID();
            totalHeadMovement += dynamicMatrix.get(0).getCurrentDifference();
            dynamicMatrix.remove(0);
        }

        minSubArray.clear();
        maxSubArray.clear();
        return totalHeadMovement;
    }

    /**
     * This function simulates the behavior of LOOK algorithm. LOOK algorithm is a version of SCAN algorithm.
     * It scans down towards the nearest end and then when it hits the process with the least request number
     * in our request queue, it starts scanning 'up', serving the requests that were not served on the way 'down'.
     *
     * @return totalHeadMovement - the total amount of head movement required performing this algorithm.
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
     * This function simulates the behavior of SCAN algorithm.
     * This approach works like an elevator does. It scans down towards the nearest end and then
     * when it hits the bottom it scans up servicing the requests that it didn't get going down.
     * If a request comes in after it has been scanned it will not be serviced until
     * the process comes back down or moves back up
     *
     * @return totalHeadMovement - the total amount of head movement required performing this algorithm.
     */
    public int performSCAN(){
        int totalHeadMovement = 0;

        minSubArray.add(DISK_CYLINDERS_MIN);
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
     * This function simulates the behavior of C-LOOK algorithm. C-LOOK algorithm is a version of C-SCAN algorithm.
     * Arm only goes as far as the last request in each direction, then reverses direction immediately,
     * without first going all the way to the end.
     *
     * @return totalHeadMovement - the total amount of head movement required performing this algorithm.
     */
    public int performCLOOK(){
        int totalHeadMovement = 0;

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
     * This function simulates the behavior of C-SCAN algorithm.
     * Circular scanning works just like the elevator to some extent. It begins its scan toward
     * the nearest end and works it way all the way to the end of the system.
     * Once it hits the bottom or top it jumps to the other end and moves in the same direction.
     * Keep in mind that the huge jump doesn't count as a head movement.
     *
     * @return totalHeadMovement - the total amount of head movement required performing this algorithm.
     */
    public int performCSCAN(){
        int totalHeadMovement = 0;

        maxSubArray.add(headPointer);

        Collections.sort(minSubArray);
        Collections.sort(maxSubArray);
        int difference = maxSubArray.get(maxSubArray.size() - 1) - minSubArray.get(0);
        maxSubArray.addAll(minSubArray);

        for (int i = 1; i < maxSubArray.size(); i++){
            if((Math.abs(maxSubArray.get(i) - maxSubArray.get(i-1)) != difference))
                totalHeadMovement+= Math.abs(maxSubArray.get(i) - maxSubArray.get(i-1));
        }

        minSubArray.clear();
        maxSubArray.clear();
        return totalHeadMovement;
    }

    private class myNode implements Comparable<myNode>{
        private int processID;
        private int currentDifference;

        public myNode() {
            processID = Integer.MIN_VALUE;
            currentDifference = Integer.MAX_VALUE;
        }

        public myNode(int processID, int currentDifference) {
            this.processID = processID;
            this.currentDifference = currentDifference;
        }

        public int getProcessID() {
            return processID;
        }

        public void setProcessID(int processID) {
            this.processID = processID;
        }

        public int getCurrentDifference() {
            return currentDifference;
        }

        public void setCurrentDifference(int currentDifference) {
            this.currentDifference = currentDifference;
        }

        @Override
        public int compareTo(myNode other) {
            return this.getCurrentDifference() - other.getCurrentDifference();
        }
    }

    /**
     * TESTING (Not part of the requirements)
     */
    public void printQueue(){
        System.out.print("Queue of Processes: ");
        for (int i = 0; i < queueOfProcesses.size(); i++) {
            System.out.print(queueOfProcesses.get(i) + " ");
        }
        System.out.println();
    }

    /**
     * TESTING (Not part of the requirements)
     */
    private void populateQueue(){

//        queueOfProcesses.add(98);
//        queueOfProcesses.add(183);
//        queueOfProcesses.add(37);
//        queueOfProcesses.add(122);
//        queueOfProcesses.add(14);
//        queueOfProcesses.add(124);
//        queueOfProcesses.add(65);
//        queueOfProcesses.add(67);

        queueOfProcesses.add(176);
        queueOfProcesses.add(79);
        queueOfProcesses.add(34);
        queueOfProcesses.add(60);
        queueOfProcesses.add(92);
        queueOfProcesses.add(11);
        queueOfProcesses.add(41);
        queueOfProcesses.add(114);

        for (int i = 0; i < queueOfProcesses.size(); i++) {
            if(queueOfProcesses.get(i) <= headPointer){
                minSubArray.add(queueOfProcesses.get(i));
            } else {
                maxSubArray.add(queueOfProcesses.get(i));
            }
        }
    }
}

