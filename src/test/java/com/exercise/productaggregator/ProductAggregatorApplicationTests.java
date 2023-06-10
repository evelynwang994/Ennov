package com.exercise.productaggregator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.exercise.productaggregator.form.FilterForm;
import com.exercise.productaggregator.form.Product;

@SpringBootTest
class ProductAggregatorApplicationTests {
	
	private static FilterForm formTest1;
	
	private static FilterForm formTest2;
	
	private static FilterForm formTest3;
	
	private static FilterForm formTest4;
	
	private static FilterForm formTest5;
	
	@BeforeAll
	static void setup() {
		// Test Case 1
		formTest1 = new FilterForm();
		formTest1.setProductLine("");
		formTest1.setProductBrand("");
		
		// Test Case 2
		formTest2 = new FilterForm();
		formTest2.setProductLine("Theta Powder");
		formTest2.setProductBrand("Theta Powder Super");
		
		// Test Case 3
		formTest3 = new FilterForm();
		formTest3.setProductLine("Theta Powder");
		formTest3.setProductBrand("");
		
		// Test Case 4
		formTest4 = new FilterForm();
		formTest4.setProductLine("");
		formTest4.setProductBrand("Theta Powder Super");
		
		// Test Case 5
		formTest5 = new FilterForm();
		formTest5.setProductLine("Venus Cream");
		formTest5.setProductBrand("Rho Powder 100100");
	}
	

	@Test
	void searchTest() throws Exception {
		search(formTest1);
		search(formTest2);
		search(formTest3);
		search(formTest4);
		search(formTest5);
	}
	
	private void search(FilterForm form) throws Exception {
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
		getSumFilteredPrice(data);
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
