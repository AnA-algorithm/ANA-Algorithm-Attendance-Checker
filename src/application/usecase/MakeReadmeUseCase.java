package application.usecase;

import domain.commitlog.model.Branch;
import domain.commitlog.model.GitFilePath;
import domain.readme.ReadMe;

import java.time.Month;
import java.time.Year;

/**
 * @author Dongun Shin
 * Created on 2023. 01. 03
 */
public interface MakeReadmeUseCase {

    /**
     * git의 branch 중 년도와 달에 해당하는 Readme를 생성한다.
     */
    ReadMe command(final Command command);

    record Command(
            Year year,
            Month month,
            GitFilePath gitFilePath,
            Branch branch
    ) {
    }
}
