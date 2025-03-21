package backslashashley.camera_mode.command;

import backslashashley.camera_mode.util.CameraPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.AbstractCommand;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.world.GameMode;

public class CameraCommand extends AbstractCommand {
	@Override
	public String getName() {
		return "cam";
	}

	@Override
	public String getUsage(CommandSource source) {
		return "cam";
	}

	@Override
	public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
		if (!(source instanceof ServerPlayerEntity)) {
			throw new CommandException("Unknown " + source.getName() + " tried to run /cam!");
		}

		ServerPlayerEntity player = asPlayer(source);
		CameraPlayer cameraPlayer = (CameraPlayer) player;

		if (!player.onGround && !cameraPlayer.isCameraMode()) {
			throw new CommandException("Must be on solid ground");
		}
		if (player.isOnFire() && !cameraPlayer.isCameraMode()) {
			throw new CommandException("Must not be on fire");
		}
		if (player.isInWall() && !cameraPlayer.isCameraMode()) {
			throw new CommandException("Cannot be suffocating");
		}

		if (!cameraPlayer.isCameraMode()) { //Enter camera mode
			cameraPlayer.setCameraMode(true);
			cameraPlayer.setSurvivalX(player.x);
			cameraPlayer.setSurvivalY(player.y);
			cameraPlayer.setSurvivalZ(player.z);
			cameraPlayer.setSurvivalYaw(player.yaw);
			cameraPlayer.setSurvivalPitch(player.pitch);

			player.setGameMode(GameMode.SPECTATOR);
		} else { //Exit camera mode
			cameraPlayer.setCameraMode(false);
			player.networkHandler.teleport(
				cameraPlayer.getSurvivalX(),
				cameraPlayer.getSurvivalY(),
				cameraPlayer.getSurvivalZ(),
				cameraPlayer.getSurvivalYaw(),
				cameraPlayer.getSurvivalPitch());

			player.setGameMode(GameMode.SURVIVAL);
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
}
