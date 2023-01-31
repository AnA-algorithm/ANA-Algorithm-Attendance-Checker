package infrastructure.persistence.git.repository.commitlog;

import domain.commitlog.model.Branch;
import domain.commitlog.model.CommitLog;
import domain.commitlog.model.GitFilePath;
import domain.commitlog.repsotory.CommitLogRepository;
import domain.member.GitUser;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;
import static util.DateUtil.parseIntToLocalDateTime;

/**
 * @author Dongun Shin
 * Created on 2023. 01. 03
 */
public class GitCommitLogRepository implements CommitLogRepository {

    @Override
    public List<CommitLog> findAllCommitLogByGithubUrlAndBranchAndYearAndMonth(final GitFilePath gitFilePath,
                                                                               Branch branch,
                                                                               final Year year,
                                                                               final Month month) {
        Repository gitRepository = getGitRepository(gitFilePath);
        Git git = new Git(gitRepository);

        return commitLogStream(git, branch)
                .map(GitCommitLogRepository::parseCommitLog)
                .filter(it -> year.equals(it.year()))
                .filter(it -> month.equals(it.month()))
                .toList();
    }

    /**
     * git repository를 반환한다.
     */
    public static Repository getGitRepository(final GitFilePath gitFilePath) {
        // first create a test-repository, the return is including the .get directory here!
        File repoDir = new File(gitFilePath.path(), GitFilePath.CHILD);

        // now open the resulting repository with a FileRepositoryBuilder
        FileRepositoryBuilder builder = new FileRepositoryBuilder();

        try {
            return builder.setGitDir(repoDir)
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 커밋 로그를 스트림으로 반환
     */
    private Stream<RevCommit> commitLogStream(final Git git, final Branch branch) {
        try {
            Iterable<RevCommit> revCommits = git.log().add(git.getRepository().resolve(branch.name())).call();
            return stream(revCommits.spliterator(), false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * RevCommit을 CommitLog 로 변환
     */
    private static CommitLog parseCommitLog(final RevCommit revCommit) {
        LocalDateTime localDateTime = parseIntToLocalDateTime(revCommit.getCommitTime(), revCommit.getAuthorIdent().getTimeZone().toZoneId());
        String username = revCommit.getAuthorIdent().getName();
        String email = revCommit.getAuthorIdent().getEmailAddress();

        return new CommitLog(GitUser.of(username, email),
                Year.of(localDateTime.getYear()),
                localDateTime.getMonth(),
                MonthDay.of(localDateTime.getMonth(), localDateTime.getDayOfMonth()),
                revCommit.getShortMessage());
    }
}
