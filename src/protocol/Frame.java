package protocol;

public abstract class Frame {
public abstract int getFrameType();
public abstract byte[] getPayload();
}
