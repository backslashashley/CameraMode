package backslashashley.camera_mode.mixin;

import backslashashley.camera_mode.command.CameraCommand;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.handler.CommandListener;
import net.minecraft.server.command.handler.CommandManager;
import net.minecraft.server.command.handler.CommandRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CommandManager.class)
public abstract class MixinCommandManager extends CommandRegistry implements CommandListener {
	@Inject(
		method = "<init>",
		at = @At(
			value = "RETURN"
		)
	)
	private void initModdedCommand(MinecraftServer server, CallbackInfo ci) {
		this.register(new CameraCommand());
	}
}
