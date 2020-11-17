package com.who.controller;

import com.who.MD5Generator;
import com.who.dto.FileDto;
import com.who.dto.PerformanceDto;
import com.who.dto.SportsDto;
import com.who.service.FileService;
import com.who.service.PerformanceService;
import com.who.service.SportsService;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Controller
@AllArgsConstructor
public class PerformanceController {
    private PerformanceService performanceService;
    private FileService fileService;
    private SportsService sportsService;

    @GetMapping("/admin/performance")
    public String list(Model model) {
        List<PerformanceDto> performanceDtoList = performanceService.getPerformaneList();
            List<FileDto> fileDtoList = new ArrayList<>();
            for(PerformanceDto performanceDto: performanceDtoList) {
                FileDto fileDto = fileService.getFile(performanceDto.getFileId());
                fileDtoList.add(fileDto);
            }

            model.addAttribute("fileList", fileDtoList);
        model.addAttribute("performanceList", performanceDtoList);
        return "admin/performance/performance";
    }

    @GetMapping("/admin/performance/post")
    public String addPerformance() {
        return "admin/performance/add";
    }

    @PostMapping("/admin/performance/post")
    public String addPerformance(@RequestParam("file")MultipartFile files,
                                 PerformanceDto performanceDto) {
        try {
            String originFileName = files.getOriginalFilename();
            int idx = originFileName.indexOf(".");
            String fileExtension = originFileName.substring(idx + 1);
            String fileName = new MD5Generator(originFileName.substring(0, idx)).toString() + "." + fileExtension;
            // 실행되는 위치의 'files' 폴더에 파일 저장
            String savePath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\images";
            // 파일이 저장되는 폴더가 없으면 폴더 생성
            if (!new File(savePath).exists()) {
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
            performanceDto.setFileId(fileId);
            performanceService.saveProduct(performanceDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/admin/performance";
    }

    @GetMapping("/admin/performance/post/{no}")
    public String detail(@PathVariable("no") Long no, Model model) {
        PerformanceDto performanceDto = performanceService.getPerformance(no);
        FileDto fileDto = fileService.getFile(performanceDto.getFileId());

        model.addAttribute("performanceDto", performanceDto);
        model.addAttribute("file", fileDto);
        return "admin/performance/detail";
    }

    @GetMapping("/admin/performance/post/edit/{no}")
    public String edit(@PathVariable("no") Long no, Model model) {
        PerformanceDto performanceDto = performanceService.getPerformance(no);
        FileDto fileDto = fileService.getFile(performanceDto.getFileId());

        model.addAttribute("performanceDto", performanceDto);
        model.addAttribute("fileDto", fileDto);
        return "admin/performance/update";
    }

    @PutMapping("/admin/performance/post/edit/{no}")
    public String update(PerformanceDto performanceDto) {
        performanceService.saveProduct(performanceDto);

        return "redirect:/admin/performance";
    }

    @DeleteMapping("/admin/performance/post/{no}")
    public String delete(@PathVariable("no") Long no) {
        performanceService.deletePerformance(no);

        return "redirect:/admin/performance";
    }

    @GetMapping("/admin/performance/search")
    public String search(@RequestParam(value = "keyword") String keyword, Model model) {
        List<PerformanceDto> performanceDtoList = performanceService.searchPerformance(keyword);

        model.addAttribute("performanceList", performanceDtoList);
        return "admin/performance/performance";
    }
    
    @GetMapping("/musical")
    public String dispmusical() {
    	return "peformance/musical";
    }
    @GetMapping("/musical/ticket")
    public String musicalTicket(Model model) {
    	List<PerformanceDto> performanceDtoList = performanceService.getPerformaneList();

        model.addAttribute("performanceList", performanceDtoList);
        return "peformance/ticket";
    }
    @GetMapping("/musical/post/{no}")
    public String soccerDetail(@PathVariable("no") Long no, Model model) {
    	PerformanceDto performanceDto = performanceService.getPerformance(no);
        FileDto fileDto = fileService.getFile(performanceDto.getFileId());

        model.addAttribute("performanceDto", performanceDto);
        model.addAttribute("fileDto", fileDto);
        return "peformance/detail";
    }
    @GetMapping("/concert")
    public String concert() {
    	return "peformance/concert";
    }
}
