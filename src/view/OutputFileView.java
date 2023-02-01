package view;

import domain.commitlog.model.CommitLog;
import domain.member.GitUser;
import domain.readme.ReadMe;
import util.FileUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public class OutputFileView {

    /**
     * 형식은 다음과 같다.
     * <p>
     * | 참여자 | 횟수 | 1일 | 2일 | 3일 | 4일 | 5일 | 6일 | 7일 | 8일 | 9일 | 10일 | 11일 | 12일 | 13일 | 14일 | 15일 | 16일 | 17일 | 18일 | 19일 | 20일 | 21일 | 22일 | 23일 | 24일 | 25일 | 26일 | 27일 | 28일 | 29일 | 30일 |
     * | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
     * |name(email)|횟수||||:white_check_mark:||||||||||||||||||||||||||:white_check_mark:|
     * |name(email)|횟수||||||:white_check_mark:|||||||||||||:white_check_mark:|:white_check_mark:|||||||||||
     */
    private static final String README_FILE_FORMAT = "%s_%s_CHECK.md";

    private static final String TITLE_FORMAT = "# %d년 %d월 하루하나 알고리즘 체크표\n";

    private static final String CLASSIFICATION_FORMAT = "| 참여자 | 횟수 | %s |\n";  // 마지막은 요일
    private static final String DIVISION_LINE_FORMAT = "| --- | --- | %s |\n";  // 마지막은 요일
    private static final String USER_LOG_FORMAT = "|%s|%s|%s|";  // 이름(이메일), 총 커밋 개수, 마지막은 일별 체크표시

    private static final String DAY_FORMAT = "%d일";
    private static final String USER_FORMAT = "%s(%s)";  // name, email 순서

    private static final String DIVISION = "---";
    private static final String BAR = " | ";
    private static final String NO_BLANK_BAR = "|";
    private static final String CHECK_MARK = ":white_check_mark:";
    private static final String EMPTY = "";
    private static final String NEW_LINE = "\n";

    public static void makeReadMe(final ReadMe readMe) {
        String title = format(TITLE_FORMAT, readMe.year().getValue(), readMe.month().getValue());
        String days = classification(readMe); // | 참여자 | 횟수 | %s |\n
        String division = division(readMe);  //  | --- | --- |...
        String userLogText = makeUsersLogText(readMe);

        File file = new File(format(README_FILE_FORMAT, readMe.year(), readMe.month().getValue()));
        FileUtil.write(file, title + days + division + userLogText);
    }

    /**
     * | 참여자 | 횟수 | 1일 | 2일 | 3일 | 4일 | 5일 | 6일 | 7일 | 8일 | 9일 | 10일 | 11일 | 12일 | 13일 | 14일 | 15일 | 16일 | 17일 | 18일 | 19일 | 20일 | 21일 | 22일 | 23일 | 24일 | 25일 | 26일 | 27일 | 28일 | 29일 | 30일 |
     */
    private static String classification(final ReadMe readMe) {
        int length = readMe.dayLength();
        return format(CLASSIFICATION_FORMAT,
                IntStream.rangeClosed(1, length)
                        .mapToObj(it -> format(DAY_FORMAT, it))
                        .collect(joining(BAR)));
    }

    /**
     * | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
     */
    private static String division(final ReadMe readMe) {
        int length = readMe.dayLength();
        return format(DIVISION_LINE_FORMAT, IntStream.rangeClosed(1, length)
                .mapToObj(it -> DIVISION)
                .collect(joining(BAR)));
    }

    /**
     * ex:
     * |name(email)|횟수||||:white_check_mark:||||||||||||||||||||||||||:white_check_mark:|
     * |name(email)|횟수||||||:white_check_mark:|||||||||||||:white_check_mark:|:white_check_mark:|||||||||||
     * ...
     */
    private static String makeUsersLogText(final ReadMe readMe) {
        Map<GitUser, List<CommitLog>> memberCommitLogs = readMe.memberCommitLogs();
        int length = readMe.dayLength();
        StringBuilder sb = new StringBuilder();
        for (GitUser gitUser : memberCommitLogs.keySet()) {
            makeUserLogText(memberCommitLogs, length, gitUser, sb);
        }
        return sb.toString();
    }

    /**
     * ex:
     * |name(email)|횟수||||:white_check_mark:||||||||||||||||||||||||||:white_check_mark:|
     */
    private static void makeUserLogText(final Map<GitUser, List<CommitLog>> memberCommitLogs,
                                        final int length,
                                        final GitUser gitUser,
                                        final StringBuilder sb) {

        String user = format(USER_FORMAT, gitUser.name(), gitUser.email());
        List<CommitLog> commitLogs = memberCommitLogs.get(gitUser);

        Map<Integer, List<CommitLog>> monthDayListMap = commitLogs.stream()
                .collect(groupingBy(CommitLog::day));

        String checkMarks = IntStream.rangeClosed(1, length)
                .mapToObj(it -> commitMark(monthDayListMap, it))
                .collect(joining(NO_BLANK_BAR));

        sb.append(String.format(USER_LOG_FORMAT, user, commitLogs.size(), checkMarks)).append(NEW_LINE);
    }

    private static String commitMark(Map<Integer, List<CommitLog>> monthDayListMap, int day) {
        if (monthDayListMap.get(day) == null || monthDayListMap.get(day).isEmpty()) {
            return EMPTY;
        }
        return CHECK_MARK;
    }

}
