package application.service;

import application.usecase.MakeReadmeUseCase;
import domain.commitlog.model.CommitLog;
import domain.commitlog.repsotory.CommitLogRepository;
import domain.member.GitUser;
import domain.readme.ReadMe;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Dongun Shin
 * Created on 2023. 01. 03
 */
public class MakeReadme implements MakeReadmeUseCase {

    private final CommitLogRepository commitLogRepository;

    public MakeReadme(final CommitLogRepository commitLogRepository) {
        this.commitLogRepository = commitLogRepository;
    }

    @Override
    public ReadMe command(final Command command) {

        List<CommitLog> commitLogs = commitLogRepository.findAllCommitLogByGithubUrlAndBranchAndYearAndMonth(
                command.gitFilePath(), command.branch(), command.year(), command.month()
        );
        Map<GitUser, List<CommitLog>> result =
                commitLogs.stream().collect(Collectors.groupingBy(CommitLog::gitUser));
        Map<GitUser, List<CommitLog>> filteredResult =
                result.entrySet()
                .stream()
                .filter(logs -> logs.getValue().size() >= 12)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ReadMe(command.year(), command.month(), filteredResult);
    }
}
