package maow.batcher.api.data;

import java.nio.file.Path;

public class Patch {
    private final Path batch;
    private final Path patch;

    public Patch(Path batch, Path patch) {
        this.batch = batch;
        this.patch = patch;
    }

    public Path getBatch() {
        return batch;
    }

    public Path getPatch() {
        return patch;
    }
}
