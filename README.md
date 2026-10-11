# TerraCraftInfo

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/luci/TerrEconomy/releases)
[![Paper](https://img.shields.io/badge/Paper-1.21.11-orange.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-25-red.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
---
Лёгкий информационный плагин для Paper-серверов: правила, помощь, информация и ссылки — всё в одном месте, с полной локализацией под клиент игрока и поддержкой MiniMessage.

## ✨ Возможности

- 📜 **`/rules`** — правила сервера с пагинацией
- ❓ **`/help`** — команды по группам LuckPerms
- ℹ️ **`/info`** — информация о сервере (с PlaceholderAPI)
- 🔗 **`/links`** — кликабельные ссылки на Discord / сайт / донат
- 🌍 **Мультиязычность** — каждый игрок получает сообщения на своём языке (по локали клиента)
- 🎨 **MiniMessage** — HEX-цвета, градиенты, клики, ховеры
- 🔌 **PlaceholderAPI** — любые плейсхолдеры в текстах
- ⚙️ **ConfigLib** — record-based YAML с комментариями
- 🚀 **CommandAPI** — современная регистрация команд
- 🔄 **Auto-update** — проверка обновлений через GitHub
- 📊 **bStats** — анонимная статистика

## 📋 Требования

| Компонент | Версия |
|---|---|
| **Java** | 25+ |
| **Paper** | 1.21.11+ |
| **CommandAPI** | 12.1.0+ (плагин на сервере) |
| **PlaceholderAPI** | 2.12.3+ *(опционально)* |
| **LuckPerms** | любая *(опционально, для групп в `/help`)* |

## 🚀 Установка

1. Скачай `TerraCraftInfo-x.x.x.jar` из [Releases](../../releases)
2. Установи на сервер [CommandAPI](https://modrinth.com/plugin/commandapi)
3. *(Опционально)* Установи [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) и [LuckPerms](https://luckperms.net)
4. Положи JAR в `plugins/`
5. Запусти сервер — конфиги создадутся автоматически
6. Отредактируй `plugins/TerraCraftInfo/config.yml` и файлы в `plugins/TerraCraftInfo/lang/`
7. `/reload confirm` или перезапуск

## 📖 Команды

| Команда | Алиасы | Описание | Права |
|---|---|---|---|
| `/rules [page]` | `/правила` | Показать правила | — |
| `/help` | `/помощь` | Помощь по командам | — |
| `/info [page]` | `/инфо` | Информация о сервере | — |
| `/links` | `/ссылки` | Наши ссылки | — |

### Права

| Право | Описание | По умолчанию |
|---|---|---|
| `terracraftinfo.update` | Уведомления об обновлениях | op |

## ⚙️ Конфигурация

### `config.yml` — контент

```yaml
language: "ru_RU"
fallbackLanguage: "en_US"

pagination:
  linesPerPage: 8

rules:
  list:
    - "<gold>1.</gold> <white>Не гриферить.</white>"
    - "<gold>2.</gold> <white>Не использовать читы.</white>"

info:
  lines:
    - "<gray>Сервер:</gray> <white>TerraCraft</white>"
    - "<gray>Онлайн:</gray> <white>%server_online%</white>"

links:
  entries:
    - name: "Discord"
      display: "<aqua>Discord</aqua>"
      url: "https://discord.gg/terracraft"

help:
  strictGroupCheck: true
  groups:
    default:
      commands:
        - cmd: "/rules"
          description: "Правила сервера"
          aliases: ["/правила"]
```

### `lang/*.yml` — все тексты

Каждый игрок получает сообщения на **своём языке** — плагин смотрит на локаль клиента Minecraft (`ru_RU`, `en_US`, `uk_UA`, ...). Если ключ не найден — берётся `fallbackLanguage`, потом `language`.

Поддерживаются:
- **MiniMessage** — `<gold>`, `<gradient:#00c6ff:#0072ff>`, `<bold>`, ...
- **PlaceholderAPI** — `%server_online%`, `%luckperms_primary_group_name%`, ...
- **Подстановки** — `{cmd}`, `{group}`, `{page}`, `{total}`, `{version}`, `{url}`, ...

Пример `lang/ru_RU.yml`:

```yaml
prefix: "<gradient:#00c6ff:#0072ff><bold>TerraCraft</bold></gradient> <dark_gray>»</dark_gray> "

rules:
  header: "<gold><bold>📜 Правила сервера</bold></gold>"
  footer: "<gray>Незнание правил не освобождает от ответственности.</gray>"

pagination:
  header: "<gold>─── {title} ({page}/{total}) ───</gold>"
  next_label: "▶"
  prev_label: "◀"
  page_indicator: " {page} / {total} "

error:
  no_permission: "<red>Недостаточно прав.</red>"
```

## 🌍 Добавление языка

1. Скопируй `lang/en_US.yml` в `lang/xx_XX.yml` (например, `de_DE.yml`)
2. Переведи значения
3. Перезапусти сервер (или `/reload confirm`)
4. Игрок с локалью `de_DE` автоматически получит немецкий

## 🎨 PlaceholderAPI

Если плагин установлен, все плейсхолдеры работают во **всех** текстах — правилах, info, help, links.

Примеры:
- `%server_online%` — онлайн сервера
- `%server_tps_1_colored%` — TPS
- `%server_version%` — версия сервера
- `%luckperms_primary_group_name%` — группа игрока
- `%player_name%` — ник игрока

Для LuckPerms-плейсхолдеров:
```
/papi ecloud download LuckPerms
/papi reload
```

## 👥 Группы в `/help`

Команды в `/help` разбиты по группам LuckPerms. Игрок видит только те, что ему доступны.

```yaml
help:
  strictGroupCheck: true
  groups:
    default:
      commands: [...]
    moderator:
      commands: [...]
    admin:
      commands: [...]
```

- `default` — видна **всем**
- Остальные группы — только тем, у кого есть `group.<name>` (через LuckPerms)
- Если `strictGroupCheck: false` — показываются все группы (для отладки)
- Если LuckPerms не установлен — показываются все группы

## 🔨 Сборка из исходников

```bash
git clone https://github.com/terracraft/TerraCraftInfo.git
cd TerraCraftInfo
./gradlew clean build
```

Готовый JAR: `build/libs/TerraCraftInfo-x.x.x.jar`

### Стек

- **Java 25**
- **Paper 1.21.11** (paperweight-userdev)
- **ConfigLib** 4.8.1 — record-based YAML
- **CommandAPI** 12.1.0 — команды
- **PlaceholderAPI** 2.12.3 — плейсхолдеры
- **mc-plugin-update-notifier** — GitHub-updates
- **bStats** 3.2.1 — статистика

## 📁 Структура

```
TerraCraftInfo/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── LICENSE
├── README.md
└── src/main/
    ├── java/ru/net/terracraft/info/
    │   ├── TerraCraftInfoPlugin.java
    │   ├── UpdateNotifier.java
    │   ├── command/
    │   │   ├── RulesCommand.java
    │   │   ├── HelpCommand.java
    │   │   ├── InfoCommand.java
    │   │   └── LinksCommand.java
    │   ├── config/
    │   │   └── InfoConfig.java
    │   ├── lang/
    │   │   ├── Lang.java
    │   │   └── LangManager.java
    │   └── util/
    │       ├── MessageUtil.java
    │       └── PaginationUtil.java
    └── resources/
        ├── paper-plugin.yml
        ├── config.yml
        └── lang/
            ├── ru_RU.yml
            ├── en_US.yml
            └── uk_UA.yml
```

## 🤝 Вклад

PR'ы приветствуются. Для крупных изменений — сначала открой issue для обсуждения.

1. Fork
2. Ветка (`git checkout -b feature/amazing`)
3. Коммит (`git commit -m 'Add amazing feature'`)
4. Push (`git push origin feature/amazing`)
5. Pull Request

## 📄 Лицензия

Проект распространяется под лицензией **MIT**. См. [LICENSE](LICENSE).

## 💬 Поддержка

- 🐛 [Issues](../../issues)
- 💬 [Discord](https://discord.gg/terracraft)
- 🌐 [terracraft.net.ru](https://terracraft.net.ru)

---

Сделано с ❤️ для **TerraCraft**