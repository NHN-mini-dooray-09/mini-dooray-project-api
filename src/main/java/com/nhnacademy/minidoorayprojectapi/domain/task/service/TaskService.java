package com.nhnacademy.minidoorayprojectapi.domain.task.service;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dao.CommentRepository;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dao.MilestoneRepository;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneDto;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.global.exception.AccessDeniedException;
import com.nhnacademy.minidoorayprojectapi.global.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dao.TagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskTagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TaskService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final MilestoneRepository milestoneRepository;
    private final TagRepository tagRepository;
    private final TaskTagRepository taskTagRepository;
    private final CommentRepository commentRepository;


    /**
     * 특정 프로젝트의 업무 목록 조회
     * @param pageable
     * @param projectSeq
     * @return
     */
    public Page<TaskSeqNameAndMemberSeqDto> getTasks(Pageable pageable, Long projectSeq ){

        Page<Task> taskPage = taskRepository.getAllByProject_ProjectSeq(pageable, projectSeq);
        return convertToTaskPageDto(taskPage);
    }

    private Page<TaskSeqNameAndMemberSeqDto> convertToTaskPageDto(Page<Task> taskPage){
        List<TaskSeqNameAndMemberSeqDto> taskPageDto = taskPage.getContent()
                .stream()
                .map(task -> new TaskSeqNameAndMemberSeqDto(task.getTaskSeq(),task.getTaskTitle() ,task.getMemberSeq()))
                .collect(Collectors.toList());
        return new PageImpl<>(taskPageDto,taskPage.getPageable(),taskPage.getTotalElements());
    }

    public TaskDto getTask(Long projectSeq, Long taskSeq){
        Task task = taskRepository.findByProject_ProjectSeqAndTaskSeq(projectSeq, taskSeq)
                .orElseThrow(() -> new ProjectNotFoundException("해당 업무를 찾을 수 없습니다."));
        List<Tag> tags = taskTagRepository.findAllByTask_TaskSeq(taskSeq).stream()
                .map(TaskTag::getTag)
                .collect(Collectors.toList());
        return convertToTaskDto(task, tags);
    }

    private TaskDto convertToTaskDto(Task task, List<Tag> tags){
        return TaskDto.builder()
                .taskSeq(task.getTaskSeq())
                .taskTitle(task.getTaskTitle())
                .taskContent(task.getTaskContent())
                .taskStatus(task.getTaskStatus())
                .taskCreatedAt(task.getTaskCreatedAt())
                .memberSeq(task.getMemberSeq())
                .milestone(
                        Objects.isNull(task.getMilestone()) ? null : MilestoneDto.builder()
                                .milestoneSeq(task.getMilestone().getMilestoneSeq())
                                .milestoneName(task.getMilestone().getMilestoneName())
                                .milestoneStartDate(task.getMilestone().getMilestoneStartDate())
                                .milestoneEndDate(task.getMilestone().getMilestoneEndDate())
                                .build()

                )
                .tags(
                        tags.stream()
                                .map(tag -> TagSeqNameDto.builder()
                                        .tagSeq(tag.getTagSeq())
                                        .tagName(tag.getTagName())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }


    /**
     * task 생성 시 tasktag 다대다 테이블도 함께 생성
     * @param projectSeq
     * @param memberSeq
     * @param taskCreateRequest
     * @return
     */
    @Transactional
    public TaskSeqDto createTask(Long projectSeq,Long memberSeq ,TaskCreateRequestDto taskCreateRequest){

        Task newTask = Task.builder()
                .memberSeq(memberSeq)
                .project(projectRepository.findById(projectSeq)
                            .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다.")))
                .taskTitle(taskCreateRequest.getTaskTitle())
                .taskContent(taskCreateRequest.getTaskContent())
                .taskStatus(taskCreateRequest.getTaskStatus())
                .build();
        taskRepository.save(newTask);

        taskCreateRequest.getTags()
                .forEach(tagSeq->{
                    Tag tag = tagRepository.findByProject_ProjectSeqAndTagSeq(projectSeq,tagSeq)
                            .orElseThrow(() -> new ProjectNotFoundException("해당 태그를 찾을 수 없습니다."));

                    taskTagRepository.save(TaskTag.builder()
                            .taskTagPk(new TaskTag.TaskTagPk(newTask.getTaskSeq(), tag.getTagSeq()))
                            .tag(tag)
                            .task(newTask)
                            .build());
                });
        return convertToTaskSeqDto(newTask);
    }

    @Transactional
    public TaskSeqDto updateTask(Long projectSeq, Long taskSeq, Long memberSeq ,TaskUpdateRequestDto taskUpdateRequest){
        Task task = taskRepository.findByProject_ProjectSeqAndTaskSeq(projectSeq, taskSeq)
                .orElseThrow(() -> new ProjectNotFoundException("해당 업무를 찾을 수 없습니다."));

        if(!memberSeq.equals(task.getMemberSeq())){
            throw new AccessDeniedException();
        }

        task.updateTask(taskUpdateRequest.getTaskTitle(),taskUpdateRequest.getTaskContent(),
                taskUpdateRequest.getTaskStatus(),
                milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(projectSeq,
                        taskUpdateRequest.getMilestoneSeq())
                        .orElseThrow(() -> new ProjectNotFoundException("프로젝트를 찾을 수 없습니다.")),
                        taskUpdateRequest.getTags().stream()
                        .map(tagSeq -> {
                            Tag tag = tagRepository.findByProject_ProjectSeqAndTagSeq(projectSeq,tagSeq)
                            .orElseThrow(() -> new ProjectNotFoundException("해당 태그를 찾을 수 없습니다."));
                            return TaskTag.builder()
                                    .taskTagPk(new TaskTag.TaskTagPk(task.getTaskSeq(), tag.getTagSeq()))
                                    .tag(tag)
                                    .task(task)
                                    .build();

                        }).collect(Collectors.toList())
        );
        return convertToTaskSeqDto(task);
    }


    private TaskSeqDto convertToTaskSeqDto(Task task){
        return new TaskSeqDto(task.getTaskSeq());
    }

    @Transactional
    public void deleteTask(Long taskSeq, Long memberSeq){
        if(!taskRepository.existsByTaskSeqAndAndMemberSeq(taskSeq,memberSeq)){
            throw new AccessDeniedException();
        }
        taskRepository.deleteById(taskSeq);
    }


}
