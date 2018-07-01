/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.wrapper.network.packets.out;

import de.tammo.cloud.network.packet.Packet;
import io.netty.buffer.ByteBufOutputStream;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
public class ServerInfoAddOutPacket implements Packet {

	private final String serverGroup;

	private final UUID uuid;

	private final int port;

	public void write(final ByteBufOutputStream byteBuf) throws IOException {
		byteBuf.writeUTF(this.serverGroup);
		byteBuf.writeLong(this.uuid.getMostSignificantBits());
		byteBuf.writeLong(this.uuid.getLeastSignificantBits());
		byteBuf.writeInt(this.port);
	}

}