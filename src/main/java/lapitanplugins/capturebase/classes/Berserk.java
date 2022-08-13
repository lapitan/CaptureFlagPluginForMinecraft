package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Berserk implements AbstractClass {

    int tpCount;

    public Berserk(){
        tpCount=0;
    }

    @Override
    public void skill1(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<50){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=50;
        player.getPlayer().setLevel(l);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,100,4));
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);
    }

    @Override
    public void skill2(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    @Override
    public void skill3(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<25){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=25;
        player.getPlayer().setLevel(l);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,80,7));
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_DOLPHIN_SPLASH,0.5F,1);
    }

    @Override
    public void skill4(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    @Override
    public void ultimate(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException {

        player.getPlayer().teleport(player.getTeam().getEnemyTeam().getPlayers().get((int)(Math.random()*1000)%player.getTeam().getEnemyTeam().getPlayers().size()).getPlayer().getLocation());
        tpCount++;

        if (tpCount==5){
            ItemStack items= player.getPlayer().getInventory().getItemInMainHand();
            if(items.getAmount()>1){
                items.setAmount(items.getAmount()-1);
            }else {
                items= new ItemStack(Material.AIR);
            }
            player.getPlayer().getInventory().setItemInMainHand(items);
            tpCount=0;
        }



    }

    @Override
    public void doTick(CaptureBasePlayer player) {
        int level=player.getPlayer().getLevel();
        if (!(level >= 100)) level+=1;
        player.getPlayer().setLevel(level);

        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,40,1));
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
        leatherArmorMeta.setColor(Color.ORANGE);
        helmet.setItemMeta(leatherArmorMeta);
        player.getPlayer().getInventory().setHelmet(helmet);
    }

    @Override
    public int getNumber() {
        return 2;
    }
}
