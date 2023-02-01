package domain.commitlog.repsotory;

import domain.commitlog.model.Branch;
import domain.commitlog.model.CommitLog;
import domain.commitlog.model.GitFilePath;

import java.time.Month;
import java.time.Year;
import java.util.List;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public interface CommitLogRepository {

    /**
     * github Url에 속한 커밋 중 특정 년도의 모든 로그 가져오기
     */
    List<CommitLog> findAllCommitLogByGithubUrlAndBranchAndYearAndMonth(final GitFilePath gitFilePath, Branch branch, final Year year, final Month month);


}
