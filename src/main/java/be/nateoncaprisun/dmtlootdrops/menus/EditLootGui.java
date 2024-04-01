package be.nateoncaprisun.dmtlootdrops.menus;

import be.nateoncaprisun.dmtlootdrops.LootdropPlugin;
import be.nateoncaprisun.dmtlootdrops.objects.Lootdrop;
import be.nateoncaprisun.dmtlootdrops.utils.Utils;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class EditLootGui {

    public void editLootGui(String type, Player player){

        Inventory inventory = Bukkit.createInventory(null, 54, Utils.color("&aLootdrop Loot " + type));

        ArrayList<ItemStack> list = (ArrayList<ItemStack>) LootdropPlugin.getInstance().getLootFile().getConfig().getList("loot."+type);

        for (int i = 0; i < list.size(); i++){
            inventory.setItem(i, list.get(i));
        }

        player.openInventory(inventory);
    }

}
