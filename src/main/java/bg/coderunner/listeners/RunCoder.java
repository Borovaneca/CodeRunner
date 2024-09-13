package bg.coderunner.listeners;

import bg.coderunner.utils.LanguageDetector;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class RunCoder extends ListenerAdapter {


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        Message msg = event.getMessage();
        String content = msg.getContentRaw();

        if (content.startsWith("!run")) {
            String[] result = removeBackticksAndLanguage(content.replace("!run", "").trim());
            String code = result[0];
            String language = result[1].isEmpty() ? LanguageDetector.detectLanguage(code) : result[1];
            if (!code.isEmpty()) {
                try {
                    String executionResult = executeCode(code, language);
                    sendEmbedMessage(event.getChannel(), language, executionResult, event.getMember().getAsMention());
                } catch (Exception e) {
                    event.getChannel().sendMessage("An error occurred while executing your code.").queue();
                }
            } else {
                event.getChannel().sendMessage("Please provide code to run!").queue();
            }
        }
    }

    private String executeCode(String code, String language) throws IOException {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();

        JSONArray filesArray = new JSONArray();
        JSONObject fileObject = new JSONObject();
        fileObject.put("name", getFileNameForLanguage(language));
        fileObject.put("content", code);
        filesArray.put(fileObject);

        json.put("language", language);
        json.put("version", getLanguageVersion(language));
        json.put("files", filesArray);

        RequestBody body = RequestBody.create(json.toString().getBytes(StandardCharsets.UTF_8), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url("https://emkc.org/api/v2/piston/execute").post(body).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return "Error occurred while executing the code";
            }

            String jsonResponseStr = new String(response.body().bytes(), StandardCharsets.UTF_8);

            JSONObject jsonResponse = new JSONObject(jsonResponseStr);

            JSONObject run = jsonResponse.getJSONObject("run");
            String stdout = run.optString("stdout", "");
            String stderr = run.optString("stderr", "");

            if (!stderr.isEmpty()) {
                return "Error: " + stderr;
            }
            return stdout.isEmpty() ? "No output" : stdout;
        }
    }


    private String[] removeBackticksAndLanguage(String code) {
        String language = "";
        if (code.startsWith("```") && code.endsWith("```")) {
            String[] lines = code.substring(3, code.length() - 3).split("\n", 2);
            language = lines[0].trim().toLowerCase();
            if (lines.length > 1) {
                code = lines[1].trim();
            } else {
                code = "";
            }
        } else if (code.startsWith("`") && code.endsWith("`")) {
            code = code.substring(1, code.length() - 1);
        }

        return new String[]{code.trim(), language};
    }

    private String getLanguageVersion(String language) {
        return switch (language) {
            case "java" -> "15.0.2";
            case "python" -> "3.10.0";
            case "javascript" -> "18.15.0";
            case "c++" -> "10.2.0";
            case "csharp" -> "6.12.0";
            default -> "3.10.0";
        };
    }

    private String getFileNameForLanguage(String language) {
        return switch (language) {
            case "java" -> "Main.java";
            case "python" -> "main.py";
            case "javascript" -> "main.js";
            case "c++" -> "main.cpp";
            case "csharp" -> "Main.cs";
            default -> "main.txt";
        };
    }

    private void sendEmbedMessage(MessageChannel channel, String language, String executionResult, String userAsMentioned) {
        if (executionResult.startsWith("Error")) {
            sendErrorMessage(channel, executionResult);
            return;
        }
        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail("https://cdn.discordapp.com/avatars/1283371499197300738/498b8d6cd94b4324e780128667305b5d.png");
        embed.setColor(0x00ff00);
        embed.addField("Language Detected", language, false);
        embed.addField("Result", executionResult.isEmpty() ? "No output" : "```\n" + executionResult + "\n```", false);
        embed.setFooter("Executed by CodeRunner", "https://cdn.discordapp.com/avatars/1283371499197300738/498b8d6cd94b4324e780128667305b5d.png");

        channel.sendMessageEmbeds(embed.build()).queue();
    }

    private void sendErrorMessage(MessageChannel channel, String error) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Error");
        embed.setColor(0xff0000);
        embed.addField("Message", error, false);
        embed.setFooter("CodeRunner Bot", "https://cdn.discordapp.com/avatars/1283371499197300738/498b8d6cd94b4324e780128667305b5d.png");

        channel.sendMessageEmbeds(embed.build()).queue();
    }
}
