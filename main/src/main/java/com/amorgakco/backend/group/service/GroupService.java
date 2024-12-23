package com.amorgakco.backend.group.service;

import com.amorgakco.backend.chatroom.service.ChatRoomService;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupRegisterResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.groupapplication.repository.GroupApplicationRepository;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final GroupApplicationRepository groupApplicationRepository;
    private final ChatRoomService chatRoomService;

    @Transactional
    public GroupRegisterResponse register(final GroupRegisterRequest request, final Member host) {
        final Group group = groupMapper.toGroup(host, request);
        final Long groupId = groupRepository.save(group).getId();
        Long chatRoomId = chatRoomService.registerChatRoom(host, group);
        return new GroupRegisterResponse(groupId, chatRoomId);
    }

    @Transactional
    public void delete(final Member member, final Long groupId) {
        final Group group = getGroup(groupId);
        group.validateGroupHost(member);
        groupRepository.delete(group);
    }

    public Group getGroup(final Long groupId) {
        return groupRepository
            .findByIdWithParticipant(groupId)
            .orElseThrow(ResourceNotFoundException::groupNotFound);
    }

    public Group getGroupWithHost(final Long groupId) {
        return groupRepository
            .findByIdWithHost(groupId)
            .orElseThrow(ResourceNotFoundException::groupNotFound);
    }

    public GroupDetailResponse getDetailGroup(final Long groupId, final Long memberId) {
        final Group group = getGroup(groupId);
        final boolean isGroupHost = group.isGroupHost(memberId);
        return groupMapper.toGroupDetailResponse(group, isGroupHost);
    }

    public GroupBasicResponse getBasicGroup(final Long groupId, final Member member) {
        final Group group =
            groupRepository
                .findByIdWithHost(groupId)
                .orElseThrow(ResourceNotFoundException::groupNotFound);
        boolean isParticipated = group.isMemberParticipated(member.getId());
        boolean isParticipationRequested = isParticipationRequested(group, member);
        return groupMapper.toGroupBasicInfoResponse(group, isParticipated,
            isParticipationRequested);
    }

    private boolean isParticipationRequested(final Group group, final Member member) {
        return groupApplicationRepository.existsByGroupAndParticipant(group, member);
    }
}
