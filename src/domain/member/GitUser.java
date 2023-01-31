package domain.member;

/**
 * @author Dongun Shin
 * Created on 2023. 01. 03
 */
public record GitUser(
        String name,
        String email
) {
    public static GitUser of(final String name,
                             final String email) {
        return new GitUser(name, email);
    }
}
