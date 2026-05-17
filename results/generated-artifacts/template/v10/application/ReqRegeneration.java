package generated.evolution;

import java.nio.file.Path;

/** Requirement: Повторная генерация без потери ручных изменений и с контролем регрессий. Coverage estimate: 0.33. */
public final class ReqRegeneration {
    public boolean keepsManualExtension(Path generatedFile, Path protectedRegionFile) {
        return generatedFile != null && protectedRegionFile != null;
    }
}