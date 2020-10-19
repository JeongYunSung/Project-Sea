package com.yunseong.project.sagas;

import com.yunseong.project.ProjectDetailMother;
import com.yunseong.project.api.ProjectServiceChannels;
import com.yunseong.project.api.TeamServiceChannels;
import com.yunseong.project.api.command.ApproveTeamCommand;
import com.yunseong.project.api.event.ProjectDetail;
import com.yunseong.project.sagaparticipants.ProjectProxyService;
import com.yunseong.project.sagaparticipants.StartProjectCommand;
import com.yunseong.project.sagaparticipants.TeamProxyService;
import com.yunseong.project.sagaparticipants.WeClassProxyService;
import com.yunseong.project.sagas.startproject.StartProjectSaga;
import com.yunseong.project.sagas.startproject.StartProjectSagaState;
import com.yunseong.weclass.api.WeClassServiceChannels;
import com.yunseong.weclass.api.command.CreateWeClassCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static io.eventuate.tram.sagas.testing.SagaUnitTestSupport.given;

@RunWith(SpringJUnit4ClassRunner.class)
public class StartProjectSagaTest {

    private ProjectProxyService projectProxyService = new ProjectProxyService();
    private TeamProxyService teamProxyService = new TeamProxyService();
    private WeClassProxyService weClassProxyService = new WeClassProxyService();

    private StartProjectSaga makeStartProjectSaga() {
        return new StartProjectSaga(projectProxyService, teamProxyService, weClassProxyService);
    }

    @Test
    public void shouldStartProject() throws Exception {
        given()
                .saga(this.makeStartProjectSaga(),
                        new StartProjectSagaState(ProjectDetailMother.PROJECT_ID))
                .expect()
                    .command(new ApproveTeamCommand(ProjectDetailMother.PROJECT_ID))
                    .to(TeamServiceChannels.teamServiceChannel)
                    .andGiven()
                    .successReply()
                .expect()
                    .command(new CreateWeClassCommand(ProjectDetailMother.PROJECT_ID))
                    .to(WeClassServiceChannels.weClassServiceChannel)
                    .andGiven()
                    .successReply()
                .expect()
                    .command(new StartProjectCommand(ProjectDetailMother.PROJECT_ID))
                    .to(ProjectServiceChannels.projectServiceChannel);
    }

    @Test
    public void shouldRejectProjectDueToTeamApproveFailed() throws Exception {
        given()
                .saga(makeStartProjectSaga(),
                        new StartProjectSagaState(ProjectDetailMother.PROJECT_ID))
                .expect()
                    .command(new ApproveTeamCommand(ProjectDetailMother.PROJECT_ID))
                    .to(TeamServiceChannels.teamServiceChannel)
                    .andGiven()
                    .failureReply();
    }
}
