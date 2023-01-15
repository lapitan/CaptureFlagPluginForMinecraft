package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Mage implements AbstractClass {

    @Override
    public void skill1(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<20){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=20;
        player.getPlayer().setLevel(l);

        Location loc=player.getPlayer().getLocation();
        loc.setY(loc.getY()+2);
        Fireball fireBall= (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
        fireBall.setVelocity(loc.getDirection());
        fireBall.setYield(1.0F);
        fireBall.setShooter(player.getPlayer());
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);
    }

    @Override
    public void skill2(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

        int l=player.getPlayer().getLevel();
        if(l<100){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=100;
        player.getPlayer().setLevel(l);
        Location loc=player.getPlayer().getLocation();
        loc.setY(loc.getY()+2);
        Fireball fireBall= (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
        fireBall.setVelocity(loc.getDirection());
        fireBall.setYield(10.0F);
        fireBall.setShooter(player.getPlayer());
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_HURT,0.5F,1);

    }

    @Override
    public void skill3(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<50){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=50;
        player.getPlayer().setLevel(l);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING,200,1));
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP,200,10));
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_DOLPHIN_SPLASH,0.5F,1);
    }

    @Override
    public void skill4(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<20){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=20;
        player.getPlayer().setLevel(l);

        Location loc=player.getPlayer().getLocation();
        loc.setY(loc.getY()+2);
        WitherSkull witherSkull= (WitherSkull) loc.getWorld().spawnEntity(loc, EntityType.WITHER_SKULL);
        witherSkull.setVelocity(loc.getDirection());
        witherSkull.setCharged(false);
        witherSkull.setYield(1.0F);
        witherSkull.setCustomName(player.getPlayer().getName());
        witherSkull.setShooter(player.getPlayer());
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_WITHER_SHOOT,0.5F,1);
    }

    @Override
    public void ultimate(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException {

        ItemStack items= player.getPlayer().getInventory().getItemInMainHand();
        if(items.getAmount()>1){
            items.setAmount(items.getAmount()-1);
        }else {
            items= new ItemStack(Material.AIR);
        }
        player.getPlayer().getInventory().setItemInMainHand(items);

        Location loc=player.getPlayer().getLocation();

        loc.setY(loc.getY()+2);

        Block block = Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt((int )loc.getX(),(int) loc.getY(),(int) loc.getZ());
        block.setType(Material.ICE);

        BlockData blockData=block.getBlockData();
        block.setType(Material.AIR);
        FallingBlock fallingBlock=(FallingBlock) Objects.requireNonNull(Bukkit.getWorld("world")).spawnFallingBlock(loc,blockData);
        fallingBlock.setVelocity(loc.getDirection().multiply(2));
        fallingBlock.setGravity(false);

        fallingBlock.setCustomName(player.getPlayer().getName());

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            fallingBlock.setGravity(true);
        },60);

    }

    @Override
    public void doTick(CaptureBasePlayer player) {
        int level=player.getPlayer().getLevel();
        if (!(level >= 200)) level+=2;
        player.getPlayer().setLevel(level);

        Block flag=player.getTeam().getCurrFlag().getBlock();
        int distance= (int) Math.sqrt(Math.pow(player.getPlayer().getLocation().getX()-flag.getX(),2)+Math.pow(player.getPlayer().getLocation().getZ()-flag.getZ(),2));
        if(distance<=20){
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,40,1));
        }
        highlight(player);
    }

    @Override
    public void doSpawnThings(CaptureBasePlayer player, Object[] someObjects) {
        ItemStack helmet=new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta leatherArmorMeta=(LeatherArmorMeta) helmet.getItemMeta();
        leatherArmorMeta.setColor(Color.AQUA);
        helmet.setItemMeta(leatherArmorMeta);
        player.getPlayer().getInventory().setHelmet(helmet);
    }

    @Override
    public int getNumber() {
        return 0;
    }
}
