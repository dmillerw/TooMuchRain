package dmillerw.rain.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;

/**
 * @author dmillerw
 */
public class NotifyUtil {

	public static void notifyOPs(String message) {
		ServerConfigurationManager configurationManager = MinecraftServer.getServer().getConfigurationManager();

		if (configurationManager != null) {
			for (Object obj : configurationManager.playerEntityList) {
				EntityPlayer player = (EntityPlayer) obj;
				if (configurationManager.func_152596_g(player.getGameProfile())) {
					player.addChatComponentMessage(new ChatComponentText(message));
				}
			}
		}
	}
}
