package bg.coderunner.listeners;

import bg.coderunner.utils.EmbeddedMessages;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChatGPTListener extends ListenerAdapter {

    @Value("${AIBOT_TOKEN}")
    private String TOKEN;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        if (!message.startsWith("!chat")) return;

        message = message.substring("!chat ".length());
        OkHttpClient client = new OkHttpClient();

        JSONObject userMessageRequest = new JSONObject();
        userMessageRequest.put("role", "user");
        userMessageRequest.put("content", message);

        JSONArray messages = new JSONArray();
        messages.put(userMessageRequest);

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 200);
        requestBody.put("temperature", 0.7);

        RequestBody body = RequestBody.create(requestBody.toString(), MediaType.parse("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + TOKEN)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        event.getChannel().sendMessage("⏳ Preparing your response...").queue((Message preparingMessage) -> {

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    JSONArray choices = jsonResponse.getJSONArray("choices");
                    String reply = choices.getJSONObject(0).getJSONObject("message").getString("content");

                    event.getChannel().sendMessageEmbeds(
                            EmbeddedMessages.generateAiResponse(reply, event.getJDA().getSelfUser().getAvatarUrl())
                    ).queue();

                    preparingMessage.delete().queue();

                } else {
                    event.getChannel().sendMessageEmbeds(
                            EmbeddedMessages.generateErrorResponse(
                                    "API Call Failed",
                                    "The API request was not successful. Status Code: " + response.code(),
                                    event.getJDA().getSelfUser().getAvatarUrl())
                    ).queue();

                    preparingMessage.delete().queue();
                }
            } catch (Exception e) {
                e.printStackTrace();
                event.getChannel().sendMessageEmbeds(
                        EmbeddedMessages.generateErrorResponse(
                                "Error Encountered",
                                "An error occurred while processing your request: " + e.getMessage(),
                                event.getJDA().getSelfUser().getAvatarUrl())
                ).queue();

                preparingMessage.delete().queue();
            }
        });
    }
}
