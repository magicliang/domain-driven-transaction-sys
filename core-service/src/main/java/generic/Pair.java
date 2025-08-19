package generic;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Objects;

/**
 * @author liangchuan
 */
public class Pair<T> {

    private T first;
    private T second;

    public Pair() {
        first = null;
        second = null;
    }

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public static void main(String[] args) throws NoSuchMethodException {
        DateInterval interval = new DateInterval();
        Pair<LocalDate> pair = interval;
        pair.setFirst(LocalDate.now());
        pair.setSecond(LocalDate.now());
        LocalDate first1 = pair.getFirst();
        Class<DateInterval> dateIntervalClass = DateInterval.class;
        Method method = dateIntervalClass.getMethod("setSecond", Object.class);
        System.out.println(method);
        Method[] methods = dateIntervalClass.getMethods();
        System.out.println(methods);

        DateInterval[] arr = new DateInterval2[10];
        arr[0] = new DateInterval();
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T newValue) {
        first = newValue;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T newValue) {
        second = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Pair<?> pair = (Pair<?>) o;
        return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }
}

class DateInterval extends Pair<LocalDate> {

    @Override
    public void setSecond(LocalDate second) {
        Throwable x = new Throwable();
        System.out.println(x);
        if (second.isAfter(getFirst())) {
            super.setSecond(second);
        }
    }
}

class DateInterval2 extends DateInterval {

}


