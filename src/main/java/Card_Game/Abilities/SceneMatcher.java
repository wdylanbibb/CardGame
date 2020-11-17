package Card_Game.Abilities;

import java.util.HashMap;
import java.util.Map;

public class SceneMatcher {

    private static SceneMatcher INSTANCE;
    public static Map<String, AbilRunScen> abilMap;

    public SceneMatcher(){
        abilMap = new HashMap<>();
        abilMap.put("onplay", AbilRunScen.PLAY);
        abilMap.put("ondeath", AbilRunScen.DEATH);
        abilMap.put("turnstart", AbilRunScen.TURNSTART);
        abilMap.put("turnend", AbilRunScen.TURNEND);
        abilMap.put("use", AbilRunScen.USE);
    }

    public static SceneMatcher getInstance() {
        if(INSTANCE==null){
            INSTANCE = new SceneMatcher();
        }
        return INSTANCE;
    }

    public AbilRunScen getAbilRunScen(String scene){
        return abilMap.getOrDefault(scene, AbilRunScen.USE);
    }


}
