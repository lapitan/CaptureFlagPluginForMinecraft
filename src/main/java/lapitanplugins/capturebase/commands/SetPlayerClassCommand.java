package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.classes.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class SetPlayerClassCommand extends AbstractCommand {

    public SetPlayerClassCommand() {
        super("class");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if(args.length!=2) return;
        Player player;
        if(args[0].equals("@p")){
            player= pSelector(sender);
        }else {
            player= Bukkit.getPlayer(args[0]);
        }

        CaptureBasePlayer captureBasePlayer= CaptureBase.getInstance().getCaptureBasePlayer(player);

        AbstractClass abstractClass=new Archer();
        String message="§2Лучник";

        if(args[1].equals("Berserk")){
            abstractClass=new Berserk();
            message="§4Берсерк";
        }
        if(args[1].equals("Mage")){
            abstractClass=new Mage();
            message="§3Маг";
        }
        if(args[1].equals("Miner")){
            abstractClass=new Miner();
            message="§7Шахтер";
        }
        if(args[1].equals("Ninja")){
            abstractClass=new Ninja();
            message="§0Ниндзя";
        }
        if(args[1].equals("Tank")){
            abstractClass=new Tank();
            message="§5ТАНК";
        }

        captureBasePlayer.setaClass(abstractClass);

        Objects.requireNonNull(player).sendTitle("Теперь ты"+message,"",10,20,10);


    }
}
