package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import lapitanplugins.capturebase.exceptions.WrongClickedBlockException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class Ninja implements AbstractClass {

    @Override
    public void skill1(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<50){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=50;
        player.getPlayer().setLevel(l);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,60,3));
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);
    }

    @Override
    public void skill2(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<50){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=50;
        player.getPlayer().setLevel(l);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,1200,1));
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);
    }

    @Override
    public void skill3(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException, WrongClickedBlockException {

        if(someObjects[0]==null){
            throw new WrongClickedBlockException("You Should click on any block");
        }

        if(!(someObjects[0] instanceof Block)) return;
        Block block=(Block) someObjects[0];

        if(block.getType()==Material.POLISHED_BLACKSTONE_PRESSURE_PLATE) return;

        if(!Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(block.getX(),block.getY()+1,block.getZ()).getType().equals(Material.AIR)){
            throw new WrongClickedBlockException("Сверху должен быть Воздух еблан");
        }

        int l=player.getPlayer().getLevel();
        if(l<20){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=20;
        player.getPlayer().setLevel(l);

        Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(block.getX(),block.getY()+1, block.getZ()).setType(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);

        Block plate= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(block.getX(),block.getY()+1, block.getZ());

        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            player.getTeam().addTrap(plate);
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE,0.5F,1);
        },100);
    }

    @Override
    public void skill4(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    boolean isPlayerNear(Player p){
        return Math.sqrt(Math.pow(p.getLocation().getX()- CaptureBase.getInstance().getBoss().getBossSpawn().getX(),2)+Math.pow(p.getLocation().getZ()-CaptureBase.getInstance().getBoss().getBossSpawn().getZ(),2)+Math.pow(p.getLocation().getY()-CaptureBase.getInstance().getBoss().getBossSpawn().getY(),2))<25;
    }

    @Override
    public void ultimate(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException {
        //smoke

        ArrayList<Player> nearEnemyPlayers=new ArrayList<>();

        for (CaptureBasePlayer p:player.getTeam().getEnemyTeam().getPlayers()
             ) {
            if( isPlayerNear(p.getPlayer())) nearEnemyPlayers.add(p.getPlayer());
        }

        if(nearEnemyPlayers.size()<1) throw new CaptureBaseException("no near enemy players");

        ItemStack items= player.getPlayer().getInventory().getItemInMainHand();
        if(items.getAmount()>1){
            items.setAmount(items.getAmount()-1);
        }else {
            items= new ItemStack(Material.AIR);
        }
        player.getPlayer().getInventory().setItemInMainHand(items);

        for (Player p: nearEnemyPlayers
             ) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,600,5));
            p.playSound(p.getLocation(),Sound.ENTITY_GHAST_SCREAM,0.5F,1F);
        }

    }

    @Override
    public void doTick(CaptureBasePlayer player) {
        int level=player.getPlayer().getLevel();
        if (!(level >= 100)) level+=1;
        player.getPlayer().setLevel(level);

        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,40,3));
        Block flag=player.getTeam().getCurrFlag().getBlock();
        int distance= (int) Math.sqrt(Math.pow(player.getPlayer().getLocation().getX()-flag.getX(),2)+Math.pow(player.getPlayer().getLocation().getZ()-flag.getZ(),2));
        if(distance<=20){
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,40,1));
        }
        highlight(player);
    }

    @Override
    public void doSpawnThings(CaptureBasePlayer player, Object[] someObjects) {

    }

    @Override
    public int getNumber() {
        return 1;
    }

    @Override
    public void highlight(CaptureBasePlayer player){
        if(player.getKillStreak()>=10){
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,5,1));
        }
    }
}
