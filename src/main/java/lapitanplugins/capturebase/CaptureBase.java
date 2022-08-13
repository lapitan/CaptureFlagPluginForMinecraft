package lapitanplugins.capturebase;

import lapitanplugins.capturebase.Events.PlayerActionsBeforeStart;
import lapitanplugins.capturebase.Events.PlayerActionsWhileMainPart;
import lapitanplugins.capturebase.Events.PlayerActionsWhilePreparing;
import lapitanplugins.capturebase.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class CaptureBase extends JavaPlugin {

    private static CaptureBase instance;
    private ArrayList<CaptureBasePlayer> allPlayers;

    private Team blue;
    private Team red;

    private Block commandAfterPreparing;

    private boolean isStarted;
    private boolean isFullStarted;

    private int secondsPassed;

    private Boss boss;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance=this;
        isStarted=false;
        isFullStarted=false;
        secondsPassed=0;
        Boss boss= new Boss();
        allPlayers=new ArrayList<>();

        blue=new Team(Color.BLUE,"§9");
        red=new Team(Color.RED,"§4");

        blue.setEnemyTeam(red);
        red.setEnemyTeam(blue);

        Bukkit.getPluginManager().registerEvents(new PlayerActionsBeforeStart(),this);

        new TestCommand();
        new TestCommand2();
        new AddMineMobsSpawn();
        new AddTeamClassTeleportsCommand();
        new AddTeamFlagCommand();
        new AddTeamTeleportsCommand();
        new SetCommandAfterPreparingCommand();
        new SetPlayerClassCommand();
        new SetPlayerTeamCommand();
        new SetTeamSpawnCommand();
        new StartCommand();
        new SetBossSpawnCommand();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static CaptureBase getInstance(){
        return instance;
    }

    public Team getBlue() {
        return blue;
    }

    public void setBlue(Team blue) {
        this.blue = blue;
    }

    public Team getRed() {
        return red;
    }

    public void setRed(Team red) {
        this.red = red;
    }

    public void start(){

        if(isStarted) return;

        isStarted=true;
        Bukkit.getPluginManager().registerEvents(new PlayerActionsWhilePreparing(),this);
        Bukkit.getScheduler().scheduleSyncDelayedTask(this,()->{
            CaptureBase.getInstance().fullStart();
        },12000);
    }

    public void fullStart(){

        if(isFullStarted) return;

        isFullStarted=true;
        Bukkit.getPluginManager().registerEvents(new PlayerActionsWhileMainPart(), this);

        commandAfterPreparing.setType(Material.REDSTONE_BLOCK);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            commandAfterPreparing.setType(Material.REDSTONE_LAMP);
        },20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this,()->{
            secondsPassed++;
            doTick();
        },0,20);

    }

    public void addPlayer(CaptureBasePlayer player){
        allPlayers.add(player);
    }

    public void gameOver(Team winner, Team loser){

        

    }

    public CaptureBasePlayer getCaptureBasePlayer(Player player){
        CaptureBasePlayer captureBasePlayer=null;
        for (CaptureBasePlayer p: allPlayers
             ) {
            if(p.equals(player)){
                captureBasePlayer=p;
                break;
            }
        }
        return captureBasePlayer;
    }

    public boolean containsPlayer(Player player){
        for (CaptureBasePlayer p: allPlayers
        ) {
            if(p.equals(player)){
                return true;
            }
        }
        return false;
    }

    private void doTick(){
        for (CaptureBasePlayer player: allPlayers
             ) {
            player.doTick();
        }
        doBossTick();
        doBossRespawn();
    }
    private void doBossTick(){
        if(!boss.isBossSpawned()) return;
        boss.regen();
        if(secondsPassed%13==0) boss.targetSkill();
        if(secondsPassed%7==0) boss.radiusSkill();
        if(secondsPassed%23==0) boss.summonSkill();
    }

    private void doBossRespawn(){
        if(boss.isBossSpawned()) return;
        if(secondsPassed!=boss.getBossRespawn()) return;
        boss.spawnBoss();

    }

    public ArrayList<CaptureBasePlayer> getAllPlayers(){
        return allPlayers;
    }

    public void setCommandAfterPreparing(Block commandAfterPreparing) {
        this.commandAfterPreparing = commandAfterPreparing;
    }

    public void setBossSpawn(Block bossSpawn) {
        this.boss.setBossSpawn(bossSpawn);
    }

    public Boss getBoss() {
        return boss;
    }

    public boolean isBossSpawned() {
        return boss.isBossSpawned();
    }

    public boolean isStarted(){
        return isStarted;
    }

    public boolean isFullStarted(){
        return isFullStarted;
    }

    public int getSecondsPassed() {
        return secondsPassed;
    }
}
