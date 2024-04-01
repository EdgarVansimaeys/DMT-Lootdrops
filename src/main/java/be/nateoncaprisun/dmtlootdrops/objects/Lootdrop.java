package be.nateoncaprisun.dmtlootdrops.objects;

import be.nateoncaprisun.dmtlootdrops.LootdropPlugin;
import be.nateoncaprisun.dmtlootdrops.utils.ItemBuilder;
import be.nateoncaprisun.dmtlootdrops.utils.NBTEditor;
import be.nateoncaprisun.dmtlootdrops.utils.Utils;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Lootdrop {
    String type;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void drop(Location location, World world, Player player, String type){
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setGravity(false);
        armorStand.setCustomName(type);
        armorStand.setCustomNameVisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setCanPickupItems(false);
        armorStand.setVisible(false);
        ItemStack loot = NBTEditor.set(new ItemStack(Material.GOLD_SPADE), "supplydrop", "mdev");
        armorStand.getEquipment().setHelmet(loot);
        armorStand.setMetadata("lootdrop-armorstand", new FixedMetadataValue(LootdropPlugin.getInstance(), true));

        Bukkit.getScheduler().runTaskTimer(LootdropPlugin.getInstance(), () -> {
            if (armorStand.getLocation().subtract(0, 0.1, 0).getBlock().getType() != Material.AIR){
                armorStand.setGravity(true);
                return;
            }
            Location loc = armorStand.getLocation().subtract(0, 0.1, 0);
            loc.setYaw((float) (loc.getYaw()+0.5));
            armorStand.teleport(loc);
            for (Entity nearbyEntity : armorStand.getNearbyEntities(40, 40, 40)) {
                if (nearbyEntity instanceof Player){
                    ((Player) nearbyEntity).spawnParticle(Particle.FLAME, armorStand.getLocation(), 5);
                }
            }

        }, 2L, 0);

    }

    public void openLootDrop(String type, Player player){
        Gui gui = Gui.gui()
                .rows(6)
                .title(Component.text(Utils.color("Lootdrop Open")))
                .disableItemPlace()
                .disableItemSwap()
                .create();

        for (int i = 0; i < LootdropPlugin.getInstance().getConfig().getInt("Hoeveelheid-Items-Lootdrop"); i++) {
            Random randSlot = new Random();
            int slot = randSlot.ints(0, 53).findAny().getAsInt();
            ItemStack randomItem = (ItemStack) LootdropPlugin.getInstance().getLootFile().getConfig().getList("loot."+type).get((new Random()).nextInt(LootdropPlugin.getInstance().getLootFile().getConfig().getList("loot."+type).size()));
            GuiItem item = new GuiItem(randomItem);
            gui.setItem(slot, item);
        }

        gui.open(player);
    }
    
    public ItemStack getFlare(String type){

        List<String> loreList = new ArrayList<>();
        for (String s : LootdropPlugin.getInstance().getConfig().getStringList("Flare-Lore")) {
            loreList.add(Utils.color(s));
        }
        
        ItemStack flare = new ItemBuilder(Material.REDSTONE_TORCH_ON, 1)
                .setColoredName(LootdropPlugin.getInstance().getConfig().getString("Flare-Name").replaceAll("%type%", type))
                .setLore(loreList)
                .toItemStack();

        return flare;
        
    }

}
