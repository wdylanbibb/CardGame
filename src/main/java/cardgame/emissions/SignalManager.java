package cardgame.emissions;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SignalManager {

    public static final Map<String, Signal> signals = new HashMap<>();

    public static Signal createSignal(String s, Class... classes) {
        return signals.computeIfAbsent(s, s1 -> new Signal(s, classes));
    }

    public static Signal getSignal(String s) {
        return signals.get(s);
    }

    public static boolean hasSignal(String s) {
        return signals.containsKey(s);
    }

    public static boolean connect(String s, EmissionListener listener, @Nullable Predicate<List<Object>> cardPredicate) {
        if (hasSignal(s)) {
            getSignal(s).connect(listener, cardPredicate);
            return true;
        }
        return false;
    }

    public static boolean connect(String s, EmissionListener listener, @Nullable Predicate<List<Object>> cardPredicate, boolean oneShot) {
        if (hasSignal(s)) {
            getSignal(s).connect(listener, cardPredicate, oneShot);
            return true;
        }
        return false;
    }

}