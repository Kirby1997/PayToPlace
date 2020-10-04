package banano.bananominecraft.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class onBucketUse implements Listener {

    @EventHandler
    public void onBucketUse(PlayerBucketEmptyEvent event){
        Player player = event.getPlayer();
        player.sendMessage("Please don't use buckety things");
        event.setCancelled(true);

    }
}
