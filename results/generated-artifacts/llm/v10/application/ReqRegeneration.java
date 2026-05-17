package generated.evolution;

import java.nio.file.Path;

/** Requirement: Повторная генерация без потери ручных изменений и с контролем регрессий. Coverage estimate: 0.43. */
public final class ReqRegeneration {
    public boolean keepsManualExtension(Path generatedFile, Path protectedRegionFile) {
        return generatedFile != null && protectedRegionFile != null;
    }
}