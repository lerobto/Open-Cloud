/*
 * Copyright (c) 2018, Open-Cloud-Services and contributors
 *
 * The code is licensed under the MIT License, which can be found in the root directory of the repository.
 */

package de.tammo.cloud.wrapper.commands;

import de.tammo.cloud.command.Command;
import de.tammo.cloud.command.CommandProviderService;
import de.tammo.cloud.core.log.Logger;
import de.tammo.cloud.service.ServiceProvider;

@Command.CommandInfo(name = "help")
public class HelpCommand implements Command {

	public boolean execute(final String[] args) {
		Logger.info("<-- Help -->");
		ServiceProvider.getService(CommandProviderService.class).getCommands().forEach(command -> {
			if (command.getClass().isAnnotationPresent(CommandInfo.class)) {
				final String name = command.getClass().getAnnotation(CommandInfo.class).name();
				if (!name.equalsIgnoreCase("help")) {
					Logger.info(name);
				}
			}
		});
		Logger.info("");
		return false;
	}

}
