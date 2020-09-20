package maow.batcher.parsing;

import maow.batcher.api.data.Patch;
import maow.batcher.api.data.Patcher;
import maow.batcher.util.PatchType;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class Parser {
    private final Patcher patcher;

    public Parser(Patcher patcher) {
        this.patcher = patcher;
    }

    public void parse() {
        List<Patch> patches = patcher.getPatches();
        if (patches.size() > 0) {
            for (Patch patch : patches) {
                try {
                    Path batchFile = patch.getBatch();
                    List<String> lines = removeComments(Files.readAllLines(patch.getPatch()));
                    List<String> batchLines = Files.readAllLines(batchFile);
                    if (lines.size() > 0) {
                        String indexLine = lines.get(1).replaceAll("eof", String.valueOf(batchLines.size()));
                        switch (Objects.requireNonNull(getTypeFromDeclaration(lines.get(0)))) {
                            case INSERT:
                                int insertIndex;
                                if (indexLine.endsWith("}")) {
                                    insertIndex = batchLines.indexOf(indexLine.substring(1, indexLine.lastIndexOf("}"))) + 1;
                                } else {
                                    insertIndex = Integer.parseInt(indexLine.substring(0,1));
                                }
                                cleanLines(lines);
                                addToBatchLines(insertIndex, lines, batchLines);
                                overwriteBatch(batchFile, batchLines);
                                break;
                            case MOD:
                                int startIndex = Integer.parseInt(indexLine.substring(0,1));
                                int endIndex = Integer.parseInt(indexLine.substring(2));
                                cleanLines(lines);
                                batchLines.subList(startIndex, endIndex).clear();
                                int i = 0;
                                addToBatchLines(i, lines, batchLines);
                                overwriteBatch(batchFile, batchLines);
                                break;
                        }
                    } else {
                        System.out.println("Parser finished, patch file empty.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Parser finished, 0 patches specified.");
        }
    }

    private PatchType getTypeFromDeclaration(String typeLine) {
        if (typeLine.startsWith("type:")) {
            String typeDeclaration = typeLine.substring(typeLine.indexOf(":") + 1);
            switch (typeDeclaration) {
                case "insert":
                    return PatchType.INSERT;
                case "mod":
                    return PatchType.MOD;
            }
        }
        return null;
    }

    private List<String> removeComments(List<String> lines) {
        lines.removeIf(line -> line.startsWith("//"));
        return lines;
    }

    private void cleanLines(List<String> lines) {
        if (lines.contains("-")) {
            lines.subList(0, lines.indexOf("-") + 1).clear();
        }
    }

    private void addToBatchLines(int i, List<String> lines, List<String> batchLines) {
        for (String line : lines) {
            batchLines.add(i, line);
            i++;
        }
    }

    private void overwriteBatch(Path batchFile, List<String> batchLines) throws IOException {
        FileWriter writer = new FileWriter(batchFile.toAbsolutePath().toString(), false);
        for (String batchLine : batchLines) {
            writer.write(batchLine + "\n");
        }
        writer.close();
    }
}
