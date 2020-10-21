package com.yunseong.weclass.service;

import com.yunseong.weclass.domain.Report;
import com.yunseong.weclass.domain.ReportRepository;
import com.yunseong.weclass.domain.WeClass;
import com.yunseong.weclass.domain.WeClassRepository;
import io.eventuate.tram.events.publisher.ResultWithEvents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
public class WeClassService {

    @Autowired
    private WeClassRepository weClassRepository;
    @Autowired
    private ReportRepository reportRepository;

    public WeClass createWeClass(long projectId) {
        ResultWithEvents<WeClass> rwe = WeClass.create(projectId);
        return this.weClassRepository.save(rwe.result);
    }

    public WeClass createReport(long weClassId, String username, String subject, String content) {
        WeClass weClass = this.weClassRepository.findById(weClassId).orElseThrow(() -> new EntityNotFoundException("해당 WeClass는 존재하지 않습니다."));
        Report report = new Report(username, subject, content);
        report.equals(weClass);
        return weClass;
    }

    public void update(long id, String writer, String subject, String content) throws DifferentOwnerException {
        Report report = getReport(id);
        if(!report.getWriter().equals(writer)) {
            throw new DifferentOwnerException("소유자가 다릅니다.");
        }
        report.changeSubejct(subject);
        report.changeContent(content);
    }

    public void delete(long id) {
        Report report = getReport(id);
        report.delete();
    }

    private Report getReport(long id) {
        return this.reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 Report는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Report findReport(long  id) {
        return this.reportRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 Report는 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Page<Report> findReports(long weClassId, Pageable pageable) {
        return this.reportRepository.findAllReportByWeClassId(weClassId, pageable);
    }

    @Transactional(readOnly = true)
    public WeClass findById(long weClassId) {
        return this.weClassRepository.findById(weClassId).orElseThrow(() -> new EntityNotFoundException("해당 WeClass는 존재하지 않습니다."));
    }
}
