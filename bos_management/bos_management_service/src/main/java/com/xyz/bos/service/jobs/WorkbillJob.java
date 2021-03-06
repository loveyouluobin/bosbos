package com.xyz.bos.service.jobs;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.xyz.bos.dao.take_delivery.WorkBillRepository;
import com.xyz.bos.domain.take_delivery.WorkBill;
import com.xyz.bos.utils.MailUtils;
@Component
public class WorkbillJob {
	@Autowired
    private WorkBillRepository workBillRepository;
    public void sendMail() {
        List<WorkBill> list = workBillRepository.findAll();
        String emailBody = "编号\t快递员\t取件状态\t时间<br/>";
        for (WorkBill workBill : list) {
            emailBody += workBill.getId() + "\t"
                    + workBill.getCourier().getName() + "\t"
                    + workBill.getPickstate() + "\t"
                    + workBill.getBuildtime().toLocaleString() + "<br/>";
        }
        MailUtils.sendMail("aa@store.com", "工单信息统计", emailBody);
        System.out.println("邮件已经发送");
    }
}
