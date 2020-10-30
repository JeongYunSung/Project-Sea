package com.yunseong.weclass.service;

import com.yunseong.weclass.domain.Report;
import com.yunseong.weclass.domain.ReportRepository;
import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.domain.WeClassRepository;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@AllArgsConstructor
public class WeClassService {

    private final WeClassRepository weClassRepository;
    private final ReportRepository reportRepository;

    public WeClass createWeClass(long projectId) {
        ResultWithEvents<WeClass> rwe = WeClass.create(projectId);
        return this.weClassRepository.save(rwe.result);
    }

    public WeClass createReport(long weClassId, String username, String subject, String content) {
        WeClass weClass = this.weClassRepository.findOpenedById(weClassId).orElseThrow(() -> new EntityNotFoundException("해당 WeClass는 존재하지 않습니다."));
        Report report = new Report(weClass, username, subject, content);
        this.reportRepository.save(report);
        return weClass;
    }

    public void update(long id, String writer, String subject, String content) throws DifferentOwnerException {
        Report report = getReport(id);
        if(!report.getWriter().equals(writer)) {
            throw new DifferentOwnerException("소유자가 다릅니다.");
        }
        report.changeSubject(subject);
        report.changeContent(content);
    }

    public void delete(long id) {
        Report report = getReport(id);
        report.delete();
    }

    private Report getReport(long id) {
        return this.reportRepository.findOpenedById(id).orElseThrow(() -> new EntityNotFoundException("해당 Report는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Report findReport(long  id) {
        return this.reportRepository.findOpenedById(id).orElseThrow(() -> new EntityNotFoundException("해당 Report는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Page<Report> findReports(long weClassId, Pageable pageable) {
        return this.reportRepository.findAllReportByWeClassId(weClassId, pageable);
    }

    @Transactional(readOnly = true)
    public WeClass findById(long weClassId) {
        return this.weClassRepository.findOpenedById(weClassId).orElseThrow(() -> new EntityNotFoundException("해당 WeClass는 존재하지 않습니다."));
    }
}
