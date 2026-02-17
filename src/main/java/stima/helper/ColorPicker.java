package stima.helper;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ColorPicker {

    public Map<Character, Float> colorDict = new HashMap<Character, Float>();

    public String getColor(char sign) {
        float saturation = 0.8f;
        float brightness = 0.9f;
        if (!colorDict.containsKey(sign)) {
            float start = 0.05f;
            float finish = 0.95f;
            if (colorDict.size() == 0) {
                colorDict.put(sign, start);
            }
            else if (colorDict.size() == 1) {
                colorDict.put(sign, finish);
            }
            else {
                float a = (finish - start)/2f;
                boolean found = false;
                float t = start;
                while (!found) {
                    boolean hit = false;
                    for (Map.Entry<Character, Float> entry: colorDict.entrySet()) {
                        float delta = Math.abs(entry.getValue() - t);
                        if (delta < 1e-5) {
                            hit = true;
                            break;
                        }
                    }
                    if (!hit) {
                        found = true;
                    }
                    else if (t >= finish) {
                        a = a/2;
                        t = start;
                    }
                    else {
                        t += a;
                    }
                }
                colorDict.put(sign, t);
                
            }
            //System.out.println("(" + sign + ") : " + Integer.toHexString(Color.HSBtoRGB(colorDict.get(sign), saturation, brightness)));
        }
        return Integer.toHexString(Color.HSBtoRGB(colorDict.get(sign), saturation, brightness));
    }
}
