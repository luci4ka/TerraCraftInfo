package ru.terracraft.info;

import de.exlll.configlib.YamlConfigurations;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;
import ru.terracraft.info.command.HelpCommand;
import ru.terracraft.info.command.InfoCommand;
import ru.terracraft.info.command.LinksCommand;
import ru.terracraft.info.command.RulesCommand;
import ru.terracraft.info.config.InfoConfig;
import ru.terracraft.info.content.ContentManager;
import ru.terracraft.info.lang.LangManager;
import ru.terracraft.info.util.UpdateNotifier;

import java.nio.file.Path;

public final class TerraCraftInfoPlugin extends JavaPlugin {

    private static TerraCraftInfoPlugin instance;
    private InfoConfig infoConfig;
    private LangManager langManager;
    private ContentManager contentManager;
    private UpdateNotifier updateNotifier;

    public static TerraCraftInfoPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        Path configPath = getDataFolder().toPath().resolve("config.yml");
        this.infoConfig = YamlConfigurations.update(configPath, InfoConfig.class);

        this.langManager = new LangManager(this, infoConfig.language, infoConfig.fallbackLanguage);
        this.contentManager = new ContentManager(this, infoConfig.language, infoConfig.fallbackLanguage);

        new RulesCommand(this).register();
        new HelpCommand(this).register();
        new InfoCommand(this).register();
        new LinksCommand(this).register();

        this.updateNotifier = new UpdateNotifier(this);
        this.updateNotifier.checkAsync();

        new Metrics(this, 25000);

        getLogger().info("TerraCraftInfo v" + getPluginMeta().getVersion() + " включён.");
    }

    @Override
    public void onDisable() {
        getLogger().info("TerraCraftInfo выключен.");
    }

    public InfoConfig getInfoConfig() { return infoConfig; }
    public LangManager getLangManager() { return langManager; }
    public ContentManager getContentManager() { return contentManager; }
    public UpdateNotifier getUpdateNotifier() { return updateNotifier; }

    public void reload() {
        Path configPath = getDataFolder().toPath().resolve("config.yml");
        this.infoConfig = YamlConfigurations.load(configPath, InfoConfig.class);
        this.langManager = new LangManager(this, infoConfig.language, infoConfig.fallbackLanguage);
        this.contentManager = new ContentManager(this, infoConfig.language, infoConfig.fallbackLanguage);
    }
}