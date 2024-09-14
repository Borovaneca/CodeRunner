package bg.coderunner.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class EmbeddedMessages {

    public static MessageEmbed generateAiResponse(String response, String avatarUrl) {
        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setDescription(response)
                .setAuthor("CodeRunner", null, avatarUrl)
                .setFooter("- AI generated response")
                .build();
    }

    public static MessageEmbed generateErrorResponse(String title, String errorMessage, String avatarUrl) {
        return new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle(title)
                .setDescription(errorMessage)
                .setAuthor("CodeRunner", null, avatarUrl)
                .setFooter("Error Handler")
                .build();
    }
}
