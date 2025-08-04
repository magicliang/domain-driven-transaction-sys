package utilization;


/**
 * project name: domain-driven-transaction-sys
 *
 * description:
 *
 * @author magicliang
 *
 *         date: 2025-08-04 14:42
 */
public class SimpleCPULoader {

    public static void main(String[] args) {
        // 默认50%利用率
        double targetLoad = 0.5;

        // 解析命令行参数
        if (args.length > 0) {
            try {
                targetLoad = Double.parseDouble(args[0]) / 100.0;
                // 限制范围在0-1之间
                targetLoad = Math.max(0.0, Math.min(1.0, targetLoad));
            } catch (NumberFormatException e) {
                System.err.println("参数格式错误，使用默认50%负载");
            }
        }

        System.out.println("开始CPU负载测试，目标利用率: " + (targetLoad * 100) + "%");
        System.out.println("按 Ctrl+C 停止");

        // 执行CPU负载控制
        executeCPULoad(targetLoad);
    }

    private static void executeCPULoad(double loadRatio) {
        final long PERIOD_MS = 100; // 100ms周期

        while (true) {
            long workTime = (long) (PERIOD_MS * loadRatio);
            long sleepTime = PERIOD_MS - workTime;

            // 工作阶段 - 消耗CPU
            long workStart = System.currentTimeMillis();
            while (System.currentTimeMillis() - workStart < workTime) {
                // 空循环消耗CPU
                Math.sqrt(Math.random());
            }

            // 休息阶段 - 让出CPU
            try {
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                System.out.println("\n程序被中断");
                return;
            }
        }
    }
}
