package com.yunseong.board.api.command;

import com.yunseong.board.api.BoardCategory;
import io.eventuate.tram.commands.common.Command;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CreateBoardCommand implements Command {

    private long projectId;
    private String writer;
    private String subject;
    private String content;
    private BoardCategory boardCategory;
    private MultipartFile[] images;
}
