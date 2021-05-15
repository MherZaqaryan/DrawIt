package me.MrIronMan.drawit.api.game;

public enum DrawerTool {

    THIN_BRUSH("thin-brush"),
    THICK_BRUSH("thick-brush"),
    SPRAY_CANVAS("spray-canvas"),
    FILL_CAN("fill-can"),
    BURN_CANVAS("burn-canvas");

    private final String path;

    DrawerTool(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
