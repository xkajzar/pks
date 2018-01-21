package pks;

public class MyCrc {
	public int crc;
	public int pol;
	
	public MyCrc(){
		pol = 0xEDB88320;
	}
	
	public int count(byte[] string){
		crc = 0xFFFFFFFF;
		for(byte b : string){
			int temp = (crc ^ b) & 0xff;
			for (int i = 0; i < 8; i++) {
                if ((temp & 1) == 1) 
                	temp = (temp >>> 1) ^ pol;
                else                 
                	temp = (temp >>> 1);
            }
			crc = (crc >>> 8) ^ temp;
		}
		crc = crc ^ 0xffffffff;
		return crc;
	}
}
