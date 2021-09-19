import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionClient {
    public static void main(String[] args) throws IOException {
        Socket s = new Socket("localhost", 12345);

        // time to write to the server
        OutputStream out = s.getOutputStream();
        out.write(55);
        out.write(54);
        out.write(34);
        out.write(23);
        out.write(2);
        out.write(7);
        out.flush();

        // time to read from the server
        InputStream in = s.getInputStream();
        int read = -1;
        while ((read = in.read()) != -1) {
            System.out.println(read);
        }
    }
}
