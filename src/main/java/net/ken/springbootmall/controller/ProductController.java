package net.ken.springbootmall.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import net.ken.springbootmall.constant.ProductCategory;
import net.ken.springbootmall.dto.ProductQueryParams;
import net.ken.springbootmall.dto.ProductRequest;
import net.ken.springbootmall.model.Product;
import net.ken.springbootmall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping("products")
    public ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        Integer productId = productService.createProduct(productRequest);

        Product product = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("products/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,
                                                 @RequestBody @Valid ProductRequest productRequest) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        productService.updateProduct(productId, productRequest);

        Product updateProduct = productService.getProductById(productId);

        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

    @DeleteMapping("products/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("products")
    public ResponseEntity<List<Product>> getProducts(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,

            @RequestParam(defaultValue = "created_date") String orderBy,
            @RequestParam(defaultValue = "desc") String sort,

            @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,
            @RequestParam(defaultValue = "0") @Min(0) Integer offset
    ) {
        ProductQueryParams params = new ProductQueryParams();
        params.setCategory(category);
        params.setSearch(search);

        params.setOrderBy(orderBy);
        params.setSort(sort);

        params.setLimit(limit);
        params.setOffset(offset);

        List<Product> products = productService.getProducts(params);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
