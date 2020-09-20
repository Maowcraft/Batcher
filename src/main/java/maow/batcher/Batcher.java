package maow.batcher;

import maow.batcher.api.data.Patch;
import maow.batcher.api.data.Patcher;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Batcher {
    public static void main(String[] args) {
        if (args.length > 1) {
            Path patchFile = Paths.get(args[1]);
            if (Files.isDirectory(patchFile)) {
                List<Patch> patches = new ArrayList<>();
                for (File individualPatch : Objects.requireNonNull(patchFile.toFile().listFiles())) {
                    patches.add(new Patch(Paths.get(args[0]), individualPatch.toPath()));
                }
                Patcher patcher = new Patcher(patches);
                patcher.patch();
            } else {
                Patch patch = new Patch(Paths.get(args[0]), Paths.get(args[1]));
                Patcher patcher = new Patcher(patch);
                patcher.patch();
            }
        } else {
            System.err.println("Missing arguments.\nSyntax: batcher <targetFile> <patchFile>");
        }
    }
}
