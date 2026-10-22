package ru.terracraft.info.lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.terracraft.info.TerraCraftInfoPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

public class LangManager {

    private final TerraCraftInfoPlugin plugin;
    private final Map<String, Map<String, String>> translations = new HashMap<>();
    private final String defaultLocale;
    private final String fallbackLocale;

    public LangManager(TerraCraftInfoPlugin plugin, String defaultLocale, String fallbackLocale) {
        this.plugin = plugin;
        this.defaultLocale = defaultLocale;
        this.fallbackLocale = fallbackLocale;
        loadAll();
    }

    private void loadAll() {
        translations.clear();

        File langRoot = new File(plugin.getDataFolder(), "lang");
        if (!langRoot.exists()) {
            langRoot.mkdirs();
            saveDefaultFolder("lang/ru_RU");
            saveDefaultFolder("lang/en_US");
        }

        File[] localeDirs = langRoot.listFiles(File::isDirectory);
        if (localeDirs == null) return;

        for (File localeDir : localeDirs) {
            String locale = localeDir.getName();
            Map<String, String> flat = new HashMap<>();

            File[] ymlFiles = localeDir.listFiles((dir, name) -> name.endsWith(".yml"));
            if (ymlFiles == null) continue;

            for (File yml : ymlFiles) {
                YamlConfiguration cfg = YamlConfiguration.loadConfiguration(yml);
                for (String key : cfg.getKeys(true)) {
                    flat.put(key, cfg.getString(key));
                }
            }

            translations.put(locale, flat);
            plugin.getLogger().info("Загружена локаль " + locale + " (" + flat.size() + " ключей)");
        }
    }

    private void saveDefaultFolder(String resourcePath) {
        String[] files = {"common.yml", "rules.yml", "info.yml", "help.yml", "links.yml"};
        for (String file : files) {
            try {
                plugin.saveResource(resourcePath + "/" + file, false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.FINE, "Ресурс не найден: " + resourcePath + "/" + file);
            }
        }
    }

    // ─── Основной API ─────────────────────────

    public String raw(Lang key, Player player) {
        String locale = player != null
                ? normalize(player.locale().toString())
                : defaultLocale;
        return resolve(key.getPath(), locale);
    }

    public String raw(Lang key, String locale) {
        return resolve(key.getPath(), normalize(locale));
    }

    public String raw(Lang key, Player player, Map<String, String> placeholders) {
        String raw = raw(key, player);
        if (raw == null) return null;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            raw = raw.replace("{" + e.getKey() + "}", e.getValue());
        }
        return raw;
    }

    /** Поиск по произвольному пути — используется InfoTranslator для GlobalTranslator. */
    public String rawByPath(String path, String locale) {
        return resolve(path, normalize(locale));
    }

    private String resolve(String path, String locale) {
        String value = get(locale, path);
        if (value == null) value = get(fallbackLocale, path);
        if (value == null) value = get(defaultLocale, path);
        return value;
    }

    private String get(String locale, String path) {
        Map<String, String> map = translations.get(locale);
        return map == null ? null : map.get(path);
    }

    private String normalize(String locale) {
        if (locale == null || locale.isEmpty()) return fallbackLocale;
        String[] parts = locale.split("_");
        if (parts.length == 1) return parts[0].toLowerCase(Locale.ROOT);
        return parts[0].toLowerCase(Locale.ROOT) + "_" + parts[1].toUpperCase(Locale.ROOT);
    }

    public void reload() {
        loadAll();
    }
}