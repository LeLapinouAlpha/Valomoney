package fr.valorantage.valomoney.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ATMCreditPayload(float amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ATMCreditPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("valomoney", "atm_credit_payload"));

    // Each pair of elements defines the stream codec of the element to encode/decode and the getter for the element to encode
    // 'amount' will be encoded and decoded as a float
    // The final parameter takes in the previous parameters in the order they are provided to construct the payload object
    public static final StreamCodec<ByteBuf, ATMCreditPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            ATMCreditPayload::amount,
            ATMCreditPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
