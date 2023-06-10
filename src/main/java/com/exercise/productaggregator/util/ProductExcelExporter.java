package com.exercise.productaggregator.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.exercise.productaggregator.form.Product;

public class ProductExcelExporter {
	
	private XSSFWorkbook workbook;
	
    private XSSFSheet sheet;
    
    private List<Product> ProductList;
    
    public ProductExcelExporter(List<Product> ProductList) {
    	this.ProductList = ProductList;
    	workbook = new XSSFWorkbook();
    }
    
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Product");
         
        Row row = sheet.createRow(0);
        
        row.createCell(0).setCellValue("Product ID");
        row.createCell(1).setCellValue("Product Brand");
        row.createCell(2).setCellValue("Product Line");
        row.createCell(3).setCellValue("Product Generic");
        row.createCell(4).setCellValue("Difference Between Line and Generic");
        row.createCell(5).setCellValue("Product Price");
         
    }
    
    private void writeDataLines() {
        int rowCount = 1;
                 
        for (Product product : ProductList) {
            Row row = sheet.createRow(rowCount);
            
            row.createCell(0).setCellValue(product.getProductId());
            row.createCell(1).setCellValue(product.getProductBrand());
            row.createCell(2).setCellValue(product.getProductLine());
            row.createCell(3).setCellValue(product.getProductGeneric());
            row.createCell(4).setCellValue(product.getDifference());
            row.createCell(5).setCellValue(product.getProductPrice());
            
            rowCount++;
        }
    }
    
    public byte[] export() throws IOException {        
        writeHeaderLine();
        writeDataLines();
         
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
         
        return out.toByteArray();
         
    }

}
