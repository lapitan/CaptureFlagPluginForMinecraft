package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.CommandBlock;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.KeyedBossBar;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class TestCommand extends AbstractCommand {
    public TestCommand() {
        super("test");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if(!(sender instanceof Player)) return;
        Player player=(Player) sender;

        Evoker boss;
        BossBar bossBar;

        boss= (Evoker) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(player.getLocation(), EntityType.EVOKER);
        boss.setCustomName("Boss");
        AttributeInstance attributeInstance= boss.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        Objects.requireNonNull(attributeInstance).setBaseValue(600.0D+1000.0D/2.0D);
        boss.setHealth(Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        boss.setAI(false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            boss.setSpell(Spellcaster.Spell.WOLOLO);
        },60);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            boss.setSpell(Spellcaster.Spell.FANGS);
        },120);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            boss.setSpell(Spellcaster.Spell.SUMMON_VEX);
        },180);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            boss.setSpell(Spellcaster.Spell.BLINDNESS);
        },240);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            boss.setSpell(Spellcaster.Spell.DISAPPEAR);
        },300);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            boss.setSpell(Spellcaster.Spell.NONE);
        },360);

        bossBar=Bukkit.createBossBar("Boss", BarColor.BLUE, BarStyle.SEGMENTED_6);
        bossBar.setProgress(boss.getHealth()/ Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
        bossBar.setVisible(true);

        bossBar.addPlayer(player);

        player.sendMessage(String.valueOf(Objects.requireNonNull(boss.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()));

        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(), bossBar::removeAll,100);
    }
}
