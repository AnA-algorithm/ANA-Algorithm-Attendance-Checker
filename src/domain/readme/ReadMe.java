package domain.readme;

import domain.commitlog.model.CommitLog;
import domain.member.GitUser;

import java.time.Month;
import java.time.Year;
import java.util.List;
import java.util.Map;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public record ReadMe(
        Year year,
        Month month,
        Map<GitUser, List<CommitLog>> memberCommitLogs
) {

    public int dayLength() {
        return month.length(year.isLeap());
    }
}
