package lapitanplugins.capturebase.commands;

import lapitanplugins.capturebase.CaptureBase;
import org.bukkit.command.CommandSender;

public class StartCommand extends AbstractCommand {

    public StartCommand() {
        super("start");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (args.length!=1) return;

        if(args[0].equals("part")) CaptureBase.getInstance().start();
        if(args[0].equals("full")) CaptureBase.getInstance().fullStart();
    }
}
