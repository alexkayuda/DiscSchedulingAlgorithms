# Disc Scheduling Algorithms (FCFS, LOOK, C-LOOK)
 ---
Operating System is responsible for using hardware efficiently - for the disk drives, this means having a fast access time and dis bandwidth. Disk bandwidth is the total number of bytes transferred, divided by the total time between the first request for service and the completion of the last transfer. Several algorithms exist to schedule the servicing of disk I/O requests. For example: `FCFS, SSTF, LOOK, C-LOOK, SCAN, C-SCAN`. 
`DiscScheduler.java` file simulates those algorithms and returns the total number of head movement needed to process all requests in a queue. 

Time Complexity of most algorithms is O(nlog(n)). 
