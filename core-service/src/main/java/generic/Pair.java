package generic;

import java.lang.reflect.Method;
import java.time.LocalDate;

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

    public T getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setFirst(T newValue) {
        first = newValue;
    }

    public void setSecond(T newValue) {
        second = newValue;
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
    }
}

class DateInterval extends Pair<LocalDate> {

    @Override
    public void setSecond(LocalDate second) {
        Throwable x = new Throwable();
        System.out.println(x);
        if (second.compareTo(getFirst()) > 0) {
            super.setSecond(second);
        }
    }
}


