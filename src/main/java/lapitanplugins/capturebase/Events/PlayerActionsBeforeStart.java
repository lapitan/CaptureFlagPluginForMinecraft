package lapitanplugins.capturebase.Events;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.classes.Archer;
import lapitanplugins.capturebase.classes.Miner;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPoseChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.Objects;

public class PlayerActionsBeforeStart implements Listener {

    @EventHandler
    public void playerJoin(PlayerJoinEvent e){
        if(CaptureBase.getInstance().isStarted()) return;
        if(CaptureBase.getInstance().containsPlayer(e.getPlayer())) return;
        CaptureBasePlayer captureBasePlayer=new CaptureBasePlayer(e.getPlayer());
        CaptureBase.getInstance().addPlayer(captureBasePlayer);
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

//    @EventHandler
//    public void entityChangeEvent(EntityChangeBlockEvent e){
//
//        if(!(e.getEntity() instanceof FallingBlock)) return;
//
//        FallingBlock fallingBlock = (FallingBlock) e.getEntity();
//
//        if(fallingBlock.getBlockData().getMaterial()==Material.ICE) {
//
//            AreaEffectCloud areaEffectCloud = (AreaEffectCloud) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(fallingBlock.getLocation(), EntityType.AREA_EFFECT_CLOUD);
//            areaEffectCloud.setRadius(15);
//            areaEffectCloud.setDuration(200);
//            areaEffectCloud.setColor(Color.AQUA);
//
//            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(), () -> {
//                e.getBlock().setType(Material.AIR);
//            }, 1);
//
//            Location location = e.getBlock().getLocation();
//
//            Player player=Bukkit.getPlayer(fallingBlock.getName());
//
//            if(player==null) return;
//
//            CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(player);
//
//            for (CaptureBasePlayer p:captureBasePlayer.getTeam().getEnemyTeam().getPlayers()
//                 ) {
//                if(Math.sqrt(Math.pow(p.getPlayer().getLocation().getX()-location.getX(),2)+Math.pow(p.getPlayer().getLocation().getZ()-location.getX(),2))<=15){
//                    p.setStunned(true);
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
//                        p.setStunned(false);
//                    },200);
//                }
//            }
//
//            location.setY(location.getY() + 10);
//
//            Block block = Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
//            block.setType(Material.SNOW);
//            int x0=location.getBlockX();
//            int z0=location.getBlockZ();
//            for (int x=x0-15;x<=x0+15;x++){
//                for(int z=z0-15;z<= z0+15;z++){
//                    location.setX(x);
//                    location.setZ(z);
//                    FallingBlock fallingBlock1 = (FallingBlock) Objects.requireNonNull(Bukkit.getWorld("world")).spawnFallingBlock(location, block.getBlockData());
//                    fallingBlock1.setDropItem(false);
//                }
//            }
//
//            block.setType(Material.AIR);
//            return;
//        }
//        if(fallingBlock.getBlockData().getMaterial()==Material.SNOW){
//            int time=((int)(Math.random()*1000)%100)+200;
//            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
//                e.getBlock().setType(Material.AIR);
//            },time);
//        }
//
//
//    }

//    @EventHandler
//    public void entityDrop(EntityDropItemEvent e){
//        if(!(e.getEntity() instanceof FallingBlock)) return;
//        if(e.getItemDrop().getItemStack().getType()==Material.ICE) {
//
//            Bukkit.broadcast(Component.text("baba"));
//
//            FallingBlock fallingBlock = (FallingBlock) e.getEntity();
//
//            AreaEffectCloud areaEffectCloud = (AreaEffectCloud) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(fallingBlock.getLocation(), EntityType.AREA_EFFECT_CLOUD);
//            areaEffectCloud.setRadius(15);
//            areaEffectCloud.setDuration(200);
//            areaEffectCloud.setColor(Color.AQUA);
//
//            Location location=fallingBlock.getLocation();
//
//            Player player=Bukkit.getPlayer(fallingBlock.getName());
//
//            if(player==null) return;
//
//            CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(player);
//
//            for (CaptureBasePlayer p:captureBasePlayer.getTeam().getEnemyTeam().getPlayers()
//            ) {
//                if(Math.sqrt(Math.pow(p.getPlayer().getLocation().getX()-location.getX(),2)+Math.pow(p.getPlayer().getLocation().getZ()-location.getX(),2))<=15){
//                    p.setStunned(true);
//                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
//                        p.setStunned(false);
//                    },200);
//                }
//            }
//
//            location.setY(location.getBlockY() + 10);
//
//            Block block = Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
//            block.setType(Material.SNOW);
//            int x0=location.getBlockX();
//            int z0=location.getBlockZ();
//            for (int x=x0-15;x<=x0+15;x++){
//                for(int z=z0-15;z<= z0+15;z++){
//                    location.setX(x);
//                    location.setZ(z);
//                    FallingBlock fallingBlock1 = (FallingBlock) Objects.requireNonNull(Bukkit.getWorld("world")).spawnFallingBlock(location, block.getBlockData());
//                    fallingBlock1.setDropItem(false);
//                }
//            }
//
//            block.setType(Material.AIR);
//
//            e.setCancelled(true);
//            return;
//        }
//        if(e.getItemDrop().getItemStack().getType()==Material.SNOW){
//            e.setCancelled(true);
//
//        }


//    }

//    @EventHandler
//    public void playerMoveEvent(PlayerMoveEvent e){
//        if(!CaptureBase.getInstance().getCaptureBasePlayer(e.getPlayer()).isStunned()) return;
//        e.setCancelled(true);
//
//    }

    @EventHandler
    public void playerDamageItemEvent(PlayerItemDamageEvent e){
        if(e.getItem().getType()==Material.GOLDEN_PICKAXE||e.getItem().getType()==Material.GOLDEN_SWORD||e.getItem().getType()==Material.SHIELD) return;
        Damageable itemMeta=(Damageable) e.getItem().getItemMeta();
        itemMeta.setDamage(0);
        e.getItem().setItemMeta((ItemMeta) itemMeta);

    }

}
