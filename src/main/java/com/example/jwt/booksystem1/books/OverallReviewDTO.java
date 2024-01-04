package com.example.jwt.booksystem1.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OverallReviewDTO {
    private boolean reviewauthority;
    private double overallratings;
    private List<ReviewDTO> reviews;
}
