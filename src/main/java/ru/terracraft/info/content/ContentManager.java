package ru.terracraft.info.content;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import ru.terracraft.info.TerraCraftInfoPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Level;

public class ContentManager {

    private final TerraCraftInfoPlugin plugin;
    private final String defaultLocale;
    private final String fallbackLocale;

    private final Map<String, Content> content = new HashMap<>();

    public ContentManager(TerraCraftInfoPlugin plugin, String defaultLocale, String fallbackLocale) {
        this.plugin = plugin;
        this.defaultLocale = defaultLocale;
        this.fallbackLocale = fallbackLocale;
        loadAll();
    }

    private void loadAll() {
        content.clear();

        File root = new File(plugin.getDataFolder(), "content");
        if (!root.exists()) {
            root.mkdirs();
            saveDefault("content/ru_RU", "rules.yml", "info.yml", "links.yml", "help.yml");
            saveDefault("content/en_US", "rules.yml", "info.yml", "links.yml", "help.yml");
        }

        File[] localeDirs = root.listFiles(File::isDirectory);
        if (localeDirs == null) return;

        for (File dir : localeDirs) {
            String locale = dir.getName();
            Content c = new Content(
                    readList(dir, "rules.yml", "rules"),
                    readList(dir, "info.yml", "info"),
                    readLinks(dir),
                    readGroups(dir)
            );
            content.put(locale, c);
            plugin.getLogger().info("Загружен контент " + locale
                    + " (rules=" + c.rules.size()
                    + ", info=" + c.info.size()
                    + ", links=" + c.links.size()
                    + ", groups=" + c.groups.size() + ")");
        }
    }

    private void saveDefault(String folder, String... files) {
        for (String file : files) {
            try {
                plugin.saveResource(folder + "/" + file, false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().log(Level.FINE, "Ресурс не найден: " + folder + "/" + file);
            }
        }
    }

    private List<String> readList(File dir, String fileName, String section) {
        File file = new File(dir, fileName);
        if (!file.exists()) return List.of();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        return cfg.getStringList(section);
    }

    private List<LinkEntry> readLinks(File dir) {
        File file = new File(dir, "links.yml");
        if (!file.exists()) return List.of();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        List<Map<?, ?>> raw = cfg.getMapList("links");
        List<LinkEntry> result = new ArrayList<>(raw.size());
        for (Map<?, ?> m : raw) {
            Object display = m.get("display");
            Object url = m.get("url");
            if (display == null || url == null) continue;
            result.add(new LinkEntry(display.toString(), url.toString()));
        }
        return result;
    }

    private Map<String, List<CommandEntry>> readGroups(File dir) {
        File file = new File(dir, "help.yml");
        if (!file.exists()) return Map.of();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection groupsSection = cfg.getConfigurationSection("groups");
        if (groupsSection == null) return Map.of();

        Map<String, List<CommandEntry>> groups = new LinkedHashMap<>();
        for (String group : groupsSection.getKeys(false)) {
            List<Map<?, ?>> raw = groupsSection.getMapList(group);
            List<CommandEntry> commands = new ArrayList<>(raw.size());
            for (Map<?, ?> m : raw) {
                Object cmd = m.get("cmd");
                Object desc = m.get("description");
                if (cmd == null) continue;
                List<String> aliases = new ArrayList<>();
                Object a = m.get("aliases");
                if (a instanceof List<?> list) {
                    for (Object o : list) aliases.add(String.valueOf(o));
                }
                commands.add(new CommandEntry(
                        String.valueOf(cmd),
                        desc == null ? "" : String.valueOf(desc),
                        aliases
                ));
            }
            groups.put(group, commands);
        }
        return groups;
    }

    public List<String> rules(Player player) { return resolve(player).rules; }
    public List<String> info(Player player) { return resolve(player).info; }
    public List<LinkEntry> links(Player player) { return resolve(player).links; }
    public Map<String, List<CommandEntry>> groups(Player player) { return resolve(player).groups; }

    private Content resolve(Player player) {
        String locale = player != null
                ? normalize(player.locale().toString())
                : defaultLocale;

        Content c = content.get(locale);
        if (c == null) c = content.get(fallbackLocale);
        if (c == null) c = content.get(defaultLocale);
        return c != null ? c : Content.EMPTY;
    }

    private String normalize(String locale) {
        if (locale == null || locale.isEmpty()) return fallbackLocale;
        String[] parts = locale.split("_");
        if (parts.length == 1) return parts[0].toLowerCase(Locale.ROOT);
        return parts[0].toLowerCase(Locale.ROOT) + "_" + parts[1].toUpperCase(Locale.ROOT);
    }

    public void reload() { loadAll(); }

    public record LinkEntry(String display, String url) {}
    public record CommandEntry(String cmd, String description, List<String> aliases) {}

    private record Content(
            List<String> rules,
            List<String> info,
            List<LinkEntry> links,
            Map<String, List<CommandEntry>> groups
    ) {
        static final Content EMPTY = new Content(List.of(), List.of(), List.of(), Map.of());
    }
}