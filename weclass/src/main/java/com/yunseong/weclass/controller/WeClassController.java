package com.yunseong.weclass.controller;

import com.yunseong.weclass.domain.Report;
import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.service.WeClassService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/class", consumes = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class WeClassController {

    private final WeClassService weClassService;

    @PostMapping("/{id}/report")
    public ResponseEntity<Long> createReport(@PathVariable long id, CreateReportRequest reportRequest) {
        WeClass report = this.weClassService.createReport(id, reportRequest.getWriter(), reportRequest.getSubject(), reportRequest.getContent());
        return new ResponseEntity<>(report.getId(), HttpStatus.CREATED);
    }

    @GetMapping("/{id}/notice")
    public ResponseEntity<WeClassNoticeResponse> findNotice(@PathVariable long id) {
        WeClass weClass = this.weClassService.findById(id);
        return ResponseEntity.ok(new WeClassNoticeResponse(weClass.getNotice()));
    }

    @GetMapping("/{id}/reports")
    public ResponseEntity<PagedModel<WeClassReportResponse>> findReports(@PathVariable long id, @PageableDefault Pageable pageable) {
        Page<WeClassReportResponse> page = this.weClassService.findReports(id, pageable).map(r -> new WeClassReportResponse(r.getWriter(), r.getSubject()));
        PagedModel.PageMetadata metadata = new PagedModel.PageMetadata(page.getSize(), page.getNumber(), page.getTotalElements(), page.getTotalPages());
        return ResponseEntity.ok(PagedModel.of(page.getContent(), metadata));
    }

    @GetMapping("/reports/{id}")
    public ResponseEntity<WeClassReportDetailResponse> findReport(@PathVariable long id) {
        Report report = this.weClassService.findReport(id);
        return ResponseEntity.ok(new WeClassReportDetailResponse(report.getWriter(), report.getSubject(), report.getContent(), report.getCreatedTime()));
    }

    @PutMapping("/reports/{id}")
    public ResponseEntity<?> updateReport(@PathVariable long id, @RequestBody WeClassReportUpdateRequest request) {
        this.weClassService.update(id, request.getWriter(), request.getSubject(), request.getContent());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable long id) {
        this.weClassService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
