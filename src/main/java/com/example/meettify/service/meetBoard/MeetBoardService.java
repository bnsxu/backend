package com.example.meettify.service.meetBoard;

import com.example.meettify.dto.meetBoard.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MeetBoardService {

    ResponseMeetBoardDetailsDTO getDetails(Long meetBoardId);

    ResponseMeetBoardDTO postBoard(MeetBoardServiceDTO meetBoardServiceDTO, String email) throws Exception;

    String deleteBoard(Long meetId, Long meetBoardId, String username) throws Exception;

    ResponseMeetBoardDTO updateBoardService(UpdateMeetBoardServiceDTO updateMeetBoardServiceDTO, String username) throws Exception;

    Page<MeetBoardSummaryDTO> getPagedList(Long meetId, Pageable pageable);
}
