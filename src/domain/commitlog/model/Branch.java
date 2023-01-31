package domain.commitlog.model;

import static java.lang.String.format;

/**
 * @author Dongun Shin
 * Created on 2023. 01. 03
 */
public record Branch(
        String name
) {

    private static final String REMOTE_ORIGIN_BRANCH_FORMAT = "remotes/origin/%s";

    public static Branch remoteOriginBranch(final String name) {
        return new Branch(format(REMOTE_ORIGIN_BRANCH_FORMAT, name));
    }

    public static Branch of(final String name) {
        return new Branch(name);
    }
}
