package lapitanplugins.capturebase.Events;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerActionsWhileMainPart implements Listener {

    private void giveItemsAndSetBlock(Block block, Player player, Material giveItem, int multiplier, Material setBlock, int delay){
        block.setType(Material.BLACKSTONE);
        int fortuneLevel= Objects.requireNonNull(player.getActiveItem()).getEnchantmentLevel(Enchantment.LUCK);
        int amountOfItems=1;
        for (int i=0;i<fortuneLevel;i++){
            if(Math.random()>0.5){
                amountOfItems++;
            }
        }

        player.getInventory().addItem(new ItemStack(giveItem,amountOfItems));

        CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(player);
        captureBasePlayer.addNetWorth(amountOfItems*multiplier);

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()-> block.setType(setBlock),delay);

    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e){

        Block block=e.getBlock();

        if(block.getType()== Material.IRON_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.IRON_INGOT, 1, Material.IRON_ORE,600);
            return;
        }
        if(block.getType()==Material.GOLD_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.GOLD_INGOT, 2, Material.GOLD_ORE,800);
            return;
        }
        if(block.getType()==Material.DIAMOND_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.DIAMOND, 4, Material.DIAMOND_ORE,1200);
            return;
        }
        if(block.getType()==Material.ANCIENT_DEBRIS){
            giveItemsAndSetBlock(block,e.getPlayer(), Material.NETHERITE_INGOT, 8,Material.ANCIENT_DEBRIS, 1200);
        }
        if(block.getType()==Material.END_STONE){
           if(!CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).getTeam().getEnemyTeam().getCurrFlag().equals(block)){
               e.getPlayer().sendMessage("Ты чета не туда воюешь");
               return;
           }
           CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).getTeam().getEnemyTeam().flagUpdate();
        }
        if (block.getType() != Material.BLUE_WOOL && block.getType() != Material.RED_WOOL) {
            block.setType(block.getType());
        }
    }

    @EventHandler
    public void playerAbilities(PlayerInteractEvent e){

        if(e.getHand()== EquipmentSlot.OFF_HAND){
            return;
        }

        if(e.getItem()==null){
            return;
        }

        if(e.getItem().getType()==Material.BLAZE_ROD){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            try {
                captureBasePlayer.getaClass().skill1(captureBasePlayer, null);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }
        if(e.getItem().getType()==Material.MAGMA_CREAM){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            try {
                captureBasePlayer.getaClass().skill2(captureBasePlayer, null);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }
        if(e.getItem().getType()==Material.GHAST_TEAR){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            Object[] objects =new Object[1];
            objects[0]=e.getClickedBlock();

            try {
                captureBasePlayer.getaClass().skill2(captureBasePlayer, objects);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }
        if(e.getItem().getType()==Material.BONE){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            try {
                captureBasePlayer.getaClass().skill2(captureBasePlayer, null);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }
        if(e.getItem().getType()==Material.NETHER_STAR){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            try {
                captureBasePlayer.getaClass().skill2(captureBasePlayer, null);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }

        }

    }

    @EventHandler
    public void arrowsAndTrident(ProjectileHitEvent e){
        Block b=e.getHitBlock();

        if(b==null){
            return;
        }

        if(e.getEntity().getType()== EntityType.TRIDENT) {
            Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(e.getHitBlock().getLocation(),EntityType.LIGHTNING);
        }

    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if (e.getDamager() instanceof Trident){
                Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(e.getEntity().getLocation(),EntityType.LIGHTNING);
            }
        }

        if((CaptureBase.getInstance().getBoss().isBossSpawned())&&(e.getEntity().equals(CaptureBase.getInstance().getBoss().getBoss()))){

            CaptureBase.getInstance().getBoss().updateBossBar();
        }


    }

    @EventHandler
    public void trapActivate(PlayerInteractEvent e){
        if(e.getAction().equals(Action.PHYSICAL)&& Objects.requireNonNull(e.getClickedBlock()).getType()==Material.POLISHED_BLACKSTONE_PRESSURE_PLATE){

            CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).trapCheck(e.getClickedBlock());

        }
    }


}
