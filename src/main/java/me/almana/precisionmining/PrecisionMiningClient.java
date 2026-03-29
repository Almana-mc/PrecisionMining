package me.almana.precisionmining;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Precisionmining.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PrecisionMiningClient {
    private static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "key.precisionmining.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "key.categories.precisionmining"
    );

    private PrecisionMiningClient() {
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY);
    }

    @EventBusSubscriber(modid = Precisionmining.MODID, value = Dist.CLIENT)
    public static final class GameEvents {
        private GameEvents() {
        }

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            while (TOGGLE_KEY.consumeClick()) {
                boolean enabled = PrecisionMiningState.toggle();
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.player != null) {
                    minecraft.player.displayClientMessage(buildToggleMessage(enabled), true);
                }
            }
        }

        private static Component buildToggleMessage(boolean enabled) {
            MutableComponent state = Component.translatable(
                    enabled ? "message.precisionmining.state_on" : "message.precisionmining.state_off"
            ).withStyle(enabled ? ChatFormatting.GREEN : ChatFormatting.RED, ChatFormatting.BOLD);

            return Component.empty()
                    .append(Component.translatable("message.precisionmining.prefix").withStyle(ChatFormatting.WHITE))
                    .append(Component.literal(" "))
                    .append(state)
                    .append(Component.literal("  "))
                    .append(Component.translatable("message.precisionmining.hint", TOGGLE_KEY.getTranslatedKeyMessage().copy().withStyle(ChatFormatting.YELLOW))
                            .withStyle(ChatFormatting.GRAY));
        }
    }
}
