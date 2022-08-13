package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.Team;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class SetTeamSpawnCommand extends AbstractCommand {

    public SetTeamSpawnCommand() {
        super("setSpawn");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(args.length!=7) return;

        Team team= CaptureBase.getInstance().getRed();
        if(args[0].equals("blue")) team=CaptureBase.getInstance().getBlue();

        int baseX=Integer.parseInt(args[1]);
        int baseY=Integer.parseInt(args[2]);
        int baseZ=Integer.parseInt(args[3]);

        Block base= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(baseX,baseY,baseZ);

        team.setBase(base);

        int spawnX=Integer.parseInt(args[4]);
        int spawnY=Integer.parseInt(args[5]);
        int spawnZ=Integer.parseInt(args[6]);

        Block spawn= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(spawnX,spawnY,spawnZ);

       team.setRespawn(spawn);
    }
}
