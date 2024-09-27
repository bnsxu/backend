package com.example.meettify.service.meet;

import com.example.meettify.dto.meet.*;
import com.example.meettify.dto.meet.category.Category;
import com.example.meettify.dto.meetBoard.MeetBoardSummaryDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/*
 *   worker : 조영흔
 *   work   : 모임 서비스 인터페이스 구현
 *   date   : 2024/09/24
 * */
public interface MeetService {

    //모임 만들기
    ResponseMeetDTO makeMeet(MeetServiceDTO meet,String email) throws IOException;
    //모임 제거
    String removeMeet(Long meetId, String email);
    boolean checkEditPermission(Long meetId,String email);
    ResponseMeetDTO update(UpdateMeetServiceDTO meetUpdateServiceDTO, List<MultipartFile> newImages);
    boolean isAlreadyMember(Long meetId, String email);
    void applyToJoinMeet(Long meetId, String email);

    MeetDetailDTO getMeetDetail(Long meetId);

    List<MeetSummaryDTO> getMeetList(Long lastId, int size, Category category);

    MeetRole getMeetRole(Long meetId, String email);

    List<MeetBoardSummaryDTO>  getMeetSummaryList(Long meetId);
}
