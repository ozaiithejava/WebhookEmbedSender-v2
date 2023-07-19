
import com.squareup.okhttp.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscordEmbedSender {
    private static String webhookURL;

    /**
     * Ayarlanacak Discord webhook URL'sini belirler.
     *
     * @param url Discord webhook URL'si
     */
    public static void setWebhookURL(String url) {
        webhookURL = url;
    }

    /**
     * Discord'a gömülü bir mesaj gönderir.
     *
     * @param title         gömülü mesajın başlığı
     * @param description   gömülü mesajın açıklaması
     * @param color         gömülü mesajın rengi (RGB değerini 10'luk sistemde vermelisiniz)
     * @param madeByOzaii   gömülü mesajın altında "Made by Ozaii" ifadesini göstermek için true olarak ayarlayın
     * @param includeImage  gömülü mesaja resim eklemek istiyorsanız true olarak ayarlayın
     * @param imageUrl      eklemek istediğiniz resmin URL'si
     */
    public static void sendEmbed(String title, String description, String color, boolean madeByOzaii, boolean includeImage, String imageUrl) {
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String madeByText = "";
        if (madeByOzaii) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedTime = now.format(formatter);
            madeByText = "Made by Ozaii at " + formattedTime;
        }

        String json = "{"
                + "\"embeds\": [{"
                + "\"title\": \"" + title + "\","
                + "\"description\": \"" + description + "\","
                + "\"color\": " + color;

        if (includeImage) {
            json += ", \"image\": {"
                    + "\"url\": \"" + imageUrl + "\""
                    + "}";
        }

        json += ", \"footer\": {"
                + "\"text\": \"" + madeByText + "\""
                + "}"
                + "}]"
                + "}";

        RequestBody requestBody = RequestBody.create(mediaType, json);
        Request request = new Request.Builder()
                .url(webhookURL)
                .post(requestBody)
                .build();

        try {
            Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("Gömülü mesaj başarıyla gönderildi.");
            } else {
                System.out.println("Gömülü mesaj gönderimi başarısız oldu. HTTP kodu: " + response.code());
            }
        } catch (IOException e) {
            System.out.println("Gömülü mesaj gönderirken bir hata oluştu: " + e.getMessage());
        }
    }
}
