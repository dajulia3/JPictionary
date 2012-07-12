package Game;

import java.util.LinkedList;

/**
 * As given, this code is simply a queue that holds
 *   objects of type T
 *   
 * When you make your changes, it becomes a queue
 *   that blocks on dequeue until it is nonempty,
 *   and blocks on enqueue until it is not full.
 *
 *
 * @param <T> the type of object placed on the queue
 *    For our multiuser game, it is typically a Runnable
 */
public class BlockingQueue<T> {

	private LinkedList<T> queue;
	private int maxSize;

	/**
	 * A queue that causes its caller to wait if
	 * the queue is empty for dequeue or
	 * the queue is full for enqueue
	 * @param maxSize -- maximum size of the queue
	 */
	public BlockingQueue(int maxSize) {
		this.maxSize = maxSize;
		queue = new LinkedList<T>();
	}

	/**
	 * Return the next element from the queue, 
	 * waiting until the queue is not empty.
	 * @return first element in the queue
	 */
	public T dequeue() {
		synchronized(queue){
			while(queue.isEmpty()){
				try {
					queue.wait();
				} catch (InterruptedException e) {
					// FIXME Auto-generated catch block
					e.printStackTrace();
				}
			}
			T ans = queue.removeFirst();
			queue.notifyAll();
			return ans;
		}
	}

	/**
	 * Add an element to the queue,
	 * blocking until the queue is not full
	 * @param thing added to end of the queue
	 */
	public  void enqueue(T thing) {
		synchronized(queue){
			while(queue.size()>=maxSize){
				try {
					queue.wait();
				} catch (InterruptedException e) {
					// FIXME Auto-generated catch block
					e.printStackTrace();
				}
			}
		queue.addLast(thing);
		queue.notifyAll();
		}
	}

	private static void sleep(int secs) {
		try {
			Thread.sleep(secs*1000);
		}
		catch (Throwable t) {

		}
	}

}
