package generic.aireport;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-08-05 15:29
 */
public class AirPlaneApp {

    public static void main(String[] args) {
        Plane<Landed> p = Plane.newPlane();
        p.getStatus();
        Plane<Flying> fly = Plane.AirTrafficController.takeOff(p);
        fly.getStatus();
        Plane<Landed> land = Plane.AirTrafficController.land(fly);
        land.getStatus();
        // 无法编译通过:
//        aireport.Plane<aireport.Landed> reallyLanded =  aireport.Plane.AirTrafficController.land(land);
//        aireport.Plane<aireport.Flying> reallyFlying =  aireport.Plane.AirTrafficController.takeOff(fly);
    }
}
