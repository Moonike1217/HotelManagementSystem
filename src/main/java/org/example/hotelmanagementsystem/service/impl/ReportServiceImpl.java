package org.example.hotelmanagementsystem.service.impl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.hotelmanagementsystem.dto.BookingStatisticsDto;
import org.example.hotelmanagementsystem.dto.RevenueStatisticsDto;
import org.example.hotelmanagementsystem.dto.OccupancyRateDto;
import org.example.hotelmanagementsystem.dto.ReportQueryDto;
import org.example.hotelmanagementsystem.mapper.ReportMapper;
import org.example.hotelmanagementsystem.service.ReportService;
import org.example.hotelmanagementsystem.util.TimestampUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportMapper reportMapper;
    
    @Override
    public List<BookingStatisticsDto> getBookingStatistics(ReportQueryDto query) {
        Long startTimestamp = null;
        Long endTimestamp = null;
        if (query.getStartDate() != null && !query.getStartDate().isEmpty()) {
            startTimestamp = TimestampUtil.parseTimestamp(query.getStartDate() + " 00:00:00");
        }
        if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
            endTimestamp = TimestampUtil.parseTimestamp(query.getEndDate() + " 23:59:59");
        }
        return reportMapper.getBookingStatistics(startTimestamp, endTimestamp, query.getHotelId());
    }
    
    @Override
    public List<RevenueStatisticsDto> getRevenueStatistics(ReportQueryDto query) {
        Long startTimestamp = null;
        Long endTimestamp = null;
        if (query.getStartDate() != null && !query.getStartDate().isEmpty()) {
            startTimestamp = TimestampUtil.parseTimestamp(query.getStartDate() + " 00:00:00");
        }
        if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
            endTimestamp = TimestampUtil.parseTimestamp(query.getEndDate() + " 23:59:59");
        }
        return reportMapper.getRevenueStatistics(startTimestamp, endTimestamp, query.getHotelId());
    }
    
    @Override
    public List<OccupancyRateDto> getOccupancyRateStatistics(ReportQueryDto query) {
        return reportMapper.getOccupancyRateStatistics(query.getStartDate(), query.getEndDate(), query.getHotelId());
    }
    
    @Override
    public String exportBookingStatisticsToExcel(ReportQueryDto query) throws IOException {
        List<BookingStatisticsDto> dataList = getBookingStatistics(query);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("预订统计报表");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"酒店ID", "酒店名称", "总预订数", "确认预订数", "入住数", "取消数", "预订率(%)", "入住率(%)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        int rowNum = 1;
        for (BookingStatisticsDto data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getHotelId());
            row.createCell(1).setCellValue(data.getHotelName());
            row.createCell(2).setCellValue(data.getTotalBookings());
            row.createCell(3).setCellValue(data.getConfirmedBookings());
            row.createCell(4).setCellValue(data.getCheckInCount());
            row.createCell(5).setCellValue(data.getCancelledBookings());
            row.createCell(6).setCellValue(data.getBookingRate());
            row.createCell(7).setCellValue(data.getCheckInRate());
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 保存文件
        String filePath = createExcelFilePath("booking_statistics");
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        
        return filePath;
    }
    
    @Override
    public String exportRevenueStatisticsToExcel(ReportQueryDto query) throws IOException {
        List<RevenueStatisticsDto> dataList = getRevenueStatistics(query);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("收入统计报表");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"酒店ID", "酒店名称", "月份", "总收入", "平均房价", "订单数"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        int rowNum = 1;
        for (RevenueStatisticsDto data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getHotelId());
            row.createCell(1).setCellValue(data.getHotelName());
            row.createCell(2).setCellValue(data.getMonth());
            row.createCell(3).setCellValue(data.getTotalRevenue().doubleValue());
            row.createCell(4).setCellValue(data.getAverageRoomPrice().doubleValue());
            row.createCell(5).setCellValue(data.getOrderCount());
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 保存文件
        String filePath = createExcelFilePath("revenue_statistics");
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        
        return filePath;
    }
    
    @Override
    public String exportOccupancyRateStatisticsToExcel(ReportQueryDto query) throws IOException {
        List<OccupancyRateDto> dataList = getOccupancyRateStatistics(query);
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("入住率统计报表");
        
        // 创建表头
        Row headerRow = sheet.createRow(0);
        String[] headers = {"酒店ID", "酒店名称", "日期", "总房间数", "已占用房间数", "入住率(%)"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        int rowNum = 1;
        for (OccupancyRateDto data : dataList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(data.getHotelId());
            row.createCell(1).setCellValue(data.getHotelName());
            row.createCell(2).setCellValue(data.getDate());
            row.createCell(3).setCellValue(data.getTotalRooms());
            row.createCell(4).setCellValue(data.getOccupiedRooms());
            row.createCell(5).setCellValue(data.getOccupancyRate());
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 保存文件
        String filePath = createExcelFilePath("occupancy_rate_statistics");
        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
        
        return filePath;
    }
    
    /**
     * 创建Excel文件路径
     * @param prefix 文件名前缀
     * @return 文件路径
     */
    private String createExcelFilePath(String prefix) {
        // 创建excel目录（如果不存在）
        java.io.File excelDir = new java.io.File("src/main/resources/excel");
        if (!excelDir.exists()) {
            excelDir.mkdirs();
        }
        
        // 生成文件名
        String fileName = prefix + "_" + System.currentTimeMillis() + ".xlsx";
        return "src/main/resources/excel/" + fileName;
    }
}