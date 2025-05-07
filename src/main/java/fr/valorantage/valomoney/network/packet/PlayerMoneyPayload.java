package fr.valorantage.valomoney.network.packet;

import fr.valorantage.valomoney.ValomoneyMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record PlayerMoneyPayload(float amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerMoneyPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ValomoneyMod.MODID, "player_money_payload"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'amount' will be encoded and decoded as a float
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, PlayerMoneyPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            PlayerMoneyPayload::amount,
            PlayerMoneyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
