package maow.batcher.api.data;

import maow.batcher.parsing.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Patcher {
    private final List<Patch> patches;

    public Patcher(Patch... patches) {
        this.patches = new ArrayList<>(Arrays.asList(patches));
    }
    public Patcher(List<Patch> patches) {
        this.patches = patches;
    }

    public void patch() {
        Parser parser = new Parser(this);
        parser.parse();
    }

    public List<Patch> getPatches() {
        return patches;
    }
}
