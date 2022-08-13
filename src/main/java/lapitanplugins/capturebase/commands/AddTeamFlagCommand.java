package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class AddTeamFlagCommand extends AbstractCommand {

    public AddTeamFlagCommand() {
        super("flag");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if(args.length!=7) return;

        Team team= CaptureBase.getInstance().getRed();
        if(args[0].equals("blue")) team=CaptureBase.getInstance().getBlue();

        int flagX=Integer.parseInt(args[1]);
        int flagY=Integer.parseInt(args[2]);
        int flagZ=Integer.parseInt(args[3]);

        Block flag= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(flagX,flagY,flagZ);

        if(flag.getType()!= Material.END_STONE) return;

        int commandX=Integer.parseInt(args[4]);
        int commandY=Integer.parseInt(args[5]);
        int commandZ=Integer.parseInt(args[6]);

        Block command= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(commandX,commandY,commandZ);

        if(command.getType()!= Material.REDSTONE_LAMP) return;

        team.addFlag(flag,command);

    }
}
