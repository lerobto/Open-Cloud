/*
 * Copyright (c) 2018, Open-Cloud-Services and contributors
 *
 * The code is licensed under the MIT License, which can be found in the root directory of the repository.
 */

package de.tammo.cloud.network;

import de.tammo.cloud.core.threading.ThreadBuilder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.RequiredArgsConstructor;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class NettyServer {

	private final int port;

	private EventLoopGroup bossGroup;

	private EventLoopGroup workerGroup;

	private SslContext sslContext;

	private ChannelFuture future;

	private final boolean EPOLL = Epoll.isAvailable();

	public final NettyServer bind(final Runnable ready, final Consumer<Channel> init) {
		new ThreadBuilder("Netty-Client", () -> {
			this.bossGroup = this.EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
			this.workerGroup = this.EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();

			try {
				final ChannelFuture future = new ServerBootstrap()
						.group(this.bossGroup, this.workerGroup)
						.channel(this.EPOLL ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
						.childHandler(new ChannelInitializer<Channel>() {

					protected void initChannel(final Channel channel) {

						if (sslContext != null) channel.pipeline().addLast(sslContext.newHandler(channel.alloc()));
						init.accept(channel);
					}

				}).bind(this.port).syncUninterruptibly();

				ready.run();

				NettyServer.this.future = future;

				future.channel().closeFuture().syncUninterruptibly();
			} finally {
				this.workerGroup.shutdownGracefully();
				this.bossGroup.shutdownGracefully();
			}
		}).setDaemon().startThread();
		return this;
	}

	public final NettyServer withSSL() {
		try {
			final SelfSignedCertificate certificate = new SelfSignedCertificate();
			this.sslContext = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
			certificate.delete();
		} catch (CertificateException | SSLException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void close(final Runnable closed) {
		if (this.future.channel().isOpen()) {
			this.future.channel().close().syncUninterruptibly();
		}
		this.workerGroup.shutdownGracefully();
		this.bossGroup.shutdownGracefully();
		closed.run();
	}

}
