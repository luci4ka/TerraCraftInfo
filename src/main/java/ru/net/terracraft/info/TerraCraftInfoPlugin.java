package ru.net.terracraft.info;

import de.exlll.configlib.YamlConfigurations;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import ru.net.terracraft.info.command.*;
import ru.net.terracraft.info.config.InfoConfig;
import ru.net.terracraft.info.lang.LangManager;

import java.nio.file.Path;

public final class TerraCraftInfoPlugin extends JavaPlugin {

    private static TerraCraftInfoPlugin instance;
    private InfoConfig infoConfig;
    private LangManager langManager;
    private UpdateNotifier updateNotifier;

    public static TerraCraftInfoPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        // ─── ConfigLib: config.yml ─────────────────
        Path configPath = getDataFolder().toPath().resolve("config.yml");
        this.infoConfig = YamlConfigurations.update(configPath, InfoConfig.class);

        // ─── LangManager ───────────────────────────
        this.langManager = new LangManager(
                this,
                infoConfig.language,
                infoConfig.fallbackLanguage
        );

        // ─── CommandAPI команды ────────────────────
        new RulesCommand(this).register();
        new HelpCommand(this).register();
        new InfoCommand(this).register();
        new LinksCommand(this).register();

        // ─── Update Notifier ───────────────────────
        this.updateNotifier = new UpdateNotifier(this);
        this.updateNotifier.checkAsync();

        // ─── bStats ─────────────────────────────────
        new Metrics(this, 25000);

        getLogger().info("TerraCraftInfo v" + getPluginMeta().getVersion() + " включён.");
    }

    @Override
    public void onDisable() {
        getLogger().info("TerraCraftInfo выключен.");
    }

    public InfoConfig getInfoConfig() { return infoConfig; }
    public LangManager getLangManager() { return langManager; }
    public UpdateNotifier getUpdateNotifier() { return updateNotifier; }

    public void reload() {
        Path configPath = getDataFolder().toPath().resolve("config.yml");
        this.infoConfig = YamlConfigurations.load(configPath, InfoConfig.class);
        this.langManager.reload();
    }
}