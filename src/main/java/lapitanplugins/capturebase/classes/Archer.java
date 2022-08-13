package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class Archer implements AbstractClass {

    @Override
    public void skill1(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

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
        int l=player.getPlayer().getLevel();
        if(l<25){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=25;
        player.getPlayer().setLevel(l);

        Wolf wolf= (Wolf) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(player.getPlayer().getLocation(), EntityType.WOLF);
        wolf.setOwner(player.getPlayer());

        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5F,1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(), () -> {
            wolf.setHealth(0);
            player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_WOLF_DEATH,0.5F,1);
        },400);
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

        Inventory inventory=Bukkit.createInventory(player.getPlayer(), 36);

        ArrayList<CaptureBasePlayer> players=player.getTeam().getEnemyTeam().getPlayers();

        for (CaptureBasePlayer p: players
        ) {
            ItemStack itemStack=new ItemStack(Material.PAPER,1);
            ItemMeta itemMeta=itemStack.getItemMeta();

            Component component = Component.text(p.getPlayer().getName());
            itemMeta.displayName(component);

            itemStack.setItemMeta(itemMeta);
            inventory.addItem(itemStack);
        }

        player.getPlayer().openInventory(inventory);

    }

    @Override
    public void doTick(CaptureBasePlayer player) {
        int level=player.getPlayer().getLevel();
        if (!(level >= 100)) level+=1;
        player.getPlayer().setLevel(level);

        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,10,1));
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
        leatherArmorMeta.setColor(Color.GREEN);
        helmet.setItemMeta(leatherArmorMeta);
        player.getPlayer().getInventory().setHelmet(helmet);

    }

    @Override
    public int getNumber() {
        return 4;
    }
}
