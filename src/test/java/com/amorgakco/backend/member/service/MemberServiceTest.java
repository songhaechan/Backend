package com.amorgakco.backend.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.dto.AdditionalInfoRequest;
import com.amorgakco.backend.member.repository.MemberRepository;
import com.amorgakco.backend.member.service.mapper.MemberMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.GeometryFactory;

import java.util.Optional;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final GeometryFactory geometryFactory = new GeometryFactory();
    private final MemberMapper memberMapper = new MemberMapper();
    private final MemberService memberService =
            new MemberService(memberRepository, geometryFactory, memberMapper);

    @Test
    @DisplayName("멤버의 추가정보를 입력할 수 있다.")
    void updateOrSaveAdditionalInfo() {
        // given
        final Member member = TestMemberFactory.create(1L);
        given(memberRepository.findByIdWithRoles(1L)).willReturn(Optional.of(member));
        final AdditionalInfoRequest request = TestMemberFactory.createAdditionalInfoRequest(true);
        // when
        memberService.updateAdditionalInfo(request, 1L);
        // then
        assertThat(member.getGithubUrl()).isEqualTo(request.githubUrl());
        assertThat(member.isSmsNotificationSetting()).isTrue();
        assertThat(member.getPhoneNumber()).isEqualTo(request.phoneNumber());
        assertThat(member.getLocation().getX()).isEqualTo(request.longitude());
        assertThat(member.getLocation().getY()).isEqualTo(request.latitude());
    }
}
