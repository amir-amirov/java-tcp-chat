package kz.shift.chat.server;

import lombok.Getter;
import lombok.Setter;

import java.io.PrintWriter;

@Getter
@Setter
public class ClientSession {
    private String username;
    private final PrintWriter out;
    private final String remoteAddress;

    public ClientSession(PrintWriter out, String remoteAddress) {
        this.out = out;
        this.remoteAddress = remoteAddress;
    }
}
