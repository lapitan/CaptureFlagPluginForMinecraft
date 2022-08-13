package lapitanplugins.capturebase.Events;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class PlayerActionsWhilePreparing implements Listener {

    private void giveItemsAndSetBlock(Block block, Player player,Material giveItem, int multiplier,Material setBlock, int delay){
        block.setType(Material.BLACKSTONE);
        int fortuneLevel=Objects.requireNonNull(player.getActiveItem()).getEnchantmentLevel(Enchantment.LUCK);
        int amountOfItems=1;
        for (int i=0;i<fortuneLevel;i++){
            if(Math.random()>0.5){
                amountOfItems++;
            }
        }

        player.getInventory().addItem(new ItemStack(giveItem,amountOfItems));

        CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(player);
        captureBasePlayer.addNetWorth(amountOfItems*multiplier);

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            block.setType(setBlock);
        },delay);

    }

    @EventHandler
    public void blockBreak(BlockBreakEvent e){
        if(CaptureBase.getInstance().isFullStarted()) return;

        Block block=e.getBlock();

        if(block.getType()== Material.IRON_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.IRON_INGOT, 1, Material.IRON_ORE,600);
        }
        else if(block.getType()==Material.GOLD_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.GOLD_INGOT, 2, Material.GOLD_ORE,800);
        }
        else if(block.getType()==Material.DIAMOND_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.DIAMOND, 4, Material.DIAMOND_ORE,1200);
        }
        else {
            if (block.getType() != Material.BLUE_WOOL && block.getType() != Material.RED_WOOL && block.getType()!= Material.FIRE) {
               block.setType(block.getType());
            }
        }
    }

    @EventHandler
    public void rightClick(PlayerInteractEvent e){
        if(CaptureBase.getInstance().isFullStarted()) return;

        Material block= Objects.requireNonNull(e.getClickedBlock()).getType();

        if(e.getAction()== Action.RIGHT_CLICK_BLOCK && block== Material.BLUE_CONCRETE){

            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            if(!CaptureBase.getInstance().getBlue().containsPlayer(captureBasePlayer)) return;

            try {
                CaptureBase.getInstance().getBlue().Teleport(captureBasePlayer,e.getClickedBlock());
            } catch (CaptureBaseException captureBaseException) {
                captureBasePlayer.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;

        }
        if(e.getAction()==Action.RIGHT_CLICK_BLOCK && block== Material.RED_CONCRETE){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            if(!CaptureBase.getInstance().getRed().containsPlayer(captureBasePlayer)) return;

            try {
                CaptureBase.getInstance().getRed().Teleport(captureBasePlayer,e.getClickedBlock());
            } catch (CaptureBaseException captureBaseException) {
                captureBasePlayer.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }

        if(e.getAction()==Action.RIGHT_CLICK_BLOCK && (block== Material.CHEST||block==Material.SHULKER_BOX||block==Material.BARREL||block==Material.FURNACE||block==Material.CRAFTING_TABLE||block==Material.SMITHING_TABLE||block==Material.ENCHANTING_TABLE||block==Material.BREWING_STAND||block==Material.DROPPER||block==Material.DISPENSER||block==Material.TRAPPED_CHEST||block==Material.CHEST_MINECART)){
            e.setCancelled(true);
        }

    }

    public void lossPoints(Player player, ItemStack itemStack){

        int points=1;

        if(itemStack.getType()==Material.GOLD_INGOT) points=2;
        if(itemStack.getType()==Material.DIAMOND) points=4;
        if(itemStack.getType()==Material.NETHERITE_INGOT) points=8;
        if(itemStack.getType()==Material.NETHER_STAR) points=200;


        CaptureBase.getInstance().getCaptureBasePlayer(player).getTeam().addTeamNetWorth(points*(-1));
        CaptureBase.getInstance().getCaptureBasePlayer(player).addNetWorth(points*(-1));

    }

    @EventHandler
    public void playerDrop(PlayerDropItemEvent e){
        Material material=e.getItemDrop().getItemStack().getType();
        if(material==Material.IRON_INGOT||material==Material.GOLD_INGOT||material==Material.DIAMOND||material==Material.NETHERITE_INGOT||material==Material.NETHER_STAR){
            lossPoints(e.getPlayer(),e.getItemDrop().getItemStack());
            return;
        }
        e.setCancelled(true);
    }

    public void gainPoints(Player player, ItemStack itemStack){

        int points=1;

        if(itemStack.getType()==Material.GOLD_INGOT) points=2;
        if(itemStack.getType()==Material.DIAMOND) points=4;
        if(itemStack.getType()==Material.NETHERITE_INGOT) points=8;
        if(itemStack.getType()==Material.NETHER_STAR) points=200;


        CaptureBase.getInstance().getCaptureBasePlayer(player).getTeam().addTeamNetWorth(points*itemStack.getAmount());
        CaptureBase.getInstance().getCaptureBasePlayer(player).addNetWorth(points*itemStack.getAmount());

    }

    @EventHandler
    public void playerPickUp(EntityPickupItemEvent e){
        if(!(e.getEntity() instanceof Player)){
            e.setCancelled(true);
            return;
        }
        Player player=(Player) e.getEntity();

        Material material=e.getItem().getItemStack().getType();
        if(material==Material.IRON_INGOT||material==Material.GOLD_INGOT||material==Material.DIAMOND||material==Material.NETHERITE_INGOT||material==Material.NETHER_STAR){
            gainPoints(player,e.getItem().getItemStack());
            return;
        }
        e.setCancelled(true);
    }

    @EventHandler
    public void playerDied(PlayerDeathEvent e){

        if(e.getEntity().getKiller()==null) return;

        CaptureBasePlayer dead=CaptureBase.getInstance().getCaptureBasePlayer(e.getEntity());
        CaptureBasePlayer killer=CaptureBase.getInstance().getCaptureBasePlayer(e.getEntity().getKiller());

        killer.getReward(dead);
    }

    @EventHandler
    public void playerRespawn(PlayerRespawnEvent e){
        CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).respawn();
    }

}
