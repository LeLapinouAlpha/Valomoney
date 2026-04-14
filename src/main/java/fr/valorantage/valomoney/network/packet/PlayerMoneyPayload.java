package fr.valorantage.valomoney.network.packet;

import fr.valorantage.valomoney.ValomoneyMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.UUID;

public record PlayerMoneyPayload(UUID playerUUID, float amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<PlayerMoneyPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ValomoneyMod.MODID, "player_money_payload"));

    public static final StreamCodec<ByteBuf, UUID> UUID_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG,
            UUID::getMostSignificantBits,
            ByteBufCodecs.VAR_LONG,
            UUID::getLeastSignificantBits,
            UUID::new
    );

    public static final StreamCodec<ByteBuf, PlayerMoneyPayload> STREAM_CODEC = StreamCodec.composite(
            UUID_CODEC,
            PlayerMoneyPayload::playerUUID,
            ByteBufCodecs.FLOAT,
            PlayerMoneyPayload::amount,
            PlayerMoneyPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
