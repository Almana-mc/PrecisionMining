package me.almana.precisionmining;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.lwjgl.glfw.GLFW;
//? if >=26.1
import net.minecraft.resources.Identifier;

public final class PrecisionMiningClient {
    public static final String MODID = "precisionmining";

    //? if >=26.1 {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath(MODID, "precisionmining")
    );
    //?} else
    /*private static final String CATEGORY = "key.categories.precisionmining";*/

    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "key.precisionmining.toggle",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            CATEGORY
    );

    private PrecisionMiningClient() {
    }

    public static void tick() {
        while (TOGGLE_KEY.consumeClick()) {
            boolean enabled = PrecisionMiningState.toggle();
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                //? if >=26.1 {
                player.sendOverlayMessage(buildToggleMessage(enabled));
                //?} else
                /*player.displayClientMessage(buildToggleMessage(enabled), true);*/
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
