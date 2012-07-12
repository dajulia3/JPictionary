package protocol;
	import java.io.DataOutputStream;
	import java.io.IOException;
	import java.io.OutputStream;
	
public class FrameOutputStream {
		
		DataOutputStream out;
		
		public FrameOutputStream(OutputStream os){
			out = new DataOutputStream(os);
		}
		public void writeFrame(Frame f) throws IOException {
			// write the sequence * frametype seq. # length payload
			out.writeByte('*');
			out.writeByte(f.getFrameType());
			
			byte[] payload = f.getPayload();
			out.writeShort(payload.length);
			out.write(payload);
			out.flush();
		}
	}
