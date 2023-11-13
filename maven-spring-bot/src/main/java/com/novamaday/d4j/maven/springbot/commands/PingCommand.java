package com.novamaday.d4j.maven.springbot.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class PingCommand implements SlashCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        Logger log = LoggerFactory.getLogger("reactor");

        MDC.put("key", "Hello");

        //We reply to the command with "Pong!" and make sure it is ephemeral (only the command user can see it)
        return event.reply()
            .withEphemeral(true)
            .withContent("Pong!")
            .doOnSuccess(result -> {
                log.info("Hi!");
            });
    }
}
