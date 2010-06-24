
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleClient {

    static class NoAuthenticationException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    private final String username;
    private final String password;
    private final String host;
    private final int    port;
    private String       token;
    private String       cookie;

    public SimpleClient(final String username, final String password,
            final String host) {
        this(username, password, host, 80);
    }

    public SimpleClient(final String username, final String password,
            final String host, final int port) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
    }

    public String get(final String path) throws IOException {
        return get(path, null);
    }

    public String get(final String path, final String payload)
            throws IOException {
        final URL url = new URL("http://" + this.host + ":" + this.port + path);
        final HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        return action(con, payload);
    }

    public String post(final String path, final String payload)
            throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL("http://"
                + this.host + ":" + this.port + path).openConnection();
        con.setRequestMethod("POST");
        return action(con, payload);
    }

    public String put(final String path, final String payload)
            throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL("http://"
                + this.host + ":" + this.port + path).openConnection();
        con.setRequestMethod("PUT");
        return action(con, payload);
    }

    public String delete(final String path) throws IOException {
        return delete(path, null);
    }

    public String delete(final String path, final String payload)
            throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL("http://"
                + this.host + ":" + this.port + path).openConnection();
        con.setRequestMethod("DELETE");
        return action(con, payload);
    }

    private String action(final HttpURLConnection con, final String payload)
            throws IOException {
        try {
            return doAction(con, payload);
        }
        catch (final IOException e) {
            if (con.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                return doAction(con, payload);
            }
            else {
                throw e;
            }
        }
    }

    private String doAction(final HttpURLConnection con, final String payload)
            throws IOException {
        if (this.token == null || this.cookie == null) {
            authenticate();
        }
        con.setRequestProperty("Content-Type", "application/xml");
        con.setRequestProperty("Authentication-Token", this.token);
        con.setRequestProperty("Cookie", this.cookie);
        con.setDoInput(true);
        if (payload != null) {
            con.setDoOutput(true);
        }
        con.connect();

        if (payload != null) {
            // write out the payload
            final OutputStream out = con.getOutputStream();
            out.write(payload.getBytes("UTF-8"));
            out.flush();
        }

        // read in the response body
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(),
                "UTF-8"));
        final StringBuilder result = new StringBuilder();
        String line = in.readLine();
        while (line != null) {
            result.append(line).append("\n");
            line = in.readLine();
        }
        in.close();

        con.disconnect();

        // replace token with new if available
        final String token = con.getHeaderField("Authentication-Token");
        if (token != null) {
            this.token = token;
        }
        return result.toString();
    }

    private void authenticate() throws IOException {
        final HttpURLConnection con = (HttpURLConnection) new URL("http://"
                + this.host + ":" + this.port + "/authentication.xml").openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/xml");
        con.setDoOutput(true);
        con.connect();

        // write out the payload
        final OutputStream out = con.getOutputStream();
        out.write(("<authentication><login>" + this.username
                + "</login><password>" + this.password + "</password></authentication>").getBytes("UTF-8"));
        out.close();

        // get the session tracking data
        this.token = con.getHeaderField("Authentication-Token");
        this.cookie = con.getHeaderField("Set-Cookie").replaceFirst(";.*", "");
        con.disconnect();
    }

    public static void main(final String... args) throws IOException {
        final SimpleClient client = new SimpleClient("root",
                "w83zf#:q*QF4",
                "localhost",
                3000);
        System.out.println("\nALL USERS");
        System.out.println(client.get("/users.xml"));

        // System.out.println("delete response body: "
        // + client.delete("/users/" + 11 + ".xml"));

        System.out.println("\nNEW USER");
        final String result = client.post("/users.xml",
                                          "<user><login>rootagain</login><name>SuperUser</name><email>rootagain@example.com</email></user>");

        final String id = result.replaceFirst("</id>.*", "")
                .replaceFirst(".*<id.*>", "")
                .trim();
        System.out.println(result);
        System.out.println("id = " + id);

        System.out.println("\nUPDATE USER");
        System.out.println(client.put("/users/" + id + ".xml",
                                      "<user><login>rootagain</login><name>SuperUser</name><email>rootagain@example.com</email></user>")
                .replace("SuperUser", "Nobody"));

        System.out.println("\nDELETE USER");
        System.out.println("delete response body: "
                + client.delete("/users/" + id + ".xml"));
    }
}
