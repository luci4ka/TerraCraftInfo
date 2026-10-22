# TerraCraftInfo

[![Version](https://img.shields.io/badge/version-1.0.1-blue.svg)](https://github.com/luci4ka/TerraCraftInfo/releases)
[![Paper](https://img.shields.io/badge/Paper-26.3-orange.svg)](https://papermc.io/)
[![Java](https://img.shields.io/badge/Java-25-red.svg)](https://adoptium.net/)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)

Лёгкий информационный плагин для Paper-серверов: правила, помощь, информация и ссылки — всё в одном месте, с полной локализацией под клиент игрока и поддержкой MiniMessage.

---

## ✨ Возможности

- 📜 **`/rules`** — правила сервера с пагинацией
- ❓ **`/help`** — команды по группам LuckPerms, локализованные описания в `/help <cmd>`
- ℹ️ **`/info`** — информация о сервере (с PlaceholderAPI)
- 🔗 **`/links`** — кликабельные ссылки на Discord / сайт / донат
- 🌍 **Мультиязычность** — контент и тексты на языке игрока (по языку клиента игры), с fallback-цепочкой
- 🎨 **MiniMessage** — HEX-цвета, градиенты, клики, ховеры
- 🔌 **PlaceholderAPI** — любые плейсхолдеры в текстах, безопасно через `unparsed` (не ломают MiniMessage)
- ⚙️ **ConfigLib** — record-based YAML с комментариями
- 🚀 **CommandAPI** — современная регистрация команд, локализованные help-топики
- 🔄 **Auto-update** — проверка обновлений через GitHub
- 📊 **bStats** — анонимная статистика

## 📋 Требования

| Компонент | Версия |
|---|---|
| **Java** | 25+ |
| **Paper** | 26.3+ |
| **CommandAPI** | 12.1.0+ (плагин на сервере) |
| **PlaceholderAPI** | 2.12.3+ *(опционально)* |
| **LuckPerms** | любая *(опционально, для групп в `/help`)* |

## 🚀 Установка

1. Скачай `TerraCraftInfo-x.x.x.jar` из [Releases](../../releases)
2. Установи на сервер [CommandAPI](https://modrinth.com/plugin/commandapi)
3. *(Опционально)* Установи [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) и [LuckPerms](https://luckperms.net)
4. Положи JAR в `plugins/`
5. Запусти сервер — конфиги создадутся автоматически
6. Отредактируй:
   - `plugins/TerraCraftInfo/config.yml` — настройки (язык, пагинация, strict-режим)
   - `plugins/TerraCraftInfo/content/ru_RU/*.yml` — контент (правила, info, ссылки, help-группы)
   - `plugins/TerraCraftInfo/lang/ru_RU/*.yml` — тексты (заголовки, формат, ошибки)
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

## 📁 Структура данных

После первого запуска в `plugins/TerraCraftInfo/` появятся:

```
plugins/TerraCraftInfo/
├── config.yml                 # настройки
├── content/
│   ├── ru_RU/
│   │   ├── rules.yml          # список правил
│   │   ├── info.yml           # строки /info
│   │   ├── links.yml          # ссылки
│   │   └── help.yml           # группы команд для /help
│   └── en_US/
│       ├── rules.yml
│       ├── info.yml
│       ├── links.yml
│       └── help.yml
└── lang/
├── ru_RU/
│   ├── common.yml         # prefix, pagination, update
│   ├── rules.yml          # заголовки /rules
│   ├── info.yml           # заголовки /info
│   ├── help.yml           # заголовки /help
│   └── links.yml          # заголовки /links
└── en_US/
└── (те же файлы)
```

**Зачем два дерева:**
- `content/` — **что** показывать (правила, строки, ссылки, команды). Локализуется по клиенту.
- `lang/` — **как** показывать (заголовки, формат строк, кнопки навигации, ошибки). Локализуется по клиенту.

## ⚙️ Конфигурация

### `config.yml` — только настройки

```yaml
# Язык по умолчанию (для консоли и как fallback)
language: "ru_RU"

# Fallback язык, если ключ не найден в языке игрока
fallback-language: "en_US"

pagination:
  # Строк на страницу
  lines-per-page: 8

help:
  # true — группы без совпадения с LuckPerms скрываются
  # false — показываются все (отладка)
  strict-group-check: true
```

### `content/ru_RU/rules.yml` — правила

```yaml
# Каждый элемент — строка MiniMessage.
rules:
  - "<gold>1.</gold> <white>Не гриферить.</white>"
  - "<gold>2.</gold> <white>Не использовать читы.</white>"
  - "<gold>3.</gold> <white>Не оскорблять других игроков.</white>"
```

### `content/ru_RU/info.yml` — строки `/info`

```yaml
# Поддерживает PlaceholderAPI.
info:
  - "<gray>Сервер:</gray> <white>TerraCraft</white>"
  - "<gray>Онлайн:</gray> <white>%server_online%</white>"
  - "<gray>Версия:</gray> <white>1.21+</white>"
```

### `content/ru_RU/links.yml` — ссылки

```yaml
links:
  - display: "<aqua>Discord</aqua>"
    url: "https://discord.gg/terracraft"
  - display: "<green>Сайт</green>"
    url: "https://terracraft.ru"
  - display: "<gold>Донат</gold>"
    url: "https://terracraft.ru/donate"
```

### `content/ru_RU/help.yml` — группы `/help`

```yaml
# Ключ — группа LuckPerms. 'default' видна всем.
groups:
  default:
    - cmd: "/rules"
      description: "Правила сервера"
      aliases: ["/правила"]
    - cmd: "/info"
      description: "Информация о сервере"
      aliases: ["/инфо"]
    - cmd: "/links"
      description: "Наши ссылки"
      aliases: ["/ссылки"]
    - cmd: "/help"
      description: "Список команд"
      aliases: ["/помощь"]
  vip:
    - cmd: "/fly"
      description: "Полёт (VIP)"
      aliases: []
```

### `lang/ru_RU/common.yml` — общие ключи

```yaml
prefix: "<gradient:#00c6ff:#0072ff><bold>TerraCraft</bold></gradient> <dark_gray>»</dark_gray> "

pagination.header: "<gold>─── {title} ({page}/{total}) ───</gold>"
pagination.footer: "<gray>→ /{command} {next_page} для следующей страницы</gray>"
pagination.next_label: "▶"
pagination.prev_label: "◀"
pagination.page_indicator: " {page} / {total} "
pagination.hover_next: "<gray>Следующая страница</gray>"
pagination.hover_prev: "<gray>Предыдущая страница</gray>"

update.available: "<gold>⚡ Доступно обновление: <white>{version}</white> <gray>({url})</gray>"
```

### `lang/ru_RU/rules.yml` — заголовки `/rules`

```yaml
rules.header: "<gold><bold>📜 Правила сервера</bold></gold>"
rules.footer: "<gray>Незнание правил не освобождает от ответственности.</gray>"
rules.help.short: "Правила сервера"
rules.help.full: "Показывает список правил сервера с постраничной навигацией."
```

Аналогично `info.yml`, `help.yml`, `links.yml` в `lang/ru_RU/`.

## 🌍 Локализация

Каждый игрок получает сообщения на **своём языке** — плагин смотрит на локаль клиента Minecraft (`ru_RU`, `en_US`, `uk_UA`, ...). Fallback-цепочка:

1. Локаль игрока (например, `de_DE`)
2. `fallback-language` из `config.yml` (по умолчанию `en_US`)
3. `language` из `config.yml` (по умолчанию `ru_RU`)

Если ключ не найден ни в одной локали — в консоль уйдёт warning, а игрок увидит пустое сообщение. Поэтому при добавлении языка **копируйте все ключи** из `ru_RU`/`en_US`.

### Добавление языка

1. Скопируй `content/en_US/` в `content/de_DE/` и переведи контент.
2. Скопируй `lang/en_US/` в `lang/de_DE/` и переведи тексты.
3. Перезапусти сервер или `/reload confirm`.
4. Игрок с локалью `de_DE` автоматически получит немецкий.

## 🎨 PlaceholderAPI

Если плагин установлен, все плейсхолдеры работают во **всех** текстах — правилах, info, help, links.

Примеры:
- `%server_online%` — онлайн сервера
- `%server_tps_1_colored%` — TPS
- `%server_version%` — версия сервера
- `%luckperms_primary_group_name%` — группа игрока
- `%player_name%` — ник игрока

PAPI-значения вставляются через `TagResolver.unparsed`, поэтому если expansion вернёт строку с `<` или `>`, она **не сломает** MiniMessage-разметку — будет показана как текст.

Для LuckPerms-плейсхолдеров:
```
/papi ecloud download LuckPerms
/papi reload
```

## 👥 Группы в `/help`

Команды в `/help` разбиты по группам LuckPerms. Игрок видит только те, что ему доступны.

- `default` — видна **всем**
- Остальные группы — только тем, у кого есть `group.<name>` (через LuckPerms)
- Если `strict-group-check: false` — показываются все группы (для отладки)
- Если LuckPerms не установлен — показываются все группы
- Консоль видит только `default` (при `strict-group-check: true`), либо все (при `false`)

### Локализованные help-топики

`/help <cmd>` показывает описание команды на языке игрока. Для этого каждая команда регистрируется с `LocalizedHelpTopic`, который берёт текст из `lang/{locale}/{cmd}.yml` (`*.help.short` / `*.help.full`). Это работает благодаря тому, что CommandAPI 12.x принимает `org.bukkit.help.HelpTopic` в `.withHelp(...)`.

## 🔨 Сборка из исходников

```bash
git clone https://github.com/luci4ka/TerraCraftInfo.git
cd TerraCraftInfo
./gradlew clean shadowJar
```

Готовый JAR: `build/libs/TerraCraftInfo-x.x.x.jar`

### Стек

- **Java 25**
- **Paper 26.3** (`paper-api:26.3.build.+`)
- **ConfigLib** 4.8.1 — record-based YAML
- **CommandAPI** 12.1.0 — команды и help-топики
- **PlaceholderAPI** 2.12.3 — плейсхолдеры (опционально)
- **mc-plugin-update-notifier** 1.0.0 — GitHub-updates
- **bStats** 3.2.1 — статистика

### Структура проекта

```
TerraCraftInfo/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── LICENSE
├── README.md
└── src/main/
    ├── java/ru/terracraft/info/
    │   ├── TerraCraftInfoPlugin.java
    │   ├── command/
    │   │   ├── RulesCommand.java
    │   │   ├── HelpCommand.java
    │   │   ├── InfoCommand.java
    │   │   └── LinksCommand.java
    │   ├── config/
    │   │   └── InfoConfig.java
    │   ├── content/
    │   │   └── ContentManager.java
    │   ├── lang/
    │   │   ├── Lang.java
    │   │   └── LangManager.java
    │   └── util/
    │       ├── LocalizedHelpTopic.java
    │       ├── MessageUtil.java
    │       ├── PaginationUtil.java
    │       └── UpdateNotifier.java
    └── resources/
        ├── paper-plugin.yml
        ├── content/
        │   ├── ru_RU/{rules,info,links,help}.yml
        │   └── en_US/{rules,info,links,help}.yml
        └── lang/
            ├── ru_RU/{common,rules,info,help,links}.yml
            └── en_US/{common,rules,info,help,links}.yml
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

---

Сделано с ❤️