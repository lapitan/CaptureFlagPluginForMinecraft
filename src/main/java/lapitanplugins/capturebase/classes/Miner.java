package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import lapitanplugins.capturebase.exceptions.WrongClickedBlockException;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class Miner implements AbstractClass {

    @Override
    public void skill1(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    @Override
    public void skill2(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        //teleport ally

        int l=player.getPlayer().getLevel();
        if(l<200){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=200;

        Inventory inventory=Bukkit.createInventory(player.getPlayer(), 36);

        ArrayList<CaptureBasePlayer> players=player.getTeam().getPlayers();

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
    public void skill3(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException, WrongClickedBlockException, CaptureBaseException {
        //heal flag
        int l=player.getPlayer().getLevel();
        if(l<10){
            throw new NotEnoughManaException("not enough mana");
        }

        if(someObjects[0]==null){
            throw new WrongClickedBlockException("You Should click on any block");
        }

        if(!(someObjects[0] instanceof Block)) return;
        Block block=(Block) someObjects[0];

        if(block.getType()!= Material.END_STONE){
            throw new WrongClickedBlockException("You should Click on Flag");
        }

        if(!player.getTeam().getCurrFlag().getBlock().equals(block)){
            throw new WrongClickedBlockException("You should Click on your Current Flag");
        }

        if(player.getTeam().getCurrFlag().getHP()>=50){
            throw new CaptureBaseException("Ты не можешь лечить флаги больше фула еблан");
        }

        l-=10;
        player.getPlayer().setLevel(l);

        ItemStack items= player.getPlayer().getInventory().getItemInMainHand();
        if(items.getAmount()>1){
            items.setAmount(items.getAmount()-1);
        }else {
            items= new ItemStack(Material.AIR);
        }
        player.getPlayer().getInventory().setItemInMainHand(items);

        player.getTeam().getCurrFlag().setHP(player.getTeam().getCurrFlag().getHP()+2);
        player.getTeam().flagUpdate();

        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.BLOCK_ANVIL_USE,0.5F,1);
    }

    private EntityType getRandomMob(){
        EntityType entityType=EntityType.ZOMBIE;
        double rand=Math.random();
        if(rand>0.25){
            entityType=EntityType.SKELETON;
        }
        if(rand>0.5){
            entityType=EntityType.CAVE_SPIDER;
        }
        if(rand>0.75){
            entityType=EntityType.STRAY;
        }

        return entityType;
    }

    @Override
    public void skill4(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        //attack of the dead
        int l=player.getPlayer().getLevel();
        if(l<200){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=200;
        ArrayList<CaptureBasePlayer> players= CaptureBase.getInstance().getRed().getPlayers();
        if(player.getTeam().equals(CaptureBase.getInstance().getRed())){
            players= CaptureBase.getInstance().getBlue().getPlayers();
        }

        for (CaptureBasePlayer p:players
        ) {
            EntityType entityType=getRandomMob();
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(p.getPlayer().getLocation(), entityType);
        }
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

        player.getTeam().getCurrFlag().setHP(101);
        player.getTeam().flagUpdate();

    }

    @Override
    public void doTick(CaptureBasePlayer player) {
        int level=player.getPlayer().getLevel();
        if (!(level >= 200)) level+=1;
        player.getPlayer().setLevel(level);

        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,40,2));
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
        leatherArmorMeta.setColor(Color.GRAY);
        helmet.setItemMeta(leatherArmorMeta);
        player.getPlayer().getInventory().setHelmet(helmet);
    }

    @Override
    public int getNumber() {
        return 5;
    }
}
