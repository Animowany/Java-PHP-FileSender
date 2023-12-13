import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TestUploader {
    private static final String EOL = "\r\n";

    public static int send(String filename, String url, String method) throws IOException {
        try (FileInputStream file = new FileInputStream(filename)) {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            final String boundary = UUID.randomUUID().toString();
            con.setDoOutput(true);
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            try (OutputStream out = con.getOutputStream()) {
                out.write(("--" + boundary + EOL +
                    "Content-Disposition: form-data; name=\"file\"; " +
                    "filename=\"" + filename + "\"" + EOL +
                    "Content-Type: application/octet-stream" + EOL + EOL)
                    .getBytes(StandardCharsets.UTF_8)
                );
                byte[] buffer = new byte[128];
                int size = -1;
                while (-1 != (size = file.read(buffer))) {
                    out.write(buffer, 0, size);
                }
                out.write((EOL + "--" + boundary + "--" + EOL).getBytes(StandardCharsets.UTF_8));
                out.flush();
                System.err.println(con.getResponseMessage());
                return con.getResponseCode();
            } finally {
                con.disconnect();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String filename = "plik.txt";
        String url = "http://localhost/index.php";
        int res = send(filename, url, "POST");
        if (res == HttpURLConnection.HTTP_OK) {
            System.err.println("Success!");
        } else {
            System.err.printf("Failed %d\n", res);
        }
    }
}