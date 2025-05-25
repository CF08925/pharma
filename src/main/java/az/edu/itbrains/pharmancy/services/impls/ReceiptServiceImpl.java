package az.edu.itbrains.pharmancy.services.impls;

import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptCreateDto;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptDto;
import az.edu.itbrains.pharmancy.dtos.receipt.ReceiptResponseDto;
import az.edu.itbrains.pharmancy.models.Receipt;
import az.edu.itbrains.pharmancy.models.ReceiptStatus;
import az.edu.itbrains.pharmancy.models.User;
import az.edu.itbrains.pharmancy.repositories.ReceiptRepository;
import az.edu.itbrains.pharmancy.services.ReceiptService;
import az.edu.itbrains.pharmancy.services.UserService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final Cloudinary cloudinary;

    @Override
    @Transactional
    public ReceiptResponseDto uploadReceipt(String userEmail, ReceiptCreateDto receiptCreateDto, List<MultipartFile> images) {
        try {
            // Validate input
            if (images == null || images.isEmpty()) {
                return ReceiptResponseDto.error("Ən azı bir şəkil yüklənməlidir");
            }

            if (images.size() > 4) {
                return ReceiptResponseDto.error("Maksimum 4 şəkil yükləyə bilərsiniz");
            }

            // Validate file sizes (10MB = 10 * 1024 * 1024 bytes)
            for (MultipartFile image : images) {
                if (image.getSize() > 10 * 1024 * 1024) {
                    return ReceiptResponseDto.error("Şəkil ölçüsü 10 MB-dan çox ola bilməz");
                }

                if (!isValidImageType(image)) {
                    return ReceiptResponseDto.error("Yalnız PNG və JPEG formatında şəkillər qəbul edilir");
                }
            }

            // Find user
            User user = userService.findUserByEmail(userEmail);
            if (user == null) {
                return ReceiptResponseDto.error("İstifadəçi tapılmadı");
            }

            // Upload images to Cloudinary
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile image : images) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(image.getBytes(),
                            ObjectUtils.asMap(
                                    "folder", "receipts",
                                    "resource_type", "auto"
                            ));
                    String photoUrl = (String) uploadResult.get("url");
                    imageUrls.add(photoUrl);
                } catch (IOException e) {
                    log.error("Error uploading image to Cloudinary", e);
                    return ReceiptResponseDto.error("Şəkil yükləmə zamanı xəta baş verdi");
                }
            }

            // Create receipt entity
            Receipt receipt = new Receipt();
            receipt.setFirstName(receiptCreateDto.getFirstName().trim());
            receipt.setLastName(receiptCreateDto.getLastName().trim());
            receipt.setPhoneOperator(receiptCreateDto.getPhoneOperator());
            receipt.setPhoneNumber(receiptCreateDto.getPhoneNumber());


            // Convert amount to cents if provided
