package lapitanplugins.capturebase.boss;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class FifthSkillSet implements CurrSkillSet {

    public FifthSkillSet() {
        CaptureBase.getInstance().getBoss().getBossBar().setColor(BarColor.YELLOW);
        CaptureBase.getInstance().getBoss().getBoss().setSpell(Spellcaster.Spell.WOLOLO);
    }

    @Override
    public void targetSkill() {
        //lightnings

        ArrayList<Player> players=getAllNearPlayers();

        if(players.isEmpty()) return;

        ArrayList<Player> targets=new ArrayList<>();

        for(int i=0;i<1+(players.size()/3);i++){
            targets.add(players.get((int)(Math.random()*1000)%players.size()));
        }

        for (Player player: targets
        ) {
            Location loc=player.getLocation();

            LightningStrike lightningStrike=(LightningStrike) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(loc,EntityType.LIGHTNING);
            lightningStrike.setFlashCount(5);
        }
    }

    @Override
    public void radiusSkill() {

        //fangs

        Location loc = CaptureBase.getInstance().getBoss().getBoss().getLocation();

        final int[] angle = {(int) (Math.random() * 1000) % 180};
        double radius=2;
        int i=0;

        double x0=loc.getX();
        double z0=loc.getZ();

        while (radius<=25){

            double finalRadius = radius;
            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                for(int j=0;j<9;j++) {
                    double x1 = x0 + finalRadius * Math.sin(Math.toRadians(angle[0]));
                    double z1 = z0 + finalRadius * Math.cos(Math.toRadians(angle[0]));

                    loc.setX(x1);
                    loc.setZ(z1);

                    Location location = new Location(Bukkit.getWorld("world"), x1, loc.getY(), z1);

                    Vector vector = new Vector();

                    vector.setZ((x1 - x0) * 0.5D);
                    vector.setX((-1)*(z1 - z0) * 0.5D);
                    vector.setY(-0.3D);

                    location.setDirection(vector);

                    EvokerFangs evokerFangs=(EvokerFangs) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location, EntityType.EVOKER_FANGS);

                    angle[0] += 40;
                }
            },2*i);
            i++;
            radius+=1;
        }


    }

    @Override
    public void summonSkill() {
        //ravagers

        ArrayList<Player> players=getAllNearPlayers();

        if(players.isEmpty()) return;

        for(int i=0;i<1+(players.size()/4);i++){
            Ravager ravager= (Ravager) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(CaptureBase.getInstance().getBoss().getBoss().getLocation(),EntityType.RAVAGER);
            Pillager raider= (Pillager) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(CaptureBase.getInstance().getBoss().getBoss().getLocation(),EntityType.PILLAGER);
            Pillager raider2= (Pillager) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(CaptureBase.getInstance().getBoss().getBoss().getLocation(),EntityType.PILLAGER);
            raider.addPassenger(raider2);
            ravager.addPassenger(raider);
        }
    }

    @Override
    public CurrSkillSet shiftForward() {
        return new SixthSkillSet();
    }

    @Override
    public CurrSkillSet shiftBackwards() {
        return new FourthSkillSet();
    }

    @Override
    public int getCurrStageNumb() {
        return 1;
    }
}
