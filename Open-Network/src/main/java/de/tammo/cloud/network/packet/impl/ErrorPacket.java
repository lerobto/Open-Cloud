/*
 * Copyright (c) 2018, Open-Cloud-Services and contributors
 *
 * The code is licensed under the MIT License, which can be found in the root directory of the repository.
 */

package de.tammo.cloud.network.packet.impl;

import de.tammo.cloud.network.packet.Packet;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.Channel;

import java.io.IOException;

public class ErrorPacket implements Packet {

	private String error;

	public ErrorPacket() {}

	public ErrorPacket(final String error) {
		this.error = error;
	}

	public final Packet handle(final Channel channel) {
		return new SuccessPacket();
	}

	public void read(final ByteBufInputStream byteBuf) throws IOException {
		this.error = byteBuf.readUTF();
	}

	public void write(final ByteBufOutputStream byteBuf) throws IOException {
		byteBuf.writeUTF(this.error);
	}
}
