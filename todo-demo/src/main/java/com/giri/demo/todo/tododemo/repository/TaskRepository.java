package com.giri.demo.todo.tododemo.repository;

import java.util.List;

import com.giri.demo.todo.tododemo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    public Task getById(long id);

    public List<Task> findAllByOwnerid(long id);

}