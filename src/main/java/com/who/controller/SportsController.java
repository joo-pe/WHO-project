package com.who.controller;

import com.who.MD5Generator;
import com.who.dto.FileDto;
import com.who.dto.SportsDto;
import com.who.service.FileService;
import com.who.service.SportsService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    //축구 관리자
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
    //야구 관리자
    @GetMapping("/admin/sports2")
    public String list2(Model model) {
        List<SportsDto> sportsDtoList = sportsService.getSportsList2();
        List<FileDto> fileDtoList = new ArrayList<>();
        for(SportsDto sportsDto: sportsDtoList) {
            FileDto fileDto = fileService.getFile(sportsDto.getFileId());
            fileDtoList.add(fileDto);
        }

        model.addAttribute("sportsList2", sportsDtoList);
        model.addAttribute("fileList2", fileDtoList);
        return "admin/sports2/sports2";
    }
    //축구 상품등록으로 가는 컨트롤러
    @GetMapping("/admin/sports/post")
    public String addSports() {
        return "admin/sports/add";
    }
    //야구 상품등록으로 가는 컨트롤러
    @GetMapping("/admin/sports2/post1")
    public String addSports2() {
        return "admin/sports2/add";
    }
    //축구 상품등록시 사진파일들을 파일DB저장 그리고 상품등록
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
    //야구 상품등록시 사진파일들을 파일DB저장 그리고 상품등록
    @PostMapping("/admin/sports2/post1")
    public String addSports2(@RequestParam("file") MultipartFile files,
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
            sportsService.saveProduct2(sportsDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/sports2";
    }
    //축구 디테일한 정보로
    @GetMapping("/admin/sports/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/sports/detail";
    }
    //야구 디테일한 정보로
    @GetMapping("/admin/sports2/post/{no}")
    public String detail2(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports2(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/sports2/detail";
    }

    //축구 수정폼으로 들어가는 컨트롤러
    @GetMapping("/admin/sports/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/sports/update";
    }
    
    //야구 수정폼으로 들어가는 컨트롤러
    @GetMapping("/admin/sports2/post/edit/{no}")
    public String edit2(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports2(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/sports2/update";
    }
    //축구 수정(수정할 내용 : 파일 id를 안가져옴)
    @PutMapping("/admin/sports/post/edit/{no}")
    public String update(SportsDto sportsDto) {
        sportsService.saveProduct(sportsDto);

        return "redirect:/admin/sports";
    }
    //야구 수정(수정할 내용 : 파일 id를 안가져옴)
    @PutMapping("/admin/sports2/post/edit/{no}")
    public String update2(SportsDto sportsDto) {
        sportsService.saveProduct2(sportsDto);

        return "redirect:/admin/sports2";
    }
    //축구 삭제
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

//    @GetMapping("/soccer/ticket")
//    public String soccerTicket(Model model) {
//        List<SportsDto> sportsDtoList = sportsService.getSportsList();
//
//        model.addAttribute("sportsList", sportsDtoList);
//        return "sports/ticket";
//    }

    @GetMapping("/soccer/post/{no}")
    public String soccerDetail(@PathVariable("no") Long no, Model model) {
        SportsDto sportsDto = sportsService.getSports(no);
        FileDto fileDto = fileService.getFile(sportsDto.getFileId());

        model.addAttribute("sportsDto", sportsDto);
        model.addAttribute("fileDto", fileDto);
        return "sports/detail";
    }
    @GetMapping("/soccer/ticket")
    public String soccerTicket(Model model) {
        List<SportsDto> sportsDtoList = sportsService.getSportsList();

        model.addAttribute("sportsList", sportsDtoList);
        return "sports/ticket";
    }

}
