package be.nateoncaprisun.dmtlootdrops.listeners;

import be.nateoncaprisun.dmtlootdrops.LootdropPlugin;
import be.nateoncaprisun.dmtlootdrops.objects.Lootdrop;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class ArmorStandInteractListener implements Listener {

    private LootdropPlugin main;

    public ArmorStandInteractListener(LootdropPlugin main){
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void armorStandInteractEvent(PlayerInteractAtEntityEvent event){
        if (!(event.getRightClicked() instanceof ArmorStand)) return;
        ArmorStand armorStand = (ArmorStand) event.getRightClicked();
        Location location = armorStand.getLocation().subtract(0,0.1,0);
        if (location.getBlock() == null || location.getBlock().getType().equals(Material.AIR)) return;
        String type = armorStand.getCustomName();
        if (armorStand.getCustomName().equals("")) return;
        if (armorStand.getCustomName() == null) return;
        if (!main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)) return;
        Player player = event.getPlayer();
        Lootdrop lootdrop = new Lootdrop();
        lootdrop.setType(type);
        lootdrop.openLootDrop(type, player);
        armorStand.remove();
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event){
        ArmorStand armorStand = event.getRightClicked();
        if (armorStand.hasMetadata("lootdrop-armorstand")) {
            event.setCancelled(true);
        }
    }

}
