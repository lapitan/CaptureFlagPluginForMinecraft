package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.Bukkit;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractCommand implements CommandExecutor {

    public AbstractCommand(String command){
        PluginCommand pluginCommand= CaptureBase.getInstance().getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(this);
        }
    }

    public abstract void execute(CommandSender sender, String label, String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        execute(sender,label,args);
        return true;
    }

    protected Player pSelector(CommandSender sender){

        BlockCommandSender commandBlock=(BlockCommandSender) sender;
        Collection<? extends Player> players;
        players= Bukkit.getServer().getOnlinePlayers();
        double minDistance=999999999;
        Player nearestPlayer=null;
        double distance;
        for (Player p:players
        ) {
            distance= Math.sqrt(Math.pow(p.getLocation().getX()-commandBlock.getBlock().getX(),2)+Math.pow(p.getLocation().getZ()-commandBlock.getBlock().getZ(),2)+Math.pow(p.getLocation().getY()-commandBlock.getBlock().getY(),2));
            if(distance<minDistance){
                nearestPlayer=p;
            }
        }
        return nearestPlayer;
    }
}
