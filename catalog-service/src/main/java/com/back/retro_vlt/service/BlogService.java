package com.back.retro_vlt.service;

import com.back.retro_vlt.model.Blog;
import com.back.retro_vlt.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> obtenerTodos() {
        return blogRepository.findAllByOrderByFechaPublicacionDesc();
    }

    public Blog obtenerPorId(Long id) {
        return blogRepository.findById(id).orElse(null);
    }
}
