package domain.commitlog.model;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public record GitFilePath(
        String path
) {
    public static final String CHILD = ".git";

    public static GitFilePath of(final String path) {
        return new GitFilePath(path);
    }
}
