package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.Team;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetPlayerTeamCommand extends AbstractCommand {

    public SetPlayerTeamCommand() {
        super("team");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if(args.length!=2) return;
        Player player;
        if(args[0].equals("@p")){
            player= pSelector(sender);
        }else {
            player=Bukkit.getPlayer(args[0]);
        }

        CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(player);

        CaptureBase.getInstance().getBlue().removePlayer(captureBasePlayer);
        CaptureBase.getInstance().getRed().removePlayer(captureBasePlayer);

        Team team=CaptureBase.getInstance().getRed();

        if(args[1].equals("blue")){
            team=CaptureBase.getInstance().getBlue();
        }

        captureBasePlayer.setTeam(team);
        team.addPlayer(captureBasePlayer);

        Objects.requireNonNull(player).sendTitle("Теперь ты в команде "+team.getColorCode()+args[1],"",10,20,10);

    }
}
