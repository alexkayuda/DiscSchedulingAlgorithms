package algorithms;

/**
 * @author Alexander Kayuda
 * Class: CISC 3320 ET6
 * Last Date Modified: 12/14/2018
 *
 * Description: This is a driver class that tests DiskScheduler class functionality.
 * Purpose:     The purpose of this assignment is to calculate the total amount of head movement required
 *              for each algorithm. The initial head pointer will be passed to the program via command line.
 *
 */
public class DiscSchedulerDriver {
    public static void main(String[] args) {
        DiskScheduler scheduler = DiskScheduler.getInstance();

        //This is a default value that will be used in case of:
        // 1. Parameter was not provided (args.length == 0)
        // 2. Parameter could not be parsed (for example, it contains letters)
        final int DEFAULT_HEAD_POINTER = 3320;
        int headPointer = DEFAULT_HEAD_POINTER;
        try{
            headPointer = converter(args, DEFAULT_HEAD_POINTER);
        }catch (NumberFormatException e) {
            System.out.println("Could not perform conversion of passed parameter to it's Integer Representation.\n" +
                               "Default value of " + DEFAULT_HEAD_POINTER + " will be used instead.");
        } finally {
            scheduler.setHeadPointer((headPointer));
            scheduler.populateQueueOfProcesses();
        }

        //For testing purposes.
        scheduler.printQueue();

//        System.out.println("\nFCFS: Total amount of head movement required: " + scheduler.performFCFS());
//        System.out.println("\nSSTF: Total amount of head movement required: " + scheduler.performSSTF());
//        System.out.println("\nLOOK: Total amount of head movement required: " + scheduler.performLOOK());
//        System.out.println("\nC-LOOK: Total amount of head movement required: " + scheduler.performCLOOK());
//        System.out.println("\nSCAN: Total amount of head movement required: " + scheduler.performSCAN());
        System.out.println("\nC-SCAN: Total amount of head movement required: " + scheduler.performCSCAN());
    }

    /**
     *
     * @param args - array of arguments that was passed through command line.
     * @param DEFAULT_HEAD_POINTER - default value that will be used in case program will not be able to convert
     *                             passed argument to it's Integer representation.
     * @return either Integer representation of the argument that was passed through command line
     *         or a pre-defined value.
     * @throws NumberFormatException if unable to convert passed argument to it's Integer representation.
     */
    public static int converter(String[] args, int DEFAULT_HEAD_POINTER) throws NumberFormatException {
        if(args.length == 0) {
            return DEFAULT_HEAD_POINTER;
        }
        return Integer.parseInt(args[0]);
    }
}
