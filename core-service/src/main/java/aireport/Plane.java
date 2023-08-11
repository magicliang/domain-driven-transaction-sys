package aireport;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * project name: domain-driven-transaction-sys
 * <p>
 * description:
 *
 * @author magicliang
 *         <p>
 *         date: 2022-08-05 15:28
 */
public class Plane<St extends FlightStatus> extends GenericType<St> {

    private int passenger;

    /**
     * 禁掉了除工厂方法和指定的状态构造方法以外的所有其他构造方法。当然，防不了反射攻击（reflection attack）。
     *
     * @param passenger 乘客
     */
    private Plane(int passenger) {
        this.passenger = passenger;
    }

    /**
     * 状态构造方法
     * 在这里每次飞机从一个状态转成另一个飞机状态，都产生了一个新的对象，类似 Value Object 的模式。
     *
     * @param p
     */
    private Plane(Plane<? extends FlightStatus> p) {
        // 在这里，我们可以使用装饰器模式。也可以使用 clone 模式，把乘客（也就是内部状态）移交过去。这取决于我们要不要把旧飞机实例的状态迁移到新飞机实例上。
        this.passenger = p.getPassenger();
        // 做任何想要做的事情
    }

    /**
     * 工厂方法
     *
     * @return
     */
    public static Plane<Landed> newPlane() {
        return new LandedPlane(10);
    }

    public int getPassenger() {
        return passenger;
    }

    public void getStatus() {

        final Class<? extends Plane> aClass = getClass();
        final Type genericSuperclass = aClass.getGenericSuperclass();

        ParameterizedType parameterizedSuperType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedSuperType.getActualTypeArguments();
        // 如果使用匿名子类，这里显示的 type variable 的名字，得不到 aireport.Landed 或者 aireport.Flying 这类类型，所以我们无法动态地把 aireport
        // .FlightStatus 里隐藏的信息读出来；如果使用具体子类，这里可以得到 aireport.FlightStatus 的子类
        Type actualTypeArgument = actualTypeArguments[0];
        final String typeName = actualTypeArgument.getTypeName();
        // 这里强依赖类型构造器
//        final PlaneStatus planeStatus = PlaneStatus.valueOf(typeName);
//        System.out.println(planeStatus);

        // 这一步走不通
//        FlightStatus planeStatus = (FlightStatus) Proxy.newProxyInstance(
//                Thread.currentThread().getContextClassL2hheoader(),
//                new Class[]{FlightStatus.class},
//                new InvocationHandler() {
//                    @Override
//                    public Object invoke(final Object proxy, final Method method, final Object[] args) throws
//                    Throwable {
//                        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
//                                .getDeclaredConstructor(Class.class);
//                        constructor.setAccessible(true);
//                        return constructor.newInstance(FlightStatus.class)
//                                .in(FlightStatus.class)
//                                .unreflectSpecial(method, FlightStatus.class)
//                                .bindTo(proxy)
//                                .invokeWithArguments();
//                    }
//                }
//        );
//        System.out.println(planeStatus.getStatus());

    }

    public static class AirTrafficController {

        public static Plane<Landed> land(Plane<Flying> p) {
            return new LandedPlane(p);
        }

        public static Plane<Flying> takeOff(Plane<Landed> p) {
            return new FlyingPlane(p);
        }
    }

    public static class LandedPlane extends Plane<Landed> {

        public LandedPlane(final Plane<? extends FlightStatus> p) {
            super(p);
        }

        public LandedPlane(final int passenger) {
            super(passenger);
        }
    }

    public static class FlyingPlane extends Plane<Flying> {

        public FlyingPlane(final int passenger) {
            super(passenger);
        }

        public FlyingPlane(final Plane<? extends FlightStatus> p) {
            super(p);
        }
    }

}
