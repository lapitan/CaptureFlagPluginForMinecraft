package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class SetCommandAfterPreparingCommand extends AbstractCommand {

    public SetCommandAfterPreparingCommand() {
        super("setAfterPreparing");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(args.length!=3) return;
        int commandX=Integer.parseInt(args[0]);
        int commandY=Integer.parseInt(args[1]);
        int commandZ=Integer.parseInt(args[2]);

        Block command= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(commandX,commandY,commandZ);

        if(command.getType()!= Material.REDSTONE_LAMP) return;

        CaptureBase.getInstance().setCommandAfterPreparing(command);
    }
}
