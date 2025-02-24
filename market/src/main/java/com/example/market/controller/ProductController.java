package com.example.market.controller;

import com.example.market.dto.*;
import com.example.market.i18n.I18nUtil;
import com.example.market.service.ProductService;
import com.example.market.util.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Tag(name = "Products", description = "Endpoints for managing products (find, find by id, find by filter, save, delete, edit)")
public class ProductController {

    ProductService productService;
    I18nUtil i18nUtil;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Save a new product with images", description = "Creates and saves a new product in the system")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was saved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error during saving product",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
            )
    })
    public ResponseEntity<ResponseDto> saveProduct(
            @RequestPart(value = "product") @Valid CreateProductDto createProductDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> files) {
        productService.save(createProductDto, files);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.PRODUCT_SUCCESS_SAVED)));
    }

    @GetMapping
    @Operation(summary = "Find all products", description = "Retrieves all products with pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Find all products (pagination included)"
    )
    public ResponseEntity<Page<ProductDto>> findAllProducts(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 20;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        Page<ProductDto> products = productService.findAll(PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok(products);
    }

    @PutMapping
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update a product", description = "Updates an existing product in the system")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Error during updating product",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was updated successfully"
            )
    })
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto updatedProductDto = productService.update(productDto);
        return ResponseEntity.ok(updatedProductDto);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            )
    })
    public ResponseEntity<ResponseDto> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(new ResponseDto(i18nUtil.getMessage(Messages.PRODUCT_SUCCESS_DELETED, String.valueOf(id))));
    }

    @GetMapping("/filter")
    @Operation(summary = "Find products by filters", description = "Retrieves products based on filter criteria with pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Find all products by filters (pagination included)"
    )
    public ResponseEntity<Page<ProductDto>> filterProducts(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestBody ProductFilterDTO productFilterDTO) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 20;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        Page<ProductDto> products = productService
                .findAllByFilter(productFilterDTO, PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by title", description = "Finds products by their title with pagination")
    @ApiResponse(
            responseCode = "200",
            description = "Find all products by title (pagination included)"
    )
    public ResponseEntity<Page<ProductDto>> searchProducts(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "title", required = false) String title) {
        if (offset == null) offset = 0;
        if (pageSize == null) pageSize = 20;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "id";
        Page<ProductDto> products = productService
                .findAllByTitle(title, PageRequest.of(offset, pageSize, Sort.by(sortBy)));
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find product by ID", description = "Retrieves a product by its unique ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AppErrorDto.class))
            ),
            @ApiResponse(
                    responseCode = "200",
                    description = "Product was founded successfully"
            )
    })
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        ProductDto productDto = productService.findById(id);
        return ResponseEntity.ok(productDto);
    }
}

