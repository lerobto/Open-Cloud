/*
 * Copyright (c) 2018. File created by Tammo
 */

package de.tammo.cloud.master.commands;

import de.tammo.cloud.command.Command;
import de.tammo.cloud.core.logging.Logger;
import de.tammo.cloud.master.Master;
import de.tammo.cloud.master.servergroup.ServerGroup;

@Command.CommandInfo(name = "group", aliases = {"servergroup", "sg"})
public class ServerGroupCommand implements Command {

	public final boolean execute(final String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) {
				Logger.info("<-- Server Groups -->");
				Master.getMaster().getServerGroupHandler().getServerGroups().forEach(serverGroup -> Logger.info("Servergroup: " + serverGroup.getName()));
				Logger.info("");
				return true;
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("delete")) {
				final ServerGroup serverGroup = Master.getMaster().getServerGroupHandler().getServerGroupByName(args[1]);
				if (serverGroup == null) {
					Logger.warn("Can not find this server group!");
				} else {
					Master.getMaster().getServerGroupHandler().removeServerGroup(serverGroup);
					Logger.info("Deleted this server group!");
				}
				return true;
			}
		} else if (args.length == 6) {
			if (args[0].equalsIgnoreCase("create")) {
				final String name = args[1];
				try {
					final int minServer = Integer.parseInt(args[2]);
					try {
						final int maxServer = Integer.parseInt(args[3]);
						try {
							final int minRam = Integer.parseInt(args[4]);
							try {
								final int maxRam = Integer.parseInt(args[5]);
								Master.getMaster().getServerGroupHandler().addServerGroup(new ServerGroup(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase(), minServer, maxServer, minRam, maxRam));
								Master.getMaster().getServerGroupHandler().getServerGroups().forEach(ServerGroup::init);
								Logger.info("Created server group with name " + name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
							} catch (final NumberFormatException e) {
								Logger.warn("Maximum of ram must be a number!");
							}
						} catch (final NumberFormatException e) {
							Logger.warn("Minimum of ram must be a number!");
						}
					} catch (final NumberFormatException e) {
						Logger.warn("Maximum of servers must be a number!");
					}
				} catch (final NumberFormatException e) {
					Logger.warn("Minimum of servers must be a number!");
				}
				return true;
			}
		}
		return false;
	}

	public void printHelp() {
		Logger.info("group list");
		Logger.info("group create <name> <min server> <max server> <min ram> <max ram>");
		Logger.info("group delete <name>");
	}
}
