package ru.net.terracraft.info.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.net.terracraft.info.lang.Lang;

import java.util.Map;

/**
 * Утилита: получить строку из LangManager + PAPI + MiniMessage → отправить.
 */
public final class MessageUtil {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final boolean PAPI =
            Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;

    private MessageUtil() {}

    /** Парсит MiniMessage + PAPI для игрока. */
    public static Component parse(String raw, Player player) {
        if (raw == null || raw.isEmpty()) return Component.empty();
        String text = raw;
        if (PAPI && player != null) {
            text = PlaceholderAPI.setPlaceholders(player, text);
        }
        return MM.deserialize(text);
    }

    /**
     * Отправить игроку сообщение по ключу Lang с подстановками.
     */
    public static void send(CommandSender sender, Lang key, Map<String, String> placeholders) {
        Player player = sender instanceof Player p ? p : null;
        String raw = TerraCraftInfoPluginHolder.lang().raw(key, player, placeholders);
        sender.sendMessage(parse(raw, player));
    }

    /** Отправить игроку сообщение по ключу Lang без подстановок. */
    public static void send(CommandSender sender, Lang key) {
        send(sender, key, Map.of());
    }

    /**
     * Отправить уже готовый текст (например, строку из config.yml с плейсхолдерами).
     */
    public static void sendRaw(CommandSender sender, String raw) {
        Player player = sender instanceof Player p ? p : null;
        sender.sendMessage(parse(raw, player));
    }

    /** Внутренний холдер, чтобы не тащить plugin в каждый вызов. */
    private static final class TerraCraftInfoPluginHolder {
        static ru.net.terracraft.info.lang.LangManager lang() {
            return ru.net.terracraft.info.TerraCraftInfoPlugin.getInstance().getLangManager();
        }
    }
}