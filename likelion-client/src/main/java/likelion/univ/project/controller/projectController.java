package likelion.univ.project.controller;

import likelion.univ.domain.project.dto.ProjectSimpleDto;
import likelion.univ.domain.project.service.ImageService;
import likelion.univ.domain.project.service.ProjectMemberService;
import likelion.univ.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class projectController {

    private final ProjectService projectService;
    private final ImageService imageService;
    private final ProjectMemberService projectMemberService;

    @GetMapping("/v1/project/update/{projectId}")
    public ProjectSimpleDto getProject(@PathVariable("projectId") Long projectId) {
        ProjectSimpleDto result = projectService.getProject(projectId);
        return result;
    }

    @PutMapping("/v1/project/update/{projectId}")
    public ProjectSimpleDto updateProject(@PathVariable("projectId") Long projectId, @RequestBody ProjectSimpleDto projectSimpleDto) {
        projectService.updateProject(projectId, projectSimpleDto);
        imageService.updateImage(projectId, projectSimpleDto);
        projectMemberService.updateProjectMember(projectId, projectSimpleDto);
        return projectService.getProject(projectId);
    }

    @DeleteMapping("/v1/project/delete/{projectId}")
    public void deleteProject(@PathVariable("projectId") Long projectId) {
        imageService.deleteImage(projectId);
        projectService.deleteProject(projectId);
    }
}
