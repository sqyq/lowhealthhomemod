# Low Health Home Mod

这是一个 Fabric  Minecraft Mod，当玩家生命值低于设定的阈值时自动发送 `/home` 命令，将玩家传送到安全地点。

## 功能

* 自动检测玩家生命值。
* 当生命值低于自定义阈值时触发 `/home` 命令。
* 可自定义低血量阈值和传送命令。

## 安装

1. 确保安装了 Minecraft Fabric API 和 Fabric Loader。
2. 将 `.jar` 文件放入 Minecraft 的 `mods` 文件夹中。

## 配置

* 在首次运行时，Mod 会自动在 `config/lowhealthhomemod.properties` 中生成配置文件。
* 配置选项：
  * `enableLowHealthTeleport`：启用/禁用功能（默认：`true`）。
  * `teleportCommand`：设置低血量时自动发送的命令（默认：`home Home` ）。
  * `lowHealthThreshold`：设置触发传送的生命值阈值（默认：`5`）。

## 注意

* 配置文件修改后需要重启客户端。
* 若被高伤害一击致死无法传送。


