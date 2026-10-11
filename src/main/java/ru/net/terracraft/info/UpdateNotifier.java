package ru.net.terracraft.info;

import com.github.ezplugins.updater.UpdateChecker;
import com.github.ezplugins.updater.UpdateResult;
import org.bukkit.Bukkit;
import ru.net.terracraft.info.lang.Lang;

/**
 * Отдельный класс для проверки обновлений через GitHub.
 * Библиотека: com.github.ez-plugins:mc-plugin-update-notifier
 *
 * Настройки репозитория:
 *   - owner:  GitHub username/org
 *   - repo:   название репозитория
 *   - version: текущая версия плагина
 */
public class UpdateNotifier {

    private static final String GITHUB_OWNER = "luci4ka";
    private static final String GITHUB_REPO  = "TerraCraftInfo";

    private final TerraCraftInfoPlugin plugin;
    private UpdateResult lastResult;

    public UpdateNotifier(TerraCraftInfoPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Асинхронная проверка обновлений.
     * Уведомляет консоль и всех онлайн-админов с правом terracraftinfo.update.
     */
    public void checkAsync() {
        String currentVersion = plugin.getPluginMeta().getVersion();

        UpdateChecker checker = UpdateChecker.builder(GITHUB_OWNER, GITHUB_REPO, currentVersion)
                .build();

        checker.checkNowAsync().thenAccept(result -> {
            this.lastResult = result;

            if (result.hasError()) {
                plugin.getLogger().warning(
                        "Update check failed: " + result.getError().orElseThrow().getMessage());
                return;
            }

            if (result.isUpdateAvailable()) {
                String latest = result.getLatestTag().orElse("unknown");
                plugin.getLogger().info("Доступно обновление: " + latest);

                // Уведомляем админов в игре (в основном потоке)
                Bukkit.getScheduler().runTask(plugin, () ->
                        Bukkit.getOnlinePlayers().stream()
                                .filter(p -> p.hasPermission("terracraftinfo.update"))
                                .forEach(p -> {
                                    String msg = plugin.getLangManager()
                                            .raw(Lang.UPDATE_AVAILABLE, p)
                                            .replace("{version}", latest)
                                            .replace("{url}", result.getReleaseUrl().orElse(""));
                                    p.sendMessage(ru.net.terracraft.info.util.MessageUtil.parse(msg, p));
                                })
                );
            } else {
                plugin.getLogger().info("Обновлений нет. Версия актуальна.");
            }
        });
    }

    public UpdateResult getLastResult() {
        return lastResult;
    }
}