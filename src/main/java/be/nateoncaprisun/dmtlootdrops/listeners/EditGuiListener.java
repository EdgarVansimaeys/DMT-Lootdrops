package be.nateoncaprisun.dmtlootdrops.listeners;

import be.nateoncaprisun.dmtlootdrops.LootdropPlugin;
import be.nateoncaprisun.dmtlootdrops.menus.EditLootGui;
import be.nateoncaprisun.dmtlootdrops.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EditGuiListener implements Listener {

    private LootdropPlugin main;

    public EditGuiListener(LootdropPlugin main){
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent event){
        if (!event.getView().getTitle().startsWith(Utils.color("&aLootdrop Loot"))) return;
        event.setCancelled(true);
        String[] parts = event.getView().getTitle().split(" ");
        String type = parts[parts.length - 1];
        Player player = (Player) event.getWhoClicked();

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        ArrayList<ItemStack> lootList = (ArrayList<ItemStack>) LootdropPlugin.getInstance().getLootFile().getConfig().getList("loot."+type);

        if (event.getView().getTopInventory() == event.getClickedInventory()){
            lootList.remove(clicked);
            LootdropPlugin.getInstance().getLootFile().getConfig().set("loot." +type, lootList);
            LootdropPlugin.getInstance().getLootFile().saveConfig();

        } else {
            lootList.add(clicked);
            LootdropPlugin.getInstance().getLootFile().getConfig().set("loot." +type, lootList);
            LootdropPlugin.getInstance().getLootFile().saveConfig();
        }
        new EditLootGui().editLootGui(type, player);
    }

}
