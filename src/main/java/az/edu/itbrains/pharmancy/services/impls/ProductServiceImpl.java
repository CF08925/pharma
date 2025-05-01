package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.product.ProductDetailDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductHomeFeaturedDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductHomeRecentDto;
import az.edu.itbrains.pharmancy.dtos.product.ProductRelatedDto;
import az.edu.itbrains.pharmancy.models.Product;
import az.edu.itbrains.pharmancy.repositories.ProductRepository;
import az.edu.itbrains.pharmancy.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public void delete(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }
    }

    @Override
    public List<ProductHomeFeaturedDto> getHomeFeaturedProducts() {
        List<Product> findProducts = productRepository.findByFeaturedTrue();
        List<ProductHomeFeaturedDto> products = findProducts.stream().map(product -> modelMapper.map(product, ProductHomeFeaturedDto.class)).collect(Collectors.toList());

        return products;
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
}

