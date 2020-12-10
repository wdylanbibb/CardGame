package cardgame.emissions;

import cardgame.cards.Card;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Signal {

    public Class[] classes;
    public String name;
    public boolean nullableParams;

    private final Map<EmissionListener, Pair<Boolean, Predicate<List<Object>>>> listeners = new HashMap<>();

    public Signal(String name, Class... classes) {
        this.name = name;
        this.classes = classes;
        nullableParams = false;
    }

    public Signal(String name, boolean nullableParams, Class... classes) {
        this.name = name;
        this.classes = classes;
        this.nullableParams = nullableParams;
    }

    public boolean emit(Object... args){
        if(args.length != classes.length) {
            Logger.getLogger("emission").severe("Arguments given are different in length than the signal class arguments (Got:\n   " + args + "\nExpected:\n   " + classes);
            return false;
        }
        for(int i=0;i < args.length;i++){
            if(args[i].getClass() != (classes[i]) ){
                Logger.getLogger("emission").severe("Incorrect data type (Given: " + args[i].getClass() + ", Expected: " + classes[i] + ")");
                return false;
            }
        }
        Logger.getLogger("emission").info("Emitting " + name);
        listeners.keySet().forEach(listener -> {
            if(listeners.get(listener).getRight().test(Arrays.asList(args))) {
                listener.onEmit(this, args);
                if (listeners.get(listener).getLeft()) {
                    disconnect(listener);
                }
            }
        });

        return true;
    }

    public void connect(EmissionListener listener, @Nullable Predicate<List<Object>> cardPredicate){
        if(cardPredicate == null){
            cardPredicate = list -> true;
        }
        listeners.put(listener, Pair.of(false, cardPredicate));
        Logger.getLogger("emission").info("Connected " + name);
    }

    public void connect(EmissionListener listener, @Nullable Predicate<List<Object>> cardPredicate, boolean oneShot){
        if(cardPredicate == null){
            cardPredicate = list -> true;
        }
        listeners.put(listener, Pair.of(oneShot, cardPredicate));
        Logger.getLogger("emission").info("Connected " + name);
    }

    public void disconnect(EmissionListener listener){
        listeners.remove(listener);
        Logger.getLogger("emission").info("Disconnected " + name);
    }

}
