package be.nateoncaprisun.dmtlootdrops.listeners;

import be.nateoncaprisun.dmtlootdrops.LootdropPlugin;
import be.nateoncaprisun.dmtlootdrops.objects.Lootdrop;
import be.nateoncaprisun.dmtlootdrops.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class FlarePlaceListener implements Listener {

    private LootdropPlugin main;

    public FlarePlaceListener(LootdropPlugin main){
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void flarePlaceListener(BlockPlaceEvent event){
        Location location = event.getBlockPlaced().getLocation();
        ItemStack hand = event.getItemInHand();
        if (!hand.getType().equals(Material.REDSTONE_TORCH_ON)) return;
        String name = hand.getItemMeta().getDisplayName();
        String[] parts = name.split(" ");
        String type = parts[parts.length - 1];
        if (!main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)) return;
        event.setCancelled(true);
        if (main.getCooldown().contains(event.getPlayer())){
            event.getPlayer().sendMessage(Utils.color("&cJe hebt nog een cooldown!"));
            return;
        }
        hand.setAmount(hand.getAmount()-1);
        Lootdrop lootdrop = new Lootdrop();
        lootdrop.setType(type);
        lootdrop.drop(location.add(0,25,0), event.getPlayer().getWorld(), event.getPlayer(), type);
        main.getCooldown().add(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(main, () -> {
            main.getCooldown().remove(event.getPlayer());
        }, 20L*10);
    }

}
