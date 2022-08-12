package club.mher.drawit.utility;

import com.cryptomorin.xseries.XMaterial;

public class MaterialUtil {

    public static String getMaterial(XMaterial material) {
        if (material == null || material.parseMaterial() == null) return "";
        if (ReflectionUtils.isLegacy()) {
            if (material.getData() != 0) {

                return material.parseMaterial().toString() + ":" + material.getData();
            }else {
                return material.parseMaterial().toString();
            }
        }else {
            return material.parseMaterial().toString();
        }
    }

}
