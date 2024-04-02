package com.example.libraryserver.controllers;

import com.example.libraryserver.requests.books.CreateBookRequest;
import com.example.libraryserver.requests.books.UpdateBookRequest;
import com.example.libraryserver.responses.books.GetBookResponse;
import com.example.libraryserver.responses.books.GetBooksResponse;
import com.example.libraryserver.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/create")
    public String createBook(@RequestBody CreateBookRequest createBookRequest){
        return bookService.createBook(createBookRequest);
    }

    @GetMapping("/get")
    public GetBooksResponse getBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/get/{id}")
    public GetBookResponse getBookById(@PathVariable("id") Long id){
        return bookService.getBookById(id);
    }

    @PatchMapping("/update")
    public String updateBook(@RequestBody UpdateBookRequest changeBookRequest){
        return bookService.updateBook(changeBookRequest);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id){
        return bookService.deleteBook(id);
    }


}
