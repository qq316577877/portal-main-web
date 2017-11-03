package com.fruit.portal.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PoiExcelUtil
{

    /**
     * @param fileName 文件路径
     * @param flag     是2003还是2007 true：2003，false：2007
     * @throws Exception
     */
    public static List<List<Map<String, Object>>> read(String fileName, boolean flag) throws Exception
    {
        Workbook wb = null;
        if (flag)
        {// 2003
            File f = new File(fileName);
            FileInputStream is = new FileInputStream(f);
            POIFSFileSystem fs = new POIFSFileSystem(is);
            wb = new HSSFWorkbook(fs);
            is.close();
        }
        else
        {// 2007
            wb = new XSSFWorkbook(fileName);
        }
        return read(wb);
    }

    /**
     * @param is   输入流
     * @param flag 是2003还是2007 true：2003，false：2007
     * @throws Exception
     */
    public static List<List<Map<String, Object>>> read(InputStream is, boolean flag) throws Exception
    {
        Workbook wb = null;
        if (flag)
        {// 2003
            wb = new HSSFWorkbook(is);
        }
        else
        {// 2007
            wb = new XSSFWorkbook(is);
        }
        return read(wb);
    }

    /**
     * 具体读取Excel
     *
     * @param wb
     * @throws Exception
     */
    public static List<List<Map<String, Object>>> read(Workbook wb) throws Exception
    {
        List<List<Map<String, Object>>> dataList = new ArrayList<List<Map<String, Object>>>();
        // sheet层
        for (int k = 0; k < wb.getNumberOfSheets(); k++)
        {
            List<Map<String, Object>> sheetList = new ArrayList<Map<String, Object>>();
            Sheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();
            // 行循环
            for (int r = 0; r < rows; r++)
            {
                // 处理具体行
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                Row row = sheet.getRow(r);
                if (row != null)
                {
                    int cells = row.getPhysicalNumberOfCells();
                    // 列循环
                    for (short c = 0; c < cells; c++)
                    {
                        // 处理具体列
                        Cell cell = row.getCell(c);
                        if (cell != null)
                        {
                            String value = null;
                            switch (cell.getCellType())
                            {
                                case Cell.CELL_TYPE_FORMULA:
                                    // 公式类型
                                    value = cell.getCellFormula();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell))
                                    {
                                        // 时间类型
                                        value = "" + cell.getDateCellValue();
                                    }
                                    else
                                    {
                                        // 数字类型
                                        value = String.valueOf(cell.getNumericCellValue());
                                        Pattern pattern = Pattern.compile("E");
                                        Matcher matcher = pattern.matcher(value);
                                        if (matcher.find())
                                        {
                                            int mulriple = Integer.parseInt(value.substring(matcher.end(), value.length()));
                                            value = value.substring(0, matcher.start());
                                            String[] values = value.split("\\.");
                                            if (values != null && values.length > 1)
                                            {
                                                String benginStr = values[0];
                                                String middStr = values[1].substring(0, mulriple);
                                                String endStr = values[1].substring(mulriple);
                                                String pointStr = "";
                                                if (endStr != null && !"".equals(endStr.trim()))
                                                {
                                                    pointStr = ".";
                                                }
                                                value = benginStr + middStr + pointStr + endStr;
                                            }
                                        }
                                        else
                                        {
                                            value = "" + cell.getNumericCellValue();
                                        }
                                    }
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    // 字符类型
                                    value = cell.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    // BOOLEAN类型
                                    value = "" + cell.getBooleanCellValue();
                                    cell.getDateCellValue();
                                    break;
                                default:
                            }
                            map.put("data" + (r + 1) + (c + 1), value);
                            sheetList.add(map);
                        }
                    }
                }
            }
            dataList.add(sheetList);
        }
        return dataList;
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        String filePath = "e:/work/excel导入/building.xlsx";
        File f = new File(filePath);
        String suffix = filePath.substring(filePath.lastIndexOf("."));
        FileInputStream is = new FileInputStream(f);
        if (suffix.contains(".xlsx"))
        {
            // 2003
            read(is, false);
        }
        else
        {
            // 2007以上
            read(is, true);
        }
    }

    /**
     * 加载excel文件，并返回文件对象
     *
     * @param file 文件整体路径
     */
    public static Workbook loadWorkbook(File file)
    {
        String path = file.getAbsolutePath();
        Workbook book = null;
        FileInputStream is = null;
        try
        {
            is = new FileInputStream(file);
            if (StringUtils.endsWithIgnoreCase(path, "xlsx"))
            {
                book = new XSSFWorkbook(is);
            }
            else
            {
                book = new HSSFWorkbook(is);
            }
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("文件格式错误!");
        }
        finally
        {
            IOUtils.closeQuietly(is);
        }
        return book;
    }

    /**
     * 加载excel文件，并返回文件对象
     *
     * @param input
     * @param fileExtension
     * @return
     */
    public static Workbook loadWorkbook(InputStream input, String fileExtension) throws Exception
    {
        Workbook book = null;
        try
        {
            if (StringUtils.equalsIgnoreCase(fileExtension, "xlsx"))
            {
                book = new XSSFWorkbook(input);
            }
            else
            {
                book = new HSSFWorkbook(input);
            }
        }
        finally
        {
            IOUtils.closeQuietly(input);
        }
        return book;
    }

    /**
     * 返回指定Cell的字符串形式
     */
    public static String getCellAsString(Row row, int cellnum)
    {
        Cell cell = row.getCell(cellnum);
        if (null == cell)
        {
            return "";
        }
        cell.setCellType(HSSFCell.CELL_TYPE_STRING);
        String stringCellValue = cell.getStringCellValue();

        return BizUtils.trimString(stringCellValue);
    }

}
