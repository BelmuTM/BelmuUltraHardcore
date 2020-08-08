package com.belmu.uhc.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyCountdown implements Runnable {

        private JavaPlugin plugin;

        private Integer assignedTaskId;

        private double seconds;
        private double secondsLeft;

        private Runnable afterTimer;

        public EasyCountdown(JavaPlugin plugin, double seconds, Runnable afterTimer) {
            this.plugin = plugin;

            this.seconds = seconds;
            this.secondsLeft = seconds;

            this.afterTimer = afterTimer;
        }

        @Override
        public void run() {

            if (secondsLeft < 1) {

                afterTimer.run();

                if (assignedTaskId != null) Bukkit.getScheduler().cancelTask(assignedTaskId);
                return;
            }

            secondsLeft--;
        }

        public double getTotalSeconds() {
            return seconds;
        }

        public double getSecondsLeft() {
            return secondsLeft;
        }

        public void scheduleTimer() {
            this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 0L, 20L);
        }
    }