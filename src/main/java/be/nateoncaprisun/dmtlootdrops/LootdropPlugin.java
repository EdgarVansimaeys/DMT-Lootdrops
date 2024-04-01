package be.nateoncaprisun.dmtlootdrops;

import be.nateoncaprisun.dmtlootdrops.listeners.EditGuiListener;
import be.nateoncaprisun.dmtlootdrops.listeners.FlarePlaceListener;
import co.aikar.commands.BukkitCommandManager;
import be.nateoncaprisun.dmtlootdrops.commands.LootdropCommand;
import be.nateoncaprisun.dmtlootdrops.listeners.ArmorStandInteractListener;
import be.nateoncaprisun.dmtlootdrops.utils.ConfigurationFile;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class LootdropPlugin extends JavaPlugin {
    private static @Getter LootdropPlugin instance;
    private @Getter ConfigurationFile lootFile;
    private @Getter BukkitCommandManager commandManager;
    private @Getter ArrayList<Player> cooldown = new ArrayList<>();
    @Override
    public void onEnable() {
        instance = this;

        lootFile = new ConfigurationFile(this, "loot.yml", true);

        commandManager = new BukkitCommandManager(this);

        registerCommands();

        new ArmorStandInteractListener(this);
        new EditGuiListener(this);
        new FlarePlaceListener(this);
    }

    public void registerCommands() {
        commandManager.registerCommand(new LootdropCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
