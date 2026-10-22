package ru.terracraft.info.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;
import org.jetbrains.annotations.NotNull;
import ru.terracraft.info.TerraCraftInfoPlugin;
import ru.terracraft.info.lang.Lang;

public class LocalizedHelpTopic extends HelpTopic {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacySection();

    private final TerraCraftInfoPlugin plugin;
    private final String commandName;
    private final Lang shortKey;
    private final Lang fullKey;

    public LocalizedHelpTopic(TerraCraftInfoPlugin plugin, String commandName, Lang shortKey, Lang fullKey) {
        this.plugin = plugin;
        this.commandName = commandName;
        this.shortKey = shortKey;
        this.fullKey = fullKey;
    }

    @Override
    public @NotNull String getName() {
        return commandName;
    }

    @Override
    public @NotNull String getShortText() {
        return toLegacy(plugin.getLangManager().raw(shortKey, (Player) null));
    }

    @Override
    public @NotNull String getFullText(@NotNull CommandSender forWho) {
        String raw = forWho instanceof Player player
                ? plugin.getLangManager().raw(fullKey, player)
                : plugin.getLangManager().raw(fullKey, (Player) null);
        return toLegacy(raw);
    }

    @Override
    public boolean canSee(@NotNull CommandSender player) {
        return true;
    }

    private String toLegacy(String miniMessage) {
        if (miniMessage == null) return "";
        return LEGACY.serialize(MM.deserialize(miniMessage));
    }
}