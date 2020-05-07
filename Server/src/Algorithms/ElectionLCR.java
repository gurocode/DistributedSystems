package Algorithms;
import java.util.Random;

import com.sun.deploy.nativesandbox.comm.Request;
import com.sun.org.apache.xerces.internal.util.Status;

public class ElectionLCR {
	public static void main(String[] args) {
		int rank, size, i, nbround;
		int TAGUID=1;
		int TAGSTATE=2;

		MPI.Init(args);
		double startTime;
		Request req = null; 
		Status status = null;

		size = MPI.COMM_WORLD.Size();
		rank = MPI.COMM_WORLD.Rank();

		int mynum;
		int[] r = new int[1];
		int[] s = new int[1];
		int e;
for (e=0;e<200;e++) {
		System.out.println("\nElection number : "+e);
		System.out.println("=======================================================");
                // generates an uid (assumes it is unique)
		mynum = MPI.Rand(1000);
		s[0] = mynum;
		System.out.println("[rank " + rank + "] generates uid="+mynum+". Let us start.");
		nbround=1;
	     	startTime = MPI.Wtime();
		MPI.COMM_WORLD.Send(s, 0, 1, MPI.INT, (rank+1)%size,TAGUID);

            // loop receiving message from left neighbourg on ring, 
            while (true) {
		   req = MPI.COMM_WORLD.Irecv(r,0,1, MPI.INT, (rank == 0 ? size-1 : rank-1), MPI.ANY_TAG);
               status = req.Wait();
		   // ----- Election phase -------
		   if (status.tag == TAGUID) {
                    if ( r[0] > s[0] ) {
                            MPI.COMM_WORLD.Send(r, 0, 1, MPI.INT, (rank+1)%size,TAGUID);
                    }
                    else {
                            if (r[0]==s[0]) {
                                    System.out.println("[rank " + rank + "] After "+nbround+" rounds, I know I am the (unique) leader with "+s[0]);
						// I am the unique leader: initiate now another round to broadcast a halting state
                                    MPI.COMM_WORLD.Send(r, 0, 1, MPI.INT, (rank+1)%size,TAGSTATE);
						// ok, the message will eventually come back. Consumes the mesage and Stop after this.
                                    MPI.COMM_WORLD.Recv(r, 0, 1, MPI.INT, (rank == 0 ? size-1 : rank-1),TAGSTATE);
						break;
                            }
                            // else ( r < s ) do nothing
                    }
	         }
		   // ---- Halting phase -------
               if (status.tag == TAGSTATE) {
                     System.out.println("[rank " + rank + "] i just get informed "+r[0]+" is elected.");
                     MPI.COMM_WORLD.Send(r, 0, 1, MPI.INT, (rank+1)%size,TAGSTATE);
                     break;
               }
		   nbround++;
            }
            double stopTime = MPI.Wtime();
		System.out.println("Time usage = " + (stopTime - startTime) + " ms");
		System.out.println("Number of iterations: "+nbround);
}
		MPI.Finalize();
	}
}
