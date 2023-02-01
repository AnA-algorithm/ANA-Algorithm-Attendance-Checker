package controller;

import application.usecase.MakeReadmeUseCase;
import domain.commitlog.model.Branch;
import domain.commitlog.model.GitFilePath;
import domain.readme.ReadMe;

import java.time.Month;
import java.time.Year;

/**
 * @author Donghun Shin
 * Created on 2023. 01. 03
 */
public class MakeReadMeController {

    private final MakeReadmeUseCase makeReadmeUseCase;

    public MakeReadMeController(final MakeReadmeUseCase makeReadmeUseCase) {
        this.makeReadmeUseCase = makeReadmeUseCase;
    }

    public ReadMe make(final MakeReadMeRequest request) {
        return makeReadmeUseCase.command(
                new MakeReadmeUseCase.Command(request.year, request.month, request.gitFilePath, request.branch)
        );
    }

    public record MakeReadMeRequest(
            Year year,
            Month month,
            GitFilePath gitFilePath,
            Branch branch
    ) {
    }
}
