package lapitanplugins.capturebase.Events;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.classes.Archer;
import lapitanplugins.capturebase.classes.Miner;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

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
        }
        if(block.getType()==Material.GOLD_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.GOLD_INGOT, 2, Material.GOLD_ORE,800);
        }
        if(block.getType()==Material.DIAMOND_ORE){
            giveItemsAndSetBlock(block, e.getPlayer(), Material.DIAMOND, 4, Material.DIAMOND_ORE,1200);
        }
        if(block.getType()==Material.ANCIENT_DEBRIS){
            giveItemsAndSetBlock(block,e.getPlayer(), Material.NETHERITE_INGOT, 8,Material.ANCIENT_DEBRIS, 1200);
        }
        if(block.getType()==Material.END_STONE){
           if(!CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).getTeam().getEnemyTeam().getCurrFlag().equals(block)){
               e.getPlayer().sendMessage("Ты чета не туда воюешь");
               e.setCancelled(true);
               return;
           }
           CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).getTeam().getEnemyTeam().flagUpdate();
        }
        if (block.getType() != Material.BLUE_WOOL && block.getType() != Material.RED_WOOL) {
            e.setCancelled(true);
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
                captureBasePlayer.getaClass().skill3(captureBasePlayer, objects);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }
        if(e.getItem().getType()==Material.BONE){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            try {
                captureBasePlayer.getaClass().skill4(captureBasePlayer, null);
            } catch (CaptureBaseException captureBaseException) {
                e.getPlayer().sendMessage(captureBaseException.getMessage());
            }
            return;
        }
        if(e.getItem().getType()==Material.NETHER_STAR){
            CaptureBasePlayer captureBasePlayer=CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer());

            try {
                captureBasePlayer.getaClass().ultimate(captureBasePlayer, null);
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

    @EventHandler
    public void playerDamage(EntityDamageEvent e){
        if (e.getEntityType()!=EntityType.EVOKER) return;
        Bukkit.broadcast(Component.text("damage"));
        CaptureBase.getInstance().getBoss().updateBossBar();
    }

    @EventHandler
    public void entityDies(EntityDeathEvent e){
        if (e.getEntityType()!=EntityType.EVOKER) return;
        Bukkit.broadcast(Component.text("damage"));
        CaptureBase.getInstance().getBoss().onDeath();
    }

    @EventHandler
    public void playerChooseItemInInventory(InventoryClickEvent e){

        if(e.getClickedInventory()==null) return;

        if(!(e.getInventory().getHolder() instanceof Player)) return;

        Player holder=(Player) e.getInventory().getHolder();

        CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(holder);

        if(captureBasePlayer.getaClass() instanceof Miner){
            if(Objects.requireNonNull(e.getCurrentItem()).getType()== Material.PAPER){

                TextComponent textComponent= (TextComponent) e.getCurrentItem().getItemMeta().displayName();

                Objects.requireNonNull(Bukkit.getServer().getPlayer(Objects.requireNonNull(textComponent).content())).teleport(holder.getLocation());

                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }
            return;
        }
        if (captureBasePlayer.getaClass() instanceof Archer){
            if(Objects.requireNonNull(e.getCurrentItem()).getType()== Material.PAPER){

                TextComponent textComponent= (TextComponent) e.getCurrentItem().getItemMeta().displayName();

                Arrow arrow = (Arrow) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(Objects.requireNonNull(Bukkit.getPlayer(Objects.requireNonNull(textComponent).content())).getLocation(),EntityType.ARROW);
                arrow.setVelocity(new Vector(0,-1000,0));
                arrow.setShooter(holder);

                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void entityChangeEvent(EntityChangeBlockEvent e){

        if(!(e.getEntity() instanceof FallingBlock)) return;

        FallingBlock fallingBlock = (FallingBlock) e.getEntity();

        if(fallingBlock.getBlockData().getMaterial()==Material.ICE) {

            AreaEffectCloud areaEffectCloud = (AreaEffectCloud) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(fallingBlock.getLocation(), EntityType.AREA_EFFECT_CLOUD);
            areaEffectCloud.setRadius(15);
            areaEffectCloud.setDuration(200);
            areaEffectCloud.setColor(Color.AQUA);

            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(), () -> {
                e.getBlock().setType(Material.AIR);
            }, 1);

            Location location = e.getBlock().getLocation();

            Player player=Bukkit.getPlayer(fallingBlock.getName());

            if(player==null) return;

            CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(player);

            for (CaptureBasePlayer p:captureBasePlayer.getTeam().getEnemyTeam().getPlayers()
                 ) {
                if(Math.sqrt(Math.pow(p.getPlayer().getLocation().getX()-location.getX(),2)+Math.pow(p.getPlayer().getLocation().getZ()-location.getX(),2))<=15){
                    p.setStunned(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                        p.setStunned(false);
                    },200);
                }
            }

            location.setY(location.getY() + 10);

            Block block = Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            block.setType(Material.SNOW);
            int x0=location.getBlockX();
            int z0=location.getBlockZ();
            for (int x=x0-15;x<=x0+15;x++){
                for(int z=z0-15;z<= z0+15;z++){
                    location.setX(x);
                    location.setZ(z);
                    FallingBlock fallingBlock1 = (FallingBlock) Objects.requireNonNull(Bukkit.getWorld("world")).spawnFallingBlock(location, block.getBlockData());
                    fallingBlock1.setDropItem(false);
                }
            }

            block.setType(Material.AIR);
            return;
        }
        if(fallingBlock.getBlockData().getMaterial()==Material.SNOW){
            int time=((int)(Math.random()*1000)%100)+200;
            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                e.getBlock().setType(Material.AIR);
            },time);
        }


    }

    @EventHandler
    public void entityDrop(EntityDropItemEvent e){
        if(!(e.getEntity() instanceof FallingBlock)) return;
        if(e.getItemDrop().getItemStack().getType()==Material.ICE) {

            Bukkit.broadcast(Component.text("baba"));

            FallingBlock fallingBlock = (FallingBlock) e.getEntity();

            AreaEffectCloud areaEffectCloud = (AreaEffectCloud) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(fallingBlock.getLocation(), EntityType.AREA_EFFECT_CLOUD);
            areaEffectCloud.setRadius(15);
            areaEffectCloud.setDuration(200);
            areaEffectCloud.setColor(Color.AQUA);

            Location location=fallingBlock.getLocation();

            Player player=Bukkit.getPlayer(fallingBlock.getName());

            if(player==null) return;

            CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(player);

            for (CaptureBasePlayer p:captureBasePlayer.getTeam().getEnemyTeam().getPlayers()
            ) {
                if(Math.sqrt(Math.pow(p.getPlayer().getLocation().getX()-location.getX(),2)+Math.pow(p.getPlayer().getLocation().getZ()-location.getX(),2))<=15){
                    p.setStunned(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                        p.setStunned(false);
                    },200);
                }
            }

            location.setY(location.getBlockY() + 10);

            Block block = Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            block.setType(Material.SNOW);
            int x0=location.getBlockX();
            int z0=location.getBlockZ();
            for (int x=x0-15;x<=x0+15;x++){
                for(int z=z0-15;z<= z0+15;z++){
                    location.setX(x);
                    location.setZ(z);
                    FallingBlock fallingBlock1 = (FallingBlock) Objects.requireNonNull(Bukkit.getWorld("world")).spawnFallingBlock(location, block.getBlockData());
                    fallingBlock1.setDropItem(false);
                }
            }

            block.setType(Material.AIR);

            e.setCancelled(true);
            return;
        }
        if(e.getItemDrop().getItemStack().getType()==Material.SNOW){
            e.setCancelled(true);

        }


    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent e){
        if(!CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).isStunned()) return;
        e.setCancelled(true);

    }

    @EventHandler
    public void playerDamageItemEvent(PlayerItemDamageEvent e){
        if(e.getItem().getType()==Material.GOLDEN_PICKAXE||e.getItem().getType()==Material.GOLDEN_SWORD||e.getItem().getType()==Material.SHIELD) return;
        org.bukkit.inventory.meta.Damageable itemMeta=(Damageable) e.getItem().getItemMeta();
        itemMeta.setDamage(0);
        e.getItem().setItemMeta((ItemMeta) itemMeta);

    }

}
