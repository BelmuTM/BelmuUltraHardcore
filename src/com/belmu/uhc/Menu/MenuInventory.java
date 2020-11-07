package com.belmu.uhc.Menu;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class MenuInventory {

    public static Inventory gameMenu() {

        Inventory menu = Bukkit.createInventory(null, 27);
        return menu;
    }

}
