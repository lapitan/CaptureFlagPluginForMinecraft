package lapitanplugins.capturebase;

import lapitanplugins.capturebase.boss.CurrSkillSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Evoker;
import org.bukkit.entity.Item;
import org.bukkit.entity.Spellcaster;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Boss {

    private int bossRespawn;
    private boolean bossSpawned;

    protected Evoker evoker;
    protected BossBar bossBar;

    private Block bossSpawn;

    private CurrSkillSet skillSet;



    public Boss() {
        bossRespawn=600;
        bossSpawned=false;
    }

    public int getBossRespawn() {
        return bossRespawn;
    }

    public void setBossRespawn(int bossRespawn) {
        this.bossRespawn = bossRespawn;
    }

    public boolean isBossSpawned() {
        return bossSpawned;
    }

    public void setBossSpawned(boolean bossSpawned) {
        this.bossSpawned = bossSpawned;
    }

    public Evoker getBoss() {
        return evoker;
    }

    public void setBoss(Evoker boss) {
        this.evoker = boss;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public void setBossBar(BossBar bossBar) {
        this.bossBar = bossBar;
    }

    public Block getBossSpawn() {
        return bossSpawn;
    }

    public void setBossSpawn(Block bossSpawn) {
        this.bossSpawn = bossSpawn;
    }

    public void spawnBoss(){

        Location location=bossSpawn.getLocation();
        location.setY(bossSpawn.getY()+1);

        evoker= (Evoker) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location, EntityType.EVOKER);
        evoker.setCustomName("Boss Of The GYM");
        AttributeInstance attributeInstance= evoker.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(attributeInstance).setBaseValue(900.0D+ (int)((CaptureBase.getInstance().getSecondsPassed()/6.0))*2);
        evoker.setSpell(Spellcaster.Spell.NONE);

        bossBar=Bukkit.createBossBar("Boss КАЧАЛКИ", BarColor.GREEN, BarStyle.SEGMENTED_6);
        bossBar.setProgress(evoker.getHealth()/ Objects.requireNonNull(evoker.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        bossBar.setVisible(true);

        CaptureBase.getInstance().getAllPlayers().forEach(player -> bossBar.addPlayer(player.getPlayer()));

        bossSpawned=true;
    }

    public void updateBossBar(){
        //GREEN WHITE BLUE PURPLE YELLOW RED
        //NONE SUMMON_VEX DISAPPEAR FANGS WOLOLO BLINDNESS

        bossBar.setProgress(evoker.getHealth()/ Objects.requireNonNull(evoker.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());

        if(evoker.getHealth()== Objects.requireNonNull(evoker.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()) return;

        if(evoker.getHealth()/6>=skillSet.getCurrStageNumb()){
            skillSet= skillSet.shiftBackwards();
            return;
        }
        if(evoker.getHealth()/6<=skillSet.getCurrStageNumb()){
            skillSet= skillSet.shiftForward();
        }
        if(evoker.isDead()){
            onDeath();
        }


    }

    public void onDeath(){
        Location location= evoker.getLocation();
        Item item= (Item) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location,EntityType.DROPPED_ITEM);
        item.setItemStack(new ItemStack(Material.NETHER_STAR,1));

        bossRespawn=CaptureBase.getInstance().getSecondsPassed()+300+(int)(Math.random()*10000)%300;
        bossSpawned=false;
    }

    public void regen(){
        evoker.setHealth(evoker.getHealth()+1);
        updateBossBar();
    }

    public void targetSkill(){
        skillSet.targetSkill();
    }

    public void radiusSkill(){
        skillSet.radiusSkill();
    }

    public void summonSkill(){
        skillSet.summonSkill();
    }

}
