package lapitanplugins.capturebase;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.EntityType;

import java.util.Objects;

public class Flag {

    private Block block;
    private Block command;
    private int HP;
    private AreaEffectCloud text;

    private final String colorCode;

    public boolean update() {
        HP--;
        text.setCustomName(colorCode+ HP);
        if (HP == 0) {
            text.setCustomName(colorCode + "здох");
            command.setType(Material.REDSTONE_BLOCK);
            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),
                    () -> command.setType(Material.REDSTONE_LAMP), 20);
        }
        return HP == 0;
    }

    public Flag(Block block_, Block command, String colorCode) {

        this.block = block_;
        this.command = command;
        this.colorCode=colorCode;
        HP = 50;
        Location location = block.getLocation();
        location.setY(location.getY() + 1);
        location.setX(location.getX() + 0.5F);
        location.setZ(location.getZ() + 0.5F);
        text = (AreaEffectCloud) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location, EntityType.AREA_EFFECT_CLOUD);
        text.setDuration(2000000000);
        text.setCustomName(colorCode + HP);
        text.setParticle(Particle.BLOCK_CRACK, Material.AIR.createBlockData());
        text.setCustomNameVisible(true);

    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Flag) {
            Flag newFlag = (Flag) object;
            return newFlag.block.equals(block);
        }
        if (object instanceof Block) {
            Block newBlock = (Block) object;
            return newBlock.equals(block);
        }
        return false;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public Block getCommand() {
        return command;
    }

    public void setCommand(Block command) {
        this.command = command;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    public AreaEffectCloud getText() {
        return text;
    }

    public void setText(AreaEffectCloud text) {
        this.text = text;
    }
}
