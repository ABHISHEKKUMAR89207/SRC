package com.example.jwt.booksystem1.books;

import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private BookTableRepository bookTableRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private  OrderRepository orderRepository;


    // Other methods...

    @PostMapping("/addReview")
    public ResponseEntity<Review> submitReview(@RequestHeader("Auth") String tokenHeader,
            @RequestParam Long bookId,
            @RequestParam String comment,
            @RequestParam double rating
    ) {
        System.out.println("api call her==================================");
        String token = tokenHeader.replace("Bearer ", "");
        String Username=jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
         Long userid=user.getUserId();
        String nameOFUser=user.getUserProfile().getFirstName()+user.getUserProfile().getLastName();
        // Check if the book exists
        BookTable book = bookTableRepository.findById(bookId).orElse(null);
        if (book == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Order> userOrders = orderRepository.findByUserUserIdAndBookId(userid, bookId);

        if (userOrders.isEmpty() || userOrders.stream().noneMatch(order -> "delivered".equalsIgnoreCase(order.getDeliveryStatus()))) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

// Rest of your code...


//String date="fd";
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

// Format the date in the desired pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy h:mm a");
        String formattedDate = currentDateTime.format(formatter);

// Print or use the formatted date
        System.out.println("Formatted Date: " + formattedDate);

        // Implement a method to get the current user (e.g., from authentication)
        Review review = new Review(book, user,nameOFUser, comment, rating,formattedDate);

        // Save the review
        Review createdReview = reviewRepository.save(review);

        return new ResponseEntity<>( HttpStatus.CREATED);
    }

//
//    @GetMapping("/{bookId}/overall-rating")
    public double getOverallRating( Long bookId) {
        Iterable<Review> reviews = reviewRepository.findAllByBookId(bookId);

        int totalRating = 0;
        int numberOfReviews = 0;

        for (Review review : reviews) {
            totalRating += review.getRating();
            numberOfReviews++;
        }
        double rating= numberOfReviews > 0 ? (double) totalRating / numberOfReviews : 0;
        BookTable book = bookTableRepository.findById(bookId).orElse(null);
        book.setRatings(rating);
        bookTableRepository.save(book);
        // Avoid division by zero
        return rating;
    }
    // Other methods...
//    @GetMapping("/by-book/{bookId}")
//    public ResponseEntity<List<ReviewDTO>> getReviewsByBookId(@PathVariable Long bookId) {
//        List<Review> reviews = (List<Review>) reviewRepository.findAllByBookId(bookId);
//
//        if (reviews.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        List<ReviewDTO> allReviews;
//      for(Review r:reviews)
//      {
//
//      }
//
//        return new ResponseEntity<>(reviews, HttpStatus.OK);
//    }
    @GetMapping("/by-book/{bookId}")
    public ResponseEntity<OverallReviewDTO> getReviewsByBookId(@RequestHeader("Auth") String tokenHeader,@PathVariable Long bookId) {
        String token = tokenHeader.replace("Bearer ", "");
        String Username=jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
        Long userid=user.getUserId();
        List<Review> reviews = (List<Review>) reviewRepository.findAllByBookId(bookId);

        if (!reviews.isEmpty()) {
            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            double overallRating = getOverallRating(bookId);
//            boolean reviewauth=false;
            List<Order> userOrders = orderRepository.findByUserUserIdAndBookId(userid, bookId);

            boolean reviewauth = userOrders.stream()
                    .anyMatch(order -> "delivered".equalsIgnoreCase(order.getDeliveryStatus()));

            OverallReviewDTO reviewsWithRating=new OverallReviewDTO(reviewauth,overallRating,reviewDTOs) ;
            return new ResponseEntity<>(reviewsWithRating, HttpStatus.OK);
        }

        // Convert entities to DTOs
        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        double overallRating = getOverallRating(bookId);
//        boolean reviewauth=false;
        List<Order> userOrders = orderRepository.findByUserUserIdAndBookId(userid, bookId);

        boolean reviewauth = userOrders.stream()
                .anyMatch(order -> "delivered".equalsIgnoreCase(order.getDeliveryStatus()));

        OverallReviewDTO reviewsWithRating=new OverallReviewDTO(reviewauth,overallRating,reviewDTOs) ;
        return new ResponseEntity<>(reviewsWithRating, HttpStatus.OK);
    }

    // Helper method to convert Review entity to ReviewDTO
    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
//                review.getId(),
//                review.getBook().getId(),
//                review.getUser().getId(),
                review.getUsername(),
                review.getComment(),
                review.getRating(),
                review.getDate()
        );
    }

}
