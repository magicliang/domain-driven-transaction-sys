package algorithm.dp;

public class ClimbStepsValidator {

    public static void main(String[] args) {
        ClimbSteps climbSteps = new ClimbSteps();

        System.out.println("验证 dpClimbWithAfterEffect 方法的正确性：");
        System.out.println("n=1: " + climbSteps.dpClimbWithAfterEffect(1) + " (期望: 1)");
        System.out.println("n=2: " + climbSteps.dpClimbWithAfterEffect(2) + " (期望: 1)");
        System.out.println("n=3: " + climbSteps.dpClimbWithAfterEffect(3) + " (期望: 2)");
        System.out.println("n=4: " + climbSteps.dpClimbWithAfterEffect(4) + " (期望: 3)");
        System.out.println("n=5: " + climbSteps.dpClimbWithAfterEffect(5) + " (期望: 4)");
        System.out.println("n=6: " + climbSteps.dpClimbWithAfterEffect(6) + " (期望: 6)");
        System.out.println("n=7: " + climbSteps.dpClimbWithAfterEffect(7) + " (期望: 9)");
        System.out.println("n=8: " + climbSteps.dpClimbWithAfterEffect(8) + " (期望: 13)");
        System.out.println("n=9: " + climbSteps.dpClimbWithAfterEffect(9) + " (期望: 19)");
        System.out.println("n=10: " + climbSteps.dpClimbWithAfterEffect(10) + " (期望: 28)");
    }
}