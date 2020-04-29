package RenderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import Entities.Entity;
import Shaders.StaticShader;
import Entities.Camera;
import Tools.Maths;

public class EntityRenderer {
	public static Vector3f temp = new Vector3f();
	public static int FOV = 100;
	
	public static void render(Entity entity, StaticShader shader) {
		//if entity isn't static (ex: crosshair), then frustum cull the entities that are rendering 
		if(!entity.isStaticEntity()) {
			Vector3f.sub(entity.position, Camera.position, temp);		
			temp.x += 5; temp.y += 5; temp.z += 5;
			if (temp.lengthSquared() > 5*5*3 * 1.732f) { 
				temp.normalise();
				float distance = Vector3f.dot(Camera.normal, temp);
				//if entity is not in the player FOV, do not render it
				if (acos(distance) < Math.toRadians(FOV)) return;
			}
		}

		GL30.glBindVertexArray(entity.getModel().getModel().getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getTextureID());

		GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
	
	private static double acos(double x) {
		return (-0.69813170079773212 * x * x - 0.87266462599716477) * x + 1.5707963267948966;
    }
	
}
