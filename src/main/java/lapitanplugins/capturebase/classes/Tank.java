package lapitanplugins.capturebase.classes;

import lapitanplugins.capturebase.CaptureBase;
import lapitanplugins.capturebase.CaptureBasePlayer;
import lapitanplugins.capturebase.exceptions.CaptureBaseException;
import lapitanplugins.capturebase.exceptions.NotEnoughManaException;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.Objects;

public class Tank implements AbstractClass {

    private boolean godMode=false;

    @Override
    public void skill1(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {
        int l=player.getPlayer().getLevel();
        if(l<99){
            throw new NotEnoughManaException("not enough mana");
        }
        l-=99;
        player.getPlayer().setLevel(l);
        godMode=true;
        player.getPlayer().removePotionEffect(PotionEffectType.SLOW);
        player.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,200,3));
        player.getPlayer().playSound(player.getPlayer().getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL,2F,1);
        Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
            godMode=false;
        },200);
    }

    @Override
    public void skill2(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    @Override
    public void skill3(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    @Override
    public void skill4(CaptureBasePlayer player, Object[] someObjects) throws NotEnoughManaException {

    }

    @Override
    public void ultimate(CaptureBasePlayer player, Object[] someObjects) throws CaptureBaseException {
        Location loc=player.getPlayer().getLocation();

        ItemStack items= player.getPlayer().getInventory().getItemInMainHand();
        if(items.getAmount()>1){
            items.setAmount(items.getAmount()-1);
        }else {
            items= new ItemStack(Material.AIR);
        }
        player.getPlayer().getInventory().setItemInMainHand(items);

        final int[] angle = {0};
        int angleVert=0;
        int radius=5;

        double x0=loc.getX();
        double z0=loc.getZ();
        final double[] y0 = {loc.getY() - 1};

        for (int j=0;j<6;j++) {


            Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{

                for (int i = 0; i < 40; i++) {


                    double x1 = x0 + radius* Math.sin(Math.toRadians(angle[0]));
                    double z1 = z0 + radius* Math.cos(Math.toRadians(angle[0]));

                    loc.setX(x1);
                    loc.setZ(z1);
                    loc.setY(y0[0]);

                    Location location = new Location(Bukkit.getWorld("world"), x1, y0[0], z1);

                    Vector vector = new Vector();

                    vector.setX(-(x1 - x0));
                    vector.setZ(-(z1 - z0));

                    location.setDirection(vector);

                    angle[0] += 9;

                    ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(Bukkit.getWorld("world")).spawnEntity(location, EntityType.ARMOR_STAND);
                    armorStand.setItem(EquipmentSlot.HAND, new ItemStack(Material.SHIELD, 1));

                    armorStand.setArms(true);
                    armorStand.setRightArmPose(EulerAngle.ZERO.add(0, Math.toRadians(90), Math.toRadians(270)));

                    armorStand.setInvisible(true);
                    armorStand.setInvulnerable(true);
                    armorStand.setGravity(false);

                    Bukkit.getScheduler().scheduleSyncDelayedTask(CaptureBase.getInstance(),()->{
                        armorStand.setHealth(0);
                    },600);

                }
                y0[0] +=1.5;
            },j*5);
        }
    }

    @Override
    public void doTick(CaptureBasePlayer player) {
        int level=player.getPlayer().getLevel();
        if (!(level >= 100)) level+=1;
        player.getPlayer().setLevel(level);

        highlight(player);

        Block flag=player.getTeam().getCurrFlag().getBlock();
        int distance= (int) Math.sqrt(Math.pow(player.getPlayer().getLocation().getX()-flag.getX(),2)+Math.pow(player.getPlayer().getLocation().getZ()-flag.getZ(),2));
        if(distance<=20){
            player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,40,1));
        }

        double health = player.getPlayer().getHealth();
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 100, 4));
        player.getPlayer().setHealth(health);
        if(godMode) return;
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 40, 2));
        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 40, 2));

    }

    @Override
    public void doSpawnThings(CaptureBasePlayer player, Object[] someObjects) {
        ItemStack helmet=new ItemStack(Material.IRON_HELMET);
        player.getPlayer().getInventory().setHelmet(helmet);
    }

    @Override
    public int getNumber() {
        return 3;
    }
}
