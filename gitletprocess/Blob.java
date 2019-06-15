package gitletprocess;

import java.io.Serializable;

public class Blob implements Serializable {

    private byte[] contents;

    public Blob(byte[] c) {
        contents = c;
    }

    public byte[] getContents() {
        return contents;
    }

}