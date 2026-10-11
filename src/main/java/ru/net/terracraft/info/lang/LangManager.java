package ru.net.terracraft.info.lang;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.net.terracraft.info.TerraCraftInfoPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Отдаёт сообщения на языке игрока (по локали клиента).
 * Файлы: lang/{locale}.yml — ru_RU, en_US, uk_UA, ...
 * Fallback: en_US.
 */
public class LangManager {

    private final TerraCraftInfoPlugin plugin;
    private final Map<String, YamlConfiguration> translations = new HashMap<>();
    private final String defaultLocale;
    private final String fallbackLocale;

    public LangManager(TerraCraftInfoPlugin plugin, String defaultLocale, String fallbackLocale) {
        this.plugin = plugin;
        this.defaultLocale = defaultLocale;
        this.fallbackLocale = fallbackLocale;
        loadAll();
    }

    private void loadAll() {
        File langDir = new File(plugin.getDataFolder(), "lang");
        if (!langDir.exists()) {
            langDir.mkdirs();
            saveResource("lang/ru_RU.yml");
            saveResource("lang/en_US.yml");
            saveResource("lang/uk_UA.yml");
        }

        File[] files = langDir.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            String locale = file.getName().replace(".yml", "");
            translations.put(locale, YamlConfiguration.loadConfiguration(file));
        }
    }

    private void saveResource(String path) {
        try {
            plugin.saveResource(path, false);
        } catch (IllegalArgumentException ignored) {}
    }

    // ─── Основной API ─────────────────────────

    /**
     * Сырая строка для игрока (по его локали клиента).
     * Если игрок null — берётся defaultLocale.
     */
    public String raw(Lang key, Player player) {
        String locale = player != null
                ? normalize(player.locale().toString())
                : defaultLocale;

        String value = getFromLocale(key.getPath(), locale);
        if (value == null) value = getFromLocale(key.getPath(), fallbackLocale);
        if (value == null) value = getFromLocale(key.getPath(), defaultLocale);
        return value;
    }

    /**
     * Сырая строка для конкретной локали (например, для консоли).
     */
    public String raw(Lang key, String locale) {
        String normalized = normalize(locale);
        String value = getFromLocale(key.getPath(), normalized);
        if (value == null) value = getFromLocale(key.getPath(), fallbackLocale);
        if (value == null) value = getFromLocale(key.getPath(), defaultLocale);
        return value;
    }

    /**
     * Сырая строка с подстановками: raw(key, player).replace("{x}", "y")...
     */
    public String raw(Lang key, Player player, Map<String, String> placeholders) {
        String raw = raw(key, player);
        if (raw == null) return null;
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            raw = raw.replace("{" + e.getKey() + "}", e.getValue());
        }
        return raw;
    }

    private String getFromLocale(String path, String locale) {
        YamlConfiguration cfg = translations.get(locale);
        return cfg == null ? null : cfg.getString(path);
    }

    private String normalize(String locale) {
        if (locale == null || locale.isEmpty()) return fallbackLocale;
        String[] parts = locale.split("_");
        if (parts.length == 1) return parts[0].toLowerCase(Locale.ROOT);
        return parts[0].toLowerCase(Locale.ROOT) + "_" + parts[1].toUpperCase(Locale.ROOT);
    }

    public void reload() {
        translations.clear();
        loadAll();
    }
}