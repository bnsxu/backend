package com.example.meettify.service.meetBoard;

import com.example.meettify.dto.meetBoard.MeetBoardServiceDTO;
import com.example.meettify.dto.meetBoard.ResponseMeetBoardDetailsDTO;
import com.example.meettify.dto.meetBoard.ResponseMeetBoardDTO;

public interface MeetBoardService {

    ResponseMeetBoardDetailsDTO getDetails(Long meetBoardId);

    ResponseMeetBoardDTO postBoard(MeetBoardServiceDTO meetBoardServiceDTO, String email) throws Exception;

    String deleteBoard(Long meetId, Long meetBoardId, String username) throws Exception;
}
