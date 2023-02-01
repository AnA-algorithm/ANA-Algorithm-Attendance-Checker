package domain.commitlog.model;


import domain.member.GitUser;

import java.time.Month;
import java.time.MonthDay;
import java.time.Year;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public record CommitLog(
        GitUser gitUser,
        Year year,
        Month month,
        MonthDay monthDay,
        String message
) {
    public int day() {
        return monthDay.getDayOfMonth();
    }
}
