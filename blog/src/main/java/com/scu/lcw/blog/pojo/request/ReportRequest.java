package com.scu.lcw.blog.pojo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import com.scu.lcw.common.page.BasePage;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author: lucw
 * @description: 该类的描述
 * @date: 2021/4/20 11:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ReportRequest extends BasePage {

    private Long reportId;

    private String reportTitle;

    private String reportContent;
}
