package com.belmu.uhc.Core.Packets.Tablist;

import com.belmu.uhc.UHC;
import com.belmu.uhc.Utils.Lag;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public class Tablist {

    public final UHC plugin;
    public Tablist(UHC plugin) {
        this.plugin = plugin;
    }

    int frames = 0;

    public void animate(Player player) {

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        new BukkitRunnable() {

            @Override
            public void run() {
                double tps = Lag.getTPS();

                DecimalFormat df = new DecimalFormat("#.0");
                String tpsFormat = df.format(tps);

                String tpsText = "§a" + tpsFormat;

                if(tps <= 17.5 && tps > 11.0) tpsText = "§e" + tpsFormat;
                else if(tps <= 11.0 && tps > 0.0) tpsText = "§c" + tpsFormat;

                try {
                    Field a = packet.getClass().getDeclaredField("a");
                    a.setAccessible(true);
                    Field b = packet.getClass().getDeclaredField("b");
                    b.setAccessible(true);

                    for(TabEnum frame : TabEnum.values()) {

                        if(frames == frame.id) {
                            Object header = new ChatComponentText(frame.header);
                            Object footer = new ChatComponentText(frame.footer + " §7| §a" + ((CraftPlayer) player).getHandle().ping + "ms\n" +
                                    "§7TPS: " + tpsText);

                            a.set(packet, header);
                            b.set(packet, footer);
                        }
                    }
                    frames += 1;
                    if(frames >= (TabEnum.values().length) - 1) frames = 0;

                    if(Bukkit.getOnlinePlayers().size() == 0) return;
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimer(plugin, 0, 15);
    }

}
