package aireport;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 * <p>
 * date: 2022-08-05 15:25
 */
public interface Flying extends FlightStatus {

    default PlaneStatus getStatus() {
        return PlaneStatus.Flying;
    }
}
