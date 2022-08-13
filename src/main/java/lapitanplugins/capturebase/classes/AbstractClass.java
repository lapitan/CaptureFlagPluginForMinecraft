package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public interface AbstractClass {

    //mana= player level

    void skill1(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException;

    void skill2(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException;

    void skill3(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException;

    void skill4(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException;

    void ultimate(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException;

    void doTick(CaptureBasePlayer player);

    int getNumber();

    void doSpawnThings(CaptureBasePlayer player, Object[] someObjects);

    default void highlight(CaptureBasePlayer player){
        if(player.getKillStreak()>=10){
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,40,1));
        }
    }

}
