package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.Team;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class AddMineMobsSpawn extends AbstractCommand {

    public AddMineMobsSpawn() {
        super("mineMob");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(args.length!=4) return;

        Team team= CaptureBase.getInstance().getRed();
        if(args[0].equals("blue")) team=CaptureBase.getInstance().getBlue();

        int x=Integer.parseInt(args[1]);
        int y=Integer.parseInt(args[2]);
        int z=Integer.parseInt(args[3]);

        Block block= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(x,y,z);

        team.addMineMobSpawn(block);
    }
}
