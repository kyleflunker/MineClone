package Tools;

import org.lwjgl.util.vector.Vector3f;

public class Vec3i {
	public int x; 
	public int y; 
	public int z; 
	public Vector3f pos;
	
	public Vec3i(int x, int y, int z) {
		this.x = x; this.y = y; this.z = z;
		this.pos = new Vector3f(x,y,z);
	}
}
