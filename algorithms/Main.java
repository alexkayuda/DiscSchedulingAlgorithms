package algorithms;

/**
 * @author Alexander Kayuda
 */
public class Main {
    public static void main(String[] args) {
        DiskScheduler scheduler = DiskScheduler.getInstance();

        //This is a default value that will be used in case of:
        // 1. Parameter was not provided (args.length == 0)
        // 2. Parameter could not be parsed (for example, it contains letters)
        final int DEFAULT_HEAD_POINTER = 3101;
        int headPointer = DEFAULT_HEAD_POINTER;
        try{
            headPointer = converter(args, DEFAULT_HEAD_POINTER);
        }catch (NumberFormatException e) {
            System.out.println("Could not perform conversion of passed parameter to it's Integer Representation.\n" +
                               "Default value of " + DEFAULT_HEAD_POINTER + " will be used instead.");
        }

        scheduler.setHeadPointer((headPointer));
        scheduler.populateQueueOfProcesses();

        //For testing purposes.
        scheduler.printQueue();

//        System.out.println("\n\tFCFS: Total amount of head movement required: " + scheduler.performFCFS());
//        System.out.println("\n\tLOOK: Total amount of head movement required: " + scheduler.performLOOK());
//        System.out.println("\n\tC-LOOK: Total amount of head movement required: " + scheduler.performCLOOK());
    }

    public static int converter(String[] args, int DEFAULT_HEAD_POINTER) throws NumberFormatException {
        if(args.length == 0) {
            return DEFAULT_HEAD_POINTER;
        }
        return Integer.parseInt(args[0]);
    }
}
