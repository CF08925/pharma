package az.edu.itbrains.pharmancy.services;

import az.edu.itbrains.pharmancy.dtos.product.*;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {


    void delete(Long id);

    List<ProductHomeFeaturedDto> getHomeFeaturedProducts();
    ProductDetailDto getDetailProductById(Long id);
    List<ProductHomeRecentDto>  getHomeRecentProducts();
    List<ProductRelatedDto> getRelatedProducts(Long id);
}
