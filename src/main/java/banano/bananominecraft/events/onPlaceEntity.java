package banano.bananominecraft.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPlaceEvent;

public class onPlaceEntity implements Listener {

    @EventHandler
    public void onPlaceEntity(EntityPlaceEvent event){
        Player player = event.getPlayer();
        Entity entity = event.getEntity();
        entity.remove();
        player.sendMessage("No placing entities");
    }
}
