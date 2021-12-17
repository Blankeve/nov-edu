package com.novedu.nov.common.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author ：juam
 * @date ：2021/12/15 11:21
 * @description： Excel导入导出工具类
 * @modified By：
 * @version:
 */
public class ExcelUtils {

    public static void defaultExport(String title, String sheetName, Class clazz, List list, HttpServletResponse response) throws IOException {
        //文件名是中文则需要设置编码，否则传给客户端会乱码
        String contentDisposition = String.format("attachment;filename=%s.xls", URLEncoder.encode(title, "UTF-8"));
        response.setHeader("content-disposition", contentDisposition);
        //告知客户端这是Excel文件
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), clazz, list);
        workbook.write(response.getOutputStream());
    }
}
