package lapitanplugins.capturebase;

import lapitanplugins.capturebase.classes.AbstractClass;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CaptureBasePlayer {

    Player player;
    AbstractClass aClass;
    Integer killStreak;
    Integer totalKills;
    Integer netWorth;
    Integer deaths;
    Team team;

    boolean isStunned=false;

    @Override
    public boolean equals(Object player2){
        if(player2 instanceof CaptureBasePlayer) {
            CaptureBasePlayer newPlayer = (CaptureBasePlayer) player2;
            return newPlayer.player.equals(player);
        }
        if(player2 instanceof Player){
            Player newPlayer=(Player) player2;
            return newPlayer.equals(player);
        }
        return false;
    }

    public CaptureBasePlayer(Player player) {
        this.player = player;
        killStreak=0;
        totalKills=0;
        netWorth=0;
        deaths=0;
    }

    public Player getPlayer() {
        return player;
    }

    public AbstractClass getaClass() {
        return aClass;
    }

    public void setaClass(AbstractClass aClass) {
        this.aClass = aClass;
    }

    public Integer getKillStreak() {
        return killStreak;
    }

    public void setKillStreak(Integer killStreak) {
        this.killStreak = killStreak;
    }

    public Integer getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(Integer netWorth) {
        this.netWorth = netWorth;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Integer getTotalKills() {
        return totalKills;
    }

    public void setTotalKills(Integer totalKills) {
        this.totalKills = totalKills;
    }

    public void getReward(CaptureBasePlayer killedPlayer){
        //base 10 iron 6 gold 2 diamonds =30
        int ironAmount=10;
        int goldAmount=6;
        int diamondsAmount=2;
        int netheriteAmount=0;
        //for each kill in killStreak 11 points
        int points=killedPlayer.getKillStreak()*11;
        ironAmount+=points/4;
        goldAmount+=points/8;
        diamondsAmount+=points/16;
        netheriteAmount+=points/32;
        killedPlayer.setKillStreak(0);
        killedPlayer.deaths+=1;
        //for each 10 difference in netWorth give 1 point
        points=(team.getTeamNetWorth()-killedPlayer.getTeam().getTeamNetWorth())/10;
        ironAmount+=points/4;
        goldAmount+=points/8;
        diamondsAmount+=points/16;
        netheriteAmount+=points/32;

        ItemStack[] itemStacks={new ItemStack(Material.IRON_INGOT,ironAmount),new ItemStack(Material.GOLD_INGOT,goldAmount),new ItemStack(Material.DIAMOND,diamondsAmount),new ItemStack(Material.NETHERITE_INGOT,netheriteAmount)};
        player.getInventory().addItem(itemStacks);

        int netWorthAdd = ironAmount + goldAmount * 2 + diamondsAmount * 4 + netheriteAmount * 8;
        addNetWorth(netWorthAdd);
        killStreak+=1;
        totalKills+=1;
    }

    public void doTick(){
        aClass.doTick(this);
    }

    public void respawn(){
        Location location=team.getRespawn().getLocation();
        location.setY(location.getY()+1);

        Location finalLocation = location;
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()-> player.teleport(finalLocation),1);

        int respawnTime=30;

        respawnTime+=team.getEnemyTeam().getCurrNumb()*5;

        location=team.getBase().getLocation();
        location.setY(location.getY()+1);

        player.setGameMode(GameMode.SPECTATOR);

        for(int i=respawnTime;i>0;i--){
            int timeLeft=respawnTime-i;
            int finalI = i;
            String colorCode="§4";
            if(finalI<respawnTime/2){
                colorCode="§6";
            }
            if(finalI<5){
                colorCode="§2";
            }
            String finalColorCode = colorCode;
            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                player.sendTitle(finalColorCode + timeLeft,"",5,10,5);
                if(finalI <5){
                    player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);
                }
            },20*(respawnTime-i));
        }
        Location finalLocation1 = location;
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            player.sendTitle("иди дерись хуй","",5,10,5);
            player.teleport(finalLocation1);
            player.setGameMode(GameMode.SURVIVAL);
            aClass.doSpawnThings(this,null);
        },20*respawnTime);
    }

    public void trapCheck(Block block){

        if(!team.getEnemyTeam().trapCheck(block)) return;

        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,200,1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,200,1));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,200,1));
        player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SCREAM, 0.5F,1);

        block.setType(Material.AIR);

    }

    public boolean isStunned() {
        return isStunned;
    }

    public void setStunned(boolean stunned) {
        isStunned = stunned;
    }

    public void addNetWorth(int points){
        netWorth+=points;
        team.addTeamNetWorth(points);
    }
}
