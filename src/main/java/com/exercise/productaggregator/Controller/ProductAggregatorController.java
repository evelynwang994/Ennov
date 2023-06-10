package com.exercise.productaggregator.Controller;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.exercise.productaggregator.form.FilterForm;
import com.exercise.productaggregator.form.Product;
import com.exercise.productaggregator.util.FileUploadUtil;
import com.exercise.productaggregator.util.ProductExcelExporter;

@Controller
public class ProductAggregatorController {

	@GetMapping("/index")
	public String pageAggregator() {
		return "upload";
	}
	
	@PostMapping("/upload")
	public Object upload(@RequestPart MultipartFile fileUpload, ModelMap model) {
		try {
			saveUploadedFiles(fileUpload);
			
			List<String> ltPL = getProductLines(); // A list of Product Lines
			List<String> ltPB = getProductBrand(); // A list of Product Brand
			
			// Calculate Sum of All Product Prices
			double sum = getSumAllPrice();
			
			model.addAttribute("ltPL", ltPL);
			model.addAttribute("ltPB", ltPB);
			model.addAttribute("sum", Math.round(sum * 100.00) / 100.00);
		}catch(Exception e) {
			return "error";
		}
		
		return "search";
	}
	
	@PostMapping("/search")
	public Object search(FilterForm form, ModelMap model) {
		try {

			List<Product> data = readCsvFile();
			
			String productLine = form.getProductLine();
			String productBrand = form.getProductBrand();
			
			Predicate<Product> byProductLine = product -> product.getProductLine().equals(productLine);
			Predicate<Product> byProductBrand = product -> product.getProductBrand().equals(productBrand);
			
			if(!productLine.isBlank()) {
				data = data.stream().filter(byProductLine).collect(Collectors.toList());
			}
			
			if(!productBrand.isBlank()) {
				data = data.stream().filter(byProductBrand).collect(Collectors.toList());
			}
			
			// Calculate sum of filtered product prices
			double filteredSum = getSumFilteredPrice(data);
			
			List<Product> result = data;
			
			List<String> ltPL = getProductLines(); // A list of Product Lines
			List<String> ltPB = getProductBrand(); // A list of Product Brand
			
			model.addAttribute("ltPL", ltPL);
			model.addAttribute("ltPB", ltPB);
			model.addAttribute("sum", form.getSum());
			
			model.addAttribute("productLine", productLine);
			model.addAttribute("productBrand", productBrand);
			model.addAttribute("filteredSum", filteredSum);
			model.addAttribute("result", result);

		} catch (Exception e) {
			return "error";
		}
		
		return "search";
		
	}
	
	@GetMapping("/download")
	public HttpEntity<ByteArrayResource> exportToExcel() throws Exception {

		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        
        List<Product> data = readCsvFile();
        List<Product> excelData = getDiffLineGeneric(data);

        ProductExcelExporter excelExporter = new ProductExcelExporter(excelData);
         
        byte[] excelContent = excelExporter.export();
        
        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=product_" + currentDateTime + ".xlsx");        
        
        return new HttpEntity<>(new ByteArrayResource(excelContent), header);
    }  
	
	private void saveUploadedFiles(MultipartFile uploadfile) throws Exception {
		String uploadDir = "files";	
        FileUploadUtil.saveFile(uploadDir, "product.csv", uploadfile);
	}
	
	private List<String> getProductLines() throws Exception{
		List<String> ltPL = new ArrayList<>();
		List<Product> ltProduct = readCsvFile();
		
		for(Product product : ltProduct) {
			String productLine = product.getProductLine();
			
			if(!productLine.isBlank() && productLine != null) {
				ltPL.add(productLine);
			}
		}
		
		return ltPL.stream().distinct().collect(Collectors.toList());
	}
	
	private List<String> getProductBrand() throws Exception{
		List<String> ltPB = new ArrayList<>();
		List<Product> ltProduct = readCsvFile();
		
		for(Product product : ltProduct) {
			String productBrand = product.getProductBrand();
			
			if(!productBrand.isBlank() && productBrand != null) {
				ltPB.add(productBrand);
			}
		}
		
		return ltPB.stream().distinct().collect(Collectors.toList());
	}
	
	private List<Product> readCsvFile() throws Exception {
		List<Product> ltProduct = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader("files/product.csv"));
		String line = reader.readLine();
		
		while ((line = reader.readLine()) != null) {
			String[] item = line.split(",");
			
			if(item.length != 1) {
				Product product = new Product();
				
				product.setProductId(item[0]);
				
				if(item.length == 4) {
					
					if(StringUtils.isNumeric(item[3])) { // id,,brand,generic,price
						product.setProductLine("");
						product.setProductBrand(item[1]);
						product.setProductGeneric(item[2]);
						product.setProductPrice(item[3]);
					}else { // id,line,brand,generic,
						product.setProductLine(item[1]);
						product.setProductBrand(item[2]);
						product.setProductGeneric(item[3]);
						product.setProductPrice("0.0");
					}
					
				}else {
					product.setProductLine(item[1]);
					product.setProductBrand(item[2]);
					product.setProductGeneric(item[3]);
					product.setProductPrice(item[4]);
				}
				
				
				ltProduct.add(product);
			}
			
		}
		
		reader.close();
		
		return ltProduct;
	}
	
	private List<Product> getDiffLineGeneric(List<Product> data) {
		List<Product> result = new ArrayList<>();
		
		for(Product product : data) {
			String productLine = product.getProductLine();
			String productGeneric = product.getProductGeneric();
			
			// Look for Difference
			String difference = "";
			int lineLen = productLine.length();
			int genericLen = productGeneric.length();
			
			if(lineLen > genericLen) {
				difference = productLine.replaceAll(productGeneric, "");
			}else {
				difference = productGeneric.replaceAll(productLine, "");
			}
			
			if(difference.isEmpty()) {
				difference = "No Difference";
			}
			
			product.setDifference(difference);
			result.add(product);
		}
		
		return result;
	}
	
	private double getSumAllPrice() throws Exception {
		List<Product> data = readCsvFile();
		double sum = calculateSum(data);
		
		return sum;
	}
	
	private double getSumFilteredPrice(List<Product> data) {
		double sum = calculateSum(data);
		
		return sum;
	}
	
	private double calculateSum(List<Product> data) {
		double sum = 0;
		
		for(Product product : data) {
			sum = sum + Double.valueOf(product.getProductPrice());
		}
		
		return Math.round(sum * 100.00) / 100.00;
	}

}
