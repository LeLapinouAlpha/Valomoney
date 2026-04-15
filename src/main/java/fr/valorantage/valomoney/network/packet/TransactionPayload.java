package fr.valorantage.valomoney.network.packet;

import fr.valorantage.valomoney.ValomoneyMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record TransactionPayload(TransactionKind kind, float value) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<TransactionPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ValomoneyMod.MODID, "transaction_payload"));

    public static final StreamCodec<FriendlyByteBuf, TransactionPayload> STREAM_CODEC = StreamCodec.composite(
            NeoForgeStreamCodecs.enumCodec(TransactionKind.class),
            TransactionPayload::kind,
            ByteBufCodecs.FLOAT,
            TransactionPayload::value,
            TransactionPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
