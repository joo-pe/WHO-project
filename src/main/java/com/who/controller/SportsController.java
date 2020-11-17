package com.who.controller;

import com.who.MD5Generator;
import com.who.domain.MemberDetail;
import com.who.domain.entity.BookingEntity;
import com.who.domain.entity.MemberEntity;
import com.who.domain.entity.SportsEntity;
import com.who.domain.repository.MemberRepository;
import com.who.dto.*;
import com.who.service.*;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class SportsController {
    private SportsService sportsService;
    private FileService fileService;
    private SeatService seatService;
    private MemberRepository memberRepository;
    private MemberService memberService;
    private BookingService bookingService;

    @GetMapping("/admin/sports")
    public String list(Model model) {
        List<SportsDto> sportsDtoList = sportsService.getSportsList();
        List<FileDto> fileDtoList = new ArrayList<>();
        for(SportsDto sportsDto: sportsDtoList) {
            FileDto fileDto = fileService.getFile(sportsDto.getFileId());
            fileDtoList.add(fileDto);
        }

        model.addAttribute("sportsList", sportsDtoList);
        model.addAttribute("fileList", fileDtoList);
        return "admin/sports/sports";
    }

    @GetMapping("/admin/sports/post")
    public String addSports() {
        return "admin/sports/add";
    }

    @PostMapping("/admin/sports/post")
    public String addSports(@RequestParam("file") MultipartFile files,
                            SportsDto sportsDto) {
        try {
            String originFileName = files.getOriginalFilename();
            int idx = originFileName.indexOf(".");
            String fileExtension = originFileName.substring(idx+1);
            String fileName = new MD5Generator(originFileName.substring(0, idx)).toString() + "." + fileExtension;
            // 실행되는 위치의 'files' 폴더에 파일 저장
            String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
            // 파일이 저장되는 폴더가 없으면 폴더 생성
            if(!new File(savePath).exists()) {
                try {
                    new File(savePath).mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + fileName;
            files.transferTo(new File(filePath));

            FileDto fileDto = new FileDto();
            fileDto.setOriginFileName(originFileName);
            fileDto.setFileName(fileName);
            fileDto.setFilePath(filePath);

            Long fileId = fileService.saveFile(fileDto);
            sportsDto.setFileId(fileId);
            sportsService.saveProduct(sportsDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/sports";
    }

    @GetMapping("/admin/sports/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/sports/detail";
    }


    @GetMapping("/admin/sports/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/sports/update";
    }

    @PutMapping("/admin/sports/post/edit/{no}")
    public String update(SportsDto sportsDto) {
        sportsService.saveProduct(sportsDto);

        return "redirect:/admin/sports";
    }

    @DeleteMapping("/admin/sports/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        sportsService.deleteSports(no);

        return "redirect:/admin/sports";
    }

    @GetMapping("/admin/sports/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<SportsDto> sportsDtoList = sportsService.searchSports(keyword);

        model.addAttribute("sportsList", sportsDtoList);
        return "admin/sports/sports";
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException {
        FileDto fileDto = fileService.getFile(fileId);
        Path path = Paths.get(fileDto.getFilePath());
        Resource resource = new InputStreamResource(Files.newInputStream(path));
        String filename =fileDto.getOriginFileName();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName=\""+fileDto.getOriginFileName() + "\"")
                .body(resource);
    }

    //----------------------------------일반------------------------------------------
    @GetMapping("/soccer")
    public String soccerList(Model model) {
        return "sports/soccer";
    }
    //야구 예매 페이지
    @GetMapping("/baseball")
    public String dispbaseball() {
    	return "sports/baseball";
    }

    @GetMapping("/soccer/ticket")
    public String soccerTicket(Model model) {
        List<SportsDto> sportsDtoList = sportsService.getSportsList();

        model.addAttribute("sportsList", sportsDtoList);
        return "sports/ticket";
    }

    @GetMapping("/soccer/ticket/{no}")
    public String soccerDetail(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());
        List<Object[]> availableSeats = seatService.countAvailableSeat();

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        model.addAttribute("availableSeats", availableSeats);
        return "sports/detail";
    }

    @PostMapping("/soccer/ticket/{no}")
    public String saveBooking(@PathVariable("no") Long no, @AuthenticationPrincipal MemberDetail memberDetail, Model model) {
//        String email = memberDetail.getUsername();
//        MemberEntity memberEntity = memberRepository.findMemberEntityByEmail(email);
////        MemberDto memberDto = memberService.convertEntityToDto(memberEntity);
//        MemberDto memberDto = memberService.getMember(memberEntity.getId());

        SportsDto sportsDto = sportsService.getSports(no);

//        BookingDto bookingDto = new BookingDto(null, memberDto, sportsDto, false);
//        Long bookNo = bookingService.saveBooking(bookingDto);
//
//        BookingDto bookingDto2 = bookingService.getBooking(bookNo);

//        model.addAttribute("bookingDto", bookingDto);

//        return "sports/pay" + bookNo;
        return "redirect:/pay/1";
    }



}
