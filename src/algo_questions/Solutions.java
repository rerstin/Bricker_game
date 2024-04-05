package algo_questions;
import java.util.Arrays;
public class Solutions {

    /**
     * Method computing the maximal amount of tasks out of n tasks that can be completed with m time slots.
     * @param tasks array of integers of length n. tasks[i] is the time in hours required to complete task i.
     * @param timeSlots array of integersof length m. timeSlots[i] is the length in hours of the slot i.
     * @return maximal amount of tasks that can be completed
     * Description of algorithm:
     * 1) Sorting of arrays
     * 2) Going in loop over array and checking which tasks can be performed.
     * The most rational way to perform task i is to perform It in min timeSlot (|min(task[i]-timeSlots[j]|).
     * It allows know how to perform the biggest number of tasks.
     * It not a good way to calculate all options of |min(task[i]-timeSlots[j]|, so sorting will help here.
     * The most rational timeSlot for task "i" is a first timeSlot that not smaller than task "i" (timeSlot j).
     * So I take this timeSlot j, increment a number of performed task and go to search the most rational timeSlot for next task.
     * Because also a tasks array is sorted, for each task I will be able to find the most rational timeSlot and therefore perform a maximum .
     * This algorithm will take N log(N) time (N = max(n,m).
     */
    public static int alotStudyTime(int[] tasks, int[] timeSlots) {
        //copy of array to avoid input changes.
        int[] tasksCopy = Arrays.copyOf(tasks, tasks.length);
        int[] timeSlotsCopy = Arrays.copyOf(timeSlots, timeSlots.length);
        // sorting of arrays
        Arrays.sort(tasksCopy);
        Arrays.sort(timeSlotsCopy);
        int nmuOfPerformedTasks = 0;
        int j = 0;
        for (int i = 0; i < timeSlotsCopy.length; i++) {
            if(j >= tasksCopy.length){
                break;
            }
            if (tasksCopy[j] <= timeSlotsCopy[i]) {
                // can perform a task
                nmuOfPerformedTasks++;
                j++;
            }
        }
        return nmuOfPerformedTasks;
    }

    /**
     * Method computing the min amount of leaps a frog needs to jump across n waterlily leaves, from leaf 1 to leaf n.
     * @param leapNum array of ints. leapNum[i] is how many leaves ahead you can jump from leaf i.
     * @return min amount of leaps a frog needs to jump across n waterlily leaves, from leaf 1 to leaf n.
     * This algorithm of The Shortest Path type algorithms. It acts in next way:
     * 1) Initialize an array with len(leapNum) elements.
     * Each element (i) in this array will be a minimum number of required jumps to arrive to leaf(i) for leaf(1).
     * Each element (except first = 0) will be initialized with MaxValue.
     * 2) Go over each leapNum[i] and develop a new array, for example: if with leaf(i) we can arrive to leaf(j)
     * more than in jumpsToLeaf[j] will be number of jump of path that include leaf(j).
     * 3) return a jumpsToLeaf[jumpsToLeaf.length - 1] that exactly represent a required number.
     * Correctness:
     * In order to find the shortest path we need to check all possible paths.
     * It is actually what we do. We compute a length of the shortest path to each leaf,
     * and in this way we check all possible paths to leaf(n) and search the shortest.
     * Run Time:
     * Outward loop has n iterations in which iterations we have leapNum[i] times of constant time in each one.
     * So in total we have O(leapNum[0] + ... + leapNum[n]).
     * in worth case it will be O(n^2), but average we will have a linear run time.
     */
    public static int minLeap(int[] leapNum){
        if(leapNum.length == 0){
            return 0;
        }
        // create a new array (each cell in array is she shortest num of jumps to it)
        int[] jumpsToLeaf = new int[leapNum.length];
        for (int i = 0; i < leapNum.length; i++) {
            jumpsToLeaf[i] = Integer.MAX_VALUE;
        }
        jumpsToLeaf[0] = 0;
        for (int i = 0; i < leapNum.length; i++) {
            for(int j = 1; j <= leapNum[i]; j++)
            {
                // check if we can jump to leaf using other leaf
                if(i + j < leapNum.length && jumpsToLeaf[i + j] > jumpsToLeaf[i] + 1)
                {
                    jumpsToLeaf[i + j] = jumpsToLeaf[i] + 1;
                }
            }
        }
        return jumpsToLeaf[jumpsToLeaf.length - 1];
    }

    /**
     * Method computing a number of possible bucket walks.
     * @param n number of liters.
     * @return number of possible bucket walks.
     * Description of algorithm:
     * 1) calculation a Fibonacci(n)
     * Correctness:
     * Dived a problem to sub problems: We can see that we can solve a problem with n liters in the following way:
     * we can add a number of possible way for getting n - 1 and n - 2 liters.
     * This claim is correct, because each time tha we increase a number of liters,
     * we append possible wal with one liter and with two liters.
     * So we can go and take one liter and receive a sub problem with n - 1 liters.
     * Or we can go and take two liters and receive a sub problem with n - 2 liters.
     * And It is actually Fibonacci sequence.
     * Runtime:
     * We have loop with n iteration with constant runtime in each one. So O(n).
     */
    public static int bucketWalk(int n)
    {
        return calcalateFibonnachiN(n);
    }

    private static int calcalateFibonnachiN(int n) {
        // create a new array for storing data for calculating
        int[] fibonnachiSequence = new int[n + 1];
        // first and second Fibonacci number is 1
        fibonnachiSequence[0] = 1;
        if(n > 0) {
            fibonnachiSequence[1] = 1;
        }
        // calculate a number according to formula
        for (int i = 2; i < fibonnachiSequence.length; i++) {
            fibonnachiSequence[i] += fibonnachiSequence[i - 1] + fibonnachiSequence[i - 2];
        }
        return fibonnachiSequence[n];

    }

    /**
     * Method computing the number of similar structure BTS.
     * @param n Number of Nodes in tree
     * @return number of similar structure BTS.
     * Description of algorithm:
     * 1) calculate an n'th Catalan's number.
     * Correctness:
     * Define F(i,n) is the number of unique BST, where the number i is served as the root of BST
     * (1 is less than or equal to i which is less than or equal to n).
     * G(n) = F(1,n)+...+f(i,n)+...+F(n,n) if a number of unique BST.
     * When we choose a root Node, we divide our problem to left subtree similar problem
     * and right subtree similar problem.
     * We notice that F(i, n) = G(i - 1) * G(n - i),
     * because to calculate number of unique BST with node i in the root, we have to multiply all possibilities in
     * left and right subtrees.
     * So in total we have:
     * G(n) = G(0) * G(n) + ... + G(n - 1) * G(0)
     * And it is exactly Catalan(n).
     * Runtime:
     * We have a 2 + 3 + 4 + ... + n iteration with constant amount ow work.
     * In total, we will receive O(n^2).
     */
    public static int numTrees(int n) {
        return calculateCatalanNum(n);
    }

    private static int calculateCatalanNum(int n) {
        // create a new array for storing data for calculating
        int[] CatalanArray = new int[n + 1];
        // first and second catalan number is 1
        CatalanArray[0] = CatalanArray[1] = 1;
        // calculate a number according to formula
        for (int i = 2; i < CatalanArray.length; i++) {
            for (int j = 1; j <= i; j++) {
                CatalanArray[i] += CatalanArray[j - 1] * CatalanArray[i - j];
            }
        }
        return CatalanArray[n];
    }
}
