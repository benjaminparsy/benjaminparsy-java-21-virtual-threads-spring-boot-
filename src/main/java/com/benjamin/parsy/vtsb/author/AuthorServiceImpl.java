package com.benjamin.parsy.vtsb.author;

import com.benjamin.parsy.vtsb.shared.service.impl.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class AuthorServiceImpl extends GenericServiceImpl<Author> implements AuthorService {

    protected AuthorServiceImpl(AuthorRepository repository) {
        super(repository);
    }

}
