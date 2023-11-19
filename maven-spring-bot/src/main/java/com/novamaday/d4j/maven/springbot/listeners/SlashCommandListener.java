package com.novamaday.d4j.maven.springbot.listeners;

import com.novamaday.d4j.maven.springbot.MdcAccessor;
import com.novamaday.d4j.maven.springbot.commands.SlashCommand;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import io.micrometer.context.ContextRegistry;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collection;
import java.util.List;

@Component
public class SlashCommandListener {

    private final Collection<SlashCommand> commands;

    public SlashCommandListener(List<SlashCommand> slashCommands, GatewayDiscordClient client) {
        commands = slashCommands;

        ContextRegistry.getInstance().registerThreadLocalAccessor(new MdcAccessor());

        ContextRegistry.getInstance().registerThreadLocalAccessor(
            "key",
            () -> MDC.get("key"),
            value -> MDC.put("key", value),
            () -> MDC.remove("key"));

        client.on(ChatInputInteractionEvent.class, this::handle).subscribe();
    }


    public Mono<Void> handle(ChatInputInteractionEvent event) {
        //Convert our list to a flux that we can iterate through
        return Flux.fromIterable(commands)
            //Filter out all commands that don't match the name this event is for
            .filter(command -> command.getName().equals(event.getCommandName()))
            //Get the first (and only) item in the flux that matches our filter
            .next()
            //Have our command class handle all logic related to its specific command.
            .flatMap(command -> command.handle(event).contextWrite(
                Context.of("key", "Hello")));
    }
}
