package com.magicliang.transaction.sys.common.concurrent.lock;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * 基于条件变量的测试
 * 这个实验告诉我们，await 和 signal 都需要在 lock 内为好
 *
 * @author liangchuan
 */
public class ConditionTest {


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }


    @Test
    void testConditionProcedure() throws InterruptedException, IOException {

        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        AtomicBoolean controlFlag = new AtomicBoolean(false);
        Thread t1 = new Thread(() -> {
            lock.lock();
            System.out.println("内线程先求锁");
            try {
                while (!controlFlag.get()) {
                    System.out.println("check input failed, begin to await，准备释放锁");
                    // 在这一行里，内线程 fullyRelease 了 lock
                    condition.await();
                    // 在这一行里，内线程重新拿回了 lock
                    System.out.println("signaled, begin to check input，重新得回锁");
                }
            } catch (Exception ignored) {
                System.out.println("isInterrupted: " + Thread.currentThread().isInterrupted());
            } finally {
                lock.unlock();
            }
        });
        t1.setName("test-thread1");
        t1.start();
        // 造成一种内线程先拿到锁，但释放后进入 wait queue 的情况
        Thread.sleep(1000L);
        lock.lock();
        try {
            // 在 idea 里暂时无法使用 prompt input，所以现阶段就用倒数计时来触发锁定
//            Scanner command = new Scanner(System.in);
//            System.out.println("Enter command: ");
//            boolean running = true;
//
//            while(running){
//                switch(command.nextLine()){
//                    case "signal":
//                        System.out.println("Machine started!");
//                        running = false;
//                        break;
//                }
//            }
//            command.close();

            controlFlag.set(true);
            // 这里会让内线程准备求锁
            System.out.println("让内线程准备求锁");
            // 要在锁里面执行 signal，其内部针对极端情况会触发 unpark，但那个unpark 只能触发 resync 入队用，如果本线程没有释放锁，则内线程求不到锁，会进入第二次 park
            condition.signal();

        } finally {
            // 在这行彻底执行完的一瞬间，内线程求锁完，才能从 await 中退出，这会导致 unpark，await 内部至少会有一次 unpark 醒来，到时候无人争抢锁的话，会直接重新回到获锁（acquired）状态
            System.out.println("释放本线程的锁，下一瞬间内线程就会求到锁");
            lock.unlock();
        }

        Thread.sleep(15000L);
    }
}
