package com.amorgakco.backend.groupparticipation.controller;

import com.amorgakco.backend.global.argumentresolver.AuthMember;
import com.amorgakco.backend.groupparticipation.service.GroupParticipationService;
import com.amorgakco.backend.member.domain.Member;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class GroupParticipationController {

    private final GroupParticipationService groupParticipationService;

    @PostMapping("/{groupId}/participation")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void participate(@PathVariable final Long groupId, @AuthMember final Member member) {
        groupParticipationService.participate(groupId, member);
    }
}
