package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class AddTeamTeleportsCommand extends AbstractCommand {

    public AddTeamTeleportsCommand() {
        super("addTeleport");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if(args.length!=7) return;

        Material material=Material.RED_CONCRETE;
        if(args[0].equals("blue")) material=Material.BLUE_CONCRETE;

        int fromX=Integer.parseInt(args[1]);
        int fromY=Integer.parseInt(args[2]);
        int fromZ=Integer.parseInt(args[3]);

        int toX=Integer.parseInt(args[4]);
        int toY=Integer.parseInt(args[5]);
        int toZ=Integer.parseInt(args[6]);

        Block from= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(fromX,fromY,fromZ);

        Block to= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(toX,toY,toZ);

        if(from.getType()!=material||to.getType()!=material) return;

        Team team= CaptureBase.getInstance().getRed();
        if(args[0].equals("blue")) team=CaptureBase.getInstance().getBlue();

        team.addTeleport(from,to);

    }
}
