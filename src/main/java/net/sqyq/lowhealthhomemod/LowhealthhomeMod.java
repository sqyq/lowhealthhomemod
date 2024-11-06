package net.sqyq.lowhealthhomemod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.loader.api.FabricLoader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Properties;

public class LowhealthhomeMod implements ModInitializer {
	public static final String MOD_ID = "lowhealthhomemod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private boolean enableLowHealthTeleport;
	private String teleportCommand;
	private float lowHealthThreshold;
	private boolean hasTeleported = false;

	@Override
	public void onInitialize() {
		loadConfig();

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			PlayerEntity player = MinecraftClient.getInstance().player;
			if (player != null) {
				checkHealthAndTeleport(player);
			}
		});
	}

	private void loadConfig() {
		Properties config = new Properties();
		File configFile = new File("config/lowhealthhomemod.properties");

		createConfigFile();

		// 加载配置文件
		try (FileInputStream input = new FileInputStream(configFile)) {
			config.load(input);
			enableLowHealthTeleport = Boolean.parseBoolean(config.getProperty("enableLowHealthTeleport", "true"));
			teleportCommand = config.getProperty("teleportCommand", "home home");
			lowHealthThreshold = Float.parseFloat(config.getProperty("lowHealthThreshold", "5.0"));
		} catch (IOException e) {
			enableLowHealthTeleport = true;
			teleportCommand = "home home";
			lowHealthThreshold = 5.0f;
		}
	}

	private void checkHealthAndTeleport(PlayerEntity player) {
		if (!enableLowHealthTeleport) return; // 如果功能被禁用，直接返回

		if (player.getHealth() <= lowHealthThreshold && !hasTeleported) {
			player.sendMessage(Text.literal("Attempt to teleport.../" + teleportCommand), false);
            if (MinecraftClient.getInstance().player != null) {
                MinecraftClient.getInstance().player.networkHandler.sendChatCommand(teleportCommand); // 使用自定义命令
            }
            hasTeleported = true;
		} else if (player.getHealth() > lowHealthThreshold) {
			hasTeleported = false;
		}
	}
	private void createConfigFile() {
		Path configPath = FabricLoader.getInstance().getConfigDir().resolve("lowhealthhomemod.properties");
		File configFile = configPath.toFile();

		// 如果配置文件不存在，则创建文件及其父目录
		if (!configFile.exists()) {
			try {
				// 确保父目录存在
				File parentDir = configFile.getParentFile();
				if (!parentDir.exists() && !parentDir.mkdirs()) {
					LOGGER.warn("Insufficient permissions");
				}

				// 创建配置文件
				if (configFile.createNewFile()) {
					LOGGER.info("The profile has been created");
					setDefaultConfigValues(configFile);
				} else {
					LOGGER.warn("The profile already exists");
				}
			} catch (IOException e) {
				LOGGER.error("I/O errors", e);
			} catch (SecurityException e) {
				LOGGER.error("Insufficient permissions", e);
			}
		}
	}
	private void setDefaultConfigValues(File configFile) {
		Properties config = new Properties();
		config.setProperty("enableLowHealthTeleport", "true");
		config.setProperty("teleportCommand", "home home");
		config.setProperty("lowHealthThreshold", "5.0");

		// 保存默认配置到文件
		try (FileOutputStream output = new FileOutputStream(configFile)) {
			config.store(output, "Low Health Home Mod Configuration");
		} catch (IOException e) {
			LOGGER.error("Unable to save configuration to file", e);
		}
	}
}