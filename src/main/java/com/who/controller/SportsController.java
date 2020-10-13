package com.who.controller;

import com.who.MD5Generator;
import com.who.domain.entity.FileEntity;
import com.who.domain.entity.SportsEntity;
import com.who.dto.FileDto;
import com.who.dto.SportsDto;
import com.who.service.FileService;
import com.who.service.SportsService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@AllArgsConstructor
public class SportsController {
    private SportsService sportsService;
    private FileService fileService;

    @GetMapping("/admin/sports")
    public String list(Model model) {
        List<SportsDto> sportsDtoList = sportsService.getSportsList();

        model.addAttribute("sportsList", sportsDtoList);
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
            String fileName = new MD5Generator(originFileName.substring(0, idx)).toString();
            // 실행되는 위치의 'files' 폴더에 파일 저장
            String savePath = System.getProperty("user.dir") + "\\files";
            // 파일이 저장되는 폴더가 없으면 폴더 생성
            if(!new File(savePath).exists()) {
                try {
                    new File(savePath).mkdir();
                }
                catch (Exception e) {
                    e.getStackTrace();
                }
            }
            String filePath = savePath + "\\" + fileName + "." + fileExtension;
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

        model.addAttribute("sportsDto", sportsDto);
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
}
