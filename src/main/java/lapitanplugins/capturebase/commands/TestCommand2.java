package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Objects;

public class TestCommand2 extends AbstractCommand {

    public TestCommand2() {
        super("test2");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;
        Location location=player.getLocation();

        Firework firework=(Firework) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location,EntityType.FIREWORK);
        FireworkMeta fireworkMeta= firework.getFireworkMeta();

        FireworkEffect.Type fireworkType= FireworkEffect.Type.BALL;
        FireworkEffect fireworkEffect=FireworkEffect.builder().with(fireworkType).withColor(Color.RED).build();

        fireworkMeta.addEffect(fireworkEffect);
        fireworkMeta.setPower(8);

        firework.setFireworkMeta(fireworkMeta);

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(), firework::detonate,20);

    }

}

