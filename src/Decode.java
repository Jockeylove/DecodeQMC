import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.filechooser.FileSystemView;
public class Decode {

	private int x = -1;
	private int y = 8;
	private int dx = 1;
	private int index = -1;

	private int[][] seedMap = { 
								{ 0x4a, 0xd6, 0xca, 0x90, 0x67, 0xf7, 0x52 },
								{ 0x5e, 0x95, 0x23, 0x9f, 0x13, 0x11, 0x7e }, 
								{ 0x47, 0x74, 0x3d, 0x90, 0xaa, 0x3f, 0x51 },
								{ 0xc6, 0x09, 0xd5, 0x9f, 0xfa, 0x66, 0xf9 }, 
								{ 0xf3, 0xd6, 0xa1, 0x90, 0xa0, 0xf7, 0xf0 },
								{ 0x1d, 0x95, 0xde, 0x9f, 0x84, 0x11, 0xf4 }, 
								{ 0x0e, 0x74, 0xbb, 0x90, 0xbc, 0x3f, 0x92 },
								{ 0x00, 0x09, 0x5b, 0x9f, 0x62, 0x66, 0xa1 } 
							  };

	public int NextMask() {
		int ret;
		index++;
		if (x < 0) {
			dx = 1;
			y = ((8 - y) % 8);
			ret = ((8 - y) % 8);
			ret = 0xc3;
		} else if (x > 6) {
			dx = -1;
			y = 7 - y;
			ret = 0xd8;
		} else {
			ret = seedMap[y][x];
		}

		x += dx;
		if (index == 0x8000 || (index > 0x8000 && (index + 1) % 0x8000 == 0)) {
			return NextMask();
		}
		return ret;
	}

	public static void io(String filePath,String filename,String end1,String end2)throws Exception {
		FileInputStream fis = new FileInputStream(new File(filename));
		byte[] buffer = new byte[fis.available()];
		fis.read(buffer);
		Decode dc = new Decode();
		for (int i = 0; i < buffer.length; ++i) {
			buffer[i] = (byte) (dc.NextMask() ^ buffer[i]);
		}
		FileOutputStream fos = new FileOutputStream(new File(filePath+"/"+filename.replace(end1, end2)));
		fos.write(buffer);
		fos.flush();
		fos.close();
		fis.close();
	}
	
	public static void main(String[] args) throws Exception {
		FileSystemView fsv = FileSystemView.getFileSystemView();
		File home = fsv.getHomeDirectory();
		String filePath = home.getPath();
		File desktop = new File(filePath); 
	    String[] arr = desktop.list();
	    for (String filename : arr) {
	        if(filename.endsWith(".qmc0")) {
	        	io(filePath,filename,".qmc0",".mp3");
	        }
	        if(filename.endsWith(".qmcflac")) {
	        	io(filePath,filename,".qmcflac",".flac");
		        }
	    }
	}
}
