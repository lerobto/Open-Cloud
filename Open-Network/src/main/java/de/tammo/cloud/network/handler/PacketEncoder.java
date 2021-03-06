/*
 * Copyright (c) 2018, Open-Cloud-Services and contributors
 *
 * The code is licensed under the MIT License, which can be found in the root directory of the repository.
 */

package de.tammo.cloud.network.handler;

import de.tammo.cloud.network.packet.Packet;
import de.tammo.cloud.network.registry.PacketRegistry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	protected void encode(final ChannelHandlerContext ctx, final Packet packet, final ByteBuf byteBuf) throws Exception {
		final int id = PacketRegistry.getIdByPacket(packet, PacketRegistry.PacketDirection.OUT);

		if (id == -1) {
			new NullPointerException("Could not get id from packet " + packet.getClass().getSimpleName() + "!").printStackTrace();
		} else {
			byteBuf.writeInt(id);
			packet.write(new ByteBufOutputStream(byteBuf));
		}
	}

}
