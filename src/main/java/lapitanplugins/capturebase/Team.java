package lapitanplugins.capturebase;

import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NoTeleportException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.*;

public class Team {

    private ArrayList<CaptureBasePlayer> players;
    private Map<Block, Block> teleports;
    private ArrayList<Block> classTeleports;
    private Block shop;
    private ArrayList<Flag> flags;
    private ArrayList<Block> mineMobs;
    private Integer currNumb;
    private Block respawn;
    private Block base;
    private Integer teamNetWorth;
    private ArrayList<Block> traps;
    private Color color;
    private String colorCode;
    private Team enemyTeam;

    public Team(Color color, String colorCode) {
        players = new ArrayList<>();
        teleports = new HashMap<>();
        classTeleports = new ArrayList<>();
        flags = new ArrayList<>();
        mineMobs = new ArrayList<>();
        traps = new ArrayList<>();
        this.color = color;
        this.colorCode = colorCode;
        currNumb=0;
        teamNetWorth=0;
    }

    public void addPlayer(CaptureBasePlayer player) {
        players.add(player);
    }

    public void removePlayer(CaptureBasePlayer player) {
        players.remove(player);
    }

    public void addTeleport(Block from, Block to) {
        teleports.remove(from);
        teleports.put(from, to);
    }

    public void removeTeleport(Block tp) {
        teleports.remove(tp);
    }

    public void addClassTeleport(Block block) {
        classTeleports.add(block);
    }

    public void removeClassTeleport(Block block) {
        classTeleports.remove(block);
    }

    public void addFlag(Block block, Block command) {

        Flag flag = new Flag(block, command, colorCode);
        flags.add(flag);

    }

    public void Teleport(CaptureBasePlayer player, Block block) throws CaptureBaseException {
        if (!teleports.containsKey(block)) throw new NoTeleportException("Это не телепорт");
        Block target = teleports.get(block);

        Location location = target.getLocation();
        location.setY(location.getY() + 1);

        player.getPlayer().teleport(location);
    }

    public void ClassTeleport(CaptureBasePlayer player) {
        Location location = classTeleports.get(player.getaClass().getNumber()).getLocation();
        location.setY(location.getY() + 1);

        player.getPlayer().teleport(location);
    }

    public void backFromShop(CaptureBasePlayer player) {
        Location location = shop.getLocation();
        location.setY(location.getY() + 1);

        player.getPlayer().teleport(location);
    }

    public Block getShop() {
        return shop;
    }

    public void setShop(Block shop) {
        this.shop = shop;
    }

    public Block getRespawn() {
        return respawn;
    }

    public void setRespawn(Block respawn) {
        this.respawn = respawn;
    }

    public Block getBase() {
        return base;
    }

    public void setBase(Block base) {
        this.base = base;
    }

    public Integer getTeamNetWorth() {
        return teamNetWorth;
    }

    public void setTeamNetWorth(Integer teamNetWorth) {
        this.teamNetWorth = teamNetWorth;
    }

    public void addTeamNetWorth(Integer points) {
        this.teamNetWorth += points;
    }

    public Flag getCurrFlag() {
        return flags.get(currNumb);
    }

    public Integer getCurrNumb() {
        return currNumb;
    }

    public void setCurrNumb(Integer currNumb) {
        this.currNumb = currNumb;
    }

    public ArrayList<CaptureBasePlayer> getPlayers() {
        return players;
    }

    public void addTrap(Block block) {
        traps.add(block);
    }

    public void removeTrap(Block block) {
        traps.remove(block);
    }

    public boolean trapCheck(Block block) {
        return traps.contains(block);
    }

    public Color getColor() {
        return color;
    }

    public Team getEnemyTeam() {
        return enemyTeam;
    }

    public void setEnemyTeam(Team enemyTeam) {
        this.enemyTeam = enemyTeam;
    }

    public boolean containsPlayer(CaptureBasePlayer player) {
        return players.contains(player);
    }

    public String getColorCode() {
        return colorCode;
    }

    public void flagUpdate() {

        if (flags.get(currNumb).update()) {

            currNumb++;

            if (currNumb.equals(flags.size())) {
                CaptureBase.getInstance().gameOver(enemyTeam, this);
                return;
            }

            for (CaptureBasePlayer p : players
            ) {
                p.getPlayer().sendTitle(enemyTeam.colorCode + "Враги Снесли Флаг", "Под номером " + currNumb, 10, 20, 10);
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1);

            }
        }

    }

    public void addMineMobSpawn(Block block) {
        mineMobs.add(block);
    }

    private EntityType getRandomMob() {
        EntityType entityType = EntityType.ZOMBIE;
        double rand = Math.random();
        if (rand > 0.25) {
            entityType = EntityType.SKELETON;
        }
        if (rand > 0.5) {
            entityType = EntityType.SPIDER;
        }
        if (rand > 0.75) {
            entityType = EntityType.CAVE_SPIDER;
        }

        return entityType;
    }

    public void doMobSpawn() {
        for (int i = 0; i < 15; i++) {
            Location randomLocation = mineMobs.get((int) (Math.random() * 1000) % mineMobs.size()).getLocation();
            randomLocation.setY(randomLocation.getY() + 1);
            Mob entity = (Mob) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(randomLocation, getRandomMob());
            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(), () -> {
                entity.setHealth(0);
            }, 3600);
        }
    }
}
