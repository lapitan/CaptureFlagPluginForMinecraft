package lapitanplugins.capturebase.boss;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public interface CurrSkillSet {



    void targetSkill();

    void radiusSkill();

    void summonSkill();

    CurrSkillSet shiftForward();

    CurrSkillSet shiftBackwards();

    int getCurrStageNumb();

    default boolean isPlayerNear(Player p){
        return Math.sqrt(Math.pow(p.getLocation().getX()- CaptureBase.getInstance().getBoss().getBossSpawn().getX(),2)+Math.pow(p.getLocation().getZ()-CaptureBase.getInstance().getBoss().getBossSpawn().getZ(),2)+Math.pow(p.getLocation().getY()-CaptureBase.getInstance().getBoss().getBossSpawn().getY(),2))<25;
    }

    default ArrayList<Player> getAllNearPlayers(){
        ArrayList<Player> players=new ArrayList<>();
        for (Player p: Bukkit.getOnlinePlayers()
             ) {
            if(isPlayerNear(p)){
                players.add(p);
            }
        }
        return players;
    }

}
