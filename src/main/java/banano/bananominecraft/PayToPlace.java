package banano.bananominecraft;

import banano.bananominecraft.events.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;


import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class PayToPlace extends JavaPlugin {


    public static Economy econ = null;
    @Override
    public void onEnable() {
        // Plugin startup logic

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new onPlace(), this);
        getServer().getPluginManager().registerEvents(new onDestroy(), this);
        getServer().getPluginManager().registerEvents(new onJoin(), this);
        getServer().getPluginManager().registerEvents(new onPlaceEntity(), this);
        getServer().getPluginManager().registerEvents(new onBucketUse(), this);
        getServer().getPluginManager().registerEvents(new onBoatPlace(), this);

        loadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        FileConfiguration cfg = this.getConfig();

        if(!(cfg.contains(p.getName()))) {
            String name = p.getName();

            this.saveConfig();

        } else {
            return;
        }
    }

    public void loadConfig(){
        getConfig().options().copyDefaults(true);
        saveConfig();

    }


}
