package gitletprocess;

public class Blob extends ValidObject {
    private byte[] contents;

    public Blob(byte[] c) {
        contents = c;
    }

    public byte[] getContents() {
        return contents;
    }
}
