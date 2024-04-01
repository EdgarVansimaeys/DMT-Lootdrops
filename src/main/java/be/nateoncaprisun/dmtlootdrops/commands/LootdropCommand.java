package be.nateoncaprisun.dmtlootdrops.commands;

import be.nateoncaprisun.dmtlootdrops.LootdropPlugin;
import be.nateoncaprisun.dmtlootdrops.menus.EditLootGui;
import be.nateoncaprisun.dmtlootdrops.objects.Lootdrop;
import be.nateoncaprisun.dmtlootdrops.utils.Utils;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

@CommandAlias("lootdrop")
@CommandPermission("lootdrop.admin")
public class LootdropCommand extends BaseCommand {

    private LootdropPlugin main = LootdropPlugin.getInstance();

    @Default
    @Subcommand("help")
    private void sendHelp(CommandSender player){

        player.sendMessage(Utils.color("&b&m----------------------"));
        player.sendMessage("");
        player.sendMessage(Utils.color("&b/lootdrop &3editloot [type] &7- &bPas de loot van een lootdrop aan"));
        player.sendMessage(Utils.color("&b/lootdrop &3create [type] &7- &bMaak een lootdrop aan"));
        player.sendMessage(Utils.color("&b/lootdrop &3delete [type] &7- &bVerwijder een lootdrop"));
        player.sendMessage(Utils.color("&b/lootdrop &3spawn [type] &7- &bSpawn een lootdrop"));
        player.sendMessage(Utils.color("&b/lootdrop &3get [type] &7- &bKrijg een flare van een lootdrop"));
        player.sendMessage("");
        player.sendMessage(Utils.color("&b&m----------------------"));

    }

    @Subcommand("create")
    @Syntax("<text>")
    public void create(Player player, String type){
        if (main.getLootFile().getConfig().getConfigurationSection("loot") == null){
            addDrop(type);
            player.sendMessage(Utils.color("&aJe hebt een lootdrop aangemaakt!"));
            return;
        }
        if (main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)){
            player.sendMessage(Utils.color("&cDeze lootdrop bestaat al!"));
            return;
        }
        addDrop(type);
        player.sendMessage(Utils.color("&aJe hebt een lootdrop aangemaakt!"));
    }

    @Subcommand("delete")
    @Syntax("<text>")
    public void delete(Player player, String type){
        if (main.getLootFile().getConfig().getConfigurationSection("loot") == null){
            player.sendMessage(Utils.color("&cEr bestaat geen lootdrop met de naam " + type));
            return;
        }
        if (!main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)){
            player.sendMessage(Utils.color("&cEr bestaat geen lootdrop met de naam " + type));
            return;
        }
        removeDrop(type);
        player.sendMessage(Utils.color("&cJe hebt de lootdrop: " + type + " verwijdert!"));
    }

    @Subcommand("editloot")
    @Syntax("<type>")
    public void editLoot(Player player, String type){
        player.sendMessage(type);
        if (main.getLootFile().getConfig().getConfigurationSection("loot") == null){
            player.sendMessage(Utils.color("&cEr bestaat geen lootdrop met de naam " + type));
            return;
        }
        if (!main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)){
            player.sendMessage(Utils.color("&cEr bestaat geen lootdrop met de naam " + type));
            return;
        }

        new EditLootGui().editLootGui(type, player);
    }

    @Subcommand("spawn")
    @Syntax("<type>")
    public void spawnDrop(Player player, String type){

        if (!main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)){
            player.sendMessage(Utils.color("&cDie lootdrop bestaat niet!"));
            return;
        }
        Lootdrop lootdrop = new Lootdrop();
        lootdrop.setType(type);
        lootdrop.drop(player.getLocation().add(0,25,0), player.getWorld(), player, lootdrop.getType());
    }

    @Subcommand("get")
    @Syntax("<type>")
    public void getFlare(Player player, String type){
        if (!main.getLootFile().getConfig().getConfigurationSection("loot").contains(type)){
            player.sendMessage(Utils.color("&cDie lootdrop bestaat niet!"));
            return;
        }
        Lootdrop lootdrop = new Lootdrop();
        lootdrop.setType(type);
        player.getInventory().addItem(lootdrop.getFlare(type));
    }

    private void addDrop(String type){
        ArrayList<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(Material.DIRT));
        if (main.getLootFile().getConfig().getConfigurationSection("loot") == null){
            main.getLootFile().getConfig().set("loot." + type, list);
            main.getLootFile().saveConfig();
            return;
        }
        ConfigurationSection configurationSection = main.getLootFile().getConfig().getConfigurationSection("loot");
        configurationSection.set(type, list);
        main.getLootFile().saveConfig();
    }
    private void removeDrop(String type){
        ConfigurationSection configurationSection = main.getLootFile().getConfig().getConfigurationSection("loot");
        configurationSection.set(type, null);
        main.getLootFile().saveConfig();
    }
}
