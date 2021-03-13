package com.belmu.uhc.Core.Packets.Tablist;

/**
 * @author Belmu (https://github.com/BelmuTM/)
 */
public enum TabEnum {

    A(0, "§c§lUHC", "  §3Good Luck!"),
    B(1, "§c§lUHC", "  §3Good Luck!"),
    C(2, "§4§lUHC", "  §3Good Luck!"),
    D(3, "§4§lUHC", "  §3Good Luck!"),
    E(4, "§c§lU§4§lHC", "  §3Good Luck!"),
    F(4, "§c§lU§4§lHC", "  §3Good Luck!"),
    G(5, "§4§lU§c§lH§4§lC", "  §3Good Luck!"),
    H(5, "§4§lU§c§lH§4§lC", "  §3Good Luck!"),
    I(6, "§4§lUH§c§lC", "  §3Good Luck!"),
    J(6, "§4§lUH§c§lC", "  §3Good Luck!");

    public int id;
    public String header, footer;

    TabEnum(int id, String header, String footer) {
        this.id = id;
        this.header = header;
        this.footer = footer;
    }

}
