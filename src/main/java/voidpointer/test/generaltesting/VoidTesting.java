package voidpointer.test.generaltesting;

import org.bukkit.plugin.java.JavaPlugin;
import voidpointer.test.generaltesting.listener.PiglinBastionGenerationListener;

public final class VoidTesting extends JavaPlugin {
    @Override public void onLoad() {

    }

    @Override public void onEnable() {
        new PiglinBastionGenerationListener(getSLF4JLogger()).register(this);
    }

    @Override public void onDisable() {

    }
}
