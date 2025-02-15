package likelion.univ.project.usecase;

import likelion.univ.annotation.UseCase;
import likelion.univ.domain.project.adapter.ProjectAdaptor;
import likelion.univ.domain.project.entity.Image;
import likelion.univ.domain.project.entity.Project;
import likelion.univ.domain.project.service.ProjectImageService;
import likelion.univ.domain.project.service.ProjectMemberService;
import likelion.univ.domain.project.service.ProjectService;
import likelion.univ.domain.project.service.ProjectTechService;
import likelion.univ.domain.university.adaptor.UniversityAdaptor;
import likelion.univ.domain.user.adaptor.UserAdaptor;
import likelion.univ.domain.user.entity.User;
import likelion.univ.project.dto.request.ProjectRequestDto;
import likelion.univ.project.dto.response.ProjectIdResponseDto;
import likelion.univ.utils.AuthenticatedUserUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@UseCase
@RequiredArgsConstructor
public class CreateProjectUsecase {

    private final AuthenticatedUserUtils authenticatedUserUtils;
    private final ProjectService projectService;
    private final ProjectTechService projectTechService;
    private final ProjectImageService projectImageService;
    private final ProjectMemberService projectMemberService;
    private final UserAdaptor userAdaptor;
    private final ProjectAdaptor projectAdaptor;
    private final UniversityAdaptor universityAdaptor;

    public ProjectIdResponseDto execute(ProjectRequestDto projectRequestDto) {

        User user = authenticatedUserUtils.getCurrentUser();
//        if(user.getAuthInfo().getRole() != Role.UNIVERSITY_ADMIN) {
//            throw new NotAdminForbiddenException();
//        }

        Project request = projectRequestDto.toEntity();
        request.updateAuthor(user);
        if(!projectRequestDto.getUniv().isEmpty())
            request.updateUniv(universityAdaptor.findByName(projectRequestDto.getUniv()));

        Project createdProject = projectService.createProject(request);
        Long id = createdProject.getId();
        Project project = projectAdaptor.findById(id);
        List<String> techNames = projectRequestDto.getProjectTeches();
        projectTechService.addProjectTech(project, techNames.stream().map(tech -> tech.toUpperCase()).toList());
        projectImageService.addImage(
                projectRequestDto.getImageUrl().stream()
                        .map(imageUrl -> new Image(project, imageUrl))
                        .collect(Collectors.toList()));
        projectMemberService.addMembers(project,
                projectRequestDto.getMembers().stream()
                .map(member -> userAdaptor.findById(member))
                .collect(Collectors.toList()));

        return ProjectIdResponseDto.of(id);
    }
}
