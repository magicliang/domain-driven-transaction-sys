package com.magicliang.transaction.sys.common.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description: 队列实验
 *
 * @author magicliang
 *         <p>
 *         date: 2022-05-10 22:30
 */
@Slf4j
public class QueueExperiment {

//    public static void main(String[] args) {
//        SynchronousQueue<Object> queue = new SynchronousQueue<>();
//        /* transferer.transfer(e, true, 0) 总是返回 null
//         *                 if (h == null || h.mode == mode) {  // empty or same-mode
//         *           if (timed && nanos <= 0) {      // can't wait
//         *               if (h != null && h.isCancelled())
//         *                   casHead(h, h.next);     // pop cancelled node
//         *               else
//         *                  // 总是在这一行返回
//         *                   return null;
//         */
//        if (queue.offer(new Object())) {
//            log.info("123");
//        } else {
//            // 所以 SynchronousQueue 总是会增加非核心线程数
//            log.info("456");
//        }
//    }
}
