package lapitanplugins.capturebase.boss;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.*;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class ThirdSkillSet implements CurrSkillSet {


    public ThirdSkillSet() {

        CaptureBase.getInstance().getBoss().getBossBar().setColor(BarColor.BLUE);
        CaptureBase.getInstance().getBoss().getBoss().setSpell(Spellcaster.Spell.DISAPPEAR);

    }

    @Override
    public void targetSkill() {

        //fireballs

        ArrayList<Player> players=getAllNearPlayers();

        if(players.isEmpty()) return;

        ArrayList<Player> targets=new ArrayList<>();

        for(int i=0;i<1+(players.size()/5);i++){
            targets.add(players.get((int)(Math.random()*1000)%players.size()));
        }

        for (Player player: targets
        ) {
            Location loc=player.getLocation();

            AreaEffectCloud areaEffectCloud=(AreaEffectCloud) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(loc, EntityType.AREA_EFFECT_CLOUD);
            areaEffectCloud.setColor(Color.RED);
            areaEffectCloud.setParticle(Particle.DAMAGE_INDICATOR);
            areaEffectCloud.setRadius(1.5F);
            areaEffectCloud.setDuration(60);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                loc.setY(loc.getY()+10);
                Vector vector=new Vector();
                vector.setY(-2);
                loc.setDirection(vector);
                Fireball fireball=(Fireball) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(loc,EntityType.FIREBALL);

                fireball.setYield(3F);
            },60);
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

                    EvokerFangs evokerFangs=(EvokerFangs) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location,EntityType.EVOKER_FANGS);

                    angle[0] += 40;
                }
            },2*i);
            i++;
            radius+=1;
        }

    }

    @Override
    public void summonSkill() {

        //vindicators

        ArrayList<Player> players=getAllNearPlayers();

        if(players.isEmpty()) return;

        for(int i=0;i<1+(players.size()/2);i++){
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(CaptureBase.getInstance().getBoss().getBoss().getLocation(),EntityType.VINDICATOR);
        }



    }

    @Override
    public CurrSkillSet shiftForward() {
        return new FourthSkillSet();
    }

    @Override
    public CurrSkillSet shiftBackwards() {
        return new SecondSkillSet();
    }

    @Override
    public int getCurrStageNumb() {
        return 3;
    }
}
