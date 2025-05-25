package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.product.*;
import az.edu.itbrains.pharmancy.models.Category;
import az.edu.itbrains.pharmancy.models.Product;
import az.edu.itbrains.pharmancy.repositories.ProductRepository;
import az.edu.itbrains.pharmancy.services.ProductService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Override
    public void createProduct(ProductCreateDto productCreateDto, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Product image is required");
        }

        try {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String photoUrl = (String) uploadResult.get("url");

            Product product = new Product();
            // FIX: Set category using ID (no need for full Category object)
            Category category = new Category();
            category.setId(productCreateDto.getCategoryId());
            product.setCategory(category);  // Set the category reference

            // Set other fields
            product.setName(productCreateDto.getName());
            product.setDescription(productCreateDto.getDescription());
            product.setSpecification(productCreateDto.getSpecification());
            product.setPrice((long) productCreateDto.getPrice());
            product.setPriceDiscount((long) productCreateDto.getPriceDiscount());
            product.setFeatured(productCreateDto.isFeatured());
            product.setRequiresReceipt(productCreateDto.isRequiresReceipt()); // ADD THIS LINE
            product.setPhotoUrl(photoUrl);
            product.setCreateData(new Date());

            productRepository.save(product);
        } catch (IOException e) {
            throw new RuntimeException("Image upload failed", e);
        }
    }

    @Override
    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> findProducts = productRepository.findAllProducts();
        List<ProductDto> products = findProducts.stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());

              return products;
    }

    @Override
    public List<ProductHomeFeaturedDto> getHomeFeaturedProducts() {
        List<Product> findProducts = productRepository.findByFeaturedTrue();
        List<ProductHomeFeaturedDto> featuredproducts = findProducts.stream().map(product -> modelMapper.map(product, ProductHomeFeaturedDto.class)).collect(Collectors.toList());

        return featuredproducts;
    }

    @Override
    public ProductDetailDto getDetailProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        ProductDetailDto productDetailDto = modelMapper.map(product, ProductDetailDto.class);
        return productDetailDto;
    }

    @Override
    public List<ProductHomeRecentDto> getHomeRecentProducts() {

        List<Product> findProducts = productRepository.findTop8ByOrderByIdDesc();
        List<ProductHomeRecentDto> products = findProducts.stream().map(product -> modelMapper.map(product, ProductHomeRecentDto.class)).collect(Collectors.toList());

        return products;
    }

    @Override
    public List<ProductRelatedDto> getRelatedProducts(Long id) {
        Product product = productRepository.findById(id).orElseThrow();
        Long categoryId = product.getCategory().getId();
        List<Product> productList = productRepository.findTop5ByCategoryIdOrderByIdDesc(categoryId);
        List<ProductRelatedDto> productRelatedDtoList = productList.stream().map(pro -> modelMapper.map(pro, ProductRelatedDto.class)).collect(Collectors.toList());

        return productRelatedDtoList;
    }

    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Product getProductById(Long productId) {
        return null;
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow();
    }
}

