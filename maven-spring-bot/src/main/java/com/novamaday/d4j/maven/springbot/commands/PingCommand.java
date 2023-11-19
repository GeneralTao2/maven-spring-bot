package com.novamaday.d4j.maven.springbot.commands;

import com.novamaday.d4j.maven.springbot.OnCompleteSignalListenerBuilder;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        //We reply to the command with "Pong!" and make sure it is ephemeral (only the command user can see it)
        return event.reply()
            .withEphemeral(true)
            .withContent("Pong!")
            .tap(OnCompleteSignalListenerBuilder.of(
                () -> log.info("Pong!"))
            );
    }
}
