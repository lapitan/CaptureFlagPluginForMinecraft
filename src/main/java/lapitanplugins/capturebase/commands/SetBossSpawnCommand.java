package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class SetBossSpawnCommand extends AbstractCommand {

    public SetBossSpawnCommand() {
        super("setBoss");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if(args.length!=3) return;
        int bossX=Integer.parseInt(args[0]);
        int bossY=Integer.parseInt(args[1]);
        int bossZ=Integer.parseInt(args[2]);

        Block boss= Objects.requireNonNull(Bukkit.getWorld("world")).getBlockAt(bossX,bossY,bossZ);

        if(boss.getType()!= Material.BEACON) return;

        CaptureBase.getInstance().setBossSpawn(boss);

    }
}
