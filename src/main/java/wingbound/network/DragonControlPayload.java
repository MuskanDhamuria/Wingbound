package wingbound.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import wingbound.Wingbound;

public record DragonControlPayload(boolean rise, boolean descend, boolean attack) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<DragonControlPayload> TYPE =
		new CustomPacketPayload.Type<>(Wingbound.id("dragon_control"));

	public static final StreamCodec<RegistryFriendlyByteBuf, DragonControlPayload> CODEC = StreamCodec.composite(
		ByteBufCodecs.BOOL,
		DragonControlPayload::rise,
		ByteBufCodecs.BOOL,
		DragonControlPayload::descend,
		ByteBufCodecs.BOOL,
		DragonControlPayload::attack,
		DragonControlPayload::new
	);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
