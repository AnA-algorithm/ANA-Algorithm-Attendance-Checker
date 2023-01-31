import application.service.MakeReadme;
import application.usecase.MakeReadmeUseCase;
import controller.MakeReadMeController;
import domain.commitlog.model.Branch;
import domain.commitlog.model.GitFilePath;
import domain.commitlog.repsotory.CommitLogRepository;
import domain.readme.ReadMe;
import infrastructure.persistence.git.repository.commitlog.GitCommitLogRepository;
import view.OutputFileView;

import java.time.Month;
import java.time.Year;

public class Main {

    public static void main(String[] args) {
        CommitLogRepository commitLogRepository = new GitCommitLogRepository();
        MakeReadmeUseCase makeReadmeUseCase = new MakeReadme(commitLogRepository);
        MakeReadMeController makeReadMeController = new MakeReadMeController(makeReadmeUseCase);

        //== 이부분만 변경하여 세팅 ==//
        Year year = Year.of(2023);
        Month month = Month.of(1);
        GitFilePath gitFilePath = GitFilePath.of("/Users/minseongkim/workspace/ANA-Daily-Algorithm");
        Branch branch = Branch.remoteOriginBranch("main");
        //== 세팅 종료 ==//

        ReadMe readMe = makeReadMeController.make(
                new MakeReadMeController.MakeReadMeRequest(year, month, gitFilePath, branch)
        );
        OutputFileView.makeReadMe(readMe);
    }
}
