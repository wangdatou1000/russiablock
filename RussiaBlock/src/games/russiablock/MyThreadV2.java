package games.russiablock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class MyThreadV2 extends Thread {

	private volatile boolean suspend = false;
	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	private Runnable logic;

	public MyThreadV2(Runnable f) {
		logic = f;
	}

	public void setSuspend(boolean suspend) {
		if (!suspend) {
			lock.lock();
			try {
				condition.signalAll();
			} finally {
				lock.unlock();
			}
		}
		this.suspend = suspend;
	}

	public boolean isSuspend() {
		return this.suspend;
	}

	public void run() {
		while (true) {
			lock.lock();
			try {
				if (suspend) {
					try {
						condition.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} finally {
				lock.unlock();
			}
			logic.run();
		}
	}


	public static void main(String[] args) throws Exception {
		Runnable logic = () -> {
			System.out.println("myThread is runing");
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		MyThreadV2 myThread = new MyThreadV2(logic);
		myThread.start();
		Thread.sleep(2000);
		myThread.setSuspend(true);
		System.out.println("myThread has stopped");
		Thread.sleep(3000);
		myThread.setSuspend(false);

	}
}
