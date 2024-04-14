package nmccoy.legendgear.client;

import static java.lang.Math.*;
import static net.minecraft.util.math.MathHelper.sin;

public interface RainbowHelper {

	public static float r(float phase) {
		phase *= PI * 2;
        float r = (sin(phase + 0f) + 1f) * 0.5f;
        float g = (sin(phase + (float) PI * 2/3) + 1f) * 0.5f;
        float b = (sin(phase + (float) PI * 4/3) + 1f) * 0.5f;
        float resat = min(r, min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1 / max(r, max(g, b));
        
        r = min(scaler * r, 1f);
        g = min(scaler * g, 1f);
        b = min(scaler * b, 1f);
        
        return r;
	}
	
	public static float g(float phase) {
		phase *= PI * 2;
        float r = (sin(phase + 0f) + 1f) * 0.5f;
        float g = (sin(phase + (float) PI * 2/3) + 1f) * 0.5f;
        float b = (sin(phase + (float) PI * 4/3) + 1f) * 0.5f;
        float resat = min(r, min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1 / max(r, max(g, b));
        
        r = min(scaler * r * 0.5f + 0.5f, 1f);
        g = min(scaler * g * 0.5f + 0.5f, 1f);
        b = min(scaler * b * 0.5f + 0.5f, 1f);
        
        return g;
	}
	
	public static float b(float phase) {
		phase *= PI * 2;
        float r = (sin(phase + 0f) + 1f) * 0.5f;
        float g = (sin(phase + (float) PI * 2/3) + 1f) * 0.5f;
        float b = (sin(phase + (float) PI * 4/3) + 1f) * 0.5f;
        float resat = min(r, min(g, b));
        r -= resat;
        g -= resat;
        b -= resat;
        float scaler = 1 / max(r, max(g, b));
        
        r = min(scaler * r * 0.5f + 0.5f, 1f);
        g = min(scaler * g * 0.5f + 0.5f, 1f);
        b = min(scaler * b * 0.5f + 0.5f, 1f);
        
        return b;
	}
	
}
