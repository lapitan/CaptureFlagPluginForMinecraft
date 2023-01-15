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

}
