package com.nhnacademy.minidoorayprojectapi.domain.task.service;

import com.nhnacademy.minidoorayprojectapi.domain.comment.dao.CommentRepository;
import com.nhnacademy.minidoorayprojectapi.domain.comment.dto.response.CommentDto;
import com.nhnacademy.minidoorayprojectapi.domain.comment.entity.Comment;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dao.MilestoneRepository;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.dto.response.MilestoneDto;
import com.nhnacademy.minidoorayprojectapi.domain.milestone.exception.MilestoneNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.project.dao.ProjectRepository;
import com.nhnacademy.minidoorayprojectapi.domain.project.exception.ProjectNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dao.TagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.tag.dto.response.TagSeqNameDto;
import com.nhnacademy.minidoorayprojectapi.domain.tag.entity.Tag;
import com.nhnacademy.minidoorayprojectapi.domain.tag.exception.TagNotFoundException;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dao.TaskTagRepository;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskCreateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.request.TaskUpdateRequestDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.dto.response.TaskSeqNameAndMemberSeqDto;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.Task;
import com.nhnacademy.minidoorayprojectapi.domain.task.entity.TaskTag;
import com.nhnacademy.minidoorayprojectapi.domain.task.exception.TaskNotFoundException;
import com.nhnacademy.minidoorayprojectapi.global.exception.UnauthorizedAccessException;
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

    public TaskDto getTask(Long projectSeq, Long taskSeq, Pageable pageable){
        Task task = taskRepository.findByProject_ProjectSeqAndTaskSeq(projectSeq, taskSeq)
                .orElseThrow(TaskNotFoundException::new);
        List<Tag> tags = taskTagRepository.findAllByTask_TaskSeq(taskSeq);
        Page<Comment> comments = commentRepository.getAllByTask_TaskSeq(taskSeq,pageable);
        return convertToTaskDto(task, tags, comments);
    }

    private TaskDto convertToTaskDto(Task task, List<Tag> tags, Page<Comment> comments){
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
                .comments(
                        new PageImpl<>(task.getComments().stream()
                                .map(comment -> new CommentDto(comment.getCommentSeq(),
                                        comment.getMemberSeq(),
                                        comment.getCommentContent(),
                                        comment.getCommentCreatedAt()))
                                .collect(Collectors.toList()),
                                comments.getPageable(),
                                comments.getTotalElements()
                        )
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
                .project(projectRepository.findById(projectSeq).orElseThrow(ProjectNotFoundException::new))
                .taskTitle(taskCreateRequest.getTaskTitle())
                .taskContent(taskCreateRequest.getTaskContent())
                .taskStatus(taskCreateRequest.getTaskStatus())
                .build();
        taskRepository.save(newTask);

        taskCreateRequest.getTags()
                .forEach(tagSeq->{
                    Tag tag = tagRepository.findByProject_ProjectSeqAndTagSeq(projectSeq,tagSeq)
                            .orElseThrow(TagNotFoundException::new);
                    TaskTag.builder()
                            .taskTagPk(new TaskTag.TaskTagPk(newTask.getTaskSeq(), tag.getTagSeq()))
                            .tag(tag)
                            .task(newTask)
                            .build();
                });

        return convertToTaskSeqDto(newTask);
    }

    @Transactional
    public TaskSeqDto updateTask(Long projectSeq, Long taskSeq, TaskUpdateRequestDto taskUpdateRequest){
        Task task = taskRepository.findByProject_ProjectSeqAndTaskSeq(projectSeq, taskSeq)
                .orElseThrow(TaskNotFoundException::new);
        if(!task.getMemberSeq().equals(task.getMemberSeq())){
            throw new UnauthorizedAccessException("접근 권한이 없습니다.");
        }
        task.updateTask(taskUpdateRequest.getTaskTitle(),taskUpdateRequest.getTaskContent(),
                taskUpdateRequest.getTaskStatus(),
                milestoneRepository.findByProject_ProjectSeqAndMilestoneSeq(projectSeq,
                        taskUpdateRequest.getMilestoneSeq()).orElseThrow(MilestoneNotFoundException::new));
        return convertToTaskSeqDto(task);
    }


    private TaskSeqDto convertToTaskSeqDto(Task task){
        return new TaskSeqDto(task.getTaskSeq());
    }

    //TODO task 삭제할 때 tag도 함께 삭제해야하나? 다대다??
    @Transactional
    public void deleteTask(Long taskSeq, Long memberSeq){
        if(taskRepository.existsByTaskSeqAndAndMemberSeq(taskSeq,memberSeq)){
            throw new UnauthorizedAccessException("접근 권한이 없습니다.");
        }
        taskRepository.deleteById(taskSeq);
    }


}