//            if (receiptCreateDto.getAmount() != null && receiptCreateDto.getAmount() > 0) {
//                receipt.setAmount((long) (receiptCreateDto.getAmount() * 100));
//            }

            receipt.setNotes(receiptCreateDto.getNotes() != null ? receiptCreateDto.getNotes().trim() : null);
            receipt.setImageUrls(imageUrls);
            receipt.setUploadDate(LocalDateTime.now());
            receipt.setStatus(ReceiptStatus.PENDING);
            receipt.setUser(user);

            // Save receipt
            Receipt savedReceipt = receiptRepository.save(receipt);

            log.info("Receipt uploaded successfully: ID {}, User: {}", savedReceipt.getId(), userEmail);

            return ReceiptResponseDto.success("Çek uğurla yükləndi", savedReceipt.getId());

        } catch (Exception e) {
            log.error("Error uploading receipt for user: " + userEmail, e);
            return ReceiptResponseDto.error("Çek yükləmə zamanı xəta baş verdi: " + e.getMessage());
        }
    }

    @Override
    public List<ReceiptDto> getUserReceipts(String userEmail) {
        try {
            List<Receipt> receipts = receiptRepository.findByUserEmailOrderByUploadDateDesc(userEmail);
            return receipts.stream()
                    .map(receipt -> {
                        ReceiptDto dto = modelMapper.map(receipt, ReceiptDto.class);
                        dto.setUserEmail(receipt.getUser().getEmail());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting user receipts for: " + userEmail, e);
            return new ArrayList<>();
        }
    }

    @Override
    public ReceiptDto getReceiptById(Long receiptId) {
        try {
            Receipt receipt = receiptRepository.findById(receiptId)
                    .orElseThrow(() -> new EntityNotFoundException("Receipt not found with ID: " + receiptId));

            ReceiptDto dto = modelMapper.map(receipt, ReceiptDto.class);
            dto.setUserEmail(receipt.getUser().getEmail());
            return dto;
        } catch (Exception e) {
            log.error("Error getting receipt by ID: " + receiptId, e);
            throw new RuntimeException("Error getting receipt: " + e.getMessage());
        }
    }

    @Override
    public List<ReceiptDto> getAllReceipts() {
        try {
            List<Receipt> receipts = receiptRepository.findAllOrderByUploadDateDesc();
            return receipts.stream()
                    .map(receipt -> {
                        ReceiptDto dto = modelMapper.map(receipt, ReceiptDto.class);
                        dto.setUserEmail(receipt.getUser().getEmail());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting all receipts", e);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ReceiptDto> getReceiptsByStatus(ReceiptStatus status) {
        try {
            List<Receipt> receipts = receiptRepository.findByStatusOrderByUploadDateDesc(status);
            return receipts.stream()
                    .map(receipt -> {
                        ReceiptDto dto = modelMapper.map(receipt, ReceiptDto.class);
                        dto.setUserEmail(receipt.getUser().getEmail());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting receipts by status: " + status, e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public boolean updateReceiptStatus(Long receiptId, ReceiptStatus status) {
        try {
            Receipt receipt = receiptRepository.findById(receiptId)
                    .orElseThrow(() -> new EntityNotFoundException("Receipt not found with ID: " + receiptId));

            receipt.setStatus(status);
            receiptRepository.save(receipt);

            log.info("Receipt status updated: ID {}, Status: {}", receiptId, status);
            return true;
        } catch (Exception e) {
            log.error("Error updating receipt status: ID " + receiptId, e);
            return false;
        }
    }

    @Override
    @Transactional
    public boolean deleteReceipt(Long receiptId) {
        try {
            if (receiptRepository.existsById(receiptId)) {
                receiptRepository.deleteById(receiptId);
                log.info("Receipt deleted: ID {}", receiptId);
                return true;
            } else {
                throw new EntityNotFoundException("Receipt not found with ID: " + receiptId);
            }
        } catch (Exception e) {
            log.error("Error deleting receipt: ID " + receiptId, e);
            return false;
        }
    }

    @Override
    public Receipt findReceiptById(Long receiptId) {
        return receiptRepository.findById(receiptId)
                .orElseThrow(() -> new EntityNotFoundException("Receipt not found with ID: " + receiptId));
    }

    @Override
    public List<ReceiptDto> getRecentReceipts() {
        try {
            LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
            List<Receipt> receipts = receiptRepository.findRecentReceipts(thirtyDaysAgo);
            return receipts.stream()
                    .map(receipt -> {
                        ReceiptDto dto = modelMapper.map(receipt, ReceiptDto.class);
                        dto.setUserEmail(receipt.getUser().getEmail());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error getting recent receipts", e);
            return new ArrayList<>();
        }
    }

    @Override
    public long countReceiptsByStatus(ReceiptStatus status) {
        try {
            return receiptRepository.countByStatus(status);
        } catch (Exception e) {
            log.error("Error counting receipts by status: " + status, e);
            return 0;
        }
    }



    private boolean isValidImageType(MultipartFile file) {
        if (file.getContentType() == null) return false;

        String contentType = file.getContentType().toLowerCase();
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/png");
    }
}